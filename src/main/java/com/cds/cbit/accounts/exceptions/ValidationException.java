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

package com.cds.cbit.accounts.exceptions;

/**
 * The BrmGenericException class is a custom exception of BRM Module which will  thrown by
 * BRM service factory when an unhandled exception occurs while processing BRM request.
 * 
 * @author  Venkata Nagaraju
 * @version 1.0.
*/
public class ValidationException extends RuntimeException {

  /** Generated serial version UUID. */
  private static final long serialVersionUID = -8258592053531210853L;

  /** 
   *  The constructor will through error message with combination of 
   *  error code and error message separated by : which can be split at
   *  factory level to prepare failure response.
  */
  public ValidationException(final String errorMsg) { 
    super(errorMsg);
  } // End of constructor.
  
  public ValidationException(final String errorMsg,final Exception e) { 
    super(errorMsg,e);
  } // End of constructor.
} // End of ValidationException.