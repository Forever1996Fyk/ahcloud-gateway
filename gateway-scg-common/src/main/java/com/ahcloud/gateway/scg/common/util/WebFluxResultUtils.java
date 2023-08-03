package com.ahcloud.gateway.scg.common.util;

import com.ahcloud.common.utils.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/8/2 21:32
 **/
public class WebFluxResultUtils {

    /**
     * 构建返回结果
     * @param exchange
     * @param result
     * @return
     */
    public static Mono<Void> result(final ServerWebExchange exchange, final Object result) {
        if (Objects.isNull(result)) {
            return Mono.empty();
        }
        Object resultData = format(result);
        MediaType mediaType = MediaType.TEXT_PLAIN;
        if (!isBasicType(result)) {
            mediaType = MediaType.APPLICATION_JSON;
        }
        exchange.getResponse().getHeaders().setContentType(mediaType);
        assert null != resultData;
        final byte[] bytes = (resultData instanceof byte[])
                ? (byte[]) resultData : resultData.toString().getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
                        .doOnNext(data -> exchange.getResponse().getHeaders().setContentLength(data.readableByteCount()))
        );
    }

    /**
     * 是否为基础类型
     * @param object
     * @return
     */
    public static boolean isBasicType(final Object object) {
        return (object instanceof Integer)
                || (object instanceof Byte)
                || (object instanceof Long)
                || (object instanceof Double)
                || (object instanceof Float)
                || (object instanceof Short)
                || (object instanceof Boolean)
                || (object instanceof CharSequence);
    }

    public static Object format(Object origin) {
        if (isBasicType(origin) || origin instanceof byte[]) {
            return origin;
        }
        return JsonUtils.toJsonString(origin);
    }
}
