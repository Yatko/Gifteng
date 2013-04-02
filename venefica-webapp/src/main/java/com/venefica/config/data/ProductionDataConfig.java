package com.venefica.config.data;

import javax.inject.Inject;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile(value = "production")
@EnableTransactionManagement
@ImportResource("classpath:jdbc.xml")
public class ProductionDataConfig {

    private static final String ModelPackage = "com.venefica.model";
    
    @Inject
    DataSource dataSource;

    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder factoryBuilder = new LocalSessionFactoryBuilder(dataSource)
                .scanPackages(ModelPackage);

        // Support GIS functions
        factoryBuilder.getProperties().put(AvailableSettings.DIALECT,
                //"org.hibernate.spatial.dialect.mysql.MySQLSpatialDialect"
                "org.hibernate.spatial.dialect.mysql.MySQLSpatialInnoDBDialect");

        // Automatically generate DDL
        factoryBuilder.getProperties().put(AvailableSettings.HBM2DDL_AUTO, "update");
        
        // For more details see:
        // http://stackoverflow.com/questions/782823/handling-datetime-values-0000-00-00-000000-in-jdbc
        // http://stackoverflow.com/questions/11133759/0000-00-00-000000-can-not-be-represented-as-java-sql-timestamp-error
        factoryBuilder.getProperties().put("hibernate.connection.zeroDateTimeBehavior", "convertToNull");
        
        // Don't show SQL
        factoryBuilder.getProperties().put(AvailableSettings.SHOW_SQL, true);

        return factoryBuilder.buildSessionFactory();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }
}
