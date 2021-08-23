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

package com.cds.cbit.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * API Gateway acts as an infrastructure service which will act as an interface
 * between private business-services and public external network. The service
 * will handle all the cross cutting Functionality like security,logging,and
 * follow EIP to route the data between external systems and internal services.
 * 
 * @author Venkata Nagaraju.
 * @version 1.0.
*/
@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan("com.cds.cbit.apigateway")
@PropertySource({"classpath:globalErrors.properties"})
//@EnableConfigurationProperties({ BrmPoolProperties.class, BrmThreadProperties.class})
public class ApiGatewayServices {
  
  public static void main(final String[] args) {
    SpringApplication.run(ApiGatewayServices.class, args);
  } // End of main.
} // End of ApiGatewayServices.