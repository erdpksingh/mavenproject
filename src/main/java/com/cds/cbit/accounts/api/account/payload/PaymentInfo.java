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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field mapping to PaymentInfo section of account creation request JSON.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo implements Serializable {
  
  private static final long serialVersionUID = 4326281316674816473L;

  protected String bank;  // Customer bank.

  protected String cardType; // Customer card type.

  protected String mid; // Bank mid.

  protected String lastDigits; // LAst digits of the card.

  protected String bankName; // Customer bank name.

  protected String gateway; // Payment gateway name.

  protected String token; // Access token.
} //End of PaymentInfo