package com.ahcloud.gateway.server.infrastructure.exception.handler;

import com.ahcloud.common.enums.ErrorCode;
import com.ahcloud.gateway.core.infrastructure.gateway.listener.event.BlackStrategyEvent;
import com.ahcloud.gateway.scg.common.exception.GatewayBlackException;
import com.ahcloud.gateway.scg.common.exception.handler.AbstractExceptionRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/16 09:48
 **/
@Slf4j
public class GatewayBlackExceptionRequestHandler extends AbstractExceptionRequestHandler<GatewayBlackException> {

    private final ApplicationEventPublisher publisher;

    public GatewayBlackExceptionRequestHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    protected Mono<Void> afterHandle(GatewayBlackException e) {
        log.info("after handle");
        publisher.publishEvent(new BlackStrategyEvent(e.getExchange(), e.getErrorMessage()));
        return Mono.empty();
    }

    @Override
    protected void afterHandle2(GatewayBlackException e) {
        publisher.publishEvent(new BlackStrategyEvent(e.getExchange(), e.getErrorMessage()));
    }

    @Override
    protected ErrorCode extract(GatewayBlackException ex) {
        return ex.getErrorCode();
    }

    @Override
    public boolean support(Throwable t) {
        return t instanceof GatewayBlackException;
    }
}
