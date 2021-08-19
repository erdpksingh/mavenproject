package com.cds.cbit.accounts.api.bill.details.payload;

import com.cds.cbit.accounts.commons.beans.ResultInfo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents request payload. which contains startDate,endDate, type of bill and
 * accountNumber information.
 * 
 * @author Meghashree Udupa.
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailResponseBody implements Serializable {
  
  /** Auto generated serial version ID. */
  private static final long serialVersionUID = 684061536541960490L;

  private Result result;

  private ResultInfo resultInfo;
} // End of BillDetailResponseBody POJO.
