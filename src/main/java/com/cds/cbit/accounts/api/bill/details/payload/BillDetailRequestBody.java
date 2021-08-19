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

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents request payload. which contains startDate,endDate, type
 * of bill and accountNumber information.
 * 
 * @author Anuradha Manda.
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailRequestBody implements Serializable {

  /** Generated serialVersionUID. */
  private static final long serialVersionUID = 1289443930305046175L;

  @NotNull(message = "Attribute 'startDate' is not found in the request.")
  @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}T([0-9]{2}:){2}[0-9]{2}Z", 
              message = "Invalid Time format provided for 'startDate'.")
  private String startDate; // StartDate.
  
  @NotNull(message = "Attribute 'endDate' is not found in the request.")
  @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}T([0-9]{2}:){2}[0-9]{2}Z", 
              message = "Invalid Time format provided for 'endDate'.")
  private String endDate; // EndDate.

  @NotNull(message = "Attribute 'accountNumber' is not found in the request.")
  @NotEmpty(message = "Attribute 'accountNumber' should not be empty.")
  private String accountNumber; // Customer account Number.

  @NotNull(message = "Attribute 'type' is not found in the request.")
  @Pattern(regexp = "BILL|FINANCE|USAGE", 
              message = "Attribute 'type' should be BILL or FINANCE or USAGE")
  private String type; // Type of Bill.
} // End of RequestBody.