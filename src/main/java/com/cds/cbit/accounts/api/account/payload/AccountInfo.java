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

package com.cds.cbit.accounts.api.account.payload;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field mapping to AccountInfo section of account creation request JSON.
 * 
 * @author  Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo implements Serializable {

  private static final long serialVersionUID = 6426687635946578312L;

  @NotNull(message = "Attribute 'msisdn' not present in 'accountInfo' section of "
                     + "the account request.")
  protected String msisdn;  // Chosen mobile number 
  
  @NotNull(message = "Attribute 'subType' not present/invalid in 'accountInfo' section of "
                     + "the account request.")
  protected String subType;  // Sub type of account if preferred.
  
  @NotNull(message = "Attribute 'category' not present/invalid in 'accountInfo' section of "
                     + "the account request.")
  @Pattern(regexp = "GENERAL|INDIVIDUAL|CORPORATE", 
           message = "'category' in 'accountInfo' should be either GENERAL, "
                                                         + "INDIVIDUAL or CORPORATE.")
  protected String category; // Account category.
  
  @NotNull(message = "Attribute 'autoBoost' not present in 'accountInfo' section of "
                     + "the account request.")
  protected String autoBoost;  // Is autoBoose option enabled ?
  
  @NotNull(message = "Attribute 'billingDate' not present in 'accountInfo' section of "
                     + "the account request.")
  protected String billingDate;  // Starting date of billing for the account.
  
  protected String custConsent;  // Customer consent.

  protected PortinInfo portinInfo;  // Customer portin information if preferred.

  @Pattern(regexp = "INDIVIDUAL|OTHERS", 
          message = "'segment' in 'accountInfo' should be either INDIVIDUAL or OTHERS.")
  protected String segment;  // Account segment.

  protected String accountType;  // Type of account Prepaid | Postpaid | Hybrid.

  protected String invoiceLangPref;  // Language preference for invoice pdf.

  protected String sim;     // Chosen SIM if preferred.

  protected String locale;  // Customer local language.
} //End of AccountInfo section of customer account request.