/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import ke.co.tra.ufs.tms.config.multitenancy.*;
import ke.co.tra.ufs.tms.service.SysConfigService;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author Owori Juma
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableEurekaClient
@EnableConfigurationProperties(MultiTenantDbConfigProperties.class)
@EnableEncryptableProperties
public class TMSApplication {

    @Autowired
    MultiTenantDbConfigProperties multiTenantDbConfigProperties;

    @Value("${baseUrl}")
    private String baseUrl;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(TMSApplication.class, args);
    }

    @Bean
    public DataSource dataSource() {
        AbstractRoutingDataSource dataSource = new TenantAwareRoutingSource();
        MultiTenantDynamicTenantAwareRoutingSource routingSource = new MultiTenantDynamicTenantAwareRoutingSource(getDatasourceConfigs());
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

        System.out.println("configurations " + Arrays.toString(configs));

        return configs;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("tDKEOF#tRCUCOM!STUFfs.");
        return accessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public TokenStore inMemoryStore() {
        return new InMemoryTokenStore();
    }

//    @Bean
//    @Primary
//    public TokenStore jdbcTokenStore() {
//        return new JdbcTokenStore(dataSource);
//    }

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
//
//    @Bean
//    public Executor asyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(2);
//        executor.setMaxPoolSize(2);
//        executor.setQueueCapacity(500);
//        executor.setThreadNamePrefix("TMS-THREAD");
//        executor.initialize();
//        return executor;
//    }

    @Bean
    @Primary
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean
    @Primary
    public JavaMailSender mailConfiguration(SysConfigService configService) {
        log.debug("=============== Intializing Mail Configuration ==================\n"
                + "Mail configuration size: " + configService.getMailConfig().size());
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties props = mailSender.getJavaMailProperties();
        configService.getMailConfig().forEach(config -> {
            if (config.getParameter().equalsIgnoreCase("mailHost")) {
                mailSender.setHost(config.getValue());
            } else if (config.getParameter().equalsIgnoreCase("mailPort")) {
                mailSender.setPort(Integer.valueOf(config.getValue()));
            } else if (config.getParameter().equalsIgnoreCase("mailUsername")) {
                mailSender.setUsername(config.getValue());
            } else if (config.getParameter().equalsIgnoreCase("mailPassword")) {
                mailSender.setPassword(config.getValue());
            } else if (config.getParameter().equalsIgnoreCase("mailSSL")) {
                String sslEnabled = "0".equals(config.getValue()) ? "false" : "true";
                props.put("mail.smtp.starttls.enable", sslEnabled);
                props.put("mail.smtp.ssl.enable", sslEnabled);
            } else if (config.getParameter().equalsIgnoreCase("mailAuth")) {
                String auth = "0".equals(config.getValue()) ? "false" : "true";
                props.put("mail.smtp.auth", auth);
            }
        });
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.fallback", "true");
        return mailSender;
    }

    @Primary
    @Bean()
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(baseUrl + "ufs-common-modules/api/v1/oauth/check_token");
        tokenService.setClientId("common_module_client");
        tokenService.setClientSecret("secret");
        tokenService.setRestTemplate(restTemplate());
        return tokenService;
    }

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("X-TenantID", ThreadLocalStorage.getTenantName());
            headers.add("X-Language", ThreadLocalStorage.getLanguage());
            return execution.execute(request, body);
        });

        interceptors.add((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.forEach((key, value) -> System.out.println("Rest Template Header >>>>>>>>>> " + key + " : " + value));
            return execution.execute(request, body);
        });
        RestTemplate template = new RestTemplate();
        template.setInterceptors(interceptors);
        return template;
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
}
