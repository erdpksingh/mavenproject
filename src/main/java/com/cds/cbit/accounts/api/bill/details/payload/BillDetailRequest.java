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

import com.cds.cbit.accounts.commons.beans.RequestHeader;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents request pay load of Bill detail POJO.
 * 
 * @author Meghashree Udupa.
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailRequest implements Serializable {
  /** Auto generated serial version ID. */
  private static final long serialVersionUID = -1293439260425642747L;

  @Valid
  @NotNull(message = "Attribute 'head' not present in the customer detail request")
  private RequestHeader head;

  @Valid
  @NotNull(message = "Attribute 'body' not present in the customer detail request")
  private BillDetailRequestBody body;
} // End of BillDetailRequest POJO.
