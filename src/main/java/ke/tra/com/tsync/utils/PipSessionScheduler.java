package ke.tra.com.tsync.utils;


import ke.tra.com.tsync.h2pkgs.models.GeneralSettingsCache;
import ke.tra.com.tsync.h2pkgs.repo.GatewaySettingsCache;
import ke.tra.com.tsync.services.CRDBPipService;
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

    @Autowired private GatewaySettingsCache gatewaySettingsCache;
    @Autowired private CRDBPipService crdbPipService;

    @Scheduled(cron = "0 0 6 * * *" ,zone="Africa/Djibouti")
    private void fetchSessionNumber() {
        try {
            logger.info("Attempting fetchSessionNumber from PIP:: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
            String sessionStr = crdbPipService.getSessionDetails();
            logger.info("sessionStr {} " , sessionStr);
            GeneralSettingsCache gs= gatewaySettingsCache.findById(1L).get();
            gs.setCrdbSessionKey(sessionStr);
           // String getCrdbSessionKey =  gs.getCrdbSessionKey();
            if(!sessionStr.isEmpty()){
                gs.setUpdated(false);
                gatewaySettingsCache.save(gs);
            } else{
                gs.setCrdbSessionKey(sessionStr);
                gs.setUpdated(true);
                gatewaySettingsCache.save(gs);
            }
        } catch (Exception e) {
            logger.error("PipSessionScheduler fetchSessionNumber error: {} " ,e);
            e.printStackTrace();
        }
    }
}
