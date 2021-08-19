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

package com.cds.cbit.accounts.api.customer.detail.payload;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field mapping to PersonalInfo section of customer detail response JSON.
 * 
 * @author  Meghashree Udupa
 * @version 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInfo implements Serializable {
  
  /** Auto generated serial version ID.   */
  private static final long serialVersionUID = -2510461207440649489L;

  private Address address;

  private String displayName;

  private Name name;

  private List<Ids> ids;

  private String dateOfBirth;

  private String altContact;

  private String email;
  
  private String autoBoost;
  
  private String category;

  private String segment;

  private String billingDate;
  
  private String accountType;

  private String invoiceLangPref;
  
  private PaymentInfo paymentInfo;
  
  private OrderInfo orderInfo;
  
  private String subType;
  
  private Boolean custConsent;
  
  private String nationality;
} // End of PersonalInfo POJO.