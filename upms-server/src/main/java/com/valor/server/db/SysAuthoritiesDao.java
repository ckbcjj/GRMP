package com.valor.server.db;

import javax.persistence.*;

/**
 * 权限表
 */
@Entity
@Table(name = "sys_authorities")
public class SysAuthoritiesDao extends AbstractLMI {

    private Integer authorityId;  // 权限id

    private String authorityName;  // 权限名称

    private String authority;  // 权限标识（权限值）

    private String accessUrl;  //  访问 url

    private Integer parentId;  // 上级权限

    private String authType;  //  权限类别字典码—— 菜单、页面元素、数据权限项、接口

    private Integer orderNumber;  // 排序号

    private String authIcon;  // 权限图标

    private String sourceCode; //系统code

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    @Column(name = "authority", length = 120)
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Column(name = "access_url", length = 120)
    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    @Column(name = "authority_name", length = 32)
    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    @Column(name = "parent_id")
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Column(name = "order_number")
    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Column(name = "auth_icon", length = 40)
    public String getAuthIcon() {
        return authIcon;
    }

    public void setAuthIcon(String authIcon) {
        this.authIcon = authIcon;
    }

    @Column(name = "source_code", length = 32)
    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    @Column(name = "auth_type", length = 20)
    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
}
