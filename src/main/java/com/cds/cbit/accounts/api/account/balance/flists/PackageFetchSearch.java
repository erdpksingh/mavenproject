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

import com.cds.cbit.accounts.commons.beans.Args;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provide field mapping to PCM_OP_SEARCH opcode.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "flist")
@XmlAccessorType(XmlAccessType.FIELD)
public class PackageFetchSearch implements Serializable {
  
  private static final long serialVersionUID = 3423020629039276761L;

  @XmlElement(name = "FLAGS")
  private String flags;  // Flags field of search template.

  @XmlElement(name = "TEMPLATE")
  private String template;  // template field of search template. [SQL query]

  @XmlElement(name = "POID")
  private String poid;  // Search poid.

  @XmlElement(name = "RESULTS")
  private List<PackageFetchResults> results;  // Results field of search template.

  @XmlElement(name = "ARGS")
  private List<Args> args;  // Args field of search template.
} //End of SearchTemplate