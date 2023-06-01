package com.ahcloud.gateway.register.common.config;

import java.util.Properties;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 14:34
 **/
public class PropertiesConfiguration {

    private final Properties props = new Properties();

    public Properties getProps() {
        return props;
    }

    public void setProps(final Properties props) {
        this.props.putAll(props);
    }
}
