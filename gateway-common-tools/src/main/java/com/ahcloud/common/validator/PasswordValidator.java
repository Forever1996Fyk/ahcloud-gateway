package com.ahcloud.common.validator;


import com.ahcloud.common.annotation.PasswordValid;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.invoke.MethodHandles;
import java.util.regex.Pattern;

/**
 * @program: permissions-center
 * @description:
 * @author: YuKai Fan
 * @create: 2021-01-23 22:23
 **/
public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {
    private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());
    private int min;
    private int max;

    private String repEx;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        return s.length() >= min && s.length() <= max && validateRep(s);
    }


    @Override
    public void initialize(PasswordValid passwordValid) {
        min = passwordValid.min();
        max = passwordValid.max();
        repEx = passwordValid.repEx();
        this.validateParameters();
    }

    private void validateParameters() {
        if (this.min < 0) {
            throw LOG.getMinCannotBeNegativeException();
        } else if (this.max < 0) {
            throw LOG.getMaxCannotBeNegativeException();
        } else if (this.max < this.min) {
            throw LOG.getLengthCannotBeNegativeException();
        }
    }

    private boolean validateRep(String value) {
        if (StringUtils.isBlank(this.repEx)) {
            return true;
        }
        Pattern p = Pattern.compile(value);
        return p.matcher(value).matches();
    }
}
