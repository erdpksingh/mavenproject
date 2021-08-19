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
import com.cds.cbit.accounts.api.account.balance.flists.SubBalImpacts;
import com.cds.cbit.accounts.api.account.balance.payload.AdditionalBalance;
import com.cds.cbit.accounts.api.account.balance.payload.CustomerBalance;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.fields.FldAmount;
import com.portal.pcm.fields.FldBalImpacts;
import com.portal.pcm.fields.FldExtraResults;
import com.portal.pcm.fields.FldGrantorObj;
import com.portal.pcm.fields.FldName;
import com.portal.pcm.fields.FldSubBalImpacts;
import com.portal.pcm.fields.FldValidFrom;
import com.portal.pcm.fields.FldValidTo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * The component will fetch customer balances and additional balances from the ECE balances.
 * 
 * @author Venkata Nagaraju.
 * @version 1.0.
*/
@Log4j2
@Component
public class CustomerBalanceRetriever {
  
  @Autowired
  private Environment props;
  
  @Autowired
  private CustomerBalanceHelper helper;
  
  private final PlanAndUsageProcessor processor;
  private final CustomerDetailsRetriever customerDetail;
  private final CustomerBalanceFlistExecutor flistExecutor;
  
  /** Constructor injection. **/
  public CustomerBalanceRetriever(
         final CustomerDetailsRetriever customerDetail,final PlanAndUsageProcessor processor,
         final CustomerBalanceFlistExecutor flistExecutor) {
    
    this.processor = processor;
    this.customerDetail = customerDetail;
    this.flistExecutor = flistExecutor;
  } // End of constructor injection.

  protected AdditionalBalance prepareAdditionalBalance(final String planName,
      final SubBalImpacts subBalance, final String balIndex) {

    final Map<String, Object> balanceDetails = processor.getDateAndUsage(subBalance, balIndex);

    final String externalId = (String) balanceDetails.get("externalId");
    final BigDecimal currentBalance = (BigDecimal) balanceDetails.get("currentBalance");
    final String validFrom = (String) balanceDetails.get("validFrom");
    final String validTo = (String) balanceDetails.get("validTo");
    return AdditionalBalance.builder().externalId(externalId)
                            .used(String.valueOf(currentBalance.intValue()))
                            .packageName(planName).fromDate(validFrom).toDate(validTo).build();
  } // End of prepareAdditionalBalance method.

  protected CustomerBalance fetchBalances(
            final FList balanceFlist,final FList balImpactFlist, final BalImpact balanceOp,
            final String accountObj,final String shadowElement,
            final Map<Integer,Integer> arrayNumberMap,final int balIndex)  
                                                         throws EBufException, JAXBException {
    
    final FList packageOp = customerDetail.fetchPackagedata(accountObj);
    final String packagename = 
          packageOp.get(FldExtraResults.getInst()).getValues().get(0).get(FldName.getInst());

    if ("CirclesZero".trim().equals(packagename) && 2000000 == balIndex) {
      // Code for voice balance fetch.
      return null;
    } else {
      final int shadowIndex = Integer.parseInt(shadowElement);
      return processSubBalances(balanceFlist,balImpactFlist,balanceOp,arrayNumberMap,shadowIndex);
    }
  } // End of fetchBalances method.
  
  protected CustomerBalance processSubBalances(
            final FList balanceFlist,final FList balImpactFlist,final BalImpact balanceOp,
            final Map<Integer,Integer> arrayNumberMap,final int shadowIndex) 
                                                            throws EBufException, JAXBException {
    
    CustomerBalance custBalance = new CustomerBalance();
    final Optional<List<SubBalImpacts>> subBalances = Optional
                                                     .ofNullable(balanceOp.getSubBalImpacts());
    if (subBalances.isPresent()) {
      int i = 0;
      for (final SubBalImpacts subBalance : subBalances.get()) {

        final Poid puchasedProduct = 
              balImpactFlist.get(FldSubBalImpacts.getInst()).getValues()
                                                          .get(i).get(FldGrantorObj.getInst());
        final Optional<String> grantObj = Optional.ofNullable(subBalance.getGrantObj());
        if (grantObj.isPresent()) {
          final String dealType = grantObj.get().contains(ConstantsUtil.PURCHASED_PRODUCT)
                                       ? ConstantsUtil.PURCHASED_PRODUCT : "/purchased_discount";
          custBalance = 
              fetchCustomerBalanceData(balanceFlist,subBalance, balanceOp,
                                       dealType,arrayNumberMap,shadowIndex,puchasedProduct);
          log.info(custBalance);
        } // End of grantObj check.
        i++;
      } // End of looping through sub balances.
    } // End of sub balance field check.
    return custBalance;
  } // End of processSubBalances method.

  protected AdditionalBalance fetchAdditionalBalances(final FList balanceFlist,
            final BalImpact balance,final int balIndex) throws EBufException, JAXBException {
    log.info("Counter balances");

    AdditionalBalance additionalBalance = new AdditionalBalance();
    final StringBuilder externalId = new StringBuilder("msg.externalId");
    
    final Optional<String> externalIdVal = Optional
                          .ofNullable(props.getProperty(externalId.append(balIndex).toString()));
    final Optional<List<SubBalImpacts>> subBalances = Optional
                                                         .ofNullable(balance.getSubBalImpacts());
    
    if (subBalances.isPresent() && externalIdVal.isPresent()) {
      int i = 0;
      for (final SubBalImpacts subBalance : subBalances.get()) {
        final Poid puchasedProduct = 
              balanceFlist.get(FldSubBalImpacts.getInst()).getValues()
                                                              .get(i).get(FldGrantorObj.getInst());
        final Optional<String> grantObj = Optional.ofNullable(subBalance.getGrantObj());
        if (grantObj.isPresent()) {
          final String dealType = grantObj.get().contains(ConstantsUtil.PURCHASED_PRODUCT)
              ? ConstantsUtil.PURCHASED_PRODUCT : "/purchased_discount";
          
          final String planName =  flistExecutor.getGrantObjForCounterBalance(
                                                         String.valueOf(puchasedProduct),dealType);
          additionalBalance = prepareAdditionalBalance(planName, subBalance, balance.getElem());
        } // End of grantobj check.
        i++;
      } // End of looping through sub balances.
    } // End of sub balances check.
    return additionalBalance;
  } // End of fetchCounterBalances method.

  private CustomerBalance fetchCustomerBalanceData(
          final FList balanceFlist,final SubBalImpacts subBalance,final BalImpact balanceOp,
          final String dealType,final Map<Integer,Integer> arrayNumberMap,final int shadowIndex,
                                   final Poid puchasedProduct) throws EBufException, JAXBException {

    final CustomerBalance custBalance = 
          prepareCustomerBalance(balanceFlist,subBalance,
                                 balanceOp.getElem(),arrayNumberMap,shadowIndex,puchasedProduct);
    final Map<String,String> details = 
                     helper.getGrantObjDetails(balanceOp, dealType,String.valueOf(puchasedProduct));

    if (details.containsKey(ConstantsUtil.PLANNAME)) {
      custBalance.setPackageName(details.get(ConstantsUtil.PLANNAME));
    }

    if (details.containsKey("type")) {
      custBalance.setType(Integer.valueOf(details.get("type")));
    }
    return custBalance;
  } // End of fetchCustomerBalanceData method.

  protected CustomerBalance prepareCustomerBalance(
            final FList balanceFlist,final SubBalImpacts subBalance,
            final String balIndex,final Map<Integer,Integer> arrayNumberMap,final int shadowIndex,
                                                  final Poid puchasedProduct) throws EBufException {
    
    final Map<String, Object> balanceDetails = processor.getDateAndUsage(subBalance, balIndex);

    final String externalId = (String) balanceDetails.get("externalId");
    final BigDecimal avialableBalance = (BigDecimal) balanceDetails.get("availableBalance");
    final BigDecimal currentBalance = (BigDecimal) balanceDetails.get("currentBalance");
    final String validFrom = (String) balanceDetails.get("validFrom");
    final String validTo = (String) balanceDetails.get("validTo");
    final BigDecimal usedAmount = 
          getUsedBalanceAmount(balanceFlist,subBalance,arrayNumberMap,
                               shadowIndex,puchasedProduct,currentBalance);
    log.info(arrayNumberMap);
    return CustomerBalance.builder().available(avialableBalance).externalId(externalId)
                          .used(usedAmount).unused(avialableBalance).fromDate(validFrom)
                          .toDate(validTo).build();
  } // End of prepareCustomerBalance method.
  
  private BigDecimal getUsedBalanceAmount(
          final FList balanceFlist,final SubBalImpacts subBalance,
          final Map<Integer,Integer> arrayNumberMap,final int shadowIndex,
                final Poid puchasedProduct,final BigDecimal currentBalance) throws EBufException {
    
    BigDecimal shadowBalance = BigDecimal.ZERO;
    
    if (arrayNumberMap.containsKey(shadowIndex)) {
      
      final int elementIndex = arrayNumberMap.get(shadowIndex);
      final Date validFrom = new Date(subBalance.getValidFrom() * 1000);
      final Date validTo = new Date(subBalance.getValidTo() * 1000);
      
      final List<FList> shadowList = 
            balanceFlist.get(FldBalImpacts.getInst()).getValues().get(elementIndex)
                                                     .get(FldSubBalImpacts.getInst()).getValues();
      for (final FList shadow : shadowList) {
        
        final Date fromDate = shadow.get(FldValidFrom.getInst());
        Date toDate = new Date(0);
        if (shadow.hasField(FldValidTo.getInst())) {
          toDate = shadow.get(FldValidTo.getInst());
        }
        final Poid grantObj = shadow.get(FldGrantorObj.getInst());

        if (validFrom.equals(fromDate) 
                                 && validTo.equals(toDate) && grantObj.equals(puchasedProduct)) {
          shadowBalance = currentBalance.subtract(shadow.get(FldAmount.getInst()));
        }
      } // Looping through shadow elements of balance imapct.
    } // End of shadow index check.
    return shadowBalance;
  } // End of getUsedBalanceAmount method.
} // End of CustomerBalanceFetcher.