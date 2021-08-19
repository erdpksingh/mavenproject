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

import com.cds.cbit.accounts.api.account.flist.NameInfo;
import com.cds.cbit.accounts.api.customer.detail.flist.ExtraResults;
import com.cds.cbit.accounts.api.customer.detail.flist.LinkedObj;
import com.cds.cbit.accounts.commons.beans.Args;
import com.cds.cbit.accounts.commons.beans.Results;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.util.BillingInfoSearchUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

/**
 * The implementation component provides methods for validating name information for given account 
 * in the system. The component overrides BillingValidationInterface validateInput method to create
 * and execute name search FList.If the validation is success then the method will return FList
 * else it will throw an exception to the caller.
 * 
 * @author Meghashree Udupa
 *
 */
@Component("nameValidator")
public class NameDetailsValidator implements BillingValidation {

  private final BillingInfoSearchUtil searchUtil;
  
  public NameDetailsValidator(final BillingInfoSearchUtil searchUtil) {
    
    this.searchUtil = searchUtil;
  } // End of constructor injection.
 
  @Override
  public FList validateInput(final String input,final String... data) 
                                                                throws EBufException,JAXBException {
    final LinkedObj linkedObj = 
          LinkedObj.builder().elem("2").accObj("").linkDirection("1")
                .extraResults(ExtraResults.builder().elem("1").billingDom("0").build()).build();
    final Results results = 
          Results.builder().elem("0")
                   .nameInfo(NameInfo.builder().elem("1").build()).linkedObj(linkedObj).build();

    final Args poidArgs = Args.builder().elem("1").poid(input).build();
    final Args accObjArgs = Args.builder().elem("2").accountObj("0.0.0.1 /account 0").build();
    final Args poid2Args = Args.builder().elem("3").poid("0.0.0.1 /account 0").build();
    
    final Args[] arguments = { poidArgs, accObjArgs, poid2Args };

    return searchUtil.executeSearchTemplate(ConstantsUtil.ACCOUNT_NAME_SQL, arguments, results);
  } // End of validateInput method.
} // End of NameDetailsValidator class.