/*
 * Copyright (C) 2019 Covalense Technologies 
 *
 * Licensed under the Covalense CBIT, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.covalensedigital.com
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
*/

package com.cds.cbit.accounts.api.bill.details.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
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
 * The POJO represents Results section of BillDetail opcode of BRM.
 * 
 * @author Anuradha Manda.
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Results implements Serializable {

  /** Generated serialVersionUID. */
  private static final long serialVersionUID = -5249965209670027839L;

  @XmlAttribute(name = "elem")
  private String elem;

  @XmlElement(name = "POID")
  private String poid;
  
  @XmlElement(name = "ACCOUNT_OBJ")
  private String accountObj;
  
  @XmlElement(name = "CREATED_T")
  private Long createdT;

  @XmlElement(name = "STATUS")
  private String status;
} // End of class Results.