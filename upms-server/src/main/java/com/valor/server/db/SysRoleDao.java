package com.valor.server.db;

import javax.persistence.*;

/**
 * 角色表
 */
@Entity
@Table(name = "sys_role")
public class SysRoleDao extends AbstractLMI {

    private Integer roleId;  // 角色id

    private String roleName;  // 角色名称

    private String comments;  // 描述

    private String sourceCode; //系统code

    private boolean isSuper; //超级角色

    public SysRoleDao(Integer roleId) {
        this.roleId = roleId;
    }

    public SysRoleDao(Integer roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public SysRoleDao() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Column(name = "role_name", length = 20)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    @Column(name = "comments", length = 100)
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments == null ? null : comments.trim();
    }

    @Column(name = "source_code", length = 32)
    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    @Column(name = "is_super")
    public boolean getIsSuper() {
        return isSuper;
    }

    public void setIsSuper(boolean aSuper) {
        isSuper = aSuper;
    }
}