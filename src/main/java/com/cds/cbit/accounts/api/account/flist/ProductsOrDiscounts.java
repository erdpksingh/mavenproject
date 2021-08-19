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

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO represents the product and discount section of the unsubscription FList.
 * 
 * @author Soundharya Nadagouda.
 * @version 1.0
*/
@XmlRootElement(name = "flist")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductsOrDiscounts implements Serializable {

  private static final long serialVersionUID = 7004864542013374924L;

  @XmlAttribute(name = "elem")
  private String elem; //Element attribute of the field.
  
  @XmlElement(name = "POID")
  private String packageId;
  
  @XmlElement(name = "PACKAGE_ID")
  private String packageIdNumber;
  
  @XmlElement(name = "USAGE_END_DETAILS")
  private String usageEndDetails;
  
  @XmlElement(name = "USAGE_START_DETAILS")
  private String usageStartDetails;
  
  @XmlElement(name = "CYCLE_START_DETAILS")
  private String cycleStartDetails;
  
  @XmlElement(name = "CYCLE_END_DETAILS")
  private String cycleEndDetails;
  
  @XmlElement(name = "PURCHASE_START_DETAILS")
  private String purchaseStartDetails;
  
  @XmlElement(name = "PURCHASE_END_DETAILS")
  private String purchaseEndDetails;

  @XmlElement(name = "PURCHASE_END_T")
  private Long purchaseEndT; //Date attribute field.

  @XmlElement(name = "CYCLE_END_T")
  private Long cycleEndT; //Date attribute field.

  @XmlElement(name = "OFFERING_OBJ")
  private String offeringObj; //Offering Object field.

  @XmlElement(name = "PRODUCT_OBJ")
  private String productObj; //Product object field.

  @XmlElement(name = "DISCOUNT_OBJ")
  private String discountObj; //Discount object field.

  @XmlElement(name = "USAGE_END_T")
  private Long usageEndT; //Date attribute of the field.
  
  @XmlElement(name = "CYCLE_DISCOUNT")
  private BigDecimal cycleDiscount;

  @XmlElement(name = "QUANTITY")
  private BigDecimal quantity;

  @XmlElement(name = "PURCHASE_START_T")
  private Long purchaseStartT;

  @XmlElement(name = "PURCHASE_DISCOUNT")
  private BigDecimal purchaseDiscount;

  @XmlElement(name = "STATUS")
  private String status;

  @XmlElement(name = "CYCLE_START_T")
  private Long cycleStartT;

  @XmlElement(name = "USAGE_START_T")
  private Long usageStartT;

  @XmlElement(name = "USAGE_DISCOUNT")
  private BigDecimal usageDiscount;

  @XmlElement(name = "STATUS_FLAGS")
  private String statusFlags;
  
  @XmlElement(name = "PURCHASE_END_OFFSET")
  private String purchaseEndOffset;
  
  @XmlElement(name = "PURCHASE_END_UNIT")
  private String purchaseEndUnit;
  
  @XmlElement(name = "CYCLE_END_UNIT")
  private String cycleEndUnit;
  
  @XmlElement(name = "CYCLE_END_OFFSET")
  private String cycleEndOffset;
  
  @XmlElement(name = "USAGE_END_OFFSET")
  private String usageEndOffset;
  
  @XmlElement(name = "USAGE_END_UNIT")
  private String usageEndUnit;
} //End of ProductsOrDiscounts.