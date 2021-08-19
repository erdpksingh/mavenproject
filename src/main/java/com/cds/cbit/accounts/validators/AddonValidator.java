package com.cds.cbit.accounts.validators;

import com.cds.cbit.accounts.api.customer.detail.flist.ExtraResults;
import com.cds.cbit.accounts.api.customer.detail.flist.LinkedObj;
import com.cds.cbit.accounts.commons.beans.Args;
import com.cds.cbit.accounts.commons.beans.Results;
import com.cds.cbit.accounts.interfaces.BillingValidation;
import com.cds.cbit.accounts.util.BillingInfoSearchUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

/**
 * The implementation component provides methods for validating account for ADDON purchase 
 * in the system. The component overrides BillingValidationInterface validateInput method to
 * create and execute  add on search FList.If the validation is success then the method will
 * return output FList else it will throw an exception to the caller.
 * 
 * @author  Meghashree Udupa
 * @version 1.0.
*/
@Component("addonValidator")
public class AddonValidator implements BillingValidation {

  private final BillingInfoSearchUtil searchUtil;

  public AddonValidator(final BillingInfoSearchUtil searchUtil) {
    this.searchUtil = searchUtil;
  } // End of constructor injection.

  /*
   * @see com.cds.cbit.inventory.interfaces.BillingValidation#validateInput
  */
  @Override
  public FList validateInput(final String input,final String... data) 
                                                                throws EBufException,JAXBException {
    final Results results = 
          Results.builder().elem("0").packageId("0").planObj("").dealObj("")
                           .purchaseEndT(0L).purchaseStartT(0L).planObj("").createdT(0L)
                           .linkedObj(LinkedObj.builder().elem("2").linkDirection("-1").dealObj("")
                           .extraResults(ExtraResults.builder().elem("0").build()).build())
                           .build();
    final Args dealObj = Args.builder().elem("1").dealObj("").build();
    final Args poidArgs = Args.builder().elem("2").poid("").build();
    final Args accountObj = Args.builder().elem("3").accountObj(input).build();
    final Args acctNoArgs = Args.builder().elem("4").status("3").build();

    final Args[] arguments = { dealObj, poidArgs, accountObj, acctNoArgs };

    return searchUtil.executeSearchTemplate(ConstantsUtil.ADDON_SQL, arguments, results, "1280");
  } // End of validateInput method.
} // End of AddonValidator class.