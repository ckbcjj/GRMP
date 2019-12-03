package com.valor.server.controller;

import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysSourceRequest;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysSourceResponse;
import com.valor.model.web.response.UpmsResponse;
import com.valor.server.aop.UpmsApiCall;
import com.valor.server.aop.UpmsLogCall;
import com.valor.server.service.SysSourceService;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping(value = "/api/upms/source")
public class SysSourceController {

    @Autowired
    private SysSourceService sourceService;

    @UpmsApiCall
    @UpmsLogCall
    @RequestMapping(value = "query/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<PageResultResponse<SysSourceResponse>> query(HttpServletRequest request, HttpServletResponse response, @RequestBody SysSourceRequest args) throws APIException {
        if (args.getRequestType() != RequestType.QUERY) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        return new UpmsResponse(sourceService.query(args));
    }

    @UpmsApiCall
    @UpmsLogCall(openLog = true)
    @RequestMapping(value = "update/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<Boolean> update(HttpServletRequest request, HttpServletResponse response, @RequestBody SysSourceRequest args) throws APIException {
        if (args.getRequestType() != RequestType.CREATE && args.getRequestType() != RequestType.UPDATE) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        if (StringUtils.isEmpty(args.getSourceCode())) {
            throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_MISS_PARAM_SOURCE, UpmsHttpCode.ERR_MISS_PARAM_SOURCE_MSG);
        }
        return new UpmsResponse(sourceService.update(args));
    }
}
