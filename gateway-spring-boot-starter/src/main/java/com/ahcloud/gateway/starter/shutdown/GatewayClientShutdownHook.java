package com.ahcloud.gateway.starter.shutdown;

import com.ahcloud.gateway.starter.repository.GatewayClientRegisterRepository;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 20:13
 **/
@Slf4j
public class GatewayClientShutdownHook {

    private static final AtomicBoolean DELAY = new AtomicBoolean(false);

    private static String hookNamePrefix = "GatewayClientShutdownHook";

    private static AtomicInteger hookId = new AtomicInteger(0);

    private static Properties props;

    private static IdentityHashMap<Thread, Thread> delayHooks = new IdentityHashMap<>();

    private static IdentityHashMap<Thread, Thread> delayedHooks = new IdentityHashMap<>();

    /**
     * Add gateway client shutdown hook.
     *
     * @param registerRepository GatewayClientRegisterRepository
     * @param props  Properties
     */
    public static void set(final GatewayClientRegisterRepository registerRepository, final Properties props) {
        String name = String.join("-", hookNamePrefix, String.valueOf(hookId.incrementAndGet()));
        Runtime.getRuntime().addShutdownHook(new Thread(registerRepository::close, name));
        log.info("Add hook {}", name);
        GatewayClientShutdownHook.props = props;
    }

    /**
     * Delay other shutdown hooks.
     */
    public static void delayOtherHooks() {
        if (!DELAY.compareAndSet(false, true)) {
            return;
        }
        TakeoverOtherHooksThread thread = new TakeoverOtherHooksThread();
        thread.start();
    }

    /**
     * Delay other shutdown hooks thread.
     */
    private static class TakeoverOtherHooksThread extends Thread {
        @Override
        public void run() {
            int shutdownWaitTime = Integer.parseInt(props.getProperty("shutdownWaitTime", "3000"));
            int delayOtherHooksExecTime = Integer.parseInt(props.getProperty("delayOtherHooksExecTime", "2000"));
            IdentityHashMap<Thread, Thread> hooks = null;
            try {
                Class<?> clazz = Class.forName(props.getProperty("applicationShutdownHooksClassName", "java.lang.ApplicationShutdownHooks"));
                Field field = clazz.getDeclaredField(props.getProperty("applicationShutdownHooksFieldName", "hooks"));
                field.setAccessible(true);
                hooks = (IdentityHashMap<Thread, Thread>) field.get(clazz);
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ex) {
                log.error(ex.getMessage(), ex);
            }
            long s = System.currentTimeMillis();
            while (System.currentTimeMillis() - s < delayOtherHooksExecTime) {
                for (Iterator<Thread> iterator = Objects.requireNonNull(hooks).keySet().iterator(); iterator.hasNext();) {
                    Thread hook = iterator.next();
                    if (hook.getName().startsWith(hookNamePrefix)) {
                        continue;
                    }
                    if (delayHooks.containsKey(hook) || delayedHooks.containsKey(hook)) {
                        continue;
                    }
                    Thread delayHook = new Thread(() -> {
                        log.info("sleep {}ms", shutdownWaitTime);
                        try {
                            TimeUnit.MILLISECONDS.sleep(shutdownWaitTime);
                        } catch (InterruptedException ignore) {
                        }
                        hook.run();
                    }, hook.getName());
                    delayHooks.put(delayHook, delayHook);
                    iterator.remove();
                }

                for (Iterator<Thread> iterator = delayHooks.keySet().iterator(); iterator.hasNext();) {
                    Thread delayHook = iterator.next();
                    Runtime.getRuntime().addShutdownHook(delayHook);
                    delayedHooks.put(delayHook, delayHook);
                    iterator.remove();
                    log.info("hook {} will sleep {}ms when it start", delayHook.getName(), shutdownWaitTime);
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException ex) {
                    log.error(ex.getMessage(), ex);
                }
            }

            hookNamePrefix = null;
            hookId = new AtomicInteger(0);
            props = null;
            delayHooks = null;
            delayedHooks = null;
        }
    }
}
