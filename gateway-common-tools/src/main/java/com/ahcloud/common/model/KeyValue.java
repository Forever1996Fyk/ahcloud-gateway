package com.ahcloud.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 11:12
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyValue<K, V> {

    private K key;
    private V value;
}
