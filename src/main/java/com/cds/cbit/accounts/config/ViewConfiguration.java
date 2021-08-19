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

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The configuration class will redirect the view requests to swagger document.
 *  
 * @author Venkata Nagaraju
 * @Version 1.0.
*/
@Configuration
public class ViewConfiguration implements WebMvcConfigurer {

  @Override
  public void addViewControllers(final ViewControllerRegistry registry) {
    
    registry.addRedirectViewController("/", "/index.html");
  } // End of view controller.
} // End of redirecting view requests to swagger.