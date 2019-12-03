package com.valor.model.web.response;

/**
 * @author Kemp.Cheng
 * created on: 2019/12/2
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class SysSourceResponse extends UpmsManageReponse {

    private String sourceCode;

    private String sourceName;

    private boolean status;

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
