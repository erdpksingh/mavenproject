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

package com.cds.cbit.accounts.util;

import com.cds.cbit.accounts.commons.beans.Args;
import com.cds.cbit.accounts.commons.beans.Results;
import com.cds.cbit.accounts.commons.beans.SearchTemplate;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.PortalContext;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Util class will provide method(s) for generating search template with the given inputs,and
 * provide business logic for executing them in the billing system.
 * 
 * @author  Venkata Nagaraju.
 * @Version 1.0.
*/
@Component
public class BillingInfoSearchUtil {
  
  @Autowired
  public BrmServiceUtil brmUtil;  // Billing util which executes given input in BRM.

  /**
   * The method will take inputs requires to create search template POJO,create the POJO with input,
   * creates Args and Results fields of search template and convert the search template to FList and
   * execute it in billing system with the help of BrmUtil.
   * 
   * @param- flags    @Optional  - Billing system opcode flag
   * @param- template @Mandatory - Query for fetching information from Billing system
   * @param- args     @Mandatory - Input elements for search query
   * @param- results  @Mandatory - Output elements of search
   * 
   * @return- output FList of billing system if opcode execution is success in billing system.
  */
  public FList executeSearchTemplate(
          final String template,final Args[] arguments,final Results result,final String... flags) 
                                                               throws EBufException, JAXBException {
      
    final SearchTemplate search =  createSearchTemplate(template,arguments,result,flags);
          
    return brmUtil.executeInputInBrm(search, 7, SearchTemplate.class,Results.class,Args.class);
  } // End of executeSearchTemplate
  
  /**
   * The method will especially invoked during a global transaction and it take inputs requires to
   * create search template POJO,create the POJO with input,creates Args and Results fields of 
   * search template and convert the search template to FList and execute it in billing system with
   * the help of BrmUtil.
   * 
   * @param- flags    @Optional  - Billing system opcode flag
   * @param- template @Mandatory - Query for fetching information from Billing system
   * @param- args     @Mandatory - Input elements for search query
   * @param- results  @Mandatory - Output elements of search
   * @param- portal   @Mandatory - BRM CM connection
   * 
   * @return- output FList of billing system if opcode execution is success in billing system.
  */
  public FList executeTransactionSearchTemplate(
              final String template,final Args[] arguments,final Results result,
              final PortalContext portal,final String... flags) throws EBufException,JAXBException {
      
    final SearchTemplate search =  createSearchTemplate(template,arguments,result,flags);
      
    final JAXBContext jaxbContext = JAXBContext.newInstance(SearchTemplate.class);
    final FList flist = brmUtil.getFListFromPojo(jaxbContext,search);
    return portal.opcode(7, flist);
  } // End of executeSearchTemplate.
  
  /**
   * The method create PCM_OP_SEARCH opcode of BRM to search given information with the given input.
  */
  private SearchTemplate createSearchTemplate(
          final String template,final Args[] arguments,final Results result,final String... flags) {
    
    final List<Args> args = Arrays.asList(arguments); 
    final List<Results> results = Arrays.asList(result);
            
    final String flag  = flags.length > 0 ? flags[0] : ConstantsUtil.SEARCH_FLAG;
      
    return SearchTemplate.builder().flags(flag).poid(ConstantsUtil.SEARCH_POID)
                                           .args(args).template(template).results(results).build();
  } // End of createSearchTemplate method.
} // End of BillingInfoSearchUtil which will create and execute search template.