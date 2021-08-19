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

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The util class will provide detailed error information about BRM connection related exceptions.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
public final class ConnectionErrorUtil {
  
  private ConnectionErrorUtil() {
    // private constructor.
  } // End of private constructor.
  
  /**
   * The method will create appropriate connection error code as per the exception message.
   * 
   * @param: e- Exception raised.
   * @return: Connection error code.
  */
  public static String getConnectionError(final Exception e) {
    
    final StringBuilder errorCode = new StringBuilder();
    final Optional<String> error = Optional.ofNullable(e.getMessage());
    if (error.isPresent() && getErrorDetail().containsKey(error.get())) {
      errorCode.append(getErrorDetail().get(error.get()));
    } else {
      errorCode.append("100102");
    } // End of creating error code as per the error message.
    return errorCode.toString();
  } // End of getConnectionError.
  
  private static Map<String, String> getErrorDetail() {

    final Map<String, String> errorMap = new ConcurrentHashMap<>();
    errorMap.put("ERR_NAP_CONNECT_FAILED", ConstantsUtil.CON_ERR);
    errorMap.put("ERR_TRANS_ALREADY_OPEN", ConstantsUtil.TRANS_ERR);
    errorMap.put("ERR_TIMEOUT", "100105");
    errorMap.put("ERR_STREAM_EOF", ConstantsUtil.CON_ERR);
    errorMap.put("ERR_IM_CONNECT_FAILED", ConstantsUtil.CON_ERR);
    errorMap.put("ERR_DM_CONNECT_FAILED", ConstantsUtil.CON_ERR);
    errorMap.put("ERR_BAD_LOGIN_RESULT", "100106");
    errorMap.put("ERR_STORAGE", ConstantsUtil.CON_ERR);
    errorMap.put("ERR_STREAM_IO", ConstantsUtil.CON_ERR);
    errorMap.put("ERR_TRANS_NOT_OPEN",  ConstantsUtil.TRANS_ERR);
    errorMap.put("ERR_TRANS_LOST",  ConstantsUtil.TRANS_ERR);

    return errorMap; // Returning error code map.
  } // End of getErrorDetail method.
} // End of ConnectionErrorUtil.