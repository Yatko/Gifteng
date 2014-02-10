package com.venefica.config.data;

import com.venefica.config.Constants;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile(value = "development")
@EnableTransactionManagement
public class DevelopmentDataConfig {
    
    private final String hibernateDialect = "org.hibernate.spatial.dialect.postgis.PostgisDialect";
    private final String hibernateHbmToDdlAuto = "update";
    private final Boolean hibernateShowSQL = true;
    private final Boolean hibernateFormatSQL = true;
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost/venefica-dev");
        dataSource.setUsername("venefica");
        dataSource.setPassword("venefica");
        return dataSource;
    }
    
    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder factoryBuilder = new LocalSessionFactoryBuilder(dataSource());
        factoryBuilder.scanPackages(Constants.MODEL_PACKAGE);
        // Support PostGIS functions
        factoryBuilder.getProperties().put(AvailableSettings.DIALECT, hibernateDialect);
        // Automatically generate DDL
        factoryBuilder.getProperties().put(AvailableSettings.HBM2DDL_AUTO, hibernateHbmToDdlAuto);
        // Show SQL
        factoryBuilder.getProperties().put(AvailableSettings.SHOW_SQL, hibernateShowSQL);
        factoryBuilder.getProperties().put(AvailableSettings.FORMAT_SQL, hibernateFormatSQL);

        return factoryBuilder.buildSessionFactory();
    }
}
