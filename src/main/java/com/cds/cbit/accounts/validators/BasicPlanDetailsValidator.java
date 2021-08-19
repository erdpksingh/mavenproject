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
 * The implementation component provides methods for validating account for BASE PLAN purchase 
 * in the system.The component overrides BillingValidationInterface validateInput method to
 * create and execute base plan search FList.If the validation is success then the method will
 * return output FList else it will throw an exception to the caller.
 * 
 * @author  Meghashree Udupa
 * @version 1.0.
*/
@Component("basicPlanValidator")
public class BasicPlanDetailsValidator implements BillingValidation {

  private final BillingInfoSearchUtil searchUtil;

  public BasicPlanDetailsValidator(final BillingInfoSearchUtil searchUtil) {
    
    this.searchUtil = searchUtil;
  } // End of constructor injection.

  @Override
  public FList validateInput(final String input,final String... data) 
                                                                throws EBufException,JAXBException {
    final Results results = 
          Results.builder().elem("1").quantity("0").packageId("0").purchaseEndT(0L)
                 .purchaseStartT(0L).planObj("").createdT(0L)
                 .linkedObj(LinkedObj.builder().elem("2").linkDirection("-1").planObj("")
                 .extraResults(ExtraResults.builder().elem("0").build()).build()).build();

    final Args planObj     = Args.builder().elem("1").planObj("").build();
    final Args poidArgs    = Args.builder().elem("2").poid("").build();
    final Args accountObj  = Args.builder().elem("3").accountObj(input).build();
    final Args acctNoArgs  = Args.builder().elem("4").status("3").build();
    final Args createdArgs = Args.builder().elem("5").createdT(0L).build();
    final Args serviceArg  = 
          Args.builder().elem("6").serviceObj("0.0.0.1 /service/telco/gsm/telephony -1").build();

    final Args[] arguments = { planObj, poidArgs, accountObj, acctNoArgs, createdArgs, serviceArg };

    return searchUtil.executeSearchTemplate(ConstantsUtil.BASEPLAN_SQL, arguments, results, "1280");
  } // End of validateInput method.
} // End of BasicPlanDetailsValidator class.