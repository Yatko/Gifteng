/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config.data;

import com.venefica.config.Constants;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    protected static final Log logger = LogFactory.getLog(TestDataConfig.class);
    
    @Resource
    private String jdbcDriver;
    @Resource
    private String jdbcUrl;
    @Resource
    private String jdbcUsername;
    @Resource
    private String jdbcPassword;
    
    @Resource
    private String hibernateDialect;
    @Resource
    private String hibernateHbmToDdlAuto = "update";
    @Resource
    private String hibernateHbmToDdlImportFiles = "import.sql";
    @Resource
    private Boolean hibernateShowSQL = true;
    @Resource
    private Boolean hibernateFormatSQL = true;
    
    @Resource
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
        
        if ( !hibernateProperties.isEmpty() ) {
            for ( Map.Entry<String, String> entry : hibernateProperties.entrySet() ) {
                String key = entry.getKey();
                String value = entry.getValue();
                factoryBuilder.getProperties().put(key, value);
                logger.debug("Setting property for hibernate session factory initialization (" + key + "=" + value + ")");
            }
        } else {
            logger.warn("Properties are empty when creating hibernate session factory");
        }

        return factoryBuilder.buildSessionFactory();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }
}
