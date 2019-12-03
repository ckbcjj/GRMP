package com.valor.server.service;

import com.valor.server.dao.UpmsDao;
import com.valor.server.db.SysUserRoleDao;
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

public class SysUserRoleService {

    @Autowired
    private UpmsDao dao;

    public List<Integer> getRoleIds(Integer userId) {
        Criterion criterion = Restrictions.eq("userId", userId);
        List<SysUserRoleDao> list = dao.listEntity(SysUserRoleDao.class, null, null, null, true, criterion);
        return list.stream().map(SysUserRoleDao::getRoleId).collect(Collectors.toList());
    }

    public void update(Integer userId, List<Integer> roleIds, String userName) {
        delete(userId, null);
        if (roleIds != null) {
            List<SysUserRoleDao> list = new ArrayList();
            for (Integer roleId : roleIds) {
                SysUserRoleDao sysUserRoleDao = new SysUserRoleDao();
                sysUserRoleDao.setUserId(userId);
                sysUserRoleDao.setRoleId(roleId);
                sysUserRoleDao.setLastModifyUser(userName);
                list.add(sysUserRoleDao);
            }
            dao.storeAll(list);
        }
    }

    public void delete(Integer userId, Integer roleId) {
        Criterion userCrt = userId == null ? null : Restrictions.eq("userId", userId);
        Criterion roleCrt = roleId == null ? null : Restrictions.eq("roleId", roleId);
        Collection collection = dao.listEntity(SysUserRoleDao.class, null, null, null, true, userCrt, roleCrt);
        dao.deleteAll(collection);
    }
}
