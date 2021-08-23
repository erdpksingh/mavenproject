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

package com.cds.cbit.apigateway.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The configuration class provides bean for JdbcTemplate.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Configuration
public class JdbcConfig {
  
  @Bean
  public JdbcTemplate getJdbcTemplate(final DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  } // End of JdbcTemplate bean.
} // End of JdbcConfig.