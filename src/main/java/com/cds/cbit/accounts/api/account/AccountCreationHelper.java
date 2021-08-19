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

import com.cds.cbit.accounts.api.account.flist.AccountInfo;
import com.cds.cbit.accounts.api.account.flist.BalanceInfo;
import com.cds.cbit.accounts.api.account.flist.BillingInfo;
import com.cds.cbit.accounts.api.account.flist.InherittedInfo;
import com.cds.cbit.accounts.api.account.flist.InvoiceInfo;
import com.cds.cbit.accounts.api.account.flist.NameInfo;
import com.cds.cbit.accounts.api.account.flist.PaymentInfoDetail;
import com.cds.cbit.accounts.api.account.flist.Profile;
import com.cds.cbit.accounts.api.account.flist.ProfileData;
import com.cds.cbit.accounts.api.account.flist.ProfileInheritInfo;
import com.cds.cbit.accounts.api.account.flist.SubscriberPreference;
import com.cds.cbit.accounts.api.account.payload.AccountRequestBody;
import com.cds.cbit.accounts.api.account.payload.Address;
import com.cds.cbit.accounts.api.account.payload.PersonalInfo;
import com.cds.cbit.accounts.properties.AccountConfigProperties;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
public class AccountCreationHelper {
  
  @Autowired
  private AccountConfigProperties account;
  
  private final AccountProfiles profiles;
  private final AccountServicesHelper helper;
  
  /** Constructor injection. **/
  public AccountCreationHelper(
         final AccountProfiles profiles,final AccountServicesHelper helper) {
    
    this.profiles = profiles;
    this.helper = helper;
  } // End of constructor injection.
  
  /**
   * The method will create balance information of the account with the inputs provided in request.
   * @throws- EBufException 
  */
  public BalanceInfo createBalanceInfo(final FList planReadOp) throws EBufException {
    
    return BalanceInfo.builder().elem("0").poid(ConstantsUtil.BALGROUP)
                                .billInfo(BillingInfo.builder().build())
                                .limits(helper.createLimitsArray(planReadOp)).build();
    
  } // End of creating customer balance information from the request payload.
  
  /**
   * The method will create billing information of the account with the inputs provided in request.
   * 
   * @param: billingDate-  @Mandatory - Customer billing date.
  */
  public BillingInfo createBillingInfo(final String billingDate) {
    
    return BillingInfo.builder().elem("0").poid(ConstantsUtil.BILLINFO)
                                      .billInfoId(account.getBillId())
                                      .payInfo(PaymentInfoDetail.builder().build())
                                      .balInfo(BalanceInfo.builder().build()).dom(billingDate)
                                      .payType(account.getPayType()).build();
  } // End of creating customer billing information from the request payload.
  
  /**
   * The method will create invoice information of the account with the inputs provided in request.
   * 
   * @param: personal-  @Mandatory - Customer personal information.
   * @param: address-   @Mandatory - Customer address details.
  */
  private InvoiceInfo createInvoice(final PersonalInfo personal,final Address address) {
    
    return InvoiceInfo.builder().elem("0").address(address.getBuildingNo())
                      .deliveryPreference(account.getDeliveryPreference()).invInstr("")
                      .name(personal.getName().getLast()).invoiceTerms(account.getInvTerms())
                      .emailAddress(personal.getEmail()).country(address.getCountry())
                      .city(address.getCity()).state(address.getState())
                      .deliveryDescription(personal.getName().getFirst()).build();
  } // End of creating customer invoice information from the request payload.
  
  /**
   * The method will create accounting information with the inputs provided in request.
  */
  public AccountInfo createAccountInfo() {
    
    return AccountInfo.builder().elem("0").poid(ConstantsUtil.ACCOUNT_POID)
                      .balInfo(BalanceInfo.builder().build()).businessType("1")
                      .currency(account.getCurrency()).build();
  } // End of creating customer accounting information from the request payload.
  
  /**
   * The method will create invoice information as part of customer account creation with the
   * following information.
   * 
   * @param: personal-  @Mandatory - Customer personal information.
   * @param: address-   @Mandatory - Customer address details.
  */
  private InherittedInfo createInheriInfo(final PersonalInfo personal,final Address address) {
    
    return InherittedInfo.builder().invoiceInfo(createInvoice(personal,address)).build();
  } // End of creating payment section inherited information from the request payload.
  
  /**
   * The method will create inherited section of payment information with the inputs provided 
   * in request.
   * 
   * @param: personal-  @Mandatory - Customer personal information.
   * @param: address-   @Mandatory - Customer address details.
  */
  public PaymentInfoDetail createPaymentInfo(final PersonalInfo personal,final Address address) {
    
    final InherittedInfo inherited = createInheriInfo(personal,address);
    
    return PaymentInfoDetail.builder().elem("0").name(account.getPaymentName())
                      .poid(ConstantsUtil.PAYINFO_INVOICE).inheritInfo(inherited)
                      .payType(account.getPayType()).build();
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
   * The method will create profile information of the account with the inputs provided in request.
   * 
   * @param: accountReq-  @Mandatory - Account creation payload.
  */
  public Profile createProfileInfo(final AccountRequestBody accountReq) {
    
    final List<ProfileData> profileData = new ArrayList<>();
    
    final AtomicInteger ordinal = new AtomicInteger(0);
    profiles.createAccountProfiles(accountReq)
            .forEach((k,v) -> {
              final String[] data = k.split("_");
              profileData.add(ProfileData.builder().elem(ordinal.getAndIncrement())
                                       .name(data[0]).value(v).category(data[1]).build());
            });
    profiles.getKycProfile(accountReq.getPersonalInfo().getIds()).forEach((k,v) -> {
      final String[] remarks = v.split(":");
      final String[] data = k.split("_");
      final ProfileData profile = ProfileData.builder()
                    .elem(ordinal.getAndIncrement())
                    .name(data[0]).value(remarks[0])
                    .remarks(remarks[1]).category(data[1]).build();
      profileData.add(profile);
    });
    
    final ProfileInheritInfo inheritInfo =
          ProfileInheritInfo.builder().profileData(profileData).build();
    
    return Profile.builder().elem("0").inheritInfo(inheritInfo)
                                      .profileObj(ConstantsUtil.FLAT_PROFILE_OBJ).build();
  } // End of creating customer profile information from the request payload.
  
  /**
   * The method will create subscriber section of the account with the inputs provided in request.
   * 
   * @param: personal-  @Mandatory - Customer personal information.
  */
  public Profile createSubscriberProfile(final PersonalInfo personal) {
    
    final List<SubscriberPreference> subscriber = new ArrayList<>();
    
    subscriber.add(SubscriberPreference.builder().elem("0").name(account.getCustName())
                   .value(personal.getName().getFirst()).subscriberId("1").build());
    subscriber.add(SubscriberPreference.builder().elem("1").name(account.getCustEmail())
                   .value(personal.getEmail()).subscriberId("2").build());
    
    final ProfileInheritInfo inheritInfo =
          ProfileInheritInfo.builder().subscriberData(subscriber).build();
    
    final StringBuilder profile = 
          new StringBuilder("0.0.0.1 ").append(ConstantsUtil.SUBSCRIBER).append(" -1 0");
    
    return Profile.builder().elem("1").inheritInfo(inheritInfo)
                                      .profileObj(profile.toString()).build();
  } // End of creating subscriber information from the request payload.
} // End of AccountCreationHelper, which create various sections of CUST_COMMIT opcode. 