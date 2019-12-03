package com.valor.server.db;

import javax.persistence.*;
import java.util.List;

/**
 * 用户表
 */
@Entity
@Table(name = "sys_user")
public class SysUserDao extends AbstractLMI {
    private Integer userId;  // 主键id

    private String userName;  // 账号

    private String password;  // 密码

    private String nickName;  // 昵称

    private String avatar;  // 头像

    private String sex;  // 性别

    private String phone;  // 手机号

    private String email;  // 邮箱

    private boolean emailVerified;  // 邮箱是否验证

    private Integer state = 0;  // 用户状态，0正常（不同于软删除，此处所有非0为异常状态），1锁定 ...

    private boolean isSuper;

    private List<SysRoleDao> sysRoleDaos;  //角色

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "username", length = 20)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    @Column(name = "password", length = 128)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "nick_name", length = 20)
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Column(name = "avatar", length = 200)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Column(name = "sex", length = 6)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Column(name = "phone", length = 12)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "email", length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "email_verified")
    public boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Column(name = "state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name = "is_super")
    public boolean getIsSuper() {
        return isSuper;
    }

    public void setIsSuper(boolean aSuper) {
        isSuper = aSuper;
    }

    @Transient
    public List<SysRoleDao> getSysRoleDaos() {
        return sysRoleDaos;
    }

    public void setSysRoleDaos(List<SysRoleDao> sysRoleDaos) {
        this.sysRoleDaos = sysRoleDaos;
    }
}
