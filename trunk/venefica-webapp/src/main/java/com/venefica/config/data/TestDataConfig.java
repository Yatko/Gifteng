package com.venefica.config.data;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile(value = "test")
@EnableTransactionManagement
public class TestDataConfig {
	private static final String ModelPackage = "com.venefica.model";

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost/venefica-tst");
		dataSource.setUsername("venefica");
		dataSource.setPassword("venefica");
		return dataSource;
	}

	@Bean
	public SessionFactory sessionFactory() {
		LocalSessionFactoryBuilder factoryBuilder = new LocalSessionFactoryBuilder(dataSource())
				.scanPackages(ModelPackage);

		// Support PostGIS functions
		factoryBuilder.getProperties().put(AvailableSettings.DIALECT,
				"org.hibernate.spatial.dialect.postgis.PostgisDialect");

		// Show SQL
		factoryBuilder.getProperties().put(AvailableSettings.SHOW_SQL, true);
		factoryBuilder.getProperties().put(AvailableSettings.FORMAT_SQL, true);

		// Recreate the database
		factoryBuilder.setProperty(AvailableSettings.HBM2DDL_AUTO, "create");

		return factoryBuilder.buildSessionFactory();
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager() {
		return new HibernateTransactionManager(sessionFactory());
	}
}
