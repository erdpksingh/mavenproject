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

package com.cds.cbit.accounts.aspects;

import lombok.extern.log4j.Log4j2;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * AOP implementation for methods annotated with @Loggable. The aspect will provide logs for
 * Different advice like before,after return and after thrown of the methods annotated with
 * Loggable. The aspect will invoke transactional rest call util during the exception advice
 * For recording the failure information of the API.
 * 
 * @author: Venkata Nagaraju.
 * @version: 1.0.
*/
@Component
@Aspect
@Log4j2
public class MethodLogAspect {

  /** Defining pointcut of AOP annotation @Loggable. **/
  @Pointcut("@annotation(Loggable)")
  public void loggableMethods() {
    
    /** pointcut for logging. **/
  } // End of pointcut.

  /**
   * Advice which will create logs, before the method starts it execution. the advice
   * receives information like method,class and argument details from joinpoint.
  */
  @Before("loggableMethods()")
  public void logExecutionBeforeEvents(final JoinPoint joinPoint) {

    // Retrieving method related information from the joinpoint.
    
    final String methodName = joinPoint.getSignature().getName();
    final String currentClass = joinPoint.getSignature().getDeclaringType().getName();
    final StringBuilder sb = new StringBuilder();
    
    // Looping through all the arguments of the method and separating then with ','.
    
    for (final Object args : joinPoint.getArgs()) {
      sb.append(args).append(',');
    }
    log.info("Class Activated  : {}", currentClass);
    log.info("Method Invoked : {}", methodName);
  } // End of before advice.
  

  /** Advice which will create logs, after the service return the response. **/
  @AfterReturning(pointcut = "@annotation(Loggable)", returning = "returnValue")
  public void logExecutionAfterReturn(final JoinPoint joinPoint, final String returnValue) {

    // Displaying method name and returned value in the log.
    
    final String methodName = joinPoint.getSignature().getName();
    log.info("Method {} returned : {}", methodName, returnValue);
  } // End of after return advice.

  
  /** Advice which will create logs, after the service completes its execution. */
  @After("loggableMethods()")
  public void after(final JoinPoint joinPoint) {

    // Displaying method name and returned value in the log.
    final String methodName = joinPoint.getSignature().getName();
    log.info("Method : {} processing completed.",methodName);
  } // End of after advice.

  
  /** 
   * Advice which will create logs, after the service throws an exception. the method
   * will call transaction util to enter the transaction record regarding the API failure. 
  */
  @AfterThrowing(value = "loggableMethods()", throwing = "e")
  public void logAfterThrowingAllMethods(final JoinPoint joinPoint, final Exception e) {

    // Displaying method name and exception details in the log.
    
    final String methodName = joinPoint.getSignature().getName();
    log.error("Exception occurred while processing the method :{}", methodName);
    log.error("Exception : {}", e.getMessage());
    // Call transaction util here..
  } // End of after throws advice.
} // End of MethodLogAspect aspect.