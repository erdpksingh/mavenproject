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

package com.cds.cbit.accounts.api.bill.details;

import com.cds.cbit.accounts.api.bill.details.payload.BillDetailRequest;
import com.cds.cbit.accounts.api.bill.details.payload.BillDetailResponse;
import com.cds.cbit.accounts.factory.BillingValidationFactory;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.properties.AccountConfigProperties;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import java.text.ParseException;
import javax.xml.bind.JAXBException;
import org.springframework.stereotype.Component;

/**
 * The component class provides factory methods, which will dynamically execute
 * the services which will perform the requested operation. The factories
 * autowired in the component will use service locator pattern for finding the
 * service beans dynamically.
 * 
 * @author Anuradha Manda.
 * @version 1.0.
*/
@Component
public class BillDetailProcessor {

  private final BillingValidationFactory factory;
  private final BillDetailsFList billDetailsFList;
  private final BillDetailsResponser responseCreator;
  private final AccountConfigProperties properties;

  /** Constructor Injection. */
  public BillDetailProcessor(
         final BillingValidationFactory factory, 
         final BillDetailsFList billDetailsFList, final BillDetailsResponser responseCreator,
                                                  final AccountConfigProperties properties) {
    this.factory = factory;
    this.billDetailsFList = billDetailsFList;
    this.responseCreator = responseCreator;
    this.properties = properties;
  } // End of Constructor injection.
  
  /** 
   * Method to get all customer bill detail from billing system by invoking appropriate
   * search in billing system. 
   * @param: request
   * @throws: EBufException
   * @throws: JAXBException
   * @throws: ParseException
   */
  public BillDetailResponse processBillDetails(final BillDetailRequest request)
                                       throws EBufException, ParseException, JAXBException {

    final StringBuilder acctProfile = new StringBuilder(properties.getType())
                                                                      .append("Profile");
    final BillingValidation validator = factory.getValidator("accountValidator");
    validator.validateInput(request.getBody().getAccountNumber(),acctProfile.toString());

    final FList outputFlist = billDetailsFList.getBillDetails(request.getBody()
                                                           .getAccountNumber(), request);
    return responseCreator.createBillDetailResponse(request, outputFlist);
  } // End of processBillDetails method.
} // End of BillDetailProcessor class.