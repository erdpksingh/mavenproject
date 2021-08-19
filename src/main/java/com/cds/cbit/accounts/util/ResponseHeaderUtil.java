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

package com.cds.cbit.accounts.util;

import com.cds.cbit.accounts.commons.beans.RequestHeader;
import com.cds.cbit.accounts.commons.beans.ResponseHeader;

import java.time.Instant;

/**
 * The Util class will provide response header creation for all the API.
 * 
 * @author  Venkata Nagaraju.
 * @Version 1.0.
*/
public final class ResponseHeaderUtil {
  
  private ResponseHeaderUtil() {
    // private constructor.
  } // End of constructor.

  /**
   * The method will take inputs of request header and prepare response header with API completion
   * time as response time field.
   * 
   * @param- header   @Mandatory  - API Request header.
  */
  public static ResponseHeader createResponseHead(final RequestHeader header) {
    
    final String[] dateVal = Instant.now().toString().split("\\.");

    return ResponseHeader.builder().action(header.getAction())
          .clientId(header.getClientId()).country(header.getCountry()).version(header.getVersion())
          .reqId(header.getReqId()).subType(header.getSubType()).respTime(dateVal[0] + "Z").build();
  } // End of executeSearchTemplate
} // End of BillingInfoSearchUtil which will create and execute search template.