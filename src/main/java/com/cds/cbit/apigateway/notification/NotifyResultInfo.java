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

package com.cds.cbit.apigateway.notification;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents result information section of the response payload.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyResultInfo implements Serializable {

  private static final long serialVersionUID = -4168746121301106423L;

  private String resultCode; //resultCode
  
  private int resultCodeId; //resultCodeId
  
  private String resultMsg; //resultMsg
} //End of ResultInfo.