package co.ke.tracom.bprgateway.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** Configures the default Swagger Documentation */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

  /**
   * Configures what to document using Swagger
   *
   * @return A Docket which is the primary interface for Swagger configuration
   */
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("co.ke.tracom.bprgateway"))
        .paths(PathSelectors.regex("/.*"))
        .build()
        .apiInfo(apiEndPointsInfo());
  }

  private ApiInfo apiEndPointsInfo() {
    return new ApiInfoBuilder()
        .title("Swagger Documentation for BPR gateway")
        .description("")
        .contact(new Contact("Ignatius Ojiambo", "https://www.linkedin.com/in/ignatius-ojiambo-a56b2146/", "ignatius.ojiambo@tracom.co.ke"))
        .build();
  }
}
