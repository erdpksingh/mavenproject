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

import com.cds.cbit.accounts.api.account.flist.DealInfo;
import com.cds.cbit.accounts.api.account.flist.ProductsOrDiscounts;
import com.cds.cbit.accounts.api.account.flist.SubscriptionPurchaseDeal;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.PortalOp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will perform the advance payment posting against the given deal subscription
 * in BRM when advance payment option available in the account creation request.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
 * 
 * @author  Meghashree udupa.
 * @version 2.0.
*/
@Log4j2
@Component
public class SubscriptionOpcodExecutor {
  
  @Autowired
  private BrmServiceUtil brmUtil;
  
  private final SubscriptionSparserCreator sparser;
  
  public SubscriptionOpcodExecutor(final SubscriptionSparserCreator sparser) {
    
    this.sparser = sparser;
  } // End of constructor injection.
  
  protected FList purchaseAddOnInBrm(final SubscriptionFlistInput input,final String quantity,
                           int status,final String planPoid) throws EBufException, JAXBException {
    final Timestamp stamp = new Timestamp(0);
    final Date date = new Date(stamp.getTime());
    
    final Optional<List<ProductsOrDiscounts>> products = 
          Optional.ofNullable(sparser.createPaymentProductSparser(input
                                                                   .getDealServiceFlist(),input));
    
    final Optional<List<ProductsOrDiscounts>> discounts = 
          Optional.ofNullable(sparser.createPaymentDiscountSparser(input
                                                                   .getDealServiceFlist(),input));
    
    final DealInfo dealInfo =
          DealInfo.builder().name(input.getDealName()).poid(input.getDealPoid())
                  .startT(date.getTime()).endT(date.getTime()).descr(input.getDealName()).build();
    if (input.getFlag() == 1) {
      dealInfo.setFlags("4194304");
    }
    
    products.ifPresent(p -> { 
      addAdditionalInputs(p,planPoid,quantity,status);
      dealInfo.setProducts(p);
    });
    discounts.ifPresent(d -> {
      addAdditionalInputs(d,planPoid,quantity,status);
      dealInfo.setDiscounts(d);
    });
    final SubscriptionPurchaseDeal subscription = 
          SubscriptionPurchaseDeal.builder().poid(input.getAccountPoid())
                                  .programName(input.getDealName()).dealInfo(dealInfo).build();
    if (!input.getDealServiceName().contains("account")) {
      subscription.setServiceObj(input.getServiceObj());
    }
    final JAXBContext jaxbContext = JAXBContext.newInstance(SubscriptionPurchaseDeal.class);
    final FList subscribe =  brmUtil.getFListFromPojo(jaxbContext,subscription);
    log.info(subscribe);
    return input.getPortal().opcode(PortalOp.SUBSCRIPTION_PURCHASE_DEAL, subscribe);
  } // End of subscribeDealsWithAdvancePayment method.
  
  private List<ProductsOrDiscounts> addAdditionalInputs(
          final List<ProductsOrDiscounts> sparser,final String planPoid,final String quantity,
                                                                               final int status) {
    
    final Optional<String> packagePoid = Optional.ofNullable(planPoid);
    
    if (quantity != null && !quantity.isEmpty()) {
      sparser.forEach(pd -> {
        pd.setQuantity(new BigDecimal(quantity));
        pd.setStatus(String.valueOf(status));
      });
    } // End of adding quantity to product / discount sparse.
    if (packagePoid.isPresent()) {
      sparser.forEach(pd -> {
        pd.setPackageId(packagePoid.get());
        pd.setStatus(String.valueOf(status));
      });
    } // End of adding packageId to product / discount sparse.
    return sparser;
  } // End of addAdditionalInputs method.
} // End of AdvancePaymentProcessor.