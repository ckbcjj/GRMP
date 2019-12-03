package com.valor.model.web.response;


/**
 * @author Kemp.Cheng
 * created on: 2019/11/11
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class SysAuthorityResponse extends UpmsManageReponse {

    private Integer authorityId;  // 权限id

    private String authorityName;  // 权限名称

    private String authority;  // 权限标识（权限值）

    private String accessUrl;  //  访问 url

    private Integer parentId;  // 上级权限

    private String authType;  //  权限类别字典码—— 菜单、页面元素、数据权限项、接口

    private Integer orderNumber;  // 排序号

    private String authIcon;  // 权限图标

    private String sourceCode; //系统code

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
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

    public String getAuthIcon() {
        return authIcon;
    }

    public void setAuthIcon(String authIcon) {
        this.authIcon = authIcon;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
}
