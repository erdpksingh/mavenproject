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
 * The configuration class will define payment service properties as a POJO.
 * 
 * @author: Venkata Nagaraju.
 * @version: 1.0.
*/
@Data
@Component
@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {
  
  private String acctStatementGrpc;
  
  private String acctStatementUrl;
  
  private String paymentReversalGrpc;
  
  private String paymentReversalUrl;
  
  private String postAdjustmentGrpc;
  
  private String postAdjustmentUrl;
  
  private String collectPaymentGrpc;
  
  private String collectPaymentUrl;
  
  private String searchItemGrpc;
  
  private String searchItemUrl; 
  
  private String creditCapGrpc;
  
  private String creditCapUrl; 
} // End of InventoryProperties.