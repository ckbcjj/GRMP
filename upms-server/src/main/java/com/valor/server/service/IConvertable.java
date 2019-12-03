package com.valor.server.service;

import com.valor.model.web.request.UpmsManageRequest;
import com.valor.model.web.response.UpmsManageReponse;
import com.valor.server.db.AbstractLMI;

import java.util.Date;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/12
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
public interface IConvertable<T1 extends UpmsManageRequest,T2 extends AbstractLMI,T3 extends UpmsManageReponse> {

    default T2 inConvert(T1 request, T2 entity) {
        return null;
    }

    default T2 getIn(T1 request, T2 entity) {
        if (request != null) {
            entity = inConvert(request, entity);
            if (entity != null) {
                entity.setLastModifyUser(request.getReqUserName());
            }
        }
        return entity;
    }

    default T3 outConvert(T2 entity) {
        return null;
    }

    default T3 getOut(T2 entity) {
        T3 response = null;
        if (entity != null) {
            response = outConvert(entity);
            if (response != null) {
                response.setCreateTime(entity.getCreateTime() == null ? new Date(0) : entity.getCreateTime());
                response.setLastModifyTime(entity.getLastModifyTime() == null ? new Date(0) : entity.getLastModifyTime());
                response.setLastModifyUser(entity.getLastModifyUser());
            }
        }
        return response;
    }
}
