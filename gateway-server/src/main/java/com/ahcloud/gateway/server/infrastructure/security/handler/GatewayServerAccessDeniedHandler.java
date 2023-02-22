package com.ahcloud.gateway.server.infrastructure.security.handler;

import com.ahcloud.common.enums.ErrorCode;
import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.core.domain.response.GatewayResponseResult;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayAccessDeniedException;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/17 10:27
 **/
@Slf4j
public class GatewayServerAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        log.error("GatewayServerAccessDeniedHandler[handle] handle is {}", Throwables.getStackTraceAsString(denied));
        Map<String, String> parameters = new LinkedHashMap<>();

        AccessDeniedResult accessDeniedResult = AccessDeniedResult.of();
        if (denied instanceof GatewayAccessDeniedException) {
            GatewayAccessDeniedException accessDeniedException = (GatewayAccessDeniedException) denied;
            ErrorCode errorCode = accessDeniedException.getErrorCode();
            accessDeniedResult = AccessDeniedResult.of(errorCode);
        }
        GatewayResponseResult result = GatewayResponseResult.ofSuccess(accessDeniedResult);

        return exchange.getPrincipal()
                .filter(AbstractOAuth2TokenAuthenticationToken.class::isInstance)
                .map(token -> errorMessageParameters(parameters))
                .switchIfEmpty(Mono.just(parameters))
                .flatMap(params -> respond(exchange, params, result));
    }

    private static Map<String, String> errorMessageParameters(Map<String, String> parameters) {
        parameters.put("error", BearerTokenErrorCodes.INSUFFICIENT_SCOPE);
        parameters.put("error_description", "The request requires higher privileges than provided by the access token.");
        parameters.put("error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1");

        return parameters;
    }

    private static Mono<Void> respond(ServerWebExchange exchange, Map<String, String> parameters, GatewayResponseResult result) {
        String wwwAuthenticate = computeWWWAuthenticateHeaderValue(parameters);
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory dataBufferFactory = response.bufferFactory();
        DataBuffer buffer = dataBufferFactory.wrap(createErrorMsg(result));
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().set(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticate);
        return response.writeWith(Mono.just(buffer))
                .doOnError(error -> DataBufferUtils.release(buffer));
    }

    private static byte[] createErrorMsg(GatewayResponseResult result) {
        return JsonUtils.beanToByte(result);
    }

    private static String computeWWWAuthenticateHeaderValue(Map<String, String> parameters) {
        StringBuilder wwwAuthenticate = new StringBuilder();
        wwwAuthenticate.append("Bearer");
        if (!parameters.isEmpty()) {
            wwwAuthenticate.append(" ");
            int i = 0;
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                wwwAuthenticate.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
                if (i != parameters.size() - 1) {
                    wwwAuthenticate.append(", ");
                }
                i++;
            }
        }

        return wwwAuthenticate.toString();
    }

    private static class AccessDeniedResult {

        /**
         * 返回码
         */
        private final int code;

        /**
         * 错误信息
         */
        private final String message;


        public AccessDeniedResult(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public static AccessDeniedResult of() {
            return new AccessDeniedResult(GatewayRetCodeEnum.SYSTEM_ERROR.getCode(), GatewayRetCodeEnum.SYSTEM_ERROR.getMessage());
        }

        public static AccessDeniedResult of(ErrorCode errorCode) {
            return new AccessDeniedResult(errorCode.getCode(), errorCode.getMessage());
        }
    }
}
