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

import com.cds.cbit.accounts.api.account.balance.payload.CustomerBalanceRequest;
import com.cds.cbit.accounts.api.account.balance.payload.CustomerBalanceResponse;
import com.cds.cbit.accounts.api.account.balance.payload.CustomerBalanceResponseResult;
import com.cds.cbit.accounts.exceptions.BillingException;
import com.cds.cbit.accounts.factory.BillingValidationFactory;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.properties.AccountConfigProperties;
import com.cds.cbit.accounts.util.UtcDateUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.fields.FldAccountObj;
import com.portal.pcm.fields.FldResults;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component class will validate the request input in BRM, for their correctness and if 
 * validation are successful, then the method will execute the required workflow and operational
 * execution with the help of "CustomerBalanceExecutor".If the operation completes successfully 
 * then the class will sends generic response to the controller otherwise will throw appropriate
 * exception to the controller.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class CustomerBalanceProcessor {

  @Autowired
  private BillingValidationFactory factory;
  
  private final CustomerBalanceResponser responser;
  private final CustomerBalanceExecutor executor;
  private final AccountConfigProperties properties;


  /** Constructor injection. */
  public CustomerBalanceProcessor(final CustomerBalanceResponser responser,
         final AccountConfigProperties properties,final CustomerBalanceExecutor executor) {
    
    this.properties = properties;
    this.executor = executor;
    this.responser = responser;
  } // End of constructor injection.

  /** 
   * The method will fetch customer balance information for the given account for the API
   * GetCustomerBalance. The method will validate the BRM input and then proceed with fetching
   * balance information from BAL_GET_ECE_BALANCES opcode.
   * 
   * @param: profile  - @Mandatory - Customer profile type.
   * @param: request  - @Mandatory - Customer balance GET call request.
   * @return 
  */
  public CustomerBalanceResponse 
         processCustomerBalance(final CustomerBalanceRequest request)
                                                 throws EBufException,ParseException,JAXBException {
    
    // Validating the given input in BRM for account existence, fromDate is future date or not.
    validateFromDateInfPresent(request.getBody().getFrom());
    final String accountPoid = fetchPoidOfGivenAccount(request.getBody().getAccountNumber());
    
    // Fetching MSISDN information from the given account.
    final BillingValidation acctMsisdnValidator = factory.getValidator("accountMsisdnValidator");
    final FList msisdnOp = acctMsisdnValidator.validateInput(accountPoid);
    
    final CustomerBalanceResponseResult result = 
                                               executor.fetchCustomerBalances(accountPoid,msisdnOp);
    result.setAccountNumber(request.getBody().getAccountNumber());
    return responser.createResponse(request, result);
  } //End of processCustomerBalance.
  
  /**
   * The method will check whether "fromDate" field available in the request or not. If it exist
   * then the method will verify whether it is future date or not. If it is a future date then an
   * exception will be thrown, as balances cannot fetch for future date.
   * 
   * @param: requestedFromDate - @Mandatory - From date provided in the request.
  */
  private void validateFromDateInfPresent(final String requestedFromDate) throws ParseException {
    
    final Optional<String> fromDateStr = Optional.ofNullable(requestedFromDate);
    
    if (fromDateStr.isPresent()) {
      
      final Date fromDate = UtcDateUtil.convertStringToDate(fromDateStr.get());
      
      if (fromDate.after(new Date())) { // If fromDate is future date throw error.
        throw new BillingException("200201");
      } // End of form date check.
    } // End of fromDate check.
  } // End of validateFromDateInfPresent method.
  
  /**
   * The method will validate whether given account number exist in the BRM or not. If it exist
   * then the method retrieve its poid from the output and return it.
   * 
   * @param: accountNumber - @Mandatory - Requested account number.
  */
  private String fetchPoidOfGivenAccount(final String accountNumber) 
                                                             throws EBufException, JAXBException {
    /* 
     * Forming beanName with accountType from config and constant "profile". If requested account
     * is flat type, then validation will be carried by "flatProfile" bean otherwise by the bean
     * "hierarchicalProfile". 
    */
    final StringBuilder acctProfile   = new StringBuilder(properties.getType()).append("Profile");
    final BillingValidation validator = factory.getValidator("accountValidator");
    
    final FList accountOp   =   validator.validateInput(accountNumber,acctProfile.toString());
    return accountOp.get(FldResults.getInst()).getValues()
                                              .get(0).get(FldAccountObj.getInst()).toString();
  } // End of fetchPoidOfGivenAccount method.
} // End of CustomerBalanceProcessor, which fetch customer balance information from ECE.