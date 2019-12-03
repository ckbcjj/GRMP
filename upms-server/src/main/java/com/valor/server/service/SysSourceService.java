package com.valor.server.service;

import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysSourceRequest;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysSourceResponse;
import com.valor.server.dao.UpmsDao;
import com.valor.server.db.SysSourceDao;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kemp.Cheng
 * created on: 2019/12/2
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysSourceService implements IConvertable<SysSourceRequest, SysSourceDao, SysSourceResponse> {

    @Autowired
    private UpmsDao dao;

    @Autowired
    private SysAuthoritiesService authoritiesService;

    @Autowired
    private SysRoleService roleService;

    public PageResultResponse<SysSourceResponse> query(SysSourceRequest args) {
        String pageResultTag = null;
        Criterion criterion1 = StringUtils.isEmpty(args.getSourceCode()) ? null : Restrictions.like("sourceCode", "%" + args.getSourceCode().trim() + "%");
        Criterion criterion2 = StringUtils.isEmpty(args.getSourceName()) ? null : Restrictions.like("sourceName", "%" + args.getSourceName().trim() + "%");
        Criterion criterion3 = args.getStatus() == null ? null : Restrictions.eq("status", args.getSourceCode());
        List<SysSourceDao> list = dao.listEntity(SysSourceDao.class, args.getPageIndex(), args.getPageSize(), args.getSortProp(), args.isAscending(), criterion1, criterion2, criterion3);
        long total = list.size();
        if (args.getPageSize() != Integer.MAX_VALUE) {
            pageResultTag = String.format("pageIndex:%d,pageSize:%d", args.getPageIndex(), args.getPageSize());
            total = dao.count(SysSourceDao.class, criterion1, criterion2, criterion3);
        }
        List<SysSourceResponse> responses = new ArrayList<SysSourceResponse>();
        list.forEach(item -> {
            SysSourceResponse response = getOut(item);
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

    public boolean update(SysSourceRequest args) throws APIException {
        if (args.getRequestType() == RequestType.CREATE) {
            SysSourceDao sourceDao = dao.getById(SysSourceDao.class, args.getSourceCode());
            if (sourceDao != null) {
                throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_SOURCE_EXIST, UpmsHttpCode.ERR_SOURCE_EXIST_MSG);
            }
            sourceDao = getIn(args, null);
            if (sourceDao != null) {
                dao.saveEntity(sourceDao);
                init(args);
            }
        } else if (args.getRequestType() == RequestType.UPDATE) {
            SysSourceDao sourceDao = dao.getById(SysSourceDao.class, args.getSourceCode());
            if (sourceDao == null) {
                throw new APIException(UpmsHttpCode.RET_UPDATE, UpmsHttpCode.ERR_SOURCE_NOT_EXIST, UpmsHttpCode.ERR_SOURCE_NOT_EXIST_MSG);
            }
            sourceDao = getIn(args, sourceDao);
            if (sourceDao != null) {
                dao.updateEntity(sourceDao);
            }
        }
        return true;
    }

    private void init(SysSourceRequest request){
        authoritiesService.initSourceAuth(request);
        roleService.initSourceRole(request);
    }

    @Override
    public SysSourceDao inConvert(SysSourceRequest request, SysSourceDao entity) {
        if (entity == null) {
            entity = new SysSourceDao();
        }
        switch (request.getSubTag()) {
            case 0://全更新
                entity.setSourceCode(request.getSourceCode());
                entity.setSourceName(request.getSourceName());
                entity.setStatus(request.getStatus() == null ? true : request.getStatus());
                break;
            case 1://更新状态
                entity.setStatus(request.getStatus());
                break;
        }
        return entity;
    }

    @Override
    public SysSourceResponse outConvert(SysSourceDao entity) {
        SysSourceResponse response = new SysSourceResponse();
        response.setSourceCode(entity.getSourceCode());
        response.setSourceName(entity.getSourceName());
        response.setStatus(entity.getStatus() == null ? true : entity.getStatus());
        return response;
    }
}
