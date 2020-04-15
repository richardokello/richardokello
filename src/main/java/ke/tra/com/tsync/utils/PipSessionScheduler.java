package ke.tra.com.tsync.utils;


import ke.tra.com.tsync.h2pkgs.models.GeneralSettingsCache;
import ke.tra.com.tsync.h2pkgs.repo.GatewaySettingsCacheRepo;
import ke.tra.com.tsync.services.crdb.CRDBPipService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PipSessionScheduler {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PipSessionScheduler.class.getName());
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd M yyyy HH:mm:ss");

    @Autowired private GatewaySettingsCacheRepo gatewaySettingsCacheRepo;
    @Autowired private CRDBPipService crdbPipService;

    @Scheduled(cron = "0 15 6 * * *" ,zone="GMT+3:00")
    private void fetchSessionNumber() {
        try {
            logger.info("Attempting fetchSessionNumber from PIP:: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
            String sessionStr = crdbPipService.getSessionDetailsOnline();
            logger.info("sessionStr {} " , sessionStr);
            PipSessionServiceAsync.updateValidSessionString(sessionStr, gatewaySettingsCacheRepo);
        } catch (Exception e) {
            logger.info("PipSessionScheduler fetchSessionNumber error: {} " ,e);
            logger.error("PipSessionScheduler fetchSessionNumber error: {} " ,e);
            e.printStackTrace();
        }
    }
}
