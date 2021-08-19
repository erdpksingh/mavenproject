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

package com.cds.cbit.accounts.api.account.balance;

import com.cds.cbit.accounts.api.account.balance.payload.CustomerBalanceRequest;
import com.cds.cbit.accounts.commons.RequetBeanValidator;
import com.cds.cbit.accounts.interfaces.GenericResponse;
import com.cds.cbit.accounts.util.ExceptionProcessor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST controller provide endpoint for customer balance related billing resources. The class 
 * expose various HTTP methods for exposing customer balance information to API gateway.
 * 
 * @author  Anuradha Manda.
 * @Version 1.0.
*/
@RestController
@Api(tags = { "BRM Customer balance information endpoint" })
public class CustomerBalanceController {

  private final ExceptionProcessor errorProcessor;
  private final RequetBeanValidator validator;
  private final CustomerBalanceProcessor processor;

  /** Constructor Dependency injection. */
  public CustomerBalanceController(final RequetBeanValidator validator,
      final CustomerBalanceProcessor processor,final ExceptionProcessor errorProcessor) {
    
    this.validator = validator;
    this.processor = processor;
    this.errorProcessor = errorProcessor;
  } // End of constructor injection.

  /** 
   * The HTTP POST method of service end point will provide read access to billing system balance
   * resource.The method will fetch customer balance information from the billing system.
   * 
   * @PathParam payload @Mandatory - Customer balance Request.
  */
  @PostMapping("/billing/api/v1/account/balance")
  @ApiOperation("HTTP POST method to consume customer balance information from BRM.")
  public CompletableFuture<GenericResponse> getCustomerBalance(
                                            @RequestBody final CustomerBalanceRequest request) {
    try {
      validator.validateRequestBean(request);
      return CompletableFuture.completedFuture(processor.processCustomerBalance(request));
      
    } catch (Exception e) {
      return CompletableFuture.completedFuture(
                                          errorProcessor.processException(request.getHead(),e));
    } // End of fetching customer balance information.
  } //End of getCustomerBalance method.
} // End of CustomerBalanceController, which expose balance resource endpoint.