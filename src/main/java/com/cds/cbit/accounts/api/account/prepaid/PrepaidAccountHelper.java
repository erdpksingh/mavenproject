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

package com.cds.cbit.accounts.api.account.prepaid;

import com.cds.cbit.accounts.api.account.AccountCreationHelper;
import com.cds.cbit.accounts.api.account.flist.AccountInfo;
import com.cds.cbit.accounts.api.account.flist.BalanceInfo;
import com.cds.cbit.accounts.api.account.flist.BillingInfo;
import com.cds.cbit.accounts.api.account.flist.CommitCustOpcode;
import com.cds.cbit.accounts.api.account.flist.LocaleInfo;
import com.cds.cbit.accounts.api.account.flist.NameInfo;
import com.cds.cbit.accounts.api.account.flist.PaymentInfoDetail;
import com.cds.cbit.accounts.api.account.flist.Profile;
import com.cds.cbit.accounts.api.account.payload.AccountRequestBody;
import com.cds.cbit.accounts.api.account.payload.Address;
import com.cds.cbit.accounts.api.account.payload.PersonalInfo;
import com.cds.cbit.accounts.properties.AccountConfigProperties;
import com.cds.cbit.accounts.properties.PrepaidProperties;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.cds.cbit.accounts.util.UtcDateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component class will provide methods which create sections like balance,billing, invoice,
 * payment and so on of CUST_COMMIT opcode of BRM.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0
*/
@Component
public class PrepaidAccountHelper {
  
  @Autowired
  private AccountConfigProperties account;
  
  @Autowired
  private AccountCreationHelper helper;
  
  @Autowired
  private PrepaidProperties prepaid;
  
  /**
   * The method will create CREATE_CUST_COMMIT opcode of BRM with requested information.
   * 
   * @param: reqBody - @Mandatory- Body section of account creation request.
  */
  public CommitCustOpcode createCustCommitOpcode(
                   final AccountRequestBody reqBody,final String endT) throws ParseException {
    
    final CommitCustOpcode custcommitOpcode =  
          CommitCustOpcode.builder().descr("Base").flags("0")
                           .poid("0.0.0.1 /account -1 0").profiles(createProfiles(reqBody))
                           .txnFlags("2").accountInfo(createAccountInfo())
                           .balInfo(createBalanceInfo()).locales(getLocale()).name("")
                           .nameInfo(createNameInfo(reqBody.getPersonalInfo(),
                                                    reqBody.getPersonalInfo().getAddress()))
                           .payInfo(createPaymentInfo()).billInfo(createBillingInfo())
                           .build();
    if (endT.length() > 0) {
      custcommitOpcode.setEndT(UtcDateUtil.convertStringToTimeStamp(endT));
    }
    return custcommitOpcode;
  } // End of createCustCommitOpcode method.
  
  /**
   * The method will create locale section of the opcode.
  */
  protected LocaleInfo getLocale() {
    
    return LocaleInfo.builder().elem("0").locale(account.getLocale()).build();
  } // End of creating locale information.
  
  /**
   * The method will create payment information section of the opcode.
  */
  public PaymentInfoDetail createPaymentInfo() {
    
    return PaymentInfoDetail.builder().elem("0").name(prepaid.getName())
             .poid(ConstantsUtil.PAYINFO_PREPAID).payType(prepaid.getPayType()).flags("1").build();
    
  } // End of creating inherited section of payment information from the request payload.
  
  /**
   * The method will create inherited section of payment information with the inputs provided 
   * in request for prepaid account.
   * 
   * @param: requestBody-  @Mandatory - Body section of the account creation request.
  */
  public List<Profile> createProfiles(final AccountRequestBody requestBody) {
    
    final List<Profile> profiles = new ArrayList<>();
    profiles.add(helper.createProfileInfo(requestBody));
    profiles.add(helper.createSubscriberProfile(requestBody.getPersonalInfo()));
    return profiles;
    
  } // End of creating inherited section of payment information from the request payload.
  
  /**
   * The method will create naming information of the account with the inputs provided in request.
   * 
   * @param: personal-  @Mandatory - Customer personal information.
   * @param: address-   @Mandatory - Customer address details.
  */
  public NameInfo createNameInfo(final PersonalInfo personal,final Address address) {

    final NameInfo nameInfo = NameInfo.builder().elem("1").lastName(".")
                   .firstName(personal.getName().getFirst())
                   .contactType(account.getContactType())
                   .emailAddress(personal.getEmail()).country(address.getCountry())
                   .zip(address.getPostalCode()).state(address.getState()).city(address.getCity())
                   .address(address.getStreet())
                   .company(personal.getDisplayName())
                   .salutation(personal.getName().getSalutation()).build();

    Optional.ofNullable(personal.getName().getMiddle()).ifPresent(nameInfo::setMiddleName);
    Optional.ofNullable(personal.getName().getLast()).ifPresent(nameInfo::setLastName);
    
    return nameInfo;
  } // End of creating customer naming information from the request payload.
  
  /**
   * The method will create balance information of the opcode.
  */
  public BalanceInfo createBalanceInfo() {
    
    return BalanceInfo.builder().elem("0").poid(ConstantsUtil.BALGROUP)
                  .billInfo(BillingInfo.builder().build()).name("Balance Group<Account>").build();
    
  } // End of creating customer balance information from the request payload.
  
  /**
   * The method will create billing information of the opcode.
  */
  public BillingInfo createBillingInfo() {
    
    return BillingInfo.builder().elem("0").poid(ConstantsUtil.BILLINFO)
                                      .billInfoId(account.getBillId())
                                      .payInfo(PaymentInfoDetail.builder().build())
                                      .balInfo(BalanceInfo.builder().build())
                                      .payType(prepaid.getPayType()).build();
  } // End of creating customer billing information from the request payload.
  
  /**
   * The method will create accounting information with the inputs provided in request.
  */
  public AccountInfo createAccountInfo() {
    
    return AccountInfo.builder().elem("0").poid(ConstantsUtil.ACCOUNT_POID)
                      .balInfo(BalanceInfo.builder().build()).businessType("1")
                      .currency(prepaid.getCurrency()).build();
  } // End of creating customer accounting information from the request payload.
} // End of PrepaidAccountHelper, which create various sections of prepaid CUST_COMMIT opcode. 