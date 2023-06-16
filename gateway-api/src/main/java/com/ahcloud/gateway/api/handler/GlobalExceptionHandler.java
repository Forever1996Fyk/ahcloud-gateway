package com.ahcloud.gateway.api.handler;

import com.ahcloud.common.result.ResponseResult;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.client.exception.BizException;
import com.ahcloud.gateway.client.exception.GatewayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Description 全局异常处理
 * @Author yin.jinbiao
 * @Date 2021/10/1 15:00
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 上传大小超出限制
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseResult<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return ResponseResult.ofFailed(GatewayRetCodeEnum.PARAM_ILLEGAL.getCode(), GatewayRetCodeEnum.PARAM_ILLEGAL.getMessage());
    }


    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BizException.class)
    public ResponseResult<Void> handleCustomException(BizException e) {
        return ResponseResult.ofFailed(e.getErrorCode().getCode(), e.getErrorMessage());
    }

    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(GatewayException.class)
    public ResponseResult<Void> handleGatewayException(GatewayException e) {
        return ResponseResult.ofFailed(e.getErrorCode().getCode(), e.getMessage());
    }

    /**
     * Spring校验 表单参数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult<Void> constraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> cves = e.getConstraintViolations();
        StringBuilder errorMsg = new StringBuilder();
        cves.forEach(ex -> errorMsg.append(ex.getMessage()).append("; "));
        return ResponseResult.ofFailed(GatewayRetCodeEnum.PARAM_ILLEGAL.getCode(), errorMsg.toString());
    }

    /**
     * Spring校验 json参数异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseResult<Void> methodArgumentNotValidException(Exception ex) {
        MethodArgumentNotValidException c = (MethodArgumentNotValidException) ex;
        List<ObjectError> errors = c.getBindingResult().getAllErrors();
        StringBuffer errorMsg = new StringBuffer();
        errors.stream().forEach(x -> {
            errorMsg.append(x.getDefaultMessage()).append(";");
        });
        return ResponseResult.ofFailed(GatewayRetCodeEnum.PARAM_ILLEGAL.getCode(), errorMsg.toString());
    }


    /**
     * Spring请求验证参数
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ResponseResult<Void> handleBindException(BindException ex) {
        // ex.getFieldError():随机返回一个对象属性的异常信息。如果要一次性返回所有对象属性异常信息，则调用ex.getAllErrors()
        FieldError fieldError = ex.getFieldError();
        String errorMsg = GatewayRetCodeEnum.PARAM_ILLEGAL.getMessage();
        if (Objects.nonNull(fieldError)) {
            errorMsg = fieldError.getDefaultMessage();
        }
        // 生成返回结果
        return ResponseResult.ofFailed(GatewayRetCodeEnum.PARAM_ILLEGAL.getCode(), errorMsg);
    }

}
