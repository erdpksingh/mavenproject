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
 * The POJO provide field mapping to Address section of account creation request JSON.
 * 
 * @author  Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {
  
  private static final long serialVersionUID = -999651462940229425L;

  @NotNull(message = "Attribute 'country' not present in 'personalInfo.address' section of"
                     + " the account request..")
  protected String country;  // Customer country of living.

  @NotNull(message = "Attribute 'buildingNo' not present in 'personalInfo.address' section "
                      + "of the account request..")
  protected String buildingNo;  // Building no of the address.

  @NotNull(message = "Attribute 'city' not present in 'personalInfo.address' section of"
                     + " the account request..")
  protected String city;  // Customer living city.

  @NotNull(message = "Attribute 'street' not present in 'personalInfo.address' section of"
                     + " the account request..")
  protected String street; // Street name of the address.

  @NotNull(message = "Attribute 'postalCode' not present in 'personalInfo.address' section of "
                     + "the account request..")
  protected String postalCode;  // Address portal code.

  @NotNull(message = "Attribute 'state' not present in 'personalInfo.address' section of"
                     + " the account request..")
  protected String state; // Customer living state.

  @NotNull(message = "Attribute 'floor' not present in 'personalInfo.address' section of"
                     + " the account request..")
  protected String floor; // Floor no of the address.
  
  protected String unit;  // Address unit of the customer.
} //End of Address