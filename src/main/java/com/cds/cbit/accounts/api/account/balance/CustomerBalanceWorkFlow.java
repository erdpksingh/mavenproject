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

package com.cds.cbit.accounts.api.account.balance;

import com.cds.cbit.accounts.api.account.balance.flists.BalImpact;
import com.cds.cbit.accounts.api.account.balance.flists.CustomerBalanceFlist;
import com.cds.cbit.accounts.api.account.balance.payload.AdditionalBalance;
import com.cds.cbit.accounts.api.account.balance.payload.CustomerBalance;
import com.cds.cbit.accounts.api.account.balance.payload.CustomerBalanceResponseResult;
import com.cds.cbit.accounts.api.account.balance.payload.CustomerPlanInfo;
import com.google.common.collect.ImmutableMap;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.SparseArray;
import com.portal.pcm.fields.FldBalImpacts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * The component will provide helper methods for retrieving customer balance information.
 * 
 * @author Venkata Nagaraju.
 * @version 1.0.
 */
@Log4j2
@Component
public class CustomerBalanceWorkFlow {
  
  private static final String BALANCE = "balance";
  private static final String COUNTER_BALANCE = "counterBalance";
  
  @Autowired
  private Environment props;
  
  private final CustomerBalanceRetriever balanceRetriever;
  private final PlanAndUsageProcessor processor;
  private final CustomerDetailsRetriever customerDetail;
  
  /** Constructor injection. **/
  public CustomerBalanceWorkFlow(final CustomerDetailsRetriever customerDetail,
         final PlanAndUsageProcessor processor,final CustomerBalanceRetriever balanceRetriever) {
    
    this.customerDetail = customerDetail;
    this.processor = processor;
    this.balanceRetriever = balanceRetriever;
  } // End of constructor injection.

  /**
   * The method will fetch balance information of given MSISDN with BAL_GET_ECE_BALANCES opcode.
   * 
   * @param: msisdn - @Mandatory - MSISDN associated with the requested account.
  */
  protected CustomerBalanceResponseResult fetchBalanceInformation(
          final FList balanceOp,final CustomerBalanceFlist custBalance, final String accountObj)
                                                             throws EBufException, JAXBException {
    
    final List<CustomerBalance> customerBalance = new ArrayList<>();
    final List<AdditionalBalance> additionalBalance = new ArrayList<>();
    
    final CustomerPlanInfo planInfo = 
                           processor.fetchPlanDetails(accountObj); // Customer plan details.
    
    // Define elements for which indexes balances need not to fetch.
    final Integer[] balIndexes = { 2000022, 2000025, 2000102, 2000103, 2000060 };
    final List<Integer> balIndexList = Arrays.asList(balIndexes);
    
    // Fetch array and index details as map from ECE balance output FList.
    final ImmutableMap<String,Map<Integer,Integer>> elementMap = fetchElementMap(balanceOp);
    final Map<Integer, Integer> indexMap = elementMap.get("indexMap");
    final Map<Integer, Integer> arrayMap = elementMap.get("arrayNumberMap");
    
    final Optional<List<BalImpact>> balImpacts =  Optional.ofNullable(custBalance.getBalImpacts());
    
    // If output FList contains balancek impacts section(s), loop through it to fetch balances. 
    if (balImpacts.isPresent()) {  // If balance impact section found proceed further.

      final List<BalImpact> balImpactList = balImpacts.get();
      int i = 0;
      for (final BalImpact balance : balImpactList) {
        
        final int balIndex = indexMap.get(i);
        if (!balIndexList.contains(balIndex)) { // Proceed, balaImpact index not there in the list.
          
          final FList balImpactFlist = balanceOp.get(FldBalImpacts.getInst()).getValues().get(i);

          final Map<String, Object> balanceMap =
                fetchBlanceShadowElementDetails(
                                    balanceOp,balImpactFlist,balance,accountObj,arrayMap,balIndex);
          
          if (balanceMap.containsKey(BALANCE)) { 

            log.info("Fetching balances.");
            customerBalance.add((CustomerBalance) balanceMap.get(BALANCE));
          }
          if (balanceMap.containsKey(COUNTER_BALANCE)) {

            log.info("Fetching counter balances.");
            additionalBalance.add((AdditionalBalance) balanceMap.get(COUNTER_BALANCE));
          }
        } // End of balance index check with the balImpactList map.
        i++;
      } // End of looping through balance impacts.
    } // End of balance impacts field check in output FList.
    return CustomerBalanceResponseResult.builder().additionalBalance(additionalBalance)
                                        .balance(customerBalance).planDetail(planInfo).build();
  } // End of fetchBalanceInformation method.
  
  /**
   * The method will fetch balance information for the shadow elements presented in the config.
   * 
   * @param: accountObj    - @Mandatory - Account POID of requested account number.
   * @param balIndex       - @Mandatory - Current balance index element of the loop.
   * @param balanceOp      - @Mandatory - Customer ECE balances POJO.
   * @param balImpactFlist - @Mandatory - Customer balanceImapact FList.
   * @param elementMap     - @Mandatory - Customer balances index and array maps.
   * @param balance        - @Mandatory - Customer balanceImapact POJO.
  */
  protected Map<String, Object> fetchBlanceShadowElementDetails(
            final FList balanceOp,final FList balImpactFlist, final BalImpact balance,
            final String accountObj, final Map<Integer, Integer> arrayNumberMap, final int balIndex)
                                                                throws EBufException,JAXBException {

    final Map<String, Object> balanceMap = new ConcurrentHashMap<>();
    final StringBuilder shadowElementProperty = new StringBuilder("msg.shadowElement");
    
    final Optional<String> shadowElement = 
          Optional.ofNullable(props.getProperty(shadowElementProperty.append(balIndex).toString()));
    
    if (shadowElement.isPresent()) { // If shadow element present then fetch balances.

      log.info("Shadow element presented.");
      final CustomerBalance balances = balanceRetriever.fetchBalances(balanceOp, balImpactFlist,
          balance, accountObj, shadowElement.get(), arrayNumberMap, balIndex);
      
      Optional.ofNullable(balances.getExternalId())
                                                 .ifPresent(b -> balanceMap.put(BALANCE,balances));
    } else {  // IF shadow element not present then fetch counter balances.
      
      final AdditionalBalance additionalBal = 
            balanceRetriever.fetchAdditionalBalances(balImpactFlist,balance,balIndex);
      Optional.ofNullable(additionalBal.getExternalId())
                                   .ifPresent(a -> balanceMap.put(COUNTER_BALANCE, additionalBal));
    } // End of shadow element check.
    return balanceMap;
  } // End of fetchBlanceShadowElementDetails method.
  
  /**
   * The method will create result section of "GetCustomerBalance" API, in case of MSISDN not
   * associated with the given account number.
   *  
   * @param: accountObj- @Mandatory - Account POID of requested account number.
  */
  protected CustomerBalanceResponseResult fetchResultWithoutMsisdn(final String accountObj) 
                                                            throws EBufException, JAXBException {
    
    final CustomerPlanInfo planInfo = processor.fetchPlanDetails(accountObj);
    final String status = customerDetail.fetchAccountServicesStatus(accountObj);
    log.info(status);
    return CustomerBalanceResponseResult.builder().additionalBalance(new ArrayList<>())
                                        .balance(new ArrayList<>()).planDetail(planInfo).build();
  } // End of fetchResultWithoutMsisdn method.
  
  /**
   * The method will create array and index maps from the customer ECE balance information.
   * 
   * @param: balanceOp - @Mandatory - Customer balance output FList of ECE.
  */
  @SuppressWarnings("rawtypes")
  protected ImmutableMap<String, Map<Integer, Integer>> 
                                 fetchElementMap(final FList balanceOp) throws EBufException {
    
    final Map<Integer, Integer> indexMap = new ConcurrentHashMap<>();
    final Map<Integer, Integer> arrayNumberMap = new ConcurrentHashMap<>();
    
    if (balanceOp.hasField(FldBalImpacts.getInst())) { // Proceed,if output FList has balImpacts.
      
      final SparseArray spa = balanceOp.get(FldBalImpacts.getInst());
      final Enumeration enum1 = spa.getKeyEnumerator();
      int k = 0;
      while (enum1.hasMoreElements()) { //Loop through all balImpacts to fetch array number & Index.
        
        final int arrIndex = ((Integer) enum1.nextElement()).intValue();
        indexMap.put(k, arrIndex);
        arrayNumberMap.put(arrIndex, k);
        k++;
      } // End of looping through balance impacts array.
    } // End of balance impacts filed check.
    return ImmutableMap.of("indexMap",indexMap,"arrayNumberMap",arrayNumberMap);
  } // End of fetchElementMap method.
} // End of CustomerBalanceFlistExecutor,which execute the FLists required for GetCustomerBalance.