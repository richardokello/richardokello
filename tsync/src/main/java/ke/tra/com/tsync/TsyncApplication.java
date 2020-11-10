package ke.tra.com.tsync;

import ke.tra.com.tsync.h2pkgs.models.GeneralSettingsCache;
import ke.tra.com.tsync.h2pkgs.repo.GatewaySettingsCacheRepo;
import ke.tra.com.tsync.services.crdb.CRDBPipService;
import org.jpos.q2.Q2;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Date;

@ComponentScan({"entities","repository", "ke"})
@EntityScan({"entities", "ke"})
@EnableJpaRepositories({"repository","ke"})
@EnableAsync
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
//@EnableTransactionManagement
@EnableScheduling
public class TsyncApplication {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TsyncApplication.class);
    private static void startJposServer() {
        JposServer.getInstance();
    }

    @Autowired private CRDBPipService crdbPipService;
    @Autowired private GatewaySettingsCacheRepo gatewaySettingsCacheRepo;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    public TsyncApplication() {
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TsyncApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
        startJposServer();
        logger.info("Application started successfully");
    }


    @PostConstruct
    private void initAppCfgs(){
        initDb();
        getSessionKeyIni();
    }

    private void initDb() {

        //insert sample values
        gatewaySettingsCacheRepo.deleteAll();
        GeneralSettingsCache gs = new GeneralSettingsCache(1,"NA",false);
        gatewaySettingsCacheRepo.save(gs);

    }

    private void getSessionKeyIni(){
        logger.info("Attempting fetchSessionNumber from PIP:: Execution Time - {}", new Date().toString());
        String sessionStr = crdbPipService.getSessionDetailsOnline();
        logger.info("sessionStr {} " , sessionStr);
        GeneralSettingsCache gs= gatewaySettingsCacheRepo.findById(1L).get();
        gs.setCrdbSessionKey(sessionStr);
        if(!sessionStr.isEmpty())
            gs.setUpdated(true);
        gatewaySettingsCacheRepo.save(gs);

    }
}

class JposServer {
    private static volatile JposServer instance = null;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TsyncApplication.class);
    public static JposServer getInstance() {
        if (instance == null) {
            synchronized (JposServer.class) {
                // Double check that the class is not initialised for concurrency.
                if (instance == null) {
                    instance = new JposServer();
                }
            }
        }
        return instance;
    }

    private JposServer() {
        try {
            new Q2("config/jpos").start();
            logger.info(" ~~~~ ISO8583 Server started ~~~~ ");
        } catch (Exception e) {
            logger.error("Could not start ISO8583 Server", e);
            throw e;
        }
    }

}
