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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import lombok.extern.log4j.Log4j2;

import org.joda.time.DateTime;

/**
 * The util class will provide methods to perform different date and time operations with JODA.
 * 
 * @author  Venkata Nagaraju
 * @version 1.0.
*/
@Log4j2
public final class JodaDateUtil {
  
  private static final String ISO_DATE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  
  private JodaDateUtil() {
  } // End of private constructor.

  /**
   * The method will find the difference between current date and given date string in milliseconds.
   * 
   * @param:  dateStr - Date string, which need to compare with current date.
  */
  public static Long findDifferenceWithCurrentDate(final String dateStr) throws ParseException {
    
    // Convert all the dates to ISO 8601 YYYY-MM-ddTHH:mm:ssZ format
    final String pattern = ISO_DATE;

    final DateFormat fromatter = new SimpleDateFormat(pattern,Locale.ENGLISH);
    fromatter.setTimeZone(TimeZone.getTimeZone("UTC"));

    // Converting current date to DateAndTime.
    final Date currentDate = new Date();
    final DateTime currentDateTime = UtcDateUtil.convertDateToDateTime(currentDate);

    // Converting given date to DateAndTime.
    final Date fromDate = fromatter.parse(dateStr);
    final DateTime fromDateTime = UtcDateUtil.convertDateToDateTime(fromDate);

    // Converting current and given date to mills and finding the difference.
    final Long currentMillis = currentDateTime.getMillis();
    final Long fromMillis = fromDateTime.getMillis();
    final Long diff = fromMillis - currentMillis;
    
    log.info("Current Time : {}", currentDateTime);
    log.info("From Time  : {}", fromDateTime);
    log.info("Difference between fromDate and currentDate : {}", diff);
    return diff;
  } // End of findDifferenceWithCurrentDate method.

  /**
   * The method will check whether given date is current date and before current time.
   * 
   * @param: dateToCheck - @Mandatory - Date which should check with current date & time.
  */
  public static boolean checkCurrentDateAndTime(final String dateToCheck) throws ParseException {
    
    boolean flag = false;
    
    // Converting given date to UTC format.
    final Date brmInputDate = UtcDateUtil.convertToUtcDate(dateToCheck);
    final String brmInputStr = String.valueOf(brmInputDate);
    log.info(brmInputStr);
    
    // Defining calendar to fetch day, month and year for given date and current date.
    final Calendar calFromDate = Calendar.getInstance();
    
    // Setting given date for Calendar.
    calFromDate.setTime(brmInputDate);
    
    // Fetching current day, month , year for given date.
    final int fromDay = calFromDate.get(Calendar.DAY_OF_MONTH);
    final int fromMonth = calFromDate.get(Calendar.MONTH) + 1;
    final int fromYear = calFromDate.get(Calendar.YEAR);
    log.info("From day : {}", fromDay);
    log.info("From month : {}", fromMonth);
    log.info("From year : {}", fromYear);

    // Fetching current day, month , year for current date.
    final int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    final int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    log.info("Current day : {}", currentDay);
    log.info("Current month : {}", currentMonth);
    log.info("Current year : {}", currentYear);

    // finding difference between given date and current date.
    final Long dateDifference = findDifferenceWithCurrentDate(dateToCheck);
    if (fromDay - currentDay == 0 && fromMonth - currentMonth == 0 && fromYear - currentYear == 0
        && dateDifference <= 0) {
      flag = true;
    } // End of checking current date and before current time.
    return flag;
  } // End of checkCurrentDateAndTime method.
  

  /**
   * the Method will calculate difference between two given dates in milliSeconds. 
   * 
   * @param: fromDateStr - @Mandatory - From date String.
   * @param: toDateStr   - @Mandatory - To date String, which has to compare with from date.
  */
  public static Long calculateDatesDifference(
                final String fromDateStr,final String toDateStr) throws ParseException {
    
    // Convert all the dates to ISO 8601 YYYY-MM-ddTHH:mm:ssZ format
    final String pattern = ISO_DATE;

    // Date formatter for converting current date, given date [which already a UTC
    // string ]
    // to UTC dates.
    final DateFormat fromatter = new SimpleDateFormat(pattern,Locale.ENGLISH);
    fromatter.setTimeZone(TimeZone.getTimeZone("UTC"));

    // Converting given date first to UTC string then converting it to UTC date with
    // the
    // function findUtcDate.
    final Date fromDate = fromatter.parse(fromDateStr);
    final DateTime fromDateTime = UtcDateUtil.convertDateToDateTime(fromDate);

    final Date toDate = fromatter.parse(toDateStr);
    final DateTime toDateTime = UtcDateUtil.convertDateToDateTime(toDate);

    // Converting current and given date to mills and finding the difference.
    final Long fromMillis = fromDateTime.getMillis();
    final Long toMillis = toDateTime.getMillis();
    final Long difference = toMillis - fromMillis;

    log.info("From and current Date difference [Millis] {} ", fromMillis);
    log.info("From and To Date difference [Millis] {} ", toMillis);

    return difference;
  } // End of calculateDatesDifference method.
} // End of JodaDateUtil. which will perform date operations with JODA library.