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

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field ARGS mapping to PCM_OP_SEARCH opcode.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Args implements Serializable {
  
  private static final long serialVersionUID = 4900867569632151830L;

  @XmlAttribute(name = "elem")
  private String elem;  // Element attribute of the field

  @XmlElement(name = "DEVICE_ID")
  private String deviceId;  // device value field MSISDN | SIM value.
  
  @XmlElement(name = "ACCOUNT_OBJ")
  private String accountObj;  // Account object field.
  
  @XmlElement(name = "POID")
  private String poid;  // input POID   account | device | inventory.
  
  @XmlElement(name = "ACCOUNT_NO")
  private String accountNumber;  // Account number field
  
  @XmlElement(name = "NAME")
  private String name; // Input name field.
  
  @XmlElement(name = "CODE")
  private String code; // Input code field.
  
  @XmlElement(name = "MEMEBERS")
  private Members member; // account members field.
  
  @XmlElement(name = "SERVICES")
  private List<Services> services;
  
  @XmlElement(name = "DEAL_OBJ")
  private String dealObj;
  
  @XmlElement(name = "STATUS")
  private String status;
  
  @XmlElement(name = "SERVICE_OBJ")
  private String serviceObj;
  
  @XmlElement(name = "CREATED_T")
  private Long createdT;  // Created date.
  
  @XmlElement(name = "PLAN_OBJ")
  private String planObj;
} //End of Args, which represent PCM_OP_SEARCH opcode "args" field.