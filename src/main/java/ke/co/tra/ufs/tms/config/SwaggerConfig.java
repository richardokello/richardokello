/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tra.ufs.tms.config;

import com.google.common.base.Predicates;
import static com.google.common.collect.Lists.newArrayList;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ClientCredentialsGrant;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author Owori Juma
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    
    @Value("${app.params.baseUrl: }")
    private String baseUrl;
    @Value("${spring.application.name: Cash Collection Api}")
    private String appName;
    @Value("${app.params.application.description: }")
    private String appDesc;
    @Value("${app.params.application.version: 0.0.1}")
    private String version;
    
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
//                .paths(PathSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/auditevents.*|/error|/autoconfig.*|/beans.*"
                        + "|/configprops.*|/dump.*|/features.*|/info.*|/mapping.*|/trace.*|/env.*|/pause.*"
                        + "|/refresh.*|/resume.*|/heapdump.*|/loggers.*|/restart.*|/oauth/error")))
                .build()
                .apiInfo( apiInfo() )
                .globalResponseMessage(RequestMethod.GET, newArrayList(
                        new ResponseMessageBuilder().code(500).message("Internal Server Error")
                                .responseModel(new ModelRef("Error"))
                        .build()
                ))
                .securitySchemes( newArrayList( oauthScheme() ))          
                ;
    }
    
    ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title(appName)
                .description(appDesc)
//                .license("")
//                .licenseUrl("")
//                .termsOfServiceUrl("")
                .version(version)
//                .contact(new Contact("", "", ""))
                .build()
                ;
    }
    
    @Bean
    public SecurityScheme oauthScheme(){
        List<GrantType> grantTypes = new ArrayList<>();
        String tokenUrl = baseUrl + "/oauth/token";
        GrantType clientGrantType = new ClientCredentialsGrant(tokenUrl);
        grantTypes.add(clientGrantType);
        GrantType passwordGrantType = new ResourceOwnerPasswordCredentialsGrant(tokenUrl);
        grantTypes.add(passwordGrantType);
        
        return new OAuthBuilder()
                .name("octaSwaggerClient")
                .grantTypes(grantTypes)
                .build();
    }

}
