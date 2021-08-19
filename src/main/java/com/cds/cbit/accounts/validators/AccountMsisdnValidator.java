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

package com.cds.cbit.accounts.validators;

import com.cds.cbit.accounts.aspects.Loggable;
import com.cds.cbit.accounts.commons.beans.Args;
import com.cds.cbit.accounts.commons.beans.Results;
import com.cds.cbit.accounts.commons.beans.Services;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.util.BillingInfoSearchUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

/**
 * The implementation component provides methods for validating given account inputs in the system.
 * The component overrides BillingValidationInterface validateInput method to create and execute 
 * account search FList.If the validation is success then the method will return output FList else
 * it will throw an exception to the caller.
 * 
 * @author  Nistha Khare.
 * @version 1.0.
*/
@Component("accountMsisdnValidator")
public class AccountMsisdnValidator implements BillingValidation {
  
  private final BillingInfoSearchUtil searchUtil;
  
  /** Constructor injection. **/
  public AccountMsisdnValidator(final BillingInfoSearchUtil searchUtil) {
    
    this.searchUtil = searchUtil;
  } // End of Constructor Injection

  /* @see com.cds.cbit.inventory.interfaces.BillingValidation#validateInput. */
  @Override
  @Loggable
  public FList validateInput(final String input,final String... data) 
                                                          throws EBufException, JAXBException {
    
    final Results results = Results.builder().elem("0").deviceId("").poid("").build();
    // Preparing Args section of PCM_OP_SEARCH.
    final Services[] services = { Services.builder().accountObj("").elem("0").build() };
    final List<Services> serviceList = Arrays.asList(services);
    
    final Args accountArgs = Args.builder().elem("1").services(serviceList).build();
    final Args poidArgs    = Args.builder().elem("2").poid("").build();
    final Args acctNoArgs  = Args.builder().elem("3").poid(input).build();
    final Args deviceArgs  = Args.builder().elem("4").poid("0.0.0.1 /device/num 1 0").build();
    
    final Args[] arguments = {accountArgs,poidArgs,acctNoArgs,deviceArgs};
        
    return searchUtil.executeSearchTemplate(
                                        ConstantsUtil.ACCOUNT_MSISDN_SQL, arguments, results);
  } // End of validating given account number in BRM.
} // End of AccountValidator, which validated given account in BRM.