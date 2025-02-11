package com.telus.api.reference;

import com.telus.api.ClientAPI;
import com.telus.api.JMeterBaseTest;
import com.telus.api.TelusAPIException;

public class PricePlanSummaryTest extends JMeterBaseTest {
	
	public void testGetOptionalServicesAndContainsPrivilige() throws TelusAPIException {
		ClientAPI api = ClientAPI.getInstance("18654", "apollo", "JMETER_TESTING");
		api.getProvider();
		ReferenceDataManager rdm = api.getReferenceDataManager();
		
		PricePlanSelectionCriteria criteria = new PricePlanSelectionCriteria();
		criteria.setProductType("C");
		criteria.setEquipmentType("D");
		criteria.setProvinceCode("ON");
		criteria.setAccountType(new Character('I'));
        criteria.setAccountSubType(new Character('R'));
		criteria.setBrandId(new Integer(1));
		criteria.setIncludeFeaturesAndServices(new Boolean(true));
		criteria.setCurrentPlansOnly(new Boolean(true));
		PricePlanSummary[] pricePlans = rdm.findPricePlans(criteria);
		for (int i = 0; i < pricePlans.length; i++) {
			PricePlan pricePlan = rdm.getPricePlan(pricePlans[i].getCode(), "D","ON", 'I', 'R', 1);
			Service[] optionalServices = pricePlan.getOptionalServices();
			for (int j = 0; j < optionalServices.length; j++) {
				optionalServices[j].containsPrivilege("CLIENT", "ADD");
			}
		}

	}
}
