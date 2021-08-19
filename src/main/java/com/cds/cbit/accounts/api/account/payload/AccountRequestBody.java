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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field mapping to account creation request JSON.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestBody implements Serializable {
  
  private static final long serialVersionUID = 704252261073232863L;

  @Valid
  @NotNull(message = "Attribute 'accountInfo' not present in the account request.")
  private AccountInfo accountInfo;  // Account information section.

  @Valid
  @NotNull(message = "Attribute 'personalInfo' not present in the account request.")
  private PersonalInfo personalInfo;  // Customer personal information section.

  private List<AdvancePayments> advancePayments; // Advance payments if applicable.

  @Valid
  @NotNull(message = "Attribute 'orderInfo' not present in the account request.")
  private OrderInfo orderInfo;  // Customer order information section.

  @Valid
  @NotNull(message = "Attribute 'planInfo' not present in the account request.")
  private PlanInfo planInfo;  // Customer plan subscription section.

  private PaymentInfo paymentInfo; // Customer payment information section.
} // End of AccountRequestBody