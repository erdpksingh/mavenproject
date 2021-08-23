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

package com.cds.cbit.apigateway.common;

import com.cds.cbit.apigateway.util.UtcDateUtil;

import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.Date;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Component;

/**
 * The component will provide methods to create entries for audit and auditInfo tables.
 * 
 * @author Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component
public class TransactionAudit {
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  public static final String SEP = "','";
  public static final String DATESEP = "',to_date('";

  /**
   * The method will create audit record with the request header information in API_AUDIT_T table.
   * 
   * @param: reqHeader      :  @Mandatory The header part of the request.
  */
  public void createAudit(final RequestHeader reqHeader) throws ParseException {
    
    final Date sqlDate = UtcDateUtil.convertStringToDate(reqHeader.getReqTime());
    final String reqDate =  UtcDateUtil.convertToSqlDate(sqlDate);
    
    final StringBuilder query = new StringBuilder("insert into api_audit_t values('")
          .append(reqHeader.getReqId()).append(SEP).append(reqHeader.getClientId()).append(SEP)
          .append(reqHeader.getAction()).append(DATESEP).append(reqDate)
          .append("','DD-MM-YYYY'),'").append(reqHeader.getVersion()).append(SEP)
          .append(reqHeader.getCountry()).append(SEP).append(reqHeader.getSubType()).append(SEP)
          .append("In-Progress").append("')");
    log.info(query);
    
    jdbcTemplate.update(query.toString());
  } // End of creating audit record with header information.
  
  
  /**
   * The method will create notification audit record with the request header information in 
   * NOTIFY_AUDIT_T table.
   * 
   * @param: reqHeader  : @Mandatory The header part of the notification request.
   * @param: reqDate    : @Mandatory - Notification creation date.
   * @param: notifyType : @Mandatory - Notification type.
   * @throws- ParseException 
  */
  public void createNotifyAudit(final RequestHeader reqHeader, final String notifyType) 
                                                                          throws ParseException {

    final Date sqlDate = UtcDateUtil.convertStringToDate(reqHeader.getReqTime());
    final String reqDate =  UtcDateUtil.convertToSqlDate(sqlDate);
    
    final StringBuilder query = new StringBuilder("insert into notify_audit_t values('")
          .append(reqHeader.getReqId()).append(SEP).append(reqHeader.getClientId()).append(SEP)
          .append(reqHeader.getAction()).append(DATESEP).append(reqDate)
          .append("','DD-MM-YYYY'),'").append(reqHeader.getVersion()).append(SEP)
          .append(reqHeader.getCountry()).append(SEP).append(reqHeader.getSubType()).append(SEP)
          .append(notifyType).append(SEP).append(3).append(SEP).append("In-Progress").append("')");
    log.info(query);
    
    jdbcTemplate.update(query.toString());
  } // End of creating audit record.
  
  /** 
   * The method will update audit table with the status information after the completion of request
   * either successfully or with failure. 
   * 
   * @param: status  :  @Mandatory  - Success / Failure status.
   * @param: reqId   :  @Mandatory  - Unique request identifier.
  */
  public void updateAudit(final String status,final String reqId) {
    
    final StringBuilder query = 
          new StringBuilder("update api_audit_t set status='").append(status)
                                           .append("' where reqid = '").append(reqId).append("'");
    jdbcTemplate.update(query.toString());
  } // End of updating audit table with the status information.
  
  /** 
   * The method will update notify audit table with the status information after the completion of
   * notification request either successfully or with failure. 
   * 
   * @param: status  :  @Mandatory  - Success / Failure status.
   * @param: reqId   :  @Mandatory  - Unique request identifier.
  */
  public void updateNotifyAudit(final String status,final String reqId) {
    
    final StringBuilder query = 
          new StringBuilder("update notify_audit_t set status='").append(status)
                                           .append("' where reqid = '").append(reqId).append("'");
    jdbcTemplate.update(query.toString());
  } // End of updating notify audit table with the status information..
  
  

  /**
   * The method will create information record for the main record of API_AUDIT_T in API_AUDI_INFO_T
   * with request payload.
   * 
   * @param: reqHeader :  @Mandatory  -  Request header.
   * @param: json      :  @Mandatory  -  Request payload.
  */
  public void createRequestAuditInfo(
                            final RequestHeader reqHeader,final String json) throws ParseException {
    
    createAuditInfo(reqHeader.getReqId(),reqHeader.getReqTime(),json,"Request");
  } // End of creating audit information with payload.
  
  /**
   * The method will create information record for the main record of API_AUDIT_T in API_AUDI_INFO_T
   * with response payload.
   * 
   * @param: reqHeader :  @Mandatory  -  Request header.
   * @param: json      :  @Mandatory  -  Request payload.
  */
  public void createResponseAuditInfo(
                           final ResponseHeader resHeader,final String json) throws ParseException {
    
    createAuditInfo(resHeader.getReqId(),resHeader.getRespTime(),json,"Response");
  } // End of creating audit information with payload.
  

  /**
   * The method will create notification request information record in NOTIFY_AUDI_INFO_T with the
   * request payload.
   * 
   * @param: reqHeader :  @Mandatory  -  Notification request header.
   * @param: json      :  @Mandatory  -  Notification request payload.
   * @param: msgType   :  @Mandatory  -  Notification type.
  */
  public void createNotifyAuditRequestInfo(
         final RequestHeader reqHeader,final String json,final String msgType) 
                                                                            throws ParseException {
    createNotifyAuditInfoRecord(reqHeader.getReqId(),reqHeader.getReqTime(),json,msgType);
  } // End of creating notify audit information with payload.
  
  /**
   * The method will create notification response information record in NOTIFY_AUDI_INFO_T with the
   * response payload.
   * 
   * @param: reqHeader :  @Mandatory  -  Notification response header.
   * @param: json      :  @Mandatory  -  Notification response payload.
   * @param: msgType   :  @Mandatory  -  Notification type.
  */
  public void createNotifyAuditResponseInfo(
         final ResponseHeader resHeader,final String json,final String msgType) 
                                                                           throws ParseException {
	  
    createNotifyAuditInfoRecord(resHeader.getReqId(),resHeader.getRespTime(),json,msgType);
  }
  
  /**
   * The method will insert record into API_AUDIT_INFO_T with payload as CLOB entry.
   * 
   * @param: reqId -  @Mandatory  - Unique request id.
   * @param: reqTime- @Mandatory  - Request / Response creation time.
   * @param: json-    @Mandatory  - Request / Response payload.
  */
  public void createAuditInfo(
      final String reqId,final String dateTime,final String json,final String msgType) 
                                                               throws ParseException {

    final Date sqlDate = UtcDateUtil.convertStringToDate(dateTime);
    final Timestamp ts = new Timestamp(sqlDate.getTime()); 
    
    final LobHandler lobHandler = new DefaultLobHandler();
    
    jdbcTemplate.update("insert into api_audit_info_t VALUES (?, ?, ?, ?)",
                         new Object[] { reqId,msgType,ts, new SqlLobValue(json, lobHandler) },
                         new int[] {Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.CLOB});
    
  } // End of creating audit info record.
  
  /**
   * The method will insert record into NOTIFY_AUDIT_INFO_T with payload as CLOB entry.
   * 
   * @param: reqId -  @Mandatory  - Unique notification request id.
   * @param: reqTime- @Mandatory  - Notification request / response creation time.
   * @param: json-    @Mandatory  - Notification request / response payload.
   * @param: msgType- @Mandatory  - Notification type
  */
  private void createNotifyAuditInfoRecord(
      final String reqId,final String dateTime,final String json,final String msgType)
                                                                          throws ParseException {
    final Date sqlDate = UtcDateUtil.convertStringToDate(dateTime);
    final Timestamp ts = new Timestamp(sqlDate.getTime()); 

    final LobHandler lobHandler = new DefaultLobHandler();
    jdbcTemplate.update("insert into notify_audit_info_t VALUES (?, ?, ?, ?)",
                         new Object[] { reqId,msgType, new SqlLobValue(json, lobHandler),ts },
                         new int[] {Types.VARCHAR, Types.VARCHAR,Types.CLOB, Types.TIMESTAMP});
  } // End of creating audit info record.
} // End of TransactionAudit.