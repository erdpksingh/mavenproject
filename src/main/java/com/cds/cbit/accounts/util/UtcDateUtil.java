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

import com.cds.cbit.accounts.exceptions.BillingException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import lombok.extern.log4j.Log4j2;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * This component provide various methods to operate on UTC date format.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0
*/
@Log4j2
public final class UtcDateUtil {
  
  private static final String UTC = "UTC";
  private static final String ISO_DATE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  
  private UtcDateUtil() {
  } // End of private constructor.

  /** The method will take start date, end date and verifies whether start date is future date
   * or not. */
  /**
   * The method will take start date, end date and verifies whether the start date is future date
   * or not. Similarly it verified whether end date is prior to start date or not. The method will
   * throw specific error codes for the error conditions.
   * 
   * @param: startDate - @Mandatory - Start date, which need to compare with future date.
   * @param: endDate   - @Mandatory - End date, which need to compare with start date.
  */
  public static void dateValidation(
                                final String startDate,final String endDate) throws ParseException {

    final Long startDateDiff = JodaDateUtil.findDifferenceWithCurrentDate(startDate);
    
    if (startDateDiff > 0) {
      throw new BillingException("200301");
    } // End of checking startDate is future date or not.
    
    final Long endDateDiff = JodaDateUtil.findDifferenceWithCurrentDate(endDate);
    if (endDateDiff < startDateDiff) {
      throw new BillingException("200302");
    } // End of checking endDate is before startDate or not.
    
  } // End of dateValidation method.

  /**
   * The method will convert given date string into java.util.Date.
   * 
   * @param: dateToConvert - @Mandatory - Date string to convert.
  */
  public static Date convertStringToDate(final String dateToConvert) throws ParseException {
    
    final SimpleDateFormat sdf = new SimpleDateFormat(ISO_DATE,Locale.ENGLISH);
    return sdf.parse(dateToConvert);
  } // End of convertStringToDate.
  
  /**
   * The method will convert given date into date and time in UTC format.
   * 
   * @param: inputDate - @Mandatory - Date to convert into date time.
  */
  public static DateTime convertDateToDateTime(final Date inputDate) {
    
    final String pattern = ISO_DATE;
    
    final ZonedDateTime dateWithZone = 
          ZonedDateTime.ofInstant(inputDate.toInstant(), ZoneId.of(UTC));
    final String dateInString = dateWithZone.format(DateTimeFormatter.ofPattern(pattern));
    
    final DateTime resultDate = new DateTime(dateInString);
    final DateTimeZone timeZoneVal = DateTimeZone.forID(UTC);
    return resultDate.withZone(timeZoneVal);
  } // End of convertToUtcDateTime method

  
  /**
   * The method will convert the current date into Unix time stamp.
  */
  public static Long currentDateToTimeStamp() {
    
    final Date date = new Date();
    final SimpleDateFormat sdf = new SimpleDateFormat(ISO_DATE,Locale.ENGLISH);
    final TimeZone utc = TimeZone.getTimeZone("UTC");
    sdf.setTimeZone(utc);
    
    final SimpleDateFormat sdf1 = new SimpleDateFormat(ISO_DATE,Locale.ENGLISH);
    try {
      return sdf1.parse(sdf.format(date)).getTime() / 1000;
    } catch (ParseException e) {
      throw new BillingException("100102",e);
    }
  } // End of converting current date to Unix timestamp.
  
  /**
   * The method will find the end of time for the given date.
   * 
   * @param: date - @Mandatory - Date for which end time need to calculate.
  */
  public static Calendar getEodTime(final Date date) {
    
    final Calendar defaultTime = Calendar.getInstance();
    if (Objects.nonNull(date)) {
      
      defaultTime.setTime(date);
      defaultTime.set(Calendar.HOUR, 23);
      defaultTime.set(Calendar.MINUTE, 59);
      defaultTime.set(Calendar.SECOND, 59);
    } // End of date null check.
    return defaultTime;
  } // End of getEodTime method.

  /**
   * The method will convert the given date string into UTC date.
   * 
   * @param: dateStr - @Mandatory- Date string to convert.
  */
  public static Date convertToUtcDate(final String dateStr) throws ParseException {
    
    log.info("UTC conversion activated");
    final DateFormat brmFormatter = new SimpleDateFormat(ISO_DATE,Locale.ENGLISH);
    brmFormatter.setTimeZone(TimeZone.getTimeZone(UTC));

    return brmFormatter.parse(dateStr);
  } // End of convertToUtcDate method.

  /**
   * The Method will convert given date string into timestamp.
   * 
   * @param: dateStr - @Mandatory - Date string which need to convert into time stamp.
  */
  public static Long convertStringToTimeStamp(final String dateStr) throws ParseException {

    log.info("Timestamp conversion activated");
    final Date date = convertToUtcDate(dateStr);

    return date.getTime() / 1000;
  } // End of convertStringToTimeStamp method.

  /**
   * The Method will find next calendar date of given date string.
   * 
   * @param: givenDate - @Mandatory - Given date string
  */
  public static String getNextDay(final String givenDate) throws ParseException {
    
    final SimpleDateFormat sdf = new SimpleDateFormat(ISO_DATE,Locale.ENGLISH);
    final Calendar cal = Calendar.getInstance();
    
    cal.setTime(sdf.parse(givenDate));
    cal.add(Calendar.DATE, 1); // number of days to add
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    return sdf.format(cal.getTime()); // givenDate is now the new date
  } // End of getNextDay method.

  /**
   * Method will convert the given date to UTC string with ISO 8601 format.
   * 
   * @param- dateToConvert- Date which should convert to UTC string.
  */
  public static String convertDateToUtc(final Date dateToConvert) {
    
    // Defining a formatter in for yyyy-MM-ddTHH:mm:ssZ [ISO 8601]
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ISO_DATE);
    // Converting given date and time to above format.
    return formatter.format(dateToConvert.toInstant().atOffset(ZoneOffset.UTC));     
  } // End of convertDateToUtc method.
} // End of class UtcDateUtil, which provide methods to operate on UTC date and time.