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

package com.cds.cbit.apigateway.grpc.client;

import com.cds.cbit.apigateway.common.ResponseHeader;
import com.cds.cbit.apigateway.notification.NotificationResponse;
import com.cds.cbit.apigateway.notification.NotificationResponseBody;
import com.cds.cbit.apigateway.notification.NotifyResultInfo;
import com.cds.cbit.apigateway.properties.NotifyProperties;
import com.cds.cbit.apigateway.util.JsonProtoConvertUtil;
import com.cds.cbit.apigateway.util.UtcDateUtil;
import com.google.gson.Gson;

import java.util.Date;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pbv1.common.Common.RequestHead;
import pbv1.notification.Notify.NotifyRequest;
import pbv1.notification.Notify.NotifyResponse;

/**
 * InventoryGrpcService component class defined all inventory modules routes which will consume GRPC
 * client messages in protobuf format and convert them into JSON, send it to appropriate services.
 * 
 * @author Venkata Nagaraju
 * @version 1.0
*/
@Component
public class NotifyGrpcService extends RouteBuilder {
  
  @Autowired
  private NotifyProperties properties;
  
  @Autowired
  private Gson gson;
  
  /* @see org.apache.camel.builder.RouteBuilder#configure() */
  @Override
  public void configure() throws Exception {

    from(properties.getNotifyGrpc())
    .doTry().process(exch -> {
      
      final NotifyRequest request = exch.getIn().getBody(NotifyRequest.class);
      final RequestHead head = request.getHead();
      final NotifyResultInfo resultInfo = 
            NotifyResultInfo.builder().resultCode("SUCCESS").resultCodeId(0)
                            .resultMsg("Notification request acknowledged successfully").build();
      final NotificationResponseBody responseBody = 
            NotificationResponseBody.builder().resultInfo(resultInfo).build();
      
      final ResponseHeader header = 
            ResponseHeader.builder().action(head.getAction()).clientId(head.getClientId())
                          .country(head.getCountry()).reqId(head.getReqId())
                          .respTime(UtcDateUtil.convertDateToUtc(new Date()))
                          .subType(head.getSubType().toString())
                          .version(head.getVersion()).build();
      final NotificationResponse response = NotificationResponse.builder().head(header)
          .body(responseBody).build();
      final String jsonResponse = gson.toJson(response);
      final NotifyResponse grpcResponse = JsonProtoConvertUtil
                                               .fromJson(jsonResponse,NotifyResponse.class);
      exch.getIn().setBody(grpcResponse);
    }).convertBodyTo(NotifyResponse.class);
  } // End of route configuration.
} // End of BrmGrpcService.