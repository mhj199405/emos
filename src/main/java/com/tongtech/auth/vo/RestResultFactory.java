package com.tongtech.auth.vo;

public class RestResultFactory {

    /**
     * 使用泛型方法来确定最终的结果
     * @param data
     * @param <T>
     * @return
     */
    public static<T> RestResult<T>  createSuccessResult(T data){
        RestResult<T> tRestResult = new RestResult<>();
        tRestResult.setData(data);
        tRestResult.setStatus(0);
        tRestResult.setMessage("");
        return tRestResult;
    }

    /**
     * 当执行失败的情况下，也会返回一个结果，只不过结果是一个空字符串，或者一个提示信息
     * @param data
     * @param <T>
     * @return
     */
    public static<T> RestResult<T>  createFailedResult(T data){
        RestResult<T> tRestResult = new RestResult<>();
        tRestResult.setData(data);
        tRestResult.setStatus(3);
        tRestResult.setMessage("内部错误");
        return tRestResult;
    }

}
