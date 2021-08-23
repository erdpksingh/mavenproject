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
import com.cds.cbit.apigateway.properties.InventoryProperties;
import com.cds.cbit.apigateway.util.JsonProtoConvertUtil;

import lombok.extern.log4j.Log4j2;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pbv1.inventory.PairMsisdnWithSim.PairMsisdnWithSimResponse;
import pbv1.inventory.PairUnpairInventory.PairUnpairInventoryResponse;
import pbv1.inventory.SearchInventoryByAccount.SearchInventoryByAccountResponse;
import pbv1.inventory.SearchInventoryByNumber.SearchInventoryByNumberResponse;
import pbv1.inventory.SearchInventoryByStatus.SearchInventoryByStatusResponse;

/**
 * InventoryGrpcService component class defined all inventory modules routes which will consume GRPC
 * client messages in protobuf format and convert them into JSON, send it to appropriate services.
 * 
 * @author Venkata Nagaraju
 * @version 1.0
*/
@Log4j2
@Component
public class InventoryGrpcService extends RouteBuilder {

  @Autowired
  private PreHandleProcessor processor;
  
  @Autowired
  private PostHandleProcessor postHandler; 
  
  @Autowired
  private InventoryProperties properties;
  
  @Autowired
  private GrpcServerErrorHandler errorHandler;
  
  /* @see org.apache.camel.builder.RouteBuilder#configure() */
  @Override
  public void configure() throws Exception {
    
    from(properties.getNumberSearchGrpc())
    .doTry()
           .process(processor)
           .to(properties.getNumberSearchUrl())
    .doCatch(Exception.class)
           .process(errorHandler) 
    .doFinally()
           .process(exch -> {
             final String responseStr = exch.getIn().getBody(String.class);
             log.info(responseStr);
             exch.getIn().setBody(
                  JsonProtoConvertUtil.fromJson(responseStr,SearchInventoryByNumberResponse.class));
           }).process(postHandler)
       .convertBodyTo(SearchInventoryByNumberResponse.class);
    
    from(properties.getAccountSearchGrpc())
    .doTry()
           .process(processor)
           .to(properties.getAccountSearchUrl())
    .doCatch(Exception.class)
           .process(errorHandler)
    .doFinally()
           .process(exch -> {
             final String responseStr = exch.getIn().getBody(String.class);
             log.info(responseStr);
             exch.getIn().setBody(
                 JsonProtoConvertUtil.fromJson(responseStr,SearchInventoryByAccountResponse.class));
           }).process(postHandler)
      .convertBodyTo(SearchInventoryByAccountResponse.class);
    
    from(properties.getSimPairGrpc())
    .doTry()
           .process(processor)
           .to(properties.getSimPairUrl())
    .doCatch(Exception.class)
           .process(errorHandler)
    .doFinally()
           .process(exch -> {
             final String responseStr = exch.getIn().getBody(String.class);
             exch.getIn().setBody(
                       JsonProtoConvertUtil.fromJson(responseStr,PairMsisdnWithSimResponse.class));
           }).process(postHandler)
      .convertBodyTo(PairMsisdnWithSimResponse.class);
    
    from(properties.getUnPairGrpc())
    .doTry()
           .process(processor)
           .to(properties.getUnPairUrl())
    .doCatch(Exception.class)
           .process(errorHandler)
    .doFinally()
           .process(exch -> {
             final String responseStr = exch.getIn().getBody(String.class);
             log.info(responseStr);
             exch.getIn().setBody(
                    JsonProtoConvertUtil.fromJson(responseStr,PairUnpairInventoryResponse.class));
           }).process(postHandler)
      .convertBodyTo(PairUnpairInventoryResponse.class);
    
    from(properties.getSubtypeSearchGrpc())
    .doTry()
           .process(processor)
           .to(properties.getSubtypeSearchUrl())
    .doCatch(Exception.class)
           .process(errorHandler)
    .doFinally()
           .process(exch -> {
             final String responseStr = exch.getIn().getBody(String.class);
             log.info(responseStr);
             exch.getIn().setBody(JsonProtoConvertUtil
                                     .fromJson(responseStr,SearchInventoryByStatusResponse.class));
           }).process(postHandler)
      .convertBodyTo(SearchInventoryByStatusResponse.class);
  } // End of route configuration.
} // End of BrmGrpcService.