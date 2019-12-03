package com.valor.model.web.response;


/**
 * @author Kemp.Cheng
 * created on: 2019/11/11
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class SysRoleResponse extends UpmsManageReponse {

    private int roleId;

    private String roleName;

    private String comments;

    private String sourceCode; //系统code

    private boolean isSuper; //超级角色

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public boolean getIsSuper() {
        return isSuper;
    }

    public void setIsSuper(boolean aSuper) {
        isSuper = aSuper;
    }
}
