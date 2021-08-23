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

package com.cds.cbit.apigateway.util;

import com.cds.cbit.apigateway.common.ErrorBody;
import com.cds.cbit.apigateway.common.FailureMessage;
import com.cds.cbit.apigateway.common.RequestHeader;
import com.cds.cbit.apigateway.common.ResponseHeader;
import com.cds.cbit.apigateway.common.ResultInfo;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Util class will provide response header creation for all the API.
 * 
 * @author Venkata Nagaraju.
 * @Version 1.0.
*/
@Component
public class FailureResponseCreator {

  @Autowired
  private Gson gson;

  /**
   * The method will prepare failure response with the given message and payload header.
   * 
   * @param: message     - @Mandatory - Error message.
   * @param: jsonPayload - @Mandatory - Request payload.
  */
  public String prepareFailureResponse(
                            final String code,final String message,final String jsonPayload) {

    final Optional<ResponseHeader> head = Optional.ofNullable(createResponseHead(jsonPayload));
    final ResultInfo resultInfo = 
          ResultInfo.builder().resultCode("FAILURE").resultMsg(message)
                                                  .resultCodeId(Integer.valueOf(code)).build();
    
    final ErrorBody error = ErrorBody.builder().resultInfo(resultInfo).build();
    final FailureMessage responseStr = FailureMessage.builder().body(error).build();
    if (head.isPresent()) {
      responseStr.setHead(head.get());
    }
    return gson.toJson(responseStr);
  } // End of preparing failure response.
  
  /**
   * The method will take inputs of request header and prepare response header
   * with API completion time as response time field.
   * @throws IOException 
   * 
   * @param- header @Mandatory - API Request header.
   */
  public ResponseHeader createResponseHead(final String json) {
    try {
      final JSONObject payloadJson = new JSONObject(json);
      final JSONObject header = payloadJson.getJSONObject("head");
      final RequestHeader reqHeader = gson.fromJson(header.toString(), RequestHeader.class);
      
      final String[] dateVal = Instant.now().toString().split("\\.");

      return ResponseHeader.builder().action(reqHeader.getAction()).country(reqHeader.getCountry())
             .clientId(reqHeader.getClientId()).subType(reqHeader.getSubType())
             .reqId(reqHeader.getReqId()).respTime(dateVal[0] + "Z").version(reqHeader.getVersion())
             .build();
    } catch (Exception e) {
      return null;
    } // End of creating response header with request header information.
  } // End of createResponseHead
} // End of BillingInfoSearchUtil which will create and execute search template.