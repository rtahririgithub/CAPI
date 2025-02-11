package com.telus.cmb.subscriber.utilities.transition;

/**
 * This class provides a skeletal implementation of the transition state.<p>
 *
 * To implement a particular transition state, the programmer needs only to
 * define the fields that comprise the state vector (state fields), define
 * the condition fields and implement abstract methods <tt>getFields</tt>,
 * <tt>getNonTransitiveFields</tt> and <tt>getConditions</tt>. Non-transitive
 * fields are a subset of state fields that cannot be changed, i.e.,
 * transitions are always impossible between the states with different values
 * of non-transitive fields.<p>
 *
 * Private field <tt>invalidFinalStates</tt> contains an array of all final
 * states, corresponding to forbidden transitions from the given initial state,
 * while the field <tt>size</tt> contains it size.
 *
 * @author  Vladimir Tsitrin
 * @version 1.0, 08/19/05
 * @version 2.0, 10/03/05
 * @see EquipmentState
 * @since 2.786.0.0
 */

abstract class State {
  /**
   * Array of final states to which transitions are not allowed from the given state.
   */
  private State[] invalidFinalStates;

  /**
   * Size of <tt>invalidFinalStates</tt>.
   */
  private int size = 0;

  /**
   * Validation result for the given final state.
   */
  protected ValidationResult validationResult;

  /**
   * Adds a forbidden final state to the list and increases the value of <tt>size</tt>.
   *
   * @param newFinalState new forbidden final state.
   */
  final void addInvalidFinalState(State newFinalState) {
    if (newFinalState == null || hasInvalidFinalState(newFinalState) != null)
      return;

    State[] oldFinalStates = invalidFinalStates;
    invalidFinalStates = new State[++size];

    if (oldFinalStates != null)
      System.arraycopy(oldFinalStates, 0, invalidFinalStates, 0, size - 1);

    invalidFinalStates[size - 1] = newFinalState;
  }

  /**
   * Adds an array of forbidden final states to the list and increases the value of <tt>size</tt>.
   *
   * @param newFinalStates new forbidden final states.
   */
  final void addInvalidFinalStates(State[] newFinalStates) {
    if (newFinalStates == null)
      return;

    for (int i = 0; i < newFinalStates.length; i++)
      addInvalidFinalState(newFinalStates[i]);
  }

  /**
   * Returns the array of final states to which transitions are not allowed.
   *
   * @return array of final states to which transitions are not allowed.
   */
  final State[] getInvalidFinalStates() {
    return invalidFinalStates;
  }

  /**
   * Aggregate constant designating all possible field values.
   */
  static final String FIELD_VALUE_ALL = "*";

  /**
   * Returns the array of state fields.
   *
   * @return array of state fields.
   */
  abstract String[] getFields();

  /**
   * Returns the array of non-transitive state fields.
   *
   * @return array of non-transitive state fields.
   */
  abstract String[] getNonTransitiveFields();

  /**
   * Returns the array of transition conditions.
   *
   * @return array of transition conditions.
   */
  abstract Condition[] getConditions();

  /**
   * Compares this state to the specified object. The result is <code>true</code>
   * if and only if the argument is not <code>null</code> and is a <code>State</code>
   * object that contains the same set of state fields as this object.
   *
   * @param o the object to compare with.
   * @return <code>true</code> if the states are the same; <code>false</code> otherwise.
   */
  public final boolean equals(Object o) {
    if (o == null)
      return false;

    if (o.getClass() != this.getClass())
      return false;

    if (o == this)
      return true;

    String[] aFields = this.getFields();
    String[] oFields = ((State) o).getFields();

    if (aFields == null || oFields == null)
      return false;
    else {
      if (oFields.length != aFields.length)
        return false;

      for (int i = 0; i < aFields.length; i++)
        if (!(aFields[i].equals(oFields[i]) || aFields[i].equals(FIELD_VALUE_ALL) || oFields[i].equals(FIELD_VALUE_ALL)))
          return false;
    }

    return true;
  }

  /**
   * Compares transition conditions of this state and of the specified one. The result is <code>true</code>
   * if and only if the argument is not <code>null</code> and is an instance of the same class as this state
   * <code>State</code> that contains the same number of transition conditions that are identical or are
   * subsets of the appropriate conditions of the given state.
   *
   * @param s the state to compare with.
   * @return <code>true</code> if the states' transition conditions are the same; <code>false</code> otherwise.
   */
  final boolean includes(State s) {
    if (s == null)
      return false;

    if (s.getClass() != this.getClass())
      return false;

    if (s == this)
      return true;

    Condition[] tConditions = getConditions();
    Condition[] sConditions = s.getConditions();

    if (tConditions == null || sConditions == null)
      return false;

    if (tConditions.length != sConditions.length)
      return false;

    for (int i = 0; i < tConditions.length; i++)
      if (!tConditions[i].satisfies(sConditions[i]))
        return false;

    return true;
  }

  /**
   * Checks if the <tt>finalState</tt> is contained in the list of forbidden final states for the given initial state.
   *
   * @param finalState final state to be checked.
   * @return Member of <tt>invalidFinalStates</tt> matching the <tt>finalState</tt>; <tt>null</tt> if no matching final state found.
   */
  final State hasInvalidFinalState(State finalState) {
    if (invalidFinalStates == null || finalState == null)
      return null;

    for (int i = 0; i < invalidFinalStates.length; i++)
      if (invalidFinalStates[i].equals(finalState))
        return invalidFinalStates[i];

    return null;
  }

  /**
   * Returns the validation result for the given final state.
   *
   * @return Validation result.
   */
  public final ValidationResult getValidationResult() {
    return validationResult;
  }
}
