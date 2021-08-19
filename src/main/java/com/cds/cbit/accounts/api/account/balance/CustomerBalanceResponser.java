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

import com.cds.cbit.accounts.api.account.balance.payload.CustomerBalanceRequest;
import com.cds.cbit.accounts.api.account.balance.payload.CustomerBalanceResponse;
import com.cds.cbit.accounts.api.account.balance.payload.CustomerBalanceResponseBody;
import com.cds.cbit.accounts.api.account.balance.payload.CustomerBalanceResponseResult;
import com.cds.cbit.accounts.api.account.balance.payload.CustomerPlanInfo;
import com.cds.cbit.accounts.commons.beans.ResponseHeader;
import com.cds.cbit.accounts.commons.beans.ResultInfo;
import com.cds.cbit.accounts.util.ResponseHeaderUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * The component provide method for creating response for customer balance related operations.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class CustomerBalanceResponser {
  
  @Autowired
  private Environment properties;

  /** The method create response for customer balance GET API call. **/
  public CustomerBalanceResponse createResponse(
               final CustomerBalanceRequest request,final CustomerBalanceResponseResult result) {
    
    final ResponseHeader header = ResponseHeaderUtil.createResponseHead(request.getHead());
    
    final ResultInfo resultInfo = ResultInfo.builder()
                                            .resultCode("SUCCESS").resultCodeId(0)
                                            .resultMsg(properties.getProperty("balance.success"))
                                            .build();
    final CustomerBalanceResponseBody responseBody = 
          CustomerBalanceResponseBody.builder().result(result).resultInfo(resultInfo).build();

    return CustomerBalanceResponse.builder().head(header).body(responseBody).build();
  } // End of createResponse method.
  
  protected CustomerPlanInfo getPlanInfo(final String fromDate) {
    
    return CustomerPlanInfo.builder().name("CirclesOne").fromDate(fromDate)
                                                .toDate("2018-08-01T08:35:00.021Z").build();
  } // End of getPlanInfo method.
} // End of CustomerBalanceResponser, which create "GetCustomerBalance" response.