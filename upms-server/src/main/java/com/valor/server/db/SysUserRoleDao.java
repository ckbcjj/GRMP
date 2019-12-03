package com.valor.server.db;

import javax.persistence.*;

@Entity
@Table(name="sys_user_role")
public class SysUserRoleDao extends AbstractLMI {

    private Integer id;  // 主键

    private Integer userId;  // 用户id

    private Integer roleId;  // 角色id

    private String roleName;  // 角色名称

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "role_id")
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Transient
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}