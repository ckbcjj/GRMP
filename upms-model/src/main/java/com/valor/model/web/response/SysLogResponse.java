package com.valor.model.web.response;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/11
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class SysLogResponse extends UpmsManageReponse{

    private long id;
    private String clientIp;
    private String requestUrl;
    private String operateUser;
    private long operatingTime;
    private String reqDetail;
    private String respDetail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public long getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(long operatingTime) {
        this.operatingTime = operatingTime;
    }

    public String getReqDetail() {
        return reqDetail;
    }

    public void setReqDetail(String reqDetail) {
        this.reqDetail = reqDetail;
    }

    public String getRespDetail() {
        return respDetail;
    }

    public void setRespDetail(String respDetail) {
        this.respDetail = respDetail;
    }
}
