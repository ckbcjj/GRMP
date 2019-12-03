package com.valor.model.web.request;

/**
 * @author Kemp.Cheng
 * created on: 2019/12/2
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public class SysSourceRequest extends UpmsManageRequest {

    private static final long serialVersionUID = -8242864007309450595L;

    private String sourceCode;

    private String sourceName;

    private Boolean status;

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
