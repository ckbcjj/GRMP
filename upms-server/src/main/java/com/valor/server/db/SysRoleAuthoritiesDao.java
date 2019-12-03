package com.valor.server.db;

import javax.persistence.*;

/**
 * 角色权限关联表
 */
@Entity
@Table(name = "sys_role_authorities")
public class SysRoleAuthoritiesDao extends AbstractLMI {

    private Integer id;  // 主键，也可以用联合主键，根据个人习惯

    private Integer roleId;  // 角色id

    private Integer authorityId;  // 权限id

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "role_id")
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Column(name = "authority_id")
    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }
}