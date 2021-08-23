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

/**
 * Custom exception for handling all inventory related API errors which occurs while processing the
 * request in billing system.
 * 
 * @author  Nanda Kishore.
 * @Version 1.0.
*/
public class ValidationException extends RuntimeException {

  private static final long serialVersionUID = 4089475232470282413L;

  public ValidationException(final String message) {
    super(message);
  } // End of Constructor
  
  public ValidationException(final String message,final Exception e) {
    super(message,e);
  } // End of Constructor overloading with exception argument.
} // End of custom exception InventoryException.