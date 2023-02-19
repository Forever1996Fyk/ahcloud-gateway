package com.ahcloud.gateway.test.webflux;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/7 16:54
 **/
public class FluxAndMonoTest {

    public static void main(String[] args) {
        String url = "findById/{id}/{id2}/{id3}";

        Set<String> paths = Sets.newHashSet(
                "{id}",
                "{id2}",
                "{id3}"
        );
        String[] strings = paths.toArray(new String[0]);
        String replace = StringUtils.replaceEach(url, strings, new String[] {"*","*","*"});
        System.out.println(replace);
    }
}
