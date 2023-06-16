package com.ahcloud.gateway.common.sentinel.rule;

import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.common.sentinel.model.AuthorityRuleDTO;
import com.ahcloud.gateway.common.sentinel.model.enums.AuthorityStrategyEnum;
import com.ahcloud.gateway.scg.common.model.BlackStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Optional;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/16 11:19
 **/
@Slf4j
public class BlackLimitRuleHandler {

    private final RedisConnection redisConnection;

    public BlackLimitRuleHandler(RedisConnectionFactory factory) {
        this.redisConnection = factory.getConnection();
    }

    /**
     * 计算并注册规则
     *
     * 这里的规则是，在period时间段内，当前key自增次数，是否大于count，如果大于count则注册黑名单
     * @param strategy
     */
    public void countAndRegisterRule(BlackStrategy strategy) {
        byte[] key = JsonUtils.beanToByte(strategy.getPrefix() + strategy.getKey());
        long value = Optional.ofNullable(redisConnection.incr(key)).orElse(0L);
        log.info("redis limit value is {}", value);
        if (value >= strategy.getCount()) {
            AuthorityRuleDTO ruleDTO = AuthorityRuleDTO.builder()
                    .authorityStrategyEnum(AuthorityStrategyEnum.BLACK)
                    .limitApp(strategy.getKey())
                    .resource(strategy.getName())
                    .build();
            AuthorityRuleHandler.register(ruleDTO);
        } else if (value == 1) {
            redisConnection.expire(key, strategy.getPeriod());
        }
    }
}
