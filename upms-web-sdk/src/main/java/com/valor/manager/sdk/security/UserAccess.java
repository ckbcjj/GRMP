package com.valor.manager.sdk.security;

import com.valor.manager.sdk.model.SysAuth;
import com.valor.manager.sdk.model.SysUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

public class UserAccess {

    /**
     * 获取当前登录的user
     */
    public static SysUser getLoginUser() {
        UpmsUserDetails details = getAuthenticatedData();
        return details != null ? details.getUser() : null;
    }

    public static Map<String, List<SysAuth>> getSysAuthMap() {
        UpmsUserDetails details = getAuthenticatedData();
        return details != null ? details.getSysAuthMap() : null;
    }

    private static UpmsUserDetails getAuthenticatedData() {
        UpmsUserDetails userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if (token.isAuthenticated()) {
                userDetails = (UpmsUserDetails) token.getPrincipal();
            }
        }
        return userDetails;
    }
}
