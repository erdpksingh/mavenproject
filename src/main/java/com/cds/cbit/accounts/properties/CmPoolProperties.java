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
 * The property configuration file provide all BRM connection pool related
 * properties. All these property names should prefix with 'cm'.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Data
@Component
@ConfigurationProperties(prefix = "cm")
public class CmPoolProperties {
  
  //BRM CM connection pool details.
  
  private int poolSize; // CM connections count.
 
  private int maxIdle; // Max no of connection at a time.
 
  private int minIdle; // Minimum connection in idle state.
 
  private int maxWait; // Max milliseconds waiting time before connection destroy.
} // End of CmPoolProperties.