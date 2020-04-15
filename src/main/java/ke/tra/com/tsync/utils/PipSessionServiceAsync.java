package ke.tra.com.tsync.utils;


import ke.tra.com.tsync.h2pkgs.models.GeneralSettingsCache;
import ke.tra.com.tsync.h2pkgs.repo.GatewaySettingsCacheRepo;
import ke.tra.com.tsync.services.crdb.CRDBPipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class PipSessionServiceAsync {


    @Autowired
    private CRDBPipService crdbPipService;
    @Autowired
    private GatewaySettingsCacheRepo gatewaySettingsCacheRepo;

    @Async
    public void refreshSessionNumber() {
        ReentrantLock lock = new ReentrantLock();
        boolean isLockAcquired = false;
        try {
            isLockAcquired = lock.tryLock(6, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isLockAcquired) {
            try {
                String sessionStr = crdbPipService.getSessionDetailsOnline();
                updateValidSessionString(sessionStr, gatewaySettingsCacheRepo);
            } finally {
                lock.unlock();
            }
        }
    }

    public static void updateValidSessionString(String sessionStr, GatewaySettingsCacheRepo gatewaySettingsCacheRepo) {
        try {
            GeneralSettingsCache gs = gatewaySettingsCacheRepo.findById(1L).get();
            gs.setCrdbSessionKey(sessionStr);
            if (!sessionStr.isEmpty()) {
                gs.setUpdated(false);
                gatewaySettingsCacheRepo.save(gs);
            } else {
                gs.setCrdbSessionKey(sessionStr);
                gs.setUpdated(true);
                gatewaySettingsCacheRepo.save(gs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
