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

import com.cds.cbit.accounts.commons.beans.Args;
import com.cds.cbit.accounts.commons.beans.Results;
import com.cds.cbit.accounts.exceptions.BillingException;
import com.cds.cbit.accounts.interfaces.TransactionValidator;
import com.cds.cbit.accounts.util.BillingInfoSearchUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.PortalContext;
import com.portal.pcm.fields.FldResults;
import javax.xml.bind.JAXBException;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;

/**
 * The implementation component provides methods for validating given deal input in the system.
 * The component overrides BillingValidationInterface validateInput method to create and execute 
 * deal existence search FList.If the validation is success then the method will return output
 * FList else it will throw an exception to the caller.
 * 
 * @author  Meghashree Udupa.
 * @version 1.0.
*/
@Log4j2
@Component("accountPlanValidator")
public class AccountPlanValidator implements TransactionValidator {

  private final BillingInfoSearchUtil searchUtil;

  public AccountPlanValidator(final BillingInfoSearchUtil searchUtil) {

    this.searchUtil = searchUtil;
  } // End of Constructor Injection

  /*
   * @see com.cds.cbit.inventory.interfaces.BillingValidation#validateInput
   */
  @Override
  public FList validateInput(final String input,final PortalContext portal,final String... data)
                                                               throws EBufException, JAXBException {

    final Args args = Args.builder().elem("1").accountObj(input).build();
    final Args[] arguments = { args };

    final Results results = 
          Results.builder().serviceObj("").packageId("").poid("").planObj("").elem("0").build();

    final FList output =  searchUtil.executeTransactionSearchTemplate(
                                        ConstantsUtil.ACCOUNT_PLAN_SQL, arguments, results, portal);

    log.info("Account plan check output : {}", output);

    if (!(output.hasField(FldResults.getInst()))) {
      
      throw new BillingException("200102");
    } // End of result field check for deal code.
    return output;
  } // End of validateInput.
} // End of DealExistenseValidator.