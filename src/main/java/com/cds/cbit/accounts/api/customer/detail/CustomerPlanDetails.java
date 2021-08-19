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

import com.cds.cbit.accounts.api.customer.detail.payload.Addons;
import com.cds.cbit.accounts.api.customer.detail.payload.DiscountDetail;
import com.cds.cbit.accounts.api.customer.detail.payload.OverrideDetail;
import com.cds.cbit.accounts.api.customer.detail.payload.PlanInfo;
import com.cds.cbit.accounts.factory.BillingValidationFactory;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.util.UtcDateUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.fields.FldCreatedT;
import com.portal.pcm.fields.FldDealObj;
import com.portal.pcm.fields.FldExtraResults;
import com.portal.pcm.fields.FldName;
import com.portal.pcm.fields.FldPackageId;
import com.portal.pcm.fields.FldPoid;
import com.portal.pcm.fields.FldPurchaseEndT;
import com.portal.pcm.fields.FldPurchaseStartT;
import com.portal.pcm.fields.FldResults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will provide methods to retrieve customer base plan and addOn plan details
 * as part of GetCustomerDetails API response.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class CustomerPlanDetails {
  
  @Autowired
  private BillingValidationFactory factory;

  /** Method to prepare ADDON details for the customer from billing system. */
  protected PlanInfo getAddonDetail(
            final Map<String, Object> responseDetails,final Poid accountPoid)
                                                            throws EBufException, JAXBException {

    final FList productDetail = (FList) responseDetails.get("productDetail");
    final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    final List<Addons> addonList = new ArrayList<>();

    if (productDetail.hasField(FldResults.getInst())) {

      for (int i = 0; i < productDetail.get(FldResults.getInst()).getValues().size(); i++) {
        final Addons addons = new Addons();
        final long dealObj = productDetail.get(FldResults.getInst()).getValues().get(i)
                                                                .get(FldDealObj.getInst()).getId();
        final BillingValidation dealsValidator = factory.getValidator("dealValidator");
        final FList dealsDetail = dealsValidator.validateInput(String.valueOf(
            productDetail.get(FldResults.getInst()).getValues().get(i).get(FldDealObj.getInst())));

        if (dealsDetail.hasField(FldResults.getInst())) {
          continue;
        }
        final Date prchaseEnd = productDetail.get(FldResults.getInst()).getValues().get(i)
                                                                   .get(FldPurchaseEndT.getInst());
        final DateTime purchaseTime = UtcDateUtil.convertDateToDateTime(prchaseEnd);
        final String purchaseEndTime = formatter.print(purchaseTime);

        addons.setEndDate(purchaseEndTime);
        final Date prchaseStart = productDetail.get(FldResults.getInst()).getValues().get(i)
                                                                 .get(FldPurchaseStartT.getInst());
        final DateTime prchaseTime = UtcDateUtil.convertDateToDateTime(prchaseStart);
        final String prchaseStartTime = formatter.print(prchaseTime);

        addons.setStartDate(prchaseStartTime);
        addons.setPackageId(productDetail.get(FldResults.getInst()).getValues().get(i)
                                                          .get(FldPackageId.getInst()).toString());

        for (int k = 0; k < productDetail.get(FldExtraResults.getInst()).getValues().size(); k++) {
          final long dealPoid = 
                     productDetail.get(FldExtraResults.getInst()).getValues().get(k)
                                                                 .get(FldPoid.getInst()).getId();
          if (dealObj == dealPoid) {
            addons.setProduct(productDetail.get(FldExtraResults.getInst()).getValues().get(k)
                                                                          .get(FldName.getInst()));
          }
        }
        addonList.add(addons);
      }
    }

    final PlanInfo planInfo = getPlanInformation(accountPoid,formatter);
    
    planInfo.setAddons(addonList);
    planInfo.setDiscountDetail(new DiscountDetail());
    planInfo.setOverrideDetail(new OverrideDetail());
    return planInfo;
  } // End of getAddonDetail method.
  
  /** Method to prepare basic plan details from billing system. */
  private PlanInfo getPlanInformation(final Poid accountPoid,final DateTimeFormatter formatter) 
                                                                throws EBufException,JAXBException {
    final StringBuilder createdDate = new StringBuilder();
    final StringBuilder packageId = new StringBuilder();
    final StringBuilder baseplanName = new StringBuilder();
    
    final BillingValidation basicPlanValidator = factory.getValidator("basicPlanValidator");
    final FList baseplanOp = basicPlanValidator.validateInput(String.valueOf(accountPoid));
    
    if (baseplanOp.hasField(FldResults.getInst())) {
      final FList basePlanResult = baseplanOp.get(FldResults.getInst()).getValues().get(0);
      
      final DateTime createdTime = 
            UtcDateUtil.convertDateToDateTime(basePlanResult.get(FldCreatedT.getInst()));
      createdDate.append(formatter.print(createdTime));
      packageId.append(String.valueOf(basePlanResult.get(FldPackageId.getInst())));
      baseplanName.append(baseplanOp.get(FldExtraResults.getInst())
                                    .getValues().get(0).get(FldName.getInst()));
    }
    return PlanInfo.builder().startDate(createdDate.toString())
                             .name(baseplanName.toString()).packageId(packageId.toString()).build();
  } // End of getPlanInformation method.
} // End of CustomerPlanDetails, which fetch customer plan details.