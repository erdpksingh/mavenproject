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
import com.cds.cbit.accounts.api.customer.detail.payload.Ids;
import com.cds.cbit.accounts.api.customer.detail.payload.OrderInfo;
import com.cds.cbit.accounts.api.customer.detail.payload.PaymentInfo;
import com.cds.cbit.accounts.api.customer.detail.payload.PlanInfo;
import com.cds.cbit.accounts.api.customer.detail.payload.ProfileData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.fields.FldContentCategoryName;
import com.portal.pcm.fields.FldName;
import com.portal.pcm.fields.FldNoteStr;
import com.portal.pcm.fields.FldProfileDataArray;
import com.portal.pcm.fields.FldResults;
import com.portal.pcm.fields.FldValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will help to prepare the response for customer detail API,
 * It will prepare all the details related to profile of the customer from billing system .
 * 
 * @author Meghashree Udupa.
 * @version 1.0
 */
@Component
public class CustomerDetailHelper {

  public static final String NAME_INFO = "nameDetail";
  
  @Autowired
  private ObjectMapper mapper;
  
  private final CustomerPlanDetails planDetails;
  
  public CustomerDetailHelper(final CustomerPlanDetails planDetails) {
    
    this.planDetails = planDetails;
  } // End of constructor injection.
  
  /**
   * The method getProfileDetail will prepare all the required response for customer detail
   * API from the billing system.
   * 
   * @param: responseDetails : map which contains all the details.
   * @param: nameFlist : FLIST which contains name information.
   * @param: address :  POJO which contains address information.
   * @param: name : POJO which contains name information.
  */
  public Map<String, Object> getProfileDetail(final Map<String, Object> responseDetails) 
                                                                        throws EBufException {
    
    final FList profiledetailsFlist = (FList) responseDetails.get("profileDetails");
     
    final List<Ids> idsList = new ArrayList<>();
    final List<FList> detailList = 
                      profiledetailsFlist.get(FldResults.getInst()).getValues().get(0)
                                                  .get(FldProfileDataArray.getInst()).getValues();

    final Map<String,Object> profileMap = new ConcurrentHashMap<>();
    final List<ProfileData> profileDataList = new ArrayList<>();
    
    for (final FList deals : detailList) {
      
      final ProfileData profileData = new ProfileData();
      profileData.setName(deals.get(FldName.getInst()));
      profileData.setValue(deals.get(FldValue.getInst()));
    
      profileDataList.add(profileData);
      profileMap.put(profileData.getName(), profileData.getValue());
      
      if (deals.get(FldContentCategoryName.getInst()).contains("ids")) {
        final Ids ids = new Ids();
        ids.setType(deals.get(FldNoteStr.getInst()));
        ids.setName(deals.get(FldName.getInst()));
        ids.setValue(deals.get(FldValue.getInst()));
        idsList.add(ids);
      }
      profileMap.put("ids", idsList);
      final Address address = mapper.convertValue(profileMap,Address.class);
      profileMap.put("address", address);
      
      final OrderInfo orderInfo = mapper.convertValue(profileMap,OrderInfo.class);
      profileMap.put("orderInfo", orderInfo);
       
      final PaymentInfo paymentInfo = mapper.convertValue(profileMap,PaymentInfo.class);
      profileMap.put("paymentInfo", paymentInfo);
      
      profileMap.values().removeIf(Objects::isNull);
      
    } // End of looping through deals.
    return CustomerInformationDetails.createCustomerDetailInformation(
                                                          responseDetails, profileMap,mapper);
  } // End of getProfileDetail method.
  
  protected PlanInfo getPlanDetails(
            final Map<String, Object> responseDetails, final Poid accountPoid) 
                                                             throws EBufException, JAXBException {
    return planDetails.getAddonDetail(responseDetails, accountPoid);
  } // End of getPlanDetails method.
} // End of CustomerDetailProfiler, which creates information for customer details response.