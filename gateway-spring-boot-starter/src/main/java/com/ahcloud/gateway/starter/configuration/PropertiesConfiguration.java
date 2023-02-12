package com.ahcloud.gateway.starter.configuration;

import java.util.Properties;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 14:34
 **/
public class PropertiesConfiguration {

    private Properties props = new Properties();

    public Properties getProps() {
        return props;
    }

    public void setProps(final Properties props) {
        this.props = props;
    }
}
