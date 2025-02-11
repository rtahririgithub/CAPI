package com.telus.cmb.subscriber.utilities.transition;

/**
 * Represents a transition condition that consists either of an array of discrete
 * values or the aggregate value <tt>ALL</tt>. Inclusive conditions contain a list
 * of values that satisfy the given condition while the exclusive ones contain a list
 * of values that don't satisfy it.<p>
 *
 * @author  Vladimir Tsitrin
 * @version 1.0, 08/19/05
 * @see State
 * @see TransitionMatrix
 * @since 2.786.0.0
 */

final class Condition {
  /**
   * Constant that designatess all possible values. Used only in inclusive conditions.
   */
  static final String ALL = "*";

  /**
   * List of condition values.
   */
  private final String[] values;

  /**
   * Indicator of the condition type.
   */
  private final boolean inclusive;

  /**
   * Constructs a condition with a specified list of valid or invalid values depending on the condition
   * type.
   *
   * @param values list of valid (for inclusive conditions) or invalid (for exclusive conditions) values.
   * @param inclusive condition type: <tt>true</tt> for inclusive; <tt>false</tt> otherwise.
   */
  Condition(String[] values, boolean inclusive) {
    if (values == null || values.length == 0) {
      this.values = new String[]{ALL};
      this.inclusive = true;
    }
    else {
      // create defensive copy [Bloch, Item 24]
      this.values = new String[values.length];
      System.arraycopy(values, 0, this.values, 0, values.length);
      this.inclusive = inclusive;
    }
  }

  /**
   * Constructs a condition with a specified valid or invalid value depending on the condition type.
   *
   * @param value valid (for inclusive conditions) or invalid (for exclusive conditions) value.
   * @param inclusive condition type: <tt>true</tt> for inclusive; <tt>false</tt> otherwise.
   */
  Condition(String value, boolean inclusive) {
    if (value == null) {
      this.values = new String[] {ALL};
      this.inclusive = true;
    }
    else {
      this.values = new String[] {value};
      this.inclusive = inclusive;
    }
  }

  /**
   * Constructs an inclusive condition with a specified list of valid values.
   *
   * @param values list of valid values.
   */
  Condition(String[] values) {
    if (values == null || values.length == 0) {
      this.values = new String[]{ALL};
    }
    else {
      this.values = values;
    }

    this.inclusive = true;
  }

  /**
   * Constructs an inclusive condition with a specified valid value.
   *
   * @param value valid value.
   */
  Condition(String value) {
    if (value == null) {
      this.values = new String[] {ALL};
    }
    else {
      this.values = new String[] {value};
    }

    this.inclusive = true;
  }

  /**
   * Constructs an all-inclusive condition.
   */
  Condition() {
    this.values = new String[]{ALL};
    this.inclusive = true;
  }

  /**
   * @return list of values.
   */
  private String[] getValues() {
    return values;
  }

  /**
   * Checks if condition <tt>c</tt> satisfies this condition. The result is <tt>true</tt> if <tt>c</tt> is not
   * <tt>null</tt> and contains at least one value satisfying this condition if the latter is inclusive or doesn't
   * contain any value matching values of this condition if the latter is exclusive.
   *
   * @param c condition to be verified.
   * @return <tt>true</tt> if <tt>c</tt> satisfies thiscondition; <tt>false</tt> otherwise.
   */
  boolean satisfies(Condition c) {
    if (c == null)
      return false;

    if (values[0].equals(ALL))
      return c.inclusive;

    String[] cValues = c.getValues();

    if (inclusive && c.inclusive) {
      for (int i = 0; i < cValues.length; i++) {
        boolean matched = false;

        for (int j = 0; j < values.length; j++)
          matched |= cValues[i].equals(values[j]);

        if (!matched)
          return false;
      }

      return true;
    }
    else if (!inclusive && !c.inclusive) {
      for (int i = 0; i < values.length; i++) {
        boolean matched = false;

        for (int j = 0; j < cValues.length; j++)
          matched |= values[i].equals(cValues[j]);

        if (!matched)
          return false;
      }

      return true;
    }
    else if (!inclusive && c.inclusive) {
      for (int i = 0; i < cValues.length; i++)
        for (int j = 0; j < values.length; j++)
          if (cValues[i].equals(ALL) || cValues[i].equals(values[j]))
            return false;

      return true;
    }

    return false;
  }

  /**
   * Return a string representation of this condition.
   *
   * @return a string representation of this condition.
   */
  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("utility.Condition:{\n");

    if (values != null)
      for (int i = 0; i < values.length; i++)
        s.append("    value[").append(i).append("]=").append(values[i]).append("]\n");
    else
      s.append("    values=[null]\n");

    s.append("    inclusive=[").append(inclusive).append("]\n");

    s.append("}");

    return s.toString();
  }
}
