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

package com.cds.cbit.accounts.api.customer.detail.payload;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents field mapping of CustomerDetailsBody section of Customer 
 * Details request JSON..
 * 
 * @author  Meghashree udupa.
 * @version 1.0.
*/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailsBody implements Serializable {
  
  /** Auto generated serial version ID.   */
  private static final long serialVersionUID = 5953961312043102255L;
  @Valid
  @NotNull(message = "Attribute 'accountNumber' not present in the customer detail request")
  private String accountNumber;
  
} // End of CustomerDetailsBody POJO.
