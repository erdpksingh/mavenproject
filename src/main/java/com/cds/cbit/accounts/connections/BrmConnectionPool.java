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

package com.cds.cbit.accounts.connections;

import com.cds.cbit.accounts.exceptions.BillingException;
import com.portal.pcm.PortalContext;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * The class will create BRM PortalContext connection pool factory with 
 * a configured number of connections.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
public class BrmConnectionPool extends GenericObjectPool<PortalContext> {

  /** Public constructor to create connections with specified configurations. **/
  public BrmConnectionPool(
      final BrmConnectionFactory poolFactory,final int maxIdle,final int minIdle,
                                                       final int maxWait,final int poolSize) {
    
    super(poolFactory,poolSize);
    this.setMaxWait(maxWait);
    this.setMaxIdle(maxIdle);
    this.setMinIdle(minIdle);
    log.info("BRM Portal context connection pool size: {}", poolSize);
  } // End of constructor

  /**
   * Borrows a BRM Connection from the pool. The pool creates Objects on demand
   * and up to the configured limit.
   * 
   * @return com.portal.pcm.PortalContext
   * @throws Exception : if any problems borrowing object form the pool
  */
  public PortalContext borrowContext() {
    try {
      return borrowObject();
    } catch (Exception e) {
      final StringBuilder error = new StringBuilder("Connection pool error -")
                                                      .append(e.getMessage());
      throw new BillingException(error.toString(),e);
    }
  } // End of borrowing BRM connection.

  /**
   * Returns CM connection to the pool.
   * @param portalContext : BRM connection object.
  */
  public void returnContext(final PortalContext portalContext) {
    try {
      returnObject(portalContext);
    } catch (Exception e) {
      log.error("Error occurred while returning the portal context to pool.");
    }
  } // End of returning connection to the pool
} // End of BrmConnectionPool.