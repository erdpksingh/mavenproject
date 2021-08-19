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

package com.cds.cbit.accounts.commons.factory;

import com.cds.cbit.accounts.factory.BillingValidationFactory;
import com.cds.cbit.accounts.factory.MultiArgValidationFactory;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.interfaces.MultiArgValidation;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The factory class provides method for fetching appropriate validator bean that implements 
 * BillingValidationInterface for validating the given input. If the validation is success
 * then the method will return output FList otherwise it will throw an exception to the caller.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class BillingInputValidateFactory {
  
  @Autowired
  private BillingValidationFactory factory; 
  
  @Autowired
  private MultiArgValidationFactory multiArgfactory; 
  
  /**
   * The method takes bean name that implements BillingValidationInterface and the input need to 
   * be validated and invoke the validateInput method to validate the bean in the billing system.
   * 
   * @param: inputValue  @Mandatory: bean name.
   * @param: beanName    @Mandatory: Input need to be validated.
   * @return output FList of the billing search.
  */
  public FList getValidator(final String beanName,final String inputValue) 
                                                             throws EBufException, JAXBException {
    
    // Fetching component with the given bean name.
    final BillingValidation validator = factory.getValidator(beanName);
    
    return validator.validateInput(inputValue);  // Validating the input.
  } // End of getValidator.
  
  /**
   * The method takes bean name that implements MultiArgValidationInterface and the input need to 
   * be validated and additional arguments as varargs data and invoke the validateInput method to 
   * validate the bean in the billing system.
   * 
   * @param: inputValue  @Mandatory: bean name.
   * @param: beanName    @Mandatory: Input need to be validated.
   * @return output FList of the billing search.
  */
  public FList getValidatorWithMultiArgs(
                               final String beanName,final String inputValue,final String...data)
                                                            throws EBufException, JAXBException {
    
    // Fetching component with the given bean name.
    final MultiArgValidation validator = multiArgfactory.getValidator(beanName);
    
    return validator.validateInput(inputValue,data);  // Validating the input.
  } // End of getValidator.
} // End of BillingValidatorFactory.