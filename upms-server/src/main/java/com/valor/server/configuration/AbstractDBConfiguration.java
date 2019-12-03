package com.valor.server.configuration;


import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

/**
 * Created by Frank.Huang on 2016/4/23.
 */
public abstract class AbstractDBConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDBConfiguration.class);

    protected String driverClassName="com.mysql.jdbc.Driver";
    protected String dialect= "org.hibernate.dialect.MySQL5Dialect";
    protected boolean poolPreparedStatements=true;
    protected String validationQuery="SELECT 1";
    protected int initialSize=10;
    protected int maxActive=500;
    protected int minIdle=10;
    protected int maxIdle=20;
    protected long maxWait=500;
    protected long minEvictableIdleTimeMillis=60000;
    protected long timeBetweenEvictionRunsMillis=60000;
    protected int numTestsPerEvictionRun=50;
    protected boolean testOnBorrow=true;
    protected boolean testWhileIdle=true;
    protected boolean testOnReturn=true;
    protected boolean useL2Cache=false;
    protected boolean useQueryCache=false;
    protected String l2CacheFactory="org.hibernate.cache.ehcache.EhCacheRegionFactory";
    protected boolean showSql=false;
    protected boolean generateStatistics=false;
    protected String hbm2ddlAuto="update";

    
    public DataSource getDataSource(String dburl, String username, String password) {
        BasicDataSource datasource = new BasicDataSource();
        datasource.setUrl(dburl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setValidationQuery(validationQuery);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setInitialSize(initialSize);
        datasource.setMaxTotal(maxActive);
        datasource.setMinIdle(minIdle);
        datasource.setMaxIdle(maxIdle);
        datasource.setMaxWaitMillis(maxWait);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);

        datasource.setLogAbandoned(true);
        datasource.setRemoveAbandonedOnBorrow(true);
        datasource.setRemoveAbandonedOnMaintenance(true);
        datasource.setRemoveAbandonedTimeout(120);
        
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnReturn(testOnReturn);

        datasource.setDefaultTransactionIsolation(TRANSACTION_READ_COMMITTED);
        return datasource;
    }

    public Properties getDBProperties(){
        Properties props = new Properties();
        props.put("hibernate.dialect", dialect);
        props.put("hibernate.show_sql", showSql);
        props.put("hibernate.format_sql",false);
        props.put("hibernate.generate_statistics", generateStatistics);
        props.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        props.put("hibernate.connection.isolation", TRANSACTION_READ_COMMITTED);
        props.put("hibernate.use_sql_comments", true);
        props.put("hibernate.cache.use_second_level_cache", false);
        props.put("hibernate.cache.use_query_cache", false);
        props.put("hibernate.connection.CharSet", "utf8");
        props.put("hibernate.connection.characterEncoding", "utf8");
        props.put("hibernate.connection.useUnicode", true);
        props.put("hibernate.autoReconnect", true);
        return props;
    }

    public FactoryBean<SessionFactory> getSessionFactory(DataSource dataSource,String...scanPackages) {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setEntityInterceptor(new AuditInterceptor());
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setPackagesToScan(scanPackages);
        sessionFactoryBean.setHibernateProperties(getDBProperties());
        return sessionFactoryBean;
    }

    public PlatformTransactionManager getTransactionManager(SessionFactory sessionFactory,int txTimeout) throws Exception {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        transactionManager.setDefaultTimeout(txTimeout);
        return transactionManager;
    }

}
