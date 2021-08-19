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

import com.cds.cbit.accounts.api.account.balance.payload.CustomerBillDetails;
import com.cds.cbit.accounts.api.bill.details.payload.BillDetailRequest;
import com.cds.cbit.accounts.api.bill.details.payload.BillDetailResponse;
import com.cds.cbit.accounts.api.bill.details.payload.BillDetailResponseBody;
import com.cds.cbit.accounts.api.bill.details.payload.Result;
import com.cds.cbit.accounts.commons.beans.ResponseHeader;
import com.cds.cbit.accounts.commons.beans.ResultInfo;
import com.cds.cbit.accounts.exceptions.BillingException;
import com.cds.cbit.accounts.util.ResponseHeaderUtil;
import com.cds.cbit.accounts.util.UtcDateUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.fields.FldAdjusted;
import com.portal.pcm.fields.FldBillNo;
import com.portal.pcm.fields.FldCurrentTotal;
import com.portal.pcm.fields.FldDisputed;
import com.portal.pcm.fields.FldDue;
import com.portal.pcm.fields.FldEndT;
import com.portal.pcm.fields.FldPreviousTotal;
import com.portal.pcm.fields.FldRecvd;
import com.portal.pcm.fields.FldResults;
import com.portal.pcm.fields.FldStartT;
import com.portal.pcm.fields.FldSysDescr;
import com.portal.pcm.fields.FldTransfered;
import com.portal.pcm.fields.FldWriteoff;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * The Component will provide method to prepare responses for all type of customer search like 
 * Bill, Finance and Usage from the details retrieved from billing system.
 * 
 * @author Anuradha Manda.
 * @version 1.0
 * @author Meghashree Udupa.
 * @version 2.0
 *
 */
@Component
public class BillDetailsResponser {

  /**
   * The component class will prepare responses for all type of customer search like Bill, 
   * Finance and Usage from the details retrieved from billing system.
   * 
   * @param- request
   * @param- outputFlist
   */
  public BillDetailResponse createBillDetailResponse(
                   final BillDetailRequest request,final FList outputFlist) throws EBufException {
    
    final String accountNo = request.getBody().getAccountNumber();

    final List<CustomerBillDetails> billDetailsList = new ArrayList<>();

    if (!outputFlist.get(FldSysDescr.getInst()).equalsIgnoreCase("Data not found")) {
      
      final List<FList> resultFlist = outputFlist.get(FldResults.getInst()).getValues();
      
      resultFlist.forEach(f -> {
        final CustomerBillDetails billDetails = new CustomerBillDetails();
        try {
          billDetails.setAdjusted(f.get(FldAdjusted.getInst())
                                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
          billDetails.setDisputed(f.get(FldDisputed.getInst())
                                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
          billDetails.setRecvd(f.get(FldRecvd.getInst()).setScale(2, BigDecimal.ROUND_HALF_UP));
          billDetails.setTransferred(f.get(FldTransfered.getInst())
                                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
          billDetails.setWriteOff(f.get(FldWriteoff.getInst())
                                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
          billDetails.setDue(f.get(FldDue.getInst()).setScale(2, BigDecimal.ROUND_HALF_UP));
          billDetails.setCurrentTotal(f.get(FldCurrentTotal.getInst())
                                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
          billDetails.setPrevTotal(f.get(FldPreviousTotal.getInst())
                                                        .setScale(2, BigDecimal.ROUND_HALF_UP));
          billDetails.setBillNo(f.get(FldBillNo.getInst()));
          billDetails.setStartDate(String.valueOf(UtcDateUtil
                                            .convertDateToDateTime(f.get(FldStartT.getInst()))));
          billDetails.setEndDate(String.valueOf(UtcDateUtil
                                              .convertDateToDateTime(f.get(FldEndT.getInst()))));
        } catch (EBufException e) {
          throw new BillingException(e.getMessage(),e);
        }
        billDetailsList.add(billDetails);
      });
    }
    final Result results = 
          Result.builder().accountNumber(accountNo).billDetails(billDetailsList).build();

    final ResultInfo resultInfo = 
          ResultInfo.builder().resultCode("SUCCESS")
                       .resultCodeId(0).resultMsg("Bills fetched successfully.").build();
    
    final BillDetailResponseBody responseBody = 
          BillDetailResponseBody.builder().result(results).resultInfo(resultInfo).build();
    final ResponseHeader header = ResponseHeaderUtil.createResponseHead(request.getHead());
    
    return BillDetailResponse.builder().head(header).body(responseBody).build();
  } // End of createBillDetail method.
} // End of BillDetailsResponser class.