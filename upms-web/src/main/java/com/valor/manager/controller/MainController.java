package com.valor.manager.controller;

import com.valor.manager.sdk.model.SysAuth;
import com.valor.manager.sdk.security.UserAccess;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * MainController
 */
@Controller
public class MainController implements ErrorController {

    /**
     * 主页
     */
    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        List<Map<String, Object>> menuTree = new ArrayList<>();
        Map<String, List<SysAuth>> map = UserAccess.getSysAuthMap();
        if (map != null && map.containsKey("menu")) {
            menuTree = getMenuTree(map.get("menu"), -1);
        }
        model.addAttribute("menus", menuTree);
        model.addAttribute("login_user", UserAccess.getLoginUser());
        return "index.html";
    }

    /**
     * 登录页
     */
    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    /**
     * iframe页
     */
    @RequestMapping("/iframe")
    public String error(String url, Model model) {
        model.addAttribute("url", url);
        return "tpl/iframe.html";
    }

    /**
     * 错误页
     */
    @RequestMapping("/error")
    public String error(String code) {
        if ("403".equals(code)) {
            return "error/403.html";
        }
        return "error/404.html";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * 递归转化树形菜单
     */
    private List<Map<String, Object>> getMenuTree(List<SysAuth> authorities, int parentId) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (authorities != null) {
            authorities.sort(new Comparator<SysAuth>() {
                @Override
                public int compare(SysAuth o1, SysAuth o2) {
                    return o1.getOrderNumber() - o2.getOrderNumber();
                }
            });
            for (int i = 0; i < authorities.size(); i++) {
                SysAuth temp = authorities.get(i);
                if (parentId == temp.getParentId()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("menuName", temp.getName());
                    map.put("menuIcon", temp.getAuthIcon());
                    map.put("menuUrl", StringUtils.isEmpty(temp.getActionUrl()) ? "javascript:;" : temp.getActionUrl());
                    map.put("subMenus", getMenuTree(authorities, authorities.get(i).getId()));
                    list.add(map);
                }
            }
        }
        return list;
    }
}
