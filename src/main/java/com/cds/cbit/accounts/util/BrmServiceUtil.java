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

package com.cds.cbit.accounts.util;

import com.cds.cbit.accounts.aspects.Loggable;
import com.cds.cbit.accounts.config.BrmCmConfig;
import com.cds.cbit.accounts.connections.BrmConnectionPool;
import com.cds.cbit.accounts.exceptions.BillingException;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalContext;
import com.portal.pcm.XMLToFlist;
import com.portal.pcm.fields.FldPoid;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The util class provides methods for executing the input request in BRM systems. The util
 * provide method for converting input request into an XML and then provide method for 
 * converting the XML into BRM FList and also provide a method for executing the generated
 * FList in BRM with the specified opcode.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component
public class BrmServiceUtil {

  private final BrmCmConfig brmPool; // Billing system CM connection pool.

  public BrmServiceUtil(final BrmCmConfig brmPool) {
    this.brmPool = brmPool;
  } // End of dependency injection.

  /**
   * Method to convert given POJO into XML and processing XML in BRM.
   * 
   * @param: pojoClass - FList POJO class name.
   * @param: opcode    - Opcode to execute in BRM.
   * @param: flist     - Flist to be executed.
   * @return : return output FList of BRM.
  */
  @SuppressWarnings("rawtypes")
  public FList 
         executeInputInBrm(final Object pojoClass,final int opcode,final Class... flist) 
                                                   throws JAXBException, EBufException {

    final JAXBContext jaxbContext = JAXBContext.newInstance(flist);

    // Converting given request flist bean to XML and executing BRM
    // opcode with XML input.
    final FList inputFlist = getFListFromPojo(jaxbContext, pojoClass);
    log.info(inputFlist);
    
    final FList outputFlist = executeBrmFlist(inputFlist, opcode);
    log.info(outputFlist);
    return outputFlist;
  } // End of executeInputInBrm.
  
  
  /**
   * Method for converting given POJO to XML and then to FList.
   * 
   * @param: input  - Input FList for the opcode.
   * @param: opcode - Opcode to execute in BRM.
   * @param: input  - Input FList for the opcode.
   * @param: opcode - Opcode to execute in BRM.
   * @return: return output FList of BRM.
  */
  @Loggable
  public FList getFListFromPojo(final JAXBContext jaxbContext, final Object flistObject) {
    
    try {
      // Converting given POJO to XML.
      final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      final StringWriter sw = new StringWriter();
      jaxbMarshaller.marshal(flistObject, sw);

      final XMLToFlist xmlToFList = XMLToFlist.getInstance();
      xmlToFList.convert(new InputSource(new StringReader(sw.toString())));
      return xmlToFList.getFList();

    } catch (IOException | SAXException | ParserConfigurationException | JAXBException e) {
      throw new BillingException("100102",e);
    } // End of converting POJO to XML.
  } // End of getFListFromPojo method.

  /**
   * Method for executing given input FList with given opcode in BRM. 
   * 
   * @param: input  - Input FList for the opcode.
   * @param: input  - Input FList for the opcode.
   * @param: opcode - Opcode to execute in BRM.
   * @return : return output FList of BRM.
  */
  @Loggable
  public FList executeBrmFlist(final FList input, final int opcode) throws EBufException {
    final BrmConnectionPool brmConnectionPool = brmPool.getBrmPool();
    final PortalContext portal = brmConnectionPool.borrowContext();
    try {
      return portal.opcode(opcode, input);
    } catch (Exception e) {
      throw new BillingException(ConnectionErrorUtil.getConnectionError(e));
    } finally {
      brmConnectionPool.returnContext(portal);
    } // End of BRM opcode execution.
  } // End of executeBrmFlist.
  
  /**
   * Method for reading details regarding given POID in BRM with PCM_OP_READ. 
   * 
   * @param: input  - Input FList for the opcode.
   * @param: input  - Input FList for the opcode.
   * @param: opcode - Opcode to execute in BRM.
   * @return : return output FList of BRM.
  */
  @Loggable
  public FList readBrmPoid(final Poid poid) throws EBufException {
    final BrmConnectionPool brmConnectionPool = brmPool.getBrmPool();
    final PortalContext portal = brmConnectionPool.borrowContext();
    try {
      final FList flist = new FList();
      flist.set(FldPoid.getInst(), poid);
      return portal.opcode(3, flist);
    } catch (Exception e) {
      throw new BillingException(ConnectionErrorUtil.getConnectionError(e));
    } finally {
      brmConnectionPool.returnContext(portal);
    } // End of BRM opcode execution.
  } // End of executeBrmFlist.
  
  /** The method will convert given FList into POJO. **/
  @Loggable
  public <T> Object getPojoFromFList(final FList input,final Class<T> className) {
    try {
      final JAXBContext jaxbContext = JAXBContext.newInstance(className);
      final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

      final StringReader reader = new StringReader(input.toXMLString());
      return unmarshaller.unmarshal(reader);
    } catch (Exception e) {
      throw new BillingException("100102",e);
    } // End of converting FList to POJO.
  } // End of getPojoFromFList method.
  
  /**
   * Method for reading details regarding given POID in BRM with PCM_OP_READ,during
   * transaction processing. 
   * 
   * @param: input  - Input FList for the opcode.
   * @param: input  - Input FList for the opcode.
   * @param: opcode - Opcode to execute in BRM.
   * @return : return output FList of BRM.
  */
  @Loggable
  public FList readBrmPoidInTransaction(final Poid poid,final PortalContext portal) 
                                                              throws EBufException {
    try {
      final FList flist = new FList();
      flist.set(FldPoid.getInst(), poid);
      return portal.opcode(3, flist);
    } catch (Exception e) {
      throw new BillingException(ConnectionErrorUtil.getConnectionError(e));
    }  // End of BRM opcode execution.
  } // End of executeBrmFlist.
} // End of BrmUtil static class.