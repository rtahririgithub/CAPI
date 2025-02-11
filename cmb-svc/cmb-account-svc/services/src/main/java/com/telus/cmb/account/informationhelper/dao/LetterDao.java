//package com.telus.cmb.account.informationhelper.dao;
//
//import com.telus.api.ApplicationException;
//import com.telus.eas.account.info.SearchResultsInfo;
//
//public interface LetterDao {
//
//	/**
//	 * Retrieves letter requests for specified BAN, level (account/subscriber/all), and created within specified date range.
//	 * The result set is limited by specifying the maximum argument.
//	 *
//	 * @param banId  					billing account number (BAN)
//	 * @param from
//	 * @param to
//	 * @param level						ChargeType.CHARGE_LEVEL_ACCOUNT, ChargeType.CHARGE_LEVEL_SUBSCRIBER, AccountManager.Search_All
//	 * @param pSubscriber (optional)
//	 * @param maximum
//	 * @return SearchResultsInfo with underlying array objects of type LMSLetterRequestInfo
//	 * @throws ApplicationException
//	 */
////	SearchResultsInfo retrieveLetterRequests(int banId, java.util.Date from, java.util.Date to, char level, String pSubscriber, int maximum) throws ApplicationException;
//
//}
