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

import com.cds.cbit.accounts.api.account.flist.AdvanceCharges;
import com.cds.cbit.accounts.api.account.flist.AdvancePaymentFlist;
import com.cds.cbit.accounts.api.account.flist.BillItemTransferFlist;
import com.cds.cbit.accounts.api.account.flist.BillItems;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalContext;
import com.portal.pcm.PortalOp;
import com.portal.pcm.fields.FldAmount;
import com.portal.pcm.fields.FldBalImpacts;
import com.portal.pcm.fields.FldItemObj;
import com.portal.pcm.fields.FldResourceId;
import com.portal.pcm.fields.FldResults;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will execute the workflow and business logic of posting advance payment
 * in BRM for the on demand deals purchased as part of account creation.
 * 
 * @author  Venkata Nagaraju.
 * @Version 1.0
*/
@Log4j2
@Component
public class AdvancePaymentProcessor {
  
  @Autowired
  private BrmServiceUtil brmUtil;
  
  /**
   * The method will post advance payment in BRM for the given deal purchase.
   * 
   * @param: input  - @MAndatory - Advance payment FList inputs.
   * @param: portal - @MAndatory - BRM CM connection.
  */
  protected void processAdvancePayment(
            final AdvancePaymentFlistInput input,final PortalContext portal) 
                                                             throws JAXBException, EBufException {
    final AdvanceCharges payment =
          AdvanceCharges.builder().elem("0")
                        .amount(input.getAmount().toString()).command("0").command("0")
                        .currency("36").transId(Poid.valueOf(input.getAccountObj()).getId())
                        .payType("CC".equals(input.getPaymentType()) ? "10003" : "10005").build();
     
    final StringBuilder description = new StringBuilder("ADVPAY:").append(input.getDescription());

    final AdvancePaymentFlist flist =
          AdvancePaymentFlist.builder().poid(input.getAccountObj())
          .programName("Advance Payment API").descr(description.toString()).currency("36")
          .amount(input.getAmount().toString())
          .payment(payment).build();
    
    final JAXBContext jaxbContext = JAXBContext.newInstance(AdvancePaymentFlist.class);
    final FList subscribe =  brmUtil.getFListFromPojo(jaxbContext,flist);
    log.info(subscribe);
    final FList advancePaymentOp = portal.opcode(PortalOp.BILL_RCV_PAYMENT, subscribe);
    log.info(advancePaymentOp);
    transferBillItem(
                advancePaymentOp,input.getSubscriptionItemPoid(),input.getAccountObj(),portal);
  } // End of processAdvancePayment method.
  
  private void transferBillItem(
          final FList advancePaymentOp,final String subscriptionItemPoid,
                                       final String accountPoid,final PortalContext portal) 
                                                            throws EBufException, JAXBException {
    log.info("Calling bill transfer....");
    final Map<String,Object> billDetails = getBillDetails(advancePaymentOp);
    final String subscriptionAmount = String.valueOf(billDetails.get("subscriptionAmount"));
    final String paymentItemPoid = String.valueOf(billDetails.get("paymentItemPoid"));
    
    final BillItems billItem = 
          BillItems.builder().amount(subscriptionAmount).elem("0")
                                                       .poid(subscriptionItemPoid).build();
    final BillItemTransferFlist flist =
          BillItemTransferFlist.builder().poid(accountPoid).programName("PAYMENT ALLOCATE")
          .itemObj(paymentItemPoid).items(billItem).build();
    
    final JAXBContext jaxbContext = JAXBContext.newInstance(BillItemTransferFlist.class);
    final FList billItemInput =  brmUtil.getFListFromPojo(jaxbContext,flist);
    log.info(billItemInput);
    final FList billTransferOp = portal.opcode(126, billItemInput);
    log.info(billTransferOp);
  }
  
  private Map<String, Object> getBillDetails(final FList advancePaymentOp) throws EBufException {
    
    final Map<String,Object> billDetails = new ConcurrentHashMap<>();
    
    for (final FList subscription : advancePaymentOp.get(FldResults.getInst()).getValues()) {
      if (subscription.hasField(FldBalImpacts.getInst())) {
        
        // Looping through balance impacts of subscription output.
        for (final FList balImpacts : subscription.get(FldBalImpacts.getInst()).getValues()) {
          if (balImpacts.get(FldResourceId.getInst()) == 36) {
            
            billDetails.put("paymentItemPoid",balImpacts.get(FldItemObj.getInst()).toString());
            billDetails.put("subscriptionAmount",balImpacts.get(FldAmount.getInst()).toString());
          } // End of resource id 36 check.
        } // End of looping through balImpacts section of subscription output.
      } // End of result check for balImpacts.
    } // End of looping through subscription output FList.
    return billDetails;
  } // End of getBillDetails method.
} // End of AdvancePaymentProcessor.