package com.valor.manager.service;

import com.valor.manager.api.ManagerWebRetrofit;
import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysUserRequest;
import com.valor.model.web.request.SysUserRoleRequest;
import com.valor.model.web.response.JsonResultResponse;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysUserResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public PageResultResponse<SysUserResponse> list(Integer pageNum, Integer pageSize, Boolean isDelete, String searchKey, String searchValue) throws Exception {
        SysUserRequest request = new SysUserRequest();
        request.setRequestType(RequestType.QUERY);
        request.setState(isDelete == null ? null : (isDelete ? 1 : 0));
        if (!StringUtils.isEmpty(searchKey)) {
            switch (searchKey) {
                case "userId":
                    Integer userId = null;
                    try {
                        userId = Integer.parseInt(searchValue);
                    } catch (Exception ex) {
                        throw new APIException(UpmsHttpCode.RET_QUERY, UpmsHttpCode.ERR_PARAM_SEARCHVALUE_INVALID, UpmsHttpCode.ERR_PARAM_SEARCHVALUE_INVALID_MSG);
                    }
                    request.setUserId(userId);
                    break;
                case "userName":
                    request.setUserName(searchValue);
                    break;
                case "nickName":
                    request.setNickName(searchValue);
                    break;
                case "phone":
                    request.setPhone(searchValue);
                    break;
                default:
                    break;
            }
        }
        if (pageNum != null) {
            request.setPageIndex(pageNum);
        }
        if (pageSize != null) {
            request.setPageSize(pageSize);
        }
        return ManagerWebRetrofit.getInstance().excuteQuryReq(ManagerWebRetrofit.getInstance().getWebRequest().userQuery(request));
    }

    public JsonResultResponse update(SysUserRequest request) {
        return ManagerWebRetrofit.getInstance().excuteReqJson(ManagerWebRetrofit.getInstance().getWebRequest().userUpdate(request));
    }

    public JsonResultResponse updateState(int userId, int state) {
        return update(new SysUserRequest() {{
            setRequestType(RequestType.UPDATE);
            setSubTag(1);
            setUserId(userId);
            setState(state);
        }});
    }

    public JsonResultResponse updatePsw(int userId, String encode) {
        return update(new SysUserRequest() {{
            setRequestType(RequestType.UPDATE);
            setSubTag(2);
            setUserId(userId);
            setPassword(encode);
        }});
    }

    public JsonResultResponse updateRolesByUser(Integer userId, List<Integer> roleIds) {
        if (userId == null || roleIds == null || roleIds.size() == 0) {
            return JsonResultResponse.error(UpmsHttpCode.ERR_INVALID_ARGS, UpmsHttpCode.ERR_INVALID_ARGS_MSG);
        }
        SysUserRoleRequest request = new SysUserRoleRequest();
        request.setRequestType(RequestType.UPDATE);
        request.setUserId(userId);
        request.setRoleIds(roleIds);
        return ManagerWebRetrofit.getInstance().excuteReqJson(ManagerWebRetrofit.getInstance().getWebRequest().updateRoles(request));
    }

    public JsonResultResponse delete(Integer userId) {
        SysUserRequest request = new SysUserRequest();
        request.setRequestType(RequestType.DELETE);
        request.setUserId(userId);
        return ManagerWebRetrofit.getInstance().excuteReqJson(ManagerWebRetrofit.getInstance().getWebRequest().userDelete(request));
    }

}

