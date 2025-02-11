package com.telus.cmb.reference.svc;
import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;


public interface ReferenceDataFacadeTestPoint {
	TestPointResultInfo testRefPds();
	String openSession(String userId, String password, String applicationId) throws ApplicationException;
	String getVersion();
}
