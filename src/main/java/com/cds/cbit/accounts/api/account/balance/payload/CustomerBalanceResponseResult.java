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

package com.cds.cbit.accounts.api.account.balance.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents "result" section of the customer balance API response.
 * 
 * @author Anuradha Manda.
 * @version 1.0
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerBalanceResponseResult implements Serializable {

  /** Generated serialVersionUID. */
  private static final long serialVersionUID = 3820049397603657039L;

  private List<CustomerBillDetails> billDetails; // Details of bill.

  private String accountNumber; // Customer account number.

  private String baAccountNumber;
  
  private String caAccountNumber;
  
  private List<CustomerBalance> balance;
  
  private List<AdditionalBalance> additionalBalance;
  
  private CustomerPlanInfo planDetail;
} // End of CustomerBalanceResponseResult.