package co.ke.tracom.bprgateway.web.sendmoney.services;


import co.ke.tracom.bprgateway.web.sendmoney.entity.MoneySend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class TestBackgroundService {

    private final MoneySendTokenExpiryTimeService expiryTimeService;
    private static final Logger log = LoggerFactory.getLogger(TestBackgroundService.class);

    public TestBackgroundService(MoneySendTokenExpiryTimeService expiryTimeService) {
        this.expiryTimeService = expiryTimeService;
    }


    @Scheduled(cron = ("*/6 * 8-17,* * *   MON-FRI,SAT-SUN"))
    private void checkTokenExpiryTime() throws ExecutionException, InterruptedException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd h:m:sZ");
        log.info("The time is now {}", format.format(new Date()));
        CompletableFuture<List<MoneySend>> listMoneySend = expiryTimeService.check();
        //List<MoneySend> tt =listMoneySend.get();
    }
}
