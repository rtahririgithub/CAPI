package com.telus.cmb.account;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.telus.cmb.account.informationhelper.InformationHelperUnitTestSuite;
import com.telus.cmb.account.lifecyclefacade.svc.impl.AccountLifecycleFacadeImplTest;
import com.telus.cmb.account.lifecyclemanager.svc.impl.AccountLifecycleManagerImplTest;
import com.telus.cmb.common.validation.BanValidatorTest;

@RunWith(Suite.class)
@SuiteClasses({
	InformationHelperUnitTestSuite.class,
	BanValidatorTest.class,
	AccountLifecycleFacadeImplTest.class,
	AccountLifecycleManagerImplTest.class
})

public class AllUnitTestSuite {

}
