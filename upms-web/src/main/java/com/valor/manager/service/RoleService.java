package com.valor.manager.service;

import com.valor.manager.api.ManagerWebRetrofit;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysRoleAuthRequest;
import com.valor.model.web.request.SysRoleRequest;
import com.valor.model.web.request.SysUserRoleRequest;
import com.valor.model.web.response.JsonResultResponse;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysRoleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    public PageResultResponse<SysRoleResponse> list(Integer roleId,String sourceCode) {
        SysRoleRequest request = new SysRoleRequest();
        request.setRequestType(RequestType.QUERY);
        request.setRoleId(roleId);
        request.setSourceCode(sourceCode);
        return ManagerWebRetrofit.getInstance().excuteQuryReq(ManagerWebRetrofit.getInstance().getWebRequest().roleQuery(request));
    }

    public JsonResultResponse update(SysRoleRequest request) {
        return ManagerWebRetrofit.getInstance().excuteReqJson(ManagerWebRetrofit.getInstance().getWebRequest().roleUpdate(request));
    }

    public JsonResultResponse delete(int roleId) {
        SysRoleRequest request = new SysRoleRequest();
        request.setRequestType(RequestType.DELETE);
        request.setRoleId(roleId);
        return ManagerWebRetrofit.getInstance().excuteReqJson(ManagerWebRetrofit.getInstance().getWebRequest().roleDelete(request));
    }

    public List<SysRoleResponse> getRolesByUser(Integer userId) {
        if (userId == null) {
            return null;
        }
        SysUserRoleRequest request = new SysUserRoleRequest();
        request.setRequestType(RequestType.QUERY);
        request.setUserId(userId);
        return ManagerWebRetrofit.getInstance().excuteQuryReq(ManagerWebRetrofit.getInstance().getWebRequest().getRoles(request)).getData();
    }

    public JsonResultResponse updateAuthsByRole(Integer roleId, List<Integer> authIds) {
        if (roleId == null || authIds == null || authIds.size() == 0) {
            return JsonResultResponse.error(UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        SysRoleAuthRequest request = new SysRoleAuthRequest();
        request.setRequestType(RequestType.UPDATE);
        request.setRoleId(roleId);
        request.setAuthIds(authIds);
        return ManagerWebRetrofit.getInstance().excuteReqJson(ManagerWebRetrofit.getInstance().getWebRequest().updateAuths(request));
    }
}
