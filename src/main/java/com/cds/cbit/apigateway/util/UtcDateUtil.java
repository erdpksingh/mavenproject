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

package com.cds.cbit.apigateway.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * The util class will provide method for various date operations.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
 *
 */
public final class UtcDateUtil {
  
  private UtcDateUtil() {
    // private constructor.
  } // End of private constructor.
  
  /**
   * Method to convert given string to Date.
   * 
   * @param: dateToConvert - Date string.
  */
  public static Date convertStringToDate(final String dateToConvert) throws ParseException {
    
    final String isoDate = ConstantsUtil.UTC_DATE;
    final SimpleDateFormat sdf = new SimpleDateFormat(isoDate, Locale.ENGLISH);
    return sdf.parse(dateToConvert);
  } // End of convertStringToDate.
  
  /**
   * Method to convert given string to Date.
   * 
   * @param: dateToConvert - Date string.
  */
  public static String convertToSqlDate(final Date dateToConvert) {
    
    final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
    return sdf.format(new java.sql.Date(dateToConvert.getTime()));
  } // End of convertStringToDate.
  
  /**
   * Method will convert the given date to UTC string with ISO 8601 format.
   * @param- dateToConvert- Date which should convert to UTC string.
   * @return- return converted string.
  */
  public static String convertDateToUtc(final Date dateToConvert) {
    // Defining a formatter in for yyyy-MM-ddTHH:mm:ssZ [ISO 8601]
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstantsUtil.UTC_DATE);
    // Converting given date and time to above format.
    return formatter.format(dateToConvert.toInstant().atOffset(ZoneOffset.UTC));     
  } // End of convertDateToUtc method.
} // End of UtcDateUtil.