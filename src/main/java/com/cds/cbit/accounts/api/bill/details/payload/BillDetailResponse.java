package com.cds.cbit.accounts.api.bill.details.payload;

import com.cds.cbit.accounts.commons.beans.ResponseHeader;
import com.cds.cbit.accounts.interfaces.GenericResponse;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents response payload which contains head and body of Bill Detail Response.
 * 
 * @author Meghashree Udupa.
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailResponse implements GenericResponse, Serializable {
  /** Auto generated serial version ID. */
  private static final long serialVersionUID = 5251867694671208625L;

  private ResponseHeader head;

  private BillDetailResponseBody body;
} // End of BillDetailResponse POJO.
