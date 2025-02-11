package com.telus.cmb.subscriber.bo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.HistorySearchException;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.InvalidServiceException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.ContractService;
import com.telus.api.account.InvoiceProperties;
import com.telus.api.account.ServiceChangeHistory;
import com.telus.api.account.Subscriber;
import com.telus.api.account.WPSFeatureException;
import com.telus.api.equipment.Card;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.equipment.SIMCardEquipment;
import com.telus.api.reference.AccountType;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.BillHoldRedirectDestination;
import com.telus.api.reference.Brand;
import com.telus.api.reference.Dealer;
import com.telus.api.reference.Feature;
import com.telus.api.reference.InvoiceSuppressionLevel;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceRelation;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.util.ClientApiUtils;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.subscriber.decorators.ContractDecorator;
import com.telus.cmb.subscriber.utilities.ActivatePortinContext;
import com.telus.cmb.subscriber.utilities.ActivationChangeContext;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.cmb.subscriber.utilities.BaseChangeContext;
import com.telus.cmb.subscriber.utilities.ContractChangeContext;
import com.telus.cmb.subscriber.utilities.MigrateSeatChangeContext;
import com.telus.cmb.subscriber.utilities.MigrationChangeContext;
import com.telus.cmb.subscriber.utilities.contract.ContractUtilities;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.eas.account.info.ServiceSubscriberCountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.Info;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.BaseChangeInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.eas.utility.info.ServiceExclusionGroupsInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServiceRelationInfo;

public class ContractBo extends ContractDecorator {

	private static final Log logger = LogFactory.getLog(ContractBo.class);
	protected BaseChangeContext<? extends BaseChangeInfo> changeContext;
	protected PricePlanBo pricePlan;
	protected boolean activation = false;
	protected boolean priceplanChange = false;
	protected boolean contractRenewal = false;
	protected boolean changeInvoiceFormat = false;
	protected boolean oldVistoPreserved = false;
	protected ArrayList<ServiceFeatureInfo> listConflictingFeatures = new ArrayList<ServiceFeatureInfo>();
	protected ServiceChangeHistory[] serviceChangeHistory = null; //don't want retrieve more then one time
	protected int term = Subscriber.TERM_PRESERVE_COMMITMENT;
	protected SubscriberBo subscriber;

	/**
	 * @param contract
	 * @param changeContext
	 * @param rawAndDefault 	If true, ignore all the activation, priceplanchange and renewal indicators. In other words, populate information based on the
	 * 							SubscriberContractInfo passed in and no additional logic would be performed on them.
	 * 
	 * @throws ApplicationException
	 */
	public <T extends BaseChangeInfo> ContractBo(SubscriberContractInfo contract, BaseChangeContext<T> changeContext, boolean rawAndDefault) throws ApplicationException {

		super(contract);
		this.changeContext = changeContext;
		this.subscriber = changeContext.getCurrentSubscriber();

		if (changeContext instanceof ContractChangeContext && !rawAndDefault) {
			ContractChangeContext context = (ContractChangeContext) changeContext;
			this.activation = context.isActivationInd();
			this.priceplanChange = context.isPricePlanChangeInd();
			this.contractRenewal = context.isRenewalInd();
			// CDR-CN addition: to capture the price plan change flag
			contract.setPricePlanChange(context.isPricePlanChangeInd());
			contract.setContractRenewal(context.isRenewalInd());
		} else if ((changeContext instanceof MigrationChangeContext || changeContext instanceof MigrateSeatChangeContext) && !rawAndDefault) {
			this.priceplanChange = contract.isPricePlanChange();
		} else if (changeContext instanceof ActivationChangeContext  || changeContext instanceof ActivatePortinContext || changeContext.getChangeInfo() instanceof ActivationChangeInfo) {
			this.activation = true;
			this.term = contract.getCommitmentMonths();
		}

		if (delegate.getPricePlan0() != null) {
			pricePlan = new PricePlanBo(delegate.getPricePlan0());
		}

		try {
			initialize();
		} catch (TelusAPIException tapie) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, tapie.getMessage(), "", tapie);
		}
	}

	public <T extends BaseChangeInfo> ContractBo(SubscriberContractInfo contract, BaseChangeContext<T> changeContext) throws ApplicationException {
		this(contract, changeContext, false);
	}

	private void setReferenceDataOnServiceAndFeatures() throws ApplicationException {

		ServiceAgreementInfo[] services = delegate.getServices0(true);
		for (int i = 0; i < services.length; i++) {
			ServiceAgreementInfo s = services[i];
			ServiceInfo service = null;

			if (s.isWPS() == true) {
				try {
					service = changeContext.getRefDataFacade().getWPSService(s.getServiceCode());
				} catch (TelusException e) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
				}
				if (service == null) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, "Couldn't find Prepaid service for code: [" + s.getServiceCode() + "]", "");
				}
				if (service.getFeatures() != null) {
					ServiceFeatureInfo[] wpsFeatures = s.getFeatures0(true);
					for (int j = 0; j < wpsFeatures.length; j++) {
						ServiceFeatureInfo wpsFtr = wpsFeatures[j];
						RatedFeatureInfo wpsRatedFtr;
						try {
							wpsRatedFtr = service.getFeature0(wpsFtr.getFeatureCode().trim());
						} catch (UnknownObjectException e) {
							String exceptionMsg = "Feature code =[" + wpsFtr.getFeatureCode().trim() + "]" + " is not compatible with service code =[" + wpsFtr.getServiceCode() + "]";
							throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, exceptionMsg, "", e);
						}
						wpsFtr.setFeature(wpsRatedFtr);
					}
				}
			} else {
				try {
					service = delegate.getPricePlan0().getService0(s.getServiceCode());
				} catch (UnknownObjectException e) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, e.getMessage(), "", e);
				}
				if (service == null) {
					// Add non-priceplan SOC.
					try {
						service = changeContext.getRefDataFacade().getRegularService(s.getServiceCode());
					} catch (TelusException e) {
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
					}
					if (service == null) {
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, "SOC [" + s.getServiceCode() + "] does not exist in the system.", "");
					}
				}
				ServiceFeatureInfo[] features = s.getFeatures0(true);
				for (int j = 0; j < features.length; j++) {
					ServiceFeatureInfo f = features[j];
					try {
						f.setFeature(service.getFeature0(f.getFeatureCode()));
					} catch (UnknownObjectException e) {
						String exceptionMsg = "Feature code =[" + f.getFeatureCode() + "]" + " is not compatible with service code =[" + f.getServiceCode() + "]";
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, exceptionMsg, "", e);
					}
				}
			}

			s.setService(service);
		}
		// Attach PricePlan features to Contract features.
		//------------------------------------------------------------------------------
		ServiceFeatureInfo[] features = delegate.getFeatures0(true);
		for (int j = 0; j < features.length; j++) {
			ServiceFeatureInfo f = features[j];
			try {
				f.setFeature(delegate.getPricePlan0().getFeature0(f.getFeatureCode()));
			} catch (UnknownObjectException e) {
				String exceptionMsg = "Feature code =[" + f.getFeatureCode() + "]" + " is not compatible with service code =[" + f.getServiceCode() + "]";
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, exceptionMsg, "", e);
			}
		}

		delegate.doPostLoadProcess();
	}

	public void initialize() throws SystemException, TelusAPIException, ApplicationException {

		if (contractRenewal && subscriber.getStatus() != Subscriber.STATUS_ACTIVE) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, "Cannot renew contract on an inactive subscriber", "");
		}

		setReferenceDataOnServiceAndFeatures();

		//--------------------------------------
		// Add the existing contract's optional
		// ContractServices in a deleted state.
		//--------------------------------------
		ContractBo oldContract = null;
		if (priceplanChange) {
			Date now = changeContext.getSystemDate();
			oldContract = (ContractBo) subscriber.getContract();
			ServiceAgreementInfo[] info = oldContract.getDelegate().getOptionalAndIncludedPromotionalServices(true);

			for (int i = 0; i < info.length; i++) {
				ServiceAgreementInfo s = info[i];
				Date expiryDate = s.getExpiryDate();
				if (!delegate.containsService0(s.getServiceCode(), false) && (expiryDate == null || expiryDate.after(now))) {
					s.setTransaction(BaseAgreementInfo.DELETE);
					delegate.addService(s);
				}
			}
		} else if (contractRenewal) {
			oldContract = (ContractBo) subscriber.getContract();
		}

		//---------------------------------------------
		// Add included services and features if
		// this is a new contract or a new priceplan.
		//---------------------------------------------
		if (activation || priceplanChange) {
			addIncludedServices();

			if (priceplanChange) {
				handleVistoServices(oldContract);
			}

			addIncludedFeatures();
		}

		if (activation || priceplanChange || contractRenewal) {
			removeNonMatchingServices(getValidationEquipment(), getAssociatedMule(), subscriber.getProvince());

			//New method for Holborn R1 project to remove non-matching SOCs (by network type) from the subscriber's contract
			ServiceInfo[] restrictedSOCsLost = removeNonMatchingRestrictedSOCs(getValidationEquipment().getNetworkType());
		}

		if (activation || priceplanChange) {
			// remove call detail feature on all postpaid PCS rateplans
			removeCallDetailFeatureOnPostpaidPCS();
		}

		// 2015 Apr release: commented out the following Mike-related code for web-based activations, as iDEN is not supported through web services.
		// R. Fong, 2015/01/19		
		//-----------------------------------------------------------
		// Add telephony blocking services if
		// this is a new dispatch only contract.
		// It's safe to assume this is IDEN.
		//-----------------------------------------------------------
		//		if ((activation || priceplanChange) && delegate.isDispatchOnly()) {
		//			addService0(ServiceSummary.BLOCK_INCOMING_CALLS_IDEN, null, null, false);
		//			addService0(ServiceSummary.BLOCK_OUTGOING_CALLS_IDEN, null, null, false);
		//		}

		// 2015 Apr release: commented out the following Mike-related code for web-based activations, as iDEN is not supported through web services.
		// R. Fong, 2015/01/19
		//------------------------------------------------------------
		// Mike data services for activation
		//------------------------------------------------------------
		//		if (activation) {
		//			ServiceInfo[] services = pricePlan.getOptionalServices(changeContext.getCurrentEquipment());
		//			for (int i = 0; i < services.length; i++) {
		//				if (services[i].containsPrivilege(BusinessRole.BUSINESS_ROLE_ALL, ServiceSummary.PRIVILEGE_AUTOADD)) {
		//					if (services[i].isMOSMS() || services[i].isMMS()) // || services[i].isJavaDownload())
		//						addService0(services[i].getCode(), null, null, false);
		//				}
		//			}
		//		}

		//-----------------------------------------------------------
		// 1) Add secondary service for non-primary, shareable
		//    priceplan subscribers.
		//
		// 2) Add shearable services that are on other subscribers
		//    with the same priceplan.
		//-----------------------------------------------------------
		if (activation || priceplanChange) {
			// Do not execute shareable priceplan subscriber count query on corporate accounts for performance reasons. 
			if (pricePlan.isSharable() && !changeContext.getCurrentAccount().getDelegate().isCorporate()) {
				PricePlanSubscriberCountInfo count = ((AccountBo) changeContext.getCurrentAccount()).getShareablePricePlanSubscriberCount(pricePlan.getCode());

				//-----------------------------------------------------------
				// Add secondary service
				//-----------------------------------------------------------
				if (count != null && ((count.getActiveSubscribers().length + count.getReservedSubscribers().length + count.getFutureDatedSubscribers().length) > 0)) {
					if (pricePlan.getSecondarySubscriberService() != null) {
						addService0(pricePlan.getSecondarySubscriberService(), null, null, false);
					}
				}

				//-----------------------------------------------------------
				// Add shearable services that are on other subscribers.
				//-----------------------------------------------------------
				addShareableServicesOnOtherSubscribers(count, false);
			}
		}

		if (activation || priceplanChange || contractRenewal) {
			removeDispatchOnlyConflicts();
		}

		//-----------------------------------------------------------
		// Setup commitment for new contracts, new priceplan, or
		// contractRenewals.
		//-----------------------------------------------------------
		if (activation || priceplanChange || contractRenewal) {
			if (term == Subscriber.TERM_PRESERVE_COMMITMENT && oldContract.getDelegate().getCommitment().isValid()) {
				setCommitmentStartDate(oldContract.getCommitmentStartDate());
				setCommitmentMonths(oldContract.getCommitmentMonths());
				setCommitmentEndDate(oldContract.getCommitmentEndDate());
			} else if (term == Subscriber.TERM_MONTH_TO_MONTH) {
				setCommitmentStartDate(null);
				setCommitmentMonths(0);
				setCommitmentEndDate(null);
				setCommitmentReasonCode(null);
			} else if (term != Subscriber.TERM_PRESERVE_COMMITMENT && !delegate.getCommitment().isModified()) {
				// Added the Commitment.isModified check to ensure we do not overwrite pre-populated commitment info mapped by the web services activation flow
				setCommitmentStartDate(changeContext.getSystemDate());
				setCommitmentMonths(term);
			}

			if (!activation && oldContract.getDelegate().getCommitment().isValid()) {
				setCommitmentReasonCode(oldContract.getCommitmentReasonCode());
			}

			if (term == Subscriber.TERM_PRESERVE_COMMITMENT) {
				// Removed the activation check here, as it is not valid for the web services flow
				delegate.getCommitment().setModified(false);
			}
		}
	}

	private void addShareableServicesOnOtherSubscribers(PricePlanSubscriberCountInfo pricePlanSubscriberCount, boolean saveContract) throws TelusAPIException, ApplicationException {
		if (pricePlanSubscriberCount == null) {
			return;
		}

		//-----------------------------------------------------------
		// Add shearable services that are on other subscribers.
		//-----------------------------------------------------------
		ServiceSubscriberCountInfo[] serviceSubscriberCount = (ServiceSubscriberCountInfo[]) pricePlanSubscriberCount.getServiceSubscriberCounts();
		for (int i = 0; i < serviceSubscriberCount.length; i++) {
			String code = serviceSubscriberCount[i].getServiceCode();
			if (!containsService(code) && canModifyShareableService(code)) {
				addService(code);
			}
		}

		if (saveContract && isModified()) {
			save();
		}
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.decorators.ContractDecorator#getPricePlan()
	 */
	@Override
	public PricePlanBo getPricePlan() {
		return pricePlan;
	}

	public boolean canModifyShareableService(String code) {
		try {
			ServiceInfo service = pricePlan.getDelegate().getService1(code);

			if (service == null) {
				throw new UnknownObjectException("couldn't find shearable service (" + code + ") on pricaplan (" + pricePlan.getCode() + ")", code);
			}

			return !service.isPromotion() && !service.hasPromotion() && !service.isIncludedPromotion() && !pricePlan.containsIncludedService(code);
		} catch (UnknownObjectException ex) {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	private void addBoundServices(ServiceInfo service, Date effDate, Date expDate) throws TelusAPIException, ApplicationException {
		logger.debug("---->     addBoundServices(" + service.getCode() + ")");

		if (service.hasBoundService() || service.hasSequentiallyBoundService()) {
			ServiceRelationInfo[] relations = getRelations(service.getCode(), getPricePlan());
			Calendar calToday = Calendar.getInstance();
			Date today = new Date(calToday.get(Calendar.YEAR) - 1900, calToday.get(Calendar.MONTH), calToday.get(Calendar.DAY_OF_MONTH));
			int compareDates = 0;
			boolean valid = false;

			for (int i = 0; i < relations.length; i++) {
				ServiceInfo s = changeContext.getRefDataFacade().getRegularService(relations[i].getServiceCode());
				if (expDate != null) {
					compareDates = today.compareTo(expDate);
				}
				//-------------------------------------------------------------
				// We only handle bound and sequentially-bound services
				// since AMDOCS handles the promotions.
				//-------------------------------------------------------------
				if (s.isBoundService()) {
					if (expDate == null || compareDates < 0) {
						valid = ContractUtilities.testBoundServiceAddition(service, s, changeContext.getLogicalDate(), getValidationEquipment().getDelegate());
					}
					if (valid) {
						String serviceType = s.getServiceType();
						if (!activation && (serviceType.equals("G") || serviceType.equals("T"))) {
							boolean skipLogic = subscriber.getContract().containsService(s.getCode());
							if (!skipLogic) {
								if (contractRenewal) {
									effDate = today;
								} else {
									// TODO: condition below was added for Roaming Passes CR (Sept 2010) and is
									// intended as a temporary fix. The condition plus the call to
									// isServiceExistInChangeHistory should be refactored into a permanent solution.
									String[] serviceFamilyGroupCodes = changeContext.getRefDataHelper().retrieveServiceFamilyGroupCodes(s.getCode(),
											ContractUtilities.ROAMING_PASSES_BOUND_SOC_FAMILY_TYPE);
									if (serviceFamilyGroupCodes != null && serviceFamilyGroupCodes.length == 0) {
										valid = !isServiceExistInChangeHistory(s.getCode());
									}
									if (valid && effDate != null && changeContext.getLogicalDate().after(effDate)) {
										effDate = today;
									}
								}
							}
						}
						if (valid) {
							addService0(s, effDate, expDate, true);
						}
					}
				} else if (s.isSequentiallyBoundService()) {
					if (expDate == null || compareDates < 0) {
						valid = ContractUtilities.testBoundServiceAddition(service, s, changeContext.getLogicalDate(), getValidationEquipment().getDelegate());
						if (valid) {

							// This will only happen once
							// -------------------------------------------------------------------------------
							// Calculate the effectiveDate and expiryDate
							// for the first service in
							// a sequentially-bound service pair. The second
							// service starts on the same
							// day the first expires.
							// -------------------------------------------------------------------------------
							ServiceAgreementInfo info = delegate.getService0(service.getCode(), false);
							Date effectiveDate = (info.getEffectiveDate() != null) ? info.getEffectiveDate() : changeContext.getSystemDate();
							Calendar expiryDate = Calendar.getInstance();
							expiryDate.setTimeInMillis(effectiveDate.getTime());
							expiryDate.add(Calendar.MONTH, service.getTermMonths());
							// -------------------------------------------------------------------------------
							// Setup the effectiveDate and expiryDates for
							// the service pair.
							// -------------------------------------------------------------------------------
							// info.setEffectiveDate(effectiveDate); --TODO:
							// this may cause issues for
							// futureDatedActivations
							info.setExpiryDate(expiryDate.getTime());
							addService0(s, expiryDate.getTime(), null, true);
						}
					}
				}
			}
		}
	}

	private ServiceRelationInfo[] getRelations(String serviceCode, PricePlanBo pricePlan) throws ApplicationException, TelusException {
		ServiceRelationInfo[] relations = changeContext.getRefDataHelper().retrieveRelations(serviceCode);
		return ContractUtilities.filterRelationsByContract(relations, pricePlan.getDelegate());
	}

	private ServiceRelationInfo[] getServiceRelations(String serviceCode, String relationType) throws TelusException, ApplicationException {
		ServiceRelationInfo[] info = changeContext.getRefDataHelper().retrieveRelations(serviceCode);

		List<ServiceRelationInfo> list = new ArrayList<ServiceRelationInfo>(info.length);
		for (int i = 0; i < info.length; i++) {
			ServiceRelationInfo r = info[i];
			if (relationType.equals(r.getType())) {
				list.add(r);
			}
		}

		info = list.toArray(new ServiceRelationInfo[list.size()]);

		return info;
	}

	private boolean isServiceExistInChangeHistory(String code) throws ApplicationException, HistorySearchException, SystemException, TelusAPIException {
		if (serviceChangeHistory == null) {
			Calendar calToday = Calendar.getInstance();
			Date to = calToday.getTime();
			Date from = subscriber.getContract().getCommitmentStartDate() != null ? subscriber.getContract().getCommitmentStartDate() : subscriber.getStartServiceDate();
			serviceChangeHistory = subscriber.getServiceChangeHistory(from, to);
		}
		for (int j = 0; j < serviceChangeHistory.length; j++) {
			if (serviceChangeHistory[j].getServiceCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

	private void removeCallDetailFeatureOnPostpaidPCS() throws TelusAPIException, ApplicationException {

		logger.debug(">>>> removeCallDetailFeatureOnPostpaidPCS() ...");

		if (!delegate.containsFeature0(ContractUtilities.CALL_DETAIL_FEATURE, null, null, false, false)) {
			logger.debug(">>>> CallDetailFeature not found on PricePlan, not removed ...");
			return;
		}

		Date currentTime = new Date();
		Date expiryTime = AppConfiguration.getCallDetailFeatureExpiryDate();

		boolean remove = false;

		if (currentTime.after(expiryTime)) {
			remove = true;
		}

		if (!remove) {
			return;
		}

		char accountType = changeContext.getCurrentAccount().getAccountType();
		boolean corporate = 'C' == accountType ? true : false;
		boolean pcs = subscriber.isPCS();
		boolean postpaid = changeContext.getCurrentAccount().isPostpaid();

		if (!corporate && pcs && postpaid) {

			//---------------------------------------------
			// Remove price plan feature: detail billing on all postpaid PCS excluding Corporate.
			// "CD" is a price plan feature, not a feature of a service (SOC).
			//---------------------------------------------
			removeFeature(ContractUtilities.CALL_DETAIL_FEATURE);
			logger.debug(">>>> callDetailFeature [" + ContractUtilities.CALL_DETAIL_FEATURE + "] expired (removed from Contract)");
		}
	}

	private void addIncludedServices() throws InvalidServiceChangeException, TelusAPIException, ApplicationException {
		ServiceInfo[] includedServices = pricePlan.getDelegate().getIncludedServices0();
		for (int i = 0; i < includedServices.length; i++) {
			logger.debug("---->  ContractBo.addIncludedServices(" + includedServices[i].getCode() + ")");
			ServiceAgreementInfo info = addService0(includedServices[i], null, null, false);

			//-------------------------------------------------
			// Since IncludedPromotions are really optional
			// SOCs to AMDOCS, they should be treated as such
			// by the provider (left in the ADD state).
			//-------------------------------------------------
			if (!info.getService0().isIncludedPromotion()) {
				info.setTransaction(ServiceAgreementInfo.NO_CHG, true, false);
			}

			addBoundServices(pricePlan.getDelegate().getIncludedService0(includedServices[i].getCode()), null, null);
		}
	}

	private void addIncludedFeatures() throws InvalidServiceChangeException {
		RatedFeatureInfo[] includedFeatures = delegate.getPricePlan0().getFeatures0();
		for (int i = 0; i < includedFeatures.length; i++) {
			logger.debug("----> ContractBo.addIncludedFeatures(" + includedFeatures[i].getCode() + ")");
			ServiceFeatureInfo info = addFeature0(includedFeatures[i]);
			info.setTransaction(ServiceFeatureInfo.NO_CHG);
		}
	}

	private ServiceFeatureInfo addFeature0(RatedFeatureInfo feature) throws InvalidServiceChangeException {
		if (!feature.isDuplFeatureAllowed() && delegate.containsFeature0(feature.getCode(), null, null, true, false)) {
			throw new InvalidServiceChangeException(InvalidServiceChangeException.DUPLICATE_FEATURE, "", null, feature.getCode());
		}

		logger.debug("            +\"" + feature.getCode() + "\"  (feature)");
		return delegate.addFeature(feature);
	}

	private void handleVistoServices(ContractBo oldContract) throws TelusAPIException, ApplicationException {
		ServiceInfo newVisto = ContractUtilities.hasVisto(false, getDelegate());
		ServiceInfo oldVisto = ContractUtilities.hasVisto(true, oldContract.getDelegate());
		if (newVisto != null && oldVisto != null && !oldVisto.getCode().equals(newVisto.getCode()) && oldVisto.isNetworkEquipmentTypeCompatible(getValidationEquipment().getDelegate())) {
			removeService(newVisto.getCode());
			logger.debug("removed new Visto SOC " + newVisto);
			addService0(oldVisto, null, null, true);
			logger.debug("preserved old Visto SOC " + oldVisto);
			oldVistoPreserved = true;
		}
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.decorators.ContractDecorator#addService(com.telus.api.reference.Service)
	 */
	@Override
	public ServiceAgreementInfo addService(ServiceInfo service) throws InvalidServiceChangeException, TelusAPIException {
		try {
			return addService(service, false);
		} catch (ApplicationException e) {
			throw new TelusAPIException(e);
		}
	}

	private ServiceAgreementInfo addService(ServiceInfo service, boolean preserveDigitalServices) throws InvalidServiceChangeException, TelusAPIException, ApplicationException {
		logger.debug("----> addService(" + service.getCode() + ")");
		logger.debug("----> preserveDigitalServices = " + preserveDigitalServices);
		return addService0((ServiceInfo) service, null, null, false, preserveDigitalServices);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.decorators.ContractDecorator#addService(java.lang.String)
	 */
	@Override
	public ServiceAgreementInfo addService(String serviceCode) throws InvalidServiceChangeException, ApplicationException, TelusAPIException {
		return addService(serviceCode, false);
	}

	public ServiceAgreementInfo addService(String serviceCode, boolean preserveDigitalServices) throws ApplicationException, InvalidServiceChangeException, TelusAPIException {
		serviceCode = Info.padService(serviceCode);
		ServiceInfo service;
		try {
			service = pricePlan.getDelegate().getService1(serviceCode);
		} catch (UnknownObjectException e) {
			service = changeContext.getRefDataFacade().getRegularService(serviceCode);
		}

		if (service == null && changeContext.getCurrentAccount().isPrepaidConsumer()) {
			service = changeContext.getRefDataFacade().getWPSService(serviceCode);
		}

		return addService(service, preserveDigitalServices);
	}

	public ServiceAgreementInfo addService(String serviceCode, Date effectiveDate, Date expiryDate) throws ApplicationException, InvalidServiceChangeException, TelusAPIException {
		serviceCode = Info.padService(serviceCode);
		ServiceInfo service;
		try {
			service = pricePlan.getDelegate().getService1(serviceCode);
		} catch (UnknownObjectException e) {
			service = changeContext.getRefDataFacade().getRegularService(serviceCode);
		}

		if (service == null && changeContext.getCurrentAccount().isPrepaidConsumer()) {
			service = changeContext.getRefDataFacade().getWPSService(serviceCode);
		}

		if (service == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, "Unknown service[" + serviceCode + "]", "");
		}
		return addService(service, effectiveDate, expiryDate);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telus.cmb.subscriber.decorators.ContractDecorator#addService(com.telus.api.reference.Service, java.util.Date, java.util.Date)
	 */
	@Override
	public ServiceAgreementInfo addService(ServiceInfo service, Date effectiveDate, Date expiryDate) throws InvalidServiceChangeException, TelusAPIException, ApplicationException {
		logger.debug("----> addService(" + service.getCode() + ")");
		if (effectiveDate != null) {
			logger.debug("----> effectiveDate = " + effectiveDate.toString());
		}
		if (expiryDate != null) {
			logger.debug("----> expiryDate = " + expiryDate.toString());
		}
		return addService0((ServiceInfo) service, effectiveDate, expiryDate, false, false);
	}



	/**
	 * These change are made to handle included Features conflicting with optional Services. In order to handle conflicts we're expiring the optional
	 * service feature that has been added. Included Features cannot be future dated or expired.
	 */
	private ServiceAgreementInfo addService0(ServiceInfo service, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional) throws InvalidServiceChangeException, TelusAPIException,
			ApplicationException {
		ServiceAgreementInfo info = addService00(service, effectiveDate, expiryDate, allowBoundAndPromotional);
		for (int i = 0; i < listConflictingFeatures.size(); i++) {
			ServiceFeatureInfo contractFeature = listConflictingFeatures.get(i);
			info.removeFeature(contractFeature.getCode());
			addFeature0(contractFeature.getFeature0());
			logger.debug(info);
		}
		listConflictingFeatures.clear();
		return info;
	}

	private ServiceAgreementInfo addService0(String serviceCode, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional) throws InvalidServiceChangeException, TelusAPIException,
			ApplicationException {
		serviceCode = Info.padService(serviceCode);

		ServiceInfo service;
		try {
			service = pricePlan.getDelegate().getService0(serviceCode);
		} catch (UnknownObjectException e) {
			service = changeContext.getRefDataFacade().getRegularService(serviceCode);
		}

		return addService0(service, effectiveDate, expiryDate, allowBoundAndPromotional);
	}

	private ServiceAgreementInfo addService0(ServiceInfo service, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional, boolean preserveDigitalServices)
			throws InvalidServiceChangeException, TelusAPIException, ApplicationException {
		ServiceAgreementInfo info = addService0(service, effectiveDate, expiryDate, allowBoundAndPromotional);

		if (effectiveDate == null) {
			effectiveDate = info.getEffectiveDate();
		}

		ServiceFeatureInfo[] features = info.getFeatures0(true);
		for (int j = 0; j < features.length; j++) {
			ServiceFeatureInfo f = features[j];
			f.setFeature(info.getService0().getFeature0(f.getFeatureCode()));
		}

		addBoundServices(service, effectiveDate, expiryDate);

		if (!preserveDigitalServices) {
			String applicationCode = null;
			if (changeContext.getClientIdentity() != null) {
				applicationCode = changeContext.getClientIdentity().getApplication();
			}
			ApplicationSummary applicationSummary = null;
			if (applicationCode != null) {
				applicationSummary = changeContext.getRefDataFacade().getApplicationSummary(applicationCode);
			}
			if ((applicationSummary != null && !applicationSummary.isBatch()) || applicationSummary == null) {
				removeDispatchOnlyConflicts();
			}
			removeOutboundCallerIdDisplay(service);
		}

		removeCombinedVoiceMailConflicts(info);

		checkInvoiceFormatChange(service, info.getTransaction() == BaseAgreementInfo.ADD);

		return info;
	}






	/**
	 * Provides provisioning lag time in minutes which is added to a service duration to
	 * accomodate service provisioning delay related to network and other bottlenecks.
	 * The value is expected to be configurable through LDAP or other configuration source.
	 * @return lag time (minutes)
	 */
	private int getProvisioningLagTime() {
		// TODO: get the value from configuration source
		return 5;
	}

	// Remove conflicting services or features if the added service is Combined Voice Mail.
	private void removeCombinedVoiceMailConflicts(ServiceAgreementInfo service) throws TelusAPIException, ApplicationException {
		if (isCombinedVoiceMail(service.getService0())) {
			// Note: check for voice mail AFTER all other conflicts have been removed, since it may already be removed through a
			// conflicting feature or service.
			removeCombinedVoiceMailConflicts0(service);
			removeVoiceMail();
		}
	}

	private boolean isCombinedVoiceMail(ServiceInfo service) throws TelusAPIException {
		// Check if the service contains Combined Voice Mail (switch code starts with "CVM").
		RatedFeatureInfo serviceFeatures[] = service.getFeatures0();
		for (int i = 0; i < serviceFeatures.length; i++) {
			if (serviceFeatures[i].getSwitchCode().startsWith(ContractUtilities.COMBINED_VOICE_MAIL_PREFIX)) {
				return true;
			}
		}
		return false;
	}

	// Remove conflicting services or features if the added service is Combined Voice Mail.
	private void removeCombinedVoiceMailConflicts0(ServiceAgreementInfo service) throws TelusAPIException, ApplicationException {

		logger.debug("---->     removeCombinedVoiceMailConflicts0(" + service.getCode() + ")");

		// Get contract services.
		ServiceAgreementInfo[] contractServices = delegate.getIncludedServices0(false);

		// Get features from the Combined Voice Mail service.
		RatedFeatureInfo serviceFeatures[] = service.getService0().getFeatures0();

		for (int i = 0; i < serviceFeatures.length; i++) {

			// Check the subscriber contract features to see if the feature already exists.
			// If there is a conflict with any Combined Voice Mail feature, remove the conflicting feature.
			if (delegate.containsFeature0(serviceFeatures[i].getCode(), null, null, false, false)) {
				if (service.getEffectiveDate() != null && changeContext.getLogicalDate().before(service.getEffectiveDate())) {
					// ContractFeature contractFeature = findContractFeature(serviceFeatures[i]
					// .getCode());
					removeFeature(serviceFeatures[i].getCode()); // need to fix when we could expire included feature
					logger.debug(">>>> Removed conflicting feature from contract: [" + serviceFeatures[i].getCode() + "] on [" + service.getEffectiveDate() + "]");
				} else {
					removeFeature(serviceFeatures[i].getCode());
					logger.debug(">>>> Removed conflicting feature from contract: [" + serviceFeatures[i].getCode() + "]");
				}

			}

			// Loop through all contract included services.
			for (int j = 0; j < contractServices.length; j++) {
				// Check the subscriber contract included services to see if the feature already exists.
				// If there is a conflict with any Combined Voice Mail feature, remove the conflicting service.
				if (contractServices[j].getService().containsFeature(serviceFeatures[i].getCode())) {
					if (service.getEffectiveDate() != null && changeContext.getLogicalDate().before(service.getEffectiveDate())) {
						contractServices[j].setExpiryDate(service.getEffectiveDate());
						logger.debug(">>>> Removed conflicting service from contract: [" + contractServices[j].getCode() + "] on " + service.getEffectiveDate() + "]");

					} else {
						removeService0(contractServices[j].getCode(), false);
						logger.debug(">>>> Removed conflicting service from contract: [" + contractServices[j].getCode() + "]");
					}
					break;
				}
			}
		}
	}

	private ServiceAgreementInfo addService00(ServiceInfo service, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional) throws InvalidServiceChangeException, TelusAPIException,
			ApplicationException {
		try {
			if (priceplanChange && service.isVisto()) {
				ContractBo oldContract = subscriber.getContract();
				ServiceInfo oldVisto = ContractUtilities.hasVisto(true, oldContract.getDelegate());
				if (oldVisto != null && //old visto exists
						!service.getCode().equals(oldVisto.getCode()) && //trying to add new visto soc
						!pricePlan.containsIncludedService(service.getCode()) && //the adding soc is not an included soc. skip because this is handled in TMContract constructor
						oldVisto.isNetworkEquipmentTypeCompatible(getValidationEquipment().getDelegate())) { //old visto is compatible with current equipment's network
					//if it is a PPC and a Visto SOC is to be added as optional services, check if there is legacy Visto SOC first.
					//if there is and is compatible with network, add the old one instead of the new one. (Tsz Chung Tong (tongts), Jul, 2007)
					ServiceInfo currentVistoService = ContractUtilities.hasVisto(false, getDelegate());
					if (currentVistoService != null) { //remove any previously added Visto service before adding the legacy one
						removeService(currentVistoService.getCode());
						logger.debug("removed Visto SOC " + currentVistoService.getCode());
					}
					oldVistoPreserved = true;
					logger.debug("preserved Visto SOC " + oldVisto);
					return addServiceImpl(oldVisto, effectiveDate, expiryDate); //July 2007 Visto project:add the old Visto (if exists) instead of new one and do not add the new one
				}
			}
			testAddition0(service, effectiveDate, expiryDate, allowBoundAndPromotional, false);
			return addServiceImpl(service, effectiveDate, expiryDate);
		} catch (InvalidServiceChangeException e) {
			//-----------------------------------------------------------------
			// If adding an optional service causes a FEATURE_CONFLICT with
			// an included service, remove the included service.
			//-----------------------------------------------------------------
			if (e.getReason() == InvalidServiceChangeException.FEATURE_CONFLICT) {
				if (e.getContractService() != null) { // Service-level conflict
					String code = e.getContractService().getService().getCode();
					//	  add by sutha to fix optional service getting removed
					//assumption: if a service is NOT included in a price plan, then we treat it as optional service: M.Liao
					if (pricePlan.containsIncludedService(code) == false) {
						throw e;
					}
					try {
						if (effectiveDate != null && changeContext.getLogicalDate().before(effectiveDate)) {
							e.getContractService().setExpiryDate(effectiveDate);
						} else {
							removeService(code);
						}
					} catch (TelusAPIException rm) {
						throw e;
					}
					return addService00(service, effectiveDate, expiryDate, allowBoundAndPromotional);
				} else { // Feature-level conflict
					String code = e.getFeatureCode();
					if (effectiveDate != null && changeContext.getLogicalDate().before(effectiveDate)) {
						ServiceFeatureInfo contractFeature = ContractUtilities.findContractFeature(code, getDelegate().getFeatures0(false));
						removeFeature(code);
						listConflictingFeatures.add(contractFeature);
					} else {
						removeFeature(code);
					}
					return addService00(service, effectiveDate, expiryDate, allowBoundAndPromotional);
				}
			}
			throw e;
		}
	}

	private ServiceAgreementInfo addServiceImpl(ServiceInfo service, Date effectiveDate, Date expiryDate) throws TelusAPIException, ApplicationException {
		logger.debug("            +\"" + service.getCode() + "\";    effective " + effectiveDate + " to " + expiryDate);

		ServiceAgreementInfo cs;
		if (service.isWPS()) {
			cs = addPrepaidServiceImpl(service, effectiveDate, null);
			logger.debug("              (+\"" + service.getCode() + "\";    effective " + cs.getEffectiveDate() + " to " + cs.getExpiryDate() + ";  Prepaid)");
		} else {
			cs = delegate.addService(service, effectiveDate, true);
			if (cs.getTransaction() != ServiceAgreementInfo.NO_CHG) {
				if (!service.isIncludedPromotion() || effectiveDate != null || expiryDate != null) {
					cs.setEffectiveDate(effectiveDate);
					cs.setExpiryDate(expiryDate);
				}
			}
		}

		return cs;
	}

	private ServiceAgreementInfo addPrepaidServiceImpl(ServiceInfo service, Date effectiveDate, Date expiryDate) throws TelusAPIException, ApplicationException {
		logger.debug("addPrepaidService(" + service.getCode() + ", " + effectiveDate + ", " + expiryDate + ")");
		// expiryDate is ignored for non-LBM features

		if (!service.isWPS()) {
			throw new TelusAPIException("The service is not Prepaid service: [" + service.getCode() + "]");
		}

		Date today = changeContext.getSystemDate();
		long lastExpiryDate = today.getTime() + ((service.getMaxConsActDays() - 1) * ContractUtilities.DAY);
		long newExpiryDate;

		ServiceAgreementInfo serviceAgreement;

		if (delegate.containsService0(service.getCode(), false)) { // Duplicate service
			serviceAgreement = delegate.getService0(service.getCode(), false);

			//--------------------------------------------------------------
			// Bug Fix:  Since wps/prepaid services can have different
			//           duration based on how they are retrieved, we
			//           always need to update the serviceAgreement's
			//           meta information.
			//--------------------------------------------------------------
			serviceAgreement.setService(service);
			serviceAgreement.setTransaction(BaseAgreementInfo.ADD); // Always set prepaid transaction as ADD

			newExpiryDate = serviceAgreement.getExpiryDate().getTime() + (service.getTerm() * ContractUtilities.DAY); // Assume termUnits are in days
		} else { // New service
			serviceAgreement = delegate.addService(service, effectiveDate, true);
			newExpiryDate = today.getTime() + (service.getTerm() * ContractUtilities.DAY); // Assume termUnits are in days
		}

		if (service.isPrepaidLBM()) {
			serviceAgreement.setEffectiveDate(effectiveDate);
			serviceAgreement.setExpiryDate(expiryDate);
		} else {
			newExpiryDate = Math.min(lastExpiryDate, newExpiryDate);
			serviceAgreement.setExpiryDate(new Date(newExpiryDate));
		}

		return serviceAgreement;
	}

	@Override
	public ServiceInfo[] testAddition(ServiceInfo[] service) throws InvalidServiceChangeException, TelusAPIException, ApplicationException {
		for (int i = 0; i < service.length; i++) {
			testAddition0(service[i], null, null, false, true);
		}
		return service;
	}

	private ServiceInfo testAddition0(ServiceInfo service, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional, boolean allowIncludedOptionalConflict)
			throws InvalidServiceChangeException, TelusAPIException, ApplicationException {

		boolean isCombinedVoiceMail = ContractUtilities.isCombinedVoiceMail(service);

		try {
			subscriber.testService(service, getValidationEquipment());
		} catch (InvalidServiceException e) {
			throw new InvalidServiceChangeException(e.getReason(), e);
		}

		if (!allowBoundAndPromotional) {
			if (service.isPromotion()) {
				throw new InvalidServiceChangeException(InvalidServiceChangeException.PROMOTIONAL_SERVICE, "service can only be added by system: " + service.getCode());
			}

			if (service.isBoundService()) {
				throw new InvalidServiceChangeException(InvalidServiceChangeException.BOUND_SERVICE, "service can only be added by system: " + service.getCode());
			}

			if (service.isSequentiallyBoundService()) {
				throw new InvalidServiceChangeException(InvalidServiceChangeException.BOUND_SERVICE, "service can only be added by system: " + service.getCode());
			}

			/*
			 * if (!service.isAvailable()) { provider.debug(service); throw new
			 * InvalidServiceChangeException(InvalidServiceChangeException.UNAVAILABLE_SERVICE, service.getCode()); }
			 */
		}

		String serviceMappingCode = ClientApiUtils.getContractServiceMappingKey(service.getCode(), effectiveDate);
		if (delegate.containsService0(serviceMappingCode, false) && !allowDuplicateService(service)) {
			ContractService oldService = delegate.getService(serviceMappingCode);
			if (ContractUtilities.isPeriodOverlapping(effectiveDate, expiryDate, oldService.getEffectiveDate(), oldService.getExpiryDate())) {
				throw new InvalidServiceChangeException(InvalidServiceChangeException.DUPLICATE_SERVICE, service.getCode(), getService(serviceMappingCode), null);
			}
		}

		// SOC Compatibility, June 1, 2005, by MQ
		validateServiceCompatibility(service, effectiveDate, expiryDate);

		RatedFeature[] feature = service.getFeatures();
		for (int i = 0; i < feature.length; i++) {
			RatedFeature r = feature[i];

			if (!r.isDuplFeatureAllowed() && delegate.containsFeature0(r.getCode(), effectiveDate, expiryDate, true, false)) {
				ServiceAgreementInfo conflictingService = delegate.getServiceByFeature0(r.getCode(), effectiveDate, expiryDate, false);

				if (!allowIncludedOptionalConflict) {
					// Handle conflicts with Combined Voice Mail services and features.
					if (isCombinedVoiceMail) {
						// Allow the testAddition0 to proceed - break out of the iteration and continue with the for loop.
						continue;
					} else {
						// Throw the InvalidServiceChangeException only if the service is NOT Combined Voice Mail.
						throw new InvalidServiceChangeException(InvalidServiceChangeException.FEATURE_CONFLICT, r.getCode(), conflictingService, r.getCode());
					}
				}
			}
		}

		return service;
	}

	protected EquipmentBo getValidationEquipment() throws ApplicationException {
		if (changeContext instanceof ContractChangeContext) {
			EquipmentBo equipment = ((ContractChangeContext) changeContext).getNewEquipment();
			if (equipment != null) {
				return equipment;
			}
		}
		return subscriber.getEquipment();
	}

	@Override
	public EquipmentChangeRequestInfo getEquipmentChangeRequest() {
		if (changeContext instanceof ContractChangeContext) {
			return ((ContractChangeContext) changeContext).getEquipmentChangeRequest();
		}

		return null;
	}

	private void validateServiceCompatibility(ServiceInfo service, Date effectiveDate, Date expiryDate) throws InvalidServiceChangeException, TelusAPIException, ApplicationException {

		ServiceAgreementInfo[] services = getDelegate().getOptionalServices0(false);

		// DO not check if already on contract - to handle prepaid
		if (containsService(service.getCode())) {
			return;
		}

		String code = null;
		ServiceExclusionGroupsInfo tempSEGInfo = null;
		HashMap<String, ServiceExclusionGroupsInfo> existingSEGInfos = new HashMap<String, ServiceExclusionGroupsInfo>();

		for (int i = 0; i < services.length; i++) {
			if (!ContractUtilities.isPeriodOverlapping(effectiveDate, expiryDate, services[i].getEffectiveDate(), services[i].getExpiryDate())) {
				continue;
			}
			code = services[i].getCode();
			tempSEGInfo = changeContext.getRefDataFacade().getServiceExclusionGroups(code);
			if (tempSEGInfo != null) {
				existingSEGInfos.put(code, tempSEGInfo);
			}
		}

		ServiceExclusionGroupsInfo newSEGInfo = changeContext.getRefDataFacade().getServiceExclusionGroups(service.getCode());
		String[] newExclusionGroups = null;
		String[] oldExclusionGroups = null;

		if (newSEGInfo != null && existingSEGInfos.size() > 0) {

			newExclusionGroups = newSEGInfo.getExclusionGroups();

			Set<String> keys = existingSEGInfos.keySet();
			Iterator<String> keyIterator = keys.iterator();
			Collection<ServiceExclusionGroupsInfo> values = existingSEGInfos.values();
			Iterator<ServiceExclusionGroupsInfo> valueIterator = values.iterator();

			while (valueIterator.hasNext() && keyIterator.hasNext()) {
				oldExclusionGroups = (valueIterator.next()).getExclusionGroups();
				code = keyIterator.next();

				for (int k = 0; k < oldExclusionGroups.length; k++) {
					for (int j = 0; j < newExclusionGroups.length; j++) {
						if (oldExclusionGroups[k].equals(newExclusionGroups[j])) {
							// int reason, String message, ContractService contractService, String featureCode
							throw new InvalidServiceChangeException(InvalidServiceChangeException.SERVICE_CONFLICT, "service conflict: [" + code + "] ", getService(code), null);
						}
					}
				}
			}
		}
	}

	private boolean allowDuplicateService(ServiceInfo service) throws UnknownObjectException, ApplicationException {
		if (service.isWPS()) {
			ServiceAgreementInfo contractService = (ServiceAgreementInfo) getService(service.getCode());
			Date today = changeContext.getSystemDate();
			Date expiryDate = contractService.getExpiryDate();

			if (expiryDate == null) {
				return false;
			}

			long daysUntilExpiry = (expiryDate.getTime() - today.getTime() + ContractUtilities.DAY - 1) / ContractUtilities.DAY;

			return daysUntilExpiry < (service.getMaxConsActDays() - 1);
		} else {
			return false;
		}
	}

	private MuleEquipment getAssociatedMule() throws TelusAPIException, SystemException, ApplicationException {
		MuleEquipment mule = null;
		if (getEquipmentChangeRequest() != null) {
			mule = getEquipmentChangeRequest().getAssociatedMuleEquipment();
			if (mule != null) {
				return mule;
			}

			Equipment newEquipment = getEquipmentChangeRequest().getNewEquipment();
			if (newEquipment != null && newEquipment.isSIMCard()) {
				mule = ((SIMCardEquipment) newEquipment).getLastMule();
				if (mule != null) {
					return mule;
				}
			}
		}

		if (subscriber.getEquipment().isSIMCard()) {
			mule = subscriber.getEquipment().getLastMule();
			if (mule != null) {
				return mule;
			}
		}

		return null;
	}

	/*
	 * New method introduced for Holborn R1 project
	 */
	private ServiceInfo[] removeNonMatchingRestrictedSOCs(String networkType) throws TelusAPIException, SystemException, ApplicationException {

		//If subscriber's equipment is compatible with all network types, then all services are matching (return immediately)
		if (networkType.equalsIgnoreCase(NetworkType.NETWORK_TYPE_ALL)) {
			return null;
		}

		ServiceInfo[] restrictedSOCs = getNonMatchingRestrictedSOCs(networkType);

		//defect PROD00154523 background
		// a bound service's network type configuration is subset of the leading SOC ( the service that this 
		// service bound to), this pose issue when swap equipment between network. 
		//  Example: 
		// SOC SRIM40NN support both CDMA and HSPA networks
		//  its bound service: SRIM3M only support CDMA network
		// When a subscriber who has SOC SRIM40NN, and trying the swap from CDMA to HSPA through equipment
		// change. SOC SRIM40NN will remain on profile whereas SOC SRIM3M should be dropped. 
		//
		// But the public API revmoeService( String serviceCode) prevent us from removing 
		// Bound Service (throwing InvalidServiceChangeException: service can only be removed by system: SRIM3MF  : [reason=BOUND_SERVICE])
		//
		// The fix are 
		// 1) make a new private method removeService(boolean allowBound, String serviceCode)
		// 2) move all logic of the current removeService(serviceCode) to new method with slight changes: 
		//     call removeSerivce0( allowBound, serviceCode) instead of removeService0(serviceCode) 
		// 3)  current removeService( serviceCode) just call the new method with allowBound=false
		// 4) remove removeService0( serviceCode )
		// 5) Here, we call new method with allowBound=true;  
		for (int i = 0; i < restrictedSOCs.length; i++) {
			removeService(true, restrictedSOCs[i].getCode());
		}

		return restrictedSOCs;
	}

	/*
	 * New method introduced for Holborn R1 project
	 */
	private ServiceInfo[] getNonMatchingRestrictedSOCs(String networkType) throws TelusAPIException {

		List<ServiceInfo> nonMatchingSOCs = new ArrayList<ServiceInfo>();

		//obtain included and optional contract services to validate networkTypes
		ServiceAgreementInfo[] includedServices = delegate.getIncludedServices0(false);
		ServiceAgreementInfo[] optionalServices = delegate.getOptionalServices0(false);

		for (int i = 0; i < includedServices.length; i++) {
			if (!includedServices[i].getService().isCompatible(networkType)) {
				nonMatchingSOCs.add(includedServices[i].getService0());
			}
		}

		for (int i = 0; i < optionalServices.length; i++) {
			if (!optionalServices[i].getService().isCompatible(networkType)) {
				nonMatchingSOCs.add(optionalServices[i].getService0());
			}
		}

		return nonMatchingSOCs.toArray(new ServiceInfo[nonMatchingSOCs.size()]);
	}

	//This only implement the public interface
	//This is change is for defect PROD00154523
	public void removeService(String serviceCode) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException, SystemException, ApplicationException {
		//The original implementation is moved to the method removeService( allBound, serviceCode) , 
		// so here we only need to delegate to it.
		removeService(false, serviceCode);
	}

	// Refactoring: add one more parameter: allowBound, and pass down this information - to allow better reuse of this method's logic
	// This is change is for defect PROD00154523
	public void removeService(boolean allowBound, String serviceCode) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException, SystemException, ApplicationException {
		logger.debug("Before PAD: serverCode =[" + serviceCode + "]");
		serviceCode = Info.padService(serviceCode);
		logger.debug("After PAD: serverCode =[" + serviceCode + "]");
		logger.debug("----> removeService(" + serviceCode + ")");
		if (priceplanChange && oldVistoPreserved) {
			// do not allow removal of Visto service if legacy Visto is already preserved on the contract
			// this allows execution order of calling removeService after a Visto bundle has been added
			ServiceInfo service;
			try {
				service = pricePlan.getService(serviceCode);
			} catch (UnknownObjectException e) {
				service = changeContext.getRefDataFacade().getRegularService(serviceCode);
			}
			if (service.isVisto()) {
				logger.debug("Visto SOC " + serviceCode + " is preserved-->not removed in removeService.");
				return; // do nothing if it's trying to remove a Visto SOC
			}
		}
		removeService0(allowBound, serviceCode);
	}

	private ServiceAgreementInfo removeService0(boolean allowBoundAndPromotional, String serviceCode) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException, SystemException,
			ApplicationException {

		logger.debug("Before PAD: serverCode =[" + serviceCode + "]");
		serviceCode = Info.padService(serviceCode);
		logger.debug("After PAD: serverCode =[" + serviceCode + "]");
		ServiceAgreementInfo info = removeService0(serviceCode, allowBoundAndPromotional);

		removeBoundServices(info);
		addConflictingIncludedFeatures(info.getService0());
		String socCode = serviceCode.trim();
		if (socCode.equals(ServiceSummary.BLOCK_INCOMING_CALLS_IDEN) || socCode.equals(ServiceSummary.BLOCK_OUTGOING_CALLS_IDEN)) {
			addDispatchOnlyConflicts();
		}
		removeDispatchOnlyConflicts();
		// removed - it should only be done for Activation, Price Plan change and Renewal - R.A. 2007-07-03
		// removeNonMatchingServices(getValidationEquipment(), getAssociatedMule(), subscriber.getProvince());
		// remove911FeaturesInEdmonton();
		addOutboundCallerIdDisplay(info.getService0());
		addCombinedVoiceMailConflicts(info.getService0());
		checkInvoiceFormatChange(info.getService0(), false);

		if (info.getService().hasPromotion()) {
			logger.debug("checking for promo service to remove");
			ServiceRelationInfo[] relations = getServiceRelations(info.getService().getCode(), ServiceRelation.TYPE_PROMOTION);
			ServiceRelationInfo[] promos = ContractUtilities.filterRelationsByContract(relations, pricePlan.getDelegate());
			logger.debug("isPromos.count=" + promos.length);
			if (promos != null) {
				for (ServiceRelationInfo promo : promos) {
					ServiceInfo s = changeContext.getRefDataFacade().getRegularService(promo.getServiceCode());
					if (ContractUtilities.isServiceInContractService(s, changeContext.getCurrentContract().getDelegate().getServices0(false))) {
						logger.debug("attempting to remove " + promo.getServiceCode());
						removeService0(true, promo.getServiceCode());
					}
				}
			}
		}

		return info;
	}

	private ServiceAgreementInfo removeService0(String serviceCode, boolean allowBoundAndPromotional) throws InvalidServiceChangeException, TelusAPIException {
		ServiceAgreementInfo s = delegate.getService0(serviceCode, false);

		if (s == null) {
			throw new UnknownObjectException("Service does not exist", serviceCode);
		}

		ContractUtilities.testRemoval0(s, allowBoundAndPromotional);
		removeServiceImpl(serviceCode);

		return s;
	}

	private ServiceAgreementInfo removeServiceImpl(String serviceCode) {
		logger.debug("            -\"" + serviceCode + "\"");
		return delegate.removeService0(serviceCode);
	}

	// Remove Voice Mail service or feature.
	private void removeVoiceMail() throws TelusAPIException {

		logger.debug("---->     removeVoiceMail()");

		// Check the subscriber contract to see if the Voice Mail feature (switch code = "VM") exists.
		ServiceFeatureInfo[] contractFeatures = delegate.getFeatures0(false);
		ServiceAgreementInfo[] contractServices = delegate.getServices0(false);

		// If the contract contains Voice Mail as a feature, remove it.
		for (int j = 0; j < contractFeatures.length; j++) {
			RatedFeatureInfo ratedFeature = contractFeatures[j].getFeature0();
			if (ratedFeature.getSwitchCode().trim().equalsIgnoreCase(Feature.SWITCH_CODE_VOICE_MAIL)) {
				removeFeature(contractFeatures[j].getCode());
				logger.debug(">>>> Voice Mail removed from SubscriberContractInfo.");
			}
		}

		// Get all services which contain Voice Mail (switch code = "VM") and remove them.
		for (int k = 0; k < contractServices.length; k++) {
			ServiceFeatureInfo[] contractServiceFeatures = contractServices[k].getFeatures0(false);
			for (int l = 0; l < contractServiceFeatures.length; l++) {
				RatedFeatureInfo ratedServiceFeature = contractServiceFeatures[l].getFeature0();
				if (ratedServiceFeature.getSwitchCode().trim().equalsIgnoreCase(Feature.SWITCH_CODE_VOICE_MAIL)) {
					removeService0(contractServices[k].getCode(), false);
					logger.debug(">>>> Voice Mail removed from SubscriberContractInfo.");
					break;
				}
			}
		}
	}

	private void removeBoundServices(ServiceAgreementInfo contractService) throws ApplicationException, InvalidServiceChangeException, TelusAPIException {
		logger.debug(">>>> removeBoundServices(" + contractService.getCode() + ")");
		// TODO: remove BUGFIX: this is a temporary workaround

		ServiceInfo service = contractService.getService0();
		if (service.getService().hasBoundService() || service.getService().hasSequentiallyBoundService()) {
			ServiceRelationInfo[] relations = getRelations(service.getCode(), getPricePlan());
			for (int i = 0; i < relations.length; i++) {
				ServiceInfo s = changeContext.getRefDataFacade().getRegularService(relations[i].getServiceCode());
				//if(s.isBoundService()) {  // AMDOCS will remove promotional services
				if (delegate.containsService0(s.getCode(), false) && (s.isBoundService() || s.isSequentiallyBoundService())) { // AMDOCS will remove promotional services
					logger.debug(">>>> removing bound service [" + s.getCode() + "]");
					removeService0(s.getCode(), true);
				} else {
					logger.debug(">>>> related service [" + s.getCode() + "]");
				}
			}
		}
	}

	private void addConflictingIncludedFeatures(ServiceInfo optionalService) throws TelusAPIException, ApplicationException {
		logger.debug("---->     addConflictingIncludedFeatures(" + optionalService.getCode() + ")");
		if (!pricePlan.containsIncludedService(optionalService.getCode())) {
			RatedFeature[] features = optionalService.getFeatures();
			for (int i = 0; i < features.length; i++) {
				RatedFeatureInfo f = (RatedFeatureInfo) features[i];
				if (!f.isDuplFeatureAllowed()) {

					//----------------------------------------------------------------------------
					// if the feature's in the priceplan, but not this contract; add it
					//----------------------------------------------------------------------------
					if (pricePlan.containsFeature(f.getCode()) && !delegate.containsFeature0(f.getCode(), null, null, false, false)) {
						try {
							addFeature0(f);
						} catch (TelusAPIException e) {
							logger.debug("            +\"" + f.getCode() + "\"  (feature) FAILED - " + e.getMessage());
						}

					}

					ServiceInfo[] services = delegate.getPricePlan0().getServicesByFeature(f.getCode());
					for (int j = 0; j < services.length; j++) {
						//----------------------------------------------------------------------------
						// if the feature's in an included service, but not this contract; add it
						//----------------------------------------------------------------------------
						if (pricePlan.containsIncludedService(services[j].getCode()) && !delegate.containsService0(services[j].getCode(), false)) {
							try {
								addService0(services[j], null, null, false);
							} catch (TelusAPIException e) {
								logger.debug("            +\"" + services[j].getCode() + "\";     FAILED - " + e.getMessage());
							}
						}
					}
				}
			}
		}
	}

	private void addDispatchOnlyConflicts() throws TelusAPIException, ApplicationException {
		logger.debug("---->     addDispatchOnlyConflicts()");
		if (isTelephonyEnabled()) {
			ServiceInfo[] services = ContractUtilities.retainTelephonyDisabledConflicts(pricePlan.getDelegate().getIncludedServices0());
			RatedFeatureInfo[] features = ContractUtilities.retainTelephonyDisabledConflicts(pricePlan.getDelegate().getFeatures0());

			for (int i = 0; i < services.length; i++) {
				if (!delegate.containsService0(services[i].getCode(), false)) {
					try {
						addService0(services[i], null, null, false);
					} catch (TelusAPIException e) {
						logger.debug("            +\"" + services[i].getCode() + "\";     FAILED - " + e.getMessage());
					}
				}

			}

			for (int i = 0; i < features.length; i++) {
				if (!delegate.containsFeature0(features[i].getCode(), null, null, false, false)) {
					try {
						addFeature0(features[i]);
					} catch (TelusAPIException e) {
						logger.debug("            +\"" + features[i].getCode() + "\"  (feature) FAILED - " + e.getMessage());
					}
				}
			}
		}
	}

	private void removeDispatchOnlyConflicts() throws TelusAPIException {
		logger.debug("---->     removeDispatchOnlyConflicts()");
		if (!isTelephonyEnabled()) {
			ServiceAgreementInfo[] services = ContractUtilities.retainTelephonyDisabledConflicts(delegate.getServices0(false));
			ServiceFeatureInfo[] features = ContractUtilities.retainTelephonyDisabledConflicts(delegate.getFeatures0(false));

			for (int i = 0; i < services.length; i++) {
				removeService0(services[i].getCode(), false);
			}

			for (int i = 0; i < features.length; i++) {
				removeFeature(features[i].getCode());
			}
		}
	}

	// Add Outbound Caller ID Display service or feature if the removed service is Caller ID Restriction.
	private void addOutboundCallerIdDisplay(ServiceInfo service) throws TelusAPIException, ApplicationException {
		logger.debug("---->     addOutboundCallerIdDisplay(" + service.getCode() + ")");

		// Check if the removed service is TELUS Caller ID Restriction (SCNIR) and add the 
		// appropriate feature or service if required.
		if (service.getCode().trim().equals(ServiceSummary.BLOCK_OUTGOING_CALLER_ID_PCS.trim())) {
			addFeatureToContract(ContractUtilities.OUTBOUND_CALLER_ID_PCS_FEATURE);
		}

		// Check if the removed service is Koodo Caller ID Restriction (3SPXCIDR) and add the 
		// appropriate feature or service if required.
		if (service.getCode().trim().equals(ServiceSummary.BLOCK_OUTGOING_CALLER_ID_KOODO.trim())) {
			addFeatureToContract(ContractUtilities.OUTBOUND_CALLER_ID_KOODO_FEATURE);
		}
	}

	private void addFeatureToContract(String feature) throws TelusAPIException, ApplicationException {

		logger.debug("---->     addFeatureToContract(" + feature + ")");

		// Check the SubscriberContractInfo to see if the feature already exists.
		if (delegate.containsFeature0(feature, null, null, true, false)) {
			logger.debug(">>>> Feature already found on SubscriberContractInfo, not added...");
			return;
		}

		// If the feature is part of the price plan, add it.
		if (pricePlan.containsFeature(feature)) {
			RatedFeatureInfo ratedFeature = pricePlan.getDelegate().getFeature0(feature);
			try {
				addFeature0(ratedFeature);
			} catch (TelusAPIException e) {
				logger.debug(e);
			}

			// Otherwise, if the feature is part of an included service, add the included service.
		} else {
			try {
				ServiceInfo[] services = pricePlan.getDelegate().getIncludedServices0();
				for (int i = 0; i < services.length; i++) {
					if (services[i].containsFeature(feature)) {
						addService0(services[i], null, null, false);
					}
				}
			} catch (TelusAPIException e) {
				logger.debug(e);
			}
		}
	}

	// Add conflicting services or features if the removed service is Combined Voice Mail.
	private void addCombinedVoiceMailConflicts(ServiceInfo service) throws TelusAPIException, ApplicationException {
		if (ContractUtilities.isCombinedVoiceMail(service)) {
			// Note: check for voice mail AFTER all other conflicts have been added, since it may already be added through a
			// conflicting feature or service.
			addCombinedVoiceMailConflicts0(service);
			addVoiceMail();
		}
	}

	// Add conflicting services or features if the removed service is Combined Voice Mail.
	private void addCombinedVoiceMailConflicts0(ServiceInfo service) throws TelusAPIException, ApplicationException {

		logger.debug("---->     addCombinedVoiceMailConflicts0(" + service.getCode() + ")");

		// Get contract included services.
		ServiceAgreementInfo[] contractServices = delegate.getIncludedServices0(false);

		// Get priceplan included services.
		ServiceInfo[] includedServices = pricePlan.getDelegate().getIncludedServices0();

		// Get features from the Combined Voice Mail service.
		RatedFeatureInfo serviceFeatures[] = service.getFeatures0();

		//---------------------------------------
		// Conflicting features check.
		//---------------------------------------
		serviceFeatureLoop: for (int i = 0; i < serviceFeatures.length; i++) {

			// Check the subscriber contract features to see if the feature already exists.
			if (delegate.containsFeature0(serviceFeatures[i].getCode(), null, null, false, false)) {
				logger.debug(">>>> Feature [" + serviceFeatures[i].getCode() + "] already found on SubscriberContractInfo, not added.");
				continue serviceFeatureLoop;
			}

			// Check the subscriber contract included services to see if the feature already exists.
			for (int j = 0; j < contractServices.length; j++) {
				if (contractServices[j].getService().containsFeature(serviceFeatures[i].getCode())) {
					logger.debug(">>>> Service feature [" + serviceFeatures[i].getCode() + "] already found on SubscriberContractInfo, not added.");
					continue serviceFeatureLoop;
				}
			}

			// Check all priceplan features.  If there is a conflict with any Combined Voice Mail feature,
			// add the conflicting feature.
			if (pricePlan.containsFeature(serviceFeatures[i].getCode())) {
				try {
					addFeature0(pricePlan.getDelegate().getFeature0(serviceFeatures[i].getCode()));
					logger.debug(">>>> Added conflicting feature from priceplan: [" + serviceFeatures[i].getCode() + "]");
					continue serviceFeatureLoop;
				} catch (TelusAPIException e) {
					logger.debug("            +\"" + serviceFeatures[i].getCode() + "\"  (feature) FAILED - " + e.getMessage());
				}
			}

			// Loop through all priceplan included services.
			for (int k = 0; k < includedServices.length; k++) {
				// Check all priceplan included service features.  If there is a conflict with any Combined Voice Mail feature,
				// add the conflicting service.
				if (includedServices[k].containsFeature(serviceFeatures[i].getCode())) {
					try {
						addService0(includedServices[k], null, null, false);
						logger.debug(">>>> Added conflicting included service from priceplan: [" + includedServices[k].getCode() + "]");
						continue serviceFeatureLoop;
					} catch (TelusAPIException e) {
						logger.debug("            +\"" + includedServices[k].getCode() + "\"  (service) FAILED - " + e.getMessage());
					}
				}
			}
		}
	}

	// Add Voice Mail service or feature.
	private void addVoiceMail() throws TelusAPIException, ApplicationException {

		logger.debug("---->     addVoiceMail()");

		// Check the subscriber contract to see if the Voice Mail feature (switch code = "VM") already exists.
		ServiceFeatureInfo[] contractFeatures = delegate.getFeatures0(false);
		ServiceAgreementInfo[] contractServices = delegate.getServices0(false);

		// Check the contract features...
		for (int j = 0; j < contractFeatures.length; j++) {
			RatedFeatureInfo ratedFeature = contractFeatures[j].getFeature0();
			if (ratedFeature.getSwitchCode().trim().equalsIgnoreCase(Feature.SWITCH_CODE_VOICE_MAIL)) {
				logger.debug(">>>> Voice Mail already found on SubscriberContractInfo, not added.");
				return;
			}
		}

		// Check the contract services...
		for (int k = 0; k < contractServices.length; k++) {
			ServiceFeatureInfo[] contractServiceFeatures = contractServices[k].getFeatures0(false);
			for (int l = 0; l < contractServiceFeatures.length; l++) {
				RatedFeatureInfo ratedServiceFeature = contractServiceFeatures[l].getFeature0();
				if (ratedServiceFeature.getSwitchCode().trim().equalsIgnoreCase(Feature.SWITCH_CODE_VOICE_MAIL)) {
					logger.debug(">>>> Voice Mail already found on SubscriberContractInfo, not added.");
					return;
				}
			}
		}

		// If the priceplan contains Voice Mail, add the feature or service here.
		RatedFeatureInfo[] pricePlanFeatures = pricePlan.getDelegate().getFeatures0();
		ServiceInfo[] includedServices = pricePlan.getDelegate().getIncludedServices0();

		// Check priceplan features...
		for (int x = 0; x < pricePlanFeatures.length; x++) {
			if (pricePlanFeatures[x].getSwitchCode().trim().equalsIgnoreCase(Feature.SWITCH_CODE_VOICE_MAIL)) {
				try {
					addFeature0(pricePlanFeatures[x]);
					logger.debug(">>>> Voice Mail added to SubscriberContractInfo.");
				} catch (TelusAPIException e) {
					logger.debug("            +\"" + pricePlanFeatures[x].getCode() + "\"  (feature) FAILED - " + e.getMessage());
				}
			}
		}

		// Check priceplan included services...
		for (int y = 0; y < includedServices.length; y++) {
			RatedFeatureInfo includedServiceFeatures[] = includedServices[y].getFeatures0();
			for (int z = 0; z < includedServiceFeatures.length; z++) {
				if (includedServiceFeatures[z].getSwitchCode().trim().equalsIgnoreCase(Feature.SWITCH_CODE_VOICE_MAIL)) {
					try {
						addService0(includedServices[y], null, null, false);
						logger.debug(">>>> Voice Mail added to SubscriberContractInfo.");
						break;
					} catch (TelusAPIException e) {
						logger.debug("            +\"" + Feature.SWITCH_CODE_VOICE_MAIL + "\"  (service) FAILED - " + e.getMessage());
					}
				}
			}
		}
	}

	private void checkInvoiceFormatChange(ServiceInfo service, boolean addService) throws TelusAPIException, ApplicationException {
		//  BAN auto -suppress

		if (pricePlan.getBrandId() == Brand.BRAND_ID_TELUS && service != null && !service.isWPS() && service.containsCategory(Feature.CATEGORY_CODE_BLIN)) {
			if (changeContext.getCurrentAccount().isPostpaid()) {
				InvoiceProperties invoiceProperties = changeContext.getCurrentAccount().getDelegate().getInvoiceProperties();
				if (addService) {
					changeInvoiceFormat = invoiceProperties.getInvoiceSuppressionLevel() != null && invoiceProperties.getInvoiceSuppressionLevel().equals(InvoiceSuppressionLevel.SUPPRESS_ALL);
				}
			}
		}
	}

	public void setCommitmentEndDate(Date commitmentEndDate) {
		delegate.setCommitmentEndDate(commitmentEndDate);
	}

	private void removeNonMatchingServices(EquipmentBo equipment, MuleEquipment associatedMule, String provinceCode) throws ApplicationException, TelusAPIException {
		logger.debug(">>>> removeNonMatchingServices(" + equipment.getEquipmentType() + ", " + provinceCode + ")");

		if (equipment.isSIMCard() && associatedMule != null) {
			equipment.setLastMule(associatedMule);
			equipment.setLastMuleIMEI(associatedMule.getSerialNumber());
		}

		ServiceAgreementInfo[] services = getDelegate().getServices0(false);
		ServiceAgreementInfo[] matchingServices = ContractUtilities.retainServices(services, equipment);
		ServiceAgreementInfo[] nonMatchingServices = ContractUtilities.difference(services, matchingServices);

		for (int i = 0; i < nonMatchingServices.length; i++) {
			if ((delegate.containsService0(nonMatchingServices[i].getCode(), false)) && (!nonMatchingServices[i].getService().isPromotion()) && (!nonMatchingServices[i].getService().isBoundService())) {
				removeService0(nonMatchingServices[i].getCode(), false);
				removeBoundServices(nonMatchingServices[i]);
			}
		}
	}

	// Remove Outbound Caller ID Display service or feature if the added service is Caller ID Restriction.
	private void removeOutboundCallerIdDisplay(ServiceInfo service) throws TelusAPIException {

		logger.debug("---->     removeOutboundCallerIdDisplay(" + service.getCode() + ")");

		// Check if the added service is TELUS Caller ID Restriction (SCNIR) and remove the 
		// appropriate feature or service if required..
		if (service.getCode().trim().equals(ServiceSummary.BLOCK_OUTGOING_CALLER_ID_PCS.trim())) {
			removeFeatureFromContract(ContractUtilities.OUTBOUND_CALLER_ID_PCS_FEATURE);
		}

		// Check if the added service is Koodo Caller ID Restriction (3SPXCIDR) and remove the 
		// appropriate feature or service if required..
		if (service.getCode().trim().equals(ServiceSummary.BLOCK_OUTGOING_CALLER_ID_KOODO.trim())) {
			removeFeatureFromContract(ContractUtilities.OUTBOUND_CALLER_ID_KOODO_FEATURE);
		}
	}

	private void removeFeatureFromContract(String feature) throws TelusAPIException {

		logger.debug("---->     removeFeatureFromContract(" + feature + ")");

		// Get all services which contain the feature and remove them.
		ServiceAgreementInfo[] services = delegate.getServicesByFeature0(feature, null, null, false);
		for (int i = 0; i < services.length; i++) {
			removeService0(services[i].getCode(), false);
		}

		// If the price plan included features contains the feature, remove it.
		if (delegate.containsFeature0(feature, null, null, false, false)) {
			removeFeature(feature);
		}
	}

	/**
	  * This method check if this contract contain any voice mail dependent services but lack of of voice mail service itself.
	  * when we find any VM orphan service, this method will remove the service. 
	  * 
	  * @param removeDependant
	  * @throws InvalidServiceChangeException
	  * @throws TelusAPIException
	 * @throws ApplicationException 
	 * @throws SystemException 
	  */
	public void checkForVoicemailService() throws InvalidServiceChangeException, TelusAPIException, SystemException, ApplicationException {

		List<ServiceFeatureInfo> vmOrphanFeatures = getVoicemailOrphanFeatures();

		if (vmOrphanFeatures.isEmpty() == false) {
			for (int i = 0; i < vmOrphanFeatures.size(); i++) {
				removeService(true, vmOrphanFeatures.get(i).getServiceCode());
			}
		}
	}

	/**
	 * Some service / feature depends on voice mail service, when the voice mail itself does not exist on a contract, those service become 
	 * "orphan" service / feature.
	 * 
	 * This method return a list of voice mail dependent feature when voice mail service itself does not exist in this contract
	 * 
	 * @return empty list if the contract has VM service, other return a list of VM orphan ContractFeature if there is any. The return can be 
	 * empty, but will never be null.
	 *   
	 * @throws UnknownObjectException
	 * @throws TelusAPIException
	 */
	public List<ServiceFeatureInfo> getVoicemailOrphanFeatures() throws UnknownObjectException, TelusAPIException {

		List<ServiceFeatureInfo> featureList = new ArrayList<ServiceFeatureInfo>();

		ServiceFeatureInfo[] features = delegate.getFeatures0(false, true);

		//loop through all the features in this contract
		for (int i = 0; i < features.length; i++) {

			String featureCategory = features[i].getFeature().getCategoryCode();

			String switchCode = features[i].getFeature().getSwitchCode();
			//trimming switchCode is necessary as the switchCode sometimes has padding space at the end.
			if (switchCode != null)
				switchCode = switchCode.trim();

			//check if the feature is VTT or VVM , both require VM service
			if (Feature.CATEGORY_CODE_VOICE2TEXT.equals(featureCategory) || Feature.CATEGORY_CODE_VISUALVOICEMAIL.equals(featureCategory)) {

				//The following check is to ensure that the V2T or VVM feature is not on promotion or bound service. 
				//These type of service is not added by customer's request, so it's okay to leave them on the contract.

				//The next line itself implies that these VM dependent feature will never be price plan included feature.
				//Otherwise, the call will fail.
				Service theService = delegate.getService(features[i].getServiceCode()).getService();
				if (!theService.isPromotion() && !theService.isBoundService() && !theService.isSequentiallyBoundService()) {
					featureList.add(features[i]);
				}
			}
			//check if the feature is VM feature
			else if (Feature.SWITCH_CODE_VOICE_MAIL.equals(switchCode)) {

				//The following check is necessary: it's to make sure we are not going to call delegate.getService( pricePlanCode );
				//which will definitely cause Exception.
				if (features[i].getServiceCode().equals(getPricePlan().getCode())) {

					//this feature is a PricePlan's included feature
					//Now we are sure the contract does have VM feature, no need to continue the checking. We can
					//safely assert that there is no VM orphan service by emptying the return collection.   
					featureList.clear();
					break;
				}

				//The following check is strange to me. Per Winnie, the only rational explanation would be: 
				//if an customer want to buy VM dependent service, then he/she has to buy the VM service. 
				//Promotion/Bound services are freebie , so exclude them from the checking.
				Service theService = delegate.getService(features[i].getServiceCode()).getService();
				if (!theService.isPromotion() && !theService.isBoundService() && !theService.isSequentiallyBoundService()) {

					//Now we are sure the contract does have VM feature, no need to continue the checking. We can
					//safely assert that there is no VM orphan service by emptying the return collection.   
					featureList.clear();
					break;
				}
			}
		}

		return featureList;
	}

	@Override
	public void save(String dealerCode, String salesRepCode) throws ApplicationException {

		logger.debug("Contract.save() => start ...");
		EquipmentBo oldEquipment = null;
		EquipmentBo newEquipment = null;

		Date contractEffectiveDate = getEffectiveDate();
		if (contractEffectiveDate != null) {

			Date targetDate = subscriber.getStartServiceDate();
			if (contractEffectiveDate.before(targetDate)) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Contract effective date (" + getEffectiveDate() + ") must be after subscriber start service date (" + targetDate + ").", "");
			}

			targetDate = subscriber.getMigrationDate();
			if (targetDate != null) {
				if (contractEffectiveDate.before(targetDate)) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Contract effective date (" + getEffectiveDate() + ") must be after subscriber migration date(" + targetDate + ").", "");
				}
			}
		}

		try {

			if (delegate.isModified() || contractRenewal) {
				checkForVoicemailService(); //Ensure VTT featue is not improperly added

				logger.debug("Contract.save() => delegate.isModified() || contractRenewal");
				String[] originalMultiRingPhones = subscriber.getDelegate().getMultiRingPhoneNumbers();
				String[] modifiedMultiRingPhones = getMultiRingPhoneNumbers();

				Service[] restrictedSOCsLost = removeNonMatchingRestrictedSOCs(getValidationEquipment().getNetworkType());

				// Fixed defect PROD00132009 - all rules pertaining to equipment type/soc relationship should be removed
				//								when swapping to an HSPA equipment or staying on an HSPA equipment
				//								Rules should be applied when swapping to or staying on non-HSPA equipment

				if (getEquipmentChangeRequest() != null) { // swapping equipment
					//swapping to HSPA; override equipment type
					if (getEquipmentChangeRequest().getNewEquipment().isHSPA()) {
						delegate.getPricePlanValidation0().setEquipmentServiceMatch(false);
					} else { //swapping to non-HSPA; validate equipment type
						delegate.getPricePlanValidation0().setEquipmentServiceMatch(true);
					}
				} else { // no swap
					//  staying on HSPA; override equipment type
					if (subscriber.getEquipment().isHSPA()) {
						delegate.getPricePlanValidation0().setEquipmentServiceMatch(false);
					} else {//  staying on non-HSPA; validate equipment type
						delegate.getPricePlanValidation0().setEquipmentServiceMatch(true);
					}
				}

				//----------------------------------------------
				// Save contract.
				//----------------------------------------------
				try {
					ClientIdentity clientIdentity = changeContext.getClientIdentity();
					if (clientIdentity == null) {
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "", "Missing Client Identity", "");
					}
					if (priceplanChange) {
						logger.debug("Contract.save() => priceplanChange");
						ServiceAgreementInfo services[] = getDelegate().getServices0(false);
						printServices(services);
						ServiceInfo service;
						for (int i = 0; i < services.length; i++) {
							service = services[i].getService0();
							if (service.isPromotion()) {
								undoChangeToService0(service.getCode());
							}
						}
						services = getDelegate().getServices0(false);
						printServices(services);

						// TODO: instead of following; change equipment & priceplan, if neccesary
						// TODO: report equipment change separately
						if (getEquipmentChangeRequest() != null) {

							StringBuffer eqcBuffer = new StringBuffer();

							oldEquipment = (EquipmentBo) changeContext.getCurrentEquipment();
							newEquipment = new EquipmentBo((EquipmentInfo) getEquipmentChangeRequest().getNewEquipment(), changeContext);

							eqcBuffer.append("Old SN(").append(oldEquipment.getSerialNumber()).append(")").append(" new SN(").append(newEquipment.getSerialNumber()).append(")");

							if (newEquipment.isHSPA() && newEquipment.getSerialNumber().equals(oldEquipment.getSerialNumber())) { // HPSA handset only swap
								//** VOLTE logic **/
								if (newEquipment.getDelegate().getAssociatedHandset() != null) {
									try {
										ServiceInfo volteSocSvcInfo = changeContext.getSubscriberLifecycleFacade().getVolteSocIfEligible(subscriber.getDelegate(), delegate,
												newEquipment.getDelegate().getAssociatedHandset(), priceplanChange);
										if (volteSocSvcInfo != null) {
											delegate.addService(volteSocSvcInfo, new Date());
										}
									} catch (Throwable t) {
										logger.error("Error adding VOLTE SOC in ContractBo. subscriber=["+subscriber.getSubscriberId()+"], ban=[" + subscriber.getBanId()+"]");
									}
								} /*** VOLTE logic ends **/
								
								changeContext.getSubscriberLifecycleFacade().changePricePlan(subscriber.getDelegate(), delegate, dealerCode, salesRepCode, delegate.getPricePlanValidation0(),
										changeContext.isNotificationSuppressionInd(), changeContext.getAuditInfo(), changeContext.getCurrentContract().getDelegate(),
										changeContext.getSubscriberLifecycleFacadeSessionId());

							} else {
								//PCS and IDEN (not mule to mule) swap - call KB to change equipment
								changeContext.getSubscriberLifecycleFacade().changeEquipment(subscriber.getDelegate(), oldEquipment.getDelegate(), newEquipment.getDelegate(),
										(EquipmentInfo[]) getEquipmentChangeRequest().getSecondaryEquipments(), dealerCode, salesRepCode, getEquipmentChangeRequest().getRequestorId(),
										getEquipmentChangeRequest().getSwapType(), delegate, delegate.getPricePlanValidation0(), changeContext.getAuditInfo(),
										changeContext.isNotificationSuppressionInd(), changeContext.getCurrentContract().getDelegate(), changeContext.getSubscriberLifecycleFacadeSessionId());

							}

							if (newEquipment.isHSPA() || oldEquipment.isHSPA()) {
								changeContext.getSubscriberLifecycleFacade().swapEquipmentForPhoneNumberInSems(subscriber.getDelegate(), getEquipmentChangeRequest());
							}

							subscriber.setEquipment(newEquipment);
							//							provider.getInteractionManager0().subscriberChangeEquipment(subscriber, oldEquipment, equipmentChangeRequest);

						} else {
							if (originalMultiRingPhones != null && originalMultiRingPhones.length > 0) {
								delegate.setMultiRingPhoneNumbers(originalMultiRingPhones);
							}

							// Multi Ring
							ServiceAgreementInfo[] allServices = delegate.getServices0(true);
							for (int i = 0; i < allServices.length; i++) {
								ServiceAgreementInfo aService = allServices[i];
								if (containsMultiRingFeature(aService)) {
									delegate.setMultiRingInfos(ContractUtilities.getMultiRingInfos(originalMultiRingPhones, modifiedMultiRingPhones, aService));
								}
							}

							changeContext.getSubscriberLifecycleFacade().changePricePlan(subscriber.getDelegate(), delegate, dealerCode, salesRepCode, delegate.getPricePlanValidation0(),
									changeContext.isNotificationSuppressionInd(), changeContext.getAuditInfo(), changeContext.getCurrentContract().getDelegate(),
									changeContext.getSubscriberLifecycleFacadeSessionId());

						}
					} else { //if (priceplanChange) 
						// No price plan change: only service change and maybe equipment change as well

						// change equipment before services, if necessary
						if (getEquipmentChangeRequest() != null) {
							getEquipmentChangeRequest().setInvokeAPNFix(false);
							changeContext.getSubscriberLifecycleFacade().changeEquipment(subscriber.getDelegate(), changeContext.getCurrentAccount().getDelegate(), getEquipmentChangeRequest(),
									changeContext.getSubscriberLifecycleFacadeSessionId());
						}

						// Multi Ring
						ServiceAgreementInfo[] allServices = delegate.getServices0(true);
						ServiceAgreementInfo aService = null;
						for (int i = 0; i < allServices.length; i++) {
							aService = allServices[i];
							if (containsMultiRingFeature(aService)) {
								logger.debug("yyyy = Services contains Multi-Ring Feature = " + aService.getCode());
								delegate.setMultiRingInfos(ContractUtilities.getMultiRingInfos(originalMultiRingPhones, modifiedMultiRingPhones, aService));
								//								printMultiRingInfo(delegate.getMultiRingInfos());
							}
						}

						if (changeContext.getCurrentAccount().isPrepaidConsumer() && ContractUtilities.containsNotSavedPrepaidCallingCircleService(delegate.getServices0(true))) {
							ServiceAgreementInfo callingCircleService = ContractUtilities.getNotSavedPrepaidCallingCircleService(delegate.getServices0(true));
							changePrepaidCallingCircleService(callingCircleService, dealerCode, salesRepCode);
						} else {
							changeContext.getSubscriberLifecycleFacade().changeServiceAgreement(subscriber.getDelegate(), delegate, dealerCode, salesRepCode, delegate.getPricePlanValidation0(),
									changeContext.isNotificationSuppressionInd(), changeContext.getAuditInfo(), changeContext.getSubscriberLifecycleFacadeSessionId());
						}
					}
					
				} catch (ApplicationException ae) {
					// Catch ApplicationExceptions here to preserve the original error code
					logger.error(ae);
					throw ae;
				} catch (SystemException se) {
					logger.error(se);
					throw se;
				} catch (Throwable t) {
					logger.error(t);
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, t.getMessage(), StringUtils.EMPTY, t);
				}

				//-----------------------------------------------------------
				// Remove the secondary service from another active subscriber
				// sharing this plan if this subscriber was primary on a shareable
				// priceplan.
				//-----------------------------------------------------------
				// FYI, the following assumes "this.subscriber.contract" has
				// been previously set to the old contract, since calling
				// this.subscriber.getContract0() otherwise would load the
				// new contract information from the database.
				//-----------------------------------------------------------
				if (priceplanChange) {
					if (subscriber.getContract().isShareablePricePlanPrimary()) {
						subscriber.yieldShareablePricePlanPrimaryStatus(dealerCode, salesRepCode, getEffectiveDate());
					}
				}

				//				provider.getInteractionManager0().contractSave(this, dealerCode, salesRepCode);
				commit(dealerCode, salesRepCode);

				// Switch between paper and e-bills for Ampd accounts.
				if (changeInvoiceFormat) {
					changeInvoiceFormat();
				}

			} else if (getEquipmentChangeRequest() != null) {
				// perform the swap even if there's no contract change
				//				subscriber.changeEquipment(getEquipmentChangeRequest(), false);
				//				equipmentChangeRequest = null;
			}

			logger.debug("Contract.save() => saveCommitment");
			CommitmentInfo commitment = delegate.getCommitment();
			if (commitment.isModified()) {
				changeContext.getSubscriberLifecycleFacade().updateCommitment(subscriber.getDelegate(), commitment, dealerCode, salesRepCode, changeContext.getSubscriberLifecycleFacadeSessionId());
				commitment.setModified(false);
			}

			serviceChangeHistory = null;
			
			if (oldEquipment != null && newEquipment != null && oldEquipment.isCDMA() && newEquipment.isHSPA()) {
				refreshSocAndFeatures();
			}

		} catch (ApplicationException ae) {
			// Catch the WCC threshold breach error here and throw it back up the chain in order to maintain the original error code
			if (StringUtils.equalsIgnoreCase(SystemCodes.CMB_OCSSAMS_DAO, ae.getSystemCode()) && StringUtils.equalsIgnoreCase(ErrorCodes.OCS_BAN_PPU_BREACH, ae.getErrorCode())) {
				throw ae;
			}
			// Otherwise, preserve the existing exception logic and throw the generic 'contract change error' code
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, ae.getMessage(), StringUtils.EMPTY, ae);
		} catch (TelusException te) {
			if ("APP20001".equals(te.id)) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.PHONE_NUMBER_ADDITIONAL_RESERVATION_FAILED, te.getMessage(), StringUtils.EMPTY, te);
			} else if ("APP20003".equals(te.id)) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.DUPLICATE_FEATURE, te.getMessage(), StringUtils.EMPTY, te);
			} else if ("PREPAID_ADDTOOMANYFEATURES_EXCEPTION".equals(te.id)) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.WPS_ADD_TOOMANY_FEATURES, WPSFeatureException.ADD_TOOMANYFEATURES_MESSAGE_EN,
						WPSFeatureException.ADD_TOOMANYFEATURES_MESSAGE_FR, te);
			} else {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, te.getMessage(), StringUtils.EMPTY, te);
			}
		} catch (TelusAPIException tae) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, tae.getMessage(), StringUtils.EMPTY, tae);
		} catch (SystemException se) {
			throw se;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, t.getMessage(), StringUtils.EMPTY, t);
		}

		logger.debug("Contract.save() => exit ...");
	}

	private ServiceAgreementInfo undoChangeToService0(String serviceCode) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException {
		ServiceAgreementInfo s = delegate.getService0(serviceCode, false);

		if (s == null) {
			throw new UnknownObjectException("Service does not exist", serviceCode);
		}

		undoRemoveServiceImpl(serviceCode);

		return s;
	}

	private ServiceAgreementInfo undoRemoveServiceImpl(String serviceCode) {
		logger.debug("            -\"" + serviceCode + "\"");
		return delegate.undoRemoveService0(serviceCode);
	}

	private static void printServices(ServiceAgreementInfo services[]) {
		ServiceInfo service;
		for (int i = 0; i < services.length; i++) {
			try {
				service = services[i].getService0();
				logger.debug("service[" + i + "]=" + service.getCode() + " hasPromo=" + service.hasPromotion() + " isPromo=" + service.isPromotion() + " " + service.getDescription());
			} catch (Throwable t) {
			}
		}
	}

	private boolean containsMultiRingFeature(ServiceAgreementInfo service) {
		if (subscriber.isPCS() && service != null) {
			ServiceFeatureInfo[] features = service.getFeatures0(true);
			for (int i = 0; i < features.length; i++) {
				if (Feature.CATEGORY_CODE_MULTI_RING.equalsIgnoreCase(features[i].getFeature().getCategoryCode()))
					return true;
			}
		}
		return false;
	}

	public boolean isShareablePricePlanPrimary() throws TelusAPIException {
		if (pricePlan.isSharable()) {
			return !containsService(pricePlan.getDelegate().getSecondarySubscriberService());
		}
		return false;
	}

	public boolean isShareablePricePlanSecondary() throws TelusAPIException {
		return pricePlan.isSharable() && !isShareablePricePlanPrimary();
	}

	public void commit(String dealerCode, String salesRepCode) throws TelusAPIException {
		activation = false;
		priceplanChange = false;

		subscriber.setContract(this);

		delegate.commit();
	}

	private void changeInvoiceFormat() throws ApplicationException {
		InvoicePropertiesInfo invoiceProperties = changeContext.getAccountInformationHelper().getInvoiceProperties(changeContext.getChangeInfo().getBan());

		if (invoiceProperties.getInvoiceSuppressionLevel() != null && invoiceProperties.getInvoiceSuppressionLevel().equals(InvoiceSuppressionLevel.SUPPRESS_ALL)) {
			invoiceProperties.setInvoiceSuppressionLevel(InvoiceSuppressionLevel.GROUP_DISPATCH);
			invoiceProperties.setHoldRedirectDestinationCode(null);
			invoiceProperties.setHoldRedirectFromDate(null);
			invoiceProperties.setHoldRedirectToDate(null);
		} else {
			invoiceProperties.setInvoiceSuppressionLevel(InvoiceSuppressionLevel.SUPPRESS_ALL);
			invoiceProperties.setHoldRedirectDestinationCode(BillHoldRedirectDestination.HOLD_BILL);
			invoiceProperties.setHoldRedirectFromDate(new java.util.GregorianCalendar().getTime());

			Date holdRedirectToDate = null;
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				dateFormat.setLenient(false);
				holdRedirectToDate = dateFormat.parse("20991231");
			} catch (Exception e) {
				holdRedirectToDate = new Date();
			}

			invoiceProperties.setHoldRedirectToDate(holdRedirectToDate);
		}

		changeContext.getAccountLifecycleManager().updateInvoiceProperties(changeContext.getChangeInfo().getBan(), invoiceProperties, changeContext.getAccountLifecycleManagerSessionId());
	}


	@Override
	public boolean isPTTServiceIncluded() {
		return ContractUtilities.isPTTServiceIncluded(getServices());
	}

	/**
	 * This method is intended to fix RIM / MB provisioning for existing RIM/MB CDMA subscribers switching to HSPA only.
	 * 
	 * This method was created for fixing CDMA to HSPA equipment change. Scenario: An APN SOC needs to be present for RIM/MB (Mobile Broadband). Such
	 * SOC needs to be added to subscriber that currently has an RIM/MB CDMA equipment, and switching to HSPA. Price plan has been updated with
	 * included SOC, and this method would re-add this missing included service to subscriber's contract.
	 * 
	 * IMPORTANT: This method should be called at the end of transaction only.
	 */
	public void refreshSocAndFeatures() {
		try {
			if (AppConfiguration.isRefreshApn()) {
				ArrayList<ServiceAgreementInfo> removedOptionalServices = new ArrayList<ServiceAgreementInfo>();
				ServiceAgreementInfo[] optionalServices = getOptionalServices();
				ServiceInfo rimAddOnSoc = changeContext.getRefDataFacade().getRegularService(ContractUtilities.getAddOnAPNSoc(Feature.FEATURE_CODE_RIMAPN));
				ServiceInfo mbAddOnSoc = changeContext.getRefDataFacade().getRegularService(ContractUtilities.getAddOnAPNSoc(Feature.FEATURE_CODE_MBAPN));
				AccountType accountType = changeContext.getRefDataFacade().getAccountType(
						String.valueOf(changeContext.getCurrentAccount().getAccountType()) + String.valueOf(changeContext.getCurrentAccount().getAccountSubType()),
						changeContext.getCurrentAccount().getBrandId());
				String defaultDealerCode = accountType.getDefaultDealer();
				String defaultSalesCode = accountType.getDefaultSalesCode();
				boolean addRimAddOn = false;
				boolean addMbAddOn = false;
				Date rimAddOnExpiry = null;
				Date mbAddOnExpiry = null;

				// STEP 1: Process optional services first as they can override included services
				for (int i = 0; i < optionalServices.length; i++) {
					ServiceInfo refOptionalService = optionalServices[i].getService0();

					if ((refOptionalService.hasRIMAPN() && !optionalServices[i].containsFeature(Feature.FEATURE_CODE_RIMAPN)) || // if RIM APN SOC and
																																	// contract
																																	// doesn't have
																																	// the latest
																																	// update
							(refOptionalService.hasMBAPN() && !optionalServices[i].containsFeature(Feature.FEATURE_CODE_MBAPN))) {
						ServiceAgreementInfo removedSoc = null;
						if ((refOptionalService.getExpiryDate() == null || refOptionalService.getExpiryDate().after(changeContext.getLogicalDate()))
								&& changeContext.getRefDataHelper().retrieveDealerbyDealerCode(optionalServices[i].getDealerCode(), false) != null) { // still
																																						// for
																																						// sale
																																						// and
																																						// dealer
																																						// is
																																						// not
																																						// expired
							removedOptionalServices.add(optionalServices[i]);
							logger.debug("Removing Service [" + optionalServices[i].getCode() + "]. Effective=" + optionalServices[i].getEffectiveDate() + ",Expiry="
									+ optionalServices[i].getExpiryDate() + ",DealerCode=" + optionalServices[i].getDealerCode() + ",SalesRep=" + optionalServices[i].getSalesRepId());
							removedSoc = removeServiceImpl(optionalServices[i].getCode());
						}

						if (removedSoc == null) {
							logger.debug("Not dropping SOC [" + refOptionalService.getCode() + "] because it is not for sale or dealer is expired.");

							// check if RIM add-on should be added instead
							if (rimAddOnSoc != null && refOptionalService.hasRIMAPN()) {
								if (!addRimAddOn) { // flag not yet true
									addRimAddOn = true;
									rimAddOnExpiry = optionalServices[i].getExpiryDate();
								} else {
									if (rimAddOnExpiry != null) { // determine the expiry date
										if (optionalServices[i].getExpiryDate() == null || rimAddOnExpiry.before(optionalServices[i].getExpiryDate())) {
											rimAddOnExpiry = optionalServices[i].getExpiryDate();
										}
									}
								}
							}

							// check if MB add-on should be added instead
							if (mbAddOnSoc != null && refOptionalService.hasMBAPN()) {
								if (!addMbAddOn) { // flag not yet true
									addMbAddOn = true;
									mbAddOnExpiry = optionalServices[i].getExpiryDate();
								} else {
									if (mbAddOnExpiry != null) { // determine the expiry date
										if (optionalServices[i].getExpiryDate() == null || mbAddOnExpiry.before(optionalServices[i].getExpiryDate())) {
											mbAddOnExpiry = optionalServices[i].getExpiryDate();
										}
									}
								}
							}
						}
					}
				}

				addRimAddOn = addRimAddOn && !containsService(rimAddOnSoc.getCode());
				addMbAddOn = addMbAddOn && !containsService(mbAddOnSoc.getCode());

				if (addRimAddOn) {
					logger.debug("Adding " + rimAddOnSoc.getCode());
					try {
						addService(rimAddOnSoc, null, rimAddOnExpiry);
					} catch (Throwable isce) {
						logger.debug("Cannot add " + rimAddOnSoc.getCode() + ". Exception" + isce);
						addRimAddOn = false;
					}
				}

				if (addMbAddOn) {
					logger.debug("Adding " + mbAddOnSoc.getCode());
					try {
						addService(mbAddOnSoc, null, mbAddOnExpiry);
					} catch (Throwable isce) {
						logger.debug("Cannot add " + mbAddOnSoc.getCode() + ". Exception" + isce);
						addMbAddOn = false;
					}
				}

				boolean sameDealerCode = ContractUtilities.haveSameDealerAndSalesRepCode(removedOptionalServices.toArray(new ServiceAgreementInfo[removedOptionalServices.size()]));
				delegate.getPricePlanValidation0().setEquipmentServiceMatch(false);
				if (removedOptionalServices.size() > 0 || addRimAddOn || addMbAddOn) {
					logger.debug("Saving contract after service removal or add-on add.");
					// commit removal or add-on soc
					boolean suppressNotification = true;
					changeContext.getSubscriberLifecycleFacade().changeServiceAgreement(subscriber.getDelegate(), delegate, defaultDealerCode, defaultSalesCode, delegate.getPricePlanValidation0(),
							suppressNotification, changeContext.getAuditInfo(), changeContext.getSubscriberLifecycleFacadeSessionId());
					delegate.commit();

					if (removedOptionalServices.size() > 0) {
						logger.debug("sameDealerCode=" + sameDealerCode);

						if (sameDealerCode) {
							// if all the SOCs to be added back had the same dealer code in SERVICE_AGREEMENT, then we can add them altogether
							// otherwise, we have to add them one by one and save them individually
							String socDealerCode = "";
							String socSalesRepId = "";
							boolean wasAnySocAdded = false;
							for (int i = 0; i < removedOptionalServices.size(); i++) {
								ServiceAgreementInfo optionalApnSoc = removedOptionalServices.get(i);
								socDealerCode = optionalApnSoc.getDealerCode();
								socSalesRepId = optionalApnSoc.getSalesRepId();
								try {
									logger.debug("Re-adding Service [" + optionalApnSoc.getCode() + "]");
									ServiceAgreementInfo soc = addService0((ServiceInfo) optionalApnSoc.getService(), null, optionalApnSoc.getExpiryDate(), true); // add
																																									// the
																																									// service
																																									// back
									soc.setTransaction(BaseAgreementInfo.ADD);
									ContractUtilities.copyServiceFeatures(soc, optionalApnSoc);
									wasAnySocAdded = true;
								} catch (Throwable t) {
									logger.debug("Exception when adding service [" + optionalApnSoc.getCode() + "] Exception:" + t);
								}
							}
							if (wasAnySocAdded) {
								logger.debug("changeServiceAgreement after adding services.");
								changeContext.getSubscriberLifecycleFacade().changeServiceAgreement(subscriber.getDelegate(), delegate, socDealerCode, socSalesRepId,
										delegate.getPricePlanValidation0(), changeContext.isNotificationSuppressionInd(), changeContext.getAuditInfo(),
										changeContext.getSubscriberLifecycleFacadeSessionId());
							}
						} else { // add and save one by one
							for (int i = 0; i < removedOptionalServices.size(); i++) {
								ServiceAgreementInfo optionalApnSoc = removedOptionalServices.get(i);
								try {
									logger.debug("Re-adding Service [" + optionalApnSoc.getCode() + "]");
									ServiceAgreementInfo soc = addService0(optionalApnSoc.getService0(), null, optionalApnSoc.getExpiryDate(), true); // add
																																						// the
																																						// service
																																						// back
									soc.setTransaction(BaseAgreementInfo.ADD);
									ContractUtilities.copyServiceFeatures(soc, optionalApnSoc);
									logger.debug("changeServiceAgreement after adding " + optionalApnSoc.getCode());
									changeContext.getSubscriberLifecycleFacade().changeServiceAgreement(subscriber.getDelegate(), delegate, optionalApnSoc.getDealerCode(),
											optionalApnSoc.getSalesRepId(), delegate.getPricePlanValidation0(), changeContext.isNotificationSuppressionInd(), changeContext.getAuditInfo(),
											changeContext.getSubscriberLifecycleFacadeSessionId());
									delegate.commit();
								} catch (Throwable t) {
									logger.debug("Exception when adding service [" + optionalApnSoc.getCode() + "]. Exception:" + t);
								}
							}
						}

						delegate.commit();
					}
				} else {
					logger.debug("No optional SOC dropped/added.");
				}

				// STEP 2: Process included services
				String[] apnSocs = AppConfiguration.getIncludedApnSocs(); // retrieve list of APN SOCs that are included in PPs
				boolean includedApnSocsAdded = false;
				for (int j = 0; j < apnSocs.length; j++) {
					if (getPricePlan().containsIncludedService(apnSocs[j]) && !delegate.containsService(apnSocs[j])) {
						// 1. if this APN SOC is included with the price plan on contract
						// 2. but it is missing from the contract
						addMissingIncludedService(apnSocs[j]); // add it back
						includedApnSocsAdded = true;
					}
				}

				if (includedApnSocsAdded) {
					logger.debug("Final contract save.");
					String socDealerCode = delegate.getPricePlanDealerCode();
					String socSalesRepId = delegate.getPricePlanSalesRepId();
					Dealer dealer = changeContext.getRefDataHelper().retrieveDealerbyDealerCode(socDealerCode, false);
					if (dealer == null) {
						socDealerCode = defaultDealerCode;
						socSalesRepId = defaultSalesCode;
					}
					changeContext.getSubscriberLifecycleFacade().changeServiceAgreement(subscriber.getDelegate(), delegate, socDealerCode, socSalesRepId, delegate.getPricePlanValidation0(),
							changeContext.isNotificationSuppressionInd(), changeContext.getAuditInfo(), changeContext.getSubscriberLifecycleFacadeSessionId());
					delegate.commit();
				} else {
					logger.debug("No PP Included APN SOC has been added to subscriber.");
				}
			}
		} catch (Throwable t) {
			logger.debug("Exception in refreshSOCOrFeatures " + t);
		}

	}

	/**
	 * Assumption: getPriceplan().getIncludedService(code) will not return UnknownObjectException
	 * 
	 * @param code
	 */
	private void addMissingIncludedService(String code) {
		try {
			ServiceInfo soc = getPricePlan().getDelegate().getIncludedService0(code); // Retrieve information about the SOC
			if (soc.hasRIMAPN() || soc.hasMBAPN()) { // logic is applicable for RIM/MB APN SOCs only
				RatedFeature[] ratedFeatures = soc.getFeatures();
				for (int i = 0; i < (ratedFeatures != null ? ratedFeatures.length : 0); i++) {
					if (!ratedFeatures[i].isDuplFeatureAllowed() && delegate.containsFeature0(ratedFeatures[i].getCode(), changeContext.getLogicalDate(), null, true, false)) {
						// if there exists feature conflict with this SOC, do not add
						return;
					}
				}
				// add only if there is no feature conflict
				logger.debug("Adding Missing Inclusive SOC [" + code + "] to PP [" + getPricePlan().getCode() + "]");
				addService(soc, null, getExpiryDate()); // add the included SOC
			}
		} catch (UnknownObjectException uoe) {
			logger.debug(" Unexpected exception. " + uoe);
		} catch (InvalidServiceChangeException isce) {
			logger.debug(" Unable to add the missing included SOC " + code + ". " + isce);
		} catch (Throwable t) {
			logger.debug(" Error adding missing included service " + code + ". Exception=" + t);
		}
	}

	@Override
	public ServiceAgreementInfo testRemoval(ServiceAgreementInfo contractService) throws InvalidServiceChangeException, TelusAPIException {
		return ContractUtilities.testRemoval0(contractService, false);
	}

	@Override
	public ServiceAgreementInfo[] addCard(Card card) {
		//not implemented
		return addCard(card, false);
	}

	@Override
	public ServiceAgreementInfo[] addCard(Card card, boolean autoRenew) {
		//not implemented
		return null;
	}

	@Override
	public ServiceAgreementInfo[] get911Services() {
		List<ServiceAgreementInfo> _911Services = new ArrayList<ServiceAgreementInfo>();
		ServiceAgreementInfo[] allServices = this.getServices();
		for (int i = 0; i < allServices.length; i++) {
			if (allServices[i].getService0().is911()) {
				_911Services.add(allServices[i]);
			}
		}

		return _911Services.toArray(new ServiceAgreementInfo[_911Services.size()]);
	}

	private void changePrepaidCallingCircleService(ServiceAgreementInfo prepaidCCService, String dealerCode, String salesRepCode) throws ApplicationException {

		ClientIdentity clientIdentity = changeContext.getClientIdentity();
		String applId = clientIdentity.getApplication();
		String userId = clientIdentity.getPrincipal();
		String phoneNumber = subscriber.getPhoneNumber();

		StringBuilder sb = new StringBuilder("Contract.changePrepaidCallingCircleService():  subscriber[").append(subscriber.getBanId()).append("/").append(subscriber.getSubscriberId()).append("] ");

		try {
			String kbMappedPrepaidServiceCode = prepaidCCService.getService().getWPSMappedKBSocCode();

			if (prepaidCCService.getTransaction() == ServiceFeatureInfo.DELETE) {
				ServiceAgreementInfo kbCCService = ContractUtilities.getKbMappedPrepaidService(kbMappedPrepaidServiceCode, delegate.getServices0(true));
				if (kbCCService != null) {
					ServiceFeatureInfo kbCCFeature = ContractUtilities.getKbCallingCircleFeature(kbCCService);
					if (kbCCFeature.getCallingCirclePhoneNumbersFromParam().length > 0) {
						sb.append(" remove CC list from prepaid;");
						changeContext.getSubscriberLifecycleManager().updatePrepaidCallingCircleParameters(applId, userId, phoneNumber, prepaidCCService, ServiceFeatureInfo.DELETE);
					}
					sb.append(" remove CC list from prepaid;");
					removeService(kbMappedPrepaidServiceCode);
				}

			} else if (prepaidCCService.getTransaction() == ServiceFeatureInfo.ADD) {

				ServiceAgreementInfo kbCCService = ContractUtilities.getKbMappedPrepaidService(kbMappedPrepaidServiceCode, delegate.getServices0(true));

				if (kbCCService == null) {
					//current contract does not contain KB SOC, add mapped KB SOC
					sb.append(" add mapped KB SOC;");
					kbCCService = addService(kbMappedPrepaidServiceCode);
				}
				ServiceFeatureInfo kbCCFeature = ContractUtilities.getKbCallingCircleFeature(kbCCService);

				ServiceFeatureInfo prepaidCCFeature = ContractUtilities.getPrepaidCallingCircleFeature(prepaidCCService, prepaidCCService.getCode().trim());

				//update prepaid system accordingly

				//if new feature's CC list is NOT empty, we add the CC list to prepaid system. 
				if (prepaidCCFeature.getCallingCirclePhoneNumbersFromParam().length > 0) {
					sb.append(" add CC list to prepaid;");
					changeContext.getSubscriberLifecycleManager().updatePrepaidCallingCircleParameters(applId, userId, phoneNumber, prepaidCCService, ServiceFeatureInfo.ADD);
				}

				//sync up KB CC feature parameter from prepaid CC feature.
				kbCCFeature.setParameter(prepaidCCFeature.getParameter());

			} else { //this is UPDATE

				ServiceAgreementInfo kbCCService = ContractUtilities.getKbMappedPrepaidService(kbMappedPrepaidServiceCode, delegate.getServices0(true));

				if (kbCCService == null) {
					//current contract does not contain KB SOC, add mapped KB SOC
					sb.append(" add mapped KB SOC;");
					kbCCService = addService(kbMappedPrepaidServiceCode);
				}
				ServiceFeatureInfo kbCCFeature = ContractUtilities.getKbCallingCircleFeature(kbCCService);

				ServiceFeatureInfo prepaidCCFeature = ContractUtilities.getPrepaidCallingCircleFeature(prepaidCCService, prepaidCCService.getCode().trim());

				if (kbCCFeature.getCallingCirclePhoneNumbersFromParam().length == 0) {
					//KB CC feature does not contain CC list, which means preapid CC feature was added with empty CC list
					sb.append(" add CC list to prepaid;");
					changeContext.getSubscriberLifecycleManager().updatePrepaidCallingCircleParameters(applId, userId, phoneNumber, prepaidCCService, ServiceFeatureInfo.ADD);
				} else {
					//KB CC feature contain CC list, assuming prepaid contain CC list
					sb.append(" update CC list in prepaid;");
					changeContext.getSubscriberLifecycleManager().updatePrepaidCallingCircleParameters(applId, userId, phoneNumber, prepaidCCService, ServiceFeatureInfo.UPDATE);
				}

				//sync up KB CC feature parameter from prepaid CC feature.
				kbCCFeature.setParameter(prepaidCCFeature.getParameter());
			}

			sb.append(" SubscriberLifecycleFacade.changeServiceAgreement()");

			changeContext.getSubscriberLifecycleFacade().changeServiceAgreement(subscriber.getDelegate(), delegate, dealerCode, salesRepCode, delegate.getPricePlanValidation0(),
					changeContext.isNotificationSuppressionInd(), changeContext.getAuditInfo(), changeContext.getSubscriberLifecycleFacadeSessionId());

			logger.debug(sb.append(" succeeded.").toString());

		} catch (Throwable t) {
			logger.error(sb.append(" failed, exception: ").append(t.getMessage()), t);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, t.getMessage(), "", t);
		}
	}

	//ported from TMContract
	public double getRecurringCharge() throws TelusAPIException {
		return getRecurringCharge(isSuppressPricePlanRecurringCharge());
	}

	//ported from TMContract
	public double getRecurringCharge(boolean suppressPricePlanRecurringCharge) throws TelusAPIException {
		return getRecurringCharge(suppressPricePlanRecurringCharge, true, true);
	}

	//ported from TMContract
	public double getRecurringCharge(boolean suppressPricePlanRecurringCharge, boolean includeShareableServices, boolean includeNonShareableServices) throws TelusAPIException {
		if (pricePlan == null) {
			throw new NullPointerException("pricePlan == null, use the API");
		}

		try {
			Date alternateRecurringChargeStartDate = null;

			double charge = (suppressPricePlanRecurringCharge) ? 0.0 : pricePlan.getRecurringCharge();

			ContractService[] services = getServices();
			for (int i = 0; i < services.length; i++) {
				Service s = services[i].getService();
				if (s.getRecurringChargeFrequency() == ServiceSummary.PAYMENT_FREQUENCY_MONTH) {
					if ((includeShareableServices && s.isSharable()) || (includeNonShareableServices && !s.isSharable())) {
						if (s.hasAlternateRecurringCharge()) {

							// get alternateRecurringChargeStartDate lazily
							if (alternateRecurringChargeStartDate == null) {
								alternateRecurringChargeStartDate = changeContext.getRefDataHelper().retrieveAlternateRCContractStartDate(subscriber.getMarketProvince());
							}

							if (getEffectiveDate() == null || alternateRecurringChargeStartDate == null || !getEffectiveDate().before(alternateRecurringChargeStartDate)) {
								charge += getServiceAlternateRecurringCharge((ServiceInfo) s);
							} else {
								charge += s.getRecurringCharge();
							}
						} else {
							charge += s.getRecurringCharge();
						}
					}
				}
			}
			return charge;
		} catch (TelusException e) {
			throw new TelusAPIException(e);
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	public double getServiceAlternateRecurringCharge(ServiceInfo serviceInfo) throws TelusException, ApplicationException, TelusAPIException {
		return changeContext.getRefDataHelper().retrieveAlternateRecurringCharge(serviceInfo, subscriber.getMarketProvince(), subscriber.getPhoneNumber().substring(0, 3),
				subscriber.getPhoneNumber().substring(3, 6), subscriber.getAccount().getCorporateId());
	}

	public boolean isSuppressPricePlanRecurringCharge() throws TelusAPIException {
		return isShareablePricePlanSecondary();
	}

	public double getServiceRecurringCharge(ServiceInfo service) {
		// Check if an alternate recurring charge exists, if so, use it.
		// Otherwise
		// use the regular recurring charge
		try {
			if (service.hasAlternateRecurringCharge())
				return getServiceAlternateRecurringCharge(service);

			return service.getRecurringCharge();
		} catch (Exception e) {
			//Log.debug("AccountView.getServiceRecurringCharge(): Error getting alternate recurring charge.");
		}

		return service.getRecurringCharge();
	}

}
