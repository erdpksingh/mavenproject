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

package com.cds.cbit.accounts.api.account.flist;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents field mapping of "invoiceInfo" section of BRM COMMIT_CUST_OPCODE.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Data
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceInfo {
  
  @XmlAttribute(name = "elem")
  private String elem;

  @XmlElement(name = "ZIP")
  private String zip;

  @XmlElement(name = "DELIVERY_PREFER")
  private String deliveryPreference;

  @XmlElement(name = "INV_INSTR")
  private String invInstr;

  @XmlElement(name = "CITY")
  private String city;

  @XmlElement(name = "COUNTRY")
  private String country;

  @XmlElement(name = "DELIVERY_DESCR")
  private String deliveryDescription;

  @XmlElement(name = "ADDRESS")
  private String address;

  @XmlElement(name = "EMAIL_ADDR")
  private String emailAddress;

  @XmlElement(name = "STATE")
  private String state;

  @XmlElement(name = "INV_TERMS")
  private String invoiceTerms;

  @XmlElement(name = "NAME")
  private String name;
} // End of InvoiceInfo