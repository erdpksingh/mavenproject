package com.cds.cbit.accounts.customfields;

import com.portal.pcm.StrField;

/**
 * Specific Field subclasses. This subclasses of <code>Field</code> is used with the FList class to
 * specify which field is being accessed, and its type. The type information is used to provide
 * compile time type checking. These classes are auto generated.
 * 
 * @version 1.0 Mon October 08 16:55:36 IST 2018
 * @author Auto generated
 */

@SuppressWarnings("serial")
public class LwFldIc2 extends StrField {
  private static LwFldIc2 me;

  /**
   * Constructs an instance of <code>LwFldIc2</code>.
   */
  public LwFldIc2() {
    super(20081, 5);
  }

  /**
   * Returns an instance of <code>LwFldIc2</code>.
   * 
   * @return An instance of <code>LwFldIc2</code>
   */
  public static synchronized LwFldIc2 getInst() {
    if (me == null) {
      me = new LwFldIc2();
    }
    return me;
  }
}