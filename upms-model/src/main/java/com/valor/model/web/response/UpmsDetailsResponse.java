package com.valor.model.web.response;

import java.util.Set;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/19
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class UpmsDetailsResponse {

    private SysUserResponse upmsUserResponse;
    private Set<SysAuthorityResponse> upmsAuthResponses;

    public SysUserResponse getUpmsUserResponse() {
        return upmsUserResponse;
    }

    public void setUpmsUserResponse(SysUserResponse upmsUserResponse) {
        this.upmsUserResponse = upmsUserResponse;
    }

    public Set<SysAuthorityResponse> getUpmsAuthResponses() {
        return upmsAuthResponses;
    }

    public void setUpmsAuthResponses(Set<SysAuthorityResponse> upmsAuthResponses) {
        this.upmsAuthResponses = upmsAuthResponses;
    }
}
