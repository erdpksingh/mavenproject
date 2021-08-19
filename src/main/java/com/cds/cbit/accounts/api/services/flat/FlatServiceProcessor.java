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

package com.cds.cbit.accounts.api.services.flat;

import com.cds.cbit.accounts.commons.beans.Args;
import com.cds.cbit.accounts.commons.beans.Members;
import com.cds.cbit.accounts.commons.beans.Results;
import com.cds.cbit.accounts.commons.factory.BillingInputValidateFactory;
import com.cds.cbit.accounts.interfaces.AccountServices;
import com.cds.cbit.accounts.interfaces.GenericResponse;
import com.cds.cbit.accounts.util.BillingInfoSearchUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;

import javax.xml.bind.JAXBException;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * The component class will provide the business logic for fetching account details from the system
 * for the flat account.The class first validate given account number and if it is valid then it 
 * will fetch the details of the given account from billing system.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0
*/
@Component("flatAccountDetails")     // Same bean name will be used for hierarchical account.
@ConditionalOnProperty(prefix = "account", name = "type", havingValue  = "flat")
public class FlatServiceProcessor implements AccountServices {
  
  private final BillingInputValidateFactory validator;  // Component to validate billing inputs.
  private final BillingInfoSearchUtil searchUtil;     // Util class to execute search opcode.
  
  /** Injecting validator and searchUtil through constructor. **/
  public FlatServiceProcessor(
               final BillingInputValidateFactory validator,final BillingInfoSearchUtil searchUtil) {
    
    this.validator = validator;
    this.searchUtil = searchUtil;
  } // End of constructor injection.
  
  
  /* @see com.cds.cbit.accounts.interfaces.AccountServices#processAccountService(java.lang.String)*/
  @Override
  public GenericResponse processAccountService(final String accountNo)  
                                                              throws EBufException, JAXBException {
    validateAccount(accountNo);
    return null;
  } // End of processing flat account service request.
  
  /**
   * The method will validate the given flat account in the billing system whether it exist or not.
   * If it exists then the method go further and process account details FList to fetch the account
   * related information.
   * 
   * @param: accountNo  @Mandatory - Customer account number.
  */
  private void validateAccount(final String accountNo) throws EBufException, JAXBException {

    validator.getValidatorWithMultiArgs(
          "hierarchicalAccount",accountNo,ConstantsUtil.FLAT_PROFILE,ConstantsUtil.FLAT_ACCT_SQL);
    fetchAccountDetail(accountNo);
  } // End of processing fetch account details request.
  
  /**
   * The method will create search FList for fetching account information for the given account.
   * 
   * @param: baAccountNo - @Mandatory - Customer billing account number.
  */
  private void fetchAccountDetail(final String baAccountNo) throws EBufException, JAXBException {
    
    final Members memeber = Members.builder().elem("1").obj("").build();
    final Args memebersFld = Args.builder().member(memeber).build();
    final Args poidFld = Args.builder().poid("").build();
    final Args accountNoFld = Args.builder().accountNumber(baAccountNo).build();
    
    final Args[] arguments = {memebersFld,poidFld,accountNoFld};
    final Results results = Results.builder().accountObj("").build();
    searchUtil.executeSearchTemplate(ConstantsUtil.ACCTDETAIL_SQL, arguments, results);
  } // End of creating and executing fetchAccountDetail FList.
} // End of AccountDetailsService for fetching account details.