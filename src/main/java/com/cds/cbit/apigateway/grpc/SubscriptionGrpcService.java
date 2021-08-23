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
import com.cds.cbit.apigateway.properties.SubscriptionProperties;
import com.cds.cbit.apigateway.util.JsonProtoConvertUtil;

import lombok.extern.log4j.Log4j2;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pbv1.subscription.CustomizeComponents.CustomizeComponentsResponse;
import pbv1.subscription.GetProductCatalogue.GetProductCatalogueResponse;
import pbv1.subscription.SubscribeAddon.SubscribeAddonResponse;
import pbv1.subscription.UnsubscribeAddon.UnsubscribeAddonResponse;

/**
 * InventoryGrpcService component class defined all inventory modules routes which will consume GRPC
 * client messages in protobuf format and convert them into JSON, send it to appropriate services.
 * 
 * @author Venkata Nagaraju
 * @version 1.0
*/
@Log4j2
@Component
public class SubscriptionGrpcService extends RouteBuilder {

  @Autowired
  private PreHandleProcessor processor; 
  
  @Autowired
  private PostHandleProcessor postHandler; 
  
  @Autowired
  private SubscriptionProperties properties;
  
  @Autowired
  private GrpcServerErrorHandler errorHandler;
  
  /* @see org.apache.camel.builder.RouteBuilder#configure() */
  @Override
  public void configure() throws Exception {
    
    from(properties.getCatalogueGrpc())
    .doTry()
          .process(processor)
          .to(properties.getCatalogueUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {  
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,GetProductCatalogueResponse.class));
          }).process(postHandler)
      .convertBodyTo(GetProductCatalogueResponse.class);
    
    from(properties.getSubscribeGrpc())
    .doTry()
          .process(processor)
          .to(properties.getSubscribeUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {  
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,SubscribeAddonResponse.class));
          }).process(postHandler)
      .convertBodyTo(SubscribeAddonResponse.class);
    
    from(properties.getUnsubscribeGrpc())
    .doTry()
          .process(processor)
          .to(properties.getUnsubscribeUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {  
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,UnsubscribeAddonResponse.class));
          }).process(postHandler)
      .convertBodyTo(UnsubscribeAddonResponse.class);
    
    from(properties.getComponentGrpc())
    .doTry()
          .process(processor)
          .to(properties.getComponentUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {  
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,CustomizeComponentsResponse.class));
          }).process(postHandler)
      .convertBodyTo(CustomizeComponentsResponse.class);
  } // End of route configuration.
} // End of BrmGrpcService.