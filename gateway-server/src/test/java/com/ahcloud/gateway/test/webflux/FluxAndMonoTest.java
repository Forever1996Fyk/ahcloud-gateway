package com.ahcloud.gateway.test.webflux;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
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
        test();
    }

    private static class Test {
        private Integer id;

        private String name;

        public Test(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static List<Integer> cache = Lists.newArrayList(1,2,3);
    private static List<Integer> cache1 = Lists.newArrayList(4,5,6);
    private static void test() {
        Flux.fromIterable(cache)
                .flatMap(FluxAndMonoTest::test)
                .then(
                        Mono.defer(() -> {
                            Flux.fromIterable(cache1)
                                    .flatMap(FluxAndMonoTest::test2)
                                    .subscribe(System.out::println);
                            return Mono.empty();
                        })

                ).subscribe();
    }

    private static Mono<Void> test(Integer item) {
        cache1.add(item);
        return Mono.empty();
    }

    private static Mono<Integer> test2(Integer item) {
        return Mono.just(item * 2);
    }

    private static Mono<Void> eq(String d) {
        if (d.equals("测试4")) {
            System.out.println("操作成功");
        }
        return Mono.empty();
    }
}
