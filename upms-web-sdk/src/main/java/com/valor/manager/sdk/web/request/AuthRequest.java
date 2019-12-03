package com.valor.manager.sdk.web.request;

import java.io.Serializable;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/15
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class AuthRequest implements Serializable {

    private static final long serialVersionUID = -1260335615897892611L;

    private String userName;
    private String sourceCode;
    private String appId;
    private String appVer;
    private long appVerCode;
    private String clientIp;
    private String checkSum;
    private long requestTime;
    private String reqUserName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public long getAppVerCode() {
        return appVerCode;
    }

    public void setAppVerCode(long appVerCode) {
        this.appVerCode = appVerCode;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public String getReqUserName() {
        return reqUserName;
    }

    public void setReqUserName(String reqUserName) {
        this.reqUserName = reqUserName;
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "userName='" + userName + '\'' +
                ", sourceCode='" + sourceCode + '\'' +
                ", appId='" + appId + '\'' +
                ", appVer='" + appVer + '\'' +
                ", appVerCode=" + appVerCode +
                ", clientIp='" + clientIp + '\'' +
                ", checkSum='" + checkSum + '\'' +
                ", requestTime=" + requestTime +
                ", reqUserName='" + reqUserName + '\'' +
                '}';
    }
}
