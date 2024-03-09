package com.ahcloud.gateway.scg.common.aware;

import com.ahcloud.gateway.scg.common.util.SpringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @program: ahcloud-common
 * @description: The type gateway application context aware.
 * @author: YuKai Fan
 * @create: 2023/8/10 08:53
 **/
public class GatewayApplicationContextAware implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.getInstance().setApplicationContext(applicationContext);
    }
}
