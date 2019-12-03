package com.valor.server.dao;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;


/**
 *
 */
@Transactional
@Repository("upmsDao")
public class UpmsDao extends UpmsBaseDao {
    private static final Logger LOG = LoggerFactory.getLogger(UpmsDao.class);

    @Autowired
    protected SessionFactory upmsSessionFactory;

    @PostConstruct
    protected void init() {
        setSessionFactory(upmsSessionFactory);
    }

    public void storeAll(final Collection entities) {
        super.storeAll(entities);
    }

    public void storeEntity(final Object object) {
        super.storeEntity(object);
    }

    public Serializable saveEntity(final Object object) {
       return super.saveEntity(object);
    }

    public void mergeAll(final Collection entities) {
        super.mergeAll(entities);
    }

    public void merge(final Object entity) {
        super.mergeEntity(entity);
    }

    public void deleteEntity(final Object entity) {
        super.deleteEntity(entity);
    }

    public void updateEntity(final Object entity) {
        super.updateEntity(entity);
    }


    public <T> List<T> getObjects(Class T, String conditions, Set<String> ignoredColumns) {
        List list = new ArrayList<>();
        if (T == null)
            return list;

        SqlScalars sqlScalars = new SqlScalars();
        List<HibenateTools.ColumnInfo> columnInfos = HibenateTools.getColumnInfos(T);
        String tableName = HibenateTools.getTableName(T);
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("select ");
        for (HibenateTools.ColumnInfo columnInfo : columnInfos) {
            if (ignoredColumns != null && ignoredColumns.contains(columnInfo.getColumnName()))
                continue;

            Method method = columnInfo.getMethod();
            if (!columnInfo.getColumnName().startsWith("`"))
                sqlBuilder.append("`");
            sqlBuilder.append(columnInfo.getColumnName());
            if (!columnInfo.getColumnName().endsWith("`"))
                sqlBuilder.append("`");

            sqlBuilder.append(" \"").append(columnInfo.getAttributeName()).append("\",");

            sqlScalars.addScalar(columnInfo.getAttributeName(), HibenateTools.getBasicType(method.getReturnType()));
        }

        // delete character ','
        sqlBuilder.setLength(sqlBuilder.length() - 1);

        sqlBuilder.append(" from ").append(tableName);
        if (!StringUtils.isEmpty(conditions))
            sqlBuilder.append(" ").append(conditions);

        String sql = sqlBuilder.toString();
        // LOG.info(sql);

        sqlScalars.addToSql(sql);
        try {
            list.addAll(query(T, sqlScalars));
        } catch (Exception e) {
            LOG.error("fail to manage:{}", sql);
            LOG.trace(e.getMessage(), e);
        }

        return list;
    }

    public Long getLatestUpdateTimeLong(Class clazz, Map<String, Object> filters) {
        Date date = getLatestUpdateTime(clazz, filters);
        if (date != null) {
            return date.getTime();
        } else {
            return 0L;
        }
    }

    public Date getLatestUpdateTime(Class clazz, Map<String, Object> filters) {
        Criteria criteria = currentSession().createCriteria(clazz);
        if (filters != null) {
            for (Map.Entry entry : filters.entrySet()) {
                if (entry.getValue() instanceof Collection) {
                    criteria.add(Restrictions.in((String) entry.getKey(), (Collection) entry.getValue()));
                } else {
                    criteria.add(Restrictions.eq((String) entry.getKey(), entry.getValue()));
                }
            }
        }

        criteria.setProjection(Projections.projectionList().add(Projections.max("lastModifyTime")));
        return (Date) criteria.uniqueResult();
    }



    public String queryTimeStamp(Class clazz) {
        Criteria criteria = currentSession().createCriteria(clazz);
        criteria.setCacheable(false);
        criteria.setReadOnly(true);
        criteria.setProjection(Projections.projectionList().add(Projections.count("lastModifyTime"))
                .add(Projections.max("lastModifyTime")));

        Object[] objList = (Object[]) criteria.uniqueResult();
        if (objList == null) {
            return "";
        } else {
            return Joiner.on("@").useForNull("-").join(objList);
        }
    }

    public <T> List<T> listEntity(Class<T> t, Integer pageIndex, Integer pageSize, String sortName,boolean ascending, Criterion... criterions) {
        Criteria criteria = currentSession().createCriteria(t);
        if (criterions != null) {
            for (Criterion criterion : criterions)
                if (criterion != null)
                    criteria.add(criterion);
        }
        if (pageIndex != null && pageSize != null) {
            criteria.setFirstResult((pageIndex - 1) * pageSize);
            criteria.setMaxResults(pageSize);
        }
        if (!StringUtils.isEmpty(sortName)) {
            criteria.addOrder(ascending ? Order.asc(sortName) : Order.desc(sortName));
        }
        return criteria.list();
    }

    public <T> T singleEntity(Class<T> t, Criterion... criterions) {
        List<T> list = listEntity(t, null, null, null,true, criterions);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public Long count(Class t, Criterion... criterions) {
        Criteria criteria = currentSession().createCriteria(t);
        if (criterions != null) {
            for (Criterion criterion : criterions)
                if (criterion != null)
                    criteria.add(criterion);
        }
        criteria.setProjection(Projections.rowCount());
        return Long.parseLong(criteria.uniqueResult().toString());
    }

    //联合子查询
    public <T> List<T> subUnionQuery(Class type, Class subType, Collection<Criterion> subCri, String unionProOfSub, String unionPro) {
        Criteria criteria = currentSession().createCriteria(type, type.getSimpleName());
        DetachedCriteria subCriteria = DetachedCriteria.forClass(subType, subType.getSimpleName());
        if (subCri != null) {
            for (Criterion criterion : subCri
            ) {
                if (criterion != null) {
                    subCriteria.add(criterion);
                }
            }
        }
        subCriteria.add(Property.forName(subCriteria.getAlias() +"."+ unionProOfSub).eqProperty(criteria.getAlias()+"." + unionPro));
        criteria.add(Subqueries.exists(subCriteria.setProjection(Projections.property(unionPro))));
        return criteria.list();
    }
}
