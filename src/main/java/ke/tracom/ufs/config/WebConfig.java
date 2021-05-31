/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.MappedInterceptor;

/**
 * @author eli
 */
@EnableWebMvc
@Configuration
@RequiredArgsConstructor
public class WebConfig
        extends WebMvcConfigurerAdapter{
//        implements WebMvcConfigurer {

//    private final TenantNameInterceptor tenantNameInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        System.err.println("aDD INTERCEPTOR");
//        registry.addInterceptor(tenantNameInterceptor);
//
//    }

//    @Bean
//    @Order(value = 1)
//    public MappedInterceptor dbEditorTenantInterceptor() {
//        return new MappedInterceptor(new String[]{"/**"}, tenantNameInterceptor);
//    }
}
