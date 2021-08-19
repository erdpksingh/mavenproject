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

package com.cds.cbit.accounts.api.account.balance;

import com.cds.cbit.accounts.api.account.balance.flists.CustomerBalanceFlist;
import com.cds.cbit.accounts.api.account.balance.payload.CustomerBalanceResponseResult;

import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.fields.FldDeviceId;
import com.portal.pcm.fields.FldResults;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;

/**
 * The component will perform the actual work flow and business logic for fetching customer balance
 * information. The method will execute "BAL_GET_ECE_BALANCES" opcode in BRM, and with the output of
 * the opcode, it will derive all the sections of GetCustomerBalance API response.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component
public class CustomerBalanceExecutor {
  
  private final CustomerBalanceWorkFlow workflow;
  private final CustomerBalanceFlistExecutor flistExecutor;

  /** Constructor Injection. **/
  public CustomerBalanceExecutor(
         final CustomerBalanceFlistExecutor flistExecutor,final CustomerBalanceWorkFlow workflow) {

    this.workflow = workflow;
    this.flistExecutor = flistExecutor;
  } // End of constructor injection.

  /**
   * The method will fetch "CustomerBalanceResponseResult" section with the help of other components
   * like "CustomerBalanceWorkFlow" and "CustomerBalanceFlistExecutor".The method will consider both
   * the scenario account with MSISDN and without MSISDN to fetch the customer balance information.
   * 
   * @param: accountObj - @Mandatory - Account POID of request account.
   * @param: msisdnOp   - @Mandatory - MSISDN output FList associated with the account.
  */
  public CustomerBalanceResponseResult fetchCustomerBalances(
                 final String accountObj, final FList msisdnOp) throws EBufException,JAXBException {

    // IF MSISDN output contain results field then fetch balanced with MSISDN otherwise without it.
    if (msisdnOp.hasField(FldResults.getInst())) {
      
      log.info("Getting balances with MSISDN.");
      return fetchBalancesWithMsisdn(msisdnOp,accountObj);
      
    } else { // If MSISDN not available fetch package and status information.
      
      log.info("Getting balances without MSISDN.");
      return workflow.fetchResultWithoutMsisdn(accountObj);
    } // End of MSISDN output check for results field.
  } // End of the fetchCustomerBalances method.
 
  /**
   * The method will be invoked if an MSISDN associated with the given account. The method will
   * execute BAL_GET_ECE_BALANCES opcode with "CustomerBalanceFlistExecutor" component and extract
   * balance and counter balance information from the output FList.
   * 
   * @param: msisdnOp  - @Mandatory - MSISDN output for the given account.
   * @param: accountObj- @Mandatory - Account POID of request account.
  */
  private CustomerBalanceResponseResult fetchBalancesWithMsisdn(
                  final FList msisdnOp,final String accountObj) throws EBufException,JAXBException {
    final String msisdn = 
          msisdnOp.get(FldResults.getInst()).getValues().get(0).get(FldDeviceId.getInst());
    
    final FList balanceOp = flistExecutor.executeCustomerBalances(msisdn);
    final CustomerBalanceFlist customerBalance = flistExecutor.fetchBalancePojoFromFlist(balanceOp);
    
    return workflow.fetchBalanceInformation(balanceOp,customerBalance,accountObj);
  } // End of fetchBalancesWithMsisdn method.
} // End of PairUnpairExecutor.