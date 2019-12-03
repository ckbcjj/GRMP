/*
 *      Copyright (c) 2004-2015 YAMJ Members
 *      https://github.com/organizations/YAMJ/teams
 *
 *      This file is part of the Yet Another Media Jukebox (YAMJ).
 *
 *      YAMJ is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      YAMJ is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with YAMJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 *      Web: https://github.com/YAMJ/yamj-v3
 *
 */
package com.valor.server.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

/**
 * Hibernate DAO implementation
 */
public abstract class UpmsBaseDao {
    private static final Logger logger = LoggerFactory.getLogger(UpmsBaseDao.class);

    @Autowired
    @Qualifier("upmsSessionFactory")
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session currentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
     * Flush and clear the session.
     */
    public void flushAndClear() {
        Session session = currentSession();
        session.flush();
        session.clear();
    }

    /**
     * store an entity.
     *
     * @param entity the entity to store
     */
    public void storeEntity(final Object entity) {
        currentSession().saveOrUpdate(entity);
    }

    /**
     * store an entity if absent.
     *
     * @param entity the entity to store
     */
    public void storeEntityIfAbsent(final Object entity) {
        Session session = currentSession();
        doStoreEntityIfAbsent(entity, session);
    }

    private void doStoreEntityIfAbsent(final Object entity, Session session) {
        boolean exists = session.contains(entity);
        if (!exists) {
            session.saveOrUpdate(entity);
        }
    }

    /**
     * Merge an entity.
     *
     * @param entity the entity to merge
     */
    public Object mergeEntity(final Object entity) {
        return currentSession().merge(entity);
    }

    public void mergeAll(final Collection entities) {
        if (entities != null && !entities.isEmpty()) {
            Session session = currentSession();
            for (Object entity : entities) {
                session.merge(entity);
            }
        }
    }

    /**
     * store all entities.
     *
     * @param entities the entities to store
     */
    @SuppressWarnings("rawtypes")
    public void storeAll(final Collection entities) {
        if (entities != null && !entities.isEmpty()) {
            Session session = currentSession();
            for (Object entity : entities) {
                session.saveOrUpdate(entity);
            }
        }
    }

    /**
     * store every entity if absent.
     *
     * @param entities the entities to store
     */
    @SuppressWarnings("rawtypes")
    public void storeAllIfAbsent(final Collection entities) {
        if (entities != null && !entities.isEmpty()) {
            Session session = currentSession();
            for (Object entity : entities) {
                doStoreEntityIfAbsent(entity, session);
            }
        }
    }

    /**
     * Delete all entities.
     *
     * @param entities the entities to delete
     */
    @SuppressWarnings("rawtypes")
    public void deleteAll(final Collection entities) {
        if (entities != null && !entities.isEmpty()) {
            Session session = currentSession();
            for (Object entity : entities) {
                session.delete(entity);
            }
        }
    }

    /**
     * Save an entity.
     *
     * @param entity the entity to save
     */
    public Serializable saveEntity(final Object entity) {
        return currentSession().save(entity);
    }

    /**
     * Update an entity.
     *
     * @param entity the entity to update
     */
    public void updateEntity(final Object entity) {
        currentSession().update(entity);
    }

    /**
     * Delete an entity.
     *
     * @param entity the entity to delete
     */
    public void deleteEntity(final Object entity) {
        currentSession().delete(entity);
    }

    /**
     * Get a single object by its id
     *
     * @param <T>
     * @param entityClass
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getById(Class<T> entityClass, Serializable id) {
        return (T) currentSession().get(entityClass, id);
    }

    /**
     * Get a single object by the passed field using the name case sensitive.
     *
     * @param <T>
     * @param entityClass
     * @param field
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getByNaturalId(Class<? extends T> entityClass, String field, String name) {
        return (T) currentSession().byNaturalId(entityClass).using(field, name).load();
    }

    /**
     * Get a single object by the passed field using the name case insensitive.
     *
     * @param <T>
     * @param entityClass
     * @param field
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getByNaturalIdCaseInsensitive(Class<? extends T> entityClass, String field, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("from ");
        sb.append(entityClass.getSimpleName());
        sb.append(" where lower(").append(field).append(") = :name) { ");

        Map<String, Object> params = Collections.singletonMap("name", (Object) name.toLowerCase());
        return (T) this.findUniqueByNamedParameters(sb, params);
    }

    /**
     * Convert row object to a string.
     *
     * @param rowElement
     * @return <code>String</code>
     */
    protected static String convertRowElementToString(Object rowElement) {
        if (rowElement == null) {
            return null;
        } else if (rowElement instanceof String) {
            return (String) rowElement;
        } else {
            return rowElement.toString();
        }
    }

    /**
     * Convert row object to Integer.
     *
     * @param rowElement
     * @return <code>Integer</code>
     */
    protected static Integer convertRowElementToInteger(Object rowElement) {
        if (rowElement == null) {
            return 0;
        } else if (StringUtils.isNumeric(rowElement.toString())) {
            return Integer.valueOf(rowElement.toString());
        } else {
            return 0;
        }
    }

    /**
     * Convert row object to Long.
     *
     * @param rowElement
     * @return <code>Long</code>
     */
    protected static Long convertRowElementToLong(Object rowElement) {
        if (rowElement == null) {
            return 0L;
        } else if (StringUtils.isNumeric(rowElement.toString())) {
            return Long.valueOf(rowElement.toString());
        } else {
            return 0L;
        }
    }

    /**
     * Convert row object to date.
     *
     * @param rowElement
     * @return
     */
    protected static Date convertRowElementToDate(Object rowElement) {
        if (rowElement == null) {
            return null;
        } else if (rowElement instanceof Date) {
            return (Date) rowElement;
        } else {
            // TODO invalid date
            return null;
        }
    }

    /**
     * Convert row object to date.
     *
     * @param rowElement
     * @return
     */
    protected static Timestamp convertRowElementToTimestamp(Object rowElement) {
        if (rowElement == null) {
            return null;
        } else if (rowElement instanceof Timestamp) {
            return (Timestamp) rowElement;
        } else {
            // TODO invalid timestamp
            return null;
        }
    }

    /**
     * Convert row object to big decimal.
     *
     * @param rowElement
     * @return <code>BigDecimal</code>
     */
    protected static BigDecimal convertRowElementToBigDecimal(Object rowElement) {
        if (rowElement == null) {
            return BigDecimal.ZERO;
        } else if (rowElement instanceof BigDecimal) {
            return (BigDecimal) rowElement;
        } else {
            return new BigDecimal(rowElement.toString());
        }
    }

    @SuppressWarnings("rawtypes")
    protected static void applyNamedParameterToQuery(Query queryObject, String paramName, Object value) throws HibernateException {
        if (value instanceof Collection) {
            queryObject.setParameterList(paramName, (Collection) value);
        } else if (value instanceof Object[]) {
            queryObject.setParameterList(paramName, (Object[]) value);
        } else if (value instanceof String) {
            queryObject.setString(paramName, (String) value);
        } else {
            queryObject.setParameter(paramName, value);
        }
    }

    /**
     * Find entries-
     *
     * @param queryString the manage string
     * @return list of entities
     */
    @SuppressWarnings("rawtypes")
    public List find(CharSequence queryString) {
        Query queryObject = currentSession().createQuery(queryString.toString());
        queryObject.setCacheable(true);
        return queryObject.list();
    }

    /**
     * Find entries by id.
     *
     * @param queryString the manage string
     * @param id          the id
     * @return list of entities
     */
    @SuppressWarnings("rawtypes")
    public List findById(CharSequence queryString, Long id) {
        Query queryObject = currentSession().createQuery(queryString.toString());
        queryObject.setCacheable(true);
        queryObject.setParameter("id", id);
        return queryObject.list();
    }


    /**
     * Find unique entity by named parameters.
     *
     * @param queryCharSequence the manage string
     * @param params            the named parameters
     * @return list of entities
     */
    public Object findUniqueByNamedParameters(CharSequence queryCharSequence, Map<String, Object> params) {
        Query query = currentSession().createQuery(queryCharSequence.toString());
        query.setCacheable(true);
        for (Entry<String, Object> param : params.entrySet()) {
            applyNamedParameterToQuery(query, param.getKey(), param.getValue());
        }
        return query.uniqueResult();
    }

    /**
     * Execute an update statement.
     *
     * @param queryCharSequence the manage string
     * @param params            the named parameters
     * @return number of affected rows
     */
    public int executeUpdate(CharSequence queryCharSequence, Map<String, Object> params) {
        Query query = currentSession().createQuery(queryCharSequence.toString());
        query.setCacheable(true);
        for (Entry<String, Object> param : params.entrySet()) {
            applyNamedParameterToQuery(query, param.getKey(), param.getValue());
        }
        return query.executeUpdate();
    }

    /**
     * Execute an update statement.
     *
     * @param queryCharSequence the manage string
     * @param params            the named parameters
     * @return number of affected rows
     */
    public int executeSqlUpdate(CharSequence queryCharSequence, Map<String, Object> params) {
        SQLQuery query = currentSession().createSQLQuery(queryCharSequence.toString());
        query.setCacheable(true);
        for (Entry<String, Object> param : params.entrySet()) {
            applyNamedParameterToQuery(query, param.getKey(), param.getValue());
        }
        return query.executeUpdate();
    }

    private void setResultTransformer(Class T, SQLQuery query) {
        if (T != null) {
            if (T.equals(String.class)) {
                // no transformer needed
            } else if (T.equals(Long.class)) {
                // no transformer needed
            } else if (T.equals(Integer.class)) {
                // no transformer needed
            } else if (T.equals(Object[].class)) {
                // no transformer needed
            } else {
                query.setResultTransformer(Transformers.aliasToBean(T));
            }
        }
    }

    public <T> List<T> query(Class T, SqlScalars sqlScalars) {
        SQLQuery query = sqlScalars.createSqlQuery(currentSession());
        query.setReadOnly(true);
        query.setCacheable(true);
        setResultTransformer(T, query);
        sqlScalars.populateScalars(query);
        return query.list();
    }

    public List<Object[]> queryObjects(String sql, Map<String, Object> params) {
        Query query = currentSession().createSQLQuery(sql);
        query.setCacheable(true);
        for (Entry<String, Object> param : params.entrySet()) {
            applyNamedParameterToQuery(query, param.getKey(), param.getValue());
        }
        List list = query.list();
        return list;
    }

    public long getCountBySql(String sql, Map<String, Object> params) {
        Query query = currentSession().createSQLQuery(sql);
        query.setCacheable(true);
        for (Entry<String, Object> param : params.entrySet()) {
            applyNamedParameterToQuery(query, param.getKey(), param.getValue());
        }
        return Long.parseLong(query.list().get(0).toString());
    }
}

