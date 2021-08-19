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

package com.cds.cbit.accounts.api.customer.detail.flist;

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
 * The POJO represents field mapping of "LinkedObj" section of BRM OPCODE.
 * 
 * @author  Meghashree udupa.
 * @version 1.0.
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class LinkedObj implements Serializable {
  
  /** Auto generated serial version ID.   */
  private static final long serialVersionUID = -519847073127997793L;

  @XmlAttribute(name = "elem")
  private String elem;

  @XmlElement(name = "DEAL_OBJ")
  private String dealObj;

  @XmlElement(name = "PLAN_OBJ")
  private String planObj;
  
  @XmlElement(name = "LINK_DIRECTION")
  private String linkDirection;

  @XmlElement(name = "EXTRA_RESULTS")
  private ExtraResults extraResults;
  
  @XmlElement(name = "ACCOUNT_OBJ")
  private String accObj;
} // End of LinkedObj POJO.