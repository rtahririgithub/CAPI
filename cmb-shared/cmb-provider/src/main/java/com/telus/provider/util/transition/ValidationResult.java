package com.telus.provider.util.transition;

/**
 * This class implements a holder of the validation results for equipment swaps.
 *
 * @author  Vladimir Tsitrin
 * @version 2.0, 10/03/05
 * @see TransitionMatrix
 * @see EquipmentState
 * @see Condition
 * @since 2.860.0.0
 */
public final class ValidationResult {
  /**
   * Error messageId.
   */
  private final long messageId;

  /**
   * Package private constructor for a validation result with specific error messageId and messages.
   * 
   * @param messageId Error messageId.
   */
  ValidationResult(long messageId) {
    this.messageId = messageId;
  }

  /**
   * Private constructor used to create a valid result.
   */
  private ValidationResult() {
    this.messageId = ERROR_CODE_VALID;
  }

  private static final long ERROR_CODE_VALID = 0;
  private static final long ERROR_CODE_GENERAL = 100;

  /**
   * Constant to be returned by successful validations.
   */
  public static final ValidationResult VALID = new ValidationResult();

  /**
   * General error constant corresponding to mismatch of the non-transitive fields.
   */
  public static final ValidationResult INVALID_NTF = new ValidationResult(ERROR_CODE_GENERAL);

  /**
   * General error constant.
   */
  public static final ValidationResult INVALID_GENERAL = new ValidationResult(ERROR_CODE_GENERAL);

  /**
   * Returns the error messageId - for future use.
   *
   * @return Error messageId.
   */
  public long getMessageId() {
    return messageId;
  }

  /**
   * Returns a string representation of the object.
   * @return String representation of the object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append("ValidationResult: {\n");
    sb.append("    valid=[").append(this == VALID).append("]\n");
    sb.append("    messageId=[").append(messageId).append("]\n");
    sb.append("}");

    return sb.toString();
  }
}
