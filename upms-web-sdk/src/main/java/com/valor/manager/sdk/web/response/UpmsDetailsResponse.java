package com.valor.manager.sdk.web.response;

import java.util.Set;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/19
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class UpmsDetailsResponse {

    private UpmsUserResponse upmsUserResponse;
    private Set<UpmsAuthResponse> upmsAuthResponses;

    public UpmsUserResponse getUpmsUserResponse() {
        return upmsUserResponse;
    }

    public void setUpmsUserResponse(UpmsUserResponse upmsUserResponse) {
        this.upmsUserResponse = upmsUserResponse;
    }

    public Set<UpmsAuthResponse> getUpmsAuthResponses() {
        return upmsAuthResponses;
    }

    public void setUpmsAuthResponses(Set<UpmsAuthResponse> upmsAuthResponses) {
        this.upmsAuthResponses = upmsAuthResponses;
    }
}
