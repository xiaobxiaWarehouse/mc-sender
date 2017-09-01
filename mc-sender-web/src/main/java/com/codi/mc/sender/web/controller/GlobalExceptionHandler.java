package com.codi.mc.sender.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.codi.base.common.Const;
import com.codi.base.domain.BaseResult;
import com.codi.base.exception.BaseAppException;
import com.codi.base.i18n.I18NMgr;
import com.codi.base.util.ExceptionUtil;
import com.codi.base.util.StringUtil;
import com.codi.base.web.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Class used to handle global controller exceptions.
 * <p>
 * Spring MVC build Exceptions, in 3.2.17 Exception HTTP Status Code
 * BindException 400 (Bad Request) ConversionNotSupportedException 500 (Internal
 * Server Error) HttpMediaTypeNotAcceptableException 406 (Not Acceptable)
 * HttpMediaTypeNotSupportedException 415 (Unsupported Media Type)
 * HttpMessageNotReadableException 400 (Bad Request)
 * HttpMessageNotWritableException 500 (Internal Server Error)
 * HttpRequestMethodNotSupportedException 405 (Method Not Allowed)
 * MethodArgumentNotValidException 400 (Bad Request)
 * MissingServletRequestParameterException 400 (Bad Request)
 * MissingServletRequestPartException 400 (Bad Request)
 * NoSuchRequestHandlingMethodException 404 (Not Found) TypeMismatchException
 * 400 (Bad Request)
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = BaseAppException.class)
    public void handleBaseAppException(HttpServletRequest request, HttpServletResponse response, BaseAppException e) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("success", false);
        jsonObj.put("errorCode", e.getCode());

        String desc = e.getDesc();
        if (StringUtil.isEmpty(desc)) {
            desc = I18NMgr.getInstance().getValue(e.getCode());
        }
        jsonObj.put("errorMessage", desc);
        ResponseUtils.renderJson2(request, response, jsonObj, true);
    }


    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    public
    @ResponseBody
    ResponseEntity<?> handleHttpMediaTypeNotAcceptableException(
        HttpMediaTypeNotAcceptableException be) {
        return new ResponseEntity<>(be.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public
    @ResponseBody
    ResponseEntity<?> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException be) {
        return new ResponseEntity<>(be.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = NoSuchRequestHandlingMethodException.class)
    public
    @ResponseBody
    ResponseEntity<?> handleNoSuchRequestHandlingMethodException(
        NoSuchRequestHandlingMethodException be) {
        return new ResponseEntity<>(be.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(BindException.class)
    public
    @ResponseBody
    ResponseEntity<BaseResult> handleBindException(BindException e) {
        BaseResult result = new BaseResult(false, null);
        List<ObjectError> errors = e.getAllErrors();
        Assert.notEmpty(errors);
        ObjectError error = e.getAllErrors().get(0);
        result.setErrorCode("1096");
        result.setErrorMessage(error.getDefaultMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MethodArgumentNotValidException.class,
        MissingServletRequestParameterException.class, MissingServletRequestPartException.class,
        TypeMismatchException.class, ServletRequestBindingException.class})
    public
    @ResponseBody
    ResponseEntity<BaseResult> handleExceptions(Exception e) {
        return new ResponseEntity<>(buildBaseResult(Const.ERROR_INVALID_REQUEST, e.getMessage()),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConversionNotSupportedException.class, HttpMessageNotWritableException.class,
        Exception.class})
    public
    @ResponseBody
    ResponseEntity<BaseResult> handleException(Exception e) {
        ExceptionUtil.logError(logger, "Unexpected exceptions!!!", e);
        return new ResponseEntity<>(buildBaseResult(Const.ERROR_SYS_EXCEPTION, null),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private BaseResult buildBaseResult(int errorCode, String errorMsg) {
        BaseResult result = new BaseResult();
        result.setSuccess(false);
        result.setErrorCode("" + errorCode);
        if (errorMsg == null) {
            String errorMessage = I18NMgr.getInstance().getValue("" + errorCode);
            result.setErrorMessage(errorMessage);
        } else {
            result.setErrorMessage(errorMsg);
        }
        return result;
    }

}
