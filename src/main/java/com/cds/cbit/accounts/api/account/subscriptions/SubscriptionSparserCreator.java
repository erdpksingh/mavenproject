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

import com.cds.cbit.accounts.api.account.flist.ProductsOrDiscounts;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.fields.FldCycleEndDetails;
import com.portal.pcm.fields.FldDiscounts;
import com.portal.pcm.fields.FldProducts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component create product or discount sparses as per the given inputs for subscription.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0
 * 
 * @author  Meghashree Udupa.
 * @version 2.0
*/
@Component
public class SubscriptionSparserCreator {
  
  @Autowired
  private BrmServiceUtil brmUtil;
  
  private ProductsOrDiscounts getProductsOrDiscounts(
      final FList productOrDiscountFlist,SubscriptionFlistInput input) throws EBufException {

    final int cycleEndDetailsId = 
          (int) productOrDiscountFlist.get(FldCycleEndDetails.getInst());
    final FList dealElementFlist = productOrDiscountFlist;
    final ProductsOrDiscounts productOrDiscount = 
         (ProductsOrDiscounts) brmUtil.getPojoFromFList(dealElementFlist,ProductsOrDiscounts.class);
    if (cycleEndDetailsId == 6148) {
      productOrDiscount.setPurchaseEndUnit("8");
      productOrDiscount.setPurchaseEndOffset("1");
      productOrDiscount.setCycleEndUnit("8");
      productOrDiscount.setCycleEndOffset("1");
      productOrDiscount.setUsageEndUnit("8");
      productOrDiscount.setUsageEndOffset("1");
      return productOrDiscount;
    } else if (input.getAddOnFlag().equals("true")) {
      productOrDiscount.setPurchaseStartT(input.getStartT());
      productOrDiscount.setCycleStartT(input.getStartT());
      productOrDiscount.setUsageStartT(input.getStartT());
      productOrDiscount.setPurchaseEndT(input.getEndT());
      productOrDiscount.setCycleEndT(input.getEndT());
      productOrDiscount.setUsageEndT(input.getEndT());
      return productOrDiscount;
    } else {
      return null;
    } // End of cycle end details check.
  } // End of getProductsOrDiscounts method.
  
  /**
   * The method will add cycle units and offsets to products of a deal if products are available.
   * 
   * @param: dealServiceFlist - Deal services information.
  */
  protected List<ProductsOrDiscounts> createPaymentProductSparser(final FList dealServiceFlist,
                                               SubscriptionFlistInput input) throws EBufException {
    final List<ProductsOrDiscounts> productList = new ArrayList<>();
    
    if (dealServiceFlist.containsKey(FldProducts.getInst())) {
      
      for (int i = 0;i < dealServiceFlist.get(FldProducts.getInst()).size();i++) {
        final FList products = dealServiceFlist.get(FldProducts.getInst()).getValues().get(i);

        final Optional<ProductsOrDiscounts> dealElement = 
                                     Optional.ofNullable(getProductsOrDiscounts(products,input));
        
        if (dealElement.isPresent()) {
          final ProductsOrDiscounts productOrDiscount = dealElement.get();
          productOrDiscount.setElem(String.valueOf(i));
          productList.add(productOrDiscount);
        } else {
          ProductsOrDiscounts productsOrDiscounts = (ProductsOrDiscounts) 
                                       brmUtil.getPojoFromFList(products,ProductsOrDiscounts.class);
          productsOrDiscounts.setElem(String.valueOf(i));
          productList.add(productsOrDiscounts);
        } // End of cycle end details check.
      }
    } // End of products existence check for given deal.
    return productList;
  } // End of createPaymentProductSparser method.
  
  /**
   * The method will add cycle units and offsets to discounts of a deal if discounts are available.
   * 
   * @param: dealServiceFlist - Deal services information.
  */
  protected List<ProductsOrDiscounts> createPaymentDiscountSparser(final FList dealServiceFlist,
                                                SubscriptionFlistInput input) throws EBufException {
    final List<ProductsOrDiscounts> discountList = new ArrayList<>();
    if (dealServiceFlist.containsKey(FldDiscounts.getInst())) {
      for (int i = 0;i < dealServiceFlist.get(FldDiscounts.getInst()).size();i++) {
        final FList discounts = dealServiceFlist.get(FldDiscounts.getInst()).getValues().get(i);
        final Optional<ProductsOrDiscounts> dealElement = Optional.ofNullable(
                                                           getProductsOrDiscounts(discounts,input));
        if (dealElement.isPresent()) {
          final ProductsOrDiscounts productOrDiscount = dealElement.get();
          productOrDiscount.setElem(String.valueOf(i));
          discountList.add(productOrDiscount);
        } else {
          ProductsOrDiscounts productsOrDiscounts = (ProductsOrDiscounts) 
                                      brmUtil.getPojoFromFList(discounts,ProductsOrDiscounts.class);
          productsOrDiscounts.setElem(String.valueOf(i));
          discountList.add(productsOrDiscounts);
        } // End of cycle end details check.
      }
    } // End of products existence check for given deal.
    return discountList;
  } // End of createPaymentDiscountSparser method.
} // End of SubscriptionSparserCreator.