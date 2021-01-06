package com.tongtech.common.vo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RestResult的工厂类
 */
public class RestResultFactory {
    private static final Logger log = LoggerFactory.getLogger(RestResultFactory.class);

    /**
     * normal
     * @param status
     * @param data
     * @param message
     * @param <T>
     * @return
     */
    public static <T> RestResult<T> createResult(int status, T data, String message) {
        RestResult<T> result = RestResult.newInstance();
        result.setStatus(status);
        result.setData(data);
        result.setMessage(message);

        if (log.isDebugEnabled()) {
            // log.debug("generate rest result:{}", result);
        }
        return result;
    }

    /**
     * success
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RestResult<T> createSuccessResult(T data) {
        return createResult(0, data, null);
    }

    public static <T> RestResult<T> createSuccessResult(T data,String msg) {
        return createResult(0, data, msg);
    }

//    /**
//     * error message
//     * @param message error message
//     * @param <T>
//     * @return
//     */
//    public static <T> RestResult<T> createErrorResult(String message) {
//        return createResult(500, null, message);
//    }

    /**
     * success no data
     * @return
     */
    public static RestResult createSuccessResult() {
        return RestResultFactory.createSuccessResult(null);
    }

    /**
     * error message
     * @param message error message
     * @param <T>
     * @return
     */
    public static <T> RestResult<T> createErrorResult(String message) {
        return createResult(3, null, message);
    }

    /**
     * need login
     */
    public static <T> RestResult<T> createNoLoginResult(String message){
        return createResult(1,null,message);
    }

    public static <T> RestResult<T> createNoAuthorityResult(String message){
        return createResult(2,null,message);
    }
}

