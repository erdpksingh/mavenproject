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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

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
@Data
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServices {

  @XmlAttribute(name = "elem")
  private String elem;

  @XmlElement(name = "DEAL_OBJ")
  private String dealObj;

  @XmlElement(name = "DEALS")
  private ServiceDeals deals;

  @XmlElement(name = "DEVICES")
  private ServiceDevices devices;

  @XmlElement(name = "SERVICE_ID")
  private String serviceId;

  @XmlElement(name = "SUBSCRIPTION_INDEX")
  private String subscriptionIndex;

  @XmlElement(name = "SERVICE_OBJ")
  private String serviceObj;

  @XmlElement(name = "BAL_INFO_INDEX")
  private String balInfoIndex;
} // End of CommitCustOpcode.