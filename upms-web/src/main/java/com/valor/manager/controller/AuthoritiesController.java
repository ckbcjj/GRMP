package com.valor.manager.controller;

import com.valor.manager.common.ReflectUtil;
import com.valor.manager.service.AuthoritiesService;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysAuthorityRequest;
import com.valor.model.web.response.JsonResultResponse;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysAuthorityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 权限管理
 **/
@Controller
@RequestMapping("/system/{source}/authorities")
public class AuthoritiesController {

    @Autowired
    private AuthoritiesService authoritiesService;

    @PreAuthorize("hasAuthority('auth:view')")
    @RequestMapping()
    public String authorities(Model model,@PathVariable String source) {
        model.addAttribute("source", source);
        return "system/auth.html";
    }

    @RequestMapping("editForm")
    public String editForm(Model model,@PathVariable String source) {
        model.addAttribute("source", source);
        return "system/auth_form.html";
    }

    /**
     * 查询所有权限
     **/
    @PreAuthorize("hasAuthority('auth:view')")
    @ResponseBody
    @RequestMapping("/list")
    public PageResultResponse<Map<String, Object>> list(@PathVariable String source) {
        List<Map<String, Object>> maps = new ArrayList<>();
        List<SysAuthorityResponse> authorities = authoritiesService.list(null, source);
        for (SysAuthorityResponse one : authorities) {
            Map<String, Object> map = ReflectUtil.objectToMap(one);
            maps.add(map);
        }
        return new PageResultResponse<>(maps);
    }

    /**
     * 添加权限
     */
    @PreAuthorize("hasAuthority('auth:add')")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResultResponse add(SysAuthorityRequest request) {
        request.setRequestType(RequestType.CREATE);
        JsonResultResponse response = authoritiesService.update(request);
        return response;
    }

    /**
     * 修改权限
     */
    @PreAuthorize("hasAuthority('auth:edit')")
    @ResponseBody
    @RequestMapping("/edit")
    public JsonResultResponse edit(SysAuthorityRequest request) {
        request.setRequestType(RequestType.UPDATE);
        JsonResultResponse response = authoritiesService.update(request);
        return response;
    }

    /**
     * 删除权限
     */
    @PreAuthorize("hasAuthority('auth:delete')")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResultResponse delete(int authorityId) {
        JsonResultResponse response = authoritiesService.delete(authorityId);
        return response;
    }

}
