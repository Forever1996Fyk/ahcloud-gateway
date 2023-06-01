package com.ahcloud.gateway.starter.disruptor.subscriber;

import com.ahcloud.gateway.register.common.dto.RouteRegisterDTO;
import com.ahcloud.gateway.register.common.subsriber.ExecutorTypeSubscriber;
import com.ahcloud.gateway.register.common.type.DataType;
import com.ahcloud.gateway.starter.repository.GatewayClientRegisterRepository;
import com.ahcloud.gateway.starter.shutdown.GatewayClientShutdownHook;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 15:27
 **/
@Slf4j
public class GatewayClientRouteExecutorSubscriber implements ExecutorTypeSubscriber<RouteRegisterDTO> {

    private final GatewayClientRegisterRepository repository;

    public GatewayClientRouteExecutorSubscriber(GatewayClientRegisterRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executor(Collection<RouteRegisterDTO> dataList) {
        for (RouteRegisterDTO routeRegisterDTO : dataList) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            while (true) {
                try(Socket ignored = new Socket(routeRegisterDTO.getHost(), routeRegisterDTO.getPort())) {
                    break;
                } catch (IOException e) {
                    long sleepTime = 1000;
                    // maybe the port is delay exposed
                    if (stopwatch.elapsed(TimeUnit.SECONDS) > 5) {
                        log.error("host:{}, port:{} connection failed, will retry",
                                routeRegisterDTO.getHost(), routeRegisterDTO.getPort());
                        // If the connection fails for a long time, Increase sleep time
                        if (stopwatch.elapsed(TimeUnit.SECONDS) > 180) {
                            sleepTime = 10000;
                        }
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(sleepTime);
                    } catch (InterruptedException ex) {
                        log.error("interrupted when sleep", ex);
                    }
                }
            }
            GatewayClientShutdownHook.delayOtherHooks();
            repository.persistRoute(routeRegisterDTO);
        }
    }

    @Override
    public DataType getType() {
        return DataType.ROUTE;
    }
}
