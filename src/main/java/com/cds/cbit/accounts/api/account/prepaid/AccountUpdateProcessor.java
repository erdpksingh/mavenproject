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
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.cds.cbit.accounts.util.UtcDateUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalContext;
import com.portal.pcm.PortalOp;
import com.portal.pcm.SparseArray;
import com.portal.pcm.fields.FldAccountObj;
import com.portal.pcm.fields.FldBalGrpObj;
import com.portal.pcm.fields.FldBalInfo;
import com.portal.pcm.fields.FldBillinfo;
import com.portal.pcm.fields.FldLimit;
import com.portal.pcm.fields.FldName;
import com.portal.pcm.fields.FldPoid;
import com.portal.pcm.fields.FldResults;
import com.portal.pcm.fields.FldServiceObj;
import com.portal.pcm.fields.FldServices;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
public class AccountUpdateProcessor {
  
  @Autowired
  private BrmServiceUtil brmUtil;
  
  /**
   * The method will update the prepaid account with the base plan information.
   * 
   * @param: reqBody    - @Mandatory - Body section of account creation request.
   * @param: custCommit - @Mandatory - Output of prepaid account creation.
   * @param: msisdnOp   - @Mandatory - MSISDN information.
   * @param: planFlist  - @Mandatory - Base plan information.
   * @param: simOp      - @Mandatory - SIM information.
   * @param: portal     - @Mandatory - BRM CM connection.
   * @param: endT       - @Mandatory - End date provided in the request.
  */
  public FList updateAccountWithBasePlan(
               final AccountRequestBody reqBody,final FList custCommit,final FList msisdnOp,
               final FList planFlist,final FList simOp,final String endT,final PortalContext portal)
                                               throws EBufException, JAXBException, ParseException {
    final Poid devicePoid = 
          msisdnOp.get(FldResults.getInst()).getValues().get(0).get(FldPoid.getInst());
    final Poid balanceGroupPoid = 
          custCommit.get(FldBillinfo.getInst()).getValues().get(0).get(FldBalGrpObj.getInst());
    final Poid accountPoid  = 
          custCommit.get(FldBalInfo.getInst()).getValues().get(0).get(FldAccountObj.getInst());
    
    final String planName = reqBody.getPlanInfo().getName();
    final Poid planPoid = 
          planFlist.get(FldResults.getInst()).getValues().get(0).get(FldPoid.getInst());
    final FList planReadOp = brmUtil.readBrmPoidInTransaction(planPoid,portal); 
    log.info(simOp);
    
    final SparseArray balInfo = createBalanceInfo(balanceGroupPoid,planReadOp);
    final List<UpdateServices> services = createServicesInfo(devicePoid,planReadOp);
    
    final CustModifyCustomer modifyCustomer = 
          CustModifyCustomer.builder().accountObj(accountPoid.toString())
                            .descr(planName).flags("0").name(planName).poid(planPoid.toString())
                            .services(services).build();
    if (endT.length() > 0) {
      modifyCustomer.setEndT(UtcDateUtil.convertStringToTimeStamp(endT));
    }
    
    final JAXBContext jaxbContext = JAXBContext.newInstance(CustModifyCustomer.class);
    final FList input =  brmUtil.getFListFromPojo(jaxbContext, modifyCustomer);
    input.set(FldBalInfo.getInst(),balInfo);
    log.info(input);
    return portal.opcode(PortalOp.CUST_MODIFY_CUSTOMER, input);
  } // End of updateAccountWithBasePlan method.
  
  /**
   * The method will create balance section of custModify opcode with plan details.
   * 
   * @param: balGroupPoid - @Mandatory - Balance group POID.
   * @param: planReadOp   - @Mandatory - Plan information.
  */
  private SparseArray createBalanceInfo(final Poid balGroupPoid,final FList planReadOp) 
                                                                              throws EBufException {
    final FList balance = new FList();
    balance.set(FldName.getInst(),"Balance Group<Account>");
    balance.set(FldPoid.getInst(),balGroupPoid);
    balance.set(FldServiceObj.getInst(),new Poid(0,0,""));
    balance.set(FldLimit.getInst(),planReadOp.get(FldLimit.getInst()));
    final SparseArray balInfo = new SparseArray();
    balInfo.add(balance);
    return balInfo;
  } // End of createBalanceInfo method.
  
  /**
   * The method will create services section of custModify opcode with plan details.
   * 
   * @param: devicePoid   - @Mandatory -   MSISDN POID.
   * @param: planReadOp   - @Mandatory - Plan information.
  */
  private List<UpdateServices> createServicesInfo(
                               final Poid devicePoid,final FList planReadOp)  throws EBufException {
    final ServiceDevices devices = 
          ServiceDevices.builder().flags("1").deviceObj(devicePoid.toString()).build();
    
    final List<UpdateServices> serviceList = new ArrayList<>();
    
    final List<FList> planServices = planReadOp.get(FldServices.getInst()).getValues();
    int i = 0;
    for (final FList service : planServices) {
      final UpdateServices servicePojo =
                      (UpdateServices)brmUtil.getPojoFromFList(service, UpdateServicesFlist.class);
      servicePojo.setDevices(devices);
      servicePojo.setElem(String.valueOf(i));
      serviceList.add(servicePojo);
      i++;
    } // End of looping through services.
    return serviceList;
  } // End of createServicesInfo method.
} // End of AccountUpdateProcessor, which updates account with base plan for prepaid account.