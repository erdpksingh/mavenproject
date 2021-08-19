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
 * The POJO provide field mapping to Name section of account creation request JSON.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameInfo implements Serializable {
  
  private static final long serialVersionUID = -5589109882270779661L;

  protected String middle; // Customer middle name.

  protected String last;  // Customer last name.

  @NotNull(message = "Attribute 'salutation' not present/invalid in 'nameInfo' section of"
                                                                       + " the account request.")
  protected String salutation; // Customer salutation.

  @NotNull(message = "Attribute 'first' not found in 'nameInfo' section of the account request.")
  protected String first; // Customer first name.
} //End of NameInfo