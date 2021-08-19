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

import com.cds.cbit.accounts.api.account.balance.flists.PackageFetchLinkedObj;
import com.cds.cbit.accounts.api.account.balance.flists.PackageFetchResults;
import com.cds.cbit.accounts.api.account.balance.flists.PackageFetchSearch;
import com.cds.cbit.accounts.aspects.Loggable;
import com.cds.cbit.accounts.commons.beans.Args;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;

import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The implementation component provides methods for validating given account inputs in the system.
 * The component overrides BillingValidationInterface validateInput method to create and execute 
 * account search FList.If the validation is success then the method will return output FList else
 * it will throw an exception to the caller.
 * 
 * @author  Nistha Khare.
 * @version 1.0.
*/
@Component("packageFetchValidator")
public class PackageFetchValidator implements BillingValidation {
  
  @Autowired
  private BrmServiceUtil brmUtil;
  
  /* @see com.cds.cbit.inventory.interfaces.BillingValidation#validateInput. */
  @Override
  @Loggable
  public FList validateInput(final String input,final String... data) 
                                                          throws EBufException, JAXBException {
    final PackageFetchLinkedObj linkObj = 
          PackageFetchLinkedObj.builder().elem("2").planObj("").extraResults("")
                                                                  .linkDirection("-1").build();
    final PackageFetchResults results = 
          PackageFetchResults.builder().elem("0").packageId("0").planObj("").quantity("0")
                           .purchaseEndT(0L).purchaseStartT(0L).createdT(0L)
                                                              .linkedObj(linkObj).build();
    // Preparing Args section of PCM_OP_SEARCH.
    final Args planObjArgs = Args.builder().elem("1").planObj("").build();
    final Args poidArgs    = Args.builder().elem("2").poid("").build();
    final Args acctNoArgs  = Args.builder().elem("3").accountObj(input).build();
    final Args planArgs    = Args.builder().elem("4").planObj("").build();
    final Args createdArgs = Args.builder().elem("5").createdT(0L).build();
    
    final Args[] arguments = {planObjArgs,poidArgs,acctNoArgs,planArgs,createdArgs};
    
    final PackageFetchSearch search = 
          PackageFetchSearch.builder().args(Arrays.asList(arguments))
              .results(Arrays.asList(results)).flags("1280").poid(ConstantsUtil.SEARCH_POID)
                                         .template(ConstantsUtil.PACKAGE_FETCH_SQL).build();
    return brmUtil.executeInputInBrm(
                           search, 7, PackageFetchValidator.class,PackageFetchSearch.class);
  } // End of validating given account number in BRM.
} // End of AccountValidator, which validated given account in BRM.