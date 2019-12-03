package com.valor.manager.sdk.service;

import com.mfc.config.ConfigTools3;
import com.valor.manager.sdk.security.UpmsUserDetails;
import com.valor.manager.sdk.security.UserAccess;
import com.valor.manager.sdk.web.request.AuthRequest;
import com.valor.manager.sdk.web.response.UpmsDetailsResponse;
import com.valor.manager.sdk.web.response.UpmsResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/11
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
@Service
public class UpmsUserDetailsService implements UserDetailsService {

    @Autowired
    private UpmsService upmsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (UserAccess.getLoginUser() != null) {
            throw new AuthenticationServiceException("User has been authenticated");
        }
        UserDetails userDetails = null;
        String sourceCode = ConfigTools3.getConfigAsString("upms.source");
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(sourceCode)) {
            AuthRequest request = new AuthRequest();
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String clientIp = upmsService.getRemoteHost(req);
            request.setUserName(username);
            request.setSourceCode(sourceCode);
            request.setClientIp(clientIp);
            request.setReqUserName(username);
            UpmsResponse<UpmsDetailsResponse> response = upmsService.excuteReq(upmsService.getApiService().upmsAuths(request));
            if ("0".equals(response.getStatus())) {
                UpmsDetailsResponse detailsResponse = response.getResult();
                if (detailsResponse.getUpmsUserResponse() != null) {
                    userDetails = new UpmsUserDetails(detailsResponse.getUpmsUserResponse(), detailsResponse.getUpmsAuthResponses());
                }
            } else {
                throw new InternalAuthenticationServiceException(response.getMsg());
            }
        }
        return userDetails;
    }
}
