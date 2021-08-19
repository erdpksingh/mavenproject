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

package com.cds.cbit.accounts.commons;

import com.cds.cbit.accounts.config.BrmCmConfig;
import com.cds.cbit.accounts.connections.BrmConnectionPool;
import com.cds.cbit.accounts.exceptions.BillingException;
import com.cds.cbit.accounts.exceptions.ValidationException;
import com.cds.cbit.accounts.factory.TransactionFactory;
import com.cds.cbit.accounts.util.ConnectionErrorUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.PortalContext;

import java.util.Map;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component provide centralized global transaction service for the API which need transaction
 * management. The  component opens a global transaction on BRM CM and process the business logic
 * Required for the API process. If the process completes successfully then the transaction will be
 * Committed otherwise it will abort.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component
public class BrmTransactionManager {
  
  @Autowired
  private TransactionFactory factory;
  
  private final BrmCmConfig brmPool; // Billing system CM connection pool.

  public BrmTransactionManager(final BrmCmConfig brmPool) {
    this.brmPool = brmPool;
  } // End of dependency injection.
  
  /**
   * The method will perform given bean under a global transaction. The method takes two parameters,
   * the first argument provides the bean name, through which appropriate process will invoke under
   * global transaction, the second argument provide the inputs required for the processor to carry
   * out operation in BRM.
   * 
   * @param: beanName -  @Mandatory - Bean name given to the invoking processor.
   * @param: inputs   -  @Mandatory - Inputs required to process the business logic.
  */
  public Object executeWithGlobalTransaction(final String beanName,final Map<String,Object> inputs) 
                                                              throws EBufException, JAXBException {
    final BrmConnectionPool brmConnectionPool = brmPool.getBrmPool();
    final PortalContext portal = brmConnectionPool.borrowContext();
    final FList flist = (FList) inputs.get("flist");
    
    try {
      portal.opcode(12, 65536, flist);
      final FList output = factory.process(beanName).processTransaction(inputs,portal);
      portal.opcode(14, flist);
      
      log.info("Committing the transaction.");
      return output;
    } catch (BillingException | ValidationException e) {
      log.error("Aborting BRM transaction due to. {}", e.getMessage());
      portal.opcode(13, flist);
      throw e;
    } catch (Exception e) {
      log.error("Aborting BRM transaction due to. {}", e.getMessage());
      portal.opcode(13, flist);
      throw new BillingException(ConnectionErrorUtil.getConnectionError(e),e);
    } finally {
      brmConnectionPool.returnContext(portal);
    } // End of BRM opcode execution.
  } // End of executeWithGlobalTransaction method.
} // End of TransactionManager.