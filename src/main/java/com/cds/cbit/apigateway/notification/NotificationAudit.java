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

package com.cds.cbit.apigateway.notification;

import com.cds.cbit.apigateway.common.RequestHeader;
import com.cds.cbit.apigateway.common.TransactionAudit;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component will create notification request audit information in the table.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class NotificationAudit {
  
  @Autowired
  private Gson gson;
  
  @Autowired
  private TransactionAudit audit;
  
  protected String createNotifyAudit(final NotificationRequest request) 
                                                     throws SQLException, ParseException {
    final String payload = gson.toJson(request);
    final RequestHeader header = request.getHead();
    final String activityId = request.getBody().getActivityId();
    
    audit.createNotifyAudit(header,activityId);
    audit.createNotifyAuditRequestInfo(header,payload,"Request");
    
    return payload;
  } // End of createNotifyAudit.
} // End of NotificationAudit.