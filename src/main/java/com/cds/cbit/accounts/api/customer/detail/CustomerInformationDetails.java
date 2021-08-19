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

package com.cds.cbit.accounts.api.customer.detail;

import com.cds.cbit.accounts.api.customer.detail.payload.Address;
import com.cds.cbit.accounts.api.customer.detail.payload.Name;
import com.cds.cbit.accounts.api.customer.detail.payload.OrderInfo;
import com.cds.cbit.accounts.api.customer.detail.payload.PersonalInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.fields.FldActgCycleDom;
import com.portal.pcm.fields.FldAddress;
import com.portal.pcm.fields.FldCity;
import com.portal.pcm.fields.FldCompany;
import com.portal.pcm.fields.FldCountry;
import com.portal.pcm.fields.FldEmailAddr;
import com.portal.pcm.fields.FldExtraResults;
import com.portal.pcm.fields.FldFirstName;
import com.portal.pcm.fields.FldLastName;
import com.portal.pcm.fields.FldMiddleName;
import com.portal.pcm.fields.FldNameinfo;
import com.portal.pcm.fields.FldResults;
import com.portal.pcm.fields.FldSalutation;
import com.portal.pcm.fields.FldState;
import com.portal.pcm.fields.FldZip;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * The component will provide methods to fetch customer information as part of GetCustomerDetail
 * API response.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public final class CustomerInformationDetails {
  
  private static final String NAME_INFO = "nameDetail";
  
  private CustomerInformationDetails() {
  } // End of private constructor.
  
  /**
   * The method will create customer information like personal, address, billing and so on as part
   * of GetCustomerDetails API response.
   * 
   * @param: responseDetails - @MAndatory - Inputs required to prepare Customer details response.
   * @param: profileMap      - @MAndatory - Customer profile information.
   * @param: mapper          - @MAndatory - Jackson Object mapper.
  */
  public static Map<String, Object> createCustomerDetailInformation(
            final Map<String, Object> responseDetails,final Map<String,Object> profileMap,
                                         final ObjectMapper mapper) throws EBufException {
    
    final PersonalInfo personalInfo = mapper.convertValue(profileMap,PersonalInfo.class);
    final Address address = getAddress(responseDetails,personalInfo);
    personalInfo.setAddress(address);
   
    final Name name = getName(responseDetails);
    personalInfo.setName(name);
    
    final FList namedetail = (FList) responseDetails.get(NAME_INFO);
    String displayName = "";
    String email = "";
    String billingDate = "";
    if (namedetail.hasField(FldResults.getInst())) {
      final FList nameFlist = namedetail.get(FldResults.getInst()).getValues().get(0)
                                                    .get(FldNameinfo.getInst()).getValues().get(0);
      displayName  = nameFlist.get(FldCompany.getInst());
      email  = nameFlist.get(FldEmailAddr.getInst());
      billingDate = namedetail.get(FldExtraResults.getInst()).getValues().get(0)
                                                              .get(FldActgCycleDom.getInst()) + "";
    }
    personalInfo.setEmail(email);
    personalInfo.setDisplayName(displayName);
    personalInfo.setBillingDate(billingDate);
    
    final Map<String, Object> infoMap = new ConcurrentHashMap<>();
    infoMap.put("personalInfo", personalInfo);
    
    infoMap.put("paymentFlag", true);
    infoMap.put("paymentInfo", personalInfo.getPaymentInfo());
   
    final OrderInfo orderInfo = new OrderInfo();
    orderInfo.setNumber(personalInfo.getOrderInfo().getOrderNumber());
    orderInfo.setCheckoutDate(personalInfo.getOrderInfo().getCheckoutDate());
    orderInfo.setReferralCode(personalInfo.getOrderInfo().getReferralCode());
    
    personalInfo.setOrderInfo(orderInfo);
    infoMap.put("orderInfo", personalInfo.getOrderInfo());
    return infoMap;
  }

  /** Method to prepare address information from billing system.  */
  private static Address getAddress(
          final Map<String,Object> responseDetails,final PersonalInfo personalInfo) 
                                                                            throws EBufException {
    final FList namedetail = (FList) responseDetails.get(NAME_INFO);
    if (namedetail.hasField(FldResults.getInst())) {
      final FList nameFlist = namedetail.get(FldResults.getInst()).getValues().get(0)
          .get(FldNameinfo.getInst()).getValues().get(0);
      
      return Address.builder().city(nameFlist.get(FldCity.getInst()))
          .country(nameFlist.get(FldCountry.getInst())).postalCode(nameFlist.get(FldZip.getInst()))
              .state(nameFlist.get(FldState.getInst())).street(nameFlist.get(FldAddress.getInst()))
                                             .buildingNo(personalInfo.getAddress().getBuildingNo())
                                                       .floor(personalInfo.getAddress().getFloor())
                                                .unit(personalInfo.getAddress().getUnit()).build();
    } else {
      return Address.builder().build();
    }
  } // End of getAddress method.

  /** Method to prepare name information from billing system. */
  private static Name getName(final Map<String, Object> responseDetails) throws EBufException {
    
    final FList namedetail = (FList) responseDetails.get(NAME_INFO);
    if (namedetail.hasField(FldResults.getInst())) {
      
      final FList nameFlist = 
            namedetail.get(FldResults.getInst()).getValues().get(0)
                                                .get(FldNameinfo.getInst()).getValues().get(0);
      return Name.builder().first(nameFlist.get(FldFirstName.getInst()))
                           .last(nameFlist.get(FldLastName.getInst()))
                           .middle(nameFlist.get(FldMiddleName.getInst()))
                           .salutation(nameFlist.get(FldSalutation.getInst())).build();
    } else {
      return Name.builder().build();
    }
  } // End of getName method.
} // End of CustomerInformationDetails.