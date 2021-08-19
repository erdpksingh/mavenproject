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
@ConfigurationProperties(prefix = "prepaid")
public class PrepaidProperties {

  protected String name; // Specifies type of account to be created.
  
  protected String payType; // Payment type.
  
  protected String currency; // Currency number.
  
  protected String payCollectType; // payment collect number.
} // End of AccountProperties