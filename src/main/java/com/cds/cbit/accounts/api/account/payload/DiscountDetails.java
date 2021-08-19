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

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field mapping to DiscountDetails section of account creation request JSON.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDetails implements Serializable {
  
  private static final long serialVersionUID = 8065765290975525946L;

  @NotNull(message = "Attribute 'dealCode' not present in 'discountDetails' section of"
                     + " the account request.")
  protected String dealCode; // Discount deal code.

  @NotNull(message = "Attribute startDate not present in 'discountDetails' section of"
                     + " the account request.")
  protected String startDate;  // Discount start date.

  protected String percentage; // Discount percentage on the product.

  protected String amount;  // Amount deducted under discounts.

  protected String endDate; // Discount start date.
} //End of DiscountDetails