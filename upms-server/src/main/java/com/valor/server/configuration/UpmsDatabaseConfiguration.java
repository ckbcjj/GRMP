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
package com.valor.server.configuration;

import com.google.common.base.Strings;
import com.mfc.config.ConfigAESTools;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

@Configuration
@EnableTransactionManagement
public class UpmsDatabaseConfiguration extends AbstractDBConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(UpmsDatabaseConfiguration.class);
	private final static String DATABASE_MODEL_BASE_PACKAGE = "com.valor.server.db";

	private static String CRYPT_KEY = "Ebw6MyVpHAC23ZMszboDDQ";

	private String host = ConfigAESTools.getConfigAsString("com.valor.server.db.host", CRYPT_KEY);
	private String dbInst = ConfigAESTools.getConfigAsString("com.valor.server.db.inst", CRYPT_KEY);
	private String username = ConfigAESTools.getConfigAsString("com.valor.server.db.user", CRYPT_KEY);
	private String password = ConfigAESTools.getConfigAsString("com.valor.server.db.password", CRYPT_KEY);
	private String appendPackagesToScan = ConfigAESTools.getConfigAsString("com.valor.server.db.appendPackage",
			CRYPT_KEY);

	private String getConnectUrl() {
		if (Strings.isNullOrEmpty(host) || Strings.isNullOrEmpty(dbInst) || Strings.isNullOrEmpty(username)
				|| Strings.isNullOrEmpty(password)) {
			LOG.error("{}-{}-{}-{}", host, dbInst, username, password);
			throw new IllegalArgumentException("Invalid parameter");
		}

		return new StringBuilder().append("jdbc:mysql://").append(host).append("/").append(dbInst).append("?")
				.append("useUnicode=true&characterEncoding=utf8&autoReconnect=true").toString();
	}

	@Bean(destroyMethod = "close", name = "upmsDataSource")
	@Primary
	public DataSource upmsDataSource() {
		LOG.info("Connect database:{}:{}:{}", host, dbInst, username);
		return getDataSource(getConnectUrl(), username, password);
	}

	@Bean(destroyMethod = "destroy", name = "upmsSessionFactory")
	@Qualifier(value = "upmsSessionFactory")
	public FactoryBean<SessionFactory> upmsSessionFactory() {
		if (Strings.isNullOrEmpty(appendPackagesToScan)) {
			return getSessionFactory(upmsDataSource(), DATABASE_MODEL_BASE_PACKAGE);
		} else {
			return getSessionFactory(upmsDataSource(), DATABASE_MODEL_BASE_PACKAGE, appendPackagesToScan);
		}
	}

	@Bean(name = "upmsTransactionManager")
	@Qualifier(value = "upmsTransactionManager")
	public PlatformTransactionManager transactionManager() throws Exception {
		return getTransactionManager(upmsSessionFactory().getObject(), 3000);
	}

	@Override
	public Properties getDBProperties() {
		Properties props = new Properties();
		props.put("hibernate.dialect", dialect);
		props.put("hibernate.show_sql", showSql);
		props.put("hibernate.generate_statistics", generateStatistics);
		props.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
		props.put("hibernate.connection.isolation", TRANSACTION_READ_COMMITTED);
		props.put("hibernate.use_sql_comments", true);

		props.put("hibernate.connection.CharSet", "utf8");
		props.put("hibernate.connection.characterEncoding", "utf8");
		props.put("hibernate.connection.useUnicode", false);
		props.put("hibernate.autoReconnect", true);

		props.put("hibernate.cache.use_second_level_cache", false);
		props.put("hibernate.cache.use_query_cache", false);

		return props;
	}

	@Override
	public FactoryBean<SessionFactory> getSessionFactory(@Qualifier("upmsDataSource") DataSource dataSource,
														 String... scanPackages) {
		return super.getSessionFactory(dataSource, scanPackages);
	}

	@Override
	public PlatformTransactionManager getTransactionManager(
			@Qualifier("upmsSessionFactory") SessionFactory sessionFactory, int txTimeout) throws Exception {
		return super.getTransactionManager(sessionFactory, txTimeout);
	}

}
