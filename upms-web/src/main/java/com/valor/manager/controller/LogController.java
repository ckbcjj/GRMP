package com.valor.manager.controller;

import com.valor.manager.service.LogService;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysLogResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Kemp.Cheng
 * created on: 2019/7/31
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */

@Controller
@RequestMapping("/system/log")
public class LogController {

    @Autowired
    private LogService logService;

    @RequestMapping
    public String log() {
        return "/system/log.html";
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageResultResponse<SysLogResponse> list(@RequestParam(value = "page", defaultValue = "1") Integer page
            , @RequestParam(value = "limit", defaultValue = "10") Integer limit
            , String clientIp, String requestUrl, String operateUser, String begin, String end) throws Exception {
        PageResultResponse<SysLogResponse> response = logService.list(page, limit, clientIp, requestUrl, operateUser, begin, end);
        return response;
    }
}
