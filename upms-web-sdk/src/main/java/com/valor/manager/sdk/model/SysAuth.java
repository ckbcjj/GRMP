package com.valor.manager.sdk.model;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/15
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class SysAuth {

    private int id;

    private String name;

    private String content;

    private String actionUrl;

    private String authIcon;

    private int parentId;

    private int orderNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getAuthIcon() {
        return authIcon;
    }

    public void setAuthIcon(String authIcon) {
        this.authIcon = authIcon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
