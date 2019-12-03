package com.valor.server.controller;

import com.valor.server.aop.UpmsApiCall;
import com.valor.server.aop.UpmsLogCall;
import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysLogRequest;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysLogResponse;
import com.valor.model.web.response.UpmsResponse;
import com.valor.server.service.SysLogService;
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
@RequestMapping(value = "/api/upms/log")
public class SysLogController {

    @Autowired
    private SysLogService logService;

    @UpmsApiCall
    @UpmsLogCall(openStat = false)
    @RequestMapping(value = "query/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<PageResultResponse<SysLogResponse>> query(HttpServletRequest request, HttpServletResponse response, @RequestBody SysLogRequest args) throws Exception {
        if (args.getRequestType() != RequestType.QUERY) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        return new UpmsResponse(logService.query(args));
    }
}
