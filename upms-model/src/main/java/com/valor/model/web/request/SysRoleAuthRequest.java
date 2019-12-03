package com.valor.model.web.request;

import java.util.List;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/12
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class SysRoleAuthRequest extends UpmsManageRequest {
    private static final long serialVersionUID = 1458524998291741430L;

    private Integer roleId;

    private List<Integer> authIds;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<Integer> getAuthIds() {
        return authIds;
    }

    public void setAuthIds(List<Integer> authIds) {
        this.authIds = authIds;
    }
}
