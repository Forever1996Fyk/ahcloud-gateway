package com.ahcloud.gateway.server.infrastructure.gateway.listener.event;

import com.ahcloud.gateway.server.infrastructure.gateway.enums.ConfigGroupEnum;
import com.ahcloud.gateway.server.infrastructure.gateway.enums.DataEventTypeEnum;
import com.google.common.collect.Lists;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 15:33
 **/
public class DataChangedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 4226617684506700868L;

    private final DataEventTypeEnum eventType;

    private final ConfigGroupEnum groupKey;

    public DataChangedEvent(List<?> source, DataEventTypeEnum eventType, ConfigGroupEnum groupKey) {
        super(source);
        this.eventType = eventType;
        this.groupKey = groupKey;
    }

    public DataChangedEvent(Object source, DataEventTypeEnum eventType, ConfigGroupEnum groupKey) {
        super(Lists.newArrayList(source));
        this.eventType = eventType;
        this.groupKey = groupKey;
    }

    public DataEventTypeEnum getEventType() {
        return eventType;
    }

    public ConfigGroupEnum getGroupKey() {
        return groupKey;
    }

    @Override
    public List<?> getSource() {
        return (List<?>) super.getSource();
    }

}
