package com.valor.server.controller;

import com.valor.server.aop.UpmsApiCall;
import com.valor.server.aop.UpmsLogCall;
import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysRoleAuthRequest;
import com.valor.model.web.request.SysRoleRequest;
import com.valor.model.web.request.SysUserRoleRequest;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysRoleResponse;
import com.valor.model.web.response.UpmsResponse;
import com.valor.server.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/11
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */

@Controller
@RequestMapping(value = "/api/upms/role")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;

    @UpmsApiCall
    @UpmsLogCall
    @RequestMapping(value = "query/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<PageResultResponse<SysRoleResponse>> query(HttpServletRequest request, HttpServletResponse response, @RequestBody SysRoleRequest args) throws APIException {
        if (args.getRequestType() != RequestType.QUERY) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        return new UpmsResponse(roleService.query(args));
    }

    @UpmsApiCall
    @UpmsLogCall(openLog = true)
    @RequestMapping(value = "update/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<Boolean> update(HttpServletRequest request, HttpServletResponse response, @RequestBody SysRoleRequest args) throws APIException {
        if (args.getRequestType() != RequestType.CREATE && args.getRequestType() != RequestType.UPDATE) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        return new UpmsResponse(roleService.update(args));
    }

    @UpmsApiCall
    @UpmsLogCall
    @RequestMapping(value = "getRoles/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<PageResultResponse<SysRoleResponse>> getRoles(HttpServletRequest request, HttpServletResponse response, @RequestBody SysUserRoleRequest args) throws APIException {
        if (args.getRequestType() != RequestType.QUERY) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        if (args.getUserId() == null) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_MISS_PARAM_USER, UpmsHttpCode.ERR_MISS_PARAM_USER_MSG);
        }
        return new UpmsResponse(roleService.getRoles(args));
    }

    @UpmsApiCall
    @UpmsLogCall(openLog = true)
    @RequestMapping(value = "updateAuths/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<Boolean> updateAuths(HttpServletRequest request, HttpServletResponse response, @RequestBody SysRoleAuthRequest args) throws APIException {
        if (args.getRequestType() != RequestType.UPDATE) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        if (args.getRoleId() == null) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_MISS_PARAM_ROLE, UpmsHttpCode.ERR_MISS_PARAM_ROLE_MSG);
        }
        if (args.getAuthIds() == null || args.getAuthIds().size() == 0) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_MISS_PARAM_AUTH, UpmsHttpCode.ERR_MISS_PARAM_AUTH_MSG);
        }
        return new UpmsResponse(roleService.updateAuths(args));
    }

    @UpmsApiCall
    @UpmsLogCall(openLog = true)
    @RequestMapping(value = "delete/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<Boolean> delete(HttpServletRequest request, HttpServletResponse response, @RequestBody SysRoleRequest args) throws APIException {
        if (args.getRequestType() != RequestType.DELETE) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        if (args.getRoleId() == null) {
            throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_MISS_PARAM_ROLE, UpmsHttpCode.ERR_MISS_PARAM_ROLE_MSG);
        }
        return new UpmsResponse(roleService.delete(args));
    }
}
