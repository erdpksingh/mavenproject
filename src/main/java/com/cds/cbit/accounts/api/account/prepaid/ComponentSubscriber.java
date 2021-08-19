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

package com.cds.cbit.accounts.api.account.prepaid;

import com.cds.cbit.accounts.api.account.payload.Components;
import com.cds.cbit.accounts.api.account.payload.PlanInfo;
import com.cds.cbit.accounts.api.account.subscriptions.AccountSubscriptionProcessor;
import com.cds.cbit.accounts.api.account.subscriptions.SubscriptionFlistInput;
import com.cds.cbit.accounts.factory.TransactionValidationFactory;
import com.cds.cbit.accounts.interfaces.TransactionValidator;
import com.cds.cbit.accounts.util.UtcDateUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalContext;
import com.portal.pcm.fields.FldName;
import com.portal.pcm.fields.FldPackageId;
import com.portal.pcm.fields.FldPermitted;
import com.portal.pcm.fields.FldPoid;
import com.portal.pcm.fields.FldResults;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will provide methods to subscribe components as part of prepaid account creation.
 * 
 * @author Venkata Nagaraju.
 * @version 1.0.
 */
@Log4j2
@Component
public class ComponentSubscriber {

  @Autowired
  private TransactionValidationFactory factory;

  
  private final AccountSubscriptionProcessor processor;
  private final PrepaidComponentSubscription subscriber;
  
  /** Constructor injection. **/
  public ComponentSubscriber(
       final AccountSubscriptionProcessor processor,final PrepaidComponentSubscription subscriber) {
    
    this.processor = processor;
    this.subscriber = subscriber;
  } // End of constructor injection.

  /**
   * The method will process component subscription as part of prepaid account creation process.
   * 
   * @param: planFlist        - @Mandatory - Base plan information.
   * @param: planType         - @Mandatory - Type of plan firstUsage / normalPlan.
   * @param: planInfo         - @Mandatory - PlanInfo section of prepaid account request.
   * @param: accountObj       - @Mandatory - Account POID.
   * @param: portal           - @Mandatory - BRM CM connection.
  */
  public void subscribeComponents(
         final PlanInfo planInfo,final Poid accountObj,final String planType,final FList planFlist,
         final String endT,final PortalContext portal)
                                               throws EBufException, JAXBException, ParseException {

    // Fetching package information from the given account poid.
    final TransactionValidator dealValidator = factory.getValidator("packageIdValidator");
    final FList packageOp = dealValidator.validateInput(accountObj.toString(), portal);
    final int packageId = 
              packageOp.get(FldResults.getInst()).getValues().get(0).get(FldPackageId.getInst());
    
    // Retrieving plan poid from the plan information FList.
    final Poid  planPoid = 
                planFlist.get(FldResults.getInst()).getValues().get(0).get(FldPoid.getInst());

    final Optional<List<Components>> components = Optional.ofNullable(planInfo.getComponents());
    
    if (components.isPresent()) {   // If components available proceed further.
      
      // Looping through all components.
      for (final Components component : components.get()) {
        
        final Map<String, Object> inputs = 
              processor.validateSubscriptionInput(component.getDealCode(), accountObj, portal);
        final FList dealOp = (FList) inputs.get("dealOp");
        final FList accountServiceOp = (FList) inputs.get("services");
        
        final ComponentInput componentInputs = 
              ComponentInput.builder().dealOp(dealOp).planType(planType).packageId(packageId)
                            .accountObj(accountObj.toString()).planPoid(planPoid)
                            .accountServiceOp(accountServiceOp).portal(portal).endT(endT).build();
        processComponents(componentInputs);
      } // End of looping through components.
    } // End of component availability check.
  } // End of subscribeComponents method.

  /**
   * The method will process component subscription logic to purchase them in BRM as part of prepaid
   * account creation process.
   * 
   * @param: dealFlist        - @Mandatory - Component deal information.
   * @param: planType         - @Mandatory - Type of plan firstUsage / normalPlan.
   * @param: packageId        - @Mandatory - Base plan packageId.
   * @param: accountObj       - @Mandatory - Account POID.
   * @param: planPoid         - @Mandatory - Base plan POID.
   * @param: accountServiceOp - @Mandatory - Account service information.
   * @param: portal           - @Mandatory - BRM CM connection.
  */
  private void processComponents(final ComponentInput componentInputs) 
                                          throws EBufException, JAXBException, ParseException {

    final FList dealFlist = componentInputs.getDealOp();
    final FList dealResult = dealFlist.get(FldResults.getInst()).getValues().get(0);
    final String dealServiceName = dealResult.get(FldPermitted.getInst());
    final String dealName = dealResult.get(FldName.getInst());
    final Poid dealPoid = dealResult.get(FldPoid.getInst());

    final String planType = componentInputs.getPlanType();
    final Poid planPoid = componentInputs.getPlanPoid();
    final int packageId = componentInputs.getPackageId();
    final String accountObj = componentInputs.getAccountObj();
    final PortalContext portal = componentInputs.getPortal();
    final String endT = componentInputs.getEndT();
    
    final FList accountServiceOp = componentInputs.getAccountServiceOp();
    final List<FList> accountServices = accountServiceOp.get(FldResults.getInst()).getValues();

    // Looping through account services to compare deal service name with account service name.
    // If a match found then subscribe the matched service in BRM.
    for (final FList accountService : accountServices) {

      final Poid accountServiceObj = accountService.get(FldPoid.getInst());
      final String accountServiceName = accountServiceObj.getType();
      
      if (accountServiceName.equals(dealServiceName) || dealServiceName.contains("account")) {

        final SubscriptionFlistInput input = 
              SubscriptionFlistInput.builder().accountPoid(accountObj).dealPoid(dealPoid.toString())
                       .dealServiceFlist(dealResult).dealServiceName(dealServiceName).portal(portal)
                               .dealName(dealName).serviceObj(accountServiceObj.toString()).build();

        if (endT.length() > 0) {
          input.setEndT(UtcDateUtil.convertStringToTimeStamp(endT));
        }
        // Subscribing given deals in the BRM system.
        final FList subscriptionOp = 
                    subscriber.subscribePrepaidComponentsInBrm(input, planType,planPoid, packageId);
        log.info(subscriptionOp);
      } // End of account and deal service check.
      
      if (dealServiceName.contains("account")) {
        break;
      } // End if deal service name is of type account.
    } // End of looping through account services.
  } // End of processComponents method.
} // End of AccountUpdateProcessor, which updates account with base plan for prepaid account.