package com.telus.cmb.account.informationhelper;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.telus.cmb.account.informationhelper.dao.impl.AccountDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.AddressDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.AdjustmentDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.CollectionDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.CreditCheckDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.DepositDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.EquipmentDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.FleetDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.FollowUpDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.InvoiceDaoImplIntTest;
//import com.telus.cmb.account.informationhelper.dao.impl.LetterDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.MemoDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.PaymentDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.PrepaidDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.SubscriberDaoImplIntTest;
import com.telus.cmb.account.informationhelper.dao.impl.UsageDaoImplIntTest;
import com.telus.cmb.account.informationhelper.svc.impl.AccountInformationHelperImplIntTest;

@RunWith(Suite.class)
@SuiteClasses({
	AccountDaoImplIntTest.class,
	AddressDaoImplIntTest.class,
	AdjustmentDaoImplIntTest.class,
	CollectionDaoImplIntTest.class,
	CreditCheckDaoImplIntTest.class,
	DepositDaoImplIntTest.class,
	EquipmentDaoImplIntTest.class,
	FleetDaoImplIntTest.class,
	FollowUpDaoImplIntTest.class,
	InvoiceDaoImplIntTest.class,
	//LetterDaoImplIntTest.class,
	MemoDaoImplIntTest.class,
	PaymentDaoImplIntTest.class,
	PrepaidDaoImplIntTest.class,
	SubscriberDaoImplIntTest.class,	
	UsageDaoImplIntTest.class,
	AccountInformationHelperImplIntTest.class
})
public class InformationHelperIntTestSuite {

}
