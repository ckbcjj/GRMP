package com.valor.manager.service;

import com.valor.manager.api.ManagerWebRetrofit;
import com.valor.model.web.request.RequestType;
import com.valor.model.web.request.SysSourceRequest;
import com.valor.model.web.response.JsonResultResponse;
import com.valor.model.web.response.SysSourceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kemp.Cheng
 * created on: 2019/12/2
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
@Service
public class SourceService {

    public List<SysSourceResponse> list(String sourceCode, String sourceName) {
        SysSourceRequest request = new SysSourceRequest();
        request.setSourceCode(sourceCode);
        request.setSourceName(sourceName);
        request.setRequestType(RequestType.QUERY);
        return ManagerWebRetrofit.getInstance().excuteQuryReq(ManagerWebRetrofit.getInstance().getWebRequest().sourceQuery(request)).getData();
    }

    public JsonResultResponse updateStatus(String sourceCode, boolean status) {
        return update(new SysSourceRequest() {{
            setRequestType(RequestType.UPDATE);
            setSubTag(1);
            setSourceCode(sourceCode);
            setStatus(status);
        }});
    }

    public JsonResultResponse update(SysSourceRequest request) {
        return ManagerWebRetrofit.getInstance().excuteReqJson(ManagerWebRetrofit.getInstance().getWebRequest().sourceUpdate(request));
    }
}
