/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.co.tra.ufs.tms;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ke.co.tra.ufs.tms.config.multitenancy.MultiTenantDynamicTenantAwareRoutingSource;
import ke.co.tra.ufs.tms.config.multitenancy.ParseJsonFile;
import ke.co.tra.ufs.tms.config.multitenancy.TenantAwareRoutingSource;
import ke.co.tra.ufs.tms.service.SysConfigService;
import ke.co.tra.ufs.tms.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author Owori Juma
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableEurekaClient
public class TMSApplication {

//    @Autowired
//    private DataSource dataSource;
    @Value("${baseUrl}")
    private String baseUrl;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(TMSApplication.class, args);
    }

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

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("TMS-THREAD");
        executor.initialize();
        return executor;
    }

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
    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(baseUrl + "ufs-common-modules/api/v1/oauth/check_token");
        tokenService.setClientId("common_module_client");
        tokenService.setClientSecret("secret");
        return tokenService;
    }
}
