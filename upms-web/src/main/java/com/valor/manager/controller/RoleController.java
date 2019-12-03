package com.valor.manager.controller;

import com.alibaba.fastjson.JSON;
import com.valor.manager.service.AuthoritiesService;
import com.valor.manager.service.RoleService;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysRoleRequest;
import com.valor.model.web.response.JsonResultResponse;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysAuthorityResponse;
import com.valor.model.web.response.SysRoleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 角色管理
 **/
@Controller
@RequestMapping("/system/{source}/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthoritiesService authoritiesService;

    @PreAuthorize("hasAuthority('role:view')")
    @RequestMapping()
    public String role(Model model, @PathVariable String source) {
        model.addAttribute("source", source);
        return "system/role.html";
    }

    /**
     * 查询所有角色
     **/
    @PreAuthorize("hasAuthority('role:view')")
    @ResponseBody
    @RequestMapping("/list")
    public PageResultResponse<SysRoleResponse> list(String keyword,@PathVariable String source) {
        List<SysRoleResponse> list = roleService.list(null, source).getData();
        if (keyword != null && !keyword.trim().isEmpty()) {
            keyword = keyword.trim();
            Iterator<SysRoleResponse> iterator = list.iterator();
            while (iterator.hasNext()) {
                SysRoleResponse next = iterator.next();
                boolean b = String.valueOf(next.getRoleId()).contains(keyword) || next.getRoleName().contains(keyword);
                if (!b) {
                    iterator.remove();
                }
            }
        }
        return new PageResultResponse<>(list);
    }

    /**
     * 添加角色
     **/
    @PreAuthorize("hasAuthority('role:add')")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResultResponse add(SysRoleRequest request) {
        request.setRequestType(RequestType.CREATE);
        JsonResultResponse response = roleService.update(request);
        return response;
    }

    /**
     * 修改角色
     **/
    @PreAuthorize("hasAuthority('role:edit')")
    @ResponseBody
    @RequestMapping("/edit")
    public JsonResultResponse edit(SysRoleRequest request) {
        request.setRequestType(RequestType.UPDATE);
        JsonResultResponse response = roleService.update(request);
        return response;
    }

    /**
     * 删除角色
     **/
    @PreAuthorize("hasAuthority('role:delete')")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResultResponse delete(Integer roleId) {
        JsonResultResponse response = roleService.delete(roleId);
        return response;
    }

    /**
     * 角色权限树
     */
    @ResponseBody
    @GetMapping("/authTree")
    public List<Map<String, Object>> authTree(int roleId,@PathVariable String source) {
        List<SysAuthorityResponse> roleAuths = authoritiesService.getAuthsByRole(roleId);
        List<SysAuthorityResponse> allAuths = authoritiesService.list(null,source);
        List<Map<String, Object>> authTrees = new ArrayList<>();
        for (SysAuthorityResponse one : allAuths) {
            Map<String, Object> authTree = new HashMap<>();
            authTree.put("id", one.getAuthorityId());
            authTree.put("name", one.getAuthorityName());
            authTree.put("pId", one.getParentId());
            authTree.put("open", true);
            authTree.put("checked", false);
            if (roleAuths != null) {
                for (SysAuthorityResponse temp : roleAuths) {
                    if (temp.getAuthorityId() == one.getAuthorityId()) {
                        authTree.put("checked", true);
                        break;
                    }
                }
            }
            authTrees.add(authTree);
        }
        return authTrees;
    }

    @PreAuthorize("hasAuthority('role:auth')")
    @ResponseBody
    @PostMapping("/updateRoleAuth")
    public JsonResultResponse updateRoleFuncAuth(int roleId, String authIds) {
        JsonResultResponse response = roleService.updateAuthsByRole(roleId, JSON.parseArray(authIds, Integer.class));
        return response;
    }
}
