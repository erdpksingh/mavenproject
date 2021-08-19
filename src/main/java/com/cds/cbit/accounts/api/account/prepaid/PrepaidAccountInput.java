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

package com.cds.cbit.accounts.api.account.prepaid;

import com.cds.cbit.accounts.api.account.payload.AccountRequestBody;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.portal.pcm.FList;
import com.portal.pcm.PortalContext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents field mapping of "payInfo" section of BRM COMMIT_CUST_OPCODE.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidAccountInput {

  private AccountRequestBody reqBody;
  
  private FList custCommit;
  
  private FList msisdnOp;

  private FList planFlist;

  private FList simOp;

  private PortalContext portal;

  private String planType;
  
  private String endT;
} // End of PrepaidAccountInput.