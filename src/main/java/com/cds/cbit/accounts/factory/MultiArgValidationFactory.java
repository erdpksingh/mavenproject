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

import com.cds.cbit.accounts.interfaces.MultiArgValidation;

/**
 * The interface represents factory for MultiArgValidation interface. The interface used to fetch
 * the required bean which has a contract with MultiArgValidation interface.These beans will perform
 * billing related validations in BRM which required multiple inputs.The Factory bean for this 
 * interface will be available in FactoryBeanConfig class.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
public interface MultiArgValidationFactory {
  
  /*
   * The method will take bean name as input and load the appropriate bean at the runtime. The
   * factory can dynamically find the beans which has a contract with MultiArgValidation interface.
  */
  MultiArgValidation getValidator(String beanName);
} // End of MultiArgValidationFactory.