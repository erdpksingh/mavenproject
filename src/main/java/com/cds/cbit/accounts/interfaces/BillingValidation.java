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

import com.portal.pcm.EBufException;
import com.portal.pcm.FList;

import javax.xml.bind.JAXBException;

/**
 * The interface will define a contract for validating given field information in billing system.
 * All components which need to validate a single request input in BRM will implement the contract.
 * 
 * @author  Venkata Nagaraju.
 * @Version 1.0.
*/
public interface BillingValidation {
  
  /*
   * The method will take a billing input field and verify its information validity in 
   * billing system.If validation is successful then the method will return field information 
   * as an FList otherwise it will throw a runtime exception.
  */
  FList validateInput(final String input,final String... data)  throws EBufException, JAXBException;
} // End of BillingValidationInterface