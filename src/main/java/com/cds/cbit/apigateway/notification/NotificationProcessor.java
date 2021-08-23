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

import com.cds.cbit.apigateway.common.ResponseHeader;
import com.cds.cbit.apigateway.common.TransactionAudit;
import com.cds.cbit.apigateway.exceptions.ApiGatewayException;
import com.cds.cbit.apigateway.exceptions.DuplicateEntryException;
import com.cds.cbit.apigateway.util.ConstantsUtil;
import com.cds.cbit.apigateway.util.JsonProtoConvertUtil;
import com.google.gson.Gson;
import com.google.protobuf.MessageOrBuilder;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The processor class of camel route will consume the request exchange of protobuf message and 
 * convert it into JSON message before sending it to service endpoint.
 * 
 * @author Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class NotificationProcessor {

  public static final String SEP = "','";

  @Autowired
  private Gson gson;

  @Autowired
  private TransactionAudit audit;

  /** The method will create audit logs for notifications. */
  public void process(final Exchange exch) {
    try {

      final String json = JsonProtoConvertUtil
                                 .toJson(exch.getOut().getBody(MessageOrBuilder.class));
      final Map<String,String> jsonData = JsonProtoConvertUtil.parseJsonString(json);
      final ResponseHeader resHeader = gson.fromJson(jsonData.get("header"),ResponseHeader.class);
      
      exch.getIn().setHeader(ConstantsUtil.SUCCESS, jsonData.get("status"));
      createTransactionRecord(exch,resHeader,json);
      
    } catch (Exception e) {
      if (e.getCause().getMessage().contains("Duplicate entry")) {
        throw new DuplicateEntryException("duplicate request found.", e);
      }
      throw new ApiGatewayException(e);
    } // End of payload pre-processing.
  } // End of processing exchange before sending it to service.

  private void createTransactionRecord(
          final Exchange exch,final ResponseHeader resHeader, final String json)
                                                       throws ParseException, SQLException {

    final String status  = "true".equals(exch.getIn().getHeader(ConstantsUtil.SUCCESS)) 
                                                                    ? "Success" : "Failure";
    audit.updateNotifyAudit(status, resHeader.getReqId());
    audit.createNotifyAuditResponseInfo(resHeader,json, "Response");
  } // End of createTransactionRecord.
} // End of PreHandleProcessor which convert proto to JSON and enable parallel.