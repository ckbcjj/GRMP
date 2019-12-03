package com.valor.model.web.response;

import com.valor.model.exception.UpmsHttpCode;

import java.util.HashMap;

/**
 * 返回结果对象
 *
 * @author wangfan
 * @date 2017-6-10 上午10:10:07
 */
public class JsonResultResponse extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    private JsonResultResponse() {
    }

    /**
     * 返回成功
     */
    public static JsonResultResponse ok() {
        return ok("Success");
    }

    /**
     * 返回成功
     */
    public static JsonResultResponse ok(String message) {
        return ok(200, message);
    }

    /**
     * 返回成功
     */
    public static JsonResultResponse ok(int code, String message) {
        JsonResultResponse jsonResult = new JsonResultResponse();
        jsonResult.put("code", code);
        jsonResult.put("msg", message);
        return jsonResult;
    }

    /**
     * 返回失败
     */
    public static JsonResultResponse error() {
        return error("Failed");
    }

    /**
     * 返回失败
     */
    public static JsonResultResponse error(String messag) {
        return error(UpmsHttpCode.WEB_ERROR, messag);
    }

    /**
     * 返回失败
     */
    public static JsonResultResponse error(int code, String message) {
        return ok(code, message);
    }

    /**
     * 设置code
     */
    public JsonResultResponse setCode(int code) {
        super.put("code", code);
        return this;
    }

    /**
     * 设置message
     */
    public JsonResultResponse setMessage(String message) {
        super.put("msg", message);
        return this;
    }

    /**
     * 放入object
     */
    @Override
    public JsonResultResponse put(String key, Object object) {
        super.put(key, object);
        return this;
    }
}