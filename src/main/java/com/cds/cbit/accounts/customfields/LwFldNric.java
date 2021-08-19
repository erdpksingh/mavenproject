package com.cds.cbit.accounts.customfields;

import com.portal.pcm.StrField;

/**
 * Specific Field subclasses. This subclasses of <code>Field</code>
 * is used with the FList class to specify which field is being
 * accessed, and its type.  The type information is used to provide
 * compile time type checking.  These classes are auto generated.
 * @version 1.0 Wed Feb 07 18:32:01 IST 2018
 * @author Auto Generated
 */

public class LwFldNric extends StrField {

  /**
   * Generated Serial Version UUID.
  */
  private static final long serialVersionUID = -3787014938716963311L;
  
  /**
   * Constructs an instance of <code>LwFldNric</code>.
  */
  public LwFldNric() { 
    super(10000052, 5); 
  }

  /**
   * Returns an instance of <code>LwFldNric</code>.
   * @return An instance of <code>LwFldNric</code>
  */
  public static synchronized LwFldNric getInst() { 
    if (me == null) {
      me = new LwFldNric();
    }
    return me;
  }
  
  private static LwFldNric me;
} // End of Field NRIC.