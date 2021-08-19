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
 * The POJO provide field mapping to "overrideDetails" section of account creation request JSON.
 * 
 * @author  Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverrideDetail implements Serializable {
  
  private static final long serialVersionUID = -7225117208608433728L;

  @NotNull(message = "Attribute 'type' not present in 'overrideDetail' section of"
                     + " the account request..")
  private String refId;
  
  private OverirdeComponent roaming;

  private String price;

  private OverirdeComponent idd;

  private OverirdeComponent local;
} //End of OverrideDetail