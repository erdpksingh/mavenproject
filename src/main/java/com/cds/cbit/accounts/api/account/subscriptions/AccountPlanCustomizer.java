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

import com.cds.cbit.accounts.api.account.flist.CommitCustOpcode;
import com.cds.cbit.accounts.api.account.payload.AccountRequestBody;
import com.cds.cbit.accounts.api.account.payload.Addons;
import com.cds.cbit.accounts.api.account.payload.Components;
import com.cds.cbit.accounts.commons.RequetBeanValidator;
import com.cds.cbit.accounts.exceptions.BillingException;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.cds.cbit.accounts.util.JodaDateUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalContext;
import com.portal.pcm.fields.FldAccountObj;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component first execute account creation opcode, and then it provide the workflow for account
 * plan customization, in case of user provides additional bundle purchase or advance payments as
 * part  of account creation.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0
 * 
 * @author  Meghashree udupa.
 * @version 2.0
*/
@Log4j2
@Component
public class AccountPlanCustomizer {
  
  @Autowired
  private OnDemandSubscriptionProcessor onDemandPurchase;
  
  @Autowired
  private AccountSubscriptionProcessor subscribeProcessor;
  
  @Autowired
  private RequetBeanValidator validator; 
  
  @Autowired
  private BrmServiceUtil brmUtil;
  
  /**
   * The method will perform account creation in BRM and if it is successful, then the method will
   * proceed with plan customization workflow if additional input like advance payment, addOn or
   * component purchase in the request.
   * 
   * @param: reqBody    - @Mandatory - Account creation request "body" section.
   * @param: custCommit - @Mandatory - Account creation input FList.
   * @param: portal     - @Mandatory - BRM CM connection.
  */
  public FList processPlanCustomization(
         final AccountRequestBody reqBody,final CommitCustOpcode custCommit,
               final Poid planPoid,final PortalContext portal) throws EBufException, 
                                                                    JAXBException, ParseException {
    
    final JAXBContext jaxbContext = JAXBContext.newInstance(CommitCustOpcode.class);
    final FList input =  brmUtil.getFListFromPojo(jaxbContext, custCommit);
    
    log.info(input);
    final FList accountOp = portal.opcode(63, input);
    final Poid accountObj = accountOp.get(FldAccountObj.getInst());

    log.info("Account created successfully...");
    onDemandPurchase.processOnDemandSubscription(reqBody, accountObj, portal);
    subscribeAddOnDeals(reqBody,accountObj,portal);
    subscribeComponents(reqBody,accountObj,planPoid,portal);
    
    return accountOp;
  } // End of processPlanCustomization method.
  
  private void subscribeAddOnDeals(
               final AccountRequestBody reqBody, final Poid accountObj, final PortalContext portal)
                                             throws  EBufException, JAXBException, ParseException  {
    final Optional<List<Addons>> addOnDeals = 
                                            Optional.ofNullable(reqBody.getPlanInfo().getAddons());
    if (addOnDeals.isPresent()) {
      for (final Addons addOn: addOnDeals.get()) {
        validator.validateRequestBean(addOn);
        
        final String startDate =  addOn.getStartDate();
        if (addOn.getEndDate() != null && !addOn.getEndDate().isEmpty()) {
          final Long difference  =  JodaDateUtil.calculateDatesDifference(startDate,
                                                                                addOn.getEndDate());
          if (difference < 0) {
            throw new BillingException("200402");
          } // End of start and end date check.
        }
        final AccountSubscriptionInput subscribeInput =
              AccountSubscriptionInput.builder()
                   .accountObj(accountObj).quantity(addOn.getQuantity()).planPoid("")
                   .startDate(addOn.getStartDate()).endDate(addOn.getEndDate())
                   .dealCode(addOn.getProduct()).status(2).portal(portal).build();
        
        subscribeProcessor.subscribeGivenDealInBrm(subscribeInput);
      } // End of looping through addOns.
    } // End of addOn deal section of account creation request.
  } // End of subscribeAddOnDeals method.
  
  private void subscribeComponents(
          final AccountRequestBody reqBody,final Poid accountObj,final Poid planPoid,
                                   final PortalContext portal) throws EBufException,
                                                                JAXBException, ParseException {
    
    final Optional<List<Components>> components = 
                                     Optional.ofNullable(reqBody.getPlanInfo().getComponents());
    if (components.isPresent()) {
      for (final Components component : components.get()) {
        
        validator.validateRequestBean(component);
        final AccountSubscriptionInput subscribeInput =
            AccountSubscriptionInput.builder()
                        .accountObj(accountObj).quantity("").planPoid(String.valueOf(planPoid))
                           .dealCode(component.getDealCode()).status(2).portal(portal).build();
      
        subscribeProcessor.subscribeGivenDealInBrm(subscribeInput);
      } // End of looping through components.
    } // End of component deal section of account creation request.
  } // End of subscribeComponents method.
} // End of AccountPlanCustomizer.