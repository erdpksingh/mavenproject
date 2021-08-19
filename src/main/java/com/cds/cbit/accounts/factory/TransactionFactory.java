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

package com.cds.cbit.accounts.factory;

import com.cds.cbit.accounts.interfaces.TransactionService;

/**
 * The interface represents factory for AccountServices interface. The interface used to fetch
 * the required bean which has a contract with AccountServices interface. These beans will perform
 * account service related operations in BRM. The Factory bean for this interface will be available
 * in FactoryBeanConfig class.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
public interface TransactionFactory {
  
  /*
   * The method will take service name as input and load the appropriate bean at the runtime. The
   * factory can dynamically find the beans which has a contract with AccountServices interface.
  */
  TransactionService process(String serviceName);
} // End of AccountServiceFactory.