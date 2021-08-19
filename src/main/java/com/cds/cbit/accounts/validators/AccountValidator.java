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
import com.cds.cbit.accounts.exceptions.BillingException;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.util.BillingInfoSearchUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.fields.FldResults;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
@Component("accountValidator")
public class AccountValidator implements BillingValidation {
  
  @Autowired
  private Environment properties;
  
  private final BillingInfoSearchUtil searchUtil;
  
  /** Constructor injection. **/
  public AccountValidator(final BillingInfoSearchUtil searchUtil) {
    
    this.searchUtil = searchUtil;
  } // End of Constructor Injection

  /* @see com.cds.cbit.inventory.interfaces.BillingValidation#validateInput. */
  @Override
  @Loggable
  public FList validateInput(final String input,final String... data) 
                                                          throws EBufException, JAXBException {
    
    final Results results = Results.builder().elem("0").accountObj("")
                                                                 .createdT(0L).poid("").build();
    // Preparing Args section of PCM_OP_SEARCH.
    
    final Args accountArgs = Args.builder().elem("1").accountObj("").build();
    final Args poidArgs    = Args.builder().elem("2").poid("").build();
    final Args acctNoArgs  = Args.builder().elem("3").accountNumber(input).build();
    
    final StringBuilder profileProps = 
                          new StringBuilder("account.").append(data[0]);
    final String acctProfile = properties.getProperty(profileProps.toString());
    final StringBuilder profile = 
                   new StringBuilder("0.0.0.1 ").append(acctProfile).append(" -1 0");
    
    final Args accountPoidArgs = Args.builder().elem("4").poid(profile.toString()).build();
    
    final Args[] arguments = {accountArgs,poidArgs,acctNoArgs,accountPoidArgs};
    
    final StringBuilder tempelate = 
          new StringBuilder("select X from ").append(acctProfile)
                        .append(" 1,/account 2  where 1.F1=2.F2 and 2.F3 = V3 and F4.type like V4");
    
    final FList output = searchUtil.executeSearchTemplate(tempelate.toString(), arguments, results);
        
    // Checking whether results field exist in the output FList or not.
    
    if (!(output.hasField(FldResults.getInst()))) {
      throw new BillingException("100111");
    } // End of account results check.
    return output;
  } // End of validating given account number in BRM.
} // End of AccountValidator, which validated given account in BRM.