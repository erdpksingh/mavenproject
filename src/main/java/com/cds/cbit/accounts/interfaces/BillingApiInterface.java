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

package com.cds.cbit.accounts.interfaces;

/**
 * The interface will define the contract between ApiGateway and all the Billing APIs.All the
 * service endpoints of billing system should implement this contract for executing the request.
 * 
 * @author  Venkata Nagaraju.
 * @Version 1.0.
*/
public interface BillingApiInterface {
  
  /*
   * The method will take API payload as a request and process it in appropriate service and return 
   * a generic response as a a success or failure message to API gateway. All API should override 
   * this method to provide their implementation.
  */
  GenericResponse processApiRequest(final String payload);
} // End of BillingApiInterface