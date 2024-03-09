package com.ahcloud.common.validator;


import com.ahcloud.common.annotation.EnumValid;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @program: permissions-center
 * @description: 枚举校验
 * @author: YuKai Fan
 * @create: 2021-01-23 22:23
 **/
public class EnumValidator implements ConstraintValidator<EnumValid, Object> {

    private Class<? extends Enum<?>> enumClass;

    private String enumMethod;

    @Override
    public void initialize(EnumValid enumValid) {
        enumMethod = enumValid.enumMethod();
        enumClass = enumValid.enumClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(value)) {
            return false;
        }
        if (StringUtils.isEmpty(enumMethod) || enumClass == null) {
            return false;
        }

        Class<?> valueClass = value.getClass();
        try {
            Method method = enumClass.getMethod(enumMethod, valueClass);
            if (!Boolean.TYPE.equals(method.getReturnType())
                    && !Boolean.class.equals(method.getReturnType())) {
                throw new RuntimeException(String.format("%s method return is not boolean type in the %s class", enumMethod, enumClass));
            }
            Boolean result = (Boolean) method.invoke(null, value);
            return result != null && result;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("This %s(%s) method does not exist in the %s", enumMethod, valueClass, enumClass), e);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
