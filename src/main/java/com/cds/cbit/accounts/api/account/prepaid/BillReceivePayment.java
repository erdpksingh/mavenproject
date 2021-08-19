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

package com.cds.cbit.accounts.api.account.prepaid;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents field mapping of BRM COMMIT_CUST_OPCODE.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@XmlRootElement(name = "flist")
@Data
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BillReceivePayment {

  @XmlElement(name = "PAYMENT")
  private BillPayment payment;

  @XmlElement(name = "END_T")
  private Long endT;

  @XmlElement(name = "AMOUNT")
  private String amount;
  
  @XmlElement(name = "CURRENCY")
  private String currency;

  @XmlElement(name = "PROGRAM_NAME")
  private String programName;

  @XmlElement(name = "VALID_TO")
  private Long validTo;

  @XmlElement(name = "VALID_FROM")
  private Long validFrom;

  @XmlElement(name = "NAME")
  private String name;

  @XmlElement(name = "POID")
  private String poid;
} // End of CommitCustOpcode.