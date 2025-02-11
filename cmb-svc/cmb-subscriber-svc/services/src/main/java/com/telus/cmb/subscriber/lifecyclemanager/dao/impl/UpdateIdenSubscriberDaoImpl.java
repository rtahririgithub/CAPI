package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.AdditionalMsisdnFtrInfo;
import amdocs.APILink.datatypes.ContractInfo;
import amdocs.APILink.datatypes.IdenEquipmentInfo;
import amdocs.APILink.datatypes.IdenResourceAllocationInfo;
import amdocs.APILink.datatypes.ManualFleetInfo;
import amdocs.APILink.datatypes.NgpNmbInfo;
import amdocs.APILink.datatypes.ProductServicesInfo;
import amdocs.APILink.datatypes.SearchAdditionalMsisdn;
import amdocs.APILink.datatypes.ServiceFeatureInfo;
import amdocs.APILink.datatypes.ServiceInfo;
import amdocs.APILink.datatypes.SocInfo;
import amdocs.APILink.datatypes.UrbanFleetId;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.IdenResourceServices;
import amdocs.APILink.sessions.interfaces.SearchServices;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;
import amdocs.APILink.sessions.interfaces.UpdateIdenConv;
import amdocs.APILink.sessions.interfaces.UpdateProductConv;
import amdocs.enjutil.exceptions.ValidationException;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.fleet.Fleet;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdateIdenPcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdateIdenSubscriberDao;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusApplicationException;
import com.telus.eas.framework.info.ExceptionInfo;
import com.telus.eas.subscriber.info.AdditionalMsiSdnFtrInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.SearchResultByMsiSdn;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.NumberGroupInfo;

@Deprecated
public class UpdateIdenSubscriberDaoImpl extends UpdateSubscriberDaoImpl
implements UpdateIdenSubscriberDao, UpdateIdenPcsSubscriberDao {

	private static final Logger LOGGER = Logger.getLogger(UpdateIdenSubscriberDaoImpl.class);

	private Class<UpdateIdenConv> updateIdenConv = UpdateIdenConv.class;

	IdenSubscriberDaoHelperImpl idenSubscriberDaoHelper;

	public void setIdenSubscriberDaoHelper(
			IdenSubscriberDaoHelperImpl idenSubscriberDaoHelper) {
		this.idenSubscriberDaoHelper = idenSubscriberDaoHelper;
	}

	@Override
	public String[] retrieveAvailablePhoneNumbers(int ban, String subscriberId,
			PhoneNumberReservationInfo phoneNumberReservation, int maxNumbers,
			String sessionId) throws ApplicationException {
		return idenSubscriberDaoHelper.retrieveAvailablePhoneNumbers(ban, phoneNumberReservation, maxNumbers, sessionId);
	}

	@Override
	public void changePhoneNumber(final SubscriberInfo subscriberInfo
			, final AvailablePhoneNumberInfo newPhoneNumber
			, final String reasonCode
			, final String dealerCode
			, final String salesRepCode, String sessionId)
	throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {

				IdenResourceAllocationInfo idenResourceAllocationInfo = new IdenResourceAllocationInfo();
				String[] phoneNumbersNonAsian = null;
				String[] phoneNumbersAsian = null;
				amdocs.APILink.datatypes.FleetInfo amdocsFleetInfo = new amdocs.APILink.datatypes.FleetInfo();

				IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
				idenSubscriberInfo = (IDENSubscriberInfo)subscriberInfo;

				boolean isPtnBasedFleet;

				ManualFleetInfo manualFleetInfo = new ManualFleetInfo();
				manualFleetInfo.urbanId = Integer.parseInt(newPhoneNumber.getPhoneNumber().substring(0, 3));
				manualFleetInfo.fleetId = Integer.parseInt(newPhoneNumber.getPhoneNumber().substring(3, 6));

				//defect PROD00102807 fix, see method for detail
				isPtnBasedFleet = isPTNBasedFleet(idenSubscriberInfo);


				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);
				IdenResourceServices amdocsIdenResourceServices = transactionContext.createBean(IdenResourceServices.class);					
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);

				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateIdenConv.setProductPK(idenSubscriberInfo.getBanId(), idenSubscriberInfo.getSubscriberId());

				// Check that phone number is available
				LOGGER.debug("calling getAvailablePtnList()...");
				phoneNumbersNonAsian = amdocsIdenResourceServices.getAvailablePtnList(
						newPhoneNumber.getNumberGroup().getNumberLocation(),
						newPhoneNumber.getNumberGroup().getCode(), "0000000000",
						newPhoneNumber.getPhoneNumber(), false);
				phoneNumbersAsian = amdocsIdenResourceServices.getAvailablePtnList(
						newPhoneNumber.getNumberGroup().getNumberLocation(),
						newPhoneNumber.getNumberGroup().getCode(), "0000000000",
						newPhoneNumber.getPhoneNumber(), true);
				if ((phoneNumbersNonAsian == null || phoneNumbersNonAsian.length == 0)&&
						(phoneNumbersAsian == null || phoneNumbersAsian.length == 0)) {
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.PHONE_NUMBER_NOT_AVAILABLE, "The supplied phone number is not available.[" +newPhoneNumber.getPhoneNumber() + "]", "");
				}

				// Populate IdenResourceAllocationInfo
				idenResourceAllocationInfo.ptnTransactionType = IdenResourceAllocationInfo.CHANGE_RESOURCE_CHANGE;
				idenResourceAllocationInfo.ptnMethod = IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_MANUAL;
				idenResourceAllocationInfo.ptn = newPhoneNumber.getPhoneNumber();
				idenResourceAllocationInfo.isPortActivity = false;

				// Member ID or entire UFMI might have to be changed for subscriber on PTN-based fleet
				if (isPtnBasedFleet) {

					// different npa/nxx -> different fleet
					if (!newPhoneNumber.getPhoneNumber().substring(0,6).equals(idenSubscriberInfo.getPhoneNumber().substring(0,6))) {

						// Set BanPK (which also retrieves the BAN)
						amdocsUpdateBanConv.setBanPK(idenSubscriberInfo.getBanId());

						// set network
						LOGGER.debug("calling setNetwork() to " + newPhoneNumber.getNumberGroup0().getNetworkId() + "...");
						amdocsUpdateBanConv.setNetwork((short)newPhoneNumber.getNumberGroup0().getNetworkId());

						// get primary number group and set it
						// (at Telus, each network should have 1 and only 1 primary numbergroup)
						NgpNmbInfo[] amdocsNgpNmbInfo = amdocsUpdateBanConv.getNumberGroupList();
						if (amdocsNgpNmbInfo == null || amdocsNgpNmbInfo.length != 1)
							throw new ApplicationException(SystemCodes.CMB_SLM_DAO
									, ErrorCodes.FAILED_TO_GET_PRIMARY_NGP_FOR_NETWORK
									, "Failed to get primary NGP for network: " + idenSubscriberInfo.getNumberGroup().getNetworkId() + " !"
									, "");
						LOGGER.debug("Calling setNumberGroup() for numberGroup: " + amdocsNgpNmbInfo[0].numberGroup + "...");
						amdocsUpdateBanConv.setNumberGroup(amdocsNgpNmbInfo[0].numberGroup);

						// call createFleet, which will create the fleet if necessary and
						// also associate the fleet to the BAN
						manualFleetInfo.fleetAlias = manualFleetInfo.urbanId + "*" + manualFleetInfo.fleetId;
						LOGGER.debug("calling createFleet() for " + manualFleetInfo.fleetAlias + "...");
						amdocsFleetInfo = amdocsUpdateBanConv.createFleet(manualFleetInfo);

						idenSubscriberInfo.setFleetClass(AttributeTranslator.stringFrombyte(amdocsFleetInfo.fleetClass));
					}

					// increase 'expected # of subscribers'
					// - needs to be done because old member id is put in 'Aging' status and still counted as subscriber
					DaoSupport.increaseExpectedNumberOfSubscribers(amdocsUpdateBanConv, idenSubscriberInfo.getBanId(), manualFleetInfo.urbanId, manualFleetInfo.fleetId);

					// Set ProductPK (which also retrieves the BAN)
					amdocsUpdateIdenConv.setProductPK(idenSubscriberInfo.getBanId(),idenSubscriberInfo.getSubscriberId());

					// call setFleet() to tell changeResource() that the fleet has been changed
					amdocs.APILink.datatypes.FleetInfo fleetInfo = new amdocs.APILink.datatypes.FleetInfo();
					fleetInfo.urbanId = Integer.parseInt(newPhoneNumber.getPhoneNumber().substring(0, 3));
					fleetInfo.fleetId = Integer.parseInt(newPhoneNumber.getPhoneNumber().substring(3, 6));

					LOGGER.debug("calling setFleet() for " + fleetInfo.urbanId + "*" + fleetInfo.fleetId + "...");
					amdocsUpdateIdenConv.setFleet(fleetInfo);

					idenResourceAllocationInfo.ufmiTransactionType = IdenResourceAllocationInfo.CHANGE_RESOURCE_CHANGE;
					idenResourceAllocationInfo.ufmiMethod = IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_MANUAL;
					idenResourceAllocationInfo.memberId = Integer.parseInt(newPhoneNumber.getPhoneNumber().substring(6, 10));
				}

				// set number group
				LOGGER.debug("calling setNumberGroup() to " + newPhoneNumber.getNumberGroup0().getCode() + "...");
				amdocsUpdateIdenConv.setNumberGroup(newPhoneNumber.getNumberGroup0().getCode());

				// Change Phone number and maybe UFMI
				LOGGER.debug("calling changeResource()...");
				LOGGER.debug("idenResourceAllocationInfo being passed in: ");
				LOGGER.debug("  ptnTransactionType: " + idenResourceAllocationInfo.ptnTransactionType);
				LOGGER.debug("  ptnMethod: " + idenResourceAllocationInfo.ptnMethod);
				LOGGER.debug("  ptn: " + idenResourceAllocationInfo.ptn);
				LOGGER.debug("  ufmiTransactionType: " + idenResourceAllocationInfo.ufmiTransactionType);
				LOGGER.debug("  ufmiMethod: " + idenResourceAllocationInfo.ufmiMethod);
				LOGGER.debug("  memberId: " + idenResourceAllocationInfo.memberId);

				if (dealerCode!=null) {
					ContractInfo ci = new ContractInfo();
					if (salesRepCode == null) {
						ci.salesCode = "0000"; 
					} else {
						ci.salesCode = salesRepCode;
					}
					ci.dealerCode = dealerCode;
					ci.salesCode = salesRepCode;

					amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo,null,ci);
				} else {
					amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo);
				}
				LOGGER.debug("Leaving...");

				return null;
			}
		});
	}

	private boolean isPTNBasedFleet(IDENSubscriberInfo idenSubscriberInfo) {
		//added first condition for defect PROD00102807 fix:
		//a subscribe could have PTN based fleet and later on removed UFMI feature, in this case, resource status will be 'C'
		return "C".equals(idenSubscriberInfo.getMemberIdentity0().getResourceStatus())==false
		&& idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getUrbanId() == Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(0, 3))
		&& idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getFleetId() == Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(3, 6));
	}

	@Override
	public void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan,
			Date activityDate, boolean transferOwnership,
			String activityReasonCode, String userMemoText, String dealerCode,
			String salesRepCode, String sessionId) throws ApplicationException {
		super.moveSubscriber(updateIdenConv, subscriberInfo, targetBan, activityDate, transferOwnership, activityReasonCode, userMemoText, dealerCode, salesRepCode, sessionId);		
	}

	@Override
	public void moveSubscriber(final SubscriberInfo subscriberInfo, final int targetBan,
			final Date activityDate, final boolean transferOwnership,
			final String activityReasonCode, final String userMemoText, final String dealerCode,
			final String salesRepCode, final FleetInfo fleetInfo, final String sessionId)
	throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				boolean isPtnBasedFleet = false;
				amdocs.APILink.datatypes.FleetInfo amdocsFleetInfo = new amdocs.APILink.datatypes.FleetInfo();

				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);

				// TOWN for subscriber with UFMI
				// - if ptn-based ufmi, create/associate fleet to new account
				if (transferOwnership && fleetInfo != null) {

					int npa = Integer.parseInt(subscriberInfo.getPhoneNumber().substring(0, 3));
					int nxx = Integer.parseInt(subscriberInfo.getPhoneNumber().substring(3, 6));
					isPtnBasedFleet = fleetInfo.getIdentity().getUrbanId() == npa && fleetInfo.getIdentity().getFleetId() == nxx;

					// PTN-based fleet
					// - fleet might not be created yet, therefore we need to create and/or associate
					// fleet to BAN
					if (isPtnBasedFleet) {

						// Set BanPK (which also retrieves the BAN)
						LOGGER.debug("Calling setBanPK() for ban: " + targetBan + "...");
						amdocsUpdateBanConv.setBanPK(targetBan);

						// call createFleet, which will create the fleet if necessary and
						// also associate the fleet to the BAN
						// (setting network and numbergroup is required)
						LOGGER.debug("Calling setNetwork() for network: " + subscriberInfo.getNumberGroup().getNetworkId() + "...");
						amdocsUpdateBanConv.setNetwork((short)subscriberInfo.getNumberGroup().getNetworkId());

						// get primary number group and set it
						// (at Telus, each network should have 1 and only 1 primary numbergroup)
						NgpNmbInfo[] amdocsNgpNmbInfo = amdocsUpdateBanConv.getNumberGroupList();
						if (amdocsNgpNmbInfo == null || amdocsNgpNmbInfo.length != 1)
							throw new ApplicationException(SystemCodes.CMB_SLM_DAO
									, ErrorCodes.FAILED_TO_GET_PRIMARY_NGP_FOR_NETWORK
									, "Failed to get primary NGP for network: " + subscriberInfo.getNumberGroup().getNetworkId() + " !"
									, "");
						LOGGER.debug("Calling setNumberGroup() for numberGroup: " + amdocsNgpNmbInfo[0].numberGroup + "...");
						amdocsUpdateBanConv.setNumberGroup(amdocsNgpNmbInfo[0].numberGroup);

						ManualFleetInfo manualFleetInfo = new ManualFleetInfo();
						manualFleetInfo.urbanId = fleetInfo.getIdentity().getUrbanId();
						manualFleetInfo.fleetId = fleetInfo.getIdentity().getFleetId();
						manualFleetInfo.fleetAlias = fleetInfo.getIdentity().getUrbanId() + "*" + fleetInfo.getIdentity().getFleetId();
						LOGGER.debug("Calling createFleet() for fleet: " + manualFleetInfo.urbanId + "*" + manualFleetInfo.fleetId + "...");
						amdocsFleetInfo = amdocsUpdateBanConv.createFleet(manualFleetInfo);

						((IDENSubscriberInfo)subscriberInfo).setFleetClass(AttributeTranslator.stringFrombyte(amdocsFleetInfo.fleetClass));
					}
				}
				// MOVE SUBSCRIBER for subscriber with UFMI
				// - associate fleet to new account (class-based AND ptn-based)
				if (!transferOwnership && fleetInfo != null) {
					try {
						// Set BanPK (which also retrieves the BAN)
						amdocsUpdateBanConv.setBanPK(targetBan);

						// populate UrbanFleetId
						UrbanFleetId amdocsUrbanFleetId = new UrbanFleetId();
						amdocsUrbanFleetId.urbanId = fleetInfo.getIdentity().getUrbanId();
						amdocsUrbanFleetId.fleetId = fleetInfo.getIdentity().getFleetId();
						amdocsUrbanFleetId.expectedSubscriberNumber = 1;
						amdocsUrbanFleetId.expectedTalkGroupNumber = 0;

						// associate exiting fleet
						boolean isPublicFleet = fleetInfo.getType() == Fleet.TYPE_PUBLIC;
						LOGGER.debug("Excecuting associateFleet() - start...");
						if (isPublicFleet)
							amdocsUpdateBanConv.associateFleet(amdocsUrbanFleetId);
						else
							amdocsUpdateBanConv.associateFleet(amdocsUrbanFleetId, "");
						LOGGER.debug("Excecuting associateFleet() - end...");
					} catch (ValidateException ve) {
						if (ve.getErrorInd() == 1111480) {
							LOGGER.debug ("moveSubscriber() Ignoring exception - Fleet is already associated to this BAN.");
						}else {
							throw ve;
						}
					}
				}

				moveSubscriber(updateIdenConv, subscriberInfo, targetBan, activityDate, transferOwnership, activityReasonCode, userMemoText, dealerCode, salesRepCode, sessionId);

				return null;
			}

		});
	}

	@Override
	public void cancelAdditionalMsisdn(
			final AdditionalMsiSdnFtrInfo[] additionalMsiSdnFtrInfo,
			final String additionalMsisdn, String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {

				IdenResourceServices amdocsIdenResourceServices = transactionContext.createBean(IdenResourceServices.class);	

				AdditionalMsisdnFtrInfo[] amdocsDataType = new AdditionalMsisdnFtrInfo[additionalMsiSdnFtrInfo.length];

				Vector<AdditionalMsisdnFtrInfo> ftrList = new Vector<AdditionalMsisdnFtrInfo>();
				for (int i = 0; i < additionalMsiSdnFtrInfo.length; i++){
					AdditionalMsisdnFtrInfo tmp = new AdditionalMsisdnFtrInfo();
					tmp.ban = additionalMsiSdnFtrInfo[i].getBan();
					tmp.ftrSocVerNo =	additionalMsiSdnFtrInfo[i].getFtrSocVerNo();
					tmp.feature = additionalMsiSdnFtrInfo[i].getFeature();
					tmp.ftrEffDate = additionalMsiSdnFtrInfo[i].getFtrEffDate();
					tmp.ftrExpDate = additionalMsiSdnFtrInfo[i].getFtrExpDate();
					tmp.ftrTrxId = additionalMsiSdnFtrInfo[i].getFtrTrxId();
					tmp.servFtrSeqNo = additionalMsiSdnFtrInfo[i].getServFtrSeqNo();
					tmp.soc = additionalMsiSdnFtrInfo[i].getSoc();
					tmp.socSeqNo = additionalMsiSdnFtrInfo[i].getSocSeqNo();
					tmp.subscriberNumber = additionalMsiSdnFtrInfo[i].getSubscriberNumber();
					ftrList.add(tmp);
				}
				ftrList.copyInto(amdocsDataType);
				amdocsIdenResourceServices.portOutCancelledMsisdn(amdocsDataType,additionalMsisdn);
				return null;
			}
		});		
	}


	@Override
	public void cancelPortedInSubscriber(
			int ban, 
			String subscriberId,
			String deactivationReason, 
			Date activityDate, 
			String portOutInd,
			boolean isBrandPort, 
			String sessionId) throws ApplicationException {
		
		super.cancelPortedInSubscriber(updateIdenConv, ban, subscriberId, deactivationReason, activityDate, portOutInd, isBrandPort, sessionId);
	}

	@Override
	public void changeAdditionalPhoneNumbers(final int ban, final String subscriberId,
			final String primaryPhoneNumber, final NumberGroupInfo numberGroup,
			final boolean portIn, String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);	

				Collection<ServiceInfo> updatedServices = new ArrayList<ServiceInfo>();
				ServiceInfo[] serviceInfoArray = null;

				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateIdenConv.setProductPK(ban,subscriberId);

				// set number group
				amdocsUpdateIdenConv.setNumberGroup(numberGroup.getCode());

				//Look for features with phone numbers that need to be changed as well
				ProductServicesInfo productServicesInfo = amdocsUpdateIdenConv.getProductServices();

				// - Priceplan SOC
				for (int i=0; i < productServicesInfo.pricePlan.feature.length; i++) {
					if (!productServicesInfo.pricePlan.feature[i].msisdn.trim().equals("")) {
						ServiceInfo serviceInfo = new ServiceInfo();
						serviceInfo.soc.soc = productServicesInfo.pricePlan.soc.soc;
						serviceInfo.soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;
						serviceInfo.feature = new amdocs.APILink.datatypes.ServiceFeatureInfo[1];
						serviceInfo.feature[0] = new amdocs.APILink.datatypes.ServiceFeatureInfo();
						serviceInfo.feature[0].featureCode = productServicesInfo.pricePlan.feature[i].featureCode;
						serviceInfo.feature[0].transactionType = amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE;
						if (!portIn)
							serviceInfo.feature[0].msisdn = amdocsUpdateIdenConv.reserveAdditionalResource(primaryPhoneNumber.substring(0,3),primaryPhoneNumber.substring(3,6));
						else
							serviceInfo.feature[0].msisdn = getMsisdn(amdocsUpdateIdenConv, numberGroup);
						updatedServices.add(serviceInfo);
					}
				}
				// - Additional services
				for (int i=0; i < productServicesInfo.addtnlSrvs.length; i++) {
					for (int j=0; j < productServicesInfo.addtnlSrvs[i].feature.length; j++) {
						if (!productServicesInfo.addtnlSrvs[i].feature[j].msisdn.trim().equals("")) {
							ServiceInfo serviceInfo = new ServiceInfo();
							serviceInfo.soc.soc = productServicesInfo.addtnlSrvs[i].soc.soc;
							serviceInfo.soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;
							serviceInfo.feature = new amdocs.APILink.datatypes.ServiceFeatureInfo[1];
							serviceInfo.feature[0] = new amdocs.APILink.datatypes.ServiceFeatureInfo();
							serviceInfo.feature[0].featureCode = productServicesInfo.addtnlSrvs[i].feature[j].featureCode;
							serviceInfo.feature[0].transactionType = amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE;
							if (!portIn)
								serviceInfo.feature[0].msisdn = amdocsUpdateIdenConv.reserveAdditionalResource(primaryPhoneNumber.substring(0,3),primaryPhoneNumber.substring(3,6));
							else
								serviceInfo.feature[0].msisdn = getMsisdn(amdocsUpdateIdenConv, numberGroup);
							updatedServices.add(serviceInfo);
						}
					}
				}
				// - Promotional services
				for (int i=0; i < productServicesInfo.promPricePlan.length; i++) {
					for (int j=0; j < productServicesInfo.promPricePlan[i].feature.length; j++) {
						if (!productServicesInfo.promPricePlan[i].feature[j].msisdn.trim().equals("")) {
							ServiceInfo serviceInfo = new ServiceInfo();
							serviceInfo.soc.soc = productServicesInfo.promPricePlan[i].soc.soc;
							serviceInfo.soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;
							serviceInfo.feature = new amdocs.APILink.datatypes.ServiceFeatureInfo[1];
							serviceInfo.feature[0] = new amdocs.APILink.datatypes.ServiceFeatureInfo();
							serviceInfo.feature[0].featureCode = productServicesInfo.promPricePlan[i].feature[j].featureCode;
							serviceInfo.feature[0].transactionType = amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE;
							if (!portIn) {
								serviceInfo.feature[0].msisdn = amdocsUpdateIdenConv.reserveAdditionalResource(primaryPhoneNumber.substring(0,3),primaryPhoneNumber.substring(3,6));
							} else {
								serviceInfo.feature[0].msisdn = getMsisdn(amdocsUpdateIdenConv, numberGroup);
							}
							updatedServices.add(serviceInfo);
						}
					}
				}

				// call changeServiceAgreement if additional phone numbers found
				if (updatedServices.size() > 0) {
					serviceInfoArray = new ServiceInfo[updatedServices.size()];
					serviceInfoArray = (ServiceInfo[])updatedServices.toArray(serviceInfoArray);

					// print serviceInfo
					LOGGER.debug("services passed into:");
					for (int i=0; i < serviceInfoArray.length; i++) {
						LOGGER.debug("   SOC    : " + serviceInfoArray[i].soc.soc + " transactionType:" + AttributeTranslator.stringFrombyte(serviceInfoArray[i].soc.transactionType));
						if (serviceInfoArray[i].feature != null) {
							for (int j=0; j < serviceInfoArray[i].feature.length; j++) {
								LOGGER.debug("     Feature: " + serviceInfoArray[i].feature[j].featureCode + " transactionType:" + AttributeTranslator.stringFrombyte(serviceInfoArray[i].feature[j].transactionType) + " ftrParam:" + serviceInfoArray[i].feature[j].ftrParam  + " msisdn:" + serviceInfoArray[i].feature[j].msisdn);
							}
						}
					}
					LOGGER.debug("calling changeServiceAgreement()...");
					amdocsUpdateIdenConv.changeServiceAgreement(serviceInfoArray);
				}

				return null;
			}
		});		
	}

	private String getMsisdn(UpdateIdenConv amdocsUpdateIdenConv, NumberGroupInfo pNumberGroup) throws ValidationException, RemoteException, ApplicationException {

		String msisdn = "";
		String[] npanxx = pNumberGroup.getNpaNXX();

		if (npanxx != null && npanxx.length > 0) {
			for (int i = 0; i < npanxx.length; i++) {
				String npa = npanxx[i].substring(0,3);
				String nxx = npanxx[i].substring(3,6);
				try {
					msisdn = amdocsUpdateIdenConv.reserveAdditionalResource(npa,nxx);
					break;			 
				} catch(ValidateException ve) {
					if ( (i+1) < npanxx.length )
						continue;
					else
						throw ve;
				}
			}
		} else {
			throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.GENERIC_THROWABLE_ERROR_CODE, "No NPA NXX available for MSISDN reservation", "");
		}

		return msisdn;
	}

	@Override
	public void changeFaxNumber(final int ban, final String subscriberId,
			final String newFaxNumber, final NumberGroupInfo numberGroup,
			final boolean isPortedInNumber, String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);	

				Collection<ServiceInfo> updatedServices = new ArrayList<ServiceInfo>();
				ServiceInfo[] serviceInfoArray = null;

				// set ProductPK (which also retrieves the BAN)
				amdocsUpdateIdenConv.setProductPK(ban, subscriberId);

				// set the number group
				amdocsUpdateIdenConv.setNumberGroup(numberGroup.getCode());

				// get the subscriber's current set of services
				ProductServicesInfo productServicesInfo = amdocsUpdateIdenConv.getProductServices();

				boolean faxFeatureFound = false;

				// iterate through the priceplan SOC features to find the fax feature
				if (!faxFeatureFound) {
					for (int i = 0; i < productServicesInfo.pricePlan.feature.length; i++) {
						// check if the msisdn number is not empty and validate that the feature is a fax feature
						if ((!productServicesInfo.pricePlan.feature[i].msisdn.trim().equals("")) && 
								(productServicesInfo.pricePlan.feature[i].featureCode.equals("MFAXM"))) {
							ServiceInfo serviceInfo = new ServiceInfo();
							serviceInfo.soc.soc = productServicesInfo.pricePlan.soc.soc;
							serviceInfo.soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;
							serviceInfo.feature = new amdocs.APILink.datatypes.ServiceFeatureInfo[1];
							serviceInfo.feature[0] = new amdocs.APILink.datatypes.ServiceFeatureInfo();
							serviceInfo.feature[0].featureCode = productServicesInfo.pricePlan.feature[i].featureCode;
							serviceInfo.feature[0].transactionType = amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE;

							if (!isPortedInNumber)
								serviceInfo.feature[0].msisdn = amdocsUpdateIdenConv.reserveAdditionalResource(newFaxNumber);
							else
								serviceInfo.feature[0].msisdn = getMsisdn(amdocsUpdateIdenConv, numberGroup);

							updatedServices.add(serviceInfo);
							faxFeatureFound = true;
							break;
						}
					}
				}

				// iterate through the additional services features to find the fax feature
				if (!faxFeatureFound) {
					for (int i = 0; i < productServicesInfo.addtnlSrvs.length; i++) {
						for (int j = 0; j < productServicesInfo.addtnlSrvs[i].feature.length; j++) {
							// check if the msisdn number is not empty and validate that the feature is a fax feature
							if ((!productServicesInfo.addtnlSrvs[i].feature[j].msisdn.trim().equals("")) &&
									(productServicesInfo.addtnlSrvs[i].feature[j].featureCode.equals("MFAXM"))) {
								ServiceInfo serviceInfo = new ServiceInfo();
								serviceInfo.soc.soc = productServicesInfo.addtnlSrvs[i].soc.soc;
								serviceInfo.soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;
								serviceInfo.feature = new amdocs.APILink.datatypes.ServiceFeatureInfo[1];
								serviceInfo.feature[0] = new amdocs.APILink.datatypes.ServiceFeatureInfo();
								serviceInfo.feature[0].featureCode = productServicesInfo.addtnlSrvs[i].feature[j].featureCode;
								serviceInfo.feature[0].transactionType = amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE;

								if (!isPortedInNumber)
									serviceInfo.feature[0].msisdn = amdocsUpdateIdenConv.reserveAdditionalResource(newFaxNumber);
								else
									serviceInfo.feature[0].msisdn = getMsisdn(amdocsUpdateIdenConv, numberGroup);

								updatedServices.add(serviceInfo);
								faxFeatureFound = true;
								break;
							}
						}
					}
				}		  

				// iterate through the promotional services features to find the fax feature
				if (!faxFeatureFound) {
					for (int i = 0; i < productServicesInfo.promPricePlan.length; i++) {
						for (int j = 0; j < productServicesInfo.promPricePlan[i].feature.length; j++) {
							// check if the msisdn number is not empty and validate that the feature is a fax feature
							if ((!productServicesInfo.promPricePlan[i].feature[j].msisdn.trim().equals("")) &&
									(productServicesInfo.promPricePlan[i].feature[j].featureCode.equals("MFAXM"))) {
								ServiceInfo serviceInfo = new ServiceInfo();
								serviceInfo.soc.soc = productServicesInfo.promPricePlan[i].soc.soc;
								serviceInfo.soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;
								serviceInfo.feature = new amdocs.APILink.datatypes.ServiceFeatureInfo[1];
								serviceInfo.feature[0] = new amdocs.APILink.datatypes.ServiceFeatureInfo();
								serviceInfo.feature[0].featureCode = productServicesInfo.promPricePlan[i].feature[j].featureCode;
								serviceInfo.feature[0].transactionType = amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE;

								if (!isPortedInNumber)
									serviceInfo.feature[0].msisdn = amdocsUpdateIdenConv.reserveAdditionalResource(newFaxNumber);
								else			
									serviceInfo.feature[0].msisdn = getMsisdn(amdocsUpdateIdenConv, numberGroup);

								updatedServices.add(serviceInfo);
								faxFeatureFound = true;
								break;
							}
						}
					}
				}

				// call changeServiceAgreement if the fax number is changed
				if (updatedServices.size() > 0) {
					serviceInfoArray = new ServiceInfo[updatedServices.size()];
					serviceInfoArray = (ServiceInfo[])updatedServices.toArray(serviceInfoArray);

					// print serviceInfo
					LOGGER.debug("Updated services array: ");
					for (int i = 0; i < serviceInfoArray.length; i++) {
						LOGGER.debug("   SOC    : " + serviceInfoArray[i].soc.soc + " transactionType:" 
								+ AttributeTranslator.stringFrombyte(serviceInfoArray[i].soc.transactionType));
						if (serviceInfoArray[i].feature != null) {
							for (int j = 0; j < serviceInfoArray[i].feature.length; j++) {
								LOGGER.debug("     Feature: " + serviceInfoArray[i].feature[j].featureCode + " transactionType:" 
										+ AttributeTranslator.stringFrombyte(serviceInfoArray[i].feature[j].transactionType) 
										+ " ftrParam:" + serviceInfoArray[i].feature[j].ftrParam  + " msisdn:" 
										+ serviceInfoArray[i].feature[j].msisdn);
							}
						}
					}
					LOGGER.debug("Calling changeServiceAgreement()...");
					amdocsUpdateIdenConv.changeServiceAgreement(serviceInfoArray);
				} else {
					LOGGER.debug("TelusValidationException occurred (no fax feature found)");
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.NO_FAX_FEATURE_FOUND, "No fax feature found", "");
				}
				return null;
			}
		});
	}

	@Override
	public void changeIMSI(final int ban, final String subscriberId, String sessionId)
	throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {

				IdenResourceAllocationInfo idenResourceAllocationInfo = new IdenResourceAllocationInfo();
				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);

				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateIdenConv.setProductPK(ban,subscriberId);

				// Populate IdenResourceAllocationInfo
				idenResourceAllocationInfo.imsiTransactionType = IdenResourceAllocationInfo.CHANGE_RESOURCE_CHANGE;
				idenResourceAllocationInfo.imsiMethod = IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_RANDOM;

				// Change IMSI
				LOGGER.debug("calling changeResource()...");
				amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo);

				LOGGER.debug("Leaving...");
				return null;				
			}

		});
		// TODO Auto-generated method stub

	}

	@Override
	public void changeIP(final int ban, final String subscriberId, final String newIp,
			final String newIpType, final String newIpCorpCode, String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				String newIpTrimmed = newIp == null ? "" : newIp.trim();

				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);
				IdenResourceAllocationInfo idenResourceAllocationInfo = new IdenResourceAllocationInfo();


				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateIdenConv.setProductPK(ban,subscriberId);

				// Populate IdenResourceAllocationInfo
				idenResourceAllocationInfo.ipTransactionType = IdenResourceAllocationInfo.CHANGE_RESOURCE_CHANGE;
				idenResourceAllocationInfo.ip = newIpTrimmed;
				idenResourceAllocationInfo.corpCode = newIpCorpCode;
				idenResourceAllocationInfo.ipMethod = newIpTrimmed.equals("") ?
						IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_RANDOM :
							IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_MANUAL;
				idenResourceAllocationInfo.ipType = newIpType.equals("C") ?
						IdenResourceAllocationInfo.IP_TYPE_CORPORATE : newIpType.equals("B") ?
								IdenResourceAllocationInfo.IP_TYPE_PUBLIC :
									IdenResourceAllocationInfo.IP_TYPE_PRIVATE;

				// Change IP
				LOGGER.debug("calling changeResource()...");
				amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo);

				return null;
			}
		});

	}

	@Override
	public void reserveAdditionalPhoneNumber(final int ban, final String subscriberId,
			final NumberGroupInfo numberGroup, final String additionalPhoneNumber,
			String sessionId) throws ApplicationException {

		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);
				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateIdenConv.setProductPK(ban,subscriberId);

				// set number group
				amdocsUpdateIdenConv.setNumberGroup(numberGroup.getCode());

				// Reserve additional Phone number
				LOGGER.debug("calling reserveAdditionalResource()...");
				amdocsUpdateIdenConv.reserveAdditionalResource(additionalPhoneNumber);

				return null;

			}
		});
	}

	@Override
	public SearchResultByMsiSdn searchSubscriberByAdditionalMsiSdn(
			final String additionalMsisdn, String sessionId)
	throws ApplicationException {
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<SearchResultByMsiSdn>() {
			@Override
			public SearchResultByMsiSdn doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {

				SearchServices amdocsSearchServices = transactionContext.createBean(SearchServices.class);

				SearchResultByMsiSdn ret = new SearchResultByMsiSdn();

				SearchAdditionalMsisdn amdocsDataType = amdocsSearchServices.searchSubscriberByAdditionalMsisdn(additionalMsisdn);
				if (amdocsDataType.effFtr != null){
					AdditionalMsiSdnFtrInfo tmpActiveFtrInfo  = new AdditionalMsiSdnFtrInfo();
					tmpActiveFtrInfo.setBan(amdocsDataType.effFtr.ban);
					tmpActiveFtrInfo.setFeature(amdocsDataType.effFtr.feature);
					tmpActiveFtrInfo.setFtrEffDate(amdocsDataType.effFtr.ftrEffDate);
					tmpActiveFtrInfo.setFtrExpDate(amdocsDataType.effFtr.ftrExpDate);
					tmpActiveFtrInfo.setFtrSocVerNo(amdocsDataType.effFtr.ftrSocVerNo);
					tmpActiveFtrInfo.setFtrTrxId(amdocsDataType.effFtr.ftrTrxId);
					tmpActiveFtrInfo.setServFtrSeqNo(amdocsDataType.effFtr.servFtrSeqNo);
					tmpActiveFtrInfo.setSoc(amdocsDataType.effFtr.soc);
					tmpActiveFtrInfo.setSocSeqNo(amdocsDataType.effFtr.socSeqNo);
					tmpActiveFtrInfo.setSubscriberNumber(amdocsDataType.effFtr.subscriberNumber);
					ret.setEffectiveFtr(tmpActiveFtrInfo);
				}
				if(amdocsDataType.allFtr != null && amdocsDataType.allFtr.length > 0){
					AdditionalMsiSdnFtrInfo[] tmpAllFtrInfo  = new AdditionalMsiSdnFtrInfo[amdocsDataType.allFtr.length];
					Vector<AdditionalMsiSdnFtrInfo> ftrList = new Vector<AdditionalMsiSdnFtrInfo>();
					for (int i = 0; i < amdocsDataType.allFtr.length; i++){
						AdditionalMsiSdnFtrInfo tmp = new AdditionalMsiSdnFtrInfo();
						tmp.setBan(amdocsDataType.allFtr[i].ban);
						tmp.setFeature(amdocsDataType.allFtr[i].feature);
						tmp.setFtrEffDate(amdocsDataType.allFtr[i].ftrEffDate);
						tmp.setFtrExpDate(amdocsDataType.allFtr[i].ftrExpDate);
						tmp.setFtrSocVerNo(amdocsDataType.allFtr[i].ftrSocVerNo);
						tmp.setFtrTrxId(amdocsDataType.allFtr[i].ftrTrxId);
						tmp.setServFtrSeqNo(amdocsDataType.allFtr[i].servFtrSeqNo);
						tmp.setSoc(amdocsDataType.allFtr[i].soc);
						tmp.setSocSeqNo(amdocsDataType.allFtr[i].socSeqNo);
						tmp.setSubscriberNumber(amdocsDataType.allFtr[i].subscriberNumber);
						ftrList.add(tmp);
					}
					ftrList.copyInto(tmpAllFtrInfo);
					ret.setAllFtrArray(tmpAllFtrInfo);
				}

				return ret;
			}
		});

	}

	@Override
	public void setPortTypeToPortIn(final int ban, final String subscriberId, final Date sysDate,
			String sessionId) throws ApplicationException {
		super.setPortTypeToPortIn(updateIdenConv, ban, subscriberId, sysDate, sessionId);
	}

	@Override
	public void portChangeSubscriberNumber(final SubscriberInfo subscriberInfo,
			final AvailablePhoneNumberInfo newPhoneNumber, String reasonCode,
			final String dealerCode, final String salesRepCode, String portProcessType,
			int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {				
				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				IdenResourceAllocationInfo idenResourceAllocationInfo = new IdenResourceAllocationInfo();
				amdocs.APILink.datatypes.FleetInfo amdocsFleetInfo = new amdocs.APILink.datatypes.FleetInfo();

				IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
				idenSubscriberInfo = (IDENSubscriberInfo)subscriberInfo;

				boolean isPtnBasedFleet;

				ManualFleetInfo manualFleetInfo = new ManualFleetInfo();
				manualFleetInfo.urbanId = Integer.parseInt(newPhoneNumber.getPhoneNumber().substring(0, 3));
				manualFleetInfo.fleetId = Integer.parseInt(newPhoneNumber.getPhoneNumber().substring(3, 6));

				//defect PROD00102807 fix, see method for detail
				isPtnBasedFleet = isPTNBasedFleet(idenSubscriberInfo);



				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateIdenConv.setProductPK(idenSubscriberInfo.getBanId(),idenSubscriberInfo.getSubscriberId());

				// Populate IdenResourceAllocationInfo
				idenResourceAllocationInfo.ptnTransactionType = IdenResourceAllocationInfo.CHANGE_RESOURCE_CHANGE;
				idenResourceAllocationInfo.ptnMethod = IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_MANUAL;
				idenResourceAllocationInfo.ptn = newPhoneNumber.getPhoneNumber();
				idenResourceAllocationInfo.isPortActivity = true;

				// Member ID or entire UFMI might have to be changed for subscriber on PTN-based fleet
				if (isPtnBasedFleet) {

					// different npa/nxx -> different fleet
					if (!newPhoneNumber.getPhoneNumber().substring(0,6).equals(idenSubscriberInfo.getPhoneNumber().substring(0,6))) {

						// Set BanPK (which also retrieves the BAN)
						amdocsUpdateBanConv.setBanPK(idenSubscriberInfo.getBanId());

						// set network
						LOGGER.debug("calling setNetwork() to " + newPhoneNumber.getNumberGroup0().getNetworkId() + "...");
						amdocsUpdateBanConv.setNetwork((short)newPhoneNumber.getNumberGroup0().getNetworkId());

						// get primary number group and set it
						// (at Telus, each network should have 1 and only 1 primary numbergroup)
						NgpNmbInfo[] amdocsNgpNmbInfo = amdocsUpdateBanConv.getNumberGroupList();
						if (amdocsNgpNmbInfo == null || amdocsNgpNmbInfo.length != 1)
							throw new TelusApplicationException(new ExceptionInfo("APP10012","Failed to get primary NGP for network: " 
									+ subscriberInfo.getNumberGroup().getNetworkId() + " !"));
						LOGGER.debug("Calling setNumberGroup() for numberGroup: " + amdocsNgpNmbInfo[0].numberGroup + "...");
						amdocsUpdateBanConv.setNumberGroup(amdocsNgpNmbInfo[0].numberGroup);

						// call createFleet, which will create the fleet if necessary and
						// also associate the fleet to the BAN
						manualFleetInfo.fleetAlias = manualFleetInfo.urbanId + "*" + manualFleetInfo.fleetId;
						LOGGER.debug("calling createFleet() for " + manualFleetInfo.fleetAlias + "...");
						amdocsFleetInfo = amdocsUpdateBanConv.createFleet(manualFleetInfo);

						idenSubscriberInfo.setFleetClass(AttributeTranslator.stringFrombyte(amdocsFleetInfo.fleetClass));
					}

					// increase 'expected # of subscribers'
					// - needs to be done because old member id is put in 'Aging' status and still counted as subscriber
					DaoSupport.increaseExpectedNumberOfSubscribers(amdocsUpdateBanConv, idenSubscriberInfo.getBanId(), manualFleetInfo.urbanId, manualFleetInfo.fleetId);

					// Set ProductPK (which also retrieves the BAN)
					amdocsUpdateIdenConv.setProductPK(idenSubscriberInfo.getBanId(),idenSubscriberInfo.getSubscriberId());

					// call setFleet() to tell changeResource() that the fleet has been changed
					amdocs.APILink.datatypes.FleetInfo fleetInfo = new amdocs.APILink.datatypes.FleetInfo();
					fleetInfo.urbanId = Integer.parseInt(newPhoneNumber.getPhoneNumber().substring(0, 3));
					fleetInfo.fleetId = Integer.parseInt(newPhoneNumber.getPhoneNumber().substring(3, 6));

					LOGGER.debug("calling setFleet() for " + fleetInfo.urbanId + "*" + fleetInfo.fleetId + "...");
					amdocsUpdateIdenConv.setFleet(fleetInfo);

					idenResourceAllocationInfo.ufmiTransactionType = IdenResourceAllocationInfo.CHANGE_RESOURCE_CHANGE;
					idenResourceAllocationInfo.ufmiMethod = IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_MANUAL;
					idenResourceAllocationInfo.memberId = Integer.parseInt(newPhoneNumber.getPhoneNumber().substring(6, 10));
				}

				// set number group
				LOGGER.debug("calling setNumberGroup() to " + newPhoneNumber.getNumberGroup0().getCode() + "...");
				amdocsUpdateIdenConv.setNumberGroup(newPhoneNumber.getNumberGroup0().getCode());

				// Change Phone number and maybe UFMI
				LOGGER.debug("calling changeResource()...");
				LOGGER.debug("idenResourceAllocationInfo being passed in: ");
				LOGGER.debug("  ptnTransactionType: " + idenResourceAllocationInfo.ptnTransactionType);
				LOGGER.debug("  ptnMethod: " + idenResourceAllocationInfo.ptnMethod);
				LOGGER.debug("  ptn: " + idenResourceAllocationInfo.ptn);
				LOGGER.debug("  ufmiTransactionType: " + idenResourceAllocationInfo.ufmiTransactionType);
				LOGGER.debug("  ufmiMethod: " + idenResourceAllocationInfo.ufmiMethod);
				LOGGER.debug("  memberId: " + idenResourceAllocationInfo.memberId);

				if (dealerCode!=null) {
					ContractInfo ci = new ContractInfo();

					if (salesRepCode==null) {
						ci.salesCode = "0000";
					} else {
						ci.salesCode = salesRepCode;
					}											
					ci.dealerCode = dealerCode;					

					amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo,null,ci);
				}
				else {
					amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo);
				}
				return null;
			}
		});		
	}

	@Override
	public void suspendPortedInSubscriber(int ban, String subscriberId,
			String deactivationReason, Date activityDate, String portOutInd, String sessionId)
	throws ApplicationException {
		super.suspendPortedInSubscriber(updateIdenConv, ban, subscriberId, deactivationReason, activityDate, portOutInd, sessionId);		
	}

	@Override
	public void resetVoiceMailPassword(int ban, String subscriberId,
			String[] voiceMailSocAndFeature, String sessionId)
	throws ApplicationException {
		super.resetVoiceMailPassword(updateIdenConv, ban, subscriberId, voiceMailSocAndFeature, sessionId);

	}

	@Override
	public void deleteMsisdnFeature(final AdditionalMsiSdnFtrInfo ftrInfo,
			String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				ServiceInfo[] srvInfo = new ServiceInfo[1];
				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean(updateIdenConv);

				try{					
					// Set ProductPK
					amdocsUpdateProductConv.setProductPK(ftrInfo.getBan(), ftrInfo.getSubscriberNumber());


					srvInfo[0] = new ServiceInfo();
					srvInfo[0].soc = new SocInfo();
					srvInfo[0].soc.soc = ftrInfo.getSoc();
					srvInfo[0].soc.effDate = ftrInfo.getFtrEffDate();
					srvInfo[0].soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;

					ServiceFeatureInfo[] srvFtrInfo = new ServiceFeatureInfo[1];
					srvFtrInfo[0] = new ServiceFeatureInfo();
					srvFtrInfo[0].featureCode = ftrInfo.getFeature();
					srvFtrInfo[0].transactionType = ServiceFeatureInfo.FTR_TRANSACTION_TYPE_DELETE;
					srvInfo[0].feature = srvFtrInfo;

					amdocsUpdateProductConv.changeServiceAgreement(srvInfo);
					return null;
				} catch (ValidateException ve){
					if(ve.getErrorInd() == 1110230){
						srvInfo[0].soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_DELETE;
						amdocsUpdateProductConv.changeServiceAgreement(srvInfo);
						return null;
					} else {
						throw ve;
					}
				}
			}
		});		
	}

	@Override
	public void changeSerialNumberAndMaybePricePlan(
			final SubscriberInfo subscriberInfo,
			final EquipmentInfo newPrimaryEquipmentInfo,
			EquipmentInfo[] newSecondaryEquipmentInfo,
			final SubscriberContractInfo subscriberContractInfo, final String dealerCode,
			final String salesRepCode, final PricePlanValidationInfo pricePlanValidation,
			String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);
//				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean(updateIdenConv);

				ProductServicesInfo productServicesInfo = new ProductServicesInfo();

				boolean pricePlanChangeRequested = subscriberContractInfo != null;

				try{

					// Set ProductPK (which also retrieves the BAN)
					amdocsUpdateIdenConv.setProductPK(subscriberInfo.getBanId(),subscriberInfo.getSubscriberId());

					// for IDEN: set numbergroup in case it is needed for reserving additional phone #s (msisdn feature parameter)
					try {
						if (pricePlanChangeRequested && subscriberInfo.isIDEN()) {
//							amdocsUpdateIdenConv = (UpdateIdenConv)amdocsUpdateProductConv;
							amdocsUpdateIdenConv.setNumberGroup(subscriberInfo.getNumberGroup().getCode());
//							amdocsUpdateProductConv = amdocsUpdateIdenConv;
						}
					} catch (ValidateException ve){
						// 1110560 - Invalid Network_Id/NumberGroup
						// this exception indicates that there are no available phone #s in this numbergroup and
						// since we only do setNumberGroup() for the case where a service is added that needs
						// an additional phone # we ignore this exception at this time. In the case where a phone #
						// is required we are then catching and handling the error 'NumberGroup required'
						if ( ve.getErrorInd() == 1110560) {
							LOGGER.debug("ValidateException occurred (" + ve.getErrorInd() + " " + ve.getErrorMsg() + ")");
							LOGGER.debug("ValidateException ignored for now - processing continued");
						} else {
							throw ve;
						}
					}

					// define old and new equipment arrays
					IdenEquipmentInfo[] oldEquipmentInfoArray = null;
					IdenEquipmentInfo[] changeEquipmentInfoArray = null;

					// get old equipment array
					oldEquipmentInfoArray = amdocsUpdateIdenConv.getEquipmentInfo();

					int oldEquipmentInfoArraySize = oldEquipmentInfoArray != null ? oldEquipmentInfoArray.length : 0;

					for (int i = 0; i < oldEquipmentInfoArraySize; i++) {
						if (oldEquipmentInfoArray[i].serialNumber.equals(newPrimaryEquipmentInfo.getSerialNumber())) {
							oldEquipmentInfoArray[i].equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.UPDATE;
							oldEquipmentInfoArray[i].activateInd = true;
							oldEquipmentInfoArray[i].primaryInd = true;

							changeEquipmentInfoArray = oldEquipmentInfoArray;
						}
						else {
							oldEquipmentInfoArray[i].equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.DELETE;
							oldEquipmentInfoArray[i].activateInd = false;
							oldEquipmentInfoArray[i].primaryInd = false;
						}
					}

					if (changeEquipmentInfoArray == null) {
						// size new array (# of old equipment + 1 for new)
						changeEquipmentInfoArray = new IdenEquipmentInfo[oldEquipmentInfoArraySize + 1];

						// move old equipment to change array and set mode to 'DELETE'
						for (int i=0; i < oldEquipmentInfoArraySize; i++) {
							changeEquipmentInfoArray[i] = oldEquipmentInfoArray[i];
						}

						// add new equipment
						changeEquipmentInfoArray[changeEquipmentInfoArray.length - 1] = new IdenEquipmentInfo();
						changeEquipmentInfoArray[changeEquipmentInfoArray.length - 1].serialNumber = newPrimaryEquipmentInfo.getSerialNumber();
						changeEquipmentInfoArray[changeEquipmentInfoArray.length - 1].equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.INSERT;
						changeEquipmentInfoArray[changeEquipmentInfoArray.length - 1].activateInd = true;
						changeEquipmentInfoArray[changeEquipmentInfoArray.length - 1].primaryInd = true;
					}

					// change Equipment and maybe Priceplan (and remove/add regular socs if required)
					// ------------------------------------------------------------------------------
					if (pricePlanChangeRequested) {
						productServicesInfo = DaoSupport.mapTelusContractToAmdocsProductServices(amdocsUpdateIdenConv, subscriberInfo, subscriberContractInfo, true, true, false);
						LOGGER.debug("Calling changePricePlan()...");
						ContractInfo contractInfo = new ContractInfo();
						if (dealerCode != null && !dealerCode.equals("")){
							contractInfo.dealerCode = dealerCode;
							contractInfo.salesCode = salesRepCode == null ? "" : salesRepCode;
						} else 
							contractInfo = null;

						boolean modified = pricePlanValidation.isModified();
						if (modified){
							LOGGER.debug("newAPIallowed = TRUE");
							LOGGER.debug("Calling changePricePlan() with dealerCode = " + contractInfo.dealerCode + " and salesCode = " + contractInfo.salesCode + "...");
							amdocs.APILink.datatypes.PricePlanValidationInfo ppValidationInfo = new amdocs.APILink.datatypes.PricePlanValidationInfo();
							if (modified){
								ppValidationInfo.currentSoc = pricePlanValidation.validateCurrent(); 
								ppValidationInfo.equipmentType = pricePlanValidation.validateEquipmentServiceMatch();	  
								ppValidationInfo.forSale = pricePlanValidation.validateForSale();
								ppValidationInfo.ppSocGrouping = pricePlanValidation.validatePricePlanServiceGrouping();
								ppValidationInfo.province = pricePlanValidation.validateProvinceServiceMatch();
							}
							amdocsUpdateIdenConv.changeProductServices(productServicesInfo,null,contractInfo,changeEquipmentInfoArray,ppValidationInfo);
						} else {
							if (contractInfo != null){
								LOGGER.debug("Calling changePricePlan() with dealerCode = " + contractInfo.dealerCode + " and salesCode = " + contractInfo.salesCode + "...");
								amdocsUpdateIdenConv.changePricePlan(productServicesInfo,contractInfo,changeEquipmentInfoArray);
							} else {
								LOGGER.debug("Calling changePricePlan()...");
								amdocsUpdateIdenConv.changePricePlan(productServicesInfo,changeEquipmentInfoArray);
							}
						}
					} else {
						LOGGER.debug("Calling changeEquipmentInfo()...");
						amdocsUpdateIdenConv.changeEquipmentInfo(changeEquipmentInfoArray);
					}
					return null;
				} catch (ValidateException ve){
					ApplicationException rootException = new ApplicationException(SystemCodes.AMDOCS, String.valueOf(ve.getErrorInd())
							, ve.getErrorMsg(), "", ve);
					// 1111410 - Invalid NPA NXX Information
					// 1110560 - Invalid Network_Id/NumberGroup
					// 1110230 - Number Group is required
					if ( ve.getErrorInd() == 1111410 || ve.getErrorInd() == 1110560 || ve.getErrorInd() == 1110230) {
						String exceptionMsg = "No phone numbers available matching criteria. Npa/Nxx is "
							+ subscriberInfo.getPhoneNumber().substring(0,3)
							+ "/" + subscriberInfo.getPhoneNumber().substring(3,6)
							+ ". [Amdocs Error: " + ve.getErrorInd() + "/"
							+ ve.getErrorMsg() + "] ";
						LOGGER.debug(exceptionMsg);
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS
								, exceptionMsg, "", rootException);
					} 
					if (ve.getErrorInd() == 1117000) {
						LOGGER.debug(ve.getErrorMsg());
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.APP20003
								, ve.getErrorMsg(), "", rootException);
					}
					if ( ve.getErrorInd() == 1116210) {
						LOGGER.debug("New Serial Number In Use Exception Occurred");
						String exceptionMsg = "The new serial number is in use. [newSerialNumber="
							+ newPrimaryEquipmentInfo.getSerialNumber() + "]";
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.NEW_SERIAL_IS_IN_USE
								, exceptionMsg, "", rootException);
					}
					throw ve;
				}
			}
		});

	}
	
	@Override
	public void resetCSCSubscription(int ban, String subscriberId, String[] cscFeature,String sessionId)
	throws ApplicationException {
		throw new UnsupportedOperationException("Method is not implemented.");
	}
}
