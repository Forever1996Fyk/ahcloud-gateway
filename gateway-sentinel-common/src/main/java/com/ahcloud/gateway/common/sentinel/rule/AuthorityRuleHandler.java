package com.ahcloud.gateway.common.sentinel.rule;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.common.sentinel.model.AuthorityRuleDTO;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;

import java.util.Collections;
import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/15 09:58
 **/
public class AuthorityRuleHandler {

    /**
     * 注册授权规则
     * @param authorityRuleDTO
     */
    public static void register(AuthorityRuleDTO authorityRuleDTO) {
        AuthorityRule authorityRule = convert(authorityRuleDTO);
        AuthorityRuleManager.loadRules(Collections.singletonList(authorityRule));
    }

    /**
     * 注册授权规则
     * @param authorityRuleDTOList
     */
    public static void register(List<AuthorityRuleDTO> authorityRuleDTOList) {
        List<AuthorityRule> authorityRuleList = CollectionUtils.convertList(authorityRuleDTOList, AuthorityRuleHandler::convert);
        AuthorityRuleManager.loadRules(authorityRuleList);
    }

    private static AuthorityRule convert(AuthorityRuleDTO authorityRuleDTO) {
        AuthorityRule authorityRule = new AuthorityRule();
        authorityRule.setResource(authorityRuleDTO.getResource());
        authorityRule.setLimitApp(authorityRuleDTO.getLimitApp());
        authorityRule.setStrategy(authorityRuleDTO.getAuthorityStrategyEnum().getType());
        return authorityRule;
    }
}
