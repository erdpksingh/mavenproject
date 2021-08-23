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

package com.cds.cbit.apigateway.processors;

import com.cds.cbit.apigateway.common.RequestHeader;
import com.cds.cbit.apigateway.common.RequetBeanValidator;
import com.cds.cbit.apigateway.common.TransactionAudit;
import com.cds.cbit.apigateway.exceptions.ApiGatewayException;
import com.cds.cbit.apigateway.exceptions.DuplicateEntryException;
import com.cds.cbit.apigateway.exceptions.ValidationException;
import com.cds.cbit.apigateway.util.ConstantsUtil;
import com.cds.cbit.apigateway.util.JsonProtoConvertUtil;
import com.google.gson.Gson;
import com.google.protobuf.MessageOrBuilder;

import java.text.ParseException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The processor class of camel route will consume the request exchange of protobuf message and
 * convert it into JSON message before sending it to service REST endpoint.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class PreHandleProcessor implements Processor {
  
  public static final String SEP = "','";
  
  @Autowired
  private Gson gson;
  
  @Autowired
  private TransactionAudit audit;
  
  @Autowired
  private RequetBeanValidator validator;
  
  /* @see org.apache.camel.Processor#process(org.apache.camel.Exchange) */
  @Override
  public void process(final Exchange exch) throws Exception {
    try {
      
      final String json = JsonProtoConvertUtil.toJson(exch.getIn().getBody(MessageOrBuilder.class));
      
      final JSONObject payloadJson = new JSONObject(json);
      final JSONObject header = payloadJson.getJSONObject("head"); 
      
      final RequestHeader reqHeader = gson.fromJson(header.toString(), RequestHeader.class);
      validator.validateRequestBean(reqHeader);
      createTransactionRecord(reqHeader,json);
      
      exch.getIn().setBody(json);
      exch.getIn().setHeader(Exchange.CONTENT_TYPE, ConstantsUtil.JSON);
      
    } catch (Exception e) {
      e.printStackTrace();
      if (e.getClass() != null && e.getClass().getName().contains("ValidationException")) {
        throw new ValidationException(e.getMessage(),e);
        
      } else if (e.getCause().getMessage().contains("Duplicate entry")) {
        throw new DuplicateEntryException("duplicate request found.",e);
      }
      throw new ApiGatewayException(e);
    } // End of payload pre-processing.
  } // End of processing exchange before sending it to service.
  
  private void createTransactionRecord(
                           final RequestHeader reqHeader,final String json) throws ParseException {
    
    audit.createAudit(reqHeader);
    audit.createRequestAuditInfo(reqHeader, json);
  } // End of createTransactionRecord.
} // End of PreHandleProcessor which convert proto to JSON and enable parallel processing.