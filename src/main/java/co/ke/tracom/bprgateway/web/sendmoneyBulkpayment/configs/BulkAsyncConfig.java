package co.ke.tracom.bprgateway.web.sendmoneyBulkpayment.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class BulkAsyncConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(executor.getMaxPoolSize());
        executor.setMaxPoolSize(executor.getMaxPoolSize());
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("Bulk-sendMoney-userThread");
        executor.initialize();
        return executor;
    }
}
