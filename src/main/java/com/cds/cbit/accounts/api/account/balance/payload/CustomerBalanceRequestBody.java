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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents "body" section of customer balance GET call API request.
 * 
 * @author  Anuradha Manda
 * @Version 1.0.
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBalanceRequestBody implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull(message = "Attribute 'accountNumber' is not present in balance request.")
  
  private String accountNumber; // Customer account Number.

  @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}T([0-9]{2}:){2}[0-9]{2}Z",
         message = "Invalid Time format provided for 'fromDate'.")
  
  private String from; // fromDate.
} //End of CustomerBalanceRequestBody.