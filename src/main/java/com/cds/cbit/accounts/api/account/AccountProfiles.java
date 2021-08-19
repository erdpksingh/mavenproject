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

import com.cds.cbit.accounts.api.account.payload.AccountInfo;
import com.cds.cbit.accounts.api.account.payload.AccountRequestBody;
import com.cds.cbit.accounts.api.account.payload.Identity;
import com.cds.cbit.accounts.api.account.payload.OrderInfo;
import com.cds.cbit.accounts.api.account.payload.PaymentInfo;
import com.cds.cbit.accounts.api.account.payload.PersonalInfo;
import com.cds.cbit.accounts.api.account.payload.PortinInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * The component provide methods which will create various information required for profile section
 * of CUST_COMMIT opcode. These include, KYC, portin, payment related information and so on.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class AccountProfiles {
   
  /** 
   * The method will create customer profile information with the given input in the request. The
   * method uses internal methods to gather complete information required for profile creation.
   * 
   * @param: account -  @Mandatory - Account create request body section.
  */
  public Map<String, String> createAccountProfiles(final AccountRequestBody account) {
    
    final Map<String,String> profiles = new ConcurrentHashMap<>();
    final AccountInfo accountInfo = account.getAccountInfo();
    profiles.putAll(
             createProfileInfo(account.getPersonalInfo(),account.getPaymentInfo(),
                                                   account.getOrderInfo(),accountInfo));
    
    Optional.ofNullable(accountInfo.getPortinInfo())
                                 .ifPresent(p ->  profiles.putAll(getPortinProfile(p)));
    return profiles;
  } // End of creating profile section of CUST_COMMIT_OPCODE.
   
  /**
   * The method will create and return a map which contains various fields need to keep in profiles.
   * 
   * @param: personal-    @Mandatory - Customer personal information.
   * @param: payment-     @Mandatory - Customer payment details.
   * @param: order-       @Mandatory - Customer order information.
   * @param: accountInfo- @Mandatory - Customer account details.
  */
  public Map<String, String> 
         createProfileInfo(final PersonalInfo personal,final PaymentInfo payment,
                          final OrderInfo order,final AccountInfo accountInfo) {
    
    final Map<String,String> profiles = new ConcurrentHashMap<>();
    
    profiles.put("dateOfBirth_personalInfo", personal.getDateOfBirth());
    profiles.put("nationality_personalInfo", personal.getNationality());
    profiles.put("autoBoost_accountInfo",   accountInfo.getAutoBoost());
    profiles.put("subType_accountInfo",     accountInfo.getSubType());
    profiles.put("category_accountInfo",    accountInfo.getCategory());
    profiles.put("orderNumber_orderInfo", order.getNumber());
    profiles.put("checkoutDate_orderInfo",order.getCheckoutDate());
    profiles.put("referralCode_orderInfo",order.getReferralCode());
    profiles.putAll(getStreetAddress(personal));
    profiles.putAll(getOptionalFields(personal,accountInfo));
    
    Optional.ofNullable(payment).ifPresent(p ->  profiles.putAll(getPaymentProfile(p)));
    
    return profiles;
  } // End of generic profile creation.
  
  /** Method to create profile section optional fields. **/
  private Map<String, String> getOptionalFields(
                              final PersonalInfo personal,final AccountInfo accountInfo) {
    
    final Map<String,String> profiles = new ConcurrentHashMap<>();
    
    profiles.put("accountType_accountInfo",accountInfo.getAccountType());
    profiles.put("invoiceLangPref_accountInfo",accountInfo.getInvoiceLangPref());
    profiles.put("segment_accountInfo",accountInfo.getSegment());
    profiles.put("altContact_personalInfo",personal.getAltContact());
    profiles.put("custConsent_accountInfo",accountInfo.getCustConsent());
    profiles.values().removeIf(Objects::isNull);
    return profiles;
  } // End of creating optional fields of profile section.
  
  /** Method to Create street address. **/
  private Map<String, String> getStreetAddress(final PersonalInfo personal) {

    final Map<String,String> profiles = new ConcurrentHashMap<>();
    profiles.put("buildingNo_address", personal.getAddress().getBuildingNo());
    profiles.put("floor_address", personal.getAddress().getFloor());
    profiles.put("unit_address", personal.getAddress().getUnit());
    
    return profiles;
  } // End of getStreetAddress.
  
  /** Method to create KYC profile. **/
  public Map<String, String> getKycProfile(final List<Identity> identity) {
    
    final Map<String,String> idMap = new ConcurrentHashMap<>();
    
    // Changes done here to convert string into string builder.
    identity.forEach(i -> {
      final StringBuilder key = new StringBuilder(i.getName()).append("_ids");
      final StringBuilder value = new StringBuilder(i.getValue()).append(':').append(i.getType());
      idMap.put(key.toString(),value.toString());
    });
    
    return idMap;
  } // End of creating KYC profile.
  
  /** Method to create portin profile. **/
  public Map<String,String> getPortinProfile(final PortinInfo portin) {
    
    final Map<String,String> portinMap = new ConcurrentHashMap<>();
    portinMap.put("portinDate_portinInfo",portin.getDate());
    portinMap.put("portinNumber_portinInfo",portin.getMsisdn());
    portinMap.put("portinDonor_portinInfo",portin.getDonor());
  
    return portinMap;
  } // End of creating portin profile.
  
  /** Method to create payment profile. **/
  private Map<String, String> getPaymentProfile(final PaymentInfo payment) {

    final Map<String,String> paymentMap = new ConcurrentHashMap<>();
    
    Optional.ofNullable(payment.getToken()).ifPresent(p -> 
                                            paymentMap.put("token_paymentInfo",payment.getToken()));
    Optional.ofNullable(payment.getMid()).ifPresent(p ->  
                                                paymentMap.put("mid_paymentInfo",payment.getMid()));
    Optional.ofNullable(payment.getLastDigits()).ifPresent(p -> 
                                  paymentMap.put("lastDigits_paymentInfo",payment.getLastDigits()));
    Optional.ofNullable(payment.getCardType()).ifPresent(p -> 
                                      paymentMap.put("cardType_paymentInfo",payment.getCardType()));
    Optional.ofNullable(payment.getBankName()).ifPresent(p ->  
                                      paymentMap.put("bankName_paymentInfo",payment.getBankName()));
    Optional.ofNullable(payment.getBank()).ifPresent(p -> 
                                              paymentMap.put("bank_paymentInfo",payment.getBank()));
    Optional.ofNullable(payment.getGateway()).ifPresent(p ->  
                                        paymentMap.put("gateway_paymentInfo",payment.getGateway()));
    
    return paymentMap;
  } // End of creating payment profile.
} // End of AccountProfiles, which create profile section of CUST_COMMIT opcode.