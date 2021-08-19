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

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents BillDetails information section of the customer balance response payload.
 * 
 * @author Anuradha Manda.
 * @version : 1.0
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBillDetails implements Serializable {

  private static final long serialVersionUID = 7229784751193620323L;

  private BigDecimal writeOff; // writeOff Information.

  private BigDecimal disputed; // Disputed Information.

  private BigDecimal transferred; // Transferred Information.

  private BigDecimal due; // Due Information.

  private String endDate; // EndDate Information.

  private BigDecimal adjusted; // Adjusted Information.

  private BigDecimal prevTotal; // PreviousTotal Information.

  private BigDecimal recvd; // Received Information.

  private BigDecimal currentTotal; // CurrentTotal Information.

  private String billNo; // Information about customer billNumber.

  private String startDate; // StartDate Information.
} // End of class BillDetails.