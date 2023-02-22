package com.ahcloud.gateway.core.infrastructure.gateway.listener;

import com.ahcloud.gateway.core.domain.api.dto.ApiRefreshDTO;
import com.ahcloud.gateway.core.domain.route.dto.RouteDefinitionDTO;
import com.ahcloud.gateway.core.infrastructure.gateway.listener.event.DataChangedEvent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 22:36
 **/
@Component
public class DataChangeEventDispatcher implements ApplicationListener<DataChangedEvent>, InitializingBean {

    private final ApplicationContext applicationContext;

    private List<DataChangeListener> listeners;

    public DataChangeEventDispatcher(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(final DataChangedEvent event) {
        for (DataChangeListener listener : listeners) {
            switch (event.getGroupKey()) {
                case ROUTE:
                    listener.onRouteDefinitionChanged((List<RouteDefinitionDTO>) event.getSource(), event.getEventType());
                    break;
                case API:
                    listener.onApiRefreshChanged((List<ApiRefreshDTO>) event.getSource(), event.getEventType());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + event.getGroupKey());
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Collection<DataChangeListener> listenerBeans = applicationContext.getBeansOfType(DataChangeListener.class).values();
        this.listeners = Collections.unmodifiableList(new ArrayList<>(listenerBeans));
    }
}
