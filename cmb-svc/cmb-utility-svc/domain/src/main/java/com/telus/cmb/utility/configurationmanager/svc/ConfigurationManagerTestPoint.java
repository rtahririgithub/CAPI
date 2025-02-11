package com.telus.cmb.utility.configurationmanager.svc;

import com.telus.eas.framework.info.TestPointResultInfo;

public interface ConfigurationManagerTestPoint {
	TestPointResultInfo testEasDataSource();
	TestPointResultInfo testServDataSource();
	TestPointResultInfo getLogUtilityPkgVersion();
	String getVersion();
}
