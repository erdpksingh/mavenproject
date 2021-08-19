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

import com.cds.cbit.accounts.api.account.balance.flists.BalImpact;
import com.cds.cbit.accounts.factory.BillingValidationFactory;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.fields.FldExtraResults;
import com.portal.pcm.fields.FldLinkedObj;
import com.portal.pcm.fields.FldLinkedObjs;
import com.portal.pcm.fields.FldName;
import com.portal.pcm.fields.FldPermitted;
import com.portal.pcm.fields.FldPoid;
import com.portal.pcm.fields.FldResults;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will provide helper methods for retrieving customer balance information.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component
public class CustomerBalanceHelper {
  
  @Autowired
  private BillingValidationFactory factory;
  
  private final GrantObjValidator granObjSearch;

  /** Constructor injection. **/
  public CustomerBalanceHelper(final GrantObjValidator granObjSearch) {
    
    this.granObjSearch = granObjSearch;
  } // End of constructor injection.
  
  /**
   * The method will fetch information of GrantObj from the customer balImpacts section.
   * 
   * @param: balanceOp - @Mandatory - Customer balances POJO.
   * @param: dealType  - @Mandatory - Current balance deal type of the loop.
  */
  protected Map<String, String> getGrantObjDetails(
            final BalImpact balanceOp,final String dealType,final String grantorObj) 
                                                            throws EBufException, JAXBException {
 
    final Map<String,String> details = new ConcurrentHashMap<>();
    final FList grantFlist = granObjSearch.executeGrantObj(dealType,grantorObj);
    
    if (grantFlist.hasField(FldResults.getInst())) { // Proceed, If grant obj has results section
      
      final Poid dealObj = 
            grantFlist.get(FldResults.getInst()).getValues().get(0)
                     .get(FldLinkedObjs.getInst()).getValues().get(0)
                     .get(FldLinkedObj.getInst()).getValues().get(0).get(FldPoid.getInst());
   
      final String planName = fetchPlanNameFromDeal(dealObj.toString(),grantFlist);
      final int status = fetchServiceAlias(balanceOp,grantFlist);
      details.put(ConstantsUtil.PLANNAME, planName);
      details.put("type",String.valueOf(status));
      
    } else { // Grant object not found for the current deal of the loop.
      log.info("Purchased product not found in BRM.");  
    } // End of Grant object results check.
    return details;
  } // End of getGrantObjDetails method.
  
  /**
   * The method will fetch plan name from grantObj FList output or Deal FList output.
   * 
   * @param: dealObj    - @Mandatory - Deal FList output.
   * @param: grantFlist - @Mandatory - GrantObj FList output
  */
  protected String fetchPlanNameFromDeal(final String dealObj,final FList grantFlist) 
                                                          throws EBufException, JAXBException {
    
    final BillingValidation validator = factory.getValidator("dealValidator");
    final FList dealPlanOp = validator.validateInput(dealObj,"512");
    
    // If deal output has results field then fetch plan name from it, otherwise fetch plan
    // name from grantObj output FList.
    if (dealPlanOp.hasField(FldResults.getInst())) {
      return dealPlanOp.get(FldResults.getInst()).getValues().get(0).get(FldName.getInst());
    }
    return grantFlist.get(FldExtraResults.getInst()).getValues().get(0).get(FldName.getInst());
  } // End of fetchPlanNameFromDeal method.
  
  /**
   * The method will fetch service status of the requested account number.
   * 
   * @param balanceOp     - @Mandatory - Customer balances POJO.
   * @param grantObjFlist - @Mandatory - GranObj output FList.
  */
  protected int fetchServiceAlias(final BalImpact balanceOp,final FList grantObjFlist)
                                                                         throws EBufException {
    final Map<String,Integer> serviceType = new ConcurrentHashMap<>();
    serviceType.put("DATA", 1);
    serviceType.put("VOICE", 2);
    serviceType.put("SMS", 3);
    serviceType.put("MMS", 4);

    final Map<String,String> aliasMap = new ConcurrentHashMap<>();
    aliasMap.put("2000000",ConstantsUtil.VOICE);
    aliasMap.put("2000202","DATA");
    aliasMap.put("2000200",ConstantsUtil.VOICE);
    aliasMap.put("2000201","SMS");
    aliasMap.put("2000014","SMS");
    if (aliasMap.containsKey(balanceOp.getElem())) {
      return serviceType.get(aliasMap.get(balanceOp.getElem()));
    }
    return getServiceAlaisFromType(grantObjFlist);
  } // End of fetchServiceAlias method.
  
  /**
   * The method will fetch default service name if element code not found in the previous method.
   * 
   * @param grantObjFlist - @Mandatory - GranObj output FList.
  */
  protected int getServiceAlaisFromType(final FList grantObjFlist) throws EBufException {
    
    final String type = grantObjFlist.get(FldExtraResults.getInst()).getValues().get(0)
                                                             .get(FldPermitted.getInst());
    
    if (type.contains("gprs") || type.contains("data")) {
      return 1;
    } else if (type.contains("telephony")) {
      return 2;
    } else if (type.contains("sms")) {
      return 3;
    } else {
      return 4;
    }
  } // End of getServiceAlaisFromType method.
} // End of CustomerBalanceHelper, which provide methods for extracting balance related information.