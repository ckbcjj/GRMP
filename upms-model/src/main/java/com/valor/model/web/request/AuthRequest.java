package com.valor.model.web.request;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/15
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class AuthRequest extends UpmsManageRequest {

    private static final long serialVersionUID = -8344140907836397160L;

    private String userName;

    private String sourceCode;

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
