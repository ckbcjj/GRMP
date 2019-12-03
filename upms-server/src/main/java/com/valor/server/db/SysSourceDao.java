package com.valor.server.db;

import javax.persistence.*;

/**
 * @author Kemp.Cheng
 * created on: 2019/12/2
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
@Entity
@Table(name = "sys_source")
public class SysSourceDao extends AbstractLMI {

    private String sourceCode;

    private String sourceName;

    private Boolean status;

    @Id
    @Column(name = "source_code", length = 20)
    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    @Column(name = "source_name", length = 32)
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Column(name = "status")
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
