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

import com.portal.pcm.EBufException;
import com.portal.pcm.PortalContext;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.springframework.stereotype.Component;

/**
 * Factory class for BRM PortalContext.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component
public class BrmConnectionFactory extends BasePoolableObjectFactory<PortalContext> {

  /**
   * Creates an instance that can be served by the pool. Instances returned from this method should
   * be in the same state as if they had been activated. They will not be activated before being 
   * served by the pool. Uses PortalContext.connect() method which assumes an Infranet.properties
   * file is in the CLASSPATH
   * 
   * @return com.portal.pcm.PortalContext
   * @throws Exception :-if we have problems connecting to BRM
  */
  @Override
  public PortalContext makeObject() throws Exception {
    
    final PortalContext portalContext = new PortalContext();
    portalContext.connect();
    return portalContext;
  } // End of creating BRM CM connection.
  

  /** Method that is called when a connection is borrowed from the pool. */
  @Override
  public void activateObject(final PortalContext obj) {
    log.info("Connection activation called.");
  } // End of activating context.
  

  /** Uninitialize an instance to be returned to the idle object pool.*/
  @Override
  public void passivateObject(final PortalContext obj) {
    log.info("Passivating the given connection.");
  } // End of context passive.
  
  /**
   * The overridden method will destroy the CM connection when CM connection returns twice or
   * CM connection waited for the maximum no of milliseconds specified without task allocation
   * In this case pool will create a new CM connection.
  */
  @Override
  public void destroyObject(final PortalContext obj) {
    try {
      final PortalContext portalContext =  obj;
      portalContext.close(true);
      super.destroyObject(obj);
    } catch (EBufException e) {
      log.error("BRM Error occurred while destroying context. {}", e.getMessage());
    } catch (Exception e) {
      log.error("Error occurred while destroying context.{}", e.getMessage());
    } // End of destroying BRM context.
  } // End of destroying portal context.
} // End of BrmConnectionFactory.