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
 * The POJO represents field mapping of "nameInfo" section of BRM COMMIT_CUST_OPCODE.
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
public class NameInfo implements Serializable {
  
  private static final long serialVersionUID = -5655221099963422956L;

  @XmlAttribute(name = "elem")
  private String elem;

  @XmlElement(name = "ZIP")
  private String zip;

  @XmlElement(name = "CITY")
  private String city;

  @XmlElement(name = "CONTACT_TYPE")
  private String contactType;

  @XmlElement(name = "COUNTRY")
  private String country;

  @XmlElement(name = "ADDRESS")
  private String address;

  @XmlElement(name = "EMAIL_ADDR")
  private String emailAddress;

  @XmlElement(name = "STATE")
  private String state;

  @XmlElement(name = "LAST_NAME")
  private String lastName;

  @XmlElement(name = "FIRST_NAME")
  private String firstName;
  
  @XmlElement(name = "MIDDLE_NAME")
  private String middleName;
  
  @XmlElement(name = "SALUTATION")
  private String salutation;
  
  @XmlElement(name = "COMPANY")
  private String company;
} // End of NameInfo