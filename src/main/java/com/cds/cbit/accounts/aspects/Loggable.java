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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * AOP annotation which will work as a pointcut to MethodLogAspect for logging different
 * parts of method execution like receiving input [arguments], after completion and
 * after thrown or exception case advice. The annotation will be used by methods for which
 * logs are required. 
 * 
 * @author: Venkata Nagaraju.
 * @version: 1.0.
*/
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Loggable {
} // end of loggable AOP annotation.