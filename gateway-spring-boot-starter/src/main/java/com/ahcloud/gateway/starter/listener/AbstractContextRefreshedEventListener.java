package com.ahcloud.gateway.starter.listener;

import com.ahcloud.gateway.client.constant.GatewayConstants;
import com.ahcloud.gateway.client.exception.GatewayClientIllegalArgumentException;
import com.ahcloud.gateway.client.util.UriUtils;
import com.ahcloud.gateway.register.common.config.PropertiesConfiguration;
import com.ahcloud.gateway.register.common.dto.MetaDataRegisterDTO;
import com.ahcloud.gateway.register.common.dto.RouteRegisterDTO;
import com.ahcloud.gateway.starter.disruptor.GatewayClientRegisterEventPublisher;
import com.ahcloud.gateway.starter.repository.GatewayClientRegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 11:19
 **/
@Slf4j
public abstract class AbstractContextRefreshedEventListener<T, A extends Annotation> implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * api path separator.
     */
    protected static final String PATH_SEPARATOR = "/";

    private final GatewayClientRegisterEventPublisher publisher = GatewayClientRegisterEventPublisher.getInstance();

    private final AtomicBoolean registered = new AtomicBoolean(false);

    private final Long appId;
    private final String appName;

    private final String serviceId;

    protected final Environment env;

    private final String contextPath;

    private final String host;

    private final String port;

    public AbstractContextRefreshedEventListener(final PropertiesConfiguration clientProps,
                                                 final Environment env,
                                                 final GatewayClientRegisterRepository repository) {
        Properties props = clientProps.getProps();
        this.env = env;
        this.appId = env.getProperty(GatewayConstants.APPID, Long.class);
        this.appName = env.getProperty(GatewayConstants.APP_NAME);
        this.serviceId = StringUtils.defaultIfBlank(env.getProperty(GatewayConstants.SERVICE_ID), this.appName);
        this.host = env.getProperty(GatewayConstants.HOST);
        this.port = env.getProperty(GatewayConstants.PORT);
        this.contextPath = Optional.ofNullable(env.getProperty(GatewayConstants.CONTEXT_PATH)).map(UriUtils::repairData).orElse("");
        if (getAppId() == null) {
            String errorMsg = "client register param must config the appId or contextPath";
            log.error(errorMsg);
            throw new GatewayClientIllegalArgumentException(errorMsg);
        }
        if (StringUtils.isBlank(getAppName())) {
            String errorMsg = "client register param must config the appName or contextPath";
            log.error(errorMsg);
            throw new GatewayClientIllegalArgumentException(errorMsg);
        }
        publisher.start(repository);
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
        publisher.publishEvent(buildRouteRegisterDTO(context, beans));
        // 接口元数据注册
        beans.forEach(this::handleMetadata);
    }

    protected void handleMetadata(final String beanName, final T bean) {
        Class<?> clazz = getCorrectedClass(bean);
        final A beanGatewayClient = AnnotatedElementUtils.findMergedAnnotation(clazz, getAnnotationType());
        final String superPath = buildApiSuperPath(clazz, beanGatewayClient);
        // Compatible with previous versions
        if (Objects.nonNull(beanGatewayClient) && superPath.contains("*")) {
            handleClass(clazz, bean, beanGatewayClient, superPath);
            return;
        }
        final Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);
        for (Method method : methods) {
            handleMethod(bean, clazz, beanGatewayClient, method, superPath);
        }
    }


    protected Class<?> getCorrectedClass(final T bean) {
        Class<?> clazz = bean.getClass();
        if (AopUtils.isAopProxy(bean)) {
            clazz = AopUtils.getTargetClass(bean);
        }
        return clazz;
    }

    protected void handleClass(final Class<?> clazz,
                               final T bean,
                               @NonNull final A beanGatewayClient,
                               final String superPath) {
        publisher.publishEvent(buildMetaDataDTO(bean, beanGatewayClient, pathJoin(contextPath, superPath), clazz, null));
    }

    protected void handleMethod(final T bean,
                                final Class<?> clazz,
                                @Nullable final A beanGatewayClient,
                                final Method method,
                                final String superPath) {
        A methodMethodClient = AnnotatedElementUtils.findMergedAnnotation(method, getAnnotationType());
        if (Objects.nonNull(methodMethodClient)) {
            publisher.publishEvent(buildMetaDataDTO(bean, methodMethodClient, buildApiPath(method, superPath, methodMethodClient), clazz, method));
        }
    }

    protected String pathJoin(@NonNull final String... path) {
        StringBuilder result = new StringBuilder(PATH_SEPARATOR);
        for (String p : path) {
            if (!result.toString().endsWith(PATH_SEPARATOR)) {
                result.append(PATH_SEPARATOR);
            }
            result.append(p.startsWith(PATH_SEPARATOR) ? p.replaceFirst(PATH_SEPARATOR, "") : p);
        }
        return result.toString();
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
     * 构建 api 父路径
     * @param clazz
     * @param beanGatewayClient
     * @return
     */
    protected abstract String buildApiSuperPath(Class<?> clazz, @Nullable A beanGatewayClient);

    /**
     * 构建api 接口路径
     * @param method
     * @param superPath
     * @param methodGatewayClient
     * @return
     */
    protected abstract String buildApiPath(Method method, String superPath, @NonNull A methodGatewayClient);

    /**
     * 构建路由注册实体
     * @param context
     * @param beans
     * @return
     */
    protected abstract RouteRegisterDTO buildRouteRegisterDTO(ApplicationContext context, Map<String, T> beans);

    /**
     * 构建元数据
     * @param bean
     * @param beanGatewayClient
     * @param pathJoin
     * @param clazz
     * @param method
     * @return
     */
    protected abstract MetaDataRegisterDTO buildMetaDataDTO(T bean, A beanGatewayClient, String pathJoin, Class<?> clazz, Method method);

    public String getAppName() {
        return appName;
    }

    public Long getAppId() {
        return appId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getEnv() {
        String[] profiles = this.env.getActiveProfiles();
        return profiles[0];
    }

    public GatewayClientRegisterEventPublisher getPublisher() {
        return publisher;
    }
}
