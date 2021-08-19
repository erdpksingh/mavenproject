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

import com.cds.cbit.accounts.api.account.payload.AccountRequestBody;
import com.cds.cbit.accounts.properties.PrepaidProperties;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.cds.cbit.accounts.util.UtcDateUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalContext;
import com.portal.pcm.PortalOp;
import com.portal.pcm.fields.FldAccountObj;
import com.portal.pcm.fields.FldBalInfo;

import java.text.ParseException;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will provide methods to update the prepaid account created with the base plan 
 * information.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component
public class PrepaidWorkFlowProcessor {
  
  @Autowired
  private BrmServiceUtil brmUtil;
  
  @Autowired
  private PrepaidProperties props;
  
  private final AccountUpdateProcessor processor;
  private final ComponentSubscriber subscriber;
  
  /** Constructor injection. **/
  public PrepaidWorkFlowProcessor(
         final AccountUpdateProcessor processor,final ComponentSubscriber subscriber) {
    
    this.processor = processor;
    this.subscriber = subscriber;
  } // End of constructor injection.
  
  /**
   * The method will update the prepaid account with the base plan information.
   * 
   * @param: accountInputs - @Mandatory - Prepaid account inputs.
  */
  public void processWorkFlow(final PrepaidAccountInput accountInputs) 
                                             throws EBufException, JAXBException, ParseException {
    
    final AccountRequestBody reqBody = accountInputs.getReqBody();
    final String planType = accountInputs.getPlanType();
    final FList custCommit = accountInputs.getCustCommit();
    final FList msisdnOp = accountInputs.getMsisdnOp();
    final FList planFlist = accountInputs.getPlanFlist();
    final FList simOp = accountInputs.getSimOp();
    final PortalContext portal = accountInputs.getPortal();
    final String endT = accountInputs.getEndT();
    
    final Poid accountPoid  = 
          custCommit.get(FldBalInfo.getInst()).getValues().get(0).get(FldAccountObj.getInst());
    
    postPaymentCollect(reqBody.getPlanInfo().getPrice(),accountPoid,endT,portal);
    
    processor.updateAccountWithBasePlan(
                                      reqBody, custCommit, msisdnOp, planFlist, simOp,endT,portal);
    subscriber.subscribeComponents(
                               reqBody.getPlanInfo(),accountPoid, planType, planFlist,endT,portal);
  } // End of updateAccountWithBasePlan method.
  
  /**
   * The method will post payment in BRM against the base plan amount provided in the request as 
   * part of prepaid account creation.
   * 
   * @param: price        - @Mandatory - Base plan price given in the request.
   * @param: accountPoid  - @Mandatory - Account POID.
   * @param: endT         - @Mandatory - End date provided in the request.
  */
  private void postPaymentCollect(
          final double price,final Poid accountPoid,final String endT,final PortalContext portal)
                                            throws ParseException, JAXBException, EBufException {
    final String amount = String.valueOf(price);
    final BillPayment payment = 
          BillPayment.builder().amount(amount).command("0")
                     .currency(props.getCurrency()).payType(props.getPayCollectType())
                     .transId(String.valueOf(accountPoid.getId())).build();
    
    final BillReceivePayment paymentCollect =
          BillReceivePayment.builder().amount(amount).currency(props.getCurrency())
                            .name("Payment").payment(payment).poid(accountPoid.toString())
                            .programName("Payment").build();
    
    paymentCollect.setValidFrom(new Date(0).getTime() / 1000);
    paymentCollect.setValidTo(0L);
    if (endT.length() > 0) {
      paymentCollect.setEndT(UtcDateUtil.convertStringToTimeStamp(endT));
      paymentCollect.setValidFrom(UtcDateUtil.convertStringToTimeStamp(endT));
    } 
    final JAXBContext jaxbContext = JAXBContext.newInstance(BillReceivePayment.class);
    final FList paymentOp =  brmUtil.getFListFromPojo(jaxbContext,paymentCollect);
    log.info(paymentOp);
    portal.opcode(PortalOp.BILL_RCV_PAYMENT, paymentOp);
  } // End of postPaymentCollect method.
} // End of AccountUpdateProcessor, which updates account with base plan for prepaid account.