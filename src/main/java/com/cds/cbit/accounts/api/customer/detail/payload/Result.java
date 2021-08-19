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

package com.cds.cbit.accounts.api.customer.detail.payload;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field mapping to public class Result section of customer detail response JSON.
 * 
 * @author  Meghashree Udupa
 * @version 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {
  
  /** Auto generated serial version ID.   */
  private static final long serialVersionUID = 8200059923444328641L;

  private AccountInfo accountInfo;

  private PersonalInfo personalInfo;

  private PlanInfo planInfo;

  private PaymentInfo paymentInfo;
  
  private OrderInfo orderInfo;
  
} // End of Result POJO>
 