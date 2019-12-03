package com.valor.server.service;

import com.valor.model.web.request.SysLogRequest;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysLogResponse;
import com.valor.server.dao.UpmsDao;
import com.valor.server.db.SysLogDao;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/11
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysLogService implements IConvertable<SysLogRequest, SysLogDao,SysLogResponse> {

    @Autowired
    private UpmsDao dao;

    public PageResultResponse<SysLogResponse> query(SysLogRequest args) throws Exception {
        String pageResultTag = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Criterion criterion1 = StringUtils.isEmpty(args.getClientIp()) ? null : Restrictions.like("clientIp", "%" + args.getClientIp().trim() + "%");
        Criterion criterion2 = StringUtils.isEmpty(args.getRequestUrl()) ? null : Restrictions.like("requestUrl", "%" + args.getRequestUrl().trim() + "%");
        Criterion criterion3 = StringUtils.isEmpty(args.getOperateUser()) ? null : Restrictions.like("operateUser", "%" + args.getOperateUser().trim() + "%");
        Criterion criterion4 = StringUtils.isEmpty(args.getBegin()) || StringUtils.isEmpty(args.getEnd()) ? null : Restrictions.between("createTime", df.parse(args.getBegin()), df.parse(args.getEnd()));
        List<SysLogDao> list = dao.listEntity(SysLogDao.class, args.getPageIndex(), args.getPageSize(), args.getSortProp(), args.isAscending(), criterion1, criterion2, criterion3, criterion4);
        long total = list.size();
        if (args.getPageSize() != Integer.MAX_VALUE) {
            pageResultTag = String.format("pageIndex:%d,pageSize:%d", args.getPageIndex(), args.getPageSize());
            total = dao.count(SysLogDao.class, criterion1, criterion2, criterion3, criterion4);
        }
        List<SysLogResponse> responses = new ArrayList<SysLogResponse>();
        list.forEach(item -> {
            SysLogResponse response = getOut(item);
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


    public void logRecord(String clientIp, String requestUrl, String operateUser, long operatingTime, String reqDetail, String respDetail) {
        SysLogDao log = new SysLogDao();
        log.setClientIp(clientIp);
        log.setRequestUrl(requestUrl);
        log.setOperateUser(operateUser);
        log.setOperatingTime(operatingTime);
        log.setReqDetail(reqDetail);
        log.setRespDetail(respDetail);
        log.setCreateTime(new Date());
        log.setLastModifyUser("system");
        dao.saveEntity(log);
    }

    @Override
    public SysLogResponse outConvert(SysLogDao entity) {
        SysLogResponse response = new SysLogResponse();
        response.setClientIp(entity.getClientIp());
        response.setRequestUrl(entity.getRequestUrl());
        response.setOperateUser(entity.getOperateUser());
        response.setOperatingTime(entity.getOperatingTime());
        response.setReqDetail(entity.getReqDetail());
        response.setRespDetail(entity.getRespDetail());
        return response;
    }
}
