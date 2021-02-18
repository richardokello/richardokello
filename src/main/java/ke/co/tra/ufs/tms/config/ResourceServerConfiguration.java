package ke.co.tra.ufs.tms.config;

import ke.co.tra.ufs.tms.security.AuthenticationFilter;
import ke.co.tra.ufs.tms.security.OTPFilter;
import ke.co.tra.ufs.tms.service.SysConfigService;
import ke.co.tra.ufs.tms.utils.ResponseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author Owori Juma
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    private final SysConfigService configService;

    private final OTPFilter otpFilter;
    private final AuthenticationFilter authFilter;
    private final ResponseFilter responseFilter;

    public ResourceServerConfiguration(SysConfigService configService, TokenStore tokenStore) {
        this.configService = configService;
        otpFilter = new OTPFilter(tokenStore, this.configService);
        authFilter = new AuthenticationFilter();
        responseFilter = new ResponseFilter();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/swagger-ui.html", "/webjars/springfox-swagger-ui/**",
                        "/swagger-resources/**", "/v2/api-docs/**", "/images/**", "/spring-security-rest/api/swagger-ui.html").permitAll()

                // manufacturers
                .antMatchers(HttpMethod.POST, "/device/make").hasAuthority("CREATE_MANUFACTURERS")
                .antMatchers(HttpMethod.GET, "/device/make", "/device/make/{\\makeId}").hasAnyAuthority("VIEW_MANUFACTURERS", "UPDATE_MANUFACTURERS", "CREATE_MANUFACTURERS", "APPROVE_MANUFACTURERS", "DELETE_MANUFACTURERS")
                .antMatchers(HttpMethod.PUT, "/device/make").hasAuthority("UPDATE_MANUFACTURERS")
                .antMatchers(HttpMethod.GET, "/device/make/{\\makeId}/changes").hasAnyAuthority("UPDATE_MANUFACTURERS", "APPROVE_MANUFACTURERS")
                .antMatchers(HttpMethod.DELETE, "/device/make").hasAuthority("DELETE_MANUFACTURERS")
                .antMatchers(HttpMethod.PUT, "/device/make/approve-actions", "/device/make/decline-actions").hasAuthority("APPROVE_MANUFACTURERS")

                // device model
                .antMatchers(HttpMethod.POST, "/device/model", "/device/file-extension", "/device/model/create").hasAuthority("CREATE_DEVICE_MODELS")
                .antMatchers(HttpMethod.GET, "/device/model", "/device/model/{\\id}").hasAnyAuthority("VIEW_DEVICE_MODELS", "UPDATE_DEVICE_MODELS", "CREATE_DEVICE_MODELS", "APPROVE_DEVICE_MODELS", "DELETE_DEVICE_MODELS")
                .antMatchers(HttpMethod.PUT, "/device/model").hasAuthority("UPDATE_DEVICE_MODELS")
                .antMatchers(HttpMethod.GET, "/device/model/{\\id}/changes").hasAnyAuthority("UPDATE_DEVICE_MODELS", "APPROVE_DEVICE_MODELS")
                .antMatchers(HttpMethod.DELETE, "/device/model").hasAuthority("DELETE_DEVICE_MODELS")
                .antMatchers(HttpMethod.PUT, "/device/model/approve-actions", "/device/model/decline-actions").hasAuthority("APPROVE_DEVICE_MODELS")

                // device type
                .antMatchers(HttpMethod.POST, "/device/type").hasAuthority("CREATE_DEVICE_TYPES")
                .antMatchers(HttpMethod.GET, "/device/type", "/device/type/{\\id}").hasAnyAuthority("VIEW_DEVICE_TYPES", "UPDATE_DEVICE_TYPES", "CREATE_DEVICE_TYPES", "APPROVE_DEVICE_TYPES", "DELETE_DEVICE_TYPES")
                .antMatchers(HttpMethod.PUT, "/device/type").hasAuthority("UPDATE_DEVICE_TYPES")
                .antMatchers(HttpMethod.GET, "/device/type/{\\id}/changes").hasAnyAuthority("UPDATE_DEVICE_TYPES", "APPROVE_DEVICE_TYPES")
                .antMatchers(HttpMethod.DELETE, "/device/type").hasAuthority("DELETE_DEVICE_TYPES")
                .antMatchers(HttpMethod.PUT, "/device/type/approve-actions", "/device/type/decline-actions").hasAuthority("APPROVE_DEVICE_TYPES")

                // products
                .antMatchers(HttpMethod.POST, "/products").hasAuthority("CREATE_BUSINESS_UNIT")
                .antMatchers(HttpMethod.GET, "/products", "/products/{\\id}").hasAnyAuthority("VIEW_BUSINESS_UNIT", "UPDATE_BUSINESS_UNIT", "UPDATE_ESTATE", "CREATE_ESTATE")
                .antMatchers(HttpMethod.PUT, "/products").hasAuthority("UPDATE_BUSINESS_UNIT")
                .antMatchers(HttpMethod.DELETE, "/products").hasAuthority("DELETE_BUSINESS_UNIT")
                .antMatchers(HttpMethod.PUT, "/products/approve-actions", "/products/decline-actions").hasAuthority("APPROVE_BUSINESS_UNIT")

                // estate
                .antMatchers(HttpMethod.POST, "/business-units/unititems").hasAuthority("CREATE_ESTATE_HIERARCHY")
                .antMatchers(HttpMethod.GET, "/business-units","/business-units/unititems/{\\id}", "/business-units/unititems/parents", "/business-units/unititems/parents/{\\id}").hasAnyAuthority("UPDATE_ESTATE_HIERARCHY", "VIEW_ESTATE_HIERARCHY", "CREATE_ESTATE_HIERARCHY")
                .antMatchers(HttpMethod.PUT, "/business-units/unititems").hasAuthority("UPDATE_ESTATE_HIERARCHY")


                // business unit
                .antMatchers(HttpMethod.GET, "/business-units", "/business-units/{\\id}", "/business-units/product/{id}").hasAuthority("VIEW_BUSINESS_UNIT")
                .antMatchers(HttpMethod.PUT, "/business-units/approve-actions", "/business-units/decline-actions").hasAuthority("APPROVE_BUSINESS_UNIT")
                .antMatchers(HttpMethod.GET, "/business-units/unititems/{\\id}", "/business-units/unititems/device/{\\id}").hasAuthority("VIEW_BUSINESS_UNIT_ITEM")
                .antMatchers(HttpMethod.PUT, "/business-units/unititems").hasAuthority("UPDATE_BUSINESS_UNIT_ITEM")
                .antMatchers(HttpMethod.POST, "/business-units/unititems").hasAuthority("CREATE_BUSINESS_UNIT_ITEM")
                .antMatchers(HttpMethod.GET, "/business-units/unititems/parents", "/business-units/unititems/parents/{\\id}", "/business-units/unititems/product/{\\id}").hasAuthority("VIEW_BUSINESS_UNIT_ITEM")
                .antMatchers(HttpMethod.PUT, "/business-units/unititems/parents", "/business-units/unititems/parents/{\\id}", "/business-units/unititems/product/{\\id}").hasAuthority("UPDATE_BUSINESS_UNIT_ITEM")
                .antMatchers(HttpMethod.POST, "/business-units/unititems/parents", "/business-units/unititems/parents/{\\id}", "/business-units/unititems/product/{\\id}").hasAuthority("CREATE_BUSINESS_UNIT_ITEM")
                .antMatchers(HttpMethod.PUT, "/business-units/unititems/approve").hasAuthority("APPROVE_BUSINESS_UNIT_ITEM")
                .antMatchers(HttpMethod.PUT, "/business-units/unititems/decline").hasAuthority("DECLINE_BUSINESS_UNIT_ITEM")
                .antMatchers(HttpMethod.PUT, "/business-units/unititems/delete").hasAuthority("DELETE_BUSINESS_UNIT_ITEM")

                // departments
                .antMatchers(HttpMethod.POST, "/departments").hasAuthority("CREATE_DEPARTMENTS")
                .antMatchers(HttpMethod.GET, "/departments", "/departments/{\\id}").hasAnyAuthority("VIEW_DEPARTMENTS", "UPDATE_DEPARTMENTS", "CREATE_DEPARTMENTS", "DELETE_DEPARTMENTS", "APPROVE_DEPARTMENTS")
                .antMatchers(HttpMethod.PUT, "/departments").hasAuthority("UPDATE_DEPARTMENTS")
                .antMatchers(HttpMethod.DELETE, "/departments").hasAuthority("DELETE_DEPARTMENTS")
                .antMatchers(HttpMethod.PUT, "/departments/approve-actions", "/departments/decline-actions").hasAuthority("APPROVE_DEPARTMENTS")

                // whitelist device
                .antMatchers(HttpMethod.POST, "/device/whitelist").hasAuthority("CREATE_WHITELIST")
                .antMatchers(HttpMethod.GET, "/whitelist-template.csv").hasAuthority("CREATE_WHITELIST")
                .antMatchers(HttpMethod.GET, "/device/whitelist", "/device/whitelist/{\\id}").hasAnyAuthority("VIEW_WHITELIST", "APPROVE_WHITELIST", "CREATE_WHITELIST", "DELETE_WHITELIST", "UPDATE_WHITELIST")
                .antMatchers(HttpMethod.PUT, "/device/whitelist").hasAuthority("UPDATE_WHITELIST")
                .antMatchers(HttpMethod.DELETE, "/device/whitelist").hasAuthority("DELETE_WHITELIST")
                .antMatchers(HttpMethod.PUT, "/device/whitelist/approve-actions", "/device/whitelist/decline-actions").hasAuthority("APPROVE_WHITELIST")


                // device
                .antMatchers(HttpMethod.POST, "/onboarding","/onbording/assign-merchant-device").hasAuthority("CREATE_ASSIGNED_DEVICE")
                .antMatchers(HttpMethod.GET, "/devices", "/devices/{\\id}", "/device/whitelist/serial/{\\serial}").hasAnyAuthority( "VIEW_ASSIGNED_DEVICE", "UPDATE_ASSIGNED_DEVICE", "VIEW_RELEASE", "VIEW_DECOMMISSION", "VIEW_ACTIVATED_DEVICE", "CREATE_DEVICE_TASK", "DELETE_DEVICE_TASK", "APPROVE_ASSIGNED_DEVICE", "CREATE_ASSIGNED_DEVICE", "VIEW_DEVICE_HISTORY")
                .antMatchers(HttpMethod.POST, "/onboarding/update-device").hasAuthority(  "UPDATE_ASSIGNED_DEVICE")
                .antMatchers(HttpMethod.PUT, "/devices/approve-actions", "/devices/decline-actions").hasAuthority("APPROVE_ASSIGNED_DEVICE")
                .antMatchers(HttpMethod.PUT,  "/devices/cancel", "/devices/params/{\\id}", "devices/merchant/{\\id}").hasAnyAuthority("APPROVE_ASSIGNED_DEVICE", "VIEW_ASSIGNED_DEVICE")
                .antMatchers(HttpMethod.PUT, "/devices/release").hasAuthority("RELEASE_DEVICE")
                .antMatchers(HttpMethod.DELETE, "/devices/decommission").hasAuthority("DECOMMISSION_DEVICE")
                .antMatchers(HttpMethod.DELETE, "/devices").hasAuthority("DECOMMISSION_DEVICE")
                .antMatchers(HttpMethod.POST, "/onboarding/add-task").hasAuthority("CREATE_DEVICE_TASK")
                .antMatchers(HttpMethod.DELETE, "/devices/cancel").hasAuthority("DELETE_DEVICE_TASK")
                .antMatchers(HttpMethod.GET, "/terminal-history").hasAuthority("VIEW_DEVICE_HISTORY")

                // device heartbeats
                .antMatchers(HttpMethod.GET, "/heart-beat").hasAuthority("VIEW_DEVICE_HEARTBEAT")

                .antMatchers(HttpMethod.GET, "/posparam", "/business-units/unititems/parents", "/business-units/unititems/parents/{\\id}", "/business-units/unititems/product/{\\id}", "/devices/merchant", "/devices/merchant/{\\agentMerchantId}").hasAuthority("CREATE_ASSIGNED_DEVICE")

                // schedule
                .antMatchers(HttpMethod.POST, "/schedule").hasAuthority("CREATE_SCHEDULE")
                .antMatchers(HttpMethod.GET, "/schedule", "/schedule/{\\id}", "/schedule/manual").hasAnyAuthority("VIEW_SCHEDULES", "CREATE_SCHEDULE", "APPROVE_SCHEDULE", "DELETE_SCHEDULE", "UPDATE_SCHEDULE")
                .antMatchers(HttpMethod.POST, "/schedule/update-schedule").hasAnyAuthority("UPDATE_SCHEDULE")
                .antMatchers(HttpMethod.PUT, "/schedule/cancel").hasAuthority("DELETE_SCHEDULE")
                .antMatchers(HttpMethod.PUT, "/schedule/approve-actions", "/schedule/decline-actions").hasAuthority("APPROVE_SCHEDULE")
                .antMatchers(HttpMethod.GET, "/download-reports/schedule").hasAuthority("VIEW_DOWNLOADED_SCHEDULE")
                .antMatchers(HttpMethod.GET, "/download-reports/device-task" ).hasAnyAuthority("VIEW_PENDING_DOWNLOAD", "VIEW_ACTIVE_DOWNLOAD")
                .antMatchers(HttpMethod.GET, "/download-reports/device-task/{\\deviceId}","/download-reports/update-logs", "download-reports/ftp-logs/device/{\\id}", "/download-reports/ftp-logs/{\\id}").hasAnyAuthority("VIEW_ASSIGNED_DEVICE", "CREATE_DEVICE_TASK", "DELETE_DEVICE_TASK")

                // application
                .antMatchers(HttpMethod.POST, "/app-management").hasAuthority("CREATE_DEVICE_APPLICATION")
                .antMatchers(HttpMethod.GET, "/app-management", "/app-management/{\\id}").hasAnyAuthority("VIEW_APPLICATIONS", "UPDATE_DEVICE_APPLICATION", "CREATE_DEVICE_APPLICATION", "DELETE_DEVICE_APPLICATION", "APPROVE_DEVICE_APPLICATION")
                .antMatchers(HttpMethod.PUT, "/app-management").hasAuthority("UPDATE_DEVICE_APPLICATION")
                .antMatchers(HttpMethod.DELETE, "/app-management").hasAuthority("DELETE_DEVICE_APPLICATION")
                .antMatchers(HttpMethod.PUT, "/app-management/approve-actions", "/app-management/decline-actions").hasAuthority("APPROVE_DEVICE_APPLICATION")

                // pos users
                .antMatchers(HttpMethod.POST, "/pos_user/reset-pin").hasAuthority("RESET_PIN")
                .antMatchers(HttpMethod.PUT, "/pos_user/approve-actions").hasAuthority("APPROVE_POS_USER")
                .antMatchers(HttpMethod.GET, "/pos_user", "/pos-user/{\\id}").hasAnyAuthority("VIEW_POS_USERS", "RESET_PIN", "UPDATE_ASSIGNED_DEVICE", "CREATE_ASSIGNED_DEVICE")


                // device options
                .antMatchers(HttpMethod.POST, "/par-device-options").hasAuthority("CREATE_DEVICE_OPTION")
                .antMatchers(HttpMethod.GET, "/par-device-options", "/par-device-options/{\\id}").hasAnyAuthority("VIEW_DEVICE_OPTION", "UPDATE_DEVICE_OPTION", "CREATE_DEVICE_OPTION", "DELETE_DEVICE_OPTION", "APPROVE_DEVICE_OPTION", "UPDATE_ASSIGNED_DEVICE", "CREATE_ASSIGNED_DEVICE")
                .antMatchers(HttpMethod.PUT, "/par-device-options").hasAuthority("UPDATE_DEVICE_OPTION")
                .antMatchers(HttpMethod.DELETE, "/par-device-options").hasAuthority("DELETE_DEVICE_OPTION")
                .antMatchers(HttpMethod.PUT, "/par-device-options/approve-actions", "/par-device-options/decline-actions").hasAuthority("APPROVE_DEVICE_OPTION")


                // menu items
                .antMatchers(HttpMethod.POST, "/menu-items").hasAuthority("CREATE_MENU_ITEMS")
                .antMatchers(HttpMethod.GET, "/menu-items", "menu-items/{\\id}").hasAnyAuthority("VIEW_MENU_ITEMS","UPDATE_MENU_ITEMS", "CREATE_MENU_ITEMS", "DELETE_MENU_ITEMS", "APPROVE_MENU_ITEMS")
                .antMatchers(HttpMethod.PUT, "/menu-items").hasAuthority("UPDATE_MENU_ITEMS")
                .antMatchers(HttpMethod.DELETE, "/menu-items").hasAuthority("DELETE_MENU_ITEMS")
                .antMatchers(HttpMethod.PUT, "/menu-items/approve-actions", "/menu-items/decline-actions").hasAuthority("APPROVE_MENU_ITEMS")

                // menu profiles
                .antMatchers(HttpMethod.POST, "/menu-profiles").hasAuthority("CREATE_MENU_PROFILES")
                .antMatchers(HttpMethod.GET, "/menu-profiles", "/menu-profiles/{\\id}").hasAnyAuthority("VIEW_MENU_PROFILES", "UPDATE_MENU_PROFILES", "CREATE_MENU_PROFILES", "DELETE_MENU_PROFILES", "APPROVE_MENU_PROFILES")
                .antMatchers(HttpMethod.PUT, "/menu-profiles").hasAuthority("UPDATE_MENU_PROFILES")
                .antMatchers(HttpMethod.DELETE, "/menu-profiles").hasAuthority("DELETE_MENU_PROFILES")
                .antMatchers(HttpMethod.PUT, "/menu-profiles/approve-actions", "/menu-profiles/decline-actions").hasAuthority("APPROVE_MENU_PROFILES")

                // parameter indexing
                .antMatchers(HttpMethod.POST, "/par-device-option-indices/create", "/customer-config-indices/create", "/global-config-indices/configs", "/menu-indices/menus").hasAuthority("CREATE_PARAMETER_INDEXING")

                //parameter file types
                .antMatchers(HttpMethod.POST, "/global-config-files").hasAuthority("CREATE_PARAMETER_FILE_TYPES")
                .antMatchers(HttpMethod.GET, "/global-config-files", "/global-config-files/{\\id}").hasAnyAuthority("VIEW_PARAMETER_FILE_TYPES", "UPDATE_PARAMETER_FILE_TYPES", "CREATE_PARAMETER_FILE_TYPES", "DELETE_PARAMETER_FILE_TYPES", "APPROVE_PARAMETER_FILE_TYPES")
                .antMatchers(HttpMethod.PUT, "/global-config-files").hasAuthority("UPDATE_PARAMETER_FILE_TYPES")
                .antMatchers(HttpMethod.DELETE, "/global-config-files").hasAuthority("DELETE_PARAMETER_FILE_TYPES")
                .antMatchers(HttpMethod.PUT, "/global-config-files/approve-actions", "/global-config-files/decline-actions").hasAuthority("APPROVE_PARAMETER_FILE_TYPES")

                // parameter form values
                .antMatchers(HttpMethod.POST, "/global-config-forms").hasAuthority("CREATE_PARAMETER_FORM_VALUES")
                .antMatchers(HttpMethod.GET, "/global-config-forms", "/global-config-forms/{\\id}").hasAnyAuthority("VIEW_PARAMETER_FORM_VALUES", "UPDATE_PARAMETER_FORM_VALUES")
                .antMatchers(HttpMethod.PUT, "/global-config-forms").hasAuthority("UPDATE_PARAMETER_FORM_VALUES")
                .antMatchers(HttpMethod.DELETE, "/global-config-forms").hasAuthority("DELETE_PARAMETER_FORM_VALUES")
                .antMatchers(HttpMethod.PUT, "/global-config-forms/approve-actions", "/global-config-forms/decline-actions").hasAuthority("APPROVE_PARAMETER_FORM_VALUES")

                // parameter profiles
                .antMatchers(HttpMethod.POST, "/global-config-profiles/config").hasAuthority("CREATE_PARAMETER_PROFILES")
                .antMatchers(HttpMethod.GET, "/global-config-profiles", "global-config-profiles/{\\id}").hasAnyAuthority("VIEW_PARAMETER_PROFILES", "UPDATE_PARAMETER_PROFILES", "CREATE_PARAMETER_PROFILES", "DELETE_PARAMETER_PROFILES", "APPROVE_PARAMETER_PROFILES")
                .antMatchers(HttpMethod.PUT, "/global-config-profiles/config").hasAuthority("UPDATE_PARAMETER_PROFILES")
                .antMatchers(HttpMethod.DELETE, "/global-config-profiles").hasAuthority("DELETE_PARAMETER_PROFILES")
                .antMatchers(HttpMethod.PUT, "/global-config-profiles/approve-actions", "/global-config-profiles/decline-actions").hasAuthority("APPROVE_PARAMETER_PROFILES")

                // master profiles
                .antMatchers(HttpMethod.POST, "/par-master-profiles").hasAuthority("CREATE_MASTER_PROFILES")
                .antMatchers(HttpMethod.GET, "/par-master-profiles", "/par-master-profiles/{\\id}").hasAnyAuthority("UPDATE_MASTER_PROFILES", "VIEW_MASTER_PROFILES", "CREATE_MASTER_PROFILES", "DELETE_MASTER_PROFILES", "APPROVE_MASTER_PROFILES")
                .antMatchers(HttpMethod.PUT, "/par-master-profiles").hasAuthority("UPDATE_MASTER_PROFILES")
                .antMatchers(HttpMethod.DELETE, "/par-master-profiles").hasAuthority("DELETE_MASTER_PROFILES")
                .antMatchers(HttpMethod.PUT, "/par-master-profiles/approve-actions", "/par-master-profiles/decline-actions").hasAuthority("APPROVE_MASTER_PROFILES")


//                .antMatchers(HttpMethod.PUT, "/bin-profile").hasAuthority("UPDATE_BIN_PROFILE")
//                .antMatchers(HttpMethod.PUT, "/bin-profile/approve-actions").hasAuthority("CREATE_BIN_PROFILE")
//                .antMatchers(HttpMethod.GET, "/bin-profile").hasAuthority("VIEW_BIN_PROFILE")
//                .antMatchers(HttpMethod.POST, "/menu-groups").hasAuthority("CREATE_MENU_GROUP")
//                .antMatchers(HttpMethod.PUT, "/menu-groups/approve-actions").hasAuthority("CREATE_MENU_GROUP")
//                .antMatchers(HttpMethod.PUT, "/menu-groups").hasAuthority("UPDATE_MENU_GROUP")
//                .antMatchers(HttpMethod.GET, "/menu-groups").hasAuthority("VIEW_MENU_GROUP")
//                .antMatchers(HttpMethod.DELETE, "/menu-groups").hasAuthority("DELETE_MENU_GROUP")
//                .antMatchers(HttpMethod.POST, "/bin-profile").hasAuthority("CREATE_BIN_PROFILE")
//                .antMatchers(HttpMethod.DELETE, "/bin-config").hasAuthority("DELETE_BIN_CONFIG")
//
//                .antMatchers(HttpMethod.GET, "/billers", "/billers/{\\id}").hasAuthority("VIEW_BILLERS")
//                .antMatchers(HttpMethod.PUT, "/billers/approve-actions", "/billers/decline-actions", "/billers/update").hasAuthority("APPROVE_BILLERS")
//                .antMatchers(HttpMethod.POST, "/billers").hasAuthority("CREATE_BILLER")
//
//                .antMatchers(HttpMethod.GET, "/bin-config").hasAuthority("VIEW_BIN_CONFIG")
//                .antMatchers(HttpMethod.PUT, "/bin-config").hasAuthority("UPDATE_BIN_CONFIG")
//                .antMatchers(HttpMethod.POST, "/bin-config").hasAuthority("CREATE_BIN_CONFIG")
//                .antMatchers(HttpMethod.PUT, "/bin-config/approve-actions").hasAuthority("CREATE_BIN_CONFIG")
//                .antMatchers(HttpMethod.DELETE, "/bin-profile").hasAuthority("DELETE_BIN_PROFILE")

//                .antMatchers(HttpMethod.GET, "/audit-trail", "/audit-trail/export.csv", "/audit-trail/export.pdf", "/audit-trail/export.xls", "/audit-trail/{\\id}", "/audit-trail/trail/{\\id}").hasAuthority("VIEW_AUDIT_TRAILS")
//                .antMatchers(HttpMethod.GET, "/heart-beat", "/heart-beat/export.csv", "/heart-beat/export.pdf", "/heart-beat/export.xls", "/heart-beat/{\\serialNo}", "/heart-beat/id/{\\id}", "/heart-beat/device/{\\id}").hasAuthority("VIEW_HEART_BEATS")
                .antMatchers("/**").fullyAuthenticated()
                .and()
                .addFilterAfter(otpFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(authFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(responseFilter, OTPFilter.class)
                .cors().configurationSource(corsConfig())
                .and()
                .logout()
                .logoutUrl("/oauth/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
        ;
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
