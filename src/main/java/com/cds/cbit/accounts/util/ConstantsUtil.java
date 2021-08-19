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

/**
 * The util class will define all the constants required for account services.
 * 
 * @author Saibabu Guntur
 * @Version 1.0.
*/
public final class ConstantsUtil {
  
  public static final String FLAT_PROFILE = "/profile/lw_account";
  
  public static final String FLAT_PROFILE_OBJ = "0.0.0.1 /profile/lw_account -1 0";
  
  public static final String BILL_ITEM_NUMBER_VALIDATOR = "billOrItemNumberValidator";
  
  public static final String BA_PROFILE = "/profile/lw_ba_account";
  
  public static final String BALGROUP = "0.0.0.1 /balance_group -1 0";
  
  public static final String  BILLINFO = "0.0.0.1 /billinfo -1 0";
  
  public static final String SUBSCRIBER = "/profile/subscriber_preferences";
  
  public static final String PAYINFO_INVOICE = "0.0.0.1 /payinfo/invoice -1 0";
  
  public static final String PAYINFO_PREPAID = "0.0.0.1 /payinfo/prepaid -1 0";
  
  public static final String ZERO_POD = "0.0.0.0  0 0";
  
  public static final String SEARCH_FLAG = "256";
  
  public static final String ACCOUNT_POID = "0.0.0.1 /account -1 0";
  
  public static final String BA_ACCT_SQL = "select X from /profile/lw_ba_account 1,"
      
      + "/account 2  where 1.F1=2.F2 and 2.F3 = V3 and F4.type like V4";
  
  public static final String FLAT_ACCT_SQL = "select X from /profile/lw_account 1,"
      
      + "/account 2  where 1.F1=2.F2 and 2.F3 = V3 and F4.type like V4";
  
  public static final String SEARCH_POID = "0.0.0.1 /search -1 0";
  
  public static final String MSISDN_SQL = "select X from /device where F1=V1";
  
  public static final String SIM_SQL = "select X from /device/sim where F1 = V1";
  
  public static final String PLAN_SQL = "select X from /plan where F1 = V1";
  
  public static final String ACCTDETAIL_SQL = 
      
        "select X from /group/billing 1, /account 2 where (1.F1 = 2.F2 and 2.F3=V3)";
  
  public static final String CON_ERR = "100103";
  
  public static final String TRANS_ERR = "100104";
  
  public static final String ERROR_CODE = "errorCode";
  
  public static final String ERROR_MSG  = "errorMsg";
  
  public static final String RESULT_CODE  = "resultCode";
  
  public static final String FAILURE_MSG  = "FAILURE";
  
  public static final String ACCT_MSISDN = "select X from /device 1 ,/account "
       
            + "2 where 1.F1=2.F2 and 2.F3=V3  and 1.F4.type=V4";
  
  public static final String DEAL_CHECK_SQL = "select X from /deal where F1 = V1";
  
  public static final String ACCOUNT_PLAN_SQL = "select X from /purchased_product "
                            + "where F1 = V1 and plan_obj_id0 !=0 and status != 3";
  
  public static final String SERVICE_SQL = "select X from /service where F1 = V1";
  
  public static final String PACKAGE_FETCH_SQL = "select X from /purchased_product 1 ,"

            + " /plan 2 where 1.F1 = 2.F2 and 1.F3 = V3 and 1.F4 != V4 order by F5 desc";
  
  public static final String ACCOUNT_MSISDN_SQL = "select X from /device 1 ,/account 2 "

                            + "where 1.F1=2.F2 and 2.F3=V3 and 1.F4.type=V4";
  
  public static final String ADDON_SQL = "select X from  /purchased_product 1 , /deal 2"
     
           + " where 1.F1=2.F2 and 1.F3=V3 and 1.F4 != V4";
  
  public static final String BASEPLAN_SQL = "select X from /purchased_product 1 , /plan 2 "
      
              + " where 1.F1=2.F2 and 1.F3=V3 and 1.F4 != V4 and 1.F6.type=V6 order by F5 desc";
  
  public static final String ACCOUNT_NAME_SQL = "select X from /account 1, /billinfo "
      
              + "where 1.F1 = V1 and 1.F3 = 2.F2";
  
  public static final String PACKAGE_ID_SQL = "select X from /purchased_product where F1 = V1 ";
  
  public static final String BILL_POID = "0.0.0.1 /bill 1 0";
  
  public static final String ITEM_POID = "0.0.0.1 /item -1 0";
  
  public static final String PLANNAME  = "planName";
  
  public static final String VOICE  = "VOICE";
  
  public static final String TYPE  = "TYPE";
  
  public static final String PURCHASED_PRODUCT = "/purchased_product";
  
  private ConstantsUtil() {
  } // End of constructor.
} // End of ConstantsUtil.