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

package com.cds.cbit.accounts.commons.beans;

import com.cds.cbit.accounts.interfaces.GenericResponse;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents failure payload,which will be return by all the controller classes,for the 
 * exceptions occur during request execution in the billing system.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailureMessage implements Serializable,GenericResponse {
  
  private static final long serialVersionUID = -6990613812776290431L;

  private ResponseHeader head;
  
  private ErrorBody body;
} // End of FailureMessage.