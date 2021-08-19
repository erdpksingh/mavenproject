package com.cds.cbit.accounts.api.customer.detail.payload;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field mapping to OrderInfo section of customer detail response JSON.
 * 
 * @author  Meghashree Udupa
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo implements Serializable {

  /** Auto generated serial version ID.   */
  private static final long serialVersionUID = 5458237892603636371L;

  private String orderNumber;

  private String referralCode;

  private String checkoutDate;
  
  private String number;
} // End of OrderInfo POJO.
