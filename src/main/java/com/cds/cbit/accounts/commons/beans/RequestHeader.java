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

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents common header for all the API request.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestHeader implements Serializable {
  
  private static final long serialVersionUID = -1538379690572426540L;

  @NotNull(message = "Attribute 'country' not present in the header")
  private String country;

  @NotNull(message = "Attribute 'clientId' not present in the header")
  private String clientId;

  @NotNull(message = "Attribute 'action' not present in the header")
  private String action;

  @NotNull(message = "Attribute 'version' not present in the header")
  private String version;

  @NotNull(message = "Attribute 'reqId' not present in the header")
  private String reqId;
  
  @NotNull(message = "Attribute 'reqTime' not present in the header")
  private String reqTime;
  
  @NotNull(message = "Attribute 'subType' not present in the header")
  private String subType;
} // End of RequestHeader.