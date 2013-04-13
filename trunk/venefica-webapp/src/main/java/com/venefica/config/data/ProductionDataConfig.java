package com.venefica.config.data;

import com.venefica.config.Constants;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
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

    @Inject
    DataSource dataSource;
    
    @Inject
    private Map<String, String> hibernateProperties = new HashMap<String, String>(0);

    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder factoryBuilder = new LocalSessionFactoryBuilder(dataSource)
                .scanPackages(Constants.MODEL_PACKAGE);

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
