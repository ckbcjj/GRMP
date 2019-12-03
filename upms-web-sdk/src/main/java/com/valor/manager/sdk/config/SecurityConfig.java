package com.valor.manager.sdk.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valor.manager.sdk.security.SecurityProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private SecurityProvider securityProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置策略
        http.authorizeRequests()
                .antMatchers("/assets/**", "/module/**").permitAll()     // 设置所有人都可以访问登录页面
                .anyRequest().authenticated()          // 任何请求,登录后可以访问
                .and()
                .formLogin().loginPage("/login").permitAll()            //  定义当需要用户登录时候，转到的登录页面。
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                            throws IOException, ServletException {
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(
                                new HashMap<String, Object>() {
                                    {
                                        put("code", HttpStatus.OK.value());
                                        put("msg", "Login Success");
                                    }
                                }));
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
                            throws IOException, ServletException {
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(
                                new HashMap<String, Object>() {
                                    {
                                        put("code", HttpStatus.FORBIDDEN.value());
                                        put("msg", "Login Failed:" + authenticationException.getMessage());
                                    }
                                }));
                    }
                })
                .and().headers().frameOptions().disable()
                .and().logout().permitAll()
                .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(securityProvider);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        //密码加密
        return new BCryptPasswordEncoder(4);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.eraseCredentials(false);
    }

    @Bean
    public HttpFirewall allowUrlEncodedPeriodHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedPeriod(true);
        return firewall;
    }
}
