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
import com.cds.cbit.apigateway.properties.AccountProperties;
import com.cds.cbit.apigateway.util.JsonProtoConvertUtil;

import lombok.extern.log4j.Log4j2;
import pbv1.account.BillDetailOuterClass.BillDetailResponse;
import pbv1.account.CreateAccount.CreateAccountResponse;
import pbv1.account.GetCustomerBalance.GetCustomerBalanceResponse;
import pbv1.account.GetCustomerDetail.GetCustomerDetailResponse;
import pbv1.account.GetCustomerUsageDetail.GetCustomerUsageDetailResponse;
import pbv1.account.SearchAccount.SearchAccountResponse;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * InventoryGrpcService component class defined all inventory modules routes which will consume GRPC
 * client messages in protobuf format and convert them into JSON, send it to appropriate services.
 * 
 * 
 * 
 * @author Venkata Nagaraju
 * @version 1.0
*/
@Log4j2
@Component
public class AccountGrpcService extends RouteBuilder {

  @Autowired
  private PreHandleProcessor processor; 
  
  @Autowired
  private PostHandleProcessor postHandler; 
  
  @Autowired
  private AccountProperties properties;
  
  @Autowired
  private GrpcServerErrorHandler errorHandler;
  
  /* @see org.apache.camel.builder.RouteBuilder#configure() */

  
  @Override
  public void configure() throws Exception {

    from(properties.getAccountGrpc())
    .doTry()
          .process(processor)
          .to(properties.getAccountUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {  
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,CreateAccountResponse.class));
          }).process(postHandler)
      .convertBodyTo(CreateAccountResponse.class);
    
    from(properties.getGetBalanceGrpc())
    .doTry()
          .process(processor)
          .to(properties.getGetBalanceUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {  
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,GetCustomerBalanceResponse.class));
          }).process(postHandler)
      .convertBodyTo(GetCustomerBalanceResponse.class);
    
    from(properties.getSearchAcctGrpc())
    .doTry()
          .process(processor)
          .to(properties.getSearchAcctUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {  
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,SearchAccountResponse.class));
          }).process(postHandler)
      .convertBodyTo(SearchAccountResponse.class);
    
    from(properties.getCustUsageGrpc())
    .doTry()
          .process(processor)
          .to(properties.getCustUsageUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {  
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,GetCustomerUsageDetailResponse.class));
          }).process(postHandler)
      .convertBodyTo(GetCustomerUsageDetailResponse.class);
    
    from(properties.getCustDetailGrpc())
    .doTry()
          .process(processor)
          .to(properties.getCustDetailUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {  
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,GetCustomerDetailResponse.class));
          }).process(postHandler)
      .convertBodyTo(GetCustomerDetailResponse.class);
    
    from(properties.getBillDetailGrpc())
    .doTry()
          .process(processor)
          .to(properties.getBillDetailUrl())
    .doCatch(Exception.class)
          .process(errorHandler)
    .doFinally()
          .process(exch -> {  
            final String responseStr = exch.getIn().getBody(String.class);
            log.info(responseStr);
            exch.getIn().setBody(JsonProtoConvertUtil
                           .fromJson(responseStr,BillDetailResponse.class));
          }).process(postHandler)
      .convertBodyTo(BillDetailResponse.class);
  } // End of route configuration.
} // End of BrmGrpcService.