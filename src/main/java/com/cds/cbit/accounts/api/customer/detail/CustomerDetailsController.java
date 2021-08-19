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

package com.cds.cbit.accounts.api.customer.detail;

import com.cds.cbit.accounts.api.customer.detail.payload.CustomerDetailsRequest;
import com.cds.cbit.accounts.aspects.ExecutionTime;
import com.cds.cbit.accounts.aspects.Loggable;
import com.cds.cbit.accounts.commons.RequetBeanValidator;
import com.cds.cbit.accounts.interfaces.GenericResponse;
import com.cds.cbit.accounts.util.ExceptionProcessor;
import com.portal.pcm.EBufException;

import java.util.concurrent.CompletableFuture;

import javax.xml.bind.JAXBException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The rest controller provide HTTP method for fetching customer details from BRM. 
 * 
 * @author  Meghashree Udupa.
 * @version 1.0
*/
@RestController
public class CustomerDetailsController {
  
  private final ExceptionProcessor  errorProcessor;
  private final RequetBeanValidator validator;
  private final CustomerDetailProcessor processor;
  
  /** Constructor Injection. **/
  public CustomerDetailsController(final RequetBeanValidator validator,
         final CustomerDetailProcessor processor,final ExceptionProcessor  errorProcessor) {
    
    this.validator = validator;
    this.processor = processor;
    this.errorProcessor = errorProcessor;
  } // End of constructor injection.
  
  /** 
   * The HTTP POST method of service end point will provide read access to BRM account resource.
   * The method will fetch customer detail information from BRM.
   * 
   * @param: request - @Mandatory - GetCustomerDetails Request.
  */
  @Loggable
  @ExecutionTime
  @PostMapping("/billing/api/v1/account/details")
  public CompletableFuture<GenericResponse> 
         getCustomerDetail(@RequestBody final CustomerDetailsRequest request)
                                                             throws EBufException,JAXBException {
    try {
      
      validator.validateRequestBean(request);
      return CompletableFuture.completedFuture(processor.processCustomerDetails(request));
    } catch (Exception e) {
      
      return CompletableFuture
                          .completedFuture(errorProcessor.processException(request.getHead(),e));
    } // End of fetching details from the billing system.
  } // End of getCustomerDetail method.
} // End of CustomerDetailsConstroller, which expose customer detail REST API.