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
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.util.BillingInfoSearchUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.JAXBException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * The implementation component provides methods for validating given account association with the 
 * device in the system.The component overrides BillingValidationInterface validateInput method to
 * create and execute device search FList.If the validation is success then the method will return
 * output FList else it will throw an exception to the caller.
 * 
 * @author  Meghashree Udupa
 * @version 1.0.
*/
@Log4j2
@Component("accountDeviceValidator")
public class AccountDeviceValidator implements BillingValidation {

  private final BillingInfoSearchUtil searchUtil;

  public AccountDeviceValidator(final BillingInfoSearchUtil searchUtil) {
    this.searchUtil = searchUtil;
  } // End of Constructor Injection

  /*
   * @see com.cds.cbit.inventory.interfaces.BillingValidation#validateInput
  */
  @Override
  public FList validateInput(final String input,final String... data) 
                                                                throws EBufException,JAXBException {

    final Results results = Results.builder().elem("0").poid("").deviceId("").build();

    final List<Services> listServices = 
                         Arrays.asList(Services.builder().elem("0").accountObj("").build());

    final Args serviceArgs = Args.builder().elem("1").services(listServices).build();
    final Args poidArgs = Args.builder().elem("2").poid("").build();
    final Args accountPoid = Args.builder().elem("3").poid(input).build();
    final Args devicePoid = Args.builder().elem("4").poid("0.0.0.1 /device/num 1 0").build();

    final Args[] arguments = { serviceArgs, poidArgs, accountPoid, devicePoid };

    final FList output = 
                searchUtil.executeSearchTemplate(ConstantsUtil.ACCT_MSISDN, arguments, results);

    log.info("ACCOUNT SIM FLIST : {}", output);
    return output;
  } // End of validating MSISDN association with the given account.
} // End of AccountDeviceValidator