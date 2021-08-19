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

package com.cds.cbit.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * The AccountService exposes various service endpoints related to billing system customer module
 * to the API gateway.The service acts as a single point of truth for all customer account related
 * operations of the billing system.After completing the request the service will return response 
 * to API gateway.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@EnableWebMvc
@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan({"com.cds.cbit.accounts"})
@PropertySource({ "classpath:accountErrors.properties","classpath:globalErrors.properties",
                  "classpath:shadowElements.properties","classpath:externalId.properties"})
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class AccountServiceMain {

  public static void main(final String[] args) {
    SpringApplication.run(AccountServiceMain.class, args);
  } // End of main.
} // End of AccountService.