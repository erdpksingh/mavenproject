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
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.util.BillingInfoSearchUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.fields.FldResults;

import java.sql.Timestamp;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

/**
 * The implementation component provides methods for validating given base plan in the system.
 * The component overrides BillingValidationInterface validateInput method to create and execute 
 * base plan search FList.If the validation is success then the method will return output FList 
 * else it will throw an exception to the caller.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component("basePlan")
public class PlanValidateImpl implements BillingValidation {
  
  private final BillingInfoSearchUtil searchUtil;  // Util class to execute search opcode.

  public PlanValidateImpl(final BillingInfoSearchUtil searchUtil) {
    
    this.searchUtil = searchUtil;
  } // End of Constructor injection.
  
  /* @see com.circles.brm.api.v1.account.interfaces.BillingValidationInterface. */
  @Override
  public FList validateInput(final String basePlan,final String... data) 
                                                           throws EBufException, JAXBException {
    return validatePlan(basePlan,ConstantsUtil.PLAN_SQL);
  } // End of validateInput.
  
  /**
  * The method will take base plan input and validate it in billing system.The method creates 
  * Args and Results fields of search template and convert the search template to FList,execute
  * it in billing system with the help of BillingInfoSearchUtil.
  */
  public FList validatePlan(final String planName,final String template) 
                                                           throws EBufException, JAXBException {

    final Args[] args = {Args.builder().elem("1").name(planName).build()};

    // Results field of plan search template.
    final Results results = Results.builder().elem("0").poid("0.0.0.1 /plan -1 0")
                                                  .createdT(new Timestamp(0).getTime()).build();
    final FList planFlist = searchUtil.executeSearchTemplate(template,args,results);
    
    if (!(planFlist.hasField(FldResults.getInst()))) {
      throw new BillingException("100114");
    } // End of plan results check.
    return planFlist;
  } // End of validating customer base plan.
} // End of PlanValidateImpl class which validates given base plan.