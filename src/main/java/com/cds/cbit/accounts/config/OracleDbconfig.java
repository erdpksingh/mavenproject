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

import com.cds.cbit.accounts.properties.DbProperties;

import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The configuration class will define bean for creating oracle database connection with the
 * properties mentioned in the application property file.
 * 
 * @author Venkata Nagaraju.
 * @version 1.0.
 */
@Configuration
public class OracleDbconfig {

  @Autowired
  private DbProperties properties;

  /**
   * Method to create Oracle data source object with properties mentioned in application property
   * file.
   */
  @Bean
  public OracleDataSource getDataSource() throws SQLException {

    final OracleDataSource dataSource = new OracleDataSource();
    dataSource.setUser(properties.getUsername());
    dataSource.setPassword(properties.getPassword());
    dataSource.setURL(properties.getUrl());
    dataSource.setImplicitCachingEnabled(true);
    dataSource.setFastConnectionFailoverEnabled(true);
    return dataSource;
  } // End of creating oracle data source.

  /** Bean for creating spring JDBC template. **/
  @Bean
  public JdbcTemplate getJdbcTemplate() throws SQLException {
    return new JdbcTemplate(getDataSource());
  } // End of JdbcTemplate bean.
} // End of OracleDbconfig configuration file.