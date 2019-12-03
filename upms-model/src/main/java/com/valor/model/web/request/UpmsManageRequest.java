package com.valor.model.web.request;

import com.valor.model.supperclz.AbstractPrintable;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/11
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public abstract class UpmsManageRequest extends AbstractPrintable {
    private static final long serialVersionUID = 4458781015217475894L;
    private String appId;
    private String appVer;
    private String clientIp;
    private String checkSum;
    private long appVerCode;
    private long requestTime;
    private String reqUserName;
    private int pageIndex = 1;
    private int pageSize = Integer.MAX_VALUE;
    private String sortProp;
    private boolean ascending = true;
    private RequestType requestType = RequestType.UNKNOWN;
    private int subTag=0; // 0为默认操作

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

    public long getAppVerCode() {
        return appVerCode;
    }

    public void setAppVerCode(long appVerCode) {
        this.appVerCode = appVerCode;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortProp() {
        return sortProp;
    }

    public void setSortProp(String sortProp) {
        this.sortProp = sortProp;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public String getReqUserName() {
        return reqUserName;
    }

    public void setReqUserName(String reqUserName) {
        this.reqUserName = reqUserName;
    }

    public int getSubTag() {
        return subTag;
    }

    public void setSubTag(int subTag) {
        this.subTag = subTag;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }
}
