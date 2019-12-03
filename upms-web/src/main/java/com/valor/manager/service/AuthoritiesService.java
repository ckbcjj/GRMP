package com.valor.manager.service;

import com.valor.manager.api.ManagerWebRetrofit;
import com.valor.model.web.response.JsonResultResponse;
import com.valor.model.web.response.SysAuthorityResponse;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysAuthorityRequest;
import com.valor.model.web.request.SysRoleAuthRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthoritiesService {

    public List<SysAuthorityResponse> list(String authType,String sourceCode) {
        SysAuthorityRequest request = new SysAuthorityRequest();
        request.setAuthType(authType);
        request.setSourceCode(sourceCode);
        request.setRequestType(RequestType.QUERY);
        return ManagerWebRetrofit.getInstance().excuteQuryReq(ManagerWebRetrofit.getInstance().getWebRequest().authQuery(request)).getData();
    }

    public JsonResultResponse update(SysAuthorityRequest request) {
        return ManagerWebRetrofit.getInstance().excuteReqJson(ManagerWebRetrofit.getInstance().getWebRequest().authUpdate(request));
    }

    public JsonResultResponse delete(int authorityId) {
        SysAuthorityRequest request = new SysAuthorityRequest();
        request.setAuthorityId(authorityId);
        request.setRequestType(RequestType.DELETE);
        return ManagerWebRetrofit.getInstance().excuteReqJson(ManagerWebRetrofit.getInstance().getWebRequest().authDelete(request));
    }

    public List<SysAuthorityResponse> getAuthsByRole(Integer roleId) {
        if (roleId == null) {
            return null;
        }
        SysRoleAuthRequest request = new SysRoleAuthRequest();
        request.setRequestType(RequestType.QUERY);
        request.setRoleId(roleId);
        return ManagerWebRetrofit.getInstance().excuteQuryReq(ManagerWebRetrofit.getInstance().getWebRequest().getAuths(request)).getData();
    }
}
