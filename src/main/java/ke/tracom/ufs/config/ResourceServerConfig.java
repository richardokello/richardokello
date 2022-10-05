/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.config;

import ke.tracom.ufs.security.AuthenticationFilter;
import ke.tracom.ufs.utils.ResponseFilter;
import ke.tracom.ufs.utils.filters.OTPFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * @author eli.muraya
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final OTPFilter otpFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationFilter authFilter;
    private final ResponseFilter responseFilter;

    private final CustomLogoutHandler customLogoutHandler;


    public ResourceServerConfig(TokenStore tokenStore, AccessDeniedHandler accessDeniedHandler, CustomLogoutHandler customLogoutHandler) {
        this.otpFilter = new OTPFilter(tokenStore);
        this.accessDeniedHandler = accessDeniedHandler;
        this.customLogoutHandler = customLogoutHandler;
        authFilter = new AuthenticationFilter();
        responseFilter = new ResponseFilter();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()

                .antMatchers(HttpMethod.POST, "/change-password", "/reset-password/forgot-password", "/reset-password", "/gender", "/user-id", "/user/login-field-agent", "/change-password/first-time","/user/logout/login-time").permitAll()
                .antMatchers("/gender", "/user-loggedin").permitAll()

                .antMatchers("/swagger-ui.html", "/webjars/springfox-swagger-ui/**",
                        "/swagger-resources/**", "/v2/api-docs/**", "/images/**",
                        "/spring-security-rest/api/swagger-ui.html", "/encrypt", "/user/me").permitAll()
                .antMatchers(HttpMethod.POST, "/role").hasAuthority("CREATE_ROLES")
                .antMatchers(HttpMethod.GET, "/role/{roleId}/changes").hasAuthority("VIEW_ROLE")
                .antMatchers(HttpMethod.GET, "/role/{roleId}").hasAuthority("VIEW_ROLE")
                .antMatchers(HttpMethod.GET, "/role").hasAuthority("VIEW_ROLE")
                .antMatchers(HttpMethod.PUT, "/role").hasAuthority("UPDATE_ROLES")
                .antMatchers(HttpMethod.PUT, "/role/approve-actions", "/roles/decline-actions").hasAuthority("APPROVE_ROLES")
                .antMatchers(HttpMethod.POST, "/role/delete-action").hasAuthority("DELETE_ROLES")
                .antMatchers(HttpMethod.DELETE, "/role").hasAuthority("DELETE_ROLES")
                .antMatchers(HttpMethod.GET, "/role/deleted").hasAuthority("VIEW_ROLE")
                .antMatchers(HttpMethod.POST, "/workgroup").hasAuthority("CREATE_WORKGROUP")
                .antMatchers(HttpMethod.GET, "/workgroup/{workgroupId}/changes").hasAuthority("VIEW_WORKGROUP")
                .antMatchers(HttpMethod.GET, "/workgroup/{workgroupId}").hasAuthority("VIEW_WORKGROUP")
                .antMatchers(HttpMethod.GET, "/workgroup").hasAuthority("VIEW_WORKGROUP")
                .antMatchers(HttpMethod.PUT, "/workgroup").hasAuthority("UPDATE_WORKGROUP")
                .antMatchers(HttpMethod.PUT, "/workgroup/approve-actions", "/workgroup/decline-actions").hasAuthority("APPROVE_WORKGROUP")
                .antMatchers(HttpMethod.POST, "/workgroup/delete-action").hasAuthority("DELETE_WORKGROUP")
                .antMatchers(HttpMethod.DELETE, "/workgroup").hasAuthority("DELETE_WORKGROUP")
                .antMatchers(HttpMethod.GET, "/workgroup/deleted").hasAuthority("VIEW_WORKGROUP")

                .antMatchers(HttpMethod.POST, "/user").hasAuthority("CREATE_USERS")
                .antMatchers(HttpMethod.GET, "/user/{userId}/changes").hasAuthority("VIEW_USERS")
                .antMatchers(HttpMethod.GET, "/user/{userId}").hasAuthority("VIEW_USERS")
                .antMatchers(HttpMethod.GET, "/user").hasAuthority("VIEW_USERS")
                .antMatchers(HttpMethod.PUT, "/user").hasAuthority("UPDATE_USERS")
                .antMatchers(HttpMethod.PUT, "/user/approve-actions", "/user/decline-actions", "/user/approve-action-lock", "/user/decline-action-lock").hasAuthority("APPROVE_USERS")
                .antMatchers(HttpMethod.POST, "/user/delete-action").hasAuthority("DELETE_USERS")
                .antMatchers(HttpMethod.DELETE, "/user").hasAuthority("DELETE_USERS")
                .antMatchers(HttpMethod.GET, "/user/deleted").hasAuthority("VIEW_USERS")
                .antMatchers(HttpMethod.POST, "/user/activate-account").hasAuthority("USER_ACTIVATE_ACCOUNT")
                .antMatchers(HttpMethod.POST, "/user/deactivate-account").hasAuthority("USER_DEACTIVATE_ACCOUNT")
                .antMatchers(HttpMethod.POST, "/user/lock-account").hasAuthority("USER_LOCK_ACCOUNT")
                .antMatchers(HttpMethod.POST, "/user/login-field-agent").hasAuthority("LOGIN_FIELD_AGENT")
                .antMatchers(HttpMethod.POST, "/user/profile-upload").hasAuthority("UPLOAD_PROFILE")
                .antMatchers(HttpMethod.POST, "/user/unlock-account").hasAuthority("USER_UNLOCK_ACCOUNT")
                .antMatchers(HttpMethod.POST, "/currency").hasAuthority("CREATE_CURRENCY")
                .antMatchers(HttpMethod.GET, "/currency/{currencyId}/changes").hasAuthority("VIEW_CURRENCY")
                .antMatchers(HttpMethod.GET, "/currency/{currencyId}").hasAuthority("VIEW_CURRENCY")
                .antMatchers(HttpMethod.GET, "/currency").hasAuthority("VIEW_CURRENCY")
                .antMatchers(HttpMethod.PUT, "/currency").hasAuthority("UPDATE_CURRENCY")
                .antMatchers(HttpMethod.PUT, "/currency/approve-actions", "/currency/decline-actions").hasAuthority("APPROVE_CURRENCY")
                .antMatchers(HttpMethod.POST, "/currency/delete-action").hasAuthority("DELETE_CURRENCY")
                .antMatchers(HttpMethod.DELETE, "/currency").hasAuthority("DELETE_CURRENCY")
                .antMatchers(HttpMethod.GET, "/currency/deleted").hasAuthority("VIEW_CURRENCY")
                .antMatchers(HttpMethod.POST, "/countries").hasAuthority("CREATE_COUNTRY")
                .antMatchers(HttpMethod.GET, "/countries/{countryId}/changes").hasAuthority("VIEW_COUNTRY")
                .antMatchers(HttpMethod.GET, "/countries/{countryId}").hasAuthority("VIEW_COUNTRY")
                .antMatchers(HttpMethod.GET, "/countries").hasAuthority("VIEW_COUNTRY")
                .antMatchers(HttpMethod.PUT, "/countries").hasAuthority("UPDATE_COUNTRY")
                .antMatchers(HttpMethod.PUT, "/countries/approve-actions", "/countries/decline-actions").hasAuthority("APPROVE_COUNTRY")
                .antMatchers(HttpMethod.POST, "/countries/delete-action").hasAuthority("DELETE_COUNTRY")
                .antMatchers(HttpMethod.DELETE, "/countries").hasAuthority("DELETE_COUNTRY")
                .antMatchers(HttpMethod.GET, "/countries/deleted").hasAuthority("VIEW_COUNTRY")
                .antMatchers(HttpMethod.POST, "/mno").hasAuthority("CREATE_MNO")
                .antMatchers(HttpMethod.GET, "/mno/{mnoId}/changes").hasAuthority("VIEW_MNO")
                .antMatchers(HttpMethod.GET, "/mno/{mnoId}").hasAuthority("VIEW_MNO")
                .antMatchers(HttpMethod.GET, "/mno").hasAuthority("VIEW_MNO")
                .antMatchers(HttpMethod.PUT, "/mno").hasAuthority("UPDATE_MNO")
                .antMatchers(HttpMethod.PUT, "/mno/approve-actions", "/mno/decline-actions").hasAuthority("APPROVE_MNO")
                .antMatchers(HttpMethod.POST, "/mno/delete-action").hasAuthority("DELETE_MNO")
                .antMatchers(HttpMethod.DELETE, "/mno").hasAuthority("DELETE_MNO")
                .antMatchers(HttpMethod.GET, "/mno/deleted").hasAuthority("VIEW_MNO")
                .antMatchers(HttpMethod.POST, "/system-config").hasAuthority("CREATE_SYS-CONFIG")
                .antMatchers(HttpMethod.GET, "/system-config/{systemConfigId}/changes").hasAuthority("VIEW_SYS_CONFIG")
                .antMatchers(HttpMethod.GET, "/system-config/{systemConfigId}").hasAuthority("VIEW_SYS_CONFIG")
                .antMatchers(HttpMethod.GET, "/system-config").hasAuthority("VIEW_SYS_CONFIG")
                .antMatchers(HttpMethod.PUT, "/system-config").hasAuthority("UPDATE_SYS_CONFIG")
                .antMatchers(HttpMethod.PUT, "/system-config/approve-actions", "/system-config/decline-actions").hasAuthority("APPROVE_SYS_CONFIG")
                .antMatchers(HttpMethod.POST, "/system-config/delete-action").hasAuthority("DELETE_SYS_CONFIG")
                .antMatchers(HttpMethod.DELETE, "/system-config").hasAuthority("DELETE_SYS_CONFIG")
                .antMatchers(HttpMethod.GET, "/system-config/deleted").hasAuthority("VIEW_SYS_CONFIG")
                .antMatchers(HttpMethod.POST, "/bank-region").hasAuthority("CREATE_BANK_REGION")
                .antMatchers(HttpMethod.GET, "/bank-region/{bankRegionId}/changes").hasAuthority("VIEW_BANK_REGION")
                .antMatchers(HttpMethod.GET, "/bank-region/{bankRegionId}").hasAuthority("VIEW_BANK_REGION")
                .antMatchers(HttpMethod.GET, "/bank-region").hasAuthority("VIEW_BANK_REGION")
                .antMatchers(HttpMethod.PUT, "/bank-region").hasAuthority("UPDATE_BANK_REGIONS")
                .antMatchers(HttpMethod.PUT, "/bank-region/approve-actions", "/bank-region/decline-actions").hasAuthority("APPROVE_BANK_REGION")
                .antMatchers(HttpMethod.POST, "/bank-region/delete-action").hasAuthority("DELETE_BANK_REGION")
                .antMatchers(HttpMethod.DELETE, "/bank-region").hasAuthority("DELETE_BANK_REGION")
                .antMatchers(HttpMethod.GET, "/bank-region/deleted").hasAuthority("VIEW_BANK_REGION")
                .antMatchers(HttpMethod.GET, "/audit-log/{auditLogId}").hasAuthority("VIEW_AUDIT_LOGS")
                .antMatchers(HttpMethod.GET, "/audit-log").hasAuthority("VIEW_AUDIT_LOGS")
                .antMatchers(HttpMethod.GET, "/audit-log/unsuccessful-logins").hasAuthority("VIEW_AUDIT_LOGS")
                .antMatchers("/**").fullyAuthenticated()
                .and()
                .addFilterAfter(otpFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(responseFilter, OTPFilter.class)
                .addFilterBefore(authFilter, ExceptionTranslationFilter.class)
//                .cors()
//                .configurationSource(corsConfig())
//                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
//                .and()
//                .logout()
//                .logoutSuccessHandler(customLogoutHandler)
//                .permitAll();
        //                .and().addFilterBefore(new WebSecurityCorsFilter(), ChannelProcessingFilter.class);
        ;
    }


//    @Bean
//    CorsConfigurationSource corsConfig() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.applyPermitDefaultValues();
//        corsConfig.addAllowedHeader("Access-Control-Allow-Origin");
//        corsConfig.addExposedHeader("Access-Control-Allow-Origin");
//        corsConfig.addAllowedMethod(HttpMethod.GET);
//        corsConfig.addAllowedMethod(HttpMethod.POST);
//        corsConfig.addAllowedMethod(HttpMethod.PUT);
//        corsConfig.addAllowedMethod(HttpMethod.OPTIONS);
//        corsConfig.addAllowedMethod(HttpMethod.DELETE);
//        corsConfig.addAllowedMethod(HttpMethod.HEAD);
//        corsConfig.addAllowedOrigin("*");
//        source.registerCorsConfiguration("/**", corsConfig);
//        return source;
//    }
}

