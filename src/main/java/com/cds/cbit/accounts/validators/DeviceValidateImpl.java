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
import com.cds.cbit.accounts.commons.beans.Services;
import com.cds.cbit.accounts.interfaces.MultiArgValidation;
import com.cds.cbit.accounts.util.BillingInfoSearchUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

/**
 * The implementation component provides methods for validating given  device inputs in the system.
 * The component overrides BillingValidationInterface validateInput method to create and execute 
 * device search FList.If the validation is success then the method will return output FList else
 * it will throw an exception to the caller.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component("device")
public class DeviceValidateImpl implements MultiArgValidation {
  
  private final BillingInfoSearchUtil searchUtil;     // Util class to execute search opcode.
  
  public DeviceValidateImpl(final BillingInfoSearchUtil searchUtil) {
    
    this.searchUtil = searchUtil;
  } // End of constructor injection.
  
  /* @see com.circles.brm.api.v1.account.interfaces.BillingValidationInterface. */
  @Override
  public FList validateInput(final String input,final String...data) 
                                                               throws EBufException, JAXBException {
    
    final String template = 
                 "sim".equalsIgnoreCase(data[0]) ? ConstantsUtil.SIM_SQL : ConstantsUtil.MSISDN_SQL;
    
    return validateDevice(input,template);
  } // End of validateInput method.
  
  /**
   * The method will take device input like MSISDN,SIM and validate it in billing system.The method
   * creates Args and Results fields of search template and convert the search template to FList and
   * execute it in billing system with the help of BillingInfoSearchUtil.
   * 
   * @param: device   @Mandatory - MSISDN / SIM value .
   * @param: template @Mandatory - Search template [SQL Query of billing system].
  */
  public FList validateDevice(final String device,final String template) 
                                                               throws EBufException, JAXBException {
   
    final Args[] args = {Args.builder().elem("1").deviceId(device).build()};
    
    final Services services = Services.builder().elem("0")
                                                .accountObj(ConstantsUtil.ACCOUNT_POID).build();
    // Results field of search template.
    final Results results = Results.builder().elem("0").services(services).stateId("1").build();
    
    return searchUtil.executeSearchTemplate(template,args,results);
  } // End of validating devices like MSISDN or SIM in BRM.
} // End DeviceValidateImpl class which validate device inputs.