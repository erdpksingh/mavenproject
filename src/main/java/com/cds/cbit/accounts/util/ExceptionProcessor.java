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

package com.cds.cbit.accounts.util;

import com.cds.cbit.accounts.aspects.Loggable;
import com.cds.cbit.accounts.commons.beans.ErrorBody;
import com.cds.cbit.accounts.commons.beans.FailureMessage;
import com.cds.cbit.accounts.commons.beans.RequestHeader;
import com.cds.cbit.accounts.commons.beans.ResponseHeader;
import com.cds.cbit.accounts.commons.beans.ResultInfo;
import com.cds.cbit.accounts.exceptions.BillingException;
import com.cds.cbit.accounts.exceptions.ValidationException;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


/**
 * The util class will provide exception handling logic to create failure message with provided
 * error code and message.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class ExceptionProcessor {
  
  @Autowired
  private Environment properties;
  
  /**
   * The method will create failure message with exception code and message and return it to
   * Controller, which intern send it to API gateway.
   * 
   * @param: header-  @Mandatory - Request header.
   * @param: message- @Mandatory - Error message from exception.
  */ 
  @Loggable
  public FailureMessage processException(final RequestHeader header,final Exception e) {
    e.printStackTrace();
    final Map<String,Object> errorMap = new ConcurrentHashMap<>();

    if (e instanceof ValidationException) {
      
      final StringBuilder error = new StringBuilder(properties.getProperty("100101"));
      errorMap.putAll(ImmutableMap.of(ConstantsUtil.ERROR_CODE,ConstantsUtil.FAILURE_MSG,
                                                        ConstantsUtil.RESULT_CODE,100101,
                       ConstantsUtil.ERROR_MSG,error.append(e.getMessage()).toString()));
      
    } else if (e instanceof BillingException) {
      final Optional<String> errorDetail = 
                                Optional.ofNullable(properties.getProperty(e.getMessage()));
      
      if (errorDetail.isPresent()) {
        errorMap.putAll(ImmutableMap.of(ConstantsUtil.ERROR_CODE,ConstantsUtil.FAILURE_MSG,
                                                  ConstantsUtil.RESULT_CODE,e.getMessage(),
                          ConstantsUtil.ERROR_MSG,properties.getProperty(e.getMessage())));
      } else {
        errorMap.putAll(getInterError());
      }
    } else {
      errorMap.putAll(getInterError());
    }
    final ResponseHeader respHeader = ResponseHeaderUtil.createResponseHead(header);
    
    final ResultInfo resultInfo = 
          ResultInfo.builder().resultCode((String)errorMap.get(ConstantsUtil.ERROR_CODE))
                 .resultCodeId(Integer.parseInt(errorMap.get(ConstantsUtil.RESULT_CODE).toString()))
                                  .resultMsg((String)errorMap.get(ConstantsUtil.ERROR_MSG)).build();
    final ErrorBody error = ErrorBody.builder().resultInfo(resultInfo).build();

    return FailureMessage.builder().head(respHeader).body(error).build();
  } // End of processing exception.
  
  private Map<String,Object> getInterError() {
    return ImmutableMap.of(ConstantsUtil.ERROR_CODE,ConstantsUtil.FAILURE_MSG,
                                                          ConstantsUtil.RESULT_CODE,100102,
                                   ConstantsUtil.ERROR_MSG,properties.getProperty("100102"));
  } // End of creating internal error details.
} // End of ExceptionProcessorUtil.