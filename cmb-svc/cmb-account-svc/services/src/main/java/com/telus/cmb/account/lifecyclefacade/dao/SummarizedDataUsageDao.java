package com.telus.cmb.account.lifecyclefacade.dao;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

/**
 * @author Michael Liao
 *
 */
public interface SummarizedDataUsageDao {

	public double getTotalOutstandingAmount(int banId, Date fromDate) throws ApplicationException;
	public double getTotalUnbilledAmount(int banId, int billCycleYear, int billCycleMonth, int billCycle) throws ApplicationException;
	public TestPointResultInfo test();

}