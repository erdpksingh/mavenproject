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

package com.cds.cbit.accounts.api.account.subscriptions;

import com.cds.cbit.accounts.api.account.payload.AccountRequestBody;
import com.cds.cbit.accounts.api.account.payload.AdvancePayments;
import com.cds.cbit.accounts.commons.RequetBeanValidator;
import com.cds.cbit.accounts.util.UtcDateUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalContext;
import com.portal.pcm.fields.FldAccountNo;
import com.portal.pcm.fields.FldBalImpacts;
import com.portal.pcm.fields.FldItemObj;
import com.portal.pcm.fields.FldPoid;
import com.portal.pcm.fields.FldResourceId;
import com.portal.pcm.fields.FldResults;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;

/**
 * The component will perform the advance payment posting against the given deal subscription
 * in BRM when advance payment option available in the account creation request.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component
public class OnDemandSubscriptionProcessor {

  private final RequetBeanValidator validator;
  private final AccountSubscriptionProcessor subscribeProcessor;
  private final AdvancePaymentProcessor advancePayment;
  
  /** Constructor injection. **/
  public OnDemandSubscriptionProcessor(final AccountSubscriptionProcessor subscribeProcessor,
         final RequetBeanValidator validator,final AdvancePaymentProcessor advancePayment) {
    
    this.validator = validator;
    this.subscribeProcessor = subscribeProcessor;
    this.advancePayment = advancePayment;
  } // End of constructor injection.
  
  /**
   * The method verifies whether advance payments section available in the request or not.If it is
   * available then it will go ahead and purchase the deals and post the payment given for the deal
   * 
   * @param: requestBody - @Mandatory - Account creation request body.
   * @param: accountNo   - @Mandatory - Account number created for the request.
  */
  public void processOnDemandSubscription(
         final AccountRequestBody requestBody,final Poid accountObj,final PortalContext portal)
                                           throws EBufException,JAXBException, ParseException {
    final Optional<List<AdvancePayments>> advancePaymentSection = 
                                          Optional.ofNullable(requestBody.getAdvancePayments());
    if (advancePaymentSection.isPresent()) {
      log.info("Processing advance payment....");
      for (final AdvancePayments advancePay : advancePaymentSection.get()) {
        
        validator.validateRequestBean(advancePay);
        subscribeDealsWithAdvancePayment(advancePay,accountObj,portal);
        
      } // End of looping through advance payments.
    } // End of advance payment availability check in request.
  } // End of processAdvancePayment method.
  
  private void subscribeDealsWithAdvancePayment(
          final AdvancePayments paymentAddOns,final Poid accountObj,final PortalContext portal) 
                                            throws EBufException, JAXBException, ParseException {
    final AccountSubscriptionInput subscribeInput =
        AccountSubscriptionInput.builder().accountObj(accountObj)
                          .dealCode(paymentAddOns.getDealCode()).status(1).portal(portal).build();
  
    final Map<String,Object> subscriptionOp = 
        subscribeProcessor.subscribeGivenDealInBrm(subscribeInput);
    
    final String dealName = (String) subscriptionOp.get("dealName");
    final FList subscriptionFlist = (FList) subscriptionOp.get("flist");
    
    // Posting advance payment against the deals in BRM.
    final AdvancePaymentFlistInput advancePayInput = createAdvancePaymentInputs(
                                   paymentAddOns,subscriptionFlist,accountObj,dealName,portal);
    advancePayment.processAdvancePayment(advancePayInput,portal);
  }
  
  /**
   * The method will retrieve subscription item poid from the subscription output FList for the
   * resource id 36.
   * 
   * @param: subscriptionFlist - @Mandatory - Subscription output Flist.
  */
  private AdvancePaymentFlistInput createAdvancePaymentInputs(
          final AdvancePayments paymentAddOns,final FList subscriptionFlist,final Poid accountObj,
          final String dealName,final PortalContext portal) throws EBufException {
    
    final StringBuilder description = new StringBuilder(dealName).append(':')
                                          .append(paymentAddOns.getDealCode())
                                          .append(":ONDEMANDPAYMENT");
    BigDecimal amount = new BigDecimal(0);
    if (paymentAddOns.getAmount() != null) {
      amount = new BigDecimal(paymentAddOns.getAmount());
    }
    log.info("Processing advance payment with amount : {}",amount);
    final AdvancePaymentFlistInput input =
          AdvancePaymentFlistInput.builder()
              .amount(amount).description(description.toString())
              .paymentDate(UtcDateUtil.currentDateToTimeStamp()).paymentType("CC")
              .accountNumber(getAccountNumber(accountObj,portal))
              .accountObj(String.valueOf(accountObj))
              .build();
    
    for (final FList subscription : subscriptionFlist.get(FldResults.getInst()).getValues()) {

      if (subscription.hasField(FldBalImpacts.getInst())) {
        
        // Looping through balance impacts of subscription output.
        for (final FList balImpacts : subscription.get(FldBalImpacts.getInst()).getValues()) {
          
          if (balImpacts.get(FldResourceId.getInst()) == 36) {
            
            input.setSubscriptionItemPoid(balImpacts.get(FldItemObj.getInst()).toString());
          } // End of resource id 36 check.
        } // End of looping through balImpacts section of subscription output.
      } // End of result check for balImpacts.
    } // End of looping through subscription output FList.
    return input;
  } // End of getSubscriptionItemPoid method.
  
  private String getAccountNumber(final Poid accountPoid,final PortalContext portal) 
                                                                             throws EBufException {
    final FList accountNumberList = new FList();
    accountNumberList.set(FldPoid.getInst(), accountPoid);
    final FList accountDetails = portal.opcode(3, accountNumberList);
    return accountDetails.get(FldAccountNo.getInst());
  } // End of getting account number from account poid.
} // End of AdvancePaymentProcessor.