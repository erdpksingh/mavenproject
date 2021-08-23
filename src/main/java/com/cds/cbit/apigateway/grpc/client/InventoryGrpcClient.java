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

package com.cds.cbit.apigateway.grpc.client;

import lombok.extern.log4j.Log4j2;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import pbv1.common.Common.RequestHead;
import pbv1.common.Common.SubscriptionType;
import pbv1.inventory.SearchInventoryByNumber.SearchInventoryByNumberRequest;
import pbv1.inventory.SearchInventoryByNumber.SearchInventoryByNumberRequestBody;

/**
 * GRPC Client for calling account creation grpc camel service.
 * 
 * @author Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@RestController
public class InventoryGrpcClient {
  
  @Autowired
  public CamelContext camel;

  /** GET Mapping for account creation client. **/
  @GetMapping("/inventory")
  public void createCustomerAccount() {
    try {
      camel.addRoutes(new RouteBuilder() {
        @Override
        public void configure() throws Exception {
          from("timer://foo?period=10000&repeatCount=1").process(exch -> {
            final RequestHead header = RequestHead.newBuilder().setClientId("BSSMW001")
                                                  .setReqId("12423").setAction("inventorySearch")
                                                  .setReqTime("2018-08-01T08:30:00.021Z")
                                                  .setVersion("1.0").setCountry("SG")
                                                  .setSubTypeValue(SubscriptionType.POSTPAID_VALUE)
                                                  .build();
            final  SearchInventoryByNumberRequestBody requestBody = 
                   SearchInventoryByNumberRequestBody.newBuilder().setInventoryId("87655674")
                                                         .setInventoryType("MSISDN").build();
            final  SearchInventoryByNumberRequest request = 
                   SearchInventoryByNumberRequest.newBuilder().setHead(header)
                                                              .setBody(requestBody).build();

            exch.getIn().setBody(request, SearchInventoryByNumberRequest.class);
          }).to("grpc://localhost:6565/pbv1.inventory.Inventory?"
                                 + "method=SearchInventoryByNumber").log("Received ${body}");
        } // End of route configuration.
      }); // End of inventory search by number route.
    } catch (Exception e) {
      log.error("Error occurred while consuming grpc request");
    } // End of camel route building.
  } // End of createCustomerAccount method.
} // End of AccountGrpcClient