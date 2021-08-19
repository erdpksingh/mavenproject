/*
 * Copyright (C) 2019 Covalensedigital 
 *
 * Licensed under the CBIT,Version 1.0,you may not use this file except in compliance with the 
 * License. You may obtain a copy of the License at 
 * 
 * http://www.covalensedigital.com/
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or
 * implied.See the License for the specific language governing permissions and limitations under.
*/

package com.cds.cbit.accounts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The configuration class will enable swagger2 for the service endpoints of billing DB service and 
 * provide swagger documentation for all the endpoints available in the package and sub-packages of 
 * com.cds.crm.cbit.accounts.
 * 
 * @author: Venkata Nagaraju.
 * @version: 1.0.
*/
@EnableSwagger2
@EnableWebMvc
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

  /** Creating request handlers for swagger with default document information. **/
  @Bean
  public Docket api() {

    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage("com.cds.cbit.accounts"))
        .paths(PathSelectors.regex("/.*")).build().apiInfo(apiEndPointsInfo());
  } // End of request handlers.

  /** Method to define Swagger document information for APIs. **/
  private ApiInfo apiEndPointsInfo() {

    return new ApiInfoBuilder().title("Billing DB Services").version("V1.0")
        .description("Billing DB Services provide various web service endpoints for "
                                     + " customer account related queries.").build();
  } // End of swagger document information.

  /** Customizing swagger UI. */
  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler("**").addResourceLocations("classpath:/dist/");

    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  } // End of defining custom UI files.
} // End of SwaggerConfig.