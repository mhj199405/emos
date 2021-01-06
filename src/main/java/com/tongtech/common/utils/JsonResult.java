package com.tongtech.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.security.SignatureException;

public class JsonResult {

    /**
     * 响应业务状态 0:正常，1：需要登录，2：无权限，3：错误,4:其他
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
    private Integer status;

    /**
     * 响应消息
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
    private String message;


    /**
     * 响应中的数据
     */
    private Object data;

    public String getToken() {
        return token;
    }


    /**
     * token处理
     */
    private String token;

    /**
     * 构造JsonResult对象
     *
     * @param status
     * @param msg
     * @param data
     * @return
     */
    public static JsonResult build(Integer status, String msg, Object data) {
        return new JsonResult(status, msg, data);
    }

    public static JsonResult normal(Object data) {
        return new JsonResult(0, "操作成功", data);
    }

    public static JsonResult normal(String msg, Object data) {
        return new JsonResult(0, msg, data);
    }

    public static JsonResult error(String msg) {
        return new JsonResult(3, msg, null);
    }

    public static JsonResult error(String msg, Object data) {
        return new JsonResult(3, msg, data);
    }

    public static JsonResult login(String msg) {
        return new JsonResult(1, msg, null);
    }

    public static JsonResult login(String msg, Object data) {
        return new JsonResult(1, msg, data);
    }

    public static JsonResult token(String msg) {
        return new JsonResult(msg);
    }

    public static JsonResult token(String msg, Object data) {
        return new JsonResult(msg, data);
    }

    public static JsonResult other(String msg) {
        return new JsonResult(4, msg, null);
    }

    public static JsonResult other(String msg, Object data) {
        return new JsonResult(4, msg, data);
    }

    public static JsonResult noAuthority(String msg) {
        return new JsonResult(2, msg, null);
    }

    public static JsonResult noAuthority(String msg, Object data) {
        return new JsonResult(2, msg, data);
    }

    public JsonResult() {
    }


    public JsonResult(Integer status, String msg, Object data) {
        this.status = status;
        this.message = msg;
        this.data = data;
        refreshToken();
    }

    public JsonResult(String token) {
        this.token = token;
        this.status = 0;
    }

    public JsonResult(String token, Object data) {
        this.token = token;
        this.data = data;
        this.status = 0;
    }

    public JsonResult(Object data)  {
        this.status = 0;
        this.data = data;
        refreshToken();
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return message;
    }

    public Object getData() {
        return data;
    }

    private void refreshToken()  {
        if (token != null) {
            return;
        }
        if (this.status != 0) {
            return;
        }
        String refresh = null;
        try {
            refresh = JwtResult.build().refresh();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        if (refresh != null) {
            this.token = refresh;
        }
    }

    /**
     * @param json
     * @return
     * @Description: 没有object对象的转化
     * @author
     * @date 2016年4月22日 下午8:35:21
     */
    public static JsonResult format(String json) {
        try {
            return JSON.parseObject(json, JsonResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String ToJson() {
        return JSON.toJSONString(this, SerializerFeature.WriteNullListAsEmpty);
    }

}
