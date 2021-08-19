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

import com.cds.cbit.accounts.api.bill.details.payload.BillDetailFlist;
import com.cds.cbit.accounts.api.bill.details.payload.BillDetailRequest;
import com.cds.cbit.accounts.exceptions.BillingException;
import com.cds.cbit.accounts.util.BrmServiceUtil;
import com.cds.cbit.accounts.util.JodaDateUtil;
import com.cds.cbit.accounts.util.UtcDateUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.fields.FldDescr;

import java.text.ParseException;
import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The REST controller provide end point for bill detail related billing resources. The class 
 * expose various HTTP methods for exposing bill detail information to API gateway.
 * 
 * @author Meghashree Udupa.
 *
 */
@Component
@Log4j2
public class BillDetailsFList {

  @Autowired
  private BrmServiceUtil brmServiceUtil;

  /**
   * This method will retrieve customer bill details for the given account number from
   * billing system.
   * 
   * @param- accountNumber
   * @param- BillDetailRequest
   */
  public FList getBillDetails(final String accountNumber,final BillDetailRequest request)
                                           throws ParseException, EBufException, JAXBException {

    final String startT = request.getBody().getStartDate(); // StartDate of bill.
    final String endT = request.getBody().getEndDate(); // StartDate of bill.

    dateValidation(request);

    final BillDetailFlist inputFlist = 
          BillDetailFlist.builder().accountNo(accountNumber)
                         .endT(UtcDateUtil.convertStringToTimeStamp(endT))
                         .orderType(request.getBody().getType()).poid("0.0.0.1 /account -1 0")
                         .startT(UtcDateUtil.convertStringToTimeStamp(startT)).build();
    // Executing billDetail in BRM.
    final FList outputFlist = 
                brmServiceUtil.executeInputInBrm(inputFlist, 300051,BillDetailFlist.class);
    
    log.info("outputFlist{}",String.valueOf(outputFlist));
    if (outputFlist.get(FldDescr.getInst()).equals("Failed")) {
      throw new BillingException("-1000201");
    }
    return outputFlist;
  } // End of getBillDetails method.
  
  /** Method to validate to and from date for bill detail APIs. */
  private void dateValidation(final BillDetailRequest request) throws ParseException {
    
    final String start = request.getBody().getStartDate();
    final Long startDateDiff = JodaDateUtil.findDifferenceWithCurrentDate(start);
    if (startDateDiff > 0) {
      throw new BillingException("-1000202");
    }
    
    final String end = request.getBody().getEndDate();
    final Long endDateDiff = JodaDateUtil.findDifferenceWithCurrentDate(end);
    if (endDateDiff < startDateDiff) {
      throw new BillingException("-1000203");
    }
  } // End of dateValidation method.
} // End of class BillDetailValidator.