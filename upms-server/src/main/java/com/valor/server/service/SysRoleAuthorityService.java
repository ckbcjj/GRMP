package com.valor.server.service;

import com.valor.server.dao.UpmsDao;
import com.valor.server.db.SysRoleAuthoritiesDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/11
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleAuthorityService {

    @Autowired
    private UpmsDao dao;

    public List<Integer> getAuthIds(Integer roleId) {
        Criterion roleCrt = Restrictions.eq("roleId", roleId);
        List<SysRoleAuthoritiesDao> list = dao.listEntity(SysRoleAuthoritiesDao.class, null, null, null, true, roleCrt);
        return list.stream().map(SysRoleAuthoritiesDao::getAuthorityId).collect(Collectors.toList());
    }

    public void update(Integer roleId, List<Integer> authorityIds, String userName) {
        delete(roleId, null);
        if (authorityIds != null) {
            List<SysRoleAuthoritiesDao> list = new ArrayList();
            for (Integer authId : authorityIds) {
                SysRoleAuthoritiesDao rAuth = new SysRoleAuthoritiesDao();
                rAuth.setRoleId(roleId);
                rAuth.setAuthorityId(authId);
                rAuth.setLastModifyUser(userName);
                list.add(rAuth);
            }
            dao.storeAll(list);
        }
    }

    public void delete(Integer roleId, Integer authId) {
        Criterion roleCrt = roleId == null ? null : Restrictions.eq("roleId", roleId);
        Criterion authCrt = authId == null ? null : Restrictions.eq("authorityId", authId);
        Collection collection = dao.listEntity(SysRoleAuthoritiesDao.class, null, null, null, true, roleCrt, authCrt);
        dao.deleteAll(collection);
    }
}
