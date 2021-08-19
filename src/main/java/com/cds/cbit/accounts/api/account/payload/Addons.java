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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field mapping to Addons section of account creation request JSON.
 * 
 * @author  Saibabu Guntur
 * @Version 1.0.
 * @author  Meghashree Udupa
 * @Version 2.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Addons implements Serializable {
  
  private static final long serialVersionUID = -6044927135617197787L;

  @NotNull(message = "Attribute 'product' not present in 'planInfo.addons' section of"
                     + " the account request.")
  protected String product;  // Product to be purchased.

  @Min(value = 0L, message = "Quantity value should be positive.")
  @NotNull(message = "Attribute 'quantity' not present in 'planInfo.addons' section of"
                     + " the account request..")
  protected String quantity; // No of products to be purchased.
  
  @NotNull(message = "Attribute 'startDate' not present in 'planInfo.addons' section of "
                     + "the account request..")
  @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}T([0-9]{2}:){2}[0-9]{2}Z", 
       message = "Invalid Time format provided for startDate.")
  protected String startDate; // Deal subscription Start date .

  @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}T([0-9]{2}:){2}[0-9]{2}Z", 
       message = "Invalid Time format provided for endDate.")
  protected String endDate;  // Deal subscription end date.

  protected DiscountDetails discountDetails; // Discounts to the product if any.
  
  protected OverrideDetail overrideDetail;
} //End of Addons.