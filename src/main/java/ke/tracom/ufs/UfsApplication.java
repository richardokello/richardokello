package ke.tracom.ufs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaxxer.hikari.HikariDataSource;
import ke.tracom.ufs.config.FileStorageProperties;
import ke.tracom.ufs.config.ParseJsonFile;
import ke.tracom.ufs.config.multitenancy.*;
import ke.tracom.ufs.utils.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Executor;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableAsync
@EnableConfigurationProperties({FileStorageProperties.class, MultiTenantDbConfigProperties.class})
public class UfsApplication {

    @Autowired
    MultiTenantDbConfigProperties multiTenantDbConfigProperties;

    @Autowired
    StringEncryptor jasyptStringEncryptor;

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

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("secret_key123456");
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        return encryptor;
    }



//    @Bean
//    @Primary
//    public Executor asyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(2);
//        executor.setMaxPoolSize(2);
//        executor.setQueueCapacity(500);
//        executor.setThreadNamePrefix("UFS-THREAD");
//        executor.initialize();
//        return executor;
//    }

    /**
     * Fetch the tenant json file containing the database configurations
     * @return
     * @throws IOException
     */
    @Bean
    public DataSource dataSource() {
        AbstractRoutingDataSource dataSource = new TenantAwareRoutingSource();
        MultiTenantDynamicTenantAwareRoutingSource routingSource = new MultiTenantDynamicTenantAwareRoutingSource(getDatasourceConfigs(), stringEncryptor());
        Map<Object, Object> tenants = routingSource.getTenants();
        dataSource.setTargetDataSources(tenants);
        dataSource.afterPropertiesSet();
        return dataSource;
    }

    public MultiTenantDatabaseConfiguration[] getDatasourceConfigs() {
        MultiTenantDatabaseConfiguration[] configs = new MultiTenantDatabaseConfiguration[multiTenantDbConfigProperties.getTenants().length];

        for (int i = 0; i < multiTenantDbConfigProperties.getTenants().length; i++) {
            MultiTenantDatabaseConfiguration config = new MultiTenantDatabaseConfiguration(
                    multiTenantDbConfigProperties.getTenants()[i],
                    multiTenantDbConfigProperties.getUrl(),
                    multiTenantDbConfigProperties.getUsers()[i],
                    multiTenantDbConfigProperties.getDataSourceClassName(),
                    multiTenantDbConfigProperties.getPassword()[i],
                    (multiTenantDbConfigProperties.getDefaultValue() == i)
            );
            configs[i] = config;
        }

        System.out.println("Database configs >>>>  " + Arrays.toString(configs));

        return configs;
    }

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("X-TenantID", ThreadLocalStorage.getTenantName());
            headers.add("X-Language", ThreadLocalStorage.getLanguage());
            return execution.execute(request, body);
        });

        interceptors.add((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.forEach((key, value) -> System.out.println("Rest Template Header " + key + " : " + value));
            return execution.execute(request, body);
        });
        RestTemplate template = new RestTemplate();
        template.setInterceptors(interceptors);
        return template;
    }
}
