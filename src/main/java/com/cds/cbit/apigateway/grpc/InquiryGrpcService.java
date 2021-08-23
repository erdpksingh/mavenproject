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

package com.cds.cbit.apigateway.grpc;

import com.cds.cbit.apigateway.exceptions.GrpcServerErrorHandler;
import com.cds.cbit.apigateway.processors.PostHandleProcessor;
import com.cds.cbit.apigateway.processors.PreHandleProcessor;
import com.cds.cbit.apigateway.properties.ServicesProperties;
import com.cds.cbit.apigateway.util.JsonProtoConvertUtil;

import lombok.extern.log4j.Log4j2;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pbv1.services.ServiceActivation.ServiceActivationResponse;
import pbv1.services.ServiceSuspension.ServiceSuspensionResponse;
import pbv1.services.ServiceTermination.ServiceTerminationResponse;
import pbv1.services.ServiceUnSuspension.ServiceUnSuspensionResponse;

/**
 * InventoryGrpcService component class defined all inventory modules routes which will consume GRPC
 * client messages in protobuf format and convert them into JSON, send it to appropriate services.
 * 
 * @author Venkata Nagaraju
 * @version 1.0
*/
@Log4j2
@Component
public class InquiryGrpcService extends RouteBuilder {

  @Autowired
  private PreHandleProcessor processor; 
  
  @Autowired
  private PostHandleProcessor postHandler; 
  
  @Autowired
  private ServicesProperties properties;
  
  @Autowired
  private GrpcServerErrorHandler errorHandler;
  
  /* @see org.apache.camel.builder.RouteBuilder#configure() */
  @Override
  public void configure() throws Exception {
    
    from(properties.getActivationGrpc())
    .doTry()
         .process(processor)
         .to(properties.getActivationUrl())
    .doCatch(Exception.class)
         .process(errorHandler)
    .doFinally()
         .process(exch -> {
           final String responseStr = exch.getIn().getBody(String.class);
           log.info(responseStr);
           exch.getIn().setBody(JsonProtoConvertUtil
                             .fromJson(responseStr,ServiceActivationResponse.class));
         }).process(postHandler)
      .convertBodyTo(ServiceActivationResponse.class);
    
    from(properties.getSuspendGrpc())
    .doTry()
          .process(processor)
          .to(properties.getSuspendUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,ServiceSuspensionResponse.class));
          }).process(postHandler)
      .convertBodyTo(ServiceSuspensionResponse.class);
    
    from(properties.getUnSuspendGrpc())
    .doTry()
          .process(processor)
          .to(properties.getUnSuspendUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,ServiceUnSuspensionResponse.class));
          }).process(postHandler)
      .convertBodyTo(ServiceUnSuspensionResponse.class);
    
    from(properties.getTerminationGrpc())
    .doTry()
          .process(processor)
          .to(properties.getTerminationUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,ServiceTerminationResponse.class));
          }).process(postHandler)
      .convertBodyTo(ServiceTerminationResponse.class);
  } // End of route configuration.
} // End of BrmGrpcService.