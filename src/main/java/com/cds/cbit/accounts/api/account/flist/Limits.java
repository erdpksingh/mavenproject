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
 * The POJO represents limitarray.
 * 
 * @author Saibabu Guntur.
 * @version 1.0.
 */
@Data
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class Limits {

  @XmlAttribute(name = "elem")
  private String elem;

  @XmlElement(name = "CREDIT_LIMIT")
  private String creditLimit;

  @XmlElement(name = "CREDIT_THRESHOLDS_FIXED")
  private String creditThresholdsFixed;

  @XmlElement(name = "CREDIT_THRESHOLDS")
  private String creditThresholds;

  @XmlElement(name = "CREDIT_FLOOR")
  private String creditFloor;
} // End of Limits.