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

package com.cds.cbit.accounts.api.account.balance.flists;

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
 * The POJO provide field SERVICES in RESULTS mapping to PCM_OP_SEARCH opcode.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class GrantLinkedObj implements Serializable {
  
  private static final long serialVersionUID = 1735999492388024638L;

  @XmlAttribute(name = "elem")
  private String elem;  // Element attribute of service field.

  @XmlElement(name = "LINK_DIRECTION")
  private String linkDirection;  
  
  @XmlElement(name = "DEAL_OBJ")
  private String dealObj;
  
  @XmlElement(name = "EXTRA_RESULTS")
  private ExtraResults extraResults;
} //End of Services, which represents PCM_OP_SEARCH 'services' filed.