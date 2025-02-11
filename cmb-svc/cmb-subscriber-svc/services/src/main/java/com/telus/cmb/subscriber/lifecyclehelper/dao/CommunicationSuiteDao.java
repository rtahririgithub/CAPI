package com.telus.cmb.subscriber.lifecyclehelper.dao;

import com.telus.eas.subscriber.info.CommunicationSuiteInfo;

public interface CommunicationSuiteDao {

	/**
	 * 
	 * @param ban - billing account number
	 * @param subscriberNum - the subscriber number that wants to retrieve the communication suite
	 * @param companionCheckLevel - 0=All, 1=Primary only, 2=Companion only. This affects the switch code to be looked for at primary or companion level. 
	 *                              If the subscriber is the primary in the communication suite, use 0 or 1 to get a result. For companion, use 0 or 2. 
	 *                              Otherwise, no result means the subscriber does not belong to a communication suite or it is not at the expected suite level.
	 * @return CommunicationSuiteInfo - the communication suite info containing the details
	 */
	public CommunicationSuiteInfo retrieveCommunicationSuite (int ban, String subscriberNum, int companionCheckLevel);
}
