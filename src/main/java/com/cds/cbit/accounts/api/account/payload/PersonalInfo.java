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
 * The POJO provide field mapping to PersonalInfo section of account creation request JSON.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfo implements Serializable {
  
  private static final long serialVersionUID = -4411204890838563123L;

  @Valid
  @NotNull(message = "Attribute 'address' not found in 'personalInfo' section of"
                     + " the account request..")
  protected Address address;  // Customer address information.
  
  @Valid
  @NotNull(message = "Attribute 'name' not found in 'personalInfo' section of"
                     + " the account request..")
  protected NameInfo name;  // Customer name information.
  
  @Valid
  @NotNull(message = "Attribute 'ids' not found in 'personalInfo' section of the account request..")
  private List<Identity> ids; // Customer KYC information.
 
  @NotNull(message = "Attribute 'nationality' not found in 'personalInfo' section of"
                     + " the account request..")
  protected String nationality; // Customer nationality.

  @NotNull(message = "Attribute 'displayName' not found in 'personalInfo' section of"
                     + " the account request..")
  protected String displayName;  // Account display name.

  @NotNull(message = "Attribute 'dateOfBirth' not found in 'personalInfo' section of"
                     + " the account request..")
  protected String dateOfBirth;  // Customer DOB.
  
  @NotNull(message = "Attribute 'email' not found in 'personalInfo' section of"
                     + " the account request..")
  protected String email; // Customer email address.

  protected String altContact; // Alternate contact number.
} //End of PersonalInfo