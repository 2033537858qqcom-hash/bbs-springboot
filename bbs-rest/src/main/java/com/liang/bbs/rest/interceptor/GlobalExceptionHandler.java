package com.liang.bbs.rest.interceptor;


import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.web.basic.ResponseResult;
import com.liang.nansheng.common.web.exception.BusinessException;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 鍏ㄥ眬寮傚父澶勭悊绋嬪簭
 *
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String BUSINESS_CODE_REG = "\\(code=(.*), desc=";
    private static final Pattern BUSINESS_CODE_PATTERN = Pattern.compile(BUSINESS_CODE_REG);

    /**
     * 涓氬姟寮傚父
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseResult<String> businessExceptionHandler(HttpServletRequest req, BusinessException e) {
        log.error("涓氬姟寮傚父:", e);
        return ResponseResult.<String>builder().code(e.getResponseCode().getCode()).desc(e.getMessage()).build();
    }

    /**
     * dubbo灏嗘墍鏈夋墽琛屽紓甯稿寘瑁呭埌ExecutionException涓?
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = ExecutionException.class)
    @ResponseBody
    public ResponseResult<String> businessExceptionHandler(HttpServletRequest req, ExecutionException e) {
        String message = e.getMessage();
        log.error("杩滅▼璋冪敤鍑虹幇寮傚父:", e);
        if (StringUtils.isNotEmpty(message) && message.contains("BusinessException")) {
            Matcher mat = BUSINESS_CODE_PATTERN.matcher(message);
            if (mat.find()) {
                ResponseCode responseCode = ResponseCode.getByCode(Integer.valueOf(mat.group(1)));
                assert responseCode != null;
                return ResponseResult.build(responseCode, "");
            }
        }
        return ResponseResult.<String>builder().code(ResponseCode.RPC_EXCEPTION.getCode()).desc(ResponseCode.RPC_EXCEPTION.getDesc()).build();
    }

    /**
     * 鍙傛暟鏍￠獙寮傚父
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResponseResult<String> defaultHandler(HttpServletRequest req, BindException e) {
        log.error("鍙傛暟鏍￠獙寮傚父:", e);
        return ResponseResult.<String>builder().code(ResponseCode.BIND_EXCEPTION.getCode()).desc(ResponseCode.BIND_EXCEPTION.getDesc()).build();
    }

    /**
     * 鏂规硶鍙傛暟鏃犳晥寮傚父
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult<String> defaultHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        log.error("鏂规硶鍙傛暟鏃犳晥寮傚父:", e);
        return ResponseResult.<String>builder().code(ResponseCode.RPC_EXCEPTION.getCode()).desc(ResponseCode.RPC_EXCEPTION.getDesc()).build();
    }

    /**
     * 缂哄皯璇锋眰鍙傛暟寮傚父
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseResult<String> defaultHandler(HttpServletRequest req, MissingServletRequestParameterException e) {
        log.error("缂哄皯璇锋眰鍙傛暟寮傚父", e);
        return ResponseResult.<String>builder().code(ResponseCode.MISSING_PARAMETER_EXCEPTION.getCode()).desc(ResponseCode.MISSING_PARAMETER_EXCEPTION.getDesc()).build();
    }

    /**
     * RPC寮傚父
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = FeignException.class)
    @ResponseBody
    public ResponseResult<String> defaultHandler(HttpServletRequest req, FeignException e) {
        log.error("RPC寮傚父:", e);
        return ResponseResult.<String>builder().code(ResponseCode.RPC_EXCEPTION.getCode()).desc(ResponseCode.RPC_EXCEPTION.getDesc()).build();
    }

    /**
     * 鍏跺畠寮傚父
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult<String> defaultHandler(HttpServletRequest req, Exception e) {
        if (e instanceof ExecutionException || e instanceof InterruptedException) {
            return businessExceptionHandler(req, new ExecutionException(e));
        } else {
            log.error("鍏跺畠寮傚父:", e);
            return ResponseResult.<String>builder().code(ResponseCode.SYSTEM_EXCEPTION.getCode()).desc(ResponseCode.SYSTEM_EXCEPTION.getDesc()).build();

        }

    }

}
