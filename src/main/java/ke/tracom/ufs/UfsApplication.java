package ke.tracom.ufs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaxxer.hikari.HikariDataSource;
import ke.tracom.ufs.config.FileStorageProperties;
import ke.tracom.ufs.config.ParseJsonFile;
import ke.tracom.ufs.config.multitenancy.MultiTenantDynamicTenantAwareRoutingSource;
import ke.tracom.ufs.config.multitenancy.TenantAwareRoutingSource;
import ke.tracom.ufs.utils.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

@Slf4j
@SpringBootApplication
@EnableEurekaClient
@EnableAsync
@EnableConfigurationProperties({FileStorageProperties.class})
public class UfsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UfsApplication.class, args);
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

    /**
     * Fetch the tenant json file containing the database configurations
     * @return
     * @throws IOException
     */
    @Bean
    public DataSource dataSource() {
        ParseJsonFile parser =  new ParseJsonFile();
        AbstractRoutingDataSource dataSource = new TenantAwareRoutingSource();
        String tenantJson = null;
        try {
            tenantJson = parser.parseJsonFile(AppConstants.TENANT_JSON_FILE_NAME).toString();
            if (tenantJson == null){
                System.out.printf(tenantJson);
                System.err.printf("An error occurred on datasource configuration, tenant json file is null after passing file");
            }
            MultiTenantDynamicTenantAwareRoutingSource routingSource = new MultiTenantDynamicTenantAwareRoutingSource(tenantJson);
            Map<Object, Object> tenants = routingSource.getTenants();

            dataSource.setTargetDataSources(tenants);
            dataSource.afterPropertiesSet();
        }catch (Exception e){
            e.printStackTrace();
            System.out.printf(tenantJson);
            System.err.printf("An error occurred on datasource configuration");
            System.exit(0);

        }
        return dataSource;
    }
}
