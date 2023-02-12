package com.ahcloud.gateway.starter.listener;

import com.ahcloud.gateway.client.constant.GatewayClientConstants;
import com.ahcloud.gateway.client.dubbo.api.dto.ApiRegisterDTO;
import com.ahcloud.gateway.client.enums.ApiHttpMethodEnum;
import com.ahcloud.gateway.starter.annotation.GatewaySpringCloudClient;
import com.ahcloud.gateway.starter.configuration.PropertiesConfiguration;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 14:33
 **/
public class SpringCloudClientEventListener extends AbstractContextRefreshedEventListener<Object, GatewaySpringCloudClient> {

    private final List<Class<? extends Annotation>> mappingAnnotation = Lists.newArrayListWithCapacity(3);

    public SpringCloudClientEventListener(final PropertiesConfiguration clientProps, final Environment env) {
        super(clientProps, env);
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
    protected List<ApiRegisterDTO> buildApiRegisterDTO(ApplicationContext context, Object bean) {
            Class<?> clazz = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();
            String superPath = buildApiSuperPath(clazz, AnnotatedElementUtils.findMergedAnnotation(clazz, getAnnotationType()));
            if (superPath.indexOf("*") > 0) {
                superPath = superPath.substring(0, superPath.lastIndexOf("/"));
            }
            Annotation annotation = AnnotatedElementUtils.findMergedAnnotation(clazz, getAnnotationType());
            if (Objects.isNull(annotation)) {
                return Lists.newArrayList();
            }
            final Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(clazz);
            List<ApiRegisterDTO> list = Lists.newArrayList();
            for (Method method : methods) {
                // 只处理带有 @GatewaySpringCloudClient, @RequestMapping, @GetMapping @PostMapping ... 注解的方法
                if (!method.isAnnotationPresent(GatewaySpringCloudClient.class) && !method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }
                RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
                GatewaySpringCloudClient gatewaySpringCloudClient = AnnotatedElementUtils.findMergedAnnotation(method, GatewaySpringCloudClient.class);
                String produce = requestMapping.produces().length == 0 ? GatewayClientConstants.MEDIA_TYPE_ALL_VALUE : String.join(",", requestMapping.produces());
                String consume = requestMapping.consumes().length == 0 ? GatewayClientConstants.MEDIA_TYPE_ALL_VALUE : String.join(",", requestMapping.consumes());
                String appName = getAppName();
                String[] paths = requestMapping.value();
                String path = getMethodPath(gatewaySpringCloudClient.path(), paths);
                RequestMethod[] requestMethods = requestMapping.method();
                List<ApiHttpMethodEnum> collect = Stream.of(requestMethods).map(item -> ApiHttpMethodEnum.of(item.name())).collect(Collectors.toList());
                ApiHttpMethodEnum[] apiHttpMethodEnums = collect.toArray(new ApiHttpMethodEnum[]{});
                // 优先使用 GatewaySpringCloudClient 的path
                if (ArrayUtils.isEmpty(paths)) {
                    build(appName, superPath, path, apiHttpMethodEnums, list, produce, consume, method, clazz.getName());
                } else {
                    for (String value : paths) {
                        build(appName, superPath, value, apiHttpMethodEnums, list, produce, consume, method, clazz.getName());
                    }
                }
            }
            return list;
    }

    @Override
    public String getAppName() {
        return this.env.getProperty("spring.application.name");
    }

    private void build(String appName, String superPath, String path, ApiHttpMethodEnum[] apiHttpMethodEnums, List<ApiRegisterDTO> list, String produce, String consume, Method method, String clazz) {
        String apiPath = pathJoin(appName, superPath, path);
        for (ApiHttpMethodEnum apiHttpMethodEnum : apiHttpMethodEnums) {
            list.add(
                    ApiRegisterDTO.builder()
                            .produce(produce)
                            .consume(consume)
                            .serviceId(appName)
                            .apiPath(apiPath)
                            .methodName(method.getName())
                            .qualifiedName(clazz)
                            .apiHttpMethodEnum(apiHttpMethodEnum)
                            .build()
            );
        }
    }

    /**
     * 如果Controller类 存在GatewaySpringCloudClient注解，且存在path, 则直接返回, 否则找到Controller类上的 RequestMapping注解获取请求path
     *
     * @param clazz
     * @param beanGatewayClient
     * @return
     */
    private String buildApiSuperPath(final Class<?> clazz, @Nullable final GatewaySpringCloudClient beanGatewayClient) {
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

    private String getMethodPath(String path, String[] paths) {
        if (StringUtils.isNotBlank(path)) {
            return path;
        } else if (ArrayUtils.isEmpty(paths)) {
            return "";
        }
        return "";
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
}
