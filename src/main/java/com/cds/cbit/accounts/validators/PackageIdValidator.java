package com.cds.cbit.accounts.validators;

import com.cds.cbit.accounts.commons.beans.Args;
import com.cds.cbit.accounts.commons.beans.Results;
import com.cds.cbit.accounts.exceptions.BillingException;
import com.cds.cbit.accounts.interfaces.TransactionValidator;
import com.cds.cbit.accounts.util.BillingInfoSearchUtil;
import com.cds.cbit.accounts.util.ConstantsUtil;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.PortalContext;
import com.portal.pcm.fields.FldResults;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;


/**
 * The implementation component provides methods for validating account for ADDON purchase 
 *  in the system.
 * The component overrides BillingValidationInterface validateInput method to create and execute 
 * add on search FList.If the validation is success then the method will return output FList else
 * it will throw an exception to the caller.
 * 
 * @author Meghashree Udupa
 *
*/
@Component("packageIdValidator")
public class PackageIdValidator implements TransactionValidator {

  private final BillingInfoSearchUtil searchUtil;

  public PackageIdValidator(final BillingInfoSearchUtil searchUtil) {

    this.searchUtil = searchUtil;
  } // End of Constructor Injection

  /*
   * @see com.cds.cbit.inventory.interfaces.BillingValidation#validateInput
   */
  @Override
  public FList validateInput(final String input,final PortalContext portal,final String... data)
                                                             throws EBufException, JAXBException {

    final Args[] arguments = { Args.builder().elem("1").accountObj(input).build() };

    final Results results = Results.builder().elem("0").packageId("0").build();

    final FList output =  searchUtil.executeTransactionSearchTemplate(
                                     ConstantsUtil.PACKAGE_ID_SQL, arguments, results, portal);

    if (!(output.hasField(FldResults.getInst()))) {
      
      throw new BillingException("100116");
    } // End of result field check for deal code.
    return output;
  } // End of validateInput.
} // End of PackageIdValidator class.