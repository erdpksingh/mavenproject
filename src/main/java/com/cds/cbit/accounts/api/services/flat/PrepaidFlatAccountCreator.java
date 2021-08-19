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
import com.cds.cbit.accounts.api.account.payload.PlanInfo;
import com.cds.cbit.accounts.api.account.prepaid.PrepaidAccountHelper;
import com.cds.cbit.accounts.api.account.prepaid.PrepaidAccountInput;
import com.cds.cbit.accounts.api.account.prepaid.PrepaidAccountValidator;
import com.cds.cbit.accounts.api.account.prepaid.PrepaidWorkFlowProcessor;
import com.cds.cbit.accounts.interfaces.TransactionService;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.PortalContext;

import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will provide COMMIT_CUST_OPCODE creation for flat account.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component("flatAccountPrepaid")
public class PrepaidFlatAccountCreator implements TransactionService {
  
  @Autowired
  private BrmServiceUtil brmUtil;
  
  private final PrepaidAccountValidator validator;
  private final PrepaidAccountHelper helper;
  private final PrepaidWorkFlowProcessor processor;
  
  /** Constructor injection. **/
  public PrepaidFlatAccountCreator(final PrepaidWorkFlowProcessor processor,
         final PrepaidAccountHelper helper,final PrepaidAccountValidator validator) {
    
    this.processor = processor;
    this.helper = helper;
    this.validator = validator;
  } // End of constructor injection.
  
  /* @see com.cds.cbit.accounts.interfaces.AccountCreator#createAccount(java.lang.String) */
  @Override
  public FList processTransaction(final Map<String,Object> inputs,final PortalContext portal) 
                                               throws EBufException, JAXBException, ParseException {
    
    final AccountRequest accountReq = (AccountRequest) inputs.get("request");
    final PlanInfo planInfo = accountReq.getBody().getPlanInfo();
    
    final String planType = validator.verifyPlanType(planInfo.getName());
    
    final String endT = getEndDate(accountReq.getBody().getPlanInfo(),planType);
    final CommitCustOpcode custCommit = helper.createCustCommitOpcode(accountReq.getBody(),endT);

    final JAXBContext jaxbContext = JAXBContext.newInstance(CommitCustOpcode.class);
    final FList input =  brmUtil.getFListFromPojo(jaxbContext, custCommit);
        
    log.info(input);
    final FList accountOp = portal.opcode(63, input);
    final FList planOp = (FList) inputs.get("flist");
    final FList msisdnOp = (FList) inputs.get("msisdn");
    final FList simOp = (FList) inputs.get("sim");
    
    final PrepaidAccountInput accountInputs = 
          PrepaidAccountInput.builder().custCommit(accountOp).endT(endT)
                  .msisdnOp(msisdnOp).planFlist(planOp).planType(planType).portal(portal)
                  .reqBody(accountReq.getBody()).simOp(simOp).build();
    processor.processWorkFlow(accountInputs);
    return accountOp;
  } // End of createAccount.
  
  /**
   * The method will check whether plan was normal plan or not. If it was normal plan and
   * planStartDate available in the request then it will consider it as endT and return.
  */
  private String getEndDate(final PlanInfo planInfo,final String planType) {
    
    final StringBuilder endT = new StringBuilder();
    final Optional<String> planStartDate = Optional.ofNullable(planInfo.getStartDate());
    // If plan was normal plan and planStartDate provided then take it as endT.
    if ("normalPlan".equals(planType) && planStartDate.isPresent()) {
      endT.append(planStartDate.get());
    } // End of plan & planStartDate check.
    return endT.toString();
  } // End of getEndDate method.
} // End of FlatAccountCreator. 