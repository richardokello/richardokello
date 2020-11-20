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
 * @author Kenneth Mwangi
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
                .antMatchers("/swagger-ui.html", "/webjars/springfox-swagger-ui/**",
                        "/swagger-resources/**", "/v2/api-docs/**", "/images/**",
                        "/spring-security-rest/api/swagger-ui.html", "/encrypt").permitAll()
                .antMatchers(HttpMethod.POST, "/bank-branches").hasAuthority("CREATE_BANK_BRANCH")
                .antMatchers(HttpMethod.GET, "/bank-branches/{bankBranchId}/changes").hasAuthority("VIEW_BANK_BRANCHES")
                .antMatchers(HttpMethod.GET, "/bank-branches/{bankBranchId}").hasAuthority("VIEW_BANK_BRANCHES")
                .antMatchers(HttpMethod.GET, "/bank-branches").hasAuthority("VIEW_BANK_BRANCHES")
                .antMatchers(HttpMethod.PUT, "/bank-branches").hasAuthority("UPDATE_BANK_BRANCH")
                .antMatchers(HttpMethod.PUT, "/bank-branches/approve-actions", "/bank-branches/decline-actions").hasAuthority("APPROVE_BANK_BRANCH")
                .antMatchers(HttpMethod.DELETE, "/bank-branches").hasAuthority("DELETE_BANK_REGION")
                .antMatchers(HttpMethod.GET, "/bank-branches/deleted").hasAuthority("VIEW_BANK_BRANCHES")
                .antMatchers(HttpMethod.PUT, "/bank-branches/suspend").hasAuthority("SUSPEND_BANK_BRANCH")
                .antMatchers(HttpMethod.PUT, "/bank-branches/reactivate").hasAuthority("REACTIVATE_BANK_BRANCH")
                .antMatchers(HttpMethod.POST, "/commercial-activities").hasAuthority("CREATE_COMMERCIAL_ACTIVITY")
                .antMatchers(HttpMethod.GET, "/commercial-activities/{commercialActivityId}/changes").hasAuthority("VIEW_COMMERCIAL_ACTIVITY")
                .antMatchers(HttpMethod.GET, "/commercial-activities/{commercialActivityId}").hasAuthority("VIEW_COMMERCIAL_ACTIVITY")
                .antMatchers(HttpMethod.GET, "/commercial-activities").hasAuthority("VIEW_COMMERCIAL_ACTIVITY")
                .antMatchers(HttpMethod.PUT, "/commercial-activities").hasAuthority("UPDATE_COMMERCIAL_ACTIVITY")
                .antMatchers(HttpMethod.PUT, "/commercial-activities/approve-actions", "/commercial-activities/decline-actions").hasAuthority("APPROVE_COMMERCIAL_ACTIVITY")
                .antMatchers(HttpMethod.DELETE, "/commercial-activities").hasAuthority("DELETE_COMMERCIAL_ACTIVITY")
                .antMatchers(HttpMethod.GET, "/commercial-activities/deleted").hasAuthority("VIEW_COMMERCIAL_ACTIVITY")
                .antMatchers(HttpMethod.POST, "/customer-owners").hasAuthority("CREATE_CUSTOMER_OWNER")
                .antMatchers(HttpMethod.GET, "/customer-owners/{customerOwnerId}/changes").hasAuthority("VIEW_CUSTOMER_OWNER")
                .antMatchers(HttpMethod.GET, "/customer-owners/{customerOwnerId}").hasAuthority("VIEW_CUSTOMER_OWNER")
                .antMatchers(HttpMethod.GET, "/customer-owners").hasAuthority("VIEW_CUSTOMER_OWNER")
                .antMatchers(HttpMethod.PUT, "/customer-owners").hasAuthority("UPDATE_CUSTOMER_OWNER")
                .antMatchers(HttpMethod.PUT, "/customer-owners/approve-actions", "/customer-owners/decline-actions").hasAuthority("APPROVE_CUSTOMER_OWNER")
                .antMatchers(HttpMethod.DELETE, "/customer-owners").hasAuthority("DELETE_CUSTOMER_OWNER")
                .antMatchers(HttpMethod.GET, "/customer-owners/deleted").hasAuthority("VIEW_CUSTOMER_OWNER")
                .antMatchers(HttpMethod.GET, "/revenue-collected").hasAuthority("VIEW_REVENUE_COLLECTED")
                .antMatchers(HttpMethod.POST, "/business-designations").hasAuthority("CREATE_BUSINESS_DESIGNATION")
                .antMatchers(HttpMethod.GET, "/business-designations/{businessDesignationId}/changes").hasAuthority("VIEW_BUSINESS_DESIGNATION")
                .antMatchers(HttpMethod.GET, "/business-designations/{businessDesignationId}").hasAuthority("VIEW_BUSINESS_DESIGNATION")
                .antMatchers(HttpMethod.GET, "/business-designations").hasAuthority("VIEW_BUSINESS_DESIGNATION")
                .antMatchers(HttpMethod.PUT, "/business-designations").hasAuthority("UPDATE_BUSINESS_DESIGNATION")
                .antMatchers(HttpMethod.PUT, "/business-designations/approve-actions", "/business-designations/decline-actions").hasAuthority("APPROVE_BUSINESS_DESIGNATION")
                .antMatchers(HttpMethod.DELETE, "/business-designations").hasAuthority("DELETE_BUSINESS_DESIGNATION")
                .antMatchers(HttpMethod.GET, "/business-designations/deleted").hasAuthority("VIEW_BUSINESS_DESIGNATION")
                .antMatchers(HttpMethod.POST, "/business-types").hasAuthority("CREATE_BUSINESS_TYPE")
                .antMatchers(HttpMethod.GET, "/business-types/{businessTypeId}/changes").hasAuthority("VIEW_BUSINESS_TYPE")
                .antMatchers(HttpMethod.GET, "/business-types/{businessTypeId}").hasAuthority("VIEW_BUSINESS_TYPE")
                .antMatchers(HttpMethod.GET, "/business-types").hasAuthority("VIEW_BUSINESS_TYPE")
                .antMatchers(HttpMethod.PUT, "/business-types").hasAuthority("UPDATE_BUSINESS_TYPE")
                .antMatchers(HttpMethod.PUT, "/business-types/approve-actions", "/business-types/decline-actions").hasAuthority("APPROVE_BUSINESS_TYPE")
                .antMatchers(HttpMethod.DELETE, "/business-types").hasAuthority("DELETE_BUSINESS_TYPE")
                .antMatchers(HttpMethod.GET, "/business-types/deleted").hasAuthority("VIEW_BUSINESS_TYPE")
                .antMatchers(HttpMethod.POST, "/contact-person").hasAuthority("CREATE_CONTACT_PERSON")
                .antMatchers(HttpMethod.GET, "/contact-person/{contactPersonId}/changes").hasAuthority("VIEW_CONTACT_PERSON")
                .antMatchers(HttpMethod.GET, "/contact-person/{contactPersonId}").hasAuthority("VIEW_CONTACT_PERSON")
                .antMatchers(HttpMethod.GET, "/contact-person").hasAuthority("VIEW_CONTACT_PERSON")
                .antMatchers(HttpMethod.PUT, "/contact-person").hasAuthority("UPDATE_CONTACT_PERSON")
                .antMatchers(HttpMethod.PUT, "/contact-person/approve-actions", "/contact-person/decline-actions").hasAuthority("APPROVE_CONTACT_PERSON")
                .antMatchers(HttpMethod.DELETE, "/contact-person").hasAuthority("DELETE_CONTACT_PERSON")
                .antMatchers(HttpMethod.GET, "/contact-person/deleted").hasAuthority("VIEW_CONTACT_PERSON")
                .antMatchers(HttpMethod.GET, "/contact-person/{customerId}/all").hasAuthority("VIEW_CONTACT_PERSON")
                .antMatchers(HttpMethod.POST, "/contact-person/assign-device").hasAuthority("CREATE_CONTACT_PERSON")
                .antMatchers(HttpMethod.POST, "/customer-class").hasAuthority("CREATE_CUSTOMER_CLASS")
                .antMatchers(HttpMethod.GET, "/customer-class/{customerClassId}/changes").hasAuthority("VIEW_CUSTOMER_CLASS")
                .antMatchers(HttpMethod.GET, "/customer-class/{customerClassId}").hasAuthority("VIEW_CUSTOMER_CLASS")
                .antMatchers(HttpMethod.GET, "/customer-class").hasAuthority("VIEW_CUSTOMER_CLASS")
                .antMatchers(HttpMethod.PUT, "/customer-class").hasAuthority("UPDATE_CUSTOMER_CLASS")
                .antMatchers(HttpMethod.PUT, "/customer-class/approve-actions", "/customer-class/decline-actions").hasAuthority("APPROVE_CUSTOMER_CLASS")
                .antMatchers(HttpMethod.DELETE, "/customer-class").hasAuthority("DELETE_CUSTOMER_CLASS")
                .antMatchers(HttpMethod.GET, "/customer-class/deleted").hasAuthority("VIEW_CUSTOMER_CLASS")
                .antMatchers(HttpMethod.POST, "/customer-complaints").hasAuthority("CREATE_CUSTOMER_COMPLAINT")
                .antMatchers(HttpMethod.POST, "/customer-complaints/add").hasAuthority("CREATE_CUSTOMER_COMPLAINT")
                .antMatchers(HttpMethod.POST, "/customer-complaints/upload").hasAuthority("CREATE_CUSTOMER_COMPLAINT")
                .antMatchers(HttpMethod.GET, "/customer-complaints").hasAuthority("VIEW_CUSTOMER_COMPLAINT")
                .antMatchers(HttpMethod.GET, "/customer-complaints/{id}").hasAuthority("VIEW_CUSTOMER_COMPLAINT")
                .antMatchers(HttpMethod.GET, "/customer-complaints/customer-complaints.template.csv").hasAuthority("VIEW_CUSTOMER_COMPLAINT")
                .antMatchers(HttpMethod.POST, "/customer-outlet").hasAuthority("CREATE_CUSTOMER_OUTLET")
                .antMatchers(HttpMethod.GET, "/customer-outlet/{id}/changes").hasAuthority("VIEW_CUSTOMER_OUTLET")
                .antMatchers(HttpMethod.GET, "/customer-outlet/{id}").hasAuthority("VIEW_CUSTOMER_OUTLET")
                .antMatchers(HttpMethod.GET, "/customer-outlet").hasAuthority("VIEW_CUSTOMER_OUTLET")
                .antMatchers(HttpMethod.PUT, "/customer-outlet").hasAuthority("UPDATE_CUSTOMER_OUTLET")
                .antMatchers(HttpMethod.PUT, "/customer-outlet/approve-actions", "/customer-outlet/decline-actions").hasAuthority("APPROVE_CUSTOMER_OUTLET")
                .antMatchers(HttpMethod.DELETE, "/customer-outlet").hasAuthority("DELETE_CUSTOMER_OUTLET")
                .antMatchers(HttpMethod.GET, "/customer-outlet/deleted").hasAuthority("VIEW_CUSTOMER_OUTLET")
                .antMatchers(HttpMethod.POST, "/customer-transfer").hasAuthority("CREATE_TRANSFER_CUSTOMER")
                .antMatchers(HttpMethod.GET, "/customer-transfer/{id}/changes").hasAuthority("VIEW_TRANSFER_CUSTOMER")
                .antMatchers(HttpMethod.GET, "/customer-transfer/{id}").hasAuthority("VIEW_TRANSFER_CUSTOMER")
                .antMatchers(HttpMethod.GET, "/customer-transfer").hasAuthority("VIEW_TRANSFER_CUSTOMER")
                .antMatchers(HttpMethod.PUT, "/customer-transfer").hasAuthority("UPDATE_TRANSFER_CUSTOMER")
                .antMatchers(HttpMethod.PUT, "/customer-transfer/approve-actions", "/customer-transfer/decline-actions").hasAuthority("APPROVE_TRANSFER_CUSTOMER")
                .antMatchers(HttpMethod.DELETE, "/customer-transfer").hasAuthority("DELETE_TRANSFER_CUSTOMER")
                .antMatchers(HttpMethod.GET, "/customer-transfer/deleted").hasAuthority("VIEW_TRANSFER_CUSTOMER")
                .antMatchers(HttpMethod.POST, "/customer-type").hasAuthority("CREATE_CUSTOMER_TYPE")
                .antMatchers(HttpMethod.GET, "/customer-type/{id}/changes").hasAuthority("VIEW_CUSTOMER_TYPE")
                .antMatchers(HttpMethod.GET, "/customer-type/{id}").hasAuthority("VIEW_CUSTOMER_TYPE")
                .antMatchers(HttpMethod.GET, "/customer-type").hasAuthority("VIEW_CUSTOMER_TYPE")
                .antMatchers(HttpMethod.PUT, "/customer-type").hasAuthority("UPDATE_CUSTOMER_TYPE")
                .antMatchers(HttpMethod.PUT, "/customer-type/approve-actions", "/customer-type/decline-actions").hasAuthority("APPROVE_CUSTOMER_TYPE")
                .antMatchers(HttpMethod.DELETE, "/customer-type").hasAuthority("DELETE_CUSTOMER_TYPE")
                .antMatchers(HttpMethod.GET, "/customer-type/deleted").hasAuthority("VIEW_CUSTOMER_TYPE")
                .antMatchers(HttpMethod.POST, "/gls").hasAuthority("CREATE_GLS")
                .antMatchers(HttpMethod.POST, "/gls/upload").hasAuthority("CREATE_GLS")
                .antMatchers(HttpMethod.GET, "/gls/{id}/changes").hasAuthority("VIEW_GLS")
                .antMatchers(HttpMethod.GET, "/gls/{id}").hasAuthority("VIEW_GLS")
                .antMatchers(HttpMethod.GET, "/gls").hasAuthority("VIEW_GLS")
                .antMatchers(HttpMethod.GET, "/gls/gls-template.csv").hasAuthority("VIEW_CUSTOMER_TYPE")
                .antMatchers(HttpMethod.PUT, "/gls").hasAuthority("UPDATE_GLS")
                .antMatchers(HttpMethod.PUT, "/gls/approve-actions", "/gls/decline-actions").hasAuthority("APPROVE_GLS")
                .antMatchers(HttpMethod.DELETE, "/gls").hasAuthority("DELETE_GLS")
                .antMatchers(HttpMethod.GET, "/gls/deleted").hasAuthority("VIEW_GLS")
                .antMatchers(HttpMethod.POST, "/mcc").hasAuthority("CREATE_MCC")
                .antMatchers(HttpMethod.GET, "/mcc/{id}/changes").hasAuthority("VIEW_MCC")
                .antMatchers(HttpMethod.GET, "/mcc/{id}").hasAuthority("VIEW_MCC")
                .antMatchers(HttpMethod.GET, "/mcc").hasAuthority("VIEW_MCC")
                .antMatchers(HttpMethod.PUT, "/mcc").hasAuthority("UPDATE_MCC")
                .antMatchers(HttpMethod.PUT, "/mcc/approve-actions", "/mcc/decline-actions").hasAuthority("APPROVE_MCC")
                .antMatchers(HttpMethod.DELETE, "/mcc").hasAuthority("DELETE_MCC")
                .antMatchers(HttpMethod.GET, "/mcc/deleted").hasAuthority("VIEW_MCC")
                .antMatchers(HttpMethod.POST, "/pos-role").hasAuthority("VIEW_POS_ROLES")
                .antMatchers(HttpMethod.GET, "/pos-role/{id}/changes").hasAuthority("VIEW_POS_ROLES")
                .antMatchers(HttpMethod.GET, "/pos-role/{id}").hasAuthority("VIEW_POS_ROLES")
                .antMatchers(HttpMethod.GET, "/pos-role").hasAuthority("VIEW_POS_ROLES")
                .antMatchers(HttpMethod.PUT, "/pos-role").hasAuthority("VIEW_POS_ROLES")
                .antMatchers(HttpMethod.PUT, "/pos-role/approve-actions", "/pos-role/decline-actions").hasAuthority("VIEW_POS_ROLES")
                .antMatchers(HttpMethod.DELETE, "/pos-role").hasAuthority("VIEW_POS_ROLES")
                .antMatchers(HttpMethod.GET, "/pos-role/deleted").hasAuthority("VIEW_POS_ROLES")
                .antMatchers(HttpMethod.POST, "/settlement").hasAuthority("VIEW_SETTLEMENT_REPORTS")
                .antMatchers(HttpMethod.GET, "/settlement/{id}/changes").hasAuthority("VIEW_SETTLEMENT_REPORTS")
                .antMatchers(HttpMethod.GET, "/settlement/{id}").hasAuthority("VIEW_SETTLEMENT_REPORTS")
                .antMatchers(HttpMethod.GET, "/settlement").hasAuthority("VIEW_SETTLEMENT_REPORTS")
                .antMatchers(HttpMethod.PUT, "/settlement").hasAuthority("VIEW_SETTLEMENT_REPORTS")
                .antMatchers(HttpMethod.PUT, "/settlement/approve-actions", "/settlement/decline-actions").hasAuthority("VIEW_SETTLEMENT_REPORTS")
                .antMatchers(HttpMethod.DELETE, "/settlement").hasAuthority("VIEW_SETTLEMENT_REPORTS")
                .antMatchers(HttpMethod.GET, "/settlement/deleted").hasAuthority("VIEW_SETTLEMENT_REPORTS")
                .antMatchers(HttpMethod.POST, "/switch").hasAuthority("CREATE_SWITCH")
                .antMatchers(HttpMethod.GET, "/switch/{id}/changes").hasAuthority("VIEW_SWITCH")
                .antMatchers(HttpMethod.GET, "/switch/{id}").hasAuthority("VIEW_SWITCH")
                .antMatchers(HttpMethod.GET, "/switch").hasAuthority("VIEW_SWITCH")
                .antMatchers(HttpMethod.PUT, "/switch").hasAuthority("UPDATE_SWITCH")
                .antMatchers(HttpMethod.PUT, "/switch/approve-actions", "/switch/decline-actions").hasAuthority("APPROVE_SWITCH")
                .antMatchers(HttpMethod.DELETE, "/switch").hasAuthority("DELETE_SWITCH")
                .antMatchers(HttpMethod.GET, "/switch/deleted").hasAuthority("VIEW_SWITCH")
                .antMatchers(HttpMethod.POST, "/fee-cycles").hasAuthority("TARRIF_FEE_CYCLE")
                .antMatchers(HttpMethod.GET, "/fee-cycles/{id}/changes").hasAuthority("TARRIF_FEE_CYCLE")
                .antMatchers(HttpMethod.GET, "/fee-cycles/{id}").hasAuthority("TARRIF_FEE_CYCLE")
                .antMatchers(HttpMethod.GET, "/fee-cycles").hasAuthority("TARRIF_FEE_CYCLE")
                .antMatchers(HttpMethod.PUT, "/fee-cycles").hasAuthority("TARRIF_FEE_CYCLE")
                .antMatchers(HttpMethod.PUT, "/fee-cycles/approve-actions", "/fee-cycles/decline-actions").hasAuthority("APPROVE_SWITCH")
                .antMatchers(HttpMethod.DELETE, "/fee-cycles").hasAuthority("TARRIF_FEE_CYCLE")
                .antMatchers(HttpMethod.GET, "/fee-cycles/deleted").hasAuthority("TARRIF_FEE_CYCLE")
                .antMatchers(HttpMethod.POST, "/customer-profile").hasAuthority("CUSTOMER_PROFILE")
                .antMatchers(HttpMethod.GET, "/customer-profile/{id}/changes").hasAuthority("CUSTOMER_PROFILE")
                .antMatchers(HttpMethod.GET, "/customer-profile/{id}").hasAuthority("CUSTOMER_PROFILE")
                .antMatchers(HttpMethod.GET, "/customer-profile").hasAuthority("CUSTOMER_PROFILE")
                .antMatchers(HttpMethod.PUT, "/customer-profile").hasAuthority("CUSTOMER_PROFILE")
                .antMatchers(HttpMethod.PUT, "/customer-profile/approve-actions", "/customer-profile/decline-actions").hasAuthority("CUSTOMER_PROFILE")
                .antMatchers(HttpMethod.DELETE, "/customer-profile").hasAuthority("CUSTOMER_PROFILE")
                .antMatchers(HttpMethod.GET, "/customer-profile/deleted").hasAuthority("CUSTOMER_PROFILE")
                .antMatchers(HttpMethod.POST, "/fees").hasAuthority("TARRIF_FEES")
                .antMatchers(HttpMethod.GET, "/fees/{id}/changes").hasAuthority("TARRIF_FEES")
                .antMatchers(HttpMethod.GET, "/fees/{id}").hasAuthority("TARRIF_FEES")
                .antMatchers(HttpMethod.GET, "/fees").hasAuthority("TARRIF_FEES")
                .antMatchers(HttpMethod.PUT, "/fees").hasAuthority("TARRIF_FEES")
                .antMatchers(HttpMethod.PUT, "/fees/approve-actions", "/fees/decline-actions").hasAuthority("TARRIF_FEES")
                .antMatchers(HttpMethod.DELETE, "/fees").hasAuthority("TARRIF_FEES")
                .antMatchers(HttpMethod.GET, "/fees/deleted").hasAuthority("TARRIF_FEES")
                .antMatchers(HttpMethod.POST, "/limits").hasAuthority("TARRIF_LIMIT")
                .antMatchers(HttpMethod.GET, "/limits/{id}/changes").hasAuthority("TARRIF_LIMIT")
                .antMatchers(HttpMethod.GET, "/limits/{id}").hasAuthority("TARRIF_LIMIT")
                .antMatchers(HttpMethod.GET, "/limits").hasAuthority("TARRIF_LIMIT")
                .antMatchers(HttpMethod.PUT, "/limits").hasAuthority("TARRIF_LIMIT")
                .antMatchers(HttpMethod.PUT, "/limits/approve-actions", "/limits/decline-actions").hasAuthority("TARRIF_LIMIT")
                .antMatchers(HttpMethod.DELETE, "/limits").hasAuthority("TARRIF_LIMIT")
                .antMatchers(HttpMethod.GET, "/limits/deleted").hasAuthority("TARRIF_LIMIT")
                .antMatchers(HttpMethod.POST, "/tariff-products").hasAuthority("TARRIF_PRODUCTS")
                .antMatchers(HttpMethod.GET, "/tariff-products/{id}/changes").hasAuthority("TARRIF_PRODUCTS")
                .antMatchers(HttpMethod.GET, "/tariff-products/{id}").hasAuthority("TARRIF_PRODUCTS")
                .antMatchers(HttpMethod.GET, "/tariff-products").hasAuthority("TARRIF_PRODUCTS")
                .antMatchers(HttpMethod.PUT, "/tariff-products").hasAuthority("TARRIF_PRODUCTS")
                .antMatchers(HttpMethod.PUT, "/tariff-products/approve-actions", "/tariff-products/decline-actions").hasAuthority("TARRIF_PRODUCTS")
                .antMatchers(HttpMethod.DELETE, "/tariff-products").hasAuthority("TARRIF_PRODUCTS")
                .antMatchers(HttpMethod.GET, "/tariff-products/deleted").hasAuthority("TARRIF_PRODUCTS")
                .antMatchers(HttpMethod.POST, "/tariffs").hasAuthority("TARIFFS")
                .antMatchers(HttpMethod.GET, "/tariffs/{id}/changes").hasAuthority("TARIFFS")
                .antMatchers(HttpMethod.GET, "/tariffs/{id}").hasAuthority("TARIFFS")
                .antMatchers(HttpMethod.GET, "/tariffs").hasAuthority("TARIFFS")
                .antMatchers(HttpMethod.PUT, "/tariffs").hasAuthority("TARIFFS")
                .antMatchers(HttpMethod.PUT, "/tariffs/approve-actions", "/tariffs/decline-actions").hasAuthority("TARIFFS")
                .antMatchers(HttpMethod.DELETE, "/tariffs").hasAuthority("TARIFFS")
                .antMatchers(HttpMethod.GET, "/tariffs/deleted").hasAuthority("TARIFFS")
                .antMatchers(HttpMethod.POST, "/trained-agents").hasAuthority("CREATE_TRAINED_AGENT")
                .antMatchers(HttpMethod.GET, "/trained-agents/{id}/changes").hasAuthority("VIEW_TRAINED_AGENT")
                .antMatchers(HttpMethod.GET, "/trained-agents/{id}").hasAuthority("VIEW_TRAINED_AGENT")
                .antMatchers(HttpMethod.GET, "/trained-agents").hasAuthority("VIEW_TRAINED_AGENT")
                .antMatchers(HttpMethod.PUT, "/trained-agents").hasAuthority("CREATE_TRAINED_AGENT")
                .antMatchers(HttpMethod.PUT, "/trained-agents/approve-actions", "/trained-agents/decline-actions").hasAuthority("CREATE_TRAINED_AGENT")
                .antMatchers(HttpMethod.DELETE, "/trained-agents").hasAuthority("CREATE_TRAINED_AGENT")
                .antMatchers(HttpMethod.GET, "/trained-agents/deleted").hasAuthority("VIEW_TRAINED_AGENT")
                .antMatchers(HttpMethod.POST, "/trained-agents/mobile/add").hasAuthority("CREATE_TRAINED_AGENT")
                .antMatchers(HttpMethod.POST, "/trained-agents/add").hasAuthority("CREATE_TRAINED_AGENT")
                .antMatchers(HttpMethod.POST, "/trained-agents/upload").hasAuthority("CREATE_TRAINED_AGENT")
                .antMatchers(HttpMethod.GET, "/trained-agents/trained-agents-template.csv").hasAuthority("VIEW_TRAINED_AGENT")
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

