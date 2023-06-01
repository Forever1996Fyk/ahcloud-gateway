package com.ahcloud.gateway.register.common.subsriber;

import com.ahcloud.gateway.register.common.type.DataType;
import com.ahcloud.gateway.register.common.type.DataTypeParent;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 10:07
 **/
public interface ExecutorTypeSubscriber<T extends DataTypeParent> extends ExecutorSubscriber<T> {

    /**
     * 数据类型
     * @return
     */
    DataType getType();
}
