package com.cds.cbit.accounts.customfields;

import com.portal.pcm.StrField;

/**
 * Specific Field subclasses. This subclasses of <code>Field</code> is used with the FList class to
 * specifiy which field is being accessed, and its type. The type information is used to provide
 * compile time type checking. These classes are auto generated.
 * 
 * @version 1.0 Mon May 28 18:56:02 IST 2018
 * @author Autogenerated
 */

public class LwFldActivityId extends StrField {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs an instance of <code>LwFldActivityId</code>.
   */
  public LwFldActivityId() {
    super(20101, 5);
  }

  /**
   * Returns an instance of <code>LwFldActivityId</code>.
   * 
   * @return An instance of <code>LwFldActivityId</code>.
   */
  public static synchronized LwFldActivityId getInst() {
    if (me == null) {
      me = new LwFldActivityId();
    }
    return me;
  }

  private static LwFldActivityId me;
}