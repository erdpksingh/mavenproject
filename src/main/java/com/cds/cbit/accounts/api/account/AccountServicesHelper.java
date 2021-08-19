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

package com.cds.cbit.accounts.api.account;

import com.cds.cbit.accounts.api.account.flist.BalanceInfo;
import com.cds.cbit.accounts.api.account.flist.Deal;
import com.cds.cbit.accounts.api.account.flist.Devices;
import com.cds.cbit.accounts.api.account.flist.Limits;
import com.cds.cbit.accounts.api.account.flist.Services;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.fields.FldBalInfoIndex;
import com.portal.pcm.fields.FldCreditFloor;
import com.portal.pcm.fields.FldCreditLimit;
import com.portal.pcm.fields.FldCreditThresholds;
import com.portal.pcm.fields.FldCreditThresholdsFixed;
import com.portal.pcm.fields.FldDealObj;
import com.portal.pcm.fields.FldDeals;
import com.portal.pcm.fields.FldLimit;
import com.portal.pcm.fields.FldServiceId;
import com.portal.pcm.fields.FldServiceObj;
import com.portal.pcm.fields.FldServices;
import com.portal.pcm.fields.FldSubscriptionIndex;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * The component will provide the helper methods to add services and limits information to account
 * creation process.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class AccountServicesHelper {
  
  /**
   * The method will create account service information with the information from plan and MSISDN
   * output FList.
   * 
   * @param: name-       @Mandatory - Base plan name.
   * @param: planReadOp- @Mandatory - Base plan output FList.
   * @param: devicePoid- @Mandatory - MSISDN.
  */
  public List<Services> createServices(
             final String name,final FList planReadOp,final Poid devicePoid) throws EBufException {
    
    final List<FList> services = planReadOp.get(FldServices.getInst()).getValues();
    final List<Services> serviceList = new ArrayList<>();
    int i = 0;
    for (final FList service : services) {
      final List<Deal> deals = createServiceDeals(service);
      final Devices device = 
            Devices.builder().elem("0").deviceObj(devicePoid.toString()).flags("1").build();
      
      serviceList.add(Services.builder().elem(String.valueOf(i)).passwordClear("XXXX").login(name)
                 .subscriptionObj("").servicedId(service.get(FldServiceId.getInst()))
                 .subscriptionIndex(service.get(FldSubscriptionIndex.getInst()).toString())
                 .balInfoIndex(service.get(FldBalInfoIndex.getInst()).toString())
                 .deals(deals).serviceObj(service.get(FldServiceObj.getInst()).toString())
                 .balInfo(BalanceInfo.builder().build()).devices(device).build());
      i++;
    } // End of looping through plan services.
    return serviceList;
  } // End of creating account service information from MSISDN and plan information.

  /**
   * The method will create limit arrays.
   * 
   * @param: planReadOp- @Mandatory - Base plan output FList.
  */
  public List<Limits> createLimitsArray(final FList planReadOp) throws EBufException {
    
    final List<FList> planLimits = planReadOp.get(FldLimit.getInst()).getValues();
    final List<Limits> limitsList = new ArrayList<>();
    int i = 0;
    final List<String> indexList = new ArrayList<>();
    final Enumeration<Integer> enum1 = planReadOp.get(FldLimit.getInst()).getKeyEnumerator();
    while (enum1.hasMoreElements()) {
      final int arrIndex = enum1.nextElement().intValue();
      indexList.add(String.valueOf(arrIndex));
    }

    for (final FList limits : planLimits) {
      limitsList.add(Limits.builder().elem(indexList.get(i))
                    .creditLimit(String.valueOf(limits.get(FldCreditLimit.getInst())))
                    .creditFloor(String.valueOf(limits.get(FldCreditFloor.getInst())))
                    .creditThresholds(String.valueOf(limits.get(FldCreditThresholds.getInst())))
              .creditThresholdsFixed(String.valueOf(limits.get(FldCreditThresholdsFixed.getInst())))
              .build());
      i++;
    } // End of looping through plan services.
    return limitsList;
  } // End of creating account service information from MSISDN and plan information.
  
  /* The method create list of deals associated for a given service like SMS / GPRS.. **/
  private List<Deal> createServiceDeals(final FList service) throws EBufException {
    
    final List<Deal> dealsList = new ArrayList<>();
    
    for (int j = 0;j < service.get(FldDeals.getInst()).size();j++) {
      final Deal deal = Deal.builder().elem(String.valueOf(j))
                                      .dealObj(service.get(FldDeals.getInst()).getValues()
                                      .get(j).get(FldDealObj.getInst()).toString()).build();
      dealsList.add(deal);
    } // End of looping through service deals.
    return dealsList;
  } // End of creating service deals list. 
} // End of AccountServiceHelper, which provide service and creditLimit information.