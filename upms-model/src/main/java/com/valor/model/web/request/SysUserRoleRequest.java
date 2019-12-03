package com.valor.model.web.request;

import java.util.List;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/12
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class SysUserRoleRequest extends UpmsManageRequest {

    private static final long serialVersionUID = 7125556067895576212L;

    private Integer userId;
    private List<Integer> roleIds;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}
