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

import com.cds.cbit.accounts.api.account.flist.Deal;
import com.cds.cbit.accounts.commons.beans.Args;
import com.cds.cbit.accounts.commons.beans.Results;
import com.cds.cbit.accounts.commons.beans.Services;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.util.BillingInfoSearchUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;

import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

/**
 * The implementation component provides methods for validating deals for given plan 
 * in the system.The component overrides BillingValidationInterface validateInput method to
 * create and execute deal search FList.If the validation is success then the method will
 * return output FList else it will throw an exception to the caller.
 * 
 * @author  Meghashree Udupa
 * @version 1.0.
*/
@Component("dealValidator")
public class DealDetailsValidator implements BillingValidation {

  private final BillingInfoSearchUtil searchUtil;

  public DealDetailsValidator(final BillingInfoSearchUtil searchUtil) {
    
    this.searchUtil = searchUtil;
  } // End of constructor injection.

  @Override
  public FList validateInput(final String input,final String... data) 
                                                                throws EBufException,JAXBException {
    final Results results = Results.builder().elem("0").build();

    final Args accountArgs = 
          Args.builder().elem("1").services(Arrays.asList(
                                   Services.builder()
                                         .deal(Arrays.asList(
                                         Deal.builder().dealObj(input).build())).build())).build();
    final Args[] arguments = { accountArgs };
    return searchUtil.executeSearchTemplate(
                                   "select X from /plan where F1 = V1", arguments, results, "512");
  } // End of validateInput method.
} // End of DealDetailsValidator class.