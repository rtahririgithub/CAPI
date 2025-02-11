package com.telus.provider.util.transition;

/**
 * This class provides a skeletal implementation of the transition matrix.<p>
 *
 * To implement a particular transition state, the programmer needs only to
 * implement the method initialize() and store all initial states that have
 * forbidden final states.<p>
 *
 * Private field <tt>initStates</tt> contains an array of all initial states
 * states that have non-empty lists of forbidden transitions associated with
 * them while the field <tt>size</tt> contains it size.
 *
 * @author  Vladimir Tsitrin
 * @version 1.0, 08/19/05
 * @version 2.0, 10/03/05
 * @see EquipmentTransitionMatrix
 * @see State
 * @see Condition
 * @see ValidationResult
 * @since 2.786.0.0
 */

abstract class TransitionMatrix {
  /**
   * List of initila states states that have non-empty lists of forbidden transitions
   * associated with them.
   *
   * @see State
   */
  private State[] initStates;

  /**
   * Size of <tt>initStates</tt>.
   */
  private int size = 0;

  /**
   * Constructs an instance of <tt>TransitionMatrix</tt> and populates its list of
   * non-trivial states.
   */
  TransitionMatrix() {
    try {
      initialize();
    }
    catch(Throwable t) {
      // do nothing
    }
  }

  /**
   * This method populates the matrix's list of non-trivial states, <i>i.e.</i>, the
   * states that have non-empty lists of forbidden transitions associated with them.
   */
  abstract void initialize() throws Throwable;

  /**
   * Adds a non-trivial initial state to <tt>initStates</tt> if the latter is not
   * already contained in <tt>initStates</tt>.
   * @param newInitState new non-trivial initial state.
   */
  final void addInitState(State newInitState) {
    if (newInitState != null) {
      State[] oldInitStates = initStates;
      initStates = new State[++size];

      if (oldInitStates != null)
        System.arraycopy(oldInitStates, 0, initStates, 0, size - 1);

      initStates[size - 1] = newInitState;
    }
  }

  /**
   * This method examines the validity of the transition between <tt>initState</tt> and <tt>finalState</tt>.
   * The former must have initialized state fields and all appropriate conditions while for the latter
   * it's enough to have only the state fields. The result is <tt>true</tt> when and only when both initial
   * and final states are properly initialized and transition was not explicitly forbidden during the matrix
   * initilization.
   *
   * @param initState initial state.
   * @param finalState final state.
   * @return <tt>ValidationResult.VALID</tt> if transition is allowed; specific validation result otherwise.
   * @see State
   * @see Condition
   * @see ValidationResult
   */
  final ValidationResult validTransition(State initState, State finalState) {
    if (initState == null || finalState == null)
      throw new NullPointerException("TransitionMatrix.invalidTransition(): input parameters cannot be null.");

    if (initState == finalState) {
      return ValidationResult.VALID;
    }
    else {
      String[] initNTF = initState.getNonTransitiveFields();
      String[] finalNTF = finalState.getNonTransitiveFields();

      if (initNTF == null || finalNTF == null)
        throw new NullPointerException("TransitionMatrix.invalidTransition(): non-transitive fields cannot be null.");

      if (initNTF.length != finalNTF.length)
        return ValidationResult.INVALID_NTF;

      for (int i = 0; i < initNTF.length; i++)
        if (initNTF[i] == null || finalNTF[i] == null || !finalNTF[i].equals(initNTF[i]))
          return ValidationResult.INVALID_NTF;

      for (int i = 0; i < size; i++)
        if (initStates[i].equals(initState) && initStates[i].includes(initState)) {
          State invalidFinalState = initStates[i].hasInvalidFinalState(finalState);

          if (invalidFinalState != null)
            return invalidFinalState.getValidationResult();
        }
    }

    return ValidationResult.VALID;
  }

  /**
   * @param initState initial state.
   * @param finalStates possible final states.
   * @param matchAll <tt>true</tt> if all final states must be allowed for the given initial state; <tt>false</tt> if
   * at least one final state must be allowed for the given initial state to mate the transition allowed.
   * @return <tt>ValidationResult.VALID</tt> if transition is allowed; specific validation result otherwise.
   * @see State
   * @see Condition
   * @see ValidationResult
   */
  final ValidationResult validTransition(State initState, State[] finalStates, boolean matchAll) {
    ValidationResult validationResult = ValidationResult.VALID;

    if (initState == null || finalStates == null || finalStates.length == 0)
      throw new NullPointerException("TransitionMatrix.invalidTransition(): input parameters cannot be null or empty.");

    for (int i = 0; i < finalStates.length; i++) {
      validationResult = validTransition(initState, finalStates[i]);

      if ((matchAll && validationResult != ValidationResult.VALID) || (!matchAll && validationResult == ValidationResult.VALID))
        return validationResult;
    }

    return validationResult;
  }
}
