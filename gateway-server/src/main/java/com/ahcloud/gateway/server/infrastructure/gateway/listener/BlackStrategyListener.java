package com.ahcloud.gateway.server.infrastructure.gateway.listener;

import com.ahcloud.gateway.common.sentinel.rule.BlackLimitRuleHandler;
import com.ahcloud.gateway.core.infrastructure.gateway.listener.event.BlackStrategyEvent;
import com.ahcloud.gateway.core.infrastructure.util.ServerWebExchangeUtils;
import com.ahcloud.gateway.scg.common.model.BlackStrategy;
import com.ahcloud.gateway.server.infrastructure.config.GatewayLimitProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/16 11:00
 **/
@Component
public class BlackStrategyListener implements ApplicationListener<BlackStrategyEvent> {

    /**
     * 时间段(单位：毫秒)
     */
    private final int period;
    /**
     * 次数
     */
    private final int count;
    private final BlackLimitRuleHandler blackLimitRuleHandler;

    @Autowired
    public BlackStrategyListener(RedisConnectionFactory redisConnectionFactory, GatewayLimitProperties properties) {
        this.blackLimitRuleHandler = new BlackLimitRuleHandler(redisConnectionFactory);
        this.period = properties.getPeriod();
        this.count = properties.getCount();
    }

    @Override
    public void onApplicationEvent(BlackStrategyEvent event) {
        ServerWebExchange exchange = event.getExchange();
        String path = ServerWebExchangeUtils.getRequestPath(exchange);
        String clientIp = ServerWebExchangeUtils.getClientIp(exchange);
        BlackStrategy blackStrategy = new BlackStrategy();
        blackStrategy.setName(path);
        blackStrategy.setKey(clientIp);
        blackStrategy.setPrefix(path);
        blackStrategy.setPeriod(period);
        blackStrategy.setCount(count);
        blackLimitRuleHandler.countAndRegisterRule(blackStrategy);
    }
}
