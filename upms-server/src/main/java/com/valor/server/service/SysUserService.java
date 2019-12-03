package com.valor.server.service;

import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysUserRequest;
import com.valor.model.web.request.SysUserRoleRequest;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysUserResponse;
import com.valor.server.dao.UpmsDao;
import com.valor.server.db.SysUserDao;
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
public class SysUserService implements IConvertable<SysUserRequest, SysUserDao,SysUserResponse> {

    @Autowired
    private UpmsDao dao;

    @Autowired
    private SysUserRoleService userRoleService;

    public PageResultResponse<SysUserResponse> query(SysUserRequest args) {
        String pageResultTag = null;
        Criterion stateCrt = args.getState() == null ? null : Restrictions.eq("state", args.getState());
        Criterion userIdCrt = args.getUserId() == null ? null : Restrictions.eq("userId", args.getUserId());
        Criterion userNameCrt = StringUtils.isEmpty(args.getUserName()) ? null : Restrictions.like("userName", "%" + args.getUserName() + "%");
        Criterion nickNameCrt = StringUtils.isEmpty(args.getNickName()) ? null : Restrictions.like("nickName", "%" + args.getNickName() + "%");
        Criterion phoneCrt = StringUtils.isEmpty(args.getPhone()) ? null : Restrictions.like("phone", "%" + args.getPhone() + "%");
        List<SysUserDao> list = dao.listEntity(SysUserDao.class, args.getPageIndex(), args.getPageSize(), args.getSortProp(), args.isAscending(), userIdCrt, userNameCrt, nickNameCrt, phoneCrt, stateCrt);
        long total = list.size();
        if (args.getPageSize() != Integer.MAX_VALUE) {
            pageResultTag = String.format("pageIndex:%d,pageSize:%d", args.getPageIndex(), args.getPageSize());
            total = dao.count(SysUserDao.class, userIdCrt, userNameCrt, nickNameCrt, phoneCrt, stateCrt);
        }
        List<SysUserResponse> responses = new ArrayList<SysUserResponse>();
        list.forEach(item -> {
            SysUserResponse response = getOut(item);
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

    public boolean update(SysUserRequest args) throws APIException {
        if (args.getRequestType() == RequestType.CREATE) {
            SysUserDao sysUserDao = dao.singleEntity(SysUserDao.class, Restrictions.eq("userName", args.getUserName()));
            if (sysUserDao != null) {
                throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_USER_EXIST, UpmsHttpCode.ERR_USER_EXIST_MSG);
            }
            SysUserDao userDao = getIn(args, null);
            if (userDao != null) {
                dao.saveEntity(userDao);
            }
        } else if (args.getRequestType() == RequestType.UPDATE) {
            SysUserDao userDao = dao.getById(SysUserDao.class, args.getUserId());
            if (userDao == null) {
                throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_USER_NOT_EXIST, UpmsHttpCode.ERR_USER_NOT_EXIST_MSG);
            }
            userDao = getIn(args, userDao);
            if (userDao != null) {
                dao.updateEntity(userDao);
            }
        }
        return true;
    }

    public boolean updateRoles(SysUserRoleRequest args) {
        userRoleService.update(args.getUserId(), args.getRoleIds(), args.getReqUserName());
        return true;
    }

    public boolean delete(SysUserRequest args) throws APIException {
        SysUserDao userDao = dao.getById(SysUserDao.class, args.getUserId());
        if (userDao == null) {
            throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_USER_NOT_EXIST, UpmsHttpCode.ERR_USER_NOT_EXIST_MSG);
        }
        if (userDao.getIsSuper()) {
            throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_SUPER_USER_DELETE, UpmsHttpCode.ERR_SUPER_USER_DELETE_MSG);
        }
        dao.deleteEntity(userDao);
        userRoleService.delete(args.getUserId(), null);
        return true;
    }

    @Override
    public SysUserDao inConvert(SysUserRequest request, SysUserDao entity) {
        if (entity == null) {
            entity = new SysUserDao();
        }
        switch (request.getSubTag()) {
            case 0://全更新
                entity.setUserId(request.getUserId());
                entity.setUserName(request.getUserName());
                entity.setPassword(request.getPassword());
                entity.setNickName(request.getNickName());
                entity.setAvatar(request.getAvatar());
                entity.setSex(request.getSex());
                entity.setPhone(request.getPhone());
                entity.setEmail(request.getEmail());
                entity.setEmailVerified(request.getEmailVerified());
                entity.setState(request.getState() == null ? 0 : request.getState());
                entity.setIsSuper(request.getIsSuper() == null ? false : request.getIsSuper());
                break;
            case 1://更新状态
                entity.setState(request.getState());
                break;
            case 2://更新密码
                entity.setPassword(request.getPassword());
                break;
        }
        return entity;
    }

    @Override
    public SysUserResponse outConvert(SysUserDao entity) {
        SysUserResponse response = new SysUserResponse();
        response.setUserId(entity.getUserId());
        response.setUserName(entity.getUserName());
        response.setPassword(entity.getPassword());
        response.setNickName(entity.getNickName());
        response.setAvatar(entity.getAvatar());
        response.setSex(entity.getSex());
        response.setPhone(entity.getPhone());
        response.setEmail(entity.getEmail());
        response.setEmailVerified(entity.getEmailVerified());
        response.setState(entity.getState() == null ? 1 : entity.getState());
        response.setIsSuper(entity.getIsSuper());
        return response;
    }
}
