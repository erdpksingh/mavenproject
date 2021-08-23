/*
 * Copyright (C) 2019 Covalense Technologies 
 *
 * Licensed under the Covalense CSMART, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.covalense.com
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
*/

package com.cds.cbit.apigateway.properties;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The property configuration file provide all BRM database connection related properties. 
 * All these property names should prefix with 'spring.datasource'.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DbProperties {
  
  //BRM CM connection pool details.
  
  private String url; // CM connections count.
 
  private String username; // Max no of connection at a time.
 
  private String password; // Minimum connection in idle state.
} // End of CmPoolProperties.