/*
 * Copyright (C) 2019 Covalense Technologies 
 *
 * Licensed under the Covalense CSMART, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.covalense.com
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
*/

package com.cds.cbit.apigateway.util;

import com.cds.cbit.apigateway.exceptions.ApiGatewayException;

import com.google.common.collect.ImmutableMap;
import com.google.protobuf.AbstractMessage.Builder;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.util.Map;

import lombok.extern.log4j.Log4j2;

import org.json.JSONObject;

/**
 * The util class JsonProtoAdapter provides methods to convert given proto message into JSON
 * and given JSON into proto.
 * 
 * @author Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
public final class JsonProtoConvertUtil {
  
  private JsonProtoConvertUtil() {
  } // End of private constructor.
  
  /**
   * The method will convert given JSON into protbuf message.
   * 
   * @param messageOrBuilder is the instance of given message.
   * @return returns JSON string of given protobuf message or builder.
   * @throws IOException if any error occurs
  */
  public static String toJson(final MessageOrBuilder messageOrBuilder) throws IOException {
    return JsonFormat.printer().print(messageOrBuilder);
  } // End of toJson.

  /**
   * Makes a new instance of required message based on the provided JSON.
   * 
   * @param: The message class type need to convert.
   * @param: JSON string which need to convert to message.
   * @return An instance of required message based on the JSON values
   * @throws IOException if any error occurs
  */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <T extends Message> 
                   T fromJson(final String json, final Class<T> clazz) throws IOException {
    try {
    	
      // Calling newBuilder method of required message.
      final Builder builder = (Builder) clazz.getMethod("newBuilder").invoke(null);
      
      // The instance is placed into the builder values
      JsonFormat.parser().ignoringUnknownFields().merge(json, builder);

      // the instance will be from the build
      return (T) builder.build();

    } catch (Exception e) {
    	e.printStackTrace();
      log.error("Error occurred while converting JSON to PROTO or vice verse");
      throw new ApiGatewayException(e.getMessage(),e);
    } // End of converting JSON to PROTO and vice versa.
  } // End of fromJson.
  
  /** The method parse the given JSON string into different JSON object. **/
  public static Map<String,String> parseJsonString(final String json) {
    
    final JSONObject payloadJson = new JSONObject(json);
    final JSONObject header = payloadJson.getJSONObject("head"); 
    final JSONObject message = payloadJson.getJSONObject("body"); 
    final JSONObject result = message.getJSONObject("resultInfo"); 
    
    final int resultCode = result.has("resultCodeId") ? result.getInt("resultCodeId") : 0;
    final String statusHeader =  resultCode == 0 ? "true" : "false"; 
    return ImmutableMap.of("status",statusHeader,"header",header.toString(),
                                                 "resultCode",String.valueOf(resultCode));
  } // End of parseJsonString method.
} // End of JsonProtoAdapter.