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

import com.cds.cbit.apigateway.properties.NotifyProperties;
import com.cds.cbit.apigateway.util.JsonProtoConvertUtil;

import lombok.extern.log4j.Log4j2;
import pbv1.notification.Notify.NotifyRequest;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST controller will consume notifications, send it to upstream system through HTTP method.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@RestController
public class NotificationController {

  @Autowired
  private NotificationAudit audit;

  @Autowired
  private ProducerTemplate template;
  
  @Autowired
  private NotifyProperties properties;
  
  @Autowired
  private NotificationProcessor processor;
  
  /** 
   * The HTTP POST Method will consume notification from notification service and send it to
   * the upstream system through GRPC client call. 
  */
  @PostMapping(value = "/notification", 
               produces = "application/json", consumes = "application/json") 
  public String processNotification(@RequestBody final NotificationRequest request) {
    try {
      final String payload = audit.createNotifyAudit(request);
      final Exchange response = template.send(properties.getNotifyGrpc(),new Processor() {
        
        @Override
        public void process(final Exchange exchange) throws Exception {
          exchange.getIn().setBody(JsonProtoConvertUtil.fromJson(payload,NotifyRequest.class));
        }
      });
      log.info(response);
      processor.process(response);
      return response.getOut().getBody(String.class);
    } catch (Exception e) {
      log.error("Error occurred while sending notification to BSS MW. {}",e.getMessage());
      return "failure";
    } // End of sending notification.
  } // End of processNotification.
} // End of NotificationController.