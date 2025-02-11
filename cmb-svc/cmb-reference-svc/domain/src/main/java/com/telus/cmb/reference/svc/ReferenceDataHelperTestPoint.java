package com.telus.cmb.reference.svc;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface ReferenceDataHelperTestPoint {
	public TestPointResultInfo testKnowbilityDataSource();
	public TestPointResultInfo testDistDataSource();
	public TestPointResultInfo testEcpcsDataSource();
	public TestPointResultInfo testConeDataSource();
	public TestPointResultInfo testCodsDataSource();
	public TestPointResultInfo testEasDataSource();
	public TestPointResultInfo testRefDataSource();
	public TestPointResultInfo testEmcmDataSource();
	public String openSession(String userId, String password, String applicationId) throws ApplicationException;
	public TestPointResultInfo testProductOfferingService();
	public TestPointResultInfo testWsoisService();
	public TestPointResultInfo  getPricePlanUtilityPkgVersion();
	public TestPointResultInfo  getReferencePkgVersion();
	public TestPointResultInfo  getRuleUtilityPkgVersion();
	String getVersion();
}
