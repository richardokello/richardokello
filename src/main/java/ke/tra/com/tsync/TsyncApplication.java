package ke.tra.com.tsync;

import ke.tra.com.tsync.h2pkgs.models.GeneralSettingsCache;
import ke.tra.com.tsync.h2pkgs.repo.GatewaySettingsCacheRepo;
import ke.tra.com.tsync.services.crdb.CRDBPipService;
import org.jpos.q2.Q2;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;

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

    @Autowired
    //@Qualifier("h2JdbcTemplate")
    private JdbcTemplate jdbcTemplate;

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
        //initDb();
        //getSessionKeyIni();
    }

    private void initDb() {

        //insert sample values
        System.out.println(String.format("****** Creating table: {} s, and Inserting switch crdb_session_str   updated settings ******", "GENERALSETTINGSCACHE"));
        String sqlStatements[] = {
                "drop table GENERALSETTINGSCACHE if exists",
                "create table GENERALSETTINGSCACHE(id int,crdb_session_str varchar(100),updated boolean)",
                "insert into GENERALSETTINGSCACHE(id, crdb_session_str,updated) values(1,'NA',false)",
        };

        Arrays.asList(sqlStatements).stream().forEach(sql -> {
            System.out.println(sql);
            jdbcTemplate.execute(sql);
        });

        System.out.println(String.format("****** Fetching from table: %s ******", "GENERALSETTINGSCACHE"));
        jdbcTemplate.query("select id,crdb_session_str,updated from GENERALSETTINGSCACHE",
            (rs, i) -> {
                System.out.printf(
                        String.format("\nid:%s, crdb_session_str:%s, updated:%s  ",
                        rs.getString("id"),
                        rs.getString("crdb_session_str"),
                        rs.getBoolean("updated")
                        )
                );
                return null;
            }
        );
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
