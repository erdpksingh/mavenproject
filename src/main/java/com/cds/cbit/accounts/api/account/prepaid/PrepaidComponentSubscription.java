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

import com.cds.cbit.accounts.api.account.flist.DealInfo;
import com.cds.cbit.accounts.api.account.flist.ProductsOrDiscounts;
import com.cds.cbit.accounts.api.account.flist.SubscriptionPurchaseDeal;
import com.cds.cbit.accounts.api.account.subscriptions.SubscriptionFlistInput;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalOp;
import com.portal.pcm.fields.FldDiscounts;
import com.portal.pcm.fields.FldProducts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will provide methods to execute prepaid component deal subscription in BRM.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component
public class PrepaidComponentSubscription {
  
  @Autowired
  private BrmServiceUtil brmUtil;
  
  /**
   * The method will subscribe given components in BRM with given information as part of prepaid
   * account creation process.
   * 
   * @param: input      - @Mandatory - Subscription related input information.
   * @param: planType   - @Mandatory - Type of plan firstUsage / normalPlan.
   * @param: packageId  - @Mandatory - Base plan packageId.
   * @param: planId     - @Mandatory - Base plan POID
  */
  protected FList subscribePrepaidComponentsInBrm(
            final SubscriptionFlistInput input,final String planType,final Poid planPoid,
                                       final int packageId) throws EBufException, JAXBException {
    
    final FList dealResult = input.getDealServiceFlist(); // component deal information FList.
    
    final DealInfo dealInfo = 
          DealInfo.builder().name(input.getDealName()).poid(input.getDealPoid()).startT(0L)
            .endT(0L).descr(input.getDealName()).flags("0").planObj(planPoid.toString()).build();
    
    final Optional<Long> endT = Optional.ofNullable(input.getEndT());
    if (endT.isPresent()) {
      dealInfo.setEndT(endT.get());
    }
    // If products available in the component deal add them to subscription.
    if (dealResult.containsKey(FldProducts.getInst())) {
      dealInfo.setProducts(updateDealProductOrDiscount(
                           dealResult.get(FldProducts.getInst()).getValues(),planType, packageId));
    }
    
    // If discounts available in the component deal add them to subscription.
    if (dealResult.containsKey(FldDiscounts.getInst())) {
      dealInfo.setDiscounts(updateDealProductOrDiscount(
                            dealResult.get(FldDiscounts.getInst()).getValues(),planType,packageId));
    }
    final SubscriptionPurchaseDeal subscription = 
          SubscriptionPurchaseDeal.builder().poid(input.getAccountPoid())
                                  .programName("Account Creation API").dealInfo(dealInfo).build();
    
    // If the deal service is not of account service type, add it to subscription.
    if (!input.getDealServiceName().contains("account")) {
      subscription.setServiceObj(input.getServiceObj());
    }
    final JAXBContext jaxbContext = JAXBContext.newInstance(SubscriptionPurchaseDeal.class);
    final FList subscribe =  brmUtil.getFListFromPojo(jaxbContext,subscription);
    log.info(subscribe);
    return input.getPortal().opcode(PortalOp.SUBSCRIPTION_PURCHASE_DEAL, subscribe);
  } // End of subscribePrepaidComponentsInBrm method.
  
  /**
   * The method will update product or discount sparser of deal with status & packageId information.
   * 
   * @param: dealElements - @Mandatory - Deal products / discounts.
   * @param: planType     - @Mandatory - Type of plan firstUsage / normalPlan.
   * @param: packageId    - @Mandatory - Base plan packageId.
  */
  protected List<ProductsOrDiscounts> updateDealProductOrDiscount(
                         final List<FList> dealElements,final String planType,final int packageId) {

    final List<ProductsOrDiscounts> dealElementList = new ArrayList<>();
    final String status = "normalPlan".equals(planType) ? "1" : "2";
    int i = 0;
    
    // Looping through deal elements [products / discounts] to add packageId and status.
    for (final FList element : dealElements) {
      
      final ProductsOrDiscounts productOrDiscount = 
            (ProductsOrDiscounts) brmUtil.getPojoFromFList(element, ProductsOrDiscounts.class);
      
      productOrDiscount.setElem(String.valueOf(i));
      productOrDiscount.setPackageIdNumber(String.valueOf(packageId));
      productOrDiscount.setStatus(status);
      dealElementList.add(productOrDiscount);
      i++;
    } // End of looping through deal products or discounts.
    return dealElementList;
  } // End of createServicesInfo method.
} // End of PrepaidComponentSubscription.