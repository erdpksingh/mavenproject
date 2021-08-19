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

import com.cds.cbit.accounts.api.customer.detail.payload.AccountInfo;
import com.cds.cbit.accounts.api.customer.detail.payload.CustomerDetailResponse;
import com.cds.cbit.accounts.api.customer.detail.payload.CustomerDetailsRequest;
import com.cds.cbit.accounts.api.customer.detail.payload.CustomerDetailsResponseBody;
import com.cds.cbit.accounts.api.customer.detail.payload.OrderInfo;
import com.cds.cbit.accounts.api.customer.detail.payload.PaymentInfo;
import com.cds.cbit.accounts.api.customer.detail.payload.PersonalInfo;
import com.cds.cbit.accounts.api.customer.detail.payload.PlanInfo;
import com.cds.cbit.accounts.api.customer.detail.payload.Result;
import com.cds.cbit.accounts.commons.beans.ResponseHeader;
import com.cds.cbit.accounts.commons.beans.ResultInfo;
import com.cds.cbit.accounts.factory.BillingValidationFactory;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.util.ResponseHeaderUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.fields.FldDeviceId;
import com.portal.pcm.fields.FldResults;

import java.text.ParseException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

/**
 * The component will help to prepare the response for customer detail API,
 * It will prepare all the details related to profile information,
 * order information, name information, address information product information ,
 * add on information of the customer from billing system .
 * 
 * @author Meghashree Udupa.
 * @version 1.0
 */
@Component
public class CustomerDetailResponser {

  private final BillingValidationFactory factory;
  private final CustomerDetailHelper responseHelper;

  /** Constructor Injection. */
  public CustomerDetailResponser(
         final BillingValidationFactory factory, final CustomerDetailHelper responseHelper) {
    
    this.responseHelper = responseHelper;
    this.factory = factory;
  } // End of Constructor injection.
  
  /**
   * The method will prepare the response for customer detail API.It will prepare all the details 
   * related to profile,order,name,address,product and add on information of the customer 
   * from billing system .
 * @throws ParseException 
   * 
   * @param- request
   * @param- responseDetails
   */
  public CustomerDetailResponse processCustomerDetailResponse(
         final CustomerDetailsRequest request,final Map<String, Object> responseDetails,
                                      final Poid accountPoid) throws EBufException,JAXBException {

    final PlanInfo planInfo = responseHelper.getPlanDetails(responseDetails, accountPoid);

    final Map<String, Object> infoMap = responseHelper.getProfileDetail(responseDetails);
    final PersonalInfo personalInfo = (PersonalInfo) infoMap.get("personalInfo");
    final PaymentInfo paymentInfo = (PaymentInfo) infoMap.get("paymentInfo");
    final OrderInfo orderInfo = (OrderInfo) infoMap.get("orderInfo");
    
    final BillingValidation accountDeviceValidator = factory.getValidator("accountDeviceValidator");
    final FList msisdnFlist = accountDeviceValidator.validateInput(String.valueOf(accountPoid));
    
    String msisdn = "";
    if (msisdnFlist.hasField(FldResults.getInst())) {
      msisdn = msisdnFlist.get(FldResults.getInst()).getValues().get(0).get(FldDeviceId.getInst());
    }
    
    final String autoBoost = personalInfo.getAutoBoost();
    final String category = personalInfo.getCategory();
    final String segment = personalInfo.getSegment();
    final String accountType = personalInfo.getAccountType();
    final String invoiceLangPref = personalInfo.getInvoiceLangPref();
    final String billingDate = personalInfo.getBillingDate();
    final String subType = personalInfo.getSubType();
    final boolean custConsent = personalInfo.getCustConsent();
    
    final Result result = 
          Result.builder().accountInfo(AccountInfo.builder()
                .accountNumber(request.getBody().getAccountNumber())
                .autoBoost(autoBoost).msisdn(msisdn).category(category).segment(segment)
                .accountType(accountType).invoiceLangPref(invoiceLangPref).billingDate(billingDate)
                .subType(subType).custConsent(custConsent).build()).personalInfo(personalInfo)
                .planInfo(planInfo).paymentInfo(paymentInfo).orderInfo(orderInfo).build();
    
    final ResultInfo resultInfo = 
          ResultInfo.builder().resultCode("SUCCESS").resultCodeId(0)
                                        .resultMsg("Cutomer details fetched successfully").build();
    final CustomerDetailsResponseBody body = 
          CustomerDetailsResponseBody.builder().result(result).resultInfo(resultInfo).build();
    
    final ResponseHeader header = ResponseHeaderUtil.createResponseHead(request.getHead());
    return CustomerDetailResponse.builder().body(body).head(header).build();
  } // End of processCustomerDetailResponse method.
} // End of CustomerDetailResponser class.