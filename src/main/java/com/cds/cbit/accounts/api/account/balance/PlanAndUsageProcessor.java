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

package com.cds.cbit.accounts.api.account.balance;

import com.cds.cbit.accounts.api.account.balance.flists.SubBalImpacts;
import com.cds.cbit.accounts.api.account.balance.payload.CustomerPlanInfo;
import com.cds.cbit.accounts.util.UtcDateUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.fields.FldExtraResults;
import com.portal.pcm.fields.FldName;
import com.portal.pcm.fields.FldPurchaseEndT;
import com.portal.pcm.fields.FldPurchaseStartT;
import com.portal.pcm.fields.FldResults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * The component provides methods to fetch plan details and usage details as part of the API
 * call GetCustomerBalances.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class PlanAndUsageProcessor {
  
  @Autowired
  private Environment props;
  
  private final CustomerDetailsRetriever customerDetail;
  
  public PlanAndUsageProcessor(final CustomerDetailsRetriever customerDetail) {
    
    this.customerDetail = customerDetail;
  } // End of constructor injection.
  
  /**
   * The method will fetch from,end date information along with customer used and unused amounts.
   * 
   * @param: subBalance - @Mandatory - SubBalanceImpacts POJO.
   * @param: balIndex   - @Mandatory - Index of current balance element of the loop.
  */
  protected Map<String, Object> getDateAndUsage(
                                          final SubBalImpacts subBalance,final String balIndex) {
    
    // Fetching external id with the current balance index, from the config file.
    final Map<String,Object> dateAndUsage = new ConcurrentHashMap<>();
    final StringBuilder externalId = new StringBuilder("msg.externalId");
    final Optional<String> externalIdVal = 
          Optional.ofNullable(props.getProperty(externalId.append(balIndex).toString()));
    
    // Fetching current and available balances from the sub balances section.
    final BigDecimal availableBalance = 
          new BigDecimal(subBalance.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
    final BigDecimal currentBalance = new BigDecimal(subBalance.getAmount());
    
    if (externalIdVal.isPresent()) {
      dateAndUsage.put("externalId", externalIdVal.get());
    }
    dateAndUsage.put("availableBalance",availableBalance);
    dateAndUsage.put("currentBalance",currentBalance);
    
    // Fetching fromDate and endDate of the sub balances.
    final Long validFrom = subBalance.getValidFrom();
    final String validFromDate  = UtcDateUtil.convertDateToUtc(new Date(validFrom * 1000));
    
    final Optional<Long> toDate = Optional.ofNullable(subBalance.getValidTo());
    if (toDate.isPresent()) {
      final Long validTo = toDate.get();
      final String validToDate  = UtcDateUtil.convertDateToUtc(new Date(validTo * 1000));
      dateAndUsage.put("validTo",validToDate);
    }
    dateAndUsage.put("validFrom",validFromDate);
    return dateAndUsage;  // Returning customer current, available balance amount with dates.
  } // End of getDateAndUsage method.
  
  /**
   * The method will fetch plan information of "GetCustomerBalance" API for both with and without
   * MSISDN association for the given account.
   * 
   * @param: accountObj - @Mandatory - Account POID of requested account number.
  */
  protected CustomerPlanInfo fetchPlanDetails(final String accountObj) 
                                                           throws EBufException, JAXBException {
    final FList packageOp = customerDetail.fetchPackagedata(accountObj);
    final CustomerPlanInfo planInfo = new CustomerPlanInfo();
    
    if (packageOp.hasField(FldResults.getInst())) { // Proceed, if package FList as result field.
      planInfo.setName(
              packageOp.get(FldExtraResults.getInst()).getValues().get(0).get(FldName.getInst()));
      
      // Looping through customer packages results.
      for (final FList packageDetail : packageOp.get(FldResults.getInst()).getValues()) {
        
        planInfo.setFromDate(
                    UtcDateUtil.convertDateToUtc(packageDetail.get(FldPurchaseStartT.getInst())));
        if (packageDetail.containsKey(FldPurchaseEndT.getInst())) {
          planInfo.setToDate(UtcDateUtil.convertDateToUtc(
                                                   packageDetail.get(FldPurchaseEndT.getInst())));
        } // End of end date check.
      } // End of looping through package details results.
    } // End of result field check for plan details FList.
    return planInfo;
  } // End of fetchPlanDetails method.
} // End of BalanceDatesAndAmount, which retrieve dates and amounts required for customer balances.