package ke.tra.com.tsync;

import ke.tra.com.tsync.h2pkgs.models.GeneralSettingsCache;
import org.jpos.q2.Q2;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;


@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class TsyncApplication {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TsyncApplication.class);
    private static void startJposServer() {
        JposServer.getInstance();
    }

    @Autowired
    @Qualifier("h2JdbcTemplate")
    private JdbcTemplate jdbcTemplate;

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
    private void initDb() {
        System.out.println(String.format("****** Creating table: %s, and Inserting switch ip port settings ******", "GENERALSETTINGSCACHE"));
        String sqlStatements[] = {
                "drop table GENERALSETTINGSCACHE if exists",
                "create table GENERALSETTINGSCACHE(id int,sign_on_state varchar(10),last_name varchar(255))",
                "insert into GENERALSETTINGSCACHE(id, sign_on_state) values(1,'"+GeneralSettingsCache.SignOnState.SIGNON+"')",
        };

        Arrays.asList(sqlStatements).stream().forEach(sql -> {
            System.out.println(sql);
            jdbcTemplate.execute(sql);
        });

        System.out.println(String.format("****** Fetching from table: %s ******", "GENERALSETTINGSCACHE"));
        jdbcTemplate.query("select id,sign_on_state from GENERALSETTINGSCACHE",
                (rs, i) -> {
                    System.out.println(String.format("id:%s,sign_on_state:%s",
                            rs.getString("id"),
                            rs.getString("sign_on_state")));
                    return null;
                });
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
