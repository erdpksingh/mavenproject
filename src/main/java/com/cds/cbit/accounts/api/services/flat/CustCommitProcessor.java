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

import com.cds.cbit.accounts.api.account.AccountCreationHelper;
import com.cds.cbit.accounts.api.account.AccountServicesHelper;
import com.cds.cbit.accounts.api.account.flist.AccountInfo;
import com.cds.cbit.accounts.api.account.flist.BalanceInfo;
import com.cds.cbit.accounts.api.account.flist.BillingInfo;
import com.cds.cbit.accounts.api.account.flist.CommitCustOpcode;
import com.cds.cbit.accounts.api.account.flist.LocaleInfo;
import com.cds.cbit.accounts.api.account.flist.NameInfo;
import com.cds.cbit.accounts.api.account.flist.PaymentInfoDetail;
import com.cds.cbit.accounts.api.account.flist.Profile;
import com.cds.cbit.accounts.api.account.flist.Services;
import com.cds.cbit.accounts.api.account.payload.AccountRequestBody;
import com.cds.cbit.accounts.api.account.payload.PersonalInfo;
import com.cds.cbit.accounts.properties.AccountConfigProperties;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will create input for CUST_COMMIT_CUSTOMER opcode, which create account in BRM.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class CustCommitProcessor {
  
  @Autowired
  private AccountConfigProperties properties;

  private final AccountCreationHelper accountHelper;
  private final AccountServicesHelper serviceHelper;
  
  /** Constructor Injection. **/
  public CustCommitProcessor(
      final AccountCreationHelper accountHelper,final AccountServicesHelper serviceHelper) {
    
    this.accountHelper = accountHelper;
    this.serviceHelper = serviceHelper;
  } // End of constructor injection.
  
  /** The method will create BRM CUST_COMMIT opcode. **/
  public CommitCustOpcode createOpcode(final AccountRequestBody reqBody,
         final FList planReadOp,final Poid planPoid,final Poid devicePoid) throws EBufException {
    
    final PersonalInfo personal = reqBody.getPersonalInfo();
    final String billingDate = reqBody.getAccountInfo().getBillingDate();
    
    final List<Profile> profiles = new ArrayList<>();
    profiles.add(accountHelper.createProfileInfo(reqBody));
    profiles.add(accountHelper.createSubscriberProfile(personal));
    
    final AccountInfo accountInfo   = accountHelper.createAccountInfo();
    final BalanceInfo balInfo       = accountHelper.createBalanceInfo(planReadOp);
    final BillingInfo billInfo      = accountHelper.createBillingInfo(billingDate);
    final LocaleInfo locale         = LocaleInfo.builder().elem("0")
                                                        .locale(properties.getLocale()).build();
    final NameInfo nameInfo         = accountHelper.createNameInfo(personal, personal.getAddress());
    final PaymentInfoDetail payInfo = accountHelper.createPaymentInfo(
                                                                   personal, personal.getAddress());
    final List<Services> services   = serviceHelper.createServices(
                                             personal.getName().getFirst(),planReadOp,devicePoid);
    
    return CommitCustOpcode.builder().dealObj("0.0.0.1 / -1 0").descr("Base").flags("0")
                                     .poid(planPoid.toString()).profiles(profiles).txnFlags("2")
                                     .accountInfo(accountInfo).balInfo(balInfo)
                                     .locales(locale).name("").nameInfo(nameInfo).payInfo(payInfo)
                                     .billInfo(billInfo).services(services).build();
  } // End of creating CUST_COMMIT opcode.
} // End of CustCommitProcessor, which prepare input for CUST_COMMIT opcode.