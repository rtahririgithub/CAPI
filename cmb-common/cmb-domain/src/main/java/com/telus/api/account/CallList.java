package com.telus.api.account;

import com.telus.api.TelusAPIException;

/**
 * <CODE>CallList</CODE>
 *
 */
public interface CallList {

  char CALL_TYPE_ROAMING = 'R';
  char CALL_TYPE_LOCAL = 'L';
  char CALL_TYPE_ALL = 'A';

	int getTotalCallCount();

	/**
	 * Retrieves the call summary array.
	 *
	 * <P>This method may involve a remote method call.
	 */
	CallSummary[] getCallSummaries() throws TelusAPIException;

}
