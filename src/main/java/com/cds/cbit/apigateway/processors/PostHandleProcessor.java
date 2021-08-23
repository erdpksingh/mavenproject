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

import com.cds.cbit.apigateway.common.ResponseHeader;
import com.cds.cbit.apigateway.common.TransactionAudit;
import com.cds.cbit.apigateway.util.ConstantsUtil;
import com.cds.cbit.apigateway.util.JsonProtoConvertUtil;
import com.google.gson.Gson;
import com.google.protobuf.MessageOrBuilder;

import java.text.ParseException;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
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
public class PostHandleProcessor implements Processor {
  
  public static final String SEP = "','";
  
  @Autowired
  private Gson gson;
  
  @Autowired
  private TransactionAudit audit;
  
  /* @see org.apache.camel.Processor#process(org.apache.camel.Exchange) */
  @Override
  public void process(final Exchange exch) throws Exception {
    try {
      final String json = JsonProtoConvertUtil.toJson(exch.getIn().getBody(MessageOrBuilder.class));
      
      final Map<String,String> jsonData = JsonProtoConvertUtil.parseJsonString(json);
      
      exch.getIn().setHeader(ConstantsUtil.SUCCESS,jsonData.get("status"));
      
      final ResponseHeader resHeader = gson.fromJson(jsonData.get("header"), ResponseHeader.class);
      
      final String resultCode = jsonData.get("resultCode");
      
      if (!ConstantsUtil.DUPLICATE.equals(resultCode)) {
        
        createTransactionRecord(exch,resHeader,json);
      }
    } catch (Exception e) {
      exch.getIn().setBody(exch.getIn().getBody());
    } // End of payload pre processing.
  } // End of processing exchange before sending it to service.
  
  private void createTransactionRecord(final Exchange exch,
                           final ResponseHeader resHeader,final String json) throws ParseException {

    final String status = "true".equals(exch.getIn().getHeader(ConstantsUtil.SUCCESS)) 
                                                                            ? "Success" : "Failure";
    audit.updateAudit(status, resHeader.getReqId());
    audit.createResponseAuditInfo(resHeader, json);
  } // End of createTransactionRecord.
} // End of PreHandleProcessor which convert proto to JSON and enable parallel processing.