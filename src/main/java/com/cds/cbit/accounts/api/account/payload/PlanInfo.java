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

package com.cds.cbit.accounts.api.account.payload;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field mapping to PlanInfo section of account creation request JSON.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanInfo implements Serializable {
  
  private static final long serialVersionUID = -8595911045273240348L;

  @NotNull(message = "Attribute 'name' not present in 'planInfo' section of the account request.")
  protected String name; // Base plan name.
  
  protected double price;  // Base plan price.
  
  protected String startDate;
  
  private List<Components> components;  // Components to be purchased.

  private List<Addons> addons;  // AddOns to be purchased.

  protected DiscountDetails discountDetails;  // Discounts if applicable.
  
  protected OverrideDetail overrideDetail;
} //End of PlanInfo