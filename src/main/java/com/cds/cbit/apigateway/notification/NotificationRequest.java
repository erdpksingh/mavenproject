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

import com.cds.cbit.apigateway.common.RequestHeader;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents account service activation request.
 * 
 * @author   Ashutosh verma
 * @version  1.0
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationRequest implements Serializable {
  
  private static final long serialVersionUID = -8134694767501367255L;
  
  @Valid
  @NotNull(message = "Attribute 'head' not present in the request")
  private RequestHeader head;
  
  @Valid
  @NotNull(message = "Attribute 'body' not present in the request")
  private NotificationRequestBody body;
} // End of ActivationRequest.