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

import com.cds.cbit.accounts.aspects.Loggable;
import com.cds.cbit.accounts.connections.BrmConnectionFactory;
import com.cds.cbit.accounts.connections.BrmConnectionPool;
import com.cds.cbit.accounts.properties.CmPoolProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration class provide BRM connection manager pool for maintaining CM connections
 * while executing requests in BRM.
 * 
 * @author Venkata Nagaraju.
 * @version 1.0.
*/
@Configuration
public class BrmCmConfig {
  
  private final BrmConnectionFactory factory;
  private final CmPoolProperties cm;
  
  public BrmCmConfig(final BrmConnectionFactory factory,final CmPoolProperties cm) {
    this.factory = factory;
    this.cm = cm; 
  } // End of dependency injection.
  
  /**
   * The bean will create a connection pool for BRM connection manager. The pool will help to
   * execute BRM operations in BRM system without overloading the CM connections.
   * 
   * @return - BRM CM connection pool.
  */
  @Bean
  @Loggable
  public BrmConnectionPool getBrmPool() {
    
    final int maxIdle  = cm.getMaxIdle();  // Maximum CM connection idle at a time.
    final int minIdle  = cm.getMinIdle();  // Minimum CM connection idle at a time.
    final int maxWait  = cm.getMaxWait();  // Maximum time CM wait without task.
    final int poolSize = cm.getPoolSize(); // Total CM connections.
    
    return new BrmConnectionPool(factory,maxIdle,minIdle, maxWait,poolSize);
  } // End of BRM connection pool configuration.
} // End of PoolConfig.