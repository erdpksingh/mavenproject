package com.cds.cbit.accounts.customfields;

import com.portal.pcm.StrField;

/**
 * Specific Field subclasses. This subclasses of <code>Field</code> is used with the FList class to
 * specifiy which field is being accessed, and its type. The type information is used to provide
 * compile time type checking. These classes are auto generated.
 * 
 * @version 1.0 Mon Jul 16 11:58:54 IST 2018
 * @author Autogenerated
 */

public class LwFldNationality extends StrField {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs an instance of <code>LwFldNationality</code>.
   */
  public LwFldNationality() {
    super(20079, 5);
  }

  /**
   * Returns an instance of <code>LwFldNationality</code>.
   * 
   * @return An instance of <code>LwFldNationality</code>.
   */
  public static synchronized LwFldNationality getInst() {
    if (me == null) {
      me = new LwFldNationality();
    }
    return me;
  }

  private static LwFldNationality me;
}