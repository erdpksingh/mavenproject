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

package com.cds.cbit.accounts.api.customer.detail;

import com.cds.cbit.accounts.api.customer.detail.payload.CustomerDetailResponse;
import com.cds.cbit.accounts.api.customer.detail.payload.CustomerDetailsRequest;
import com.cds.cbit.accounts.factory.BillingValidationFactory;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.properties.AccountConfigProperties;
import com.google.common.collect.ImmutableMap;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.fields.FldAccountObj;
import com.portal.pcm.fields.FldResults;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

/**
 * The component will process the customer detail API workflow, which consists of validating
 * BRM inputs provided in the request, and fetching customer details from the BRM and finally
 * preparing the response. 
 * 
 * @author  Meghashree Udupa
 * @version 1.0.
*/
@Component
public class CustomerDetailProcessor {

  private final BillingValidationFactory factory;
  private final AccountConfigProperties properties;
  private final CustomerDetailResponser responser;

  /** Constructor Injection. */
  public CustomerDetailProcessor(final BillingValidationFactory factory, 
         final CustomerDetailResponser responser, final AccountConfigProperties properties) {
    
    this.factory = factory;
    this.properties = properties;
    this.responser = responser;
  } // End of Constructor injection.

  /** 
   * The method will fetch customer details from BRM for the requested account number.
   * 
   * @param: request - @Mandatory - GetCustomerDetails request.
  */
  public CustomerDetailResponse processCustomerDetails(final CustomerDetailsRequest request) 
                                                             throws EBufException, JAXBException {
    
    // Fetching account validation bean name as per the account type.
    final StringBuilder acctProfile = new StringBuilder(properties.getType()).append("Profile");
    
    final String accountNumber = request.getBody().getAccountNumber();
    
    // Validating given account number in BRM.
    final BillingValidation validator = factory.getValidator("accountValidator");
    final FList accountOp  = validator.validateInput(accountNumber,acctProfile.toString());
    final Poid accountPoid = 
               accountOp.get(FldResults.getInst()).getValues().get(0).get(FldAccountObj.getInst());
    
    // Fetching customer other information like profile, products and nameInfo with accountPoid.
    final Map<String,Object> inputFlists = new ConcurrentHashMap<>();
    inputFlists.putAll(getCustomerInformation(accountNumber,acctProfile,accountPoid));
    
    return responser.processCustomerDetailResponse(request,inputFlists,accountPoid);
  } // End of method processCustomerDetails.
  
  /**
   * The method will validate and fetch customer profile,deals,name information with the given 
   * account information.
   * 
   * @param: accountNumber - @Mandatory - Customer account number.
   * @param: acctProfile   - @Mandatory - Account type.
   * @param: accountPoid   - @Mandatory - Account POID.
  */
  private ImmutableMap<String, FList> getCustomerInformation(
          final String accountNumber,final StringBuilder acctProfile,final Poid accountPoid) 
                                                             throws EBufException, JAXBException {
    
    final BillingValidation profileValidator  = factory.getValidator("profileDetailsValidator");
    final FList profileOp = profileValidator.validateInput(accountNumber,acctProfile.toString());
    
    final BillingValidation nameInfoValidator = factory.getValidator("nameValidator");
    final FList nameInfoOp = nameInfoValidator.validateInput(accountPoid.toString());
    
    final BillingValidation addonValidator  = factory.getValidator("addonValidator");
    final FList addOnDetailOp = addonValidator.validateInput(String.valueOf(accountPoid));
    
    return ImmutableMap.of(
               "profileDetails",profileOp,"nameDetail",nameInfoOp,"productDetail",addOnDetailOp);
  } // End of getCustomerInformation method.
} // End of class CustomerDetailProcessor, which process customer detail workflow.