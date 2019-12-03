package com.valor.server.controller;

import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.AuthRequest;
import com.valor.model.web.response.UpmsDetailsResponse;
import com.valor.model.web.response.UpmsResponse;
import com.valor.server.aop.UpmsApiCall;
import com.valor.server.aop.UpmsLogCall;
import com.valor.server.service.UpmsDetailsService;
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
@RequestMapping(value = "/api/upms/upmsDetails")
public class UpmsDetailsController {

    @Autowired
    private UpmsDetailsService upmsDetailsService;


    @UpmsApiCall
    @UpmsLogCall
    @RequestMapping(value = "upmsAuths/v1", method = {RequestMethod.POST})
    @ResponseBody
    public UpmsResponse<UpmsDetailsResponse> upmsAuths(HttpServletRequest request, HttpServletResponse response, @RequestBody AuthRequest args) throws APIException {
        if (StringUtils.isEmpty(args.getUserName())) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_MISS_PARAM_USER, UpmsHttpCode.ERR_MISS_PARAM_USER_MSG);
        }
        if (StringUtils.isEmpty(args.getSourceCode())) {
            throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_MISS_PARAM_SOURCE, UpmsHttpCode.ERR_MISS_PARAM_SOURCE_MSG);
        }
        return new UpmsResponse(upmsDetailsService.upmsDetails(args));
    }
}
