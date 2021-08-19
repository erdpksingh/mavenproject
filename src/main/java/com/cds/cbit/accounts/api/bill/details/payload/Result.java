/*
 * Copyright (C) 2019 Covalense Technologies 
 *
 * Licensed under the Covalense CBIT, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.covalensedigital.com
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
*/

package com.cds.cbit.accounts.api.bill.details.payload;

import com.cds.cbit.accounts.api.account.balance.payload.CustomerBillDetails;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents result code of the response payload.
 * 
 * @author Anuradha Manda.
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {

  /** Generated serialVersionUID. */
  private static final long serialVersionUID = 3820049397603657039L;

  /** Generated serialVersionUID. */

  private List<CustomerBillDetails> billDetails; // Details of bill.

  private String accountNumber; // Customer account number.

  private String baAccountNumber;
  
  private String caAccountNumber;

} // End of class Result.