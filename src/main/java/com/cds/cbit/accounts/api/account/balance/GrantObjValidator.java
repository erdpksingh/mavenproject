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

package com.cds.cbit.accounts.api.account.balance;

import com.cds.cbit.accounts.api.account.balance.flists.ExtraResults;
import com.cds.cbit.accounts.api.account.balance.flists.GrantLinkedObj;
import com.cds.cbit.accounts.api.account.balance.flists.GrantObjResults;
import com.cds.cbit.accounts.api.account.balance.flists.GrantObjSearch;
import com.cds.cbit.accounts.commons.beans.Args;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;

import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will provide method to search grant obj information in BRM.
 * 
 * @author  Venkata Nagaraju
 * @version 1.0.
*/
@Component
public class GrantObjValidator {
  
  @Autowired
  private BrmServiceUtil brmUtil;
  
  protected FList executeGrantObj(final String purchaseType,final String grantorObj) 
                                                       throws JAXBException, EBufException {
    
    final Args accountArgs = Args.builder().elem("1").dealObj("").build();
    final Args poidArgs    = Args.builder().elem("2").poid("").build();
    final Args acctNoArgs  = Args.builder().elem("3").poid(grantorObj).build();
   
    final Args[] arguments = {accountArgs,poidArgs,acctNoArgs};
    
    final ExtraResults extraResult = 
          ExtraResults.builder().elem("0").name("").permitted("").build();
    final GrantLinkedObj linkedObj = 
          GrantLinkedObj.builder().linkDirection("-1").elem("2")
                                              .dealObj("").extraResults(extraResult).build();
    final GrantObjResults results = 
          GrantObjResults.builder().planObj("").poid("").linkedObj(linkedObj).build();
    
    final StringBuilder query = 
        new StringBuilder("select X from ").append(purchaseType)
                                           .append(" 1, /deal 2 where 1.F1=2.F2 and 1.F3=V3");
    final GrantObjSearch grantSearch = 
          GrantObjSearch.builder().args(Arrays.asList(arguments)).results(Arrays.asList(results))
                .flags("256").poid(ConstantsUtil.SEARCH_POID).template(query.toString()).build();
    
    return brmUtil.executeInputInBrm(grantSearch,7,GrantObjValidator.class,GrantObjSearch.class);
  } // End of executeGrantObj method.
} // End of GranObjSearch.