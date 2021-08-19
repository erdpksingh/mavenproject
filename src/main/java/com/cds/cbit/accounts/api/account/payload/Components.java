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
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field mapping to Components section of account creation request JSON.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Components implements Serializable {
  
  private static final long serialVersionUID = 2569713615748339947L;

  @NotNull(message = "Attribute 'type' not present in 'components' section of"
                     + " the account request..")
  @Pattern(regexp = "DATA|SMS|VOICE", flags = Pattern.Flag.CASE_INSENSITIVE,
           message = "Component type should be DATA | SMS | VOICE")
  protected String type; // Type of the component DATA | SMS | VOICE.
  
  @NotNull(message = "Attribute 'dealCode' not present in 'components' section of"
                     + " the account request..")
  protected String dealCode; // Component deal to be purchased.
  
  protected DiscountDetails discountDetails; // Component discounts if applicable.
  
  protected OverrideDetail overrideDetail;
} //End of Components