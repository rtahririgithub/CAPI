package com.telus.cmb.unit.test.subscriber.svc.facade;

import static org.mockito.Mockito.spy;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.common.dao.provisioning.VOIPSupplementaryServiceDao;
import com.telus.cmb.common.util.EJBController;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.subscriber.lifecyclefacade.dao.PenaltyCalculationServiceDao;
import com.telus.cmb.subscriber.lifecyclefacade.svc.impl.SubscriberLifecycleFacadeImpl;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;

public class SLFBaseUnitTestHelper {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Mock (name = "subscriberLifecycleHelper")
	protected SubscriberLifecycleHelper subscriberLifecycleHelper;
	
	@Mock (name ="ejbController")
	protected EJBController ejbController;		
	
	@InjectMocks public SubscriberLifecycleFacadeImpl subscriberLifecycleFacade = spy(new SubscriberLifecycleFacadeImpl());
	
	@Mock protected AccountInformationHelper accountInformationHelper;
	@Mock protected ReferenceDataFacade referenceDataFacade;
	@Mock protected PenaltyCalculationServiceDao penaltyCalculationServiceDao;
	@Mock protected VOIPSupplementaryServiceDao voipSupplementaryServiceDao;
	
}
