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

package com.cds.cbit.accounts.api.services.flat;

import com.cds.cbit.accounts.api.account.flist.CommitCustOpcode;
import com.cds.cbit.accounts.api.account.payload.AccountRequest;
import com.cds.cbit.accounts.api.account.subscriptions.AccountPlanCustomizer;
import com.cds.cbit.accounts.interfaces.TransactionService;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalContext;
import com.portal.pcm.fields.FldPoid;
import com.portal.pcm.fields.FldResults;

import java.text.ParseException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will provide COMMIT_CUST_OPCODE creation for flat account.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component("flatAccountPostpaid")
public class PostpaidFlatAccountCreator implements TransactionService {

  @Autowired
  private CustCommitProcessor processor;
  
  @Autowired
  private BrmServiceUtil brmUtil;
  
  @Autowired
  private AccountPlanCustomizer planCustomizer;
  
  /* @see com.cds.cbit.accounts.interfaces.AccountCreator#createAccount(java.lang.String) */
  @Override
  public FList processTransaction(final Map<String,Object> inputs,final PortalContext portal) 
                                        throws EBufException, JAXBException, ParseException {
    
    final AccountRequest accountReq = (AccountRequest) inputs.get("request");
    
    final FList planFlist = (FList) inputs.get("flist");
    final Poid planPoid = 
                planFlist.get(FldResults.getInst()).getValues().get(0).get(FldPoid.getInst());
    final FList msisdn = (FList) inputs.get("msisdn");
    final Poid devicePoid = msisdn.get(FldResults.getInst()).getValues().get(0)
                                                                      .get(FldPoid.getInst());
    final FList planReadOp = brmUtil.readBrmPoidInTransaction(planPoid,portal);

    final CommitCustOpcode custCommit = 
          processor.createOpcode(accountReq.getBody(),planReadOp,planPoid,devicePoid);
    
    return planCustomizer.processPlanCustomization(accountReq.getBody(),custCommit,planPoid,portal);
  } // End of createAccount.
} // End of FlatAccountCreator. 