package com.valor.manager.sdk.security;

import com.valor.manager.sdk.service.UpmsUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/11
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
@Service
public class SecurityProvider implements AuthenticationProvider {

    @Autowired
    private UpmsUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authenticate) throws AuthenticationException {
        try {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authenticate;
            String username = token.getName();
            UpmsUserDetails userDetails = (UpmsUserDetails) userDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                throw new UsernameNotFoundException("user is not exist");
            }
            if (!userDetails.isEnabled() || !userDetails.isAccountNonLocked() || !userDetails.isAccountNonExpired()) {
                throw new LockedException("account has locked");
            }
            if (!new BCryptPasswordEncoder(4).matches(token.getCredentials().toString(), userDetails.getPassword())) {
                throw new BadCredentialsException("password incorrect");
            }
            return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
