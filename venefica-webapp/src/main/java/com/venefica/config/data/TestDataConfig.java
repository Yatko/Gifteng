/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config.data;

import com.venefica.config.Constants;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
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

/**
 * Test environment to enable full configuration via injection from spring
 * XML definitions.
 * 
 * @author gyuszi
 */
@Configuration
@Profile(value = "test")
@EnableTransactionManagement
public class TestDataConfig {

    @Inject
    private String jdbcDriver;
    @Inject
    private String jdbcUrl;
    @Inject
    private String jdbcUsername;
    @Inject
    private String jdbcPassword;
    
    @Inject
    private String hibernateDialect;
    @Inject
    private String hibernateHbmToDdlAuto = "update";
    @Inject
    private String hibernateHbmToDdlImportFiles = "import.sql";
    @Inject
    private Boolean hibernateShowSQL = true;
    @Inject
    private Boolean hibernateFormatSQL = true;
    @Inject
    private Map<String, String> hibernateProperties = new HashMap<String, String>(0);
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }

    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder factoryBuilder = new LocalSessionFactoryBuilder(dataSource())
                .scanPackages(Constants.MODEL_PACKAGE);

        factoryBuilder.getProperties().put(AvailableSettings.DIALECT, hibernateDialect);
        factoryBuilder.getProperties().put(AvailableSettings.HBM2DDL_AUTO, hibernateHbmToDdlAuto);
        factoryBuilder.getProperties().put(AvailableSettings.HBM2DDL_IMPORT_FILES, hibernateHbmToDdlImportFiles);
        factoryBuilder.getProperties().put(AvailableSettings.SHOW_SQL, hibernateShowSQL);
        factoryBuilder.getProperties().put(AvailableSettings.FORMAT_SQL, hibernateFormatSQL);
        
        for ( Map.Entry<String, String> entry : hibernateProperties.entrySet() ) {
            String key = entry.getKey();
            String value = entry.getValue();
            factoryBuilder.getProperties().put(key, value);
        }

        return factoryBuilder.buildSessionFactory();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }
}
