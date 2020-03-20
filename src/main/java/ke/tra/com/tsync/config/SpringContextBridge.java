/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.com.tsync.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ke.tra.com.tsync.services.template.CoreProcessorTemplate;

@Component
public class SpringContextBridge implements SpringContextBridgedServices, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Autowired
    private CoreProcessorTemplate coreProcessor;

    @Override
    public CoreProcessorTemplate getcoreProcessor() {
        return coreProcessor;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        SpringContextBridge.applicationContext = ac;
    }

    public static SpringContextBridgedServices services() {
        return applicationContext.getBean(SpringContextBridgedServices.class);
    }

}
