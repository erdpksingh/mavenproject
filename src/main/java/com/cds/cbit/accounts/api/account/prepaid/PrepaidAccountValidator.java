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

import com.cds.cbit.accounts.api.account.payload.PlanInfo;
import com.cds.cbit.accounts.util.UtcDateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List; 
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * The component will provide methods which validates prepaid related information of the
 * account creation request.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class PrepaidAccountValidator {
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  /**
   * The method will verify whether given plan name in the account creation request is normal/first
   * usage activated.
   * 
   * @param: planName:  @Mandatory - Plan name given in the request.
  */
  public String verifyPlanType(final String planName) {
    
    final StringBuilder query = new StringBuilder(
          "select distinct dp.PURCHASE_START_DETAILS from plan_t p, PLAN_SERVICES_DEALS_T"
          + " psd, deal_t d, deal_products_t dp where p.poid_id0 = psd.obj_id0 and "
          + " d.poid_id0 = dp.obj_id0 and d.permitted='/service/telco/gsm/telephony' and "
          + " psd.deal_obj_id0 = dp.obj_id0 and p.name = '");
    
    query.append(planName).append("'"); 
    
    final List<Map<String, Object>> rows = jdbcTemplate.queryForList(query.toString());
    final String count = rows.get(0).get("PURCHASE_START_DETAILS").toString();
    return  Integer.parseInt(count) == 3 ? "firstUsage" : "normalPlan";
  } // End of verifyPlanType method.
  
  /**
   * The method will verify whether given plan has start date in the account creation request.If
   * it present and plan type is not firstUsage, then the method will find Unix timestamp of it 
   * and return.
   * 
   * @param: planInfo:  @Mandatory - Plan information section of the request.
   * @param: planName:  @Mandatory - Plan name given in the request.
  */
  public Long getPlanEndT(final PlanInfo planInfo,final String planType) throws ParseException {
    
    Long planEndT = 0L;
    final Optional<String> startDate = Optional.ofNullable(planInfo.getStartDate());
    
    if (!"firstUsage".equals(planType) && startDate.isPresent()) {
      
      planEndT = UtcDateUtil.convertStringToTimeStamp(startDate.get());
    } // End of setting plan end date.
    return planEndT;
  } // End of getPlanEndT method.
  
  /**
   * The method will verify whether given plan has start date in the account creation request.If
   * it present and plan type is not firstUsage, then the method will find Unix timestamp of it 
   * and return.
   * 
   * @param: planInfo:  @Mandatory - Plan information section of the request.
   * @param: planName:  @Mandatory - Plan name given in the request.
  */
  public String getPlanAmountFromBrm(final PlanInfo planInfo,final String planName) {
    
    final StringBuilder amountVal = new StringBuilder();
    final StringBuilder query = new StringBuilder(
          "select sum(rbi.scaled_amount) as AMOUNT from DEAL_PRODUCTS_T dp, "
          + " rate_plan_t rp, rate_t r, RATE_BAL_IMPACTS_T rbi where "
          + " dp.product_obj_id0 = rp.product_obj_id0 and rp.EVENT_TYPE "
          + " like '%cycle_forward%' and rp.poid_id0 = r.RATE_PLAN_OBJ_ID0 "
          + " and r.poid_id0 = rbi.obj_id0 and rbi.element_id = 360 and dp.obj_id0 in "
          + " ((select ps.deal_obj_id0 from plan_t pl, PLAN_SERVICES_DEALS_T ps "
          + " where pl.poid_id0 = ps.obj_id0 and pl.name = '");
    query.append(planName).append("')");
    
    final String amountQuery = addComponentDealsToQuery(planInfo,query);
    final List<Map<String, Object>> rows = jdbcTemplate.queryForList(amountQuery);
    
    if (!rows.isEmpty() && rows.get(0).get("AMOUNT") != null) {
      amountVal.append(rows.get(0).get("AMOUNT"));
    } // End of verifying rows.
    return amountVal.toString();
  } // End of getPlanAmountFromBrm method.
  
  /**
   * The method verifies whether component section presented in the request or not. If present
   * then the method will loop through all the deals of it and append them to the query.
   * 
   * @param: planInfo:  @Mandatory - Plan information section of the request.
   * @param: query   :  @Mandatory - Query of usage amount.
  */
  private String addComponentDealsToQuery(final PlanInfo planInfo,final StringBuilder query) {
    
    Optional.ofNullable(planInfo.getComponents()).ifPresent(component -> {
      final List<String> deals = new ArrayList<>();
      component.forEach(c -> deals.add(c.getDealCode()));
    
      if (!deals.isEmpty()) {
        
        query.append(" union (select poid_id0 from deal_t where code in (");
        for (int i = 0; i < deals.size();i++) {
          
          query.append('\'' + deals.get(i) + '\'');
          if (i < deals.size() - 1) {
            query.append(',');
          }
        } // End of looping through deals.
        query.append(")))");
      } // End of components deal check.
    }); // End of components existence check.
    return query.toString();
  } // End of addComponentDealsToQuery method.
} // End of PrepaidAccountValidator, which validate prepaid plan information of the request.