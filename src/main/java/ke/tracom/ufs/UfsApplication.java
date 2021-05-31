package ke.tracom.ufs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaxxer.hikari.HikariDataSource;
import ke.tracom.ufs.config.FileStorageProperties;
import ke.tracom.ufs.config.MultiTenantDatabaseConfiguration;
import ke.tracom.ufs.config.MultiTenantDynamicTenantAwareRoutingSource;
import ke.tracom.ufs.config.TenantAwareRoutingSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
@EnableEurekaClient
@EnableAsync
//@EnableScheduling
//@EnableTransactionManagement
@EnableConfigurationProperties({FileStorageProperties.class})
public class UfsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UfsApplication.class, args);
//        new UfsApplication()
//                .configure(new SpringApplicationBuilder(UfsApplication.class))
//                .properties(getDefaultProperties())
//                .run(args);
    }

    @Bean
    @Primary
    public TokenStore jdbcTokenStore(DataSource dataSource) {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MappingJackson2HttpMessageConverter converter
                = new MappingJackson2HttpMessageConverter(mapper);
        return converter;
    }

    @Bean
    @Primary
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("UFS-THREAD");
        executor.initialize();
        return executor;
    }

    @Bean
    public DataSource dataSource() throws IOException {
//
//        File resource = new ClassPathResource("tenant.json").getFile();
//        String tenantJson = new String(Files.readAllBytes(resource.toPath()));
//        MultiTenantDynamicTenantAwareRoutingSource routingSource = new MultiTenantDynamicTenantAwareRoutingSource(tenantJson);
//        Map<String, HikariDataSource> tenants = routingSource.getTenants();

        Map<Object, Object> targetDataSources = new HashMap<>();
//        for (Map.Entry<String, HikariDataSource> entry : tenants.entrySet()) {
//            targetDataSources.put(entry.getKey() , entry.getValue());
        targetDataSources.put("crdbtz", getCRDBTZ());
        targetDataSources.put("crdbbi", getCRDBBI());
//        }

        AbstractRoutingDataSource dataSource = new TenantAwareRoutingSource();
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.afterPropertiesSet();

        return dataSource;
    }

    private HikariDataSource getCRDBBI() {

        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setInitializationFailTimeout(0);
        dataSource.setMaximumPoolSize(5);
        dataSource.setJdbcUrl("jdbc:oracle:thin:@41.215.130.247:5097:trcm");
        dataSource.setUsername("UFS_CRDB_BURUNDI_UAT");
        dataSource.setPassword("pass123");

        return dataSource;
    }

    private HikariDataSource getCRDBTZ() {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setInitializationFailTimeout(0);
        dataSource.setMaximumPoolSize(5);
        dataSource.setJdbcUrl("jdbc:oracle:thin:@41.215.130.247:5097:trcm");
        dataSource.setUsername("UFS_CRDB_UAT");
        dataSource.setPassword("pass123");

        return dataSource;
    }

    private static Properties getDefaultProperties() {

        Properties defaultProperties = new Properties();

        // Set sane Spring Hibernate properties:
        defaultProperties.put("spring.jpa.show-sql", "true");
        defaultProperties.put("spring.jpa.hibernate.naming.physical-strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
        defaultProperties.put("spring.datasource.initialize", "false");

        // Prevent JPA from trying to Auto Detect the Database:
        defaultProperties.put("spring.jpa.database", "oracle");

        // Prevent Hibernate from Automatic Changes to the DDL Schema:
        defaultProperties.put("spring.jpa.hibernate.ddl-auto", "none");

        return defaultProperties;
    }
}
