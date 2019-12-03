package com.valor.manager.controller;

import com.valor.manager.service.SourceService;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysSourceRequest;
import com.valor.model.web.response.JsonResultResponse;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysSourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Kemp.Cheng
 * created on: 2019/12/2
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
@Controller
@RequestMapping("/system/source")
public class SourceController {

    @Autowired
    private SourceService sourceService;

    @PreAuthorize("hasAuthority('source:view')")
    @RequestMapping()
    public String authorities() {
        return "system/source.html";
    }

    @RequestMapping("editForm")
    public String editForm() {
        return "system/source_form.html";
    }

    /**
     * 查询所有权限
     **/
    @PreAuthorize("hasAuthority('source:view')")
    @ResponseBody
    @RequestMapping("/list")
    public PageResultResponse<SysSourceResponse> list(String sourceCode, String sourceName) {
        return new PageResultResponse<>(sourceService.list(sourceCode, sourceName));
    }

    @PreAuthorize("hasAuthority('source:add')")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResultResponse add(SysSourceRequest request) {
        request.setRequestType(RequestType.CREATE);
        JsonResultResponse response = sourceService.update(request);
        return response;
    }

    /**
     * 状态变更
     */
    @PreAuthorize("hasAuthority('source:status')")
    @ResponseBody
    @RequestMapping("/updateStatus")
    public JsonResultResponse updateStatus(String sourceCode, boolean status) {
        JsonResultResponse response = sourceService.updateStatus(sourceCode, status);
        return response;
    }

}
