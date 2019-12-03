package com.valor.server.service;

import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.*;
import com.valor.model.web.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UpmsDetailsService {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysSourceService sourceService;

    @Autowired
    private SysAuthoritiesService authoritiesService;

    public UpmsDetailsResponse upmsDetails(AuthRequest args) throws APIException {

        List<SysSourceResponse> sourceCollection = sourceService.query(new SysSourceRequest() {{
            setSourceCode(args.getSourceCode());
        }}).getData();
        if (sourceCollection.size() == 0) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_SOURCE_NOT_EXIST, UpmsHttpCode.ERR_SOURCE_NOT_EXIST_MSG);
        }

        SysSourceResponse sourceResponse = sourceCollection.get(0);
        if (!sourceResponse.getStatus()) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_SOURCE_UNAVALIABLE, UpmsHttpCode.ERR_SOURCE_UNAVALIABLE_MSG);
        }

        List<SysUserResponse> userCollection = userService.query(new SysUserRequest() {{
            setUserName(args.getUserName());
        }}).getData();
        if (userCollection.size() == 0) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_USER_NOT_EXIST, UpmsHttpCode.ERR_USER_NOT_EXIST_MSG);
        }

        UpmsDetailsResponse response = new UpmsDetailsResponse();
        response.setUpmsUserResponse(userCollection.get(0));

        Set<SysAuthorityResponse> authorityResponseSet = new HashSet<>();
        List<SysRoleResponse> roleList = roleService.getRoles(new SysUserRoleRequest() {{
            setUserId(userCollection.get(0).getUserId());
        }}).getData();
        for (SysRoleResponse role : roleList) {
            if (role.getSourceCode().equals(args.getSourceCode())) {
                List<SysAuthorityResponse> list = authoritiesService.getAuths(new SysRoleAuthRequest() {{
                    setRoleId(role.getRoleId());
                }}).getData();
                authorityResponseSet.addAll(list);
            }
        }

        response.setUpmsAuthResponses(authorityResponseSet);
        return response;
    }

}
