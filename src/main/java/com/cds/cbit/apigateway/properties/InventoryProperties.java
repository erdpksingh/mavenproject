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

package com.cds.cbit.apigateway.properties;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The configuration class will define inventory service properties as a POJO.
 * 
 * @author: Venkata Nagaraju.
 * @version: 1.0.
*/
@Data
@Component
@ConfigurationProperties(prefix = "inv")
public class InventoryProperties {
  
  private String numberSearchGrpc;
  
  private String numberSearchUrl;
  
  private String accountSearchGrpc;
  
  private String accountSearchUrl;
  
  private String simPairGrpc;
  
  private String simPairUrl;
  
  private String unPairGrpc;
  
  private String unPairUrl;
  
  private String subtypeSearchGrpc;
  
  private String subtypeSearchUrl;
} // End of InventoryProperties.