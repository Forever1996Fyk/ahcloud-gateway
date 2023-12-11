package com.ahcloud.gateway.starter.listener;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.client.constant.GatewayConstants;
import com.ahcloud.gateway.client.enums.ApiHttpMethodEnum;
import com.ahcloud.gateway.client.enums.RpcTypeEnum;
import com.ahcloud.gateway.client.util.IpUtils;
import com.ahcloud.gateway.register.common.config.PropertiesConfiguration;
import com.ahcloud.gateway.register.common.dto.MetaDataRegisterDTO;
import com.ahcloud.gateway.register.common.dto.RouteRegisterDTO;
import com.ahcloud.gateway.starter.annotation.GatewaySpringCloudClient;
import com.ahcloud.gateway.starter.repository.GatewayClientRegisterRepository;
import com.ahcloud.gateway.starter.util.PortUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 14:33
 **/
public class SpringCloudClientEventListener extends AbstractContextRefreshedEventListener<Object, GatewaySpringCloudClient> {

    private final List<Class<? extends Annotation>> mappingAnnotation = Lists.newArrayListWithCapacity(3);

    private final String servletContextPath;

    public SpringCloudClientEventListener(final PropertiesConfiguration clientProps, final Environment env, final GatewayClientRegisterRepository repository) {
        super(clientProps, env, repository);
        this.servletContextPath = env.getProperty("server.servlet.context-path", "");
        mappingAnnotation.add(GatewaySpringCloudClient.class);
        mappingAnnotation.add(RequestMapping.class);
    }

    @Override
    protected Map<String, Object> getBeans(ApplicationContext context) {
        return context.getBeansWithAnnotation(Controller.class);
    }

    @Override
    protected Class<GatewaySpringCloudClient> getAnnotationType() {
        return GatewaySpringCloudClient.class;
    }

    @Override
    protected void handleMethod(final Object bean,
                                final Class<?> clazz,
                                @Nullable final GatewaySpringCloudClient beanGatewayClient,
                                final Method method,
                                final String superPath) {
        // 只处理带有 @GatewaySpringCloudClient, @RequestMapping, @GetMapping @PostMapping ... 注解的方法
        if (!method.isAnnotationPresent(GatewaySpringCloudClient.class) && !method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        final RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        GatewaySpringCloudClient methodGatewayClient = AnnotatedElementUtils.findMergedAnnotation(method, GatewaySpringCloudClient.class);
        methodGatewayClient = Objects.isNull(methodGatewayClient) ? beanGatewayClient : methodGatewayClient;
        // the result of ReflectionUtils#getUniqueDeclaredMethods contains methods such as hashCode, wait, toSting
        // add Objects.nonNull(requestMapping) to make sure not register wrong method
        if (Objects.nonNull(methodGatewayClient) && Objects.nonNull(requestMapping)) {
            getPublisher().publishEvent(
                    buildMetaDataDTO(bean, methodGatewayClient, buildApiPath(method, superPath, methodGatewayClient), clazz, method)
            );
        }
    }

    @Override
    protected RouteRegisterDTO buildRouteRegisterDTO(ApplicationContext context, Map<String, Object> beans) {
        String host = getHost();
        int port = Integer.parseInt(Optional.ofNullable(getPort()).orElseGet(() -> "-1"));
        final int mergedPort = port <= 0 ? PortUtils.findPort(context.getAutowireCapableBeanFactory()) : port;
        return RouteRegisterDTO.builder()
                .host(IpUtils.isCompleteHost(host) ? host : IpUtils.getHost(host))
                .port(mergedPort)
                .appId(getAppId())
                .serviceId(getServiceId())
                .appName(getAppName())
                .contextPath(StringUtils.defaultIfBlank(getContextPath(), this.servletContextPath))
                .rpcType(RpcTypeEnum.SPRING_CLOUD.getName())
                .env(getEnv())
                .build();
    }

    @Override
    protected MetaDataRegisterDTO buildMetaDataDTO(Object bean, GatewaySpringCloudClient beanGatewayClient, String pathJoin, Class<?> clazz, Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Set<String> pathMarkSet = Sets.newHashSet();
        for (Annotation[] parameter : parameterAnnotations) {
            if (ArrayUtils.isEmpty(parameter)) {
                continue;
            }
            for (Annotation parameterAnnotation : parameter) {
                if (parameterAnnotation.annotationType().equals(PathVariable.class)) {
                    PathVariable pathVariable = (PathVariable) parameterAnnotation;
                    String value = pathVariable.value();
                    if (StringUtils.isNotBlank(value)) {
                        String mark = "{" + value + "}";
                        pathMarkSet.add(mark);
                    }
                }
            }
        }
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        String produce = requestMapping.produces().length == 0 ? GatewayConstants.MEDIA_TYPE_ALL_VALUE : String.join(",", requestMapping.produces());
        String consume = requestMapping.consumes().length == 0 ? GatewayConstants.MEDIA_TYPE_ALL_VALUE : String.join(",", requestMapping.consumes());
        RequestMethod[] requestMethods = requestMapping.method();
        ApiHttpMethodEnum apiHttpMethodEnum = ApiHttpMethodEnum.OPTIONS;
        if (ArrayUtils.isNotEmpty(requestMethods)) {
            apiHttpMethodEnum = ApiHttpMethodEnum.valueOf(requestMethods[0].name());
        }
        if (CollectionUtils.isNotEmpty(pathMarkSet)) {
            for (String pathMark : pathMarkSet) {
                pathJoin = StringUtils.replace(pathJoin, pathMark, "**");
            }
        }
        return MetaDataRegisterDTO.builder()
                .appId(getAppId())
                .appName(getAppName())
                .serviceId(getServiceId())
                .contextPath(StringUtils.defaultIfBlank(getContextPath(), this.servletContextPath))
                .apiHttpMethodEnum(apiHttpMethodEnum)
                .produce(produce)
                .consume(consume)
                .qualifiedName(clazz.getName())
                .methodName(method.getName())
                .apiPath(pathJoin)
                .rpcType(RpcTypeEnum.SPRING_CLOUD.getName())
                .env(getEnv())
                .build();
    }

    /**
     * 如果Controller类 存在GatewaySpringCloudClient注解，且存在path, 则直接返回, 否则找到Controller类上的 RequestMapping注解获取请求path
     *
     * @param clazz
     * @param beanGatewayClient
     * @return
     */
    @Override
    protected String buildApiSuperPath(final Class<?> clazz, @Nullable final GatewaySpringCloudClient beanGatewayClient) {
        if (Objects.nonNull(beanGatewayClient) && StringUtils.isNotBlank(beanGatewayClient.path())) {
            return beanGatewayClient.path();
        }
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
        // Only the first path is supported temporarily
        if (Objects.nonNull(requestMapping) && ArrayUtils.isNotEmpty(requestMapping.path())
                && StringUtils.isNotBlank(requestMapping.path()[0])) {
            return requestMapping.path()[0];
        }
        return "";
    }

    @Override
    protected String buildApiPath(Method method, String superPath, GatewaySpringCloudClient methodGatewayClient) {
        final String contextPath = getContextPath();
        if (StringUtils.isNotBlank(methodGatewayClient.path())) {
            return pathJoin(getServiceId(), contextPath, superPath, methodGatewayClient.path());
        }
        final String path = getPathByMethod(method);
        if (StringUtils.isNotBlank(path)) {
            return pathJoin(getServiceId(), contextPath, superPath, path);
        }
        return pathJoin(getServiceId(), contextPath, superPath);
    }

    /**
     * 此方法的目的是为了, 保证@GatewaySpringCloudClient与@RequestMapping兼容
     * <p>
     * 也就是说，只要方法上存在其中一个接口，且带有path 即可。
     *
     * @param method
     * @return
     */
    private String getPathByMethod(@NonNull final Method method) {
        // 优先使用 GatewaySpringCloudClient的path
        for (Class<? extends Annotation> mapping : mappingAnnotation) {
            final Annotation mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, mapping);
            final String pathByAnnotation = getPathByAnnotation(mergedAnnotation);
            if (StringUtils.isNotBlank(pathByAnnotation)) {
                return pathByAnnotation;
            }
        }
        return null;
    }

    private String getPathByAnnotation(@Nullable final Annotation annotation) {
        if (Objects.isNull(annotation)) {
            return null;
        }
        final Object value = AnnotationUtils.getValue(annotation, "value");
        if (value instanceof String && StringUtils.isNotBlank((String) value)) {
            return (String) value;
        }
        if (value instanceof String[] && ArrayUtils.isNotEmpty((String[]) value)
                && StringUtils.isNotBlank(((String[]) value)[0])) {
            return ((String[]) value)[0];
        }
        return null;
    }
}
