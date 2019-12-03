package com.valor.manager.service;

import com.valor.manager.api.ManagerWebRetrofit;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysLogRequest;
import com.valor.model.web.response.PageResultResponse;
import com.valor.model.web.response.SysLogResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/11
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
@Service
public class LogService {

    public PageResultResponse<SysLogResponse> list(Integer pageNum, Integer pageSize, String clientIp, String requestUrl, String operateUser, String begin, String end) throws Exception {
        if (!StringUtils.isEmpty(begin) || !StringUtils.isEmpty(end)) {
            Date beginDate = new Date(0);
            Date endDate = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (!StringUtils.isEmpty(begin)) {
                beginDate = df.parse(begin);
            }
            if (!StringUtils.isEmpty(end)) {
                endDate = df.parse(end);
            }
            if (endDate.compareTo(beginDate) < 0) {
                return new PageResultResponse(new ArrayList());
            } else {
                begin = df.format(beginDate) + " 00:00:00";
                end = df.format(endDate) + " 23:59:59";
            }
        }
        SysLogRequest request = new SysLogRequest();
        request.setRequestType(RequestType.QUERY);
        request.setClientIp(clientIp);
        request.setRequestUrl(requestUrl);
        request.setOperateUser(operateUser);
        request.setBegin(begin);
        request.setEnd(end);
        if(pageNum!=null) {
            request.setPageIndex(pageNum);
        }
        if(pageSize!=null) {
            request.setPageSize(pageSize);
        }
        request.setSortProp("createTime");
        request.setAscending(false);
        return ManagerWebRetrofit.getInstance().excuteQuryReq(ManagerWebRetrofit.getInstance().getWebRequest().logQuery(request));
    }
}
