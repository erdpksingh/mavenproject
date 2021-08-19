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

import com.cds.cbit.accounts.factory.TransactionValidationFactory;
import com.cds.cbit.accounts.interfaces.TransactionValidator;
import com.cds.cbit.accounts.util.JodaDateUtil;
import com.google.common.collect.ImmutableMap;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalContext;
import com.portal.pcm.fields.FldName;
import com.portal.pcm.fields.FldPackageId;
import com.portal.pcm.fields.FldPermitted;
import com.portal.pcm.fields.FldPoid;
import com.portal.pcm.fields.FldResults;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * .
 * 
 * @author  Venkata
 * @version 1.0.
*/
@Log4j2
@Component
public class AccountSubscriptionProcessor {
  
  @Autowired
  private TransactionValidationFactory factory;
  
  @Autowired
  private SubscriptionOpcodExecutor executor;
  
  /** .
 * @throws ParseException **/
  public Map<String, Object> 
           subscribeGivenDealInBrm(final AccountSubscriptionInput inputs) 
                                           throws EBufException, JAXBException, ParseException {
    final String dealCode = inputs.getDealCode();
    final Poid accountObj = inputs.getAccountObj();
    final PortalContext portal = inputs.getPortal();
    
    final Map<String, Object> inputMap = validateSubscriptionInput(dealCode, accountObj, portal);

    final FList dealOp = (FList) inputMap.get("dealOp");
    final List<FList> dealResults = dealOp.get(FldResults.getInst()).getValues();
    final FList dealServiceFlist = dealResults.get(0);
    final String dealServiceName = dealServiceFlist.get(FldPermitted.getInst());
    final String dealName = dealServiceFlist.get(FldName.getInst());

    final FList accountServiceOp = (FList) inputMap.get("services");

    final List<FList> accountServices = accountServiceOp.get(FldResults.getInst()).getValues();

    final Map<String, Object> subscriptionOutputMap = new ConcurrentHashMap<>();
    for (final FList accountService : accountServices) {

      final Poid accountServiceObj = accountService.get(FldPoid.getInst());
      final String accountServiceName = accountServiceObj.getType();
      if (accountServiceName.equals(dealServiceName) || dealServiceName.contains("account")) {

        subscriptionOutputMap.putAll(processSubscription(
              inputs,dealServiceFlist,accountObj,accountServiceObj,dealName,dealServiceName));
      }
      if (dealServiceName.contains("account")) {
        break;
      }
    } // End of looping through account services.
    return subscriptionOutputMap;
  } // End of subscribeDealsWithAdvancePayment method.
  
  /**
   * The method will provide the business logic to subscribe given input in BRM as part of 
   * post paid account creation.
   * 
   * @param: inputs           - @Mandatory - Subscription inputs.
   * @param dealServiceFlist  - @Mandatory - Deal services information.
   * @param accountObj        - @Mandatory - Account POID.
   * @param accountServiceObj - @Mandatory - Account services information.
   * @param dealName          - @Mandatory - Name of the deal.
   * @param dealServiceName   - @Mandatory - Name of deal service.
  */
  private Map<String, Object> processSubscription(
          final AccountSubscriptionInput inputs,final FList dealServiceFlist,
          final Poid accountObj,final Poid accountServiceObj,final String dealName,
            final String dealServiceName) throws EBufException, JAXBException, ParseException {
    
    final Map<String, Object> subscriptionOutputMap = new ConcurrentHashMap<>();
    final Poid dealPoid = dealServiceFlist.get(FldPoid.getInst());

    final SubscriptionFlistInput input = 
          SubscriptionFlistInput.builder().accountPoid(accountObj.toString())
              .dealPoid(dealPoid.toString()).dealServiceFlist(dealServiceFlist)
              .dealServiceName(dealServiceName).portal(inputs.getPortal())
              .dealName(dealName).serviceObj(accountServiceObj.toString()).flag(1).build();
    
    final Optional<String> startT = Optional.ofNullable(inputs.getStartDate());
    final Optional<String> endT = Optional.ofNullable(inputs.getEndDate());
    
    input.setAddOnFlag("false");
    if (startT.isPresent() || endT.isPresent()) {
      
      input.setAddOnFlag("true");
      Long endDiff = 0L;
      Long fromDiff = getFromDateDiff(startT.get());
      if (endT.isPresent()) {
        endDiff = getEndDateDiff(endT.get());
      }
      input.setStartT(fromDiff);
      input.setEndT(endDiff);
    }
   
    // Subscribing given deals in the BRM system.
    final String quantity = inputs.getQuantity();
    final String planPoid = inputs.getPlanPoid();
    final int status = inputs.getStatus();
    final FList subscriptionOp = executor.purchaseAddOnInBrm(input,quantity,status,planPoid);
    log.info(subscriptionOp);
    subscriptionOutputMap.put("flist", subscriptionOp);
    subscriptionOutputMap.put("dealName", dealName);
    return subscriptionOutputMap;
  } // End of processSubscription method.

  /**
   * The method will validate deal code, account services and plan as part of the account creation
   * subscription process.
   * 
   * @param: dealCode   :  @Mandatory - Deal to be purchased.
   * @param: accountObj :  @Mandatory - Account POID.
   * @param: portal     :  @Mandatory - BRM CM connection.
  */
  public Map<String, Object> validateSubscriptionInput(
          final String dealCode,final Poid accountObj, final PortalContext portal) 
                                                             throws EBufException, JAXBException {

    final TransactionValidator dealValidator = factory.getValidator("accountDealCheck");
    final FList dealOp = dealValidator.validateInput(dealCode, portal);

    final TransactionValidator accountServices = factory.getValidator("accountServicesCheck");
    final FList accountServiceOp = accountServices.validateInput(accountObj.toString(), portal);

    final TransactionValidator accountPlan = factory.getValidator("accountPlanValidator");
    final FList accountPlanOp = accountPlan.validateInput(accountObj.toString(), portal);
    final int packageId = 
          accountPlanOp.get(FldResults.getInst()).getValues().get(0).get(FldPackageId.getInst());

    return ImmutableMap.of("dealOp", dealOp, "services", accountServiceOp, "planPoid", packageId);
  } // End of validatedvancePaymentInput method.
  
  /** Method to get End date difference. */
  private Long getEndDateDiff(String endDate) throws ParseException {
    Long endDiff = null;
    if (endDate != "" && endDate != null) {
      endDiff = JodaDateUtil.findDifferenceWithCurrentDate(endDate);
    } else {
      endDiff = 0L;
    }
    return endDiff / 1000;
  } // End of getEndDateDiff method.

  /** Method to get from date difference . */
  private Long getFromDateDiff(String startDate) throws ParseException {

    Long fromDiff;
    Long fromAndCurrentdifference = JodaDateUtil.findDifferenceWithCurrentDate(startDate);
    if (new BigDecimal(fromAndCurrentdifference).compareTo(BigDecimal.ZERO) < 0) {
      fromDiff = 0L;
    } else {
      fromDiff = JodaDateUtil.findDifferenceWithCurrentDate(startDate);
    }
    return fromDiff / 1000;
  } // End of getFromDateDiff method.
}