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

package com.cds.cbit.accounts.commons.beans;

import com.cds.cbit.accounts.api.account.flist.NameInfo;
import com.cds.cbit.accounts.api.customer.detail.flist.LinkedObj;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field RESULTS mapping to PCM_OP_SEARCH opcode.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Results implements Serializable {

  private static final long serialVersionUID = -7838714978288849304L;

  @XmlAttribute(name = "elem")
  private String elem;  // element attribute of results field.

  @XmlElement(name = "STATE_ID")
  private String stateId;  // device state id field.

  @XmlElement(name = "SERVICES")
  private Services services;  // 
  
  @XmlElement(name = "ACCOUNT_OBJ")
  private String accountObj;

  @XmlElement(name = "CREATED_T")
  private Long createdT;  // Created date.

  @XmlElement(name = "POID")
  private String poid;  // result poid.
  
  @XmlElement(name = "DEAL_OBJ")
  private String dealObj;
  
  @XmlElement(name = "LINKED_OBJ")
  private LinkedObj linkedObj;

  @XmlElement(name = "PLAN_OBJ")
  private String planObj;

  @XmlElement(name = "PACKAGE_ID")
  private String packageId;
  
  @XmlElement(name = "QUANTITY")
  private String quantity;
  
  @XmlElement(name = "PURCHASE_END_T")
  private Long purchaseEndT;
  
  @XmlElement(name = "PURCHASE_START_T")
  private Long purchaseStartT;
  
  @XmlElement(name = "DEVICE_ID")
  private String deviceId;
  
  @XmlElement(name = "NAMEINFO")
  private NameInfo nameInfo;
  
  @XmlElement(name = "SERVICE_OBJ")
  private String serviceObj;
} //End of Results, which represents PCM_OP_SEARCH 'result' field.