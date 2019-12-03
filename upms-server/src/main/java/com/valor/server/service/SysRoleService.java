package com.valor.server.service;

import com.valor.model.web.request.*;
import com.valor.server.dao.UpmsDao;
import com.valor.server.db.SysRoleDao;
import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysRoleResponse;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleService implements IConvertable<SysRoleRequest, SysRoleDao,SysRoleResponse> {

    @Autowired
    private UpmsDao dao;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private SysRoleAuthorityService roleAuthorityService;

    public PageResultResponse<SysRoleResponse> query(SysRoleRequest args) {
        String pageResultTag = null;
        Criterion roleCrt = args.getRoleId() == null ? null : Restrictions.eq("roleId", args.getRoleId());
        Criterion sourceCrt = StringUtils.isEmpty(args.getSourceCode()) ? null : Restrictions.eq("sourceCode", args.getSourceCode());
        List<SysRoleDao> list = dao.listEntity(SysRoleDao.class, args.getPageIndex(), args.getPageSize(), args.getSortProp(), args.isAscending(), roleCrt, sourceCrt);
        long total = list.size();
        if (args.getPageSize() != Integer.MAX_VALUE) {
            pageResultTag = String.format("pageIndex:%d,pageSize:%d", args.getPageIndex(), args.getPageSize());
            total = dao.count(SysRoleDao.class, roleCrt);
        }
        List<SysRoleResponse> responses = new ArrayList<>();
        list.forEach(item -> {
            SysRoleResponse response = getOut(item);
            if (response != null) {
                responses.add(response);
            }
        });
        PageResultResponse pageResultResponse = new PageResultResponse(total, responses);
        if (!StringUtils.isEmpty(pageResultTag)) {
            pageResultResponse.setMsg(pageResultTag);
        }
        return pageResultResponse;
    }


    public PageResultResponse<SysRoleResponse> getRoles(SysUserRoleRequest args) {
        List<Integer> roleIds = userRoleService.getRoleIds(args.getUserId());
        if (roleIds.size() == 0) {
            return new PageResultResponse<>(new ArrayList());
        }
        Criterion roleCrt = Restrictions.in("roleId", roleIds);
        List<SysRoleDao> list = dao.listEntity(SysRoleDao.class, null, null, null, true, roleCrt);
        List<SysRoleResponse> responses = new ArrayList<>();
        list.forEach(item -> {
            SysRoleResponse response = getOut(item);
            if (response != null) {
                responses.add(response);
            }
        });
        return new PageResultResponse(responses);
    }

    public boolean update(SysRoleRequest args) throws APIException {
        if (args.getRequestType() == RequestType.CREATE) {
            SysRoleDao roleDao = getIn(args, null);
            if (roleDao != null) {
                dao.saveEntity(roleDao);
            }
        } else if (args.getRequestType() == RequestType.UPDATE) {
            SysRoleDao roleDao = dao.getById(SysRoleDao.class, args.getRoleId());
            if (roleDao == null) {
                throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_ROLE_NOT_EXIST, UpmsHttpCode.ERR_ROLE_NOT_EXIST_MSG);
            }
            roleDao = getIn(args, roleDao);
            if (roleDao != null) {
                dao.updateEntity(roleDao);
            }
        }
        return true;
    }

    public boolean updateAuths(SysRoleAuthRequest args) {
        roleAuthorityService.update(args.getRoleId(), args.getAuthIds(), args.getReqUserName());
        return true;
    }

    public boolean delete(SysRoleRequest args) throws APIException {
        SysRoleDao roleDao = dao.getById(SysRoleDao.class, args.getRoleId());
        if (roleDao == null) {
            throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_ROLE_NOT_EXIST, UpmsHttpCode.ERR_ROLE_NOT_EXIST_MSG);
        }
        if(roleDao.getIsSuper()){
            throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_SUPER_ROLE_DELETE, UpmsHttpCode.ERR_SUPER_ROLE_DELETE_MSG);
        }
        dao.deleteEntity(roleDao);
        roleAuthorityService.delete(roleDao.getRoleId(), null);
        userRoleService.delete(null, roleDao.getRoleId());
        return true;
    }

    public void initSourceRole(SysSourceRequest request) {
        SysRoleDao roleDao = getIn(new SysRoleRequest() {{
            setRoleName("superrole");
            setComments(String.format("superrole of %s,created by system when source is initializing", request.getSourceCode()));
            setSourceCode(request.getSourceCode());
            setRequestType(RequestType.CREATE);
            setReqUserName("system");
        }}, null);
        if (roleDao != null) {
            dao.saveEntity(roleDao);
        }
    }

    @Override
    public SysRoleDao inConvert(SysRoleRequest request, SysRoleDao entity) {
        if (entity == null) {
            entity = new SysRoleDao();
        }
        entity.setRoleId(request.getRoleId());
        entity.setRoleName(request.getRoleName());
        entity.setComments(request.getComments());
        entity.setSourceCode(request.getSourceCode());
        entity.setIsSuper(request.getIsSuper() == null ? false : request.getIsSuper());
        return entity;
    }

    @Override
    public SysRoleResponse outConvert(SysRoleDao entity) {
        SysRoleResponse response = new SysRoleResponse();
        response.setRoleId(entity.getRoleId());
        response.setRoleName(entity.getRoleName());
        response.setComments(entity.getComments());
        response.setSourceCode(entity.getSourceCode());
        response.setIsSuper(entity.getIsSuper());
        return response;
    }

}
