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

package com.cds.cbit.accounts.properties;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The POJO represents property fields of account module mentioned in property 
 * file with prefix "account".
 * 
 * @author  Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Component
@ConfigurationProperties(prefix = "account")
public class AccountConfigProperties {

  protected String type; // Specifies type of account to be created.
  
  protected String locale; // Customer locale language.
  
  protected String deliveryPreference; // Delivery Preference.
  
  protected String payType; // Payment type.
  
  protected String invTerms; // Invoice Terms.
  
  protected String paymentName; // Payment name of invoice section.
  
  protected String contactType; // Contact type of the customer.
  
  protected String custName; // Customer name.
  
  protected String custEmail; // Customer email.
  
  protected String billId; // Billing info ID.
  
  protected String currency; // Currency number.
} // End of AccountProperties