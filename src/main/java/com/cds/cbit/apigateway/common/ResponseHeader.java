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

package com.cds.cbit.apigateway.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents common header for all the API responses, that will return to API gateway.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseHeader implements Serializable {
  
  private static final long serialVersionUID = 6457282910382563257L;

  private String country;

  private String clientId;

  private String action;

  private String version;

  private String reqId;
  
  private String respTime;
  
  private String subType;
} // End of ResponseHeader.