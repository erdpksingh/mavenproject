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

import com.cds.cbit.accounts.api.account.payload.AccountRequest;
import com.cds.cbit.accounts.api.account.payload.AccountResponse;
import com.cds.cbit.accounts.api.account.payload.AccountResponseBody;
import com.cds.cbit.accounts.api.account.payload.AccountResponseResult;
import com.cds.cbit.accounts.commons.beans.ResponseHeader;
import com.cds.cbit.accounts.commons.beans.ResultInfo;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.cds.cbit.accounts.util.ResponseHeaderUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.Poid;
import com.portal.pcm.fields.FldAccountNo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * The component class will prepare responses for all type of account creations like simple account 
 * creation, account creation with plan customization and so on.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class AccountResponser {
  
  @Autowired
  private BrmServiceUtil brmUtil;
  
  @Autowired
  private Environment properties;
  
  /**
   * The method create response for simple flat account creation with request,account object and
   * creation date.
   * 
   * @param: request - @Mandatory - Account creation request payload.
  */
  protected AccountResponse createAccountCreationResponse(final AccountRequest request,
                              final Poid accountObj,final Long createdT) throws EBufException {
    
    final String accountNo   = brmUtil.readBrmPoid(accountObj).get(FldAccountNo.getInst());
    final ResponseHeader header = ResponseHeaderUtil.createResponseHead(request.getHead());
    
    final DateFormat fromatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.ENGLISH);
    fromatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    
    final AccountResponseResult result = 
          AccountResponseResult.builder().accountNumber(accountNo)
                                         .createdDate(fromatter.format(new Date(createdT))).build();
    final ResultInfo resultInfo = ResultInfo.builder()
                                            .resultCode("SUCCESS").resultCodeId(0)
                                            .resultMsg(properties.getProperty("account.success"))
                                            .build();
    final AccountResponseBody response =  AccountResponseBody.builder()
                                                     .resultInfo(resultInfo).result(result).build();
    return AccountResponse.builder().head(header).body(response).build();
  } // End of createAccountCreationResponse method.
} // End of AccountResponser, which create different types of account creation response.