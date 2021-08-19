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

package com.cds.cbit.accounts.api.account.balance;

import com.cds.cbit.accounts.factory.BillingValidationFactory;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.google.common.collect.ImmutableMap;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.fields.FldResults;
import com.portal.pcm.fields.FldStatus;

import java.util.Map;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component provide methods to retrieve customer information other than balances for the API
 * GetCustomerBalances. The component will use "BillingValidationFactory" for validating the input
 * like packages and services associated with the account.
 * 
 * @author Venkata Nagaraju
 * @version 1.0.
*/
@Component
public class CustomerDetailsRetriever {

  @Autowired
  private BillingValidationFactory factory;

  /**
   * The method will fetch package information for the given account POID from BRM.
   * 
   * @param: accountObj - @Mandatory - Account POID of requested account number.
  */
  protected FList fetchPackagedata(final String accountObj) throws EBufException, JAXBException {

    final BillingValidation validator = factory.getValidator("packageFetchValidator");
    return validator.validateInput(accountObj);
  } // End of getBalanacesWithoutMsisdn method.

  /**
   * The method will fetch service status information for the given account POID from BRM.
   * 
   * @param: accountObj - @Mandatory - Account POID of requested account number.
  */
  protected String fetchAccountServicesStatus(final String  accountObj)
                                                            throws EBufException, JAXBException {
    final BillingValidation validator = factory.getValidator("accountServices");
    final FList serviceOp = validator.validateInput(accountObj);
    final int status = serviceOp.get(FldResults.getInst()).getValues().get(0)
                                                          .get(FldStatus.getInst());
    final Map<Integer,Integer> statusMap = ImmutableMap.of(10100,1,10102,3,10103,4);
    return String.valueOf(statusMap.get(status));
  } // End of fetchAccountServicesStatus method.
} // End of CustomerOtherDetailProcessor, which fetch customer details other than balances.