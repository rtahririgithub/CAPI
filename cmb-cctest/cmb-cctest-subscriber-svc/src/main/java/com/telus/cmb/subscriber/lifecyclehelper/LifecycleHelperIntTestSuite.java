package com.telus.cmb.subscriber.lifecyclehelper;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.AccountDaoImplIntTest;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.AddressDaoImplIntTest;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.DepositDaoImplIntTest;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.FleetDaoImplIntTest;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.InvoiceDaoImplIntTest;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.MemoDaoImplIntTest;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.PrepaidDaoImplIntTest;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.ProvisioningDaoImplIntTest;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.ServiceAgreementDaoImplIntTest;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.SubscriberDaoImplIntTest;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.SubscriberEquipmentDaoImplIntTest;


@RunWith(Suite.class)
@SuiteClasses({
	PrepaidDaoImplIntTest.class,
	ProvisioningDaoImplIntTest.class,
	AccountDaoImplIntTest.class,
	AddressDaoImplIntTest.class,
	DepositDaoImplIntTest.class,
	FleetDaoImplIntTest.class,
	InvoiceDaoImplIntTest.class,
	MemoDaoImplIntTest.class,
	ServiceAgreementDaoImplIntTest.class,
	SubscriberDaoImplIntTest.class,
	SubscriberEquipmentDaoImplIntTest.class


	
})
public class LifecycleHelperIntTestSuite {

}
