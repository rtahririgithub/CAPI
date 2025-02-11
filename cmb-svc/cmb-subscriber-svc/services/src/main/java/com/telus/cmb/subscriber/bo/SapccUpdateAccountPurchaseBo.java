package com.telus.cmb.subscriber.bo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.cmb.subscriber.utilities.SapccUpdateAccountPurchaseContext;

public class SapccUpdateAccountPurchaseBo {

	private static final Log logger = LogFactory.getLog(SapccUpdateAccountPurchaseBo.class);

	private static final String SAPCC_UPDATE_ACCOUNT_PURCHASE_CHARGE = "CHARGE";
	private static final String SAPCC_UPDATE_ACCOUNT_PURCHASE_REIMBURSE = "REIMBURSE";

	private final SapccUpdateAccountPurchaseContext context;

	public SapccUpdateAccountPurchaseBo(SapccUpdateAccountPurchaseContext context) {
		this.context = context;
	}

	public boolean chargeSapccAcountPurchaseAmount() throws ApplicationException {
		return updateSapccAcountPurchaseAmount(SAPCC_UPDATE_ACCOUNT_PURCHASE_CHARGE);
	}

	public boolean reimburseSapccAcountPurchaseAmount() throws ApplicationException {
		return updateSapccAcountPurchaseAmount(SAPCC_UPDATE_ACCOUNT_PURCHASE_REIMBURSE);	
	}
	
	private boolean updateSapccAcountPurchaseAmount(String actionCode) throws ApplicationException {
		
		// Set the sapccUpdated flag to 'false' to signal that OCSSAM has not been called yet
		context.setSapccUpdated(false);
		
		// If there are no WCC services being added to the contract we can simply return 'false' and continue BAU
		if (CollectionUtils.isEmpty(context.getWCCServicesList())) {
			logger.debug("SapccUpdateAccountPurchaseBo.updateSapccAcountPurchaseAmount - no WCC service changes on the subscriber contract.");
			return isSappccUpdated();
		}

		// Call OCSSAM to determine if a breach has occurred and to update the counters
		context.getSubscriberLifecycleFacade().updateSapccAccountPurchaseAmount(context.getSubscriber(), context.getDomesticAmount(), context.getRoamingAmount(), actionCode, context.getAuditInfo().getOriginatorAppId());

		// If there's no breach, set the sapccUpdated flag to 'true' to signal that the OCSSAM call was successful
		context.setSapccUpdated(true);
		
		// Return 'true' to signal that OCSSAM call has successfully updated the BAN PPU counters
		return isSappccUpdated();
	}
	
	public boolean isSappccUpdated() {
		return context.isSapccUpdated();
	}

}