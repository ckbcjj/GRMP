package com.valor.manager.sdk.security;

import com.valor.manager.sdk.model.SysAuth;
import com.valor.manager.sdk.model.SysUser;
import com.valor.manager.sdk.web.response.UpmsAuthResponse;
import com.valor.manager.sdk.web.response.UpmsUserResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

public class UpmsUserDetails implements UserDetails {

    private SysUser user;

    private Map<String, List<SysAuth>> sysAuthMap = new HashMap<>();

    private Set<SimpleGrantedAuthority> authorities = new HashSet<>();

    public UpmsUserDetails(UpmsUserResponse userResponse, Set<UpmsAuthResponse> authorityResponses) {
        this.user = userConvert(userResponse);
        if (authorityResponses != null) {
            for (UpmsAuthResponse auth : authorityResponses) {
                if (!StringUtils.isEmpty(auth.getAuthority())) {
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(auth.getAuthority());
                    authorities.add(simpleGrantedAuthority);
                }
                sysAuthMap.putIfAbsent(auth.getAuthType(), new ArrayList<>());
                sysAuthMap.get(auth.getAuthType()).add(autConvert(auth));
            }
        }
    }

    public SysUser getUser() {
        return user;
    }

    public Map<String, List<SysAuth>> getSysAuthMap() {
        return sysAuthMap;
    }

    private SysAuth autConvert(UpmsAuthResponse response) {
        SysAuth authorityDetail = new SysAuth();
        if (response != null) {
            authorityDetail.setId(response.getAuthorityId());
            authorityDetail.setName(response.getAuthorityName());
            authorityDetail.setContent(response.getAuthority());
            authorityDetail.setActionUrl(response.getAccessUrl());
            authorityDetail.setAuthIcon(response.getAuthIcon());
            authorityDetail.setOrderNumber(response.getOrderNumber());
            authorityDetail.setParentId(response.getParentId());
        }
        return authorityDetail;
    }

    private SysUser userConvert(UpmsUserResponse response) {
        SysUser sysUser = new SysUser();
        if (response != null) {
            sysUser.setId(response.getUserId());
            sysUser.setUserName(response.getUserName());
            sysUser.setPassword(response.getPassword());
            sysUser.setNickName(response.getNickName());
            sysUser.setAvatar(response.getAvatar());
            sysUser.setSex(response.getSex());
            sysUser.setState(response.getState());
        }
        return sysUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getState() == 0 ? true : false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getState() == 0 ? true : false;
    }
}
