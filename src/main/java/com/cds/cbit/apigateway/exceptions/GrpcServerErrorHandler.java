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

package com.cds.cbit.apigateway.exceptions;

import com.cds.cbit.apigateway.util.FailureResponseCreator;
import com.cds.cbit.apigateway.util.JsonProtoConvertUtil;
import com.google.protobuf.MessageOrBuilder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * The processor will handle exceptions occurs while sending request from gRPC server to appropriate
 * BRM service. The processor will create appropriate failure message as per the exception and set
 * it to the exchange.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class GrpcServerErrorHandler implements Processor {
  @Autowired
  private Environment properties;
  
  @Autowired
  private FailureResponseCreator responseCreator;
  
  /* @see org.apache.camel.Processor#process(org.apache.camel.Exchange)  */
  @Override
  public void process(final Exchange exchange) throws Exception {

    final Throwable ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
    final StringBuilder message = new StringBuilder();
    final StringBuilder code = new StringBuilder();
    final StringBuilder jsonPayload = new StringBuilder();
    if (ex.getClass() != null && ex.getClass().getName().contains("ValidationException")) {
      
      code.append("100101");
      message.append(properties.getProperty("100101")).append(ex.getMessage());
      jsonPayload.append(JsonProtoConvertUtil
                                 .toJson(exchange.getIn().getBody(MessageOrBuilder.class)));
      
    } else if (ex.getCause() != null 
        && ex.getCause().getClass().getName().contains("DuplicateKeyException")) {
      
      code.append("100107");
      message.append(properties.getProperty("100107"));
      jsonPayload.append(JsonProtoConvertUtil
                                 .toJson(exchange.getIn().getBody(MessageOrBuilder.class)));
    } else {
      
      code.append("100102");
      message.append(properties.getProperty("100102"));
      jsonPayload.append(exchange.getIn().getBody(String.class));
    }
    final String response = 
          responseCreator.prepareFailureResponse(
                                code.toString(),message.toString(), jsonPayload.toString());
    exchange.getIn().setBody(response);
  } // End of processing exchange.
} // End of GrpcServerErrorHandler.