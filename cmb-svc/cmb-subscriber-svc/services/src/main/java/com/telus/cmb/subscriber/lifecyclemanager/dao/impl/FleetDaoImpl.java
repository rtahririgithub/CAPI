package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.ContractInfo;
import amdocs.APILink.datatypes.IdenResourceAllocationInfo;
import amdocs.APILink.datatypes.ManualFleetInfo;
import amdocs.APILink.datatypes.NgpNmbInfo;
import amdocs.APILink.datatypes.ProductServicesInfo;
import amdocs.APILink.datatypes.ServiceInfo;
import amdocs.APILink.datatypes.TalkGroupChangeInfo;
import amdocs.APILink.datatypes.UFMIInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.IdenResourceServices;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;
import amdocs.APILink.sessions.interfaces.UpdateIdenConv;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclemanager.dao.FleetDao;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.framework.exception.TelusApplicationException;
import com.telus.eas.framework.exception.TelusValidationException;
import com.telus.eas.framework.info.ExceptionInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;

@Deprecated
public class FleetDaoImpl extends AmdocsDaoSupport implements FleetDao {	

	private final Logger LOGGER = Logger.getLogger(FleetDaoImpl.class);

	private final String ufmiWildCards = "%%%%%";

	@Override
	public void addMemberIdentity(final IDENSubscriberInfo idenSubscriberInfo,
			final SubscriberContractInfo subscriberContractInfo, final String dealerCode,
			final String salesRepCode, final int urbanId, final int fleetId, final String memberId,
			final boolean pricePlanChange, String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);

				IdenResourceAllocationInfo idenResourceAllocationInfo = new IdenResourceAllocationInfo();
				String[] memberIds = null;
				ServiceInfo[] serviceInfo = null;
				ProductServicesInfo productServicesInfo = null;
				boolean isPtnBasedFleet = false;
				amdocs.APILink.datatypes.FleetInfo amdocsFleetInfo = new amdocs.APILink.datatypes.FleetInfo();

				int npa = Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(0, 3));
				int nxx = Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(3, 6));

				isPtnBasedFleet = urbanId == npa && fleetId == nxx;

				try{
					// PTN-based fleet
					// - fleet might not be created yet, therefore we need to create and/or associate
					// fleet to BAN
					if (isPtnBasedFleet) {

						// Set BanPK (which also retrieves the BAN)
						LOGGER.debug("Calling setBanPK() for ban: " + idenSubscriberInfo.getBanId() + "...");
						amdocsUpdateBanConv.setBanPK(idenSubscriberInfo.getBanId());

						// call createFleet, which will create the fleet if necessary and
						// also associate the fleet to the BAN
						// (setting network and numbergroup is required)
						LOGGER.debug("Calling setNetwork() for network: " + idenSubscriberInfo.getNumberGroup().getNetworkId() + "...");
						amdocsUpdateBanConv.setNetwork((short)idenSubscriberInfo.getNumberGroup().getNetworkId());

						// get primary number group and set it
						// (at Telus, each network should have 1 and only 1 primary numbergroup)
						NgpNmbInfo[] amdocsNgpNmbInfo = amdocsUpdateBanConv.getNumberGroupList();
						if (amdocsNgpNmbInfo == null || amdocsNgpNmbInfo.length != 1)
							throw new TelusApplicationException(new ExceptionInfo("APP10012","Failed to get primary NGP for network: " + idenSubscriberInfo.getNumberGroup().getNetworkId() + " !"));
						LOGGER.debug("Calling setNumberGroup() for numberGroup: " + amdocsNgpNmbInfo[0].numberGroup + "...");
						amdocsUpdateBanConv.setNumberGroup(amdocsNgpNmbInfo[0].numberGroup);

						ManualFleetInfo manualFleetInfo = new ManualFleetInfo();
						manualFleetInfo.urbanId = urbanId;
						manualFleetInfo.fleetId = fleetId;
						manualFleetInfo.fleetAlias = urbanId + "*" + fleetId;
						LOGGER.debug("Calling createFleet() for fleet: " + manualFleetInfo.urbanId + "*" + manualFleetInfo.fleetId + "...");
						amdocsFleetInfo = amdocsUpdateBanConv.createFleet(manualFleetInfo);

						idenSubscriberInfo.setFleetClass(AttributeTranslator.stringFrombyte(amdocsFleetInfo.fleetClass));
					}

					// Set ProductPK (which also retrieves the BAN)
					LOGGER.debug("Calling setProductPK() for ban/subscriber: " + idenSubscriberInfo.getBanId() + "/" + idenSubscriberInfo.getSubscriberId() + "...");
					amdocsUpdateIdenConv.setProductPK(idenSubscriberInfo.getBanId(),idenSubscriberInfo.getSubscriberId());

					// set fleet
					amdocs.APILink.datatypes.FleetInfo fleetInfo = new amdocs.APILink.datatypes.FleetInfo();
					fleetInfo.urbanId = urbanId;
					fleetInfo.fleetId = fleetId;
					LOGGER.debug("Calling setFleet() for fleet: " + urbanId + "*" + fleetId + "...");
					amdocsUpdateIdenConv.setFleet(fleetInfo);

					// Check that member id is available
					String newMemberId = memberId.replace('*','%');
					if (newMemberId.length() < 5)
						newMemberId.concat(ufmiWildCards.substring(0,5 - newMemberId.length()));
					LOGGER.debug("calling getAvailableUfmiList()... with pattern=[" + newMemberId + "]");
					memberIds = amdocsUpdateIdenConv.getAvailableUfmiList(false,"0",newMemberId);
					if (memberIds == null || memberIds.length == 0) {
						throw new TelusValidationException("VAL20033","The supplied member id is not available.");
					}

					// increase 'expected # of subscribers' for subscriber with UFMI
					// - needs to be done because old member id is put in 'Aging' status and still counted as subscriber
					DaoSupport.increaseExpectedNumberOfSubscribers(amdocsUpdateBanConv, idenSubscriberInfo.getBanId(), urbanId, fleetId);

					// set numbergroup in case it is needed for reserving additional phone #s (msisdn feature parameter)
					try {
						amdocsUpdateIdenConv.setNumberGroup(idenSubscriberInfo.getNumberGroup().getCode());
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

					// Populate IdenResourceAllocationInfo
					idenResourceAllocationInfo.ufmiTransactionType = IdenResourceAllocationInfo.CHANGE_RESOURCE_ADD;
					idenResourceAllocationInfo.ufmiMethod = newMemberId.trim().equals(ufmiWildCards) ?
							IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_RANDOM :
								IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_MANUAL;
					idenResourceAllocationInfo.memberId = Integer.parseInt(memberIds[0].trim());

					// Populate Amdocs structures based on whether price plan has changed or not:
					// - Yes: populate ProductServicesInfo
					// - No: populate ServiceInfo[]
					if (pricePlanChange) {
						// populate and print ProductServices
						productServicesInfo = DaoSupport.mapTelusContractToAmdocsProductServices(amdocsUpdateIdenConv
								, idenSubscriberInfo, subscriberContractInfo, true, true, false);
						// - Price Plan
						LOGGER.debug("ProductServices being set to for Subscriber " + idenSubscriberInfo.getSubscriberId() + ":");
						LOGGER.debug("PricePlan SOC    : [" + productServicesInfo.pricePlan.soc.soc + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.pricePlan.soc.transactionType) + " effDate:" + productServicesInfo.pricePlan.soc.effDate + " expDate:" + productServicesInfo.pricePlan.soc.expDate);
						for (int i=0; i < productServicesInfo.pricePlan.feature.length; i++) {
							LOGGER.debug("          Feature: [" + productServicesInfo.pricePlan.feature[i].featureCode + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.pricePlan.feature[i].transactionType) + " ftrParam:" + productServicesInfo.pricePlan.feature[i].ftrParam + " msisdn:" + productServicesInfo.pricePlan.feature[i].msisdn);
						}
						// - Additional services
						for (int i=0; i < productServicesInfo.addtnlSrvs.length; i++) {
							LOGGER.debug("Additional SOC    : [" + productServicesInfo.addtnlSrvs[i].soc.soc + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.addtnlSrvs[i].soc.transactionType) + " effDate:" + productServicesInfo.addtnlSrvs[i].soc.effDate + " expDate:" + productServicesInfo.addtnlSrvs[i].soc.expDate);
							for (int j=0; j < productServicesInfo.addtnlSrvs[i].feature.length; j++) {
								LOGGER.debug("           Feature: [" + productServicesInfo.addtnlSrvs[i].feature[j].featureCode + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.addtnlSrvs[i].feature[j].transactionType) + " ftrParam:" + productServicesInfo.addtnlSrvs[i].feature[j].ftrParam + " msisdn:" + productServicesInfo.addtnlSrvs[i].feature[j].msisdn);
							}
						}
						// - Promotional services
						for (int i=0; i < productServicesInfo.promPricePlan.length; i++) {
							LOGGER.debug("Promotional SOC    : [" + productServicesInfo.promPricePlan[i].soc.soc + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.promPricePlan[i].soc.transactionType) + " effDate:" + productServicesInfo.promPricePlan[i].soc.effDate + " expDate:" + productServicesInfo.promPricePlan[i].soc.expDate);
							for (int j=0; j < productServicesInfo.promPricePlan[i].feature.length; j++) {
								LOGGER.debug("            Feature: [" + productServicesInfo.promPricePlan[i].feature[j].featureCode + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.promPricePlan[i].feature[j].transactionType) + " ftrParam:" + productServicesInfo.promPricePlan[i].feature[j].ftrParam + " msisdn:" + productServicesInfo.promPricePlan[i].feature[j].msisdn);
							}
						}
					} else {
						// populate and print serviceInfo
						serviceInfo = DaoSupport.buildServiceInfo(amdocsUpdateIdenConv, idenSubscriberInfo, subscriberContractInfo);

						LOGGER.debug("Services passed into for Subscriber " + idenSubscriberInfo.getSubscriberId() + ":");
						for (int i=0; i < serviceInfo.length; i++) {
							LOGGER.debug("Regular SOC    : " + serviceInfo[i].soc.soc + " transactionType:" + AttributeTranslator.stringFrombyte(serviceInfo[i].soc.transactionType) + " effDate:" + serviceInfo[i].soc.effDate + " expDate:" + serviceInfo[i].soc.expDate);
							if (serviceInfo[i].feature != null) {
								for (int j=0; j < serviceInfo[i].feature.length; j++) {
									LOGGER.debug("     Feature: " + serviceInfo[i].feature[j].featureCode + " transactionType:" + AttributeTranslator.stringFrombyte(serviceInfo[i].feature[j].transactionType) + " ftrParam:" + serviceInfo[i].feature[j].ftrParam  + " /msisdn:" + serviceInfo[i].feature[j].msisdn);
								}
							}
						}
					}
					// print idenResourceAllocationInfo
					LOGGER.debug("Iden Resource Allocation Info passed in for Subscriber " + idenSubscriberInfo.getSubscriberId() + ":");
					LOGGER.debug("  ufmiTransactionType: [" + AttributeTranslator.stringFrombyte(idenResourceAllocationInfo.ufmiTransactionType) + "]");
					LOGGER.debug("  ufmiMethod         : [" + AttributeTranslator.stringFrombyte(idenResourceAllocationInfo.ufmiMethod) + "]");
					LOGGER.debug("  memberId           : [" + idenResourceAllocationInfo.memberId + "]");

					// Add member identity - with/without contractInfo
					// Note: amdocs method to change price plan with contract information does not exist
					ContractInfo contractInfo = new ContractInfo();
					if (dealerCode != null && !dealerCode.equals("")){
						contractInfo.dealerCode = dealerCode;
						contractInfo.salesCode = salesRepCode == null ? "" : salesRepCode;
					} else 
						contractInfo = null;

					boolean modified = subscriberContractInfo.getPricePlanValidation0().isModified();
					if (modified){
						amdocs.APILink.datatypes.PricePlanValidationInfo   ppValidationInfo = new amdocs.APILink.datatypes.PricePlanValidationInfo();
						if (modified){
							PricePlanValidationInfo pricePlanValidation = subscriberContractInfo.getPricePlanValidation0();
							ppValidationInfo.currentSoc = pricePlanValidation.validateCurrent(); 
							ppValidationInfo.equipmentType = pricePlanValidation.validateEquipmentServiceMatch();	  
							ppValidationInfo.forSale = pricePlanValidation.validateForSale();
							ppValidationInfo.ppSocGrouping = pricePlanValidation.validatePricePlanServiceGrouping();
							ppValidationInfo.province = pricePlanValidation.validateProvinceServiceMatch();
						}

						if (!pricePlanChange){
							if (productServicesInfo == null)
								productServicesInfo = new ProductServicesInfo();
							productServicesInfo.addtnlSrvs = serviceInfo;
						}
						amdocsUpdateIdenConv.changeResourceServices(idenResourceAllocationInfo,productServicesInfo,contractInfo,ppValidationInfo);
					} else {
						if (contractInfo != null){
							if (pricePlanChange)
								amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo, productServicesInfo);
							else
								amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo, serviceInfo, contractInfo);
						} else {
							if (pricePlanChange)
								amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo, productServicesInfo);
							else
								amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo, serviceInfo);
						}
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
							+ idenSubscriberInfo.getPhoneNumber().substring(0,3)
							+ "/" + idenSubscriberInfo.getPhoneNumber().substring(3,6)
							+ ". [Amdocs Error: " + ve.getErrorInd() + "/"
							+ ve.getErrorMsg() + "] ";
						LOGGER.debug(exceptionMsg);
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS
								, exceptionMsg, "", rootException);
					}
					// 1117290 - Dispatch resource is not supported by the selected Service Agreement
					if ( ve.getErrorInd() == 1117290) {
						String exceptionMsg = "Service Agreement does not support dispatch resource. "
							+ " Subscriber=[/" + idenSubscriberInfo.getPhoneNumber()
							+ "]. [Amdocs Error: " + ve.getErrorInd() + "/"
							+ ve.getErrorMsg() + "] ";
						LOGGER.debug(exceptionMsg);
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SERVICE_AGREEMENT_DOES_NOT_SUPPORT_DISPATCH_RESOURCE
								, exceptionMsg, "", rootException);
					}
					// 1111440 - This combination of fleetId and urbanId is not associated with this Ban.
					if ( ve.getErrorInd() == 1111440) {
						LOGGER.debug("Fleet not associated to ban Exception Occurred");
						String exceptionMsg = "The fleet is not associated to this ban. [ban="
							+ idenSubscriberInfo.getBanId() + " fleet="
							+ urbanId + "*" + fleetId + "]";			
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.FLEET_NOT_ASSOCIATED_TO_BAN
								, exceptionMsg, "", rootException);
					}
					throw ve;
				}
			}
		});
	}

	@Override
	public void changeMemberId(final IDENSubscriberInfo idenSubscriberInfo,
			final String newMemberId, String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				IdenResourceAllocationInfo idenResourceAllocationInfo = new IdenResourceAllocationInfo();
				String[] memberIds = null;

				boolean isPtnBasedFleet;

				int npa = Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(0, 3));
				int nxx = Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(3, 6));

				isPtnBasedFleet = idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getUrbanId() == npa 
				&& idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getFleetId() == nxx;

				// Subscriber cannot be on a PTN-based fleet
				if (isPtnBasedFleet) {
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_CANNOT_BE_ON_PTN_FLEET
							, "Subscriber cannot be on a PTN-based fleet.", "");
				}

				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateIdenConv.setProductPK(idenSubscriberInfo.getBanId(),idenSubscriberInfo.getSubscriberId());

				// set fleet
				amdocs.APILink.datatypes.FleetInfo fleetInfo = new amdocs.APILink.datatypes.FleetInfo();
				fleetInfo.urbanId = idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getUrbanId();
				fleetInfo.fleetId = idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getFleetId();
				amdocsUpdateIdenConv.setFleet(fleetInfo);

				// Check that member id is available
				String memberId = newMemberId.replace('*','%');
				if (memberId.length() < 5)
					memberId.concat(ufmiWildCards.substring(0,5 - memberId.length()));
				LOGGER.debug("calling getAvailableUfmiList()... with pattern=[" + memberId + "]");
				memberIds = amdocsUpdateIdenConv.getAvailableUfmiList(false,"0",memberId);
				if (memberIds == null || memberIds.length == 0) {
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.MEMBER_ID_NOT_FOUND, "The supplied member id is not available.", "");
				}

				// increase 'expected # of subscribers' for subscriber with UFMI
				// - needs to be done because old member id is put in 'Aging' status and still counted as subscriber
				DaoSupport.increaseExpectedNumberOfSubscribers(amdocsUpdateBanConv, idenSubscriberInfo.getBanId()
						, idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getUrbanId(), idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getFleetId());

				// set number group
				amdocsUpdateIdenConv.setNumberGroup(idenSubscriberInfo.getNumberGroup().getCode());

				// Populate IdenResourceAllocationInfo
				idenResourceAllocationInfo.ufmiTransactionType = IdenResourceAllocationInfo.CHANGE_RESOURCE_CHANGE;
				idenResourceAllocationInfo.ufmiMethod = memberId.trim().equals(ufmiWildCards) ?
						IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_RANDOM :
							IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_MANUAL;
				idenResourceAllocationInfo.memberId = Integer.parseInt(memberIds[0].trim());

				// Change member id
				LOGGER.debug("calling changeResource()...");
				amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo);

				return null;
			}
		});
	}

	@Override
	public void changeMemberIdentity(final IDENSubscriberInfo idenSubscriberInfo,
			final int newUrbanId, final int newFleetId, final String newMemberId, String sessionId)
	throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {

				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);

				IdenResourceAllocationInfo idenResourceAllocationInfo = new IdenResourceAllocationInfo();
				String[] memberIds = null;
				boolean isPtnBasedFleet = false;
				amdocs.APILink.datatypes.FleetInfo amdocsFleetInfo = new amdocs.APILink.datatypes.FleetInfo();

				int npa = Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(0, 3));
				int nxx = Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(3, 6));

				isPtnBasedFleet = newUrbanId == npa && newFleetId == nxx;

				try{
					// PTN-based fleet
					// - fleet might not be created yet, therefore we need to create and/or associate
					// fleet to BAN
					if (isPtnBasedFleet) {

						// Set BanPK (which also retrieves the BAN)
						LOGGER.debug("Calling setBanPK() for ban: " + idenSubscriberInfo.getBanId() + "...");
						amdocsUpdateBanConv.setBanPK(idenSubscriberInfo.getBanId());

						// call createFleet, which will create the fleet if necessary and
						// also associate the fleet to the BAN
						// (setting network and numbergroup is required)
						LOGGER.debug("Calling setNetwork() for network: " + idenSubscriberInfo.getNumberGroup().getNetworkId() + "...");
						amdocsUpdateBanConv.setNetwork((short)idenSubscriberInfo.getNumberGroup().getNetworkId());

						// get primary number group and set it
						// (at Telus, each network should have 1 and only 1 primary numbergroup)
						NgpNmbInfo[] amdocsNgpNmbInfo = amdocsUpdateBanConv.getNumberGroupList();
						if (amdocsNgpNmbInfo == null || amdocsNgpNmbInfo.length != 1)
							throw new TelusApplicationException(new ExceptionInfo("APP10012","Failed to get primary NGP for network: " + idenSubscriberInfo.getNumberGroup().getNetworkId() + " !"));
						LOGGER.debug("Calling setNumberGroup() for numberGroup: " + amdocsNgpNmbInfo[0].numberGroup + "...");
						amdocsUpdateBanConv.setNumberGroup(amdocsNgpNmbInfo[0].numberGroup);

						ManualFleetInfo manualFleetInfo = new ManualFleetInfo();
						manualFleetInfo.urbanId = newUrbanId;
						manualFleetInfo.fleetId = newFleetId;
						manualFleetInfo.fleetAlias = newUrbanId + "*" + newFleetId;
						LOGGER.debug("Calling createFleet() for fleet: " + manualFleetInfo.urbanId + "*" + manualFleetInfo.fleetId + "...");
						amdocsFleetInfo = amdocsUpdateBanConv.createFleet(manualFleetInfo);

						idenSubscriberInfo.setFleetClass(AttributeTranslator.stringFrombyte(amdocsFleetInfo.fleetClass));
					}

					// Set ProductPK (which also retrieves the BAN)
					amdocsUpdateIdenConv.setProductPK(idenSubscriberInfo.getBanId(),idenSubscriberInfo.getSubscriberId());

					// set fleet
					amdocs.APILink.datatypes.FleetInfo fleetInfo = new amdocs.APILink.datatypes.FleetInfo();
					fleetInfo.urbanId = newUrbanId;
					fleetInfo.fleetId = newFleetId;
					amdocsUpdateIdenConv.setFleet(fleetInfo);

					// Check that member id is available
					String memberId = newMemberId.replace('*','%');
					if (memberId.length() < 5)
						memberId.concat(ufmiWildCards.substring(0,5 - memberId.length()));
					LOGGER.debug("calling getAvailableUfmiList()... with pattern=[" + memberId + "]");
					memberIds = amdocsUpdateIdenConv.getAvailableUfmiList(false,"0",memberId);
					if (memberIds == null || memberIds.length == 0) {
						throw new TelusValidationException("VAL20033","The supplied member id is not available.");
					}

					// increase 'expected # of subscribers' for subscriber with UFMI
					// - needs to be done because old member id is put in 'Aging' status and still counted as subscriber
					DaoSupport.increaseExpectedNumberOfSubscribers(amdocsUpdateBanConv, idenSubscriberInfo.getBanId(), newUrbanId, newFleetId);

					// set number group
					amdocsUpdateIdenConv.setNumberGroup(idenSubscriberInfo.getNumberGroup().getCode());

					// Populate IdenResourceAllocationInfo
					idenResourceAllocationInfo.ufmiTransactionType = IdenResourceAllocationInfo.CHANGE_RESOURCE_CHANGE;
					idenResourceAllocationInfo.ufmiMethod = memberId.trim().equals(ufmiWildCards) ?
							IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_RANDOM :
								IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_MANUAL;
					idenResourceAllocationInfo.memberId = Integer.parseInt(memberIds[0].trim());

					// Change member id
					LOGGER.debug("calling changeResource()...");
					amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo);

					LOGGER.debug("Leaving...");
					return null;
				} catch (ValidateException ve){
					ApplicationException rootException = new ApplicationException(SystemCodes.AMDOCS, String.valueOf(ve.getErrorInd()), ve.getErrorMsg(), "", ve);
					// 1111440 - This combination of fleetId and urbanId is not associated with this Ban.
					if ( ve.getErrorInd() == 1111440) {
						LOGGER.debug("Fleet not associated to ban Exception Occurred");
						String exceptionMsg = "The fleet is not associated to this ban. [ban="
							+ idenSubscriberInfo.getBanId() + " fleet="
							+ newUrbanId + "*" + newFleetId + "]";
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.FLEET_NOT_ASSOCIATED_TO_BAN, exceptionMsg, "", rootException);
					} else {
						throw ve;
					}
				}
			}

		});		
	}

	@Override
	public void changeTalkGroups(final IDENSubscriberInfo idenSubscriberInfo,
			final TalkGroupInfo[] addedTalkGroups, final TalkGroupInfo[] removedTalkGroups, String sessionId)
	throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);

				TalkGroupChangeInfo[] amdocsTalkGroupChangeInfoArray = null;
				com.telus.eas.account.info.FleetInfo fleetInfo = new com.telus.eas.account.info.FleetInfo();
				int urbanId = 0;
				int fleetId = 0;


				// check that talk group arrays are not empty
				LOGGER.debug("Checking that talk group arrays are not empty...");
				if (addedTalkGroups.length == 0 && removedTalkGroups.length == 0)
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.TALK_GROUPS_EMPTY, "List of Talk Groups empty.", "");

				// check that all talk groups in the array of talk groups to be added are for the same fleet
				LOGGER.debug("Checking that all talk groups in the array to be added are for the same fleet...");
				if (addedTalkGroups.length > 0) {
					urbanId = addedTalkGroups[0].getFleetIdentity().getUrbanId();
					fleetId = addedTalkGroups[0].getFleetIdentity().getFleetId();
					LOGGER.debug("fleet is: " + urbanId + "*" + fleetId);
					fleetInfo.getIdentity0().setUrbanId(urbanId);
					fleetInfo.getIdentity0().setFleetId(fleetId);
					LOGGER.debug("fleetInfo.fleet is: " + fleetInfo.getIdentity0().getUrbanId( ) + "*" + fleetInfo.getIdentity0().getFleetId());

					for (int i=1; i < addedTalkGroups.length; i++) {
						if (addedTalkGroups[i].getFleetIdentity().getUrbanId() != urbanId ||
								addedTalkGroups[i].getFleetIdentity().getFleetId() != fleetId)
							throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.TALK_GROUPS_FROM_SAME_FLEET, "Talk Groups to be added must be from same fleet.", "");
					}
				}

				// check that member id has been allocated
				LOGGER.debug("Checking that member id has been allocated...");
				if (idenSubscriberInfo.getMemberIdentity0().getMemberId() == null || idenSubscriberInfo.getMemberIdentity0().getMemberId().trim().equals("") || idenSubscriberInfo.getMemberIdentity0().getMemberId().trim().equals("0") || idenSubscriberInfo.getMemberIdentity0().getResourceStatus().equals("C"))
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.DISPATCH_RESOURCE_NOT_ALLOCATED, "Dispatch Resource has to be allocated.", "");


				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateIdenConv.setProductPK(idenSubscriberInfo.getBanId(), idenSubscriberInfo.getSubscriberId());

				// map Telus to Amdocs
				amdocsTalkGroupChangeInfoArray = new TalkGroupChangeInfo[addedTalkGroups.length + removedTalkGroups.length];
				for (int i=0; i < addedTalkGroups.length; i++) {
					amdocsTalkGroupChangeInfoArray[i] = new TalkGroupChangeInfo();
					amdocsTalkGroupChangeInfoArray[i].actMode = TalkGroupChangeInfo.TALK_GROUP_CHANGE_INSERT;
					amdocsTalkGroupChangeInfoArray[i].urbanId = urbanId;
					amdocsTalkGroupChangeInfoArray[i].fleetId = fleetId;
					amdocsTalkGroupChangeInfoArray[i].talkGroupId = (short)addedTalkGroups[i].getTalkGroupId();
				}
				for (int i=addedTalkGroups.length; i < addedTalkGroups.length + removedTalkGroups.length; i++) {
					amdocsTalkGroupChangeInfoArray[i] = new TalkGroupChangeInfo();
					amdocsTalkGroupChangeInfoArray[i].actMode = TalkGroupChangeInfo.TALK_GROUP_CHANGE_DELETE;
					amdocsTalkGroupChangeInfoArray[i].urbanId = urbanId;
					amdocsTalkGroupChangeInfoArray[i].fleetId = fleetId;
					amdocsTalkGroupChangeInfoArray[i].talkGroupId = (short)removedTalkGroups[i-addedTalkGroups.length].getTalkGroupId();
				}

				// set talkGroups
				LOGGER.debug("Calling changeTalkGroupList()...");
				LOGGER.debug("talkGroups being passed in:...");
				for (int i=0; i < amdocsTalkGroupChangeInfoArray.length; i++) {
					LOGGER.debug("  " + i + " actMode:" + amdocsTalkGroupChangeInfoArray[i].actMode);
					LOGGER.debug("  " + i + " urbanId:" + amdocsTalkGroupChangeInfoArray[i].urbanId);
					LOGGER.debug("  " + i + " fleetId:" + amdocsTalkGroupChangeInfoArray[i].fleetId);
					LOGGER.debug("  " + i + " talkGroupId:" + amdocsTalkGroupChangeInfoArray[i].talkGroupId);
				}

				amdocsUpdateIdenConv.changeTalkGroupList(amdocsTalkGroupChangeInfoArray);

				return null;				
			}
		});

	}

	@Override
	public int[] retrieveAvailableMemberIDs(final int urbanId, final int fleetId,
			final String newMemberIdPattern, final int max, String sessionId) throws ApplicationException {
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<int[]>() {

			@Override
			public int[] doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				IdenResourceServices amdocsIdenResourceServices = transactionContext.createBean(IdenResourceServices.class);

				String [] memberIds = null;
				String pattern = "%%%%%";
				int [] ret = null;
				boolean moreMemberIDsAvailable = true;
				Collection<String> memberIdsFound = new ArrayList<String>();
				String startNumber = "0";

				// Check that member id is available
				String memberIdPattern = newMemberIdPattern.replace('*','%');
				if (memberIdPattern.length() < 5)
					memberIdPattern.concat(pattern.substring(0,5 - memberIdPattern.length()));
				LOGGER.debug("calling getAvailableUfmiList()... with pattern=[" + memberIdPattern + "]");


				// loop until the requested number of memberIds is found OR no more numbers are
				// available
				while (memberIdsFound.size() < max && moreMemberIDsAvailable) {
					memberIds = amdocsIdenResourceServices.getAvailableUfmiList(urbanId, fleetId, false, startNumber, memberIdPattern);
					if (memberIds == null || memberIds.length < 10)
						moreMemberIDsAvailable = false;
					if (memberIds != null && memberIds.length > 0) {
						for (int i=0; i < memberIds.length; i++) {
							memberIdsFound.add(memberIds[i]);
							if (memberIdsFound.size() == max)
								break;
						}
						startNumber = (memberIds[memberIds.length-1]).trim();
					}
				}

				String [] tmp = new String[memberIdsFound.size()];
				tmp = (String[])memberIdsFound.toArray(tmp);

				ret = new int[tmp.length];
				for (int i = 0; i < tmp.length; i++){
					ret[i] = Integer.parseInt(tmp[i].trim());
				}
				if (memberIdsFound.size() == 0)
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS, "No member IDs available matching criteria", "");

				return ret;
			}
		});
	}

	@Override
	public void removeMemberIdentity(final IDENSubscriberInfo idenSubscriberInfo,
			final SubscriberContractInfo subscriberContractInfo, final String dealerCode,
			final String salesRepCode, final boolean pricePlanChange, String sessionId)
	throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);
				IdenResourceAllocationInfo idenResourceAllocationInfo = new IdenResourceAllocationInfo();
				ServiceInfo[] serviceInfo = null;
				ProductServicesInfo productServicesInfo = null;

				try{

					// Set ProductPK (which also retrieves the BAN)
					amdocsUpdateIdenConv.setProductPK(idenSubscriberInfo.getBanId(),idenSubscriberInfo.getSubscriberId());

					// set numbergroup in case it is needed for reserving additional phone #s (msisdn feature parameter)
					try {
						amdocsUpdateIdenConv.setNumberGroup(idenSubscriberInfo.getNumberGroup().getCode());
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

					// Populate IdenResourceAllocationInfo
					idenResourceAllocationInfo.ufmiTransactionType = IdenResourceAllocationInfo.CHANGE_RESOURCE_CANCEL;

					// Populate Amdocs structures based on whether price plan has changed or not:
					// - Yes: populate ProductServicesInfo
					// - No: populate ServiceInfo[]
					if (pricePlanChange) {
						// populate and print ProductServices
						productServicesInfo = DaoSupport.mapTelusContractToAmdocsProductServices(amdocsUpdateIdenConv, idenSubscriberInfo, subscriberContractInfo, true, true, false);
						// - Price Plan
						LOGGER.debug("ProductServices being set to for Subscriber " + idenSubscriberInfo.getSubscriberId() + ":");
						LOGGER.debug("PricePlan SOC    : [" + productServicesInfo.pricePlan.soc.soc + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.pricePlan.soc.transactionType) + " effDate:" + productServicesInfo.pricePlan.soc.effDate + " expDate:" + productServicesInfo.pricePlan.soc.expDate);
						for (int i=0; i < productServicesInfo.pricePlan.feature.length; i++) {
							LOGGER.debug("          Feature: [" + productServicesInfo.pricePlan.feature[i].featureCode + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.pricePlan.feature[i].transactionType) + " ftrParam:" + productServicesInfo.pricePlan.feature[i].ftrParam + " msisdn:" + productServicesInfo.pricePlan.feature[i].msisdn);
						}
						// - Additional services
						for (int i=0; i < productServicesInfo.addtnlSrvs.length; i++) {
							LOGGER.debug("Additional SOC    : [" + productServicesInfo.addtnlSrvs[i].soc.soc + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.addtnlSrvs[i].soc.transactionType) + " effDate:" + productServicesInfo.addtnlSrvs[i].soc.effDate + " expDate:" + productServicesInfo.addtnlSrvs[i].soc.expDate);
							for (int j=0; j < productServicesInfo.addtnlSrvs[i].feature.length; j++) {
								LOGGER.debug("           Feature: [" + productServicesInfo.addtnlSrvs[i].feature[j].featureCode + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.addtnlSrvs[i].feature[j].transactionType) + " ftrParam:" + productServicesInfo.addtnlSrvs[i].feature[j].ftrParam + " msisdn:" + productServicesInfo.addtnlSrvs[i].feature[j].msisdn);
							}
						}
						// - Promotional services
						for (int i=0; i < productServicesInfo.promPricePlan.length; i++) {
							LOGGER.debug("Promotional SOC    : [" + productServicesInfo.promPricePlan[i].soc.soc + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.promPricePlan[i].soc.transactionType) + " effDate:" + productServicesInfo.promPricePlan[i].soc.effDate + " expDate:" + productServicesInfo.promPricePlan[i].soc.expDate);
							for (int j=0; j < productServicesInfo.promPricePlan[i].feature.length; j++) {
								LOGGER.debug("            Feature: [" + productServicesInfo.promPricePlan[i].feature[j].featureCode + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.promPricePlan[i].feature[j].transactionType) + " ftrParam:" + productServicesInfo.promPricePlan[i].feature[j].ftrParam + " msisdn:" + productServicesInfo.promPricePlan[i].feature[j].msisdn);
							}
						}
					} else {
						// populate and print serviceInfo
						serviceInfo = DaoSupport.buildServiceInfo(amdocsUpdateIdenConv, idenSubscriberInfo, subscriberContractInfo);

						LOGGER.debug("Services passed into for Subscriber " + idenSubscriberInfo.getSubscriberId() + ":");
						for (int i=0; i < serviceInfo.length; i++) {
							LOGGER.debug("Regular SOC    : " + serviceInfo[i].soc.soc + " transactionType:" + AttributeTranslator.stringFrombyte(serviceInfo[i].soc.transactionType) + " effDate:" + serviceInfo[i].soc.effDate + " expDate:" + serviceInfo[i].soc.expDate);
							if (serviceInfo[i].feature != null) {
								for (int j=0; j < serviceInfo[i].feature.length; j++) {
									LOGGER.debug("     Feature: " + serviceInfo[i].feature[j].featureCode + " transactionType:" + AttributeTranslator.stringFrombyte(serviceInfo[i].feature[j].transactionType) + " ftrParam:" + serviceInfo[i].feature[j].ftrParam  + " /msisdn:" + serviceInfo[i].feature[j].msisdn);
								}
							}
						}
					}
					// print idenResourceAllocationInfo
					LOGGER.debug("Iden Resource Allocation Info passed in for Subscriber " + idenSubscriberInfo.getSubscriberId() + ":");
					LOGGER.debug("  ufmiTransactionType: [" + AttributeTranslator.stringFrombyte(idenResourceAllocationInfo.ufmiTransactionType) + "]");
					LOGGER.debug("  ufmiMethod         : [" + AttributeTranslator.stringFrombyte(idenResourceAllocationInfo.ufmiMethod) + "]");
					LOGGER.debug("  memberId           : [" + idenResourceAllocationInfo.memberId + "]");

					// Remove member identity - with/without contractInfo
					// Note: amdocs method to change price plan with contract information does not exist
					ContractInfo contractInfo = new ContractInfo();
					if (dealerCode != null && !dealerCode.equals("")){
						contractInfo.dealerCode = dealerCode;
						contractInfo.salesCode = salesRepCode == null ? "" : salesRepCode;
					} else
						contractInfo = null;

					boolean modified = subscriberContractInfo.getPricePlanValidation0().isModified();
					if (modified){
						amdocs.APILink.datatypes.PricePlanValidationInfo   ppValidationInfo = new amdocs.APILink.datatypes.PricePlanValidationInfo();
						if (modified){
							PricePlanValidationInfo pricePlanValidation = subscriberContractInfo.getPricePlanValidation0();
							ppValidationInfo.currentSoc = pricePlanValidation.validateCurrent(); 
							ppValidationInfo.equipmentType = pricePlanValidation.validateEquipmentServiceMatch();	  
							ppValidationInfo.forSale = pricePlanValidation.validateForSale();
							ppValidationInfo.ppSocGrouping = pricePlanValidation.validatePricePlanServiceGrouping();
							ppValidationInfo.province = pricePlanValidation.validateProvinceServiceMatch();
						}
						if (!pricePlanChange){
							if (productServicesInfo == null)
								productServicesInfo = new ProductServicesInfo();
							productServicesInfo.addtnlSrvs = serviceInfo;
						}
						amdocsUpdateIdenConv.changeResourceServices(idenResourceAllocationInfo,productServicesInfo,contractInfo,ppValidationInfo);
					} else {      
						if (contractInfo != null){
							if (pricePlanChange)
								amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo, productServicesInfo);
							else
								amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo, serviceInfo, contractInfo);
						} else {
							if (pricePlanChange)
								amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo, productServicesInfo);
							else
								amdocsUpdateIdenConv.changeResource(idenResourceAllocationInfo, serviceInfo);
						}
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
							+ idenSubscriberInfo.getPhoneNumber().substring(0,3)
							+ "/" + idenSubscriberInfo.getPhoneNumber().substring(3,6)
							+ ". [Amdocs Error: " + ve.getErrorInd() + "/"
							+ ve.getErrorMsg() + "] ";
						LOGGER.debug(exceptionMsg);
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS
								, exceptionMsg, "", rootException);
					}

					// 1117300 - Service Agreement supports Dispatch resource    Subscriber does not have this resource.
					if ( ve.getErrorInd() == 1117300) {
						String exceptionMsg = "Service Agreement supports Dispatch resource. Subscriber does not have this resource. "
							+ " Subscriber=[/" + idenSubscriberInfo.getPhoneNumber()
							+ "]. [Amdocs Error: " + ve.getErrorInd() + "/"
							+ ve.getErrorMsg() + "] ";
						LOGGER.debug(exceptionMsg);
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_DOES_NOT_HAVE_DISPATCH_SERVICE, exceptionMsg, "", rootException);
					}
					throw ve;
				}
			}
		});

	}

	@Override
	public IDENSubscriberInfo reserveMemberId(
			final IDENSubscriberInfo idenSubscriberInfo,
			final FleetIdentityInfo fleetIdentityInfo, String wildCard, final boolean isPtnBasedFleet,
			final String sessionId) throws ApplicationException {
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<IDENSubscriberInfo>() {

			@Override
			public IDENSubscriberInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				IdenResourceServices amdocsIdenResourceServices = transactionContext.createBean(IdenResourceServices.class);

				ExceptionInfo exceptionInfo = new ExceptionInfo();
				UFMIInfo ufmiInfo = new UFMIInfo();
				String wildCard = new String();
				boolean isPtnBasedFleet;
				amdocs.APILink.datatypes.FleetInfo amdocsFleetInfo = new amdocs.APILink.datatypes.FleetInfo();

				int npa = Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(0, 3));
				int nxx = Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(3, 6));

				isPtnBasedFleet = fleetIdentityInfo.getUrbanId() == npa && fleetIdentityInfo.getFleetId() == nxx;

				// set urbanId/fleetId on subscriber
				idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().setUrbanId(fleetIdentityInfo.getUrbanId());
				idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().setFleetId(fleetIdentityInfo.getFleetId());

				// PTN-based fleet
				// - fleet might not be created yet, therefore we need to create and/or associate
				// fleet to BAN
				if (isPtnBasedFleet) {

					// Set BanPK (which also retrieves the BAN)
					amdocsUpdateBanConv.setBanPK(idenSubscriberInfo.getBanId());

					// call createFleet, which will create the fleet if necessary and
					// also associate the fleet to the BAN
					// (setting network and numbergroup is required)
					LOGGER.debug("Calling setNetwork() for network: " + idenSubscriberInfo.getNumberGroup().getNetworkId() + "...");
					amdocsUpdateBanConv.setNetwork((short)idenSubscriberInfo.getNumberGroup().getNetworkId());

					// get primary number group and set it
					// (at Telus, each network should have 1 and only 1 primary numbergroup)
					NgpNmbInfo[] amdocsNgpNmbInfo = amdocsUpdateBanConv.getNumberGroupList();
					if (amdocsNgpNmbInfo == null || amdocsNgpNmbInfo.length != 1)
						throw new TelusApplicationException(new ExceptionInfo("APP10012","Failed to get primary NGP for network: " + idenSubscriberInfo.getNumberGroup().getNetworkId() + " !"));
					LOGGER.debug("Calling setNumberGroup() for numberGroup: " + amdocsNgpNmbInfo[0].numberGroup + "...");
					amdocsUpdateBanConv.setNumberGroup(amdocsNgpNmbInfo[0].numberGroup);

					ManualFleetInfo manualFleetInfo = new ManualFleetInfo();
					manualFleetInfo.urbanId = fleetIdentityInfo.getUrbanId();
					manualFleetInfo.fleetId = fleetIdentityInfo.getFleetId();
					manualFleetInfo.fleetAlias = fleetIdentityInfo.getUrbanId() + "*" + fleetIdentityInfo.getFleetId();
					LOGGER.debug("Calling createFleet() for fleet: " + manualFleetInfo.urbanId + "*" + manualFleetInfo.fleetId + "...");
					amdocsFleetInfo = amdocsUpdateBanConv.createFleet(manualFleetInfo);

					idenSubscriberInfo.setFleetClass(AttributeTranslator.stringFrombyte(amdocsFleetInfo.fleetClass));

				}

				// get list of available member ids
				String newWildCard =  wildCard.replace('*','%');
				if (newWildCard.length() < 5)
					newWildCard.concat(ufmiWildCards.substring(0,5 - newWildCard.length()));

				String[] availableMemberIds = amdocsIdenResourceServices.
				getAvailableUfmiList(fleetIdentityInfo.getUrbanId(),
						fleetIdentityInfo.getFleetId(), false, "0",
						newWildCard);

				// no available member ids found.
				if (availableMemberIds == null || availableMemberIds.length == 0) {
					String msg = new StringBuffer( "No member ids available matching criteria[")
					.append(fleetIdentityInfo.getUrbanId()).append("/")
					.append(fleetIdentityInfo.getFleetId()).append("/")
					.append(newWildCard).append("]")
					.toString();

					LOGGER.debug("Lookup UFMI for BAN" + idenSubscriberInfo.getBanId() + ", failed: " + msg);
					throw new TelusApplicationException("APP20004",msg);
				}
				
				
//				Moved below logic to service as DaoSupport.associateFleetAndTGsToBan acessing the EJB
				
				// Class-based fleet  -- moved to service 
				// - fleet might not associated to ban yet
				if (!isPtnBasedFleet) {
					// associate fleet to BAN
					ClientIdentity clientIdentity = getAmdocsTemplate().getSessionManager().getClientIdentity(sessionId);
					DaoSupport.associateFleetAndTGsToBan(amdocsUpdateBanConv, idenSubscriberInfo
							, new com.telus.eas.account.info.TalkGroupInfo[0], clientIdentity.getPrincipal()
							, clientIdentity.getCredential(), clientIdentity.getApplication());
				}

				// increase 'expected # of subscribers' for subscriber with UFMI
				DaoSupport.increaseExpectedNumberOfSubscribers(amdocsUpdateBanConv, idenSubscriberInfo.getBanId(), fleetIdentityInfo.getUrbanId(), fleetIdentityInfo.getFleetId());

				// reserve member id
				ufmiInfo.urbanId = fleetIdentityInfo.getUrbanId();
				ufmiInfo.fleetId = fleetIdentityInfo.getFleetId();
				ufmiInfo.memberId = Integer.parseInt(availableMemberIds[0].trim());

				ufmiInfo = amdocsIdenResourceServices.allocateUfmiManualResource(
						idenSubscriberInfo.getBanId(), ufmiInfo, idenSubscriberInfo.getIMSI(),"");

				LOGGER.debug(
						"Reserved UFMI: " + ufmiInfo.urbanId + "*" + ufmiInfo.fleetId + "*" +
						ufmiInfo.memberId);

				// set member id
				idenSubscriberInfo.getMemberIdentity0().setMemberId(new Integer(ufmiInfo.memberId).toString());

				LOGGER.debug("Leaving...");
				return idenSubscriberInfo;				
			}
		});
	}

	@Override
	public String[] retrieveAvailableMemberIds(final int urbanId, final int fleetId,
			final String newMemberIdPattern, final int maxMemberIds, String sessionId)
	throws ApplicationException {
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<String[]>() {

			@Override
			public String[] doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				IdenResourceServices amdocsIdenResourceServices = transactionContext.createBean(IdenResourceServices.class);
				String[] memberIds = new String[0];
				Collection<String> memberIdsFound = new ArrayList<String>();
				boolean moreMemberIdsAvailable = true;
				String startFromMemberId = "0";


				// get list of available member ids
				String memberIdPattern = newMemberIdPattern.replace('*','%');
				if (memberIdPattern.length() < 5)
					memberIdPattern.concat(ufmiWildCards.substring(0,5 - memberIdPattern.length()));

				// loop until the requested number of member ids is found OR no more ids are
				// available
				while (memberIdsFound.size() < maxMemberIds && moreMemberIdsAvailable) {
					memberIds = amdocsIdenResourceServices.getAvailableUfmiList(
							urbanId,
							fleetId,
							false,
							startFromMemberId,
							memberIdPattern);
					if (memberIds == null || memberIds.length < 10)
						moreMemberIdsAvailable = false;

					if (memberIds != null && memberIds.length > 0) {
						for (int i=0; i < memberIds.length; i++) {
							memberIdsFound.add(memberIds[i]);
							if (memberIdsFound.size() == maxMemberIds)
								break;
						}
						startFromMemberId = memberIds[memberIds.length-1].trim();
					}
				}

				memberIds = new String[memberIdsFound.size()];
				memberIds = (String[])memberIdsFound.toArray(memberIds);
				if (memberIdsFound.size() == 0)
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.NO_MEMBER_IDS_AVAILABLE_MATCHING_CRITERIA, "No member ids available matching criteria", "");

				return memberIds;
			}
		});
	}

	@Override
	public Collection<TalkGroupInfo> retrieveTalkGroupsBySubscriber(final int ban,
			final String subscriberId, String sessionId) throws ApplicationException {
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Collection<TalkGroupInfo>>() {

			@Override
			public Collection<TalkGroupInfo> doInTransaction(
					AmdocsTransactionContext transactionContext)
					throws Exception {
				UpdateIdenConv amdocsUpdateIdenConv = transactionContext.createBean(UpdateIdenConv.class);
				amdocs.APILink.datatypes.TalkGroupInfo[] amdocsTalkGroups = new amdocs.APILink.datatypes.TalkGroupInfo[0];
				com.telus.eas.account.info.TalkGroupInfo[] talkGroups = new com.telus.eas.account.info.TalkGroupInfo[0];

				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateIdenConv.setProductPK(ban,subscriberId);

				// retrieve talk groups
				amdocsTalkGroups = amdocsUpdateIdenConv.getTalkGroupList();

				// map Amdocs to Telus
				if (amdocsTalkGroups != null) {
					talkGroups = new com.telus.eas.account.info.TalkGroupInfo[amdocsTalkGroups.length];
					for (int i=0; i < amdocsTalkGroups.length; i++) {
						talkGroups[i] = new com.telus.eas.account.info.TalkGroupInfo();
						talkGroups[i].getFleetIdentity().setUrbanId(amdocsTalkGroups[i].urbanId);
						talkGroups[i].getFleetIdentity().setFleetId(amdocsTalkGroups[i].fleetId);
						talkGroups[i].setTalkGroupId(amdocsTalkGroups[i].talkGroupId);
						talkGroups[i].setName(amdocsTalkGroups[i].talkGroupAlias);
					}
				}
				return Arrays.asList(talkGroups);
			}

		}); 
	}

	@Override
	public boolean availableFleetList(final IDENSubscriberInfo pIdenSubscriberInfo,final FleetInfo fleetInfo,
			String sessionId) throws ApplicationException{

		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(
					AmdocsTransactionContext transactionContext)
					throws Exception {
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				amdocsUpdateBanConv.setBanPK(pIdenSubscriberInfo.getBanId());
				LOGGER.debug("Calling getFleetList()...");
				boolean fleetAlreadyAssociated = false;
				
				amdocs.APILink.datatypes.FleetInfo[] associatedFleets = amdocsUpdateBanConv.getFleetList();

				// check that fleet has been associate to this ban
				if (associatedFleets != null) {
					LOGGER.debug("# of associated fleets: " + associatedFleets.length);
					for (int i = 0; i < associatedFleets.length; i++) {
						LOGGER.debug(
								"associated fleet: " + associatedFleets[i].urbanId + "*" +
								associatedFleets[i].fleetId);
						if (associatedFleets[i].urbanId == fleetInfo.getIdentity0().getUrbanId() &&
								associatedFleets[i].fleetId == fleetInfo.getIdentity0().getFleetId()) {
							fleetAlreadyAssociated = true;
							break;
						}
					}
				}

				LOGGER.debug("fleet already associated: " + fleetAlreadyAssociated);
				return fleetAlreadyAssociated;
			}
		});

	}

}
