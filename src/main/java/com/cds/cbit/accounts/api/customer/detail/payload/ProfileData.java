package com.cds.cbit.accounts.api.customer.detail.payload;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field mapping to ProfileData section of customer detail response JSON.
 * 
 * @author  Meghashree Udupa
 * @Version 1.0.
*/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileData implements Serializable {

  /** Auto generated serial version ID. */
  private static final long serialVersionUID = 5458237892603636371L;

  private String value;

  private String name;

  private String noteStr;

  private String categoryName;
} // End of ProfileData POJO.
