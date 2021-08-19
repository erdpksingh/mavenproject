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


public class LwFldDateofbirth extends StrField {

  /**
   * Generated Serial Version UUID.
  */
  private static final long serialVersionUID = 7177857478552481887L;

  /**
   * Constructs an instance of <code>LwFldDateofbirth</code>.
  */
  public LwFldDateofbirth() { 
    super(10000051, 5); 
  }

  /**
   * Returns an instance of <code>LwFldDateofbirth</code>.
   * @return An instance of <code>LwFldDateofbirth</code>
  */
  public static synchronized LwFldDateofbirth getInst() { 
    if (me == null) {
      me = new LwFldDateofbirth();
    }
    return me;
  }

  private static LwFldDateofbirth me;
} // End of Date Of Birth.