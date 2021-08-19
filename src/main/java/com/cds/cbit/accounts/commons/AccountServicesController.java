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

package com.cds.cbit.accounts.commons;

import com.cds.cbit.accounts.api.account.AccountCreationProcessor;
import com.cds.cbit.accounts.api.account.payload.AccountRequest;
import com.cds.cbit.accounts.api.bill.details.BillDetailProcessor;
import com.cds.cbit.accounts.api.bill.details.payload.BillDetailRequest;
import com.cds.cbit.accounts.interfaces.GenericResponse;
import com.cds.cbit.accounts.util.ExceptionProcessor;
import com.portal.pcm.EBufException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.concurrent.CompletableFuture;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The factory class provide basic validation of the account creation, if validations are successful
 * then the class will invoke appropriate component class for executing the account request.
 * 
 * @author  Saibabu Guntur.
 * @Version 1.0.
*/
@Log4j2
@RestController
@Api(tags = {"BRM Customer account creation endpoint."})
public class AccountServicesController {

  private final ExceptionProcessor errorProcessor;
  private final RequetBeanValidator validator;
  private final AccountCreationProcessor accountProcessor; 
  private final BillDetailProcessor billDeatilProcessor;

  /** Constructor injection. **/
  public AccountServicesController(final AccountCreationProcessor accountProcessor,
                                   final RequetBeanValidator validator,
                                   final ExceptionProcessor errorProcessor,
                                   final BillDetailProcessor billDeatilProcessor) {
    this.validator = validator;
    this.accountProcessor = accountProcessor;
    this.errorProcessor = errorProcessor;
    this.billDeatilProcessor = billDeatilProcessor;
    
  } // End of Constructor injection.
  
  /**
   * The HTTP POST method of the service endpoint will provide write access on BRM account resource.
   * The method will forward the request to AccountCreationProcessor for further processing.
   *  
   * @param: payload: Account request.
  */
  @Async
  @ApiOperation("HTTP POST method to create customer account in BRM.")
  @PostMapping(value = "/billing/api/v1/account", consumes = "application/json")
  public CompletableFuture<GenericResponse> 
                                         processRequest(@RequestBody final AccountRequest payload) {
    try {
      validator.validateRequestBean(payload);
      return CompletableFuture.completedFuture(accountProcessor.createAccountInBrm(payload));
    } catch (Exception e) {
      return CompletableFuture.completedFuture(
                                              errorProcessor.processException(payload.getHead(),e));
    } // End of creating account in BRM.
  } // End of creating customer account in BRM.
  
  /** 
   * The HTTP POST method of service end point will provide read access to billing system balance
   * resource.The method will fetch customer Bill detail information from the billing system.
  */
  @Async
  @ApiOperation("HTTP POST method for fetching bill details from BRM.")
  @PostMapping("/billing/api/v1/account/billDetails")
  public CompletableFuture<GenericResponse> 
         getCustomerDetail(@RequestBody final BillDetailRequest request) 
                                                                throws EBufException,JAXBException {
    try {
      validator.validateRequestBean(request);
      return CompletableFuture.completedFuture(billDeatilProcessor.processBillDetails(request));
    } catch (Exception e) {
      log.info("Exception occurred while processing request : {} ", e.getMessage());
      return CompletableFuture.completedFuture(errorProcessor
                                                           .processException(request.getHead(), e));
    } // End of fetching details from the billing system.
  } // End of getBillDetails.
} // End of AccountServicesController.