package com.tongtech.common.vo;


import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * 仅仅当执行成功时，使用此类返回结果；
 * 自己写的代码中不处理异常，只抛出异常，使用Spring自带的异常处理机制处理异常；
 * Spring默认异常返回结果为：
 * {
 "timestamp": "2018-11-15T01:42:43.931+0000",
 "status": 404,
 "error": "Not Found",
 "message": "No message available",
 "path": "/api/form/items"
 }
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RestResult<T> {
    private Integer status;//与Spring默认返回的json一致

    private T data;//执行成功时返回的数据

    private RestResult() {}

    private String message;

    public static <T> RestResult<T> newInstance() {
        return new RestResult<>();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RestResult{" +
                "result=" + status +
                ", data=" + data +
                ", message="+message+
                '}';
    }
}
