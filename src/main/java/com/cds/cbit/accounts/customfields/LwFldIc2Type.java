package com.cds.cbit.accounts.customfields;

import com.portal.pcm.StrField;

/**
 * Specific Field subclasses. This subclasses of <code>Field</code> is used with
 * the FList class to specify which field is being accessed, and its type. The
 * type information is used to provide compile time type checking. These classes
 * are auto generated.
 * 
 * @version 1.0 Mon October 08 16:55:36 IST 2018
 * @author Auto generated
 */

@SuppressWarnings("serial")
public class LwFldIc2Type extends StrField {
  private static LwFldIc2Type me;

  /**
   * Constructs an instance of <code>LwFldIc2Type</code>.
  */
  public LwFldIc2Type() {
    super(20082, 5);
  }

  /**
   * Returns an instance of <code>LwFldIc2Type</code>.
   * 
   * @return An instance of <code>LwFldIc2Type</code>
   */
  public static synchronized LwFldIc2Type getInst() {
    if (me == null) {
      me = new LwFldIc2Type();
    }
    return me;
  }
}