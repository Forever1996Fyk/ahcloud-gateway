package com.ahcloud.gateway.server.infrastructure.filter;

import com.ahcloud.common.result.ResponseResult;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.scg.common.util.WebFluxResultUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/8/2 08:56
 **/
@Component
public class FileSizeFilter implements WebFilter, Ordered {
    private static final int BYTES_PER_MB = 1024 * 1024;

    @Value("${gateway.file.maxSize:10}")
    private int fileMaxSize;

    private final List<HttpMessageReader<?>> messageReaders;

    public FileSizeFilter() {
        HandlerStrategies handlerStrategies = HandlerStrategies.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(fileMaxSize * BYTES_PER_MB)).build();
        this.messageReaders = handlerStrategies.messageReaders();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(mediaType)) {
            ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);
            return serverRequest.bodyToMono(DataBuffer.class)
                    .flatMap(size -> {
                        if (size.capacity() > BYTES_PER_MB * fileMaxSize) {
                            ServerHttpResponse response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.BAD_REQUEST);
                            ResponseResult<Void> result = ResponseResult.ofFailed(GatewayRetCodeEnum.PAYLOAD_TOO_LARGE);
                            return WebFluxResultUtils.result(exchange, result);
                        }
                        BodyInserter<Mono<DataBuffer>, ReactiveHttpOutputMessage> bodyInsert = BodyInserters.fromPublisher(Mono.just(size), DataBuffer.class);
                        HttpHeaders headers = new HttpHeaders();
                        headers.putAll(exchange.getRequest().getHeaders());
                        headers.remove(HttpHeaders.CONTENT_LENGTH);
                        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(
                                exchange, headers);
                        return bodyInsert.insert(outputMessage, new BodyInserterContext())
                                .then(Mono.defer(() -> {
                                    ServerHttpRequest decorator = decorate(exchange, outputMessage);
                                    return chain.filter(exchange.mutate().request(decorator).build());

                                }));
                    });
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -10;
    }

    private ServerHttpRequestDecorator decorate(final ServerWebExchange exchange, final CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }
}
