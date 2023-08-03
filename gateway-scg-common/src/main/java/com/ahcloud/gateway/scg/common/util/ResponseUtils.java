package com.ahcloud.gateway.scg.common.util;

import com.ahcloud.gateway.client.constant.GatewayConstants;
import com.ahcloud.gateway.scg.common.support.BodyInserterContext;
import com.ahcloud.gateway.scg.common.support.CachedBodyOutputMessage;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/8/3 23:22
 **/
public class ResponseUtils {
    private static final String CHUNKED = "chunked";

    /**
     * create CachedBodyOutputMessage.
     *
     * @param exchange ServerWebExchange
     * @return CachedBodyOutputMessage.
     */
    public static CachedBodyOutputMessage newCachedBodyOutputMessage(final ServerWebExchange exchange) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        return new CachedBodyOutputMessage(exchange, headers);
    }

    /**
     * the response write with data.
     *
     * @param clientResponse the client response
     * @param exchange the exchange
     * @param publisher the publisher
     * @param elementClass the elementClass
     * @param <T> the element type
     * @param <P> the publishing type
     * @return the response wrapper data
     */
    public static <T, P extends Publisher<T>> Mono<Void> writeWith(final ClientResponse clientResponse,
                                                                   final ServerWebExchange exchange,
                                                                   final P publisher,
                                                                   final Class<T> elementClass) {
        BodyInserter<P, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(publisher, elementClass);
        CachedBodyOutputMessage outputMessage = ResponseUtils.newCachedBodyOutputMessage(exchange);
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
            Mono<DataBuffer> messageBody = ResponseUtils.fixBodyMessage(exchange.getResponse(), outputMessage);
            exchange.getAttributes().put(GatewayConstants.CLIENT_RESPONSE_ATTR, clientResponse);
            return exchange.getResponse().writeWith(messageBody);
        })).onErrorResume((Function<Throwable, Mono<Void>>) throwable -> ResponseUtils.release(outputMessage, throwable));
    }

    /**
     * fix the body message.
     *
     * @param response      current response
     * @param outputMessage cache message
     * @return fixed body message
     */
    public static Mono<DataBuffer> fixBodyMessage(final ServerHttpResponse response,
                                                  final CachedBodyOutputMessage outputMessage) {
        fixHeaders(response.getHeaders());
        return DataBufferUtils.join(outputMessage.getBody());
    }

    /**
     * release source.
     *
     * @param outputMessage CachedBodyOutputMessage
     * @param <T> the reified {@link Subscriber} type
     * @param throwable     Throwable
     * @return Mono.
     */
    public static <T> Mono<T> release(final CachedBodyOutputMessage outputMessage, final Throwable throwable) {
        if (Boolean.TRUE.equals(outputMessage.getCache())) {
            return outputMessage.getBody().map(DataBufferUtils::release).then(Mono.error(throwable));
        }
        return Mono.error(throwable);
    }

    /**
     * build client response with current response data.
     *
     * @param response current response
     * @param body     current response body
     * @return the client response
     */
    public static ClientResponse buildClientResponse(final ServerHttpResponse response,
                                                     final Publisher<? extends DataBuffer> body) {
        ClientResponse.Builder builder = ClientResponse.create(Objects.requireNonNull(response.getStatusCode()), getReaders());
        return builder
                .headers(headers -> headers.putAll(response.getHeaders()))
                .cookies(cookies -> response.getCookies())
                .body(Flux.from(body)).build();
    }

    /**
     * fix headers.
     *
     * @param httpHeaders the headers
     */
    private static void fixHeaders(final HttpHeaders httpHeaders) {
        httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
        httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, CHUNKED);
    }
}
