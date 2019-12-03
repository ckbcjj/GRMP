package com.valor.manager.controller;

import com.alibaba.fastjson.JSONArray;
import com.valor.manager.sdk.security.UserAccess;
import com.valor.manager.service.RoleService;
import com.valor.manager.service.UserService;
import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysUserRequest;
import com.valor.model.web.response.JsonResultResponse;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysRoleResponse;
import com.valor.model.web.response.SysUserResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户管理
 */
@Controller
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('user:view')")
    @RequestMapping
    public String user() {
        return "system/user.html";
    }

    @RequestMapping("/editForm")
    public String userEdit() {
        return "system/user_form.html";
    }

    @RequestMapping("/editUserRoleForm")
    public String userRoleEdit(Model model, int userId) {
        Map<String, List<SysRoleResponse>> map = new HashMap<>();
        List<SysRoleResponse> roleList = roleService.list(null, null).getData();
        for (SysRoleResponse role : roleList) {
            if (!map.containsKey(role.getSourceCode())) {
                map.put(role.getSourceCode(), new ArrayList<>());
            }
            map.get(role.getSourceCode()).add(role);
        }
        model.addAttribute("roleMap", map);
        List<Integer> roleIds = new ArrayList();
        if (userId != -1) {
            roleIds.addAll(roleService.getRolesByUser(userId).stream().map(SysRoleResponse::getRoleId).collect(Collectors.toList()));
        }
        model.addAttribute("roleIds", JSONArray.toJSONString(roleIds));
        return "system/userRole_form.html";
    }

    /**
     * 查询用户列表
     */
    @PreAuthorize("hasAuthority('user:view')")
    @ResponseBody
    @RequestMapping("/list")
    public PageResultResponse<SysUserResponse> list(Integer page, Integer limit, String searchKey, String searchValue) throws Exception {
        if (StringUtils.isEmpty(searchValue)) {
            searchKey = "";
        }
        PageResultResponse<SysUserResponse> response = userService.list(page, limit, null, searchKey, searchValue);
        return response;
    }

    /**
     * 添加用户
     **/
    @PreAuthorize("hasAuthority('user:add')")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResultResponse add(SysUserRequest request) {
        request.setPassword(passwordEncoder.encode("123456"));
        request.setRequestType(RequestType.CREATE);
        JsonResultResponse response = userService.update(request);
        return response;
    }

    /**
     * 修改用户
     **/
    @PreAuthorize("hasAuthority('user:edit')")
    @ResponseBody
    @RequestMapping("/edit")
    public JsonResultResponse edit(SysUserRequest request) {
        request.setRequestType(RequestType.UPDATE);
        JsonResultResponse response = userService.update(request);
        return response;
    }

    /**
     * 删除用户
     **/
    @PreAuthorize("hasAuthority('user:delete')")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResultResponse delete(Integer userId) {
        JsonResultResponse response = userService.delete(userId);
        return response;
    }

    //修改角色
    @PreAuthorize("hasAuthority('user:edit')")
    @ResponseBody
    @RequestMapping("/userRoleUpdate")
    public JsonResultResponse userRoleUpdate(int userId, String roleId) {
        List<Integer> roleIds = Arrays.stream(roleId.split(",")).map(item -> Integer.parseInt(item.trim())).collect(Collectors.toList());
        JsonResultResponse response = userService.updateRolesByUser(userId, roleIds);
        return response;
    }

    /**
     * 修改用户状态
     **/
    @PreAuthorize("hasAuthority('user:delete')")
    @ResponseBody
    @RequestMapping("/updateState")
    public JsonResultResponse updateState(int userId, Integer state) {
        if (state == null) {
            return JsonResultResponse.error(UpmsHttpCode.ERR_MISS_PARAM_USERSTATE, UpmsHttpCode.ERR_MISS_PARAM_USERSTATE_MSG);
        }
        JsonResultResponse response = userService.updateState(userId, state);
        return response;
    }

    /**
     * 修改自己密码
     **/
    @ResponseBody
    @RequestMapping("/updatePsw")
    public JsonResultResponse updatePsw(String oldPsw, String newPsw) {
        if (!passwordEncoder.matches(oldPsw, UserAccess.getLoginUser().getPassword())) {
            return JsonResultResponse.error("old password is error");
        }
        JsonResultResponse response = userService.updatePsw(UserAccess.getLoginUser().getId(), passwordEncoder.encode(newPsw));
        return response;
    }

    /**
     * 重置密码
     **/
    @PreAuthorize("hasAuthority('user:edit')")
    @ResponseBody
    @RequestMapping("/restPsw")
    public JsonResultResponse resetPsw(Integer userId) {
        JsonResultResponse response = userService.updatePsw(userId, passwordEncoder.encode("123456"));
        return response;
    }
}
