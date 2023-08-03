package com.ahcloud.gateway.scg.common.support;

import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/8/3 23:32
 **/
public class BodyInserterContext implements BodyInserter.Context {
    private final ExchangeStrategies exchangeStrategies;

    public BodyInserterContext() {
        this.exchangeStrategies = ExchangeStrategies.withDefaults();
    }

    @Override
    public List<HttpMessageWriter<?>> messageWriters() {
        return exchangeStrategies.messageWriters();
    }

    @Override
    public Optional<ServerHttpRequest> serverRequest() {
        return Optional.empty();
    }

    @Override
    public Map<String, Object> hints() {
        return Collections.emptyMap();
    }
}
