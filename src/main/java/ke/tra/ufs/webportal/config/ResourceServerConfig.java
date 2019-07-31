/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tra.ufs.webportal.config;

import ke.tra.ufs.webportal.security.AuthenticationFilter;
import ke.tra.ufs.webportal.utils.ResponseFilter;
import ke.tra.ufs.webportal.utils.filters.OTPFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author eli.muraya
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationFilter authFilter;


    public ResourceServerConfig(AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
        authFilter = new AuthenticationFilter();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers(CorsUtils::isCorsRequest).permitAll()
                .antMatchers("/**").fullyAuthenticated()
                .and()
                .addFilterBefore(authFilter, ExceptionTranslationFilter.class)
                .cors()
                .configurationSource(corsConfig())
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
    }


    @Bean
    CorsConfigurationSource corsConfig() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.applyPermitDefaultValues();
        corsConfig.addAllowedHeader("Access-Control-Allow-Origin");
        corsConfig.addExposedHeader("Access-Control-Allow-Origin");
        corsConfig.addAllowedMethod(HttpMethod.GET);
        corsConfig.addAllowedMethod(HttpMethod.POST);
        corsConfig.addAllowedMethod(HttpMethod.PUT);
        corsConfig.addAllowedMethod(HttpMethod.OPTIONS);
        corsConfig.addAllowedMethod(HttpMethod.DELETE);
        corsConfig.addAllowedMethod(HttpMethod.HEAD);
        corsConfig.addAllowedOrigin("*");
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}

