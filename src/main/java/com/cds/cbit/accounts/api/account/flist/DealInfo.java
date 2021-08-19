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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents field mapping of "deals" section of BRM COMMIT_CUST_OPCODE.
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
public class DealInfo {
  
  @XmlElement(name = "END_T")
  private Long endT;

  @XmlElement(name = "START_T")
  private Long startT;

  @XmlElement(name = "PRODUCTS")
  private List<ProductsOrDiscounts> products;

  @XmlElement(name = "FLAGS")
  private String flags;

  @XmlElement(name = "DISCOUNTS")
  private List<ProductsOrDiscounts> discounts;

  @XmlElement(name = "DESCR")
  private String descr;

  @XmlElement(name = "NAME")
  private String name;

  @XmlElement(name = "POID")
  private String poid;
  
  @XmlElement(name = "PLAN_OBJ")
  private String planObj;
} // End of DealInfo POJO