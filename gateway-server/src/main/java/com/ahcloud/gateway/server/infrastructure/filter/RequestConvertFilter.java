package com.ahcloud.gateway.server.infrastructure.filter;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.server.domain.context.GatewayContext;
import com.google.common.base.Throwables;
import io.netty.buffer.ByteBufAllocator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @program: ahcloud-gateway
 * @description: 请求过滤器
 * @author: YuKai Fan
 * @create: 2023/1/16 08:54
 **/
@Slf4j
@Component
public class RequestConvertFilter implements GlobalFilter, Ordered {

    /**
     * 默认的 HttpMessageReader
     */
    private static final List<HttpMessageReader<?>> MESSAGE_READERS = HandlerStrategies.withDefaults().messageReaders();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        GatewayContext context = GatewayContext.builder()
                .path(request.getPath().pathWithinApplication().value())
                .ipAddress(String.valueOf(request.getRemoteAddress()))
                .headers(headers)
                .build();
        context.getFormData().addAll(request.getQueryParams());

        log.info("[RequestConvertFilter] HttpMethod is {}, Url is {}", request.getMethod(), request.getURI().getRawPath());

        //注意，因为webflux的响应式编程 不能再采取原先的编码方式 即应该先将gatewayContext放入exchange中，否则其他地方可能取不到
        exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, context);

        // 处理参数
        MediaType contentType = headers.getContentType();
        long contentLength = headers.getContentLength();
        if (contentLength > 0) {
            if (MediaType.APPLICATION_JSON.equals(contentType)) {
                return readBody(exchange, chain, context);
            }
            if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
                return readFormData(exchange, chain, context);
            }
        }
        log.info("[RequestConvertFilter] GatewayContext contentType is {}, Gateway context is set with {}", contentType, context);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    /**
     * 读取请求的form表单数据
     *
     * @param exchange
     * @param chain
     * @param context
     * @return
     */
    private Mono<Void> readFormData(ServerWebExchange exchange, GatewayFilterChain chain, GatewayContext context) {
        final ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        return exchange.getFormData().doOnNext(multiValueMap -> {
            // 填充FormData
            context.setFormData(multiValueMap);
            log.info("GatewayContext Read FormData is {}", multiValueMap);
        }).then(Mono.defer(() -> {
                    Charset charset = headers.getContentType().getCharset();
                    charset = charset == null ? StandardCharsets.UTF_8 : charset;
                    String charsetName = charset.name();
                    MultiValueMap<String, String> formData = context.getFormData();
                    if (CollectionUtils.isEmpty(formData)) {
                        return chain.filter(exchange);
                    }
                    StringBuilder formDataBodyBuilder = new StringBuilder();
                    String entryKey;
                    List<String> entryValue;
                    try {
                        for (Map.Entry<String, List<String>> entry : formData.entrySet()) {
                            entryKey = entry.getKey();
                            entryValue = entry.getValue();
                            if (entryValue.size() > 1) {
                                for (String s : entryValue) {
                                    formDataBodyBuilder.append(entryKey)
                                            .append("=")
                                            .append(URLEncoder.encode(s, charsetName))
                                            .append("&");
                                }
                            }
                        }
                    } catch (UnsupportedEncodingException e) {
                        // ignore URLEncode Exception
                        log.error("URLEncoder exception is {}", Throwables.getStackTraceAsString(e));
                    }
                    String formDataBodyString = "";
                    if (formDataBodyBuilder.length() > 0) {
                        formDataBodyString = formDataBodyBuilder.substring(0, formDataBodyBuilder.length() - 1);
                    }
                    byte[] bodyBytes = formDataBodyString.getBytes(charset);
                    int contentLength = bodyBytes.length;
                    ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(request) {
                        @Override
                        public HttpHeaders getHeaders() {
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.putAll(super.getHeaders());
                            if (contentLength > 0) {
                                httpHeaders.setContentLength(contentLength);
                            } else {
                                httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                            }
                            return httpHeaders;
                        }
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return DataBufferUtils.read(new ByteArrayResource(bodyBytes),
                                    new NettyDataBufferFactory(ByteBufAllocator.DEFAULT),
                                    contentLength
                            );
                        }
                    };
                    ServerWebExchange mutateExchange = exchange.mutate().request(decorator).build();
                    return chain.filter(mutateExchange);
                }
        ));
    }

    /**
     * 读取请求的 jsonBody
     *
     * @param exchange
     * @param chain
     * @param context
     * @return
     */
    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain, GatewayContext context) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    // 释放内存
                    DataBufferUtils.release(dataBuffer);
                    Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                        DataBufferUtils.retain(buffer);
                        return Mono.just(buffer);
                    });
                    ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return cachedFlux;
                        }
                    };
                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                    return ServerRequest.create(mutatedExchange, MESSAGE_READERS)
                            .bodyToMono(String.class)
                            .doOnNext(objValue -> {
                                context.setCacheBody(objValue);
                                log.info("GatewayContext Read JsonBody is {}", objValue);
                            }).then(chain.filter(mutatedExchange));

                });
    }
}
