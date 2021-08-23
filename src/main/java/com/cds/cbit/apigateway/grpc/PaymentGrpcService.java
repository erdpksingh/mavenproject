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
import com.cds.cbit.apigateway.properties.PaymentProperties;
import com.cds.cbit.apigateway.util.JsonProtoConvertUtil;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pbv1.billing.SearchItemsByBillNumber.SearchItemsByBillNumberResponse;
import pbv1.payment.CollectPayment.CollectPaymentResponse;
import pbv1.payment.CreditCapPayment.CreditCapPaymentResponse;
import pbv1.payment.GetAccountStatement.GetAccountStatementResponse;
import pbv1.payment.PaymentReversal.PaymentReversalResponse;
import pbv1.payment.PostAdjustment.PostAdjustmentResponse;

/**
 * InventoryGrpcService component class defined all inventory modules routes which will consume GRPC
 * client messages in protobuf format and convert them into JSON, send it to appropriate services.
 * 
 * @author Venkata Nagaraju
 * @version 1.0
*/
@Component
public class PaymentGrpcService extends RouteBuilder {

  @Autowired
  private PreHandleProcessor processor; 
  
  @Autowired
  private PostHandleProcessor postHandler; 
  
  @Autowired
  private PaymentProperties properties;
  
  @Autowired
  private GrpcServerErrorHandler errorHandler;
  
  /* @see org.apache.camel.builder.RouteBuilder#configure() */
  @Override
  public void configure() throws Exception {
    
    from(properties.getAcctStatementGrpc())
    .doTry()
         .process(processor)
         .to(properties.getAcctStatementUrl())
    .doCatch(Exception.class)
         .process(errorHandler)
    .doFinally()
         .process(exch -> {
           final String responseStr = exch.getIn().getBody(String.class);
           log.info(responseStr);
           exch.getIn().setBody(JsonProtoConvertUtil
                             .fromJson(responseStr,GetAccountStatementResponse.class));
         }).process(postHandler)
      .convertBodyTo(GetAccountStatementResponse.class);
    
    from(properties.getPaymentReversalGrpc())
    .doTry()
         .process(processor)
         .to(properties.getPaymentReversalUrl())
    .doCatch(Exception.class)
         .process(errorHandler)
    .doFinally()
         .process(exch -> {
           final String responseStr = exch.getIn().getBody(String.class);
           log.info(responseStr);
           exch.getIn().setBody(JsonProtoConvertUtil
                             .fromJson(responseStr,PaymentReversalResponse.class));
         }).process(postHandler)
      .convertBodyTo(PaymentReversalResponse.class);
    
    from(properties.getPostAdjustmentGrpc())
    .doTry()
         .process(processor)
         .to(properties.getPostAdjustmentUrl())
    .doCatch(Exception.class)
         .process(errorHandler)
    .doFinally()
         .process(exch -> {
           final String responseStr = exch.getIn().getBody(String.class);
           log.info(responseStr);
           exch.getIn().setBody(JsonProtoConvertUtil
                             .fromJson(responseStr,PostAdjustmentResponse.class));
         }).process(postHandler)
      .convertBodyTo(PostAdjustmentResponse.class);
    
    from(properties.getCollectPaymentGrpc())
    .doTry()
         .process(processor)
         .to(properties.getCollectPaymentUrl())
    .doCatch(Exception.class)
         .process(errorHandler)
    .doFinally()
         .process(exch -> {
           final String responseStr = exch.getIn().getBody(String.class);
           log.info(responseStr);
           exch.getIn().setBody(JsonProtoConvertUtil
                             .fromJson(responseStr,CollectPaymentResponse.class));
         }).process(postHandler)
      .convertBodyTo(CollectPaymentResponse.class);
    
    from(properties.getCreditCapGrpc())
    .doTry()
         .process(processor)
         .to(properties.getCreditCapUrl())
    .doCatch(Exception.class)
         .process(errorHandler)
    .doFinally()
         .process(exch -> {
           final String responseStr = exch.getIn().getBody(String.class);
           log.info(responseStr);
           exch.getIn().setBody(JsonProtoConvertUtil
                             .fromJson(responseStr,CreditCapPaymentResponse.class));
         }).process(postHandler)
      .convertBodyTo(CreditCapPaymentResponse.class);
    
    from(properties.getSearchItemGrpc())
    .doTry()
         .process(processor)
         .to(properties.getSearchItemUrl())
    .doCatch(Exception.class)
         .process(errorHandler)
    .doFinally()
         .process(exch -> {
           final String responseStr = exch.getIn().getBody(String.class);
           log.info(responseStr);
           exch.getIn().setBody(JsonProtoConvertUtil
                             .fromJson(responseStr,SearchItemsByBillNumberResponse.class));
         }).process(postHandler)
      .convertBodyTo(SearchItemsByBillNumberResponse.class);
    
  } // End of route configuration.
} // End of BrmGrpcService.