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
 * The POJO provide field mapping to PortinInfo section of account creation request JSON.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortinInfo implements Serializable {
  
  private static final long serialVersionUID = 6852303866022148590L;

  @NotNull(message = "Attribute 'date' not present in PortinInfo section of the account request..")
  protected String date;  // Portin date.

  @NotNull(message = "Attribute 'donor' not present in PortinInfo section of the account request..")
  protected String donor;  // Portin donor.

  @NotNull(message = "Attribute 'msisdn' not present in PortinInfo section of"
                     + " the account request..")
  protected String msisdn; // Portin phone number.
} //End of PortinInfo