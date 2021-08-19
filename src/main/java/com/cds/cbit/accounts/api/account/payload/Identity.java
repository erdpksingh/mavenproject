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
 * The POJO provide field mapping to Ids section of account creation request JSON.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Identity implements Serializable {
  
  private static final long serialVersionUID = -2183216480106447746L;

  @NotNull(message = "Attribute 'name' not present in 'ids' section of the account request.")
  protected String name; // KYC Identity name

  @NotNull(message = "Attribute 'type' not present in 'ids' section of the account request.")
  protected String type; // KYC identity type.

  @NotNull(message = "Attribute 'value' not present in 'ids' section of the account request.")
  protected String value; // KYC identity value.
} //End of Identity