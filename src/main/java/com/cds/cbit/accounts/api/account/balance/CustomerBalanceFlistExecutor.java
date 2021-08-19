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

import com.cds.cbit.accounts.api.account.balance.flists.CustomerBalanceFlist;
import com.cds.cbit.accounts.api.account.balance.flists.GetEceCustBalances;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalOp;
import com.portal.pcm.fields.FldExtraResults;
import com.portal.pcm.fields.FldName;
import com.portal.pcm.fields.FldPlanObj;
import com.portal.pcm.fields.FldResults;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will provide FList execution in BRM with the help of "BrmServiceUtil" component,
 * for all the FList involved in GetCustomerBalance" API workflow.
 * 
 * @author Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component
public class CustomerBalanceFlistExecutor {

  @Autowired
  private BrmServiceUtil brmUtil;
  
  private final GrantObjValidator grantObjValidator;
  
  public CustomerBalanceFlistExecutor(final GrantObjValidator grantObjValidator) {
    
    this.grantObjValidator = grantObjValidator;
  } // End of constructor injection.

  /**
   * The method will fetch balance information of given MSISDN with BAL_GET_ECE_BALANCES opcode.
   *  
   * @param: msisdn - @Mandatory - MSISDN associated with the requested account.
  */
  protected FList executeCustomerBalances(final String msisdn) throws EBufException, JAXBException {

    final GetEceCustBalances custBalance = 
          GetEceCustBalances.builder().login(msisdn)
                                        .mode("1").poid("0.0.0.1 /service/telco/gprs -1 0").build();
    return brmUtil.executeInputInBrm(
                               custBalance, PortalOp.BAL_GET_ECE_BALANCES,GetEceCustBalances.class);
  } // End of executeCustomerEceBalances method.

  
  /**
   * The method will convert the EceCustomerBalances FList output to CustomerBalanceFlist POJO.
   * 
   * @param: eceBalancesOp - @Mandatory - BAL_GET_ECE_BALANCES opcode output FList.
  */
  protected CustomerBalanceFlist fetchBalancePojoFromFlist(final FList eceBalancesOp) {

    return (CustomerBalanceFlist)brmUtil.getPojoFromFList(eceBalancesOp,CustomerBalanceFlist.class);
  } // End of executeCustomerEceBalances method.

  
  /**
   * The method will verify whether grantobj FList output has results field or not, If it has then
   * the method will derive the plan name from it.
   * 
   * @param: dealType : @Mandatory - type of the deal product / discount.
  */
  protected String getGrantObjForCounterBalance(final String grantorObj,
                                        final String dealType) throws EBufException, JAXBException {
    
    final StringBuilder planName = new StringBuilder();
    final FList grantFlist = grantObjValidator.executeGrantObj(dealType,grantorObj);

    if (grantFlist.hasField(FldResults.getInst())) {
      
      final Poid planObj = 
          grantFlist.get(FldResults.getInst()).getValues().get(0).get(FldPlanObj.getInst());
      if (planObj.getId() == 0) {
        planName.append(
              grantFlist.get(FldExtraResults.getInst()).getValues().get(0).get(FldName.getInst()));
      } else {
        final FList planOp = brmUtil.readBrmPoid(planObj);
        planName.append(planOp.get(FldName.getInst()));
      } // End of deriving plan name.
    } else {
      log.info("Purchased product not found in BRM.");
    }  // End of grantobj result check, to fetch plan name of the given deal.
    return planName.toString();
  } // End of getGrantObjForCounterBalance method.
} // End of CustomerBalanceFlistExecutor,which execute the FLists required for GetCustomerBalance.