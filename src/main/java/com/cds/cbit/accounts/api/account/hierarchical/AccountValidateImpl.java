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

package com.cds.cbit.accounts.api.account.hierarchical;

import com.cds.cbit.accounts.commons.beans.Args;
import com.cds.cbit.accounts.commons.beans.Results;
import com.cds.cbit.accounts.interfaces.MultiArgValidation;
import com.cds.cbit.accounts.util.BillingInfoSearchUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

/**
 * The implementation component provides methods for validating given CA | BA | SA accounts in the 
 * system. The component overrides MultiArgValidationInterface::validateInput method to create and 
 * execute account search FList.If the validation is success then the method will return output 
 * FList else it will throw an exception to the caller.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component("hierarchicalAccount")
public class AccountValidateImpl implements MultiArgValidation {
  
  private final BillingInfoSearchUtil searchUtil;     // Util class to execute search opcode.
  
  public AccountValidateImpl(final BillingInfoSearchUtil searchUtil) {
    
    this.searchUtil = searchUtil;
  } // End of constructor injection.
  
  /* @see com.circles.brm.api.v1.account.interfaces.BillingValidationInterface. */
  @Override
  public FList validateInput(final String accountNo,final String...data) 
                                                               throws EBufException, JAXBException {

    return validateHierachicalAccount(accountNo,data[0],data[1]);
  } // End of validateInput method.
  
  /**
   * The method will take account number as input and validate it in billing system.The method
   * creates Args and Results fields of search template and convert the search template to FList and
   * execute it in billing system with the help of BillingInfoSearchUtil.
   * 
   * @param: accountNo   @Mandatory - CA | BA | SA account number.
   * @param: accountObj  @Mandatory - Account object. 
   * @param: template    @Mandatory - Search template [SQL Query of billing system].
  */
  public FList validateHierachicalAccount(
                            final String accountNo,final String accountObj,final String template) 
                                                               throws EBufException, JAXBException {
   
    final Args poidInput = Args.builder().elem("1").poid(ConstantsUtil.ZERO_POD).build();
    final Args accountInput = Args.builder().elem("2").accountNumber(accountNo).build();
    final Args accountObjInput = Args.builder().elem("3").accountObj(accountObj).build();
    
    final Args[] args = { poidInput,accountInput,accountObjInput };
    
    // Results field of search template.
    final Results results = Results.builder().elem("0").accountObj("").poid(ConstantsUtil.ZERO_POD)
                                             .createdT(0L).build();
    
    return searchUtil.executeSearchTemplate(template,args,results);
  } // End of validating CA | BA | SA account in BRM.
} // End AccountValidateImpl class which validate CA | BA | SA accounts.