package com.telus.cmb.subscriber.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemException;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.util.BaseContext;
import com.telus.cmb.common.util.DateUtil;
import com.telus.cmb.common.util.EJBController;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.SapccOfferInfo;
import com.telus.eas.utility.info.WCCServiceExtendedInfo;

public class SapccUpdateAccountPurchaseContext extends BaseContext {

	private SubscriberInfo subscriberInfo;
	private SubscriberContractInfo subscriberContractInfo;
	private List<WCCServiceExtendedInfo> wccServicesList;
	private double domesticAmount = 0;
	private double roamingAmount = 0;
	private boolean sapccUpdated;
	private Date logicalDate;

	public SapccUpdateAccountPurchaseContext(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, EJBController ejbController,
			ClientIdentity clientIdentity, AuditInfo auditInfo) {		
		super(ejbController, clientIdentity, auditInfo);
		this.subscriberInfo = subscriberInfo;
		this.subscriberContractInfo = subscriberContractInfo;
	}

	public ReferenceDataFacade getReferenceDataFacade() throws ApplicationException {
		return ejbController.getEjb(ReferenceDataFacade.class);
	}

	public ReferenceDataHelper getReferenceDataHelper() throws ApplicationException {
		return ejbController.getEjb(ReferenceDataHelper.class);
	}

	public AccountInformationHelper getAccountInformationHelper() throws ApplicationException {
		return ejbController.getEjb(AccountInformationHelper.class);
	}

	public AccountLifecycleManager getAccountLifecycleManager() throws ApplicationException {
		return ejbController.getEjb(AccountLifecycleManager.class);
	}

	public AccountLifecycleFacade getAccountLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(AccountLifecycleFacade.class);
	}

	public SubscriberLifecycleFacade getSubscriberLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(SubscriberLifecycleFacade.class);
	}

	public String getAccountLifecycleFacadeSessionId() throws ApplicationException {
		return getSessionId(getAccountLifecycleFacade());
	}

	public String getAccountLifecycleManagerSessionId() throws ApplicationException {
		return getSessionId(getAccountLifecycleManager());
	}

	public void initialize() throws ApplicationException {
		// Retrieve context objects required to execute WCC SAPCC update account-level pay-per-use (PPU) threshold purchase functionality
		try {
			this.logicalDate = getReferenceDataFacade().getLogicalDate();
			this.wccServicesList = Arrays.asList(getReferenceDataFacade().getWCCServiceExtendedInfo(subscriberContractInfo.getServiceCodes0(false)));
		} catch (TelusException te) {
			throw new ApplicationException(ErrorCodes.REFERENCE_DATA_ERROR, "Error retrieving reference data from WCC SAPCC.", "", te);
		}
		filterServices();
		calculateChargeAmounts();
	}

	public SubscriberInfo getSubscriber() {
		return subscriberInfo;
	}

	public SubscriberContractInfo getSubscriberContract() {
		return subscriberContractInfo;
	}

	public List<WCCServiceExtendedInfo> getWCCServicesList() {
		return wccServicesList;
	}

	public Double getDomesticAmount() {
		return domesticAmount;
	}

	public void setDomesticAmount(double domesticAmount) {
		this.domesticAmount = domesticAmount;
	}

	public Double getRoamingAmount() {
		return roamingAmount;
	}

	public void setRoamingAmount(double roamingAmount) {
		this.roamingAmount = roamingAmount;
	}

	@Override
	public void refresh() throws SystemException, ApplicationException {
		// TODO Auto-generated method stub		
	}
	
	private void filterServices() {
		// For purposes of calculating updates to the SAPCC counters, we only care about immediate ADD transactions - all other services should be filtered from
		// the final WCC services list.
		List<WCCServiceExtendedInfo> filteredServicesList = new ArrayList<WCCServiceExtendedInfo>();
		for (WCCServiceExtendedInfo info : wccServicesList) {
			ServiceAgreementInfo service = subscriberContractInfo.getService0(info.getCode(), false);			
			if (service != null && service.getTransaction() == ServiceAgreementInfo.ADD && !DateUtil.isAfter(service.getEffectiveDate(), logicalDate)) {
				filteredServicesList.add(info);
			}
		}
		this.wccServicesList = filteredServicesList;
	}
	
	private void calculateChargeAmounts() {
		// Calculate the WCC charges by zone. Note: the assumption is that this logic is executed AFTER all higher level business logic to add / remove / modify 
		// services have already been applied to the service agreement. The service agreement should be as close as final as possible, other than saving it to KB.
		for (WCCServiceExtendedInfo info : wccServicesList) {
			if (StringUtils.equalsIgnoreCase(SapccOfferInfo.ZONE_DOMESTIC, info.getSapccOfferInfo().getZone())) {
				this.domesticAmount += info.getChargeAmount();
			}
			if (StringUtils.equalsIgnoreCase(SapccOfferInfo.ZONE_INTERNATIONAL, info.getSapccOfferInfo().getZone())) {
				this.roamingAmount += info.getChargeAmount();
			}
		}
	}

	public boolean isSapccUpdated() {
		return sapccUpdated;
	}

	public void setSapccUpdated(boolean sapccUpdated) {
		this.sapccUpdated = sapccUpdated;
	}

}