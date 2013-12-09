package com.venefica.config.data;

import com.venefica.config.Constants;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile(value = "production")
@EnableTransactionManagement
public class ProductionDataConfig {

    private final String hibernateDialect = "org.hibernate.spatial.dialect.mysql.MySQLSpatialInnoDBDialect";
    private final String hibernateHbmToDdlAuto = "update";
    private final Boolean hibernateShowSQL = false;
    
    @Inject
    private DataSource dataSource;
    
    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder factoryBuilder = new LocalSessionFactoryBuilder(dataSource);
        factoryBuilder.scanPackages(Constants.MODEL_PACKAGE);
        
        factoryBuilder.getProperties().put(AvailableSettings.DIALECT, hibernateDialect);
        factoryBuilder.getProperties().put(AvailableSettings.HBM2DDL_AUTO, hibernateHbmToDdlAuto);
        factoryBuilder.getProperties().put(AvailableSettings.SHOW_SQL, hibernateShowSQL);
        // For more details see:
        // http://stackoverflow.com/questions/782823/handling-datetime-values-0000-00-00-000000-in-jdbc
        // http://stackoverflow.com/questions/11133759/0000-00-00-000000-can-not-be-represented-as-java-sql-timestamp-error
        factoryBuilder.getProperties().put("hibernate.connection.zeroDateTimeBehavior", "convertToNull");

        return factoryBuilder.buildSessionFactory();
    }
}
