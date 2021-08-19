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

package com.cds.cbit.accounts.config;

import com.cds.cbit.accounts.factory.AccountServiceFactory;
import com.cds.cbit.accounts.factory.BillingValidationFactory;
import com.cds.cbit.accounts.factory.MultiArgValidationFactory;
import com.cds.cbit.accounts.factory.TransactionFactory;
import com.cds.cbit.accounts.factory.TransactionValidationFactory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration class will define beans for factory interfaces through service locator pattern.
 * Configured beans will identify services attached to a particular factory dynamically at runtime.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Configuration
@SuppressWarnings("rawtypes")
public class BeanFactoryConfig {

  /** The bean will define Factory bean for AccountServiceFactory interface. */
  @Bean
  public FactoryBean serviceactoryBean() {
    
    final ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
    factoryBean.setServiceLocatorInterface(AccountServiceFactory.class);
    return factoryBean;
  } // End of factory bean for AccountServices.
  
  /** The bean will define Factory bean for BillingValidationFactory interface. */
  @Bean
  public FactoryBean validatorFactoryBean() {
    
    final ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
    factoryBean.setServiceLocatorInterface(BillingValidationFactory.class);
    return factoryBean;
  } // End of factory bean for BillingValidationInterface.
  
  /** The bean will define Factory bean for BillingValidationFactory interface. */
  @Bean
  public FactoryBean transactionBean() {
    
    final ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
    factoryBean.setServiceLocatorInterface(TransactionValidationFactory.class);
    return factoryBean;
  } // End of factory bean for BillingValidationInterface.
  
  /** The bean will define Factory bean for MultiArgValidationFactory interface. */
  @Bean
  public FactoryBean multiArgvalidatorFactoryBean() {
    
    final ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
    factoryBean.setServiceLocatorInterface(MultiArgValidationFactory.class);
    return factoryBean;
  } // End of factory bean for MultiArgValidationInterface.
  
  /** The bean will define Factory bean for MultiArgValidationFactory interface. */
  @Bean
  public FactoryBean accountFactoryBean() {
    
    final ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
    factoryBean.setServiceLocatorInterface(TransactionFactory.class);
    return factoryBean;
  } // End of factory bean for MultiArgValidationInterface.
} // End of BeanFactoryConfig which configures factory beans.