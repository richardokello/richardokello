/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.config;

import ke.tra.com.tsync.RequestHandlers;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Owori Juma
 */
@Configuration
public class ProjectSetUp {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProjectSetUp.class);

    @Bean
    public RequestHandlers requestHandlers() {
        logger.info("Start Iso server");
        return new RequestHandlers();
    }
    
    //vinny
    @Bean
    public RestTemplate restTemplate() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(7000);
        factory.setReadTimeout(7000);
        return new RestTemplate(factory);
    }

}
