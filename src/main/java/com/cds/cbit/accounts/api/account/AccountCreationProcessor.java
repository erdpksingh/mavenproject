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

import com.cds.cbit.accounts.api.account.flist.BillGenerationFlist;
import com.cds.cbit.accounts.api.account.payload.AccountRequest;
import com.cds.cbit.accounts.api.account.payload.AccountResponse;
import com.cds.cbit.accounts.commons.BrmTransactionManager;
import com.cds.cbit.accounts.commons.factory.BillingInputValidateFactory;
import com.cds.cbit.accounts.exceptions.BillingException;
import com.cds.cbit.accounts.properties.AccountConfigProperties;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.cds.cbit.accounts.util.ConnectionErrorUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.google.common.collect.ImmutableMap;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.fields.FldAccountNo;
import com.portal.pcm.fields.FldAccountObj;
import com.portal.pcm.fields.FldResults;
import com.portal.pcm.fields.FldStartT;
import com.portal.pcm.fields.FldStateId;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * The component provide methods to process basic functionality applicable to all type of accounts 
 * like flat, hierarchical, pre paid, post paid and so on. These functionality includes validating
 * given base plan, MSISDN, SIM and so on, and after these validation the createAccountInBrm will
 * invoke global transaction component to create account in BRM. 
 * 
 * @author Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component
public class AccountCreationProcessor {

  @Autowired
  private BillingInputValidateFactory validator; 
  @Autowired
  private AccountConfigProperties properties;
  @Autowired
  private BrmServiceUtil brmServiceUtil;
  
  private final BrmTransactionManager transaction;
  private final AccountResponser responser;
  
  /** Constructor Injection. **/
  public AccountCreationProcessor(
                        final BrmTransactionManager transaction,final AccountResponser responser) {
    
    this.transaction = transaction;
    this.responser = responser;
  } // End of constructor injection.
  
  /** 
   * The method will validate SIM,MSISDN, and base plan through internal methods and if validation
   * are successful, then it invokes global transaction to create account in BRM.
  */
  public AccountResponse createAccountInBrm(final AccountRequest request) {
    try {
      
      final FList planOp   = validateBasePlan(request.getBody().getPlanInfo().getName());
      final FList msisdnOp = validateMsisdn(request.getBody().getAccountInfo().getMsisdn());
      
      final FList simOp = validateSim(request.getBody().getAccountInfo().getSim());
      
      // Creating inputs required for account creation.
      final Map<String,Object> inputs = 
            ImmutableMap.of("flist",planOp,"request",request,"msisdn",msisdnOp,"sim",simOp);
      
      // Creating account type bean name which route the control to specific type of account.
      final StringBuilder accountType = 
            new StringBuilder(properties.getType()).append("Account")
                             .append(StringUtils.capitalize(
                                     request.getHead().getSubType().toLowerCase(Locale.ENGLISH)));
      // creating account with global transaction.
      final FList accountOp = 
                  (FList) transaction.executeWithGlobalTransaction(accountType.toString(),inputs);
      
      final Poid accountPoid = accountOp.get(FldAccountObj.getInst());
      
      if (request.getBody().getAdvancePayments() != null) {
        billGeneration(request.getBody().getAccountInfo().getMsisdn(), accountPoid);
      }
      
      final Long createdT = accountOp.get(FldStartT.getInst()).getTime();
      return responser.createAccountCreationResponse(request, accountPoid,createdT);
    
    } catch (EBufException e) {
      
      throw new BillingException(ConnectionErrorUtil.getConnectionError(e));
    } catch (JAXBException e) {
      
      throw new BillingException("100110",e);
    }
  } // End of creating account in BRM, with global transaction, for different type of accounts.
  
  /**
   * The method will validate whether given base plan exist in the BRM or not. If it exist then
   * the method will return the output FList of base plan, otherwise it will throw an error.
   *
   * @param:  basePlanName  - @Mandatory - Base plan name.
   * @return: base plan output FList retrieved from BRM system.
  */
  private FList validateBasePlan(final String basePlanName) throws EBufException, JAXBException {
    
    return validator.getValidator("basePlan",basePlanName);
  } // End of validating base plan in BRM.
  
  /**
   * The method will validate whether given SIM exist in the BRM or not. If it not exist then the
   * method throw an error.
   *
   * @param: simNumber  - @Mandatory - SIM number provided in the request.
  */
  private FList validateSim(final String simNumber) throws EBufException, JAXBException {
    
    // If SIM Provided in the request then validate its availability.
    final Optional<String> sim = Optional.ofNullable(simNumber);
  
    if (sim.isPresent()) {
      
      // The first parameter tells the validator device bean to fetch, second param provides
      // SIM value, and the third parameter specifies type of device to be validated SIM.
      
      return validator.getValidatorWithMultiArgs("device",sim.get(),"sim");
    } 
    return new FList();
  } // End of validating whether given SIM exist in BRM or not.
  
  /**
   * The method will validate whether given MSISDN exist in the BRM or not. If it exist the method
   * verifies whether it is assigned to another account or not. If given MSISDN not available in
   * BRM then the method throw 200102 error code and if it is already assigned then the method will
   * throw 200101 error code.
   * 
   * @param:  msisdn  - @Mandatory - Customer phone number.
   * @return: MSISDN output FList retrieved from BRM system.
  */
  private FList validateMsisdn(final String msisdn) throws EBufException, JAXBException {
    
    final FList  msisdnOp = validator.getValidatorWithMultiArgs("device", msisdn,"msisdn");
    if (!(msisdnOp.hasField(FldResults.getInst()))) {
      throw new BillingException("100112");
    } // End of msisdn results check.
    
    final int status = msisdnOp.get(FldResults.getInst())
                                              .getValues().get(0).get(FldStateId.getInst());
    if (status != 1 && status != 4) {
      throw new BillingException("200101");
    } // End of MSISDN status check.
    return msisdnOp;
  } // End of validating given MSISDN in BRM.
  
  /**
   * Method to process billGeneration in BRM.
   * @param- jsonRequest
   * @param- accountNumber
   * @param- portalContext
   */
  private void billGeneration(final String msisdn,final Poid accountObj) {
    log.info("Processing bill generation...");
    try {
      final FList accountDetails = brmServiceUtil.readBrmPoid(accountObj);
      final String accountNo = String.valueOf(accountDetails.get(FldAccountNo.getInst()));
      final BillGenerationFlist billGen = 
            BillGenerationFlist.builder().poid(ConstantsUtil.ACCOUNT_POID)
                                         .accountNo(accountNo).msisdn(msisdn).build();

      brmServiceUtil.executeInputInBrm(billGen,200082,BillGenerationFlist.class);
    } catch (Exception e) {
      log.info("Error occurred while generating BILL. {}",e.getMessage());
    }
  } // End of billGeneration Method.
} // End of AccountCreationProcessor, which process account related API logic.