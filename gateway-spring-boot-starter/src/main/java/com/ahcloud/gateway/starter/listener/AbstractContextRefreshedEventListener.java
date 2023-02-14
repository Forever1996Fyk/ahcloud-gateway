package com.ahcloud.gateway.starter.listener;

import com.ahcloud.gateway.client.constant.GatewayClientConstants;
import com.ahcloud.gateway.client.dubbo.api.dto.ApiRegisterDTO;
import com.ahcloud.gateway.client.dubbo.route.dto.RouteRegisterDTO;
import com.ahcloud.gateway.client.exception.GatewayClientIllegalArgumentException;
import com.ahcloud.gateway.starter.configuration.PropertiesConfiguration;
import com.ahcloud.gateway.starter.register.event.ApiRegisterEvent;
import com.ahcloud.gateway.starter.register.event.RouteRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 11:19
 **/
@Slf4j
public abstract class AbstractContextRefreshedEventListener<T, A extends Annotation> implements ApplicationListener<ContextRefreshedEvent>, ApplicationEventPublisherAware {

    /**
     * api path separator.
     */
    protected static final String PATH_SEPARATOR = "/";

    private final AtomicBoolean registered = new AtomicBoolean(false);

    private ApplicationEventPublisher publisher;

    private final String appName;

    protected final Environment env;

    public AbstractContextRefreshedEventListener(final PropertiesConfiguration clientProps, final Environment env) {
        this.env = env;
        this.appName = clientProps.getProps().getProperty(GatewayClientConstants.APP_NAME);
        if (StringUtils.isBlank(getAppName())) {
            String errorMsg = "client register param must config the appName or contextPath";
            log.error(errorMsg);
            throw new GatewayClientIllegalArgumentException(errorMsg);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final ApplicationContext context = event.getApplicationContext();
        Map<String, T> beans = getBeans(context);
        if (CollectionUtils.isEmpty(beans)) {
            return;
        }
        if (!registered.compareAndSet(false, true)) {
            return;
        }
        // 路由注册
        publisher.publishEvent(new RouteRegisterEvent(buildRouteRegisterDTO(context, beans)));

        // 接口注册
        beans.forEach((k, v) -> publisher.publishEvent(new ApiRegisterEvent(buildApiRegisterDTO(context, v))));

    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 获取spring bean
     * @param context
     * @return
     */
    protected abstract Map<String, T> getBeans(ApplicationContext context);

    /**
     * 注解类型
     * @return
     */
    protected abstract Class<A> getAnnotationType();

    /**
     * 构建api注册实体
     * @param context
     * @param beans
     * @return
     */
    protected abstract List<ApiRegisterDTO> buildApiRegisterDTO(ApplicationContext context, T beans);

    /**
     * 构建路由注册实体
     * @param context
     * @param beans
     * @return
     */
    protected abstract RouteRegisterDTO buildRouteRegisterDTO(ApplicationContext context, Map<String, T> beans);

    public String getAppName() {
        return appName;
    }
}
