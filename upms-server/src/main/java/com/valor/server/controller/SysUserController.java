package com.valor.server.controller;

import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysUserRequest;
import com.valor.model.web.request.SysUserRoleRequest;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysUserResponse;
import com.valor.model.web.response.UpmsResponse;
import com.valor.server.aop.UpmsApiCall;
import com.valor.server.aop.UpmsLogCall;
import com.valor.server.service.SysUserService;
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
@RequestMapping(value = "/api/upms/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;


    @UpmsApiCall
    @UpmsLogCall
    @RequestMapping(value = "query/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<PageResultResponse<SysUserResponse>> query(HttpServletRequest request, HttpServletResponse response, @RequestBody SysUserRequest args) throws APIException {
        if (args.getRequestType() != RequestType.QUERY) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        return new UpmsResponse(userService.query(args));
    }

    @UpmsApiCall
    @UpmsLogCall(openLog = true)
    @RequestMapping(value = "update/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<Boolean> update(HttpServletRequest request, HttpServletResponse response, @RequestBody SysUserRequest args) throws APIException {
        if (args.getRequestType() != RequestType.CREATE && args.getRequestType() != RequestType.UPDATE) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        return new UpmsResponse(userService.update(args));
    }

    @UpmsApiCall
    @UpmsLogCall(openLog = true)
    @RequestMapping(value = "updateRoles/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<Boolean> updateRoles(HttpServletRequest request, HttpServletResponse response, @RequestBody SysUserRoleRequest args) throws APIException {
        if (args.getRequestType() != RequestType.UPDATE) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        if (args.getUserId() == null) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_MISS_PARAM_USER, UpmsHttpCode.ERR_MISS_PARAM_USER_MSG);
        }
        if (args.getRoleIds() == null || args.getRoleIds().size() == 0) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_MISS_PARAM_ROLE, UpmsHttpCode.ERR_MISS_PARAM_ROLE_MSG);
        }
        return new UpmsResponse(userService.updateRoles(args));
    }

    @UpmsApiCall
    @UpmsLogCall(openLog = true)
    @RequestMapping(value = "delete/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<Boolean> delete(HttpServletRequest request, HttpServletResponse response, @RequestBody SysUserRequest args) throws APIException {
        if (args.getRequestType() != RequestType.DELETE) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        if (args.getUserId() == null) {
            throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_MISS_PARAM_USER, UpmsHttpCode.ERR_MISS_PARAM_USER_MSG);
        }
        return new UpmsResponse(userService.delete(args));
    }
}
