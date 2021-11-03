package ke.tra.ufs.webportal.scheduler;

import ke.tra.ufs.webportal.service.CustomerService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@CommonsLog
public class UpdateMerchantMids {

    private final CustomerService customerService;

    public UpdateMerchantMids(CustomerService customerService) {
        this.customerService = customerService;
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void updateMids() {
        log.error("Starting to update MIDS");
        customerService.updateCustomersMids();
    }

    @Bean
    @Primary
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

}
