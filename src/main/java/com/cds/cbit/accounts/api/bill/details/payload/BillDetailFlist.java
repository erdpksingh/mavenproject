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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO provides mapping fields for BillDetail opcode of BRM.
 * 
 * @author Anuradha Manda.
 * @version : 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "flist")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillDetailFlist implements Serializable {

  /** Generated serial version UID. */
  private static final long serialVersionUID = 4743375115537331136L;

  @XmlElement(name = "END_T")
  private Long endT;

  @XmlElement(name = "START_T")
  private Long startT;

  @XmlElement(name = "ORDER_TYPE")
  private String orderType;

  @XmlElement(name = "ACCOUNT_NO")
  private String accountNo;

  @XmlElement(name = "POID")
  private String poid;
} // End of class BillDetailFlist.