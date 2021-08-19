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

package com.cds.cbit.accounts.interfaces;

import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.PortalContext;

import java.text.ParseException;
import java.util.Map;

import javax.xml.bind.JAXBException;

/**
 * The interface will define the contract for creating a billing account, different types of 
 * accounts like flat,hierarchical will implement this contract.
 * 
 * @author  Venkata Nagaraju.
 * @Version 1.0.
*/
public interface TransactionService {
  
  /*
   * The method will take either flat or hierarchical accountno and process the service operations
   * related to that account with the services who implemented this contract.
  */
  FList processTransaction(final Map<String,Object> inputs,final PortalContext portal)
                                               throws EBufException, JAXBException,ParseException;
} // End of AccountCreator.