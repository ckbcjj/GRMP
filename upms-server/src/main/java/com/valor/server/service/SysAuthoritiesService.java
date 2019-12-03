package com.valor.server.service;

import com.valor.model.web.request.SysSourceRequest;
import com.valor.server.dao.UpmsDao;
import com.valor.server.db.SysAuthoritiesDao;
import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysAuthorityRequest;
import com.valor.model.web.request.SysRoleAuthRequest;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysAuthorityResponse;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysAuthoritiesService implements IConvertable<SysAuthorityRequest, SysAuthoritiesDao, SysAuthorityResponse> {

    @Autowired
    private UpmsDao dao;

    @Autowired
    private SysRoleAuthorityService roleAuthorityService;

    public PageResultResponse<SysAuthorityResponse> query(SysAuthorityRequest args) {
        Criterion authIdCrt = args.getAuthorityId() == null ? null : Restrictions.eq("authorityId", args.getAuthorityId());
        Criterion authType = StringUtils.isEmpty(args.getAuthType()) ? null : Restrictions.eq("authType", args.getAuthType());
        Criterion sourceCrt = StringUtils.isEmpty(args.getSourceCode()) ? null : Restrictions.eq("sourceCode", args.getSourceCode());
        List<SysAuthoritiesDao> list = dao.listEntity(SysAuthoritiesDao.class, null, null, args.getSortProp(), true, authIdCrt, authType, sourceCrt);
        List<SysAuthorityResponse> responses = new ArrayList<SysAuthorityResponse>();
        list.forEach(item -> {
            SysAuthorityResponse response = getOut(item);
            if (response != null) {
                responses.add(response);
            }
        });
        return new PageResultResponse(responses);
    }

    public PageResultResponse<SysAuthorityResponse> getAuths(SysRoleAuthRequest args) {
        List<Integer> authIds = roleAuthorityService.getAuthIds(args.getRoleId());
        if (authIds.size() == 0) {
            return new PageResultResponse<>(new ArrayList());
        }
        Criterion authCrt = Restrictions.in("authorityId", authIds);
        List<SysAuthoritiesDao> list = dao.listEntity(SysAuthoritiesDao.class, null, null, null, true, authCrt);
        List<SysAuthorityResponse> responses = new ArrayList<SysAuthorityResponse>();
        list.forEach(item -> {
            SysAuthorityResponse response = getOut(item);
            if (response != null) {
                responses.add(response);
            }
        });
        return new PageResultResponse(responses);
    }

    public boolean update(SysAuthorityRequest args) throws APIException {
        if (args.getRequestType() == RequestType.CREATE) {
            SysAuthoritiesDao authoritiesDao = getIn(args, null);
            if (authoritiesDao != null) {
                dao.saveEntity(authoritiesDao);
            }
        } else if (args.getRequestType() == RequestType.UPDATE) {
            if (args.getAuthorityId() == null) {
                throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_MISS_PARAM_AUTH, UpmsHttpCode.ERR_MISS_PARAM_AUTH_MSG);
            }
            SysAuthoritiesDao authoritiesDao = dao.getById(SysAuthoritiesDao.class, args.getAuthorityId());
            if (authoritiesDao == null) {
                throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_AUTH_NOT_EXIST, UpmsHttpCode.ERR_AUTH_NOT_EXIST_MSG);
            }
            authoritiesDao = getIn(args, authoritiesDao);
            if (authoritiesDao != null) {
                dao.updateEntity(authoritiesDao);
            }
        }
        return true;
    }

    public void initSourceAuth(SysSourceRequest request) {
        SysAuthoritiesDao sourceMenu = getIn(new SysAuthorityRequest() {{
            setAuthorityName(request.getSourceName());
            setParentId(-1);
            setAuthIcon("layui-icon-component");
            setSourceCode("upms");
            setAuthType("menu");
            setRequestType(RequestType.CREATE);
            setReqUserName("system");
        }}, null);
        if (sourceMenu != null) {
            int sourceMenuId = (int) dao.saveEntity(sourceMenu);
            SysAuthoritiesDao authMenu = getIn(new SysAuthorityRequest() {{
                setAuthorityName("Auth Management");
                setParentId(sourceMenuId);
                setSourceCode("upms");
                setAuthType("menu");
                setAuthIcon("layui-icon-auz");
                setAccessUrl(String.format("system/%s/authorities", request.getSourceCode()));
                setRequestType(RequestType.CREATE);
                setReqUserName("system");
            }}, null);
            if (authMenu != null) {
                int authMenuId = (int) dao.saveEntity(authMenu);
                SysAuthoritiesDao authView = getIn(new SysAuthorityRequest() {{
                    setAuthorityName("Authority Query");
                    setAuthority("auth:view");
                    setParentId(authMenuId);
                    setSourceCode("upms");
                    setAuthType("element");
                    setRequestType(RequestType.CREATE);
                    setReqUserName("system");
                }}, null);
                if (authView != null) {
                    dao.saveEntity(authView);
                }
                SysAuthoritiesDao authAdd = getIn(new SysAuthorityRequest() {{
                    setAuthorityName("Authority Add");
                    setAuthority("auth:add");
                    setParentId(authMenuId);
                    setSourceCode("upms");
                    setAuthType("element");
                    setRequestType(RequestType.CREATE);
                    setReqUserName("system");
                }}, null);
                if (authAdd != null) {
                    dao.saveEntity(authAdd);
                }
                SysAuthoritiesDao authEdit = getIn(new SysAuthorityRequest() {{
                    setAuthorityName("Authority Edit");
                    setAuthority("auth:edit");
                    setParentId(authMenuId);
                    setSourceCode("upms");
                    setAuthType("element");
                    setRequestType(RequestType.CREATE);
                    setReqUserName("system");
                }}, null);
                if (authEdit != null) {
                    dao.saveEntity(authEdit);
                }
                SysAuthoritiesDao authDelete = getIn(new SysAuthorityRequest() {{
                    setAuthorityName("Authority Delete");
                    setAuthority("auth:delete");
                    setParentId(authMenuId);
                    setSourceCode("upms");
                    setAuthType("element");
                    setRequestType(RequestType.CREATE);
                    setReqUserName("system");
                }}, null);
                if (authDelete != null) {
                    dao.saveEntity(authDelete);
                }
            }
            SysAuthoritiesDao roleMenu = getIn(new SysAuthorityRequest() {{
                setAuthorityName("Role Management");
                setParentId(sourceMenuId);
                setSourceCode("upms");
                setAuthType("menu");
                setAuthIcon("layui-icon-flag");
                setAccessUrl(String.format("system/%s/role", request.getSourceCode()));
                setRequestType(RequestType.CREATE);
                setReqUserName("system");
            }}, null);
            if (roleMenu != null) {
                int roleMenuId = (int) dao.saveEntity(roleMenu);
                SysAuthoritiesDao roleView = getIn(new SysAuthorityRequest() {{
                    setAuthorityName("Role Query");
                    setAuthority("role:view");
                    setParentId(roleMenuId);
                    setSourceCode("upms");
                    setAuthType("element");
                    setRequestType(RequestType.CREATE);
                    setReqUserName("system");
                }}, null);
                if (roleView != null) {
                    dao.saveEntity(roleView);
                }
                SysAuthoritiesDao roleAdd = getIn(new SysAuthorityRequest() {{
                    setAuthorityName("Role Add");
                    setAuthority("role:add");
                    setParentId(roleMenuId);
                    setSourceCode("upms");
                    setAuthType("element");
                    setRequestType(RequestType.CREATE);
                    setReqUserName("system");
                }}, null);
                if (roleAdd != null) {
                    dao.saveEntity(roleAdd);
                }
                SysAuthoritiesDao roleEdit = getIn(new SysAuthorityRequest() {{
                    setAuthorityName("Role Edit");
                    setAuthority("role:edit");
                    setParentId(roleMenuId);
                    setSourceCode("upms");
                    setAuthType("element");
                    setRequestType(RequestType.CREATE);
                    setReqUserName("system");
                }}, null);
                if (roleEdit != null) {
                    dao.saveEntity(roleEdit);
                }
                SysAuthoritiesDao roleDelete = getIn(new SysAuthorityRequest() {{
                    setAuthorityName("Role Delete");
                    setAuthority("role:delete");
                    setParentId(roleMenuId);
                    setSourceCode("upms");
                    setAuthType("element");
                    setRequestType(RequestType.CREATE);
                    setReqUserName("system");
                }}, null);
                if (roleDelete != null) {
                    dao.saveEntity(roleDelete);
                }
                SysAuthoritiesDao roleAuth = getIn(new SysAuthorityRequest() {{
                    setAuthorityName("Role Authority");
                    setAuthority("role:auth");
                    setParentId(roleMenuId);
                    setSourceCode("upms");
                    setAuthType("element");
                    setRequestType(RequestType.CREATE);
                    setReqUserName("system");
                }}, null);
                if (roleAuth != null) {
                    dao.saveEntity(roleAuth);
                }
            }
        }
    }

    public boolean delete(SysAuthorityRequest args) throws APIException {
        SysAuthoritiesDao entity = dao.getById(SysAuthoritiesDao.class, args.getAuthorityId());
        if (entity == null) {
            throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_AUTH_NOT_EXIST, UpmsHttpCode.ERR_AUTH_NOT_EXIST_MSG);
        }
        Criterion criterion = Restrictions.eq("parentId", args.getAuthorityId());
        long childCount = dao.count(SysAuthoritiesDao.class, criterion);
        if (childCount > 0) {
            throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_HAS_CHILD_NODES, UpmsHttpCode.ERR_HAS_CHILD_NODES_MSG);
        }
        dao.deleteEntity(entity);
        roleAuthorityService.delete(null, args.getAuthorityId());
        return true;
    }

    @Override
    public SysAuthoritiesDao inConvert(SysAuthorityRequest request, SysAuthoritiesDao entity) {
        if (entity == null) {
            entity = new SysAuthoritiesDao();
        }
        entity.setLastModifyUser(request.getReqUserName());
        entity.setAuthorityId(request.getAuthorityId());
        entity.setAuthorityName(request.getAuthorityName());
        entity.setAuthority(request.getAuthority());
        entity.setAccessUrl(request.getAccessUrl());
        entity.setParentId(request.getParentId() == null ? -1 : request.getParentId());
        entity.setAuthType(request.getAuthType());
        entity.setOrderNumber(request.getOrderNumber() == null ? -1 : request.getOrderNumber());
        entity.setAuthIcon(request.getAuthIcon());
        entity.setSourceCode(request.getSourceCode());
        return entity;
    }

    @Override
    public SysAuthorityResponse outConvert(SysAuthoritiesDao entity) {
        SysAuthorityResponse response = new SysAuthorityResponse();
        response.setAuthorityId(entity.getAuthorityId());
        response.setAuthorityName(entity.getAuthorityName());
        response.setAuthority(entity.getAuthority());
        response.setAccessUrl(entity.getAccessUrl());
        response.setParentId(entity.getParentId() == null ? -1 : entity.getParentId());
        response.setAuthType(entity.getAuthType());
        response.setOrderNumber(entity.getOrderNumber() == null ? -1 : entity.getOrderNumber());
        response.setAuthIcon(entity.getAuthIcon());
        response.setSourceCode(entity.getSourceCode());
        return response;
    }
}
