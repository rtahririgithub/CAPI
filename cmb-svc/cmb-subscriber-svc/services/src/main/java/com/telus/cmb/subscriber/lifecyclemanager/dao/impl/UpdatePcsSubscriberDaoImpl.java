package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.BrandPortInfo;
import amdocs.APILink.datatypes.CellularEquipmentInfo;
import amdocs.APILink.datatypes.ContractInfo;
import amdocs.APILink.datatypes.ImsiInfo;
import amdocs.APILink.datatypes.MigrateSubscriberInfo;
import amdocs.APILink.datatypes.ProductAdditionalInfo;
import amdocs.APILink.datatypes.ProductDepositInfo;
import amdocs.APILink.datatypes.ProductServicesInfo;
import amdocs.APILink.datatypes.SMBResourceAllocationInfo;
import amdocs.APILink.datatypes.ServiceInfo;
import amdocs.APILink.datatypes.SocInfo;
import amdocs.APILink.datatypes.UpdateProductAdditionalInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.UpdateCellularConv;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.portability.PortInEligibility;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.common.util.Utility;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdateIdenPcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdatePcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdateSubscriberDao;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.ResourceActivityInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.NumberGroupInfo;

public class UpdatePcsSubscriberDaoImpl extends UpdateSubscriberDaoImpl implements UpdateSubscriberDao, UpdateIdenPcsSubscriberDao, UpdatePcsSubscriberDao {

	private final Logger LOGGER = Logger.getLogger(UpdatePcsSubscriberDaoImpl.class);

	private Class<UpdateCellularConv> updatePcsConv = UpdateCellularConv.class;

	PcsSubscriberDaoHelperImpl pcsSubscriberDaoHelper;

	public void setPcsSubscriberDaoHelper(PcsSubscriberDaoHelperImpl pcsSubscriberDaoHelper) {
		this.pcsSubscriberDaoHelper = pcsSubscriberDaoHelper;
	}

	@Override
	public String[] retrieveAvailablePhoneNumbers(final int ban, final String subscriberId,
			final PhoneNumberReservationInfo phoneNumberReservation, final int maxNumbers,
			String sessionId) throws ApplicationException {
		final String startFromPhoneNumber = "0";
		String[] phoneNumbers = new String[0];
		try {
			if (((NumberGroupInfo)phoneNumberReservation.getNumberGroup()).getNumberRanges().length > 0) {

				phoneNumbers = pcsSubscriberDaoHelper.getSubscriberLifecycleHelper().retrieveAvailableCellularPhoneNumbersByRanges(phoneNumberReservation
						, startFromPhoneNumber
						, super.getWildCard(phoneNumberReservation.getPhoneNumberPattern())
						, phoneNumberReservation.isAsian()
						, maxNumbers);
			} else {

				phoneNumbers = super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<String[]>() {

					@Override
					public String[] doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
						// Set ProductPK (which also retrieves the BAN)
						UpdateCellularConv updateCellularConv = transactionContext.createBean(UpdateCellularConv.class);

						updateCellularConv.setProductPK(ban, subscriberId);					

						return retrievePhoneNumbers(updateCellularConv, ban, phoneNumberReservation, maxNumbers);		
					}
				});		
			}
		} catch (TelusAPIException e) {
			throw new SystemException(SystemCodes.CMB_SLM_DAO, "TelusAPIException encountered while calling getNumberRanges","", e);
		}
		
		return super.returnAvailablePhoneNumbers(phoneNumbers);
	}

	@Override
	public void changePhoneNumber(SubscriberInfo subscriberInfo,
			AvailablePhoneNumberInfo newPhoneNumber, String reasonCode,
			String dealerCode, String salesRepCode, String sessionId)
	throws ApplicationException {
		super.changePhoneNumber(updatePcsConv, subscriberInfo, newPhoneNumber, reasonCode, dealerCode, salesRepCode, sessionId);
	}

	@Override
	public void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan,
			Date activityDate, boolean transferOwnership,
			String activityReasonCode, String userMemoText, String dealerCode,
			String salesRepCode, String sessionId) throws ApplicationException {
		super.moveSubscriber(updatePcsConv, subscriberInfo, targetBan, activityDate
				, transferOwnership, activityReasonCode, userMemoText, dealerCode, salesRepCode, sessionId);		
	}

	@Override
	public void cancelPortedInSubscriber(
			final int ban, 
			final String subscriberId,
			final String deactivationReason, 
			final Date activityDate, 
			final String portOutInd,
			final boolean isBrandPort, 
			String sessionId) throws ApplicationException {
		super.cancelPortedInSubscriber(updatePcsConv, ban, subscriberId, deactivationReason, activityDate, portOutInd, isBrandPort, sessionId);
	}

	@Override
	public void migrateSubscriber(final SubscriberInfo subscriberInfo, final int targetBan,
			final Date activityDate, final SubscriberContractInfo subscriberContractInfo,
			final EquipmentInfo newPrimaryEquipmentInfo,
			final EquipmentInfo[] newSecondaryEquipmentInfo,
			final MigrationRequestInfo migrationRequestInfo, String sessionId)
	throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {				
				UpdateCellularConv amdocsUpdateCellularConv = transactionContext.createBean(UpdateCellularConv.class);
				MigrateSubscriberInfo  migrateSubscriberInfo = new MigrateSubscriberInfo();
				ProductServicesInfo productServicesInfo = null;
				CellularEquipmentInfo[] changeEquipmentInfoArray = null;

				boolean pricePlanChangeRequested = subscriberContractInfo != null;

				// print input to this method
				LOGGER.debug("Input for subscriber with banId=[" + subscriberInfo.getBanId() 
						+ "], subscriberId=[" + subscriberInfo.getSubscriberId() + "]...");
				LOGGER.debug(
						"  newPrimaryEquipmentInfo " +
						"getSerialNumber=[" + newPrimaryEquipmentInfo.getSerialNumber() + "] " +
						"getEquipmentType=[" + newPrimaryEquipmentInfo.getEquipmentType() + "]");
				if (newSecondaryEquipmentInfo != null) {
					for (int i=0; i < newSecondaryEquipmentInfo.length; i++) {
						LOGGER.debug(
								"  newSecondaryEquipmentInfo[" + i + "] " +
								"getSerialNumber=[" + newSecondaryEquipmentInfo[i].getSerialNumber() + "] " +
								"getEquipmentType=[" + newSecondaryEquipmentInfo[i].getEquipmentType() + "]");
					}
				} else {
					LOGGER.debug(" newSecondaryEquipmentInfo is null");
				}

				// Set ProductPK (which also retrieves the BAN)
				LOGGER.debug("amdocsUpdateCellularConv.setProductPK(subscriberInfo.getBanId(),subscriberInfo.getSubscriberId());");
				amdocsUpdateCellularConv.setProductPK(subscriberInfo.getBanId(),subscriberInfo.getSubscriberId());
				LOGGER.debug("end amdocsUpdateCellularConv.setProductPK(subscriberInfo.getBanId(),subscriberInfo.getSubscriberId());");

				// populate new/changed equipment array
				changeEquipmentInfoArray = populateMigrateeEquipmentArray(
						amdocsUpdateCellularConv.getEquipmentInfo(),
						newPrimaryEquipmentInfo,
						subscriberInfo.getEquipment0()
				);

				LOGGER.info(DaoSupport.extractCellularEquipmentInfoArray(subscriberInfo, changeEquipmentInfoArray));

				// change Equipment and Priceplan (and remove/add regular socs if required)
				// ------------------------------------------------------------------------
				if (pricePlanChangeRequested) {
					productServicesInfo = DaoSupport.mapTelusContractToAmdocsProductServices(amdocsUpdateCellularConv, subscriberInfo
							, subscriberContractInfo, true, true, false);
					LOGGER.debug("Calling amdocsUpdateProductConv being maped()...");
				}
				// Populate migrateSubscriberInfo
				migrateSubscriberInfo.targetBan = targetBan;
				migrateSubscriberInfo.activityDate = activityDate;
				migrateSubscriberInfo.activityReason = migrationRequestInfo.getMigrationReasonCode();
				migrateSubscriberInfo.migrationType = migrationRequestInfo.getMigrationType().getCode();
				migrateSubscriberInfo.depositInd = true;
				migrateSubscriberInfo.memoText = migrationRequestInfo.getUserMemoText() == null ? new String("") : migrationRequestInfo.getUserMemoText();


				if (migrationRequestInfo.getDealerCode() != null && migrationRequestInfo.getSalesRepCode() != null) {
					migrateSubscriberInfo.dealerCode = migrationRequestInfo.getDealerCode();
					migrateSubscriberInfo.salesCode = migrationRequestInfo.getSalesRepCode();
				}

				//newly introduced in KB9.1, for now just make it empty. Later, DepositInfo can accept information from
				//migrationRequestInfo.
				ProductDepositInfo pdi = new ProductDepositInfo();
				pdi.depositAmount = 0;
				pdi.depositKept = false;
				migrateSubscriberInfo.productDepositInfo = pdi;

				LOGGER.debug("amdocsUpdateCellularConv.migrateP2P(productServicesInfo, changeEquipmentInfoArray, migrateSubscriberInfo);");
				amdocsUpdateCellularConv.migrateP2P(productServicesInfo, changeEquipmentInfoArray, migrateSubscriberInfo);
				
				return null;
			}
		});
	}

	public CellularEquipmentInfo[] populateMigrateeEquipmentArray(
			CellularEquipmentInfo[] oldEquipmentInfoArray,
			com.telus.eas.equipment.info.EquipmentInfo newPrimaryEquipmentInfo,
			com.telus.eas.equipment.info.EquipmentInfo oldPrimaryEquipmentInfo
	) {

		TreeMap<String, CellularEquipmentInfo> equipmentHash = new TreeMap<String, CellularEquipmentInfo>();
		String currentPrimaryEquipmentSerialNumber = null;

		// put old/current equipment into hash table
		// - default equipment to NO_CHANGE or DELETE depending on whether secondary equipment
		//   was passed in or not
		// - also store current primary equipment serial number
		LOGGER.debug("Current Equipment ...");

		String newPrimarySerialNumber = newPrimaryEquipmentInfo.getSerialNumber();
		if (newPrimaryEquipmentInfo.isUSIMCard()) {
			newPrimarySerialNumber = com.telus.eas.equipment.info.EquipmentInfo.DUMMY_ESN_FOR_USIM;
		}
		for (int i = 0; i < oldEquipmentInfoArray.length; i++) {
			LOGGER.debug(
					"  oldEquipmentInfoArray[" + i + "]: " +
					"serialNumber=[" + oldEquipmentInfoArray[i].serialNumber + "] " +
					"activateInd=[" + oldEquipmentInfoArray[i].activateInd + "] " +
					"primaryInd=[" + oldEquipmentInfoArray[i].primaryInd + "]");
			oldEquipmentInfoArray[i].equipmentMode =
				oldEquipmentInfoArray[i].primaryInd && oldEquipmentInfoArray[i].serialNumber.equals(newPrimarySerialNumber)?
						amdocs.APILink.datatypes.EquipmentInfo.NO_CHANGE :
							amdocs.APILink.datatypes.EquipmentInfo.DELETE;

			equipmentHash.put(oldEquipmentInfoArray[i].serialNumber, oldEquipmentInfoArray[i]);
			if (oldEquipmentInfoArray[i].primaryInd == true) currentPrimaryEquipmentSerialNumber = oldEquipmentInfoArray[i].serialNumber;
		}

		ImsiInfo [] imsiList = null;
		boolean isNetworkChange = false;

		// I. primary equipment
		// --------------------
		// if new primary equipment exists
		// - set equipmentMode to INSERT
		// - set primary indicator to true
		if (equipmentHash.get(newPrimarySerialNumber) == null) { //equipment changed
			CellularEquipmentInfo cellularEquipmentInfo = new CellularEquipmentInfo();
			cellularEquipmentInfo.equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.INSERT;
			cellularEquipmentInfo.serialNumber = newPrimarySerialNumber;
			cellularEquipmentInfo.activateInd = true;
			cellularEquipmentInfo.primaryInd = true;
			cellularEquipmentInfo.base = CellularEquipmentInfo.EQUIPMENT_BASE_DECIMAL;
			cellularEquipmentInfo.equipmentType = AttributeTranslator.byteFromString(newPrimaryEquipmentInfo.getEquipmentType());
			cellularEquipmentInfo.possession = (byte)'P';

			equipmentHash.put(cellularEquipmentInfo.serialNumber, cellularEquipmentInfo);

			if ( newPrimaryEquipmentInfo.isUSIMCard() ) { 
				//CDMA to HSPA

				imsiList = DaoSupport.extractImsiInfo(newPrimaryEquipmentInfo, ImsiInfo.INSERT );
				isNetworkChange = true;
			} 
			else if ( oldPrimaryEquipmentInfo.isUSIMCard() ){
				//HSPA to CDMA

				isNetworkChange = true;
				imsiList = DaoSupport.extractImsiInfo(oldPrimaryEquipmentInfo, ImsiInfo.DELETE );

			} else {
				//CDMA to CDMA (changed)
				//do nothing
			}
		} else {
			//no equipment changed
			if (newPrimaryEquipmentInfo.isUSIMCard()) {
				//HSPA to HSPA, we need to compare the USIM

				if (newPrimaryEquipmentInfo.getProfile().isSame(oldPrimaryEquipmentInfo.getProfile()) == false) {
					CellularEquipmentInfo oldCellularEquipmentInfo = (CellularEquipmentInfo) equipmentHash.get(currentPrimaryEquipmentSerialNumber);
					//update the IMSI only

					//defect PROD00133589 fix: 
					// when sending UPDATE ( because we think this is SIM swap ), will get Tuxedo error like:
					//     Message 4: No ESN for Subscriber No: ${subscrier_no}, Product Type (C), BAN: ${new ban #}.
					//What really happens is in KB, IMSI has no relation to equipment, although they use CellularEquipmentInfo as placeholder to 
					//carry the IMSI info when passing the IMSI parameter. So to KB, really there is no change the the equipment - still the 
					//same dummy ESN. And when equipmentMode is NO_CHANGE, KB internal will move this equipment to the new subscriber as part
					//of migration transaction. 
					//As result, we should be sending equipmentMode as NO_CHANGE.   
					oldCellularEquipmentInfo.equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.NO_CHANGE;
					imsiList = DaoSupport.extractImsiInfos(newPrimaryEquipmentInfo, oldPrimaryEquipmentInfo );
				}
			} else {
				//CDMA to CDMA
				//do nothing
			}
		}

		CellularEquipmentInfo[] equipments = (CellularEquipmentInfo[]) equipmentHash.values().toArray(new CellularEquipmentInfo[equipmentHash.size()]);

		if (imsiList != null) {
			//This means there are IMSI need to be added or deleted

			//As per KB 9.9, always put imsiList and isNetworkChagne flag to the first element in the array
			//and it seem the equipmentMode affect the processing of imsiList, so we have to make sure 
			//that the first element is not equipmentMode=D.

			//sort the CellularEquipment by equipmentMode in following order: 'I'; 'U'; 'N'; 'D' 
			Arrays.sort(equipments, new DaoSupport.CellularEquipmentModeComparator());

			//then put imsiList and isNetworkChagne flag to the first element in the array
			equipments[0].isNetworkChange = isNetworkChange;

			//when change HSPA to CDMA, if we send ISMI list with 'DELETE', we will get a error: 
			//   id=1116460; No need to provide the IMSI list for this change
			//not sure this is per KB design or a bug.
			//work-around: only when new equipment is USIMCard, then we send imsiList
			if (newPrimaryEquipmentInfo.isUSIMCard())
				equipments[0].imsiList = imsiList;
		}

		return equipments;
	}

	@Override
	public void setPortTypeToPortIn(final int ban, final String subscriberId, final Date sysDate,
			String sessionId) throws ApplicationException {
		super.setPortTypeToPortIn(updatePcsConv, ban, subscriberId, sysDate, sessionId);
	}

	@Override
	public void portChangeSubscriberNumber(final SubscriberInfo subscriberInfo,
			final AvailablePhoneNumberInfo newPhoneNumber, final String reasonCode,
			String dealerCode, String salesRepCode, final String portProcessType,
			final int oldBanId, final String oldSubscriberId, String sessionId)
	throws ApplicationException {
		
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				
				UpdateCellularConv amdocsUpdateCellularConv = transactionContext.createBean(UpdateCellularConv.class);

				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateCellularConv.setProductPK(subscriberInfo.getBanId(),subscriberInfo.getSubscriberId());

				// Change Phone Number
				LOGGER.debug("calling portChangeSubscriberNumber()...");
				/* Defect PROD00085773
				      ProductAdditionalInfo productAdditionalInfo = new ProductAdditionalInfo();
				      ContractInfo ci = new ContractInfo();

				      if (dealerCode!= null) {
				      	if (salesRepCode == null)
				      		salesRepCode="0000";
				      	ci.dealerCode = dealerCode;
				      	ci.salesCode = salesRepCode;
				      }else{
				    	  ci.dealerCode = newPhoneNumber.getNumberGroup().getDefaultDealerCode();
				    	  ci.salesCode = newPhoneNumber.getNumberGroup().getDefaultSalesCode();
				      }
				 */
				// Defect PROD00085773 
				// Two parameters:	ContractInfo and ProductAdditionalInfo = null, 
				// because we are not changing dealer(Contract) info and Product Additional info during the 
				// phone number change
				if (PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT.equals( portProcessType ) ) {
					BrandPortInfo brandPortInfo = new BrandPortInfo();
					brandPortInfo.isBrandPortActivity=true;
					brandPortInfo.previousBan = oldBanId;
					brandPortInfo.previousSubscriber = oldSubscriberId;
					amdocsUpdateCellularConv.portChangeSubscriberNumber(newPhoneNumber.getPhoneNumber(), reasonCode, null, null, brandPortInfo);
				} else {
					amdocsUpdateCellularConv.portChangeSubscriberNumber(newPhoneNumber.getPhoneNumber(), reasonCode, null, null);
				}
				
				return null;				    
			}
		});		
	}

	@Override
	public void suspendPortedInSubscriber(int ban, String subscriberId,
			String deactivationReason, Date activityDate, String portOutInd,
			String sessionId) throws ApplicationException {
		super.suspendPortedInSubscriber(updatePcsConv, ban, subscriberId, deactivationReason, activityDate, portOutInd, sessionId);
	}

	@Override
	public void resetVoiceMailPassword(int ban, String subscriberId,
			String[] voiceMailSocAndFeature, String sessionId)
	throws ApplicationException {
		super.resetVoiceMailPassword(updatePcsConv, ban, subscriberId, voiceMailSocAndFeature, sessionId);
	}

	@Override
	public void updateBirthDate(final SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException {
		
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				
				UpdateCellularConv amdocsUpdateCellularConv = transactionContext.createBean(UpdateCellularConv.class);
				SimpleDateFormat df = new SimpleDateFormat("MMdd");
				Date birthDate = new Date();
				boolean sfbcSocFound = false;

				birthDate = subscriberInfo.getBirthDate() == null ? subscriberInfo.getStartServiceDate() : subscriberInfo.getBirthDate();

				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateCellularConv.setProductPK(subscriberInfo.getBanId(),subscriberInfo.getSubscriberId());

				// check that current service agreement has 'Free Birthday Calling' soc
				ProductServicesInfo currentServices = amdocsUpdateCellularConv.getProductServices();
				for (int i=0; i < currentServices.addtnlSrvs.length; i++) {
					if (currentServices.addtnlSrvs[i].soc.soc.trim().equals("SFBC"))  sfbcSocFound = true;
				}
				if (!sfbcSocFound) {
					LOGGER.debug("Leaving... because SOC SFBC not on current service agreement");
					return null;
				}

				ServiceInfo[] serviceInfo = new ServiceInfo[1];
				serviceInfo[0] = new ServiceInfo();
				serviceInfo[0].soc.soc = "SFBC";
				serviceInfo[0].soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;

				amdocs.APILink.datatypes.ServiceFeatureInfo[] feature = new amdocs.APILink.datatypes.ServiceFeatureInfo[1];
				feature[0] = new amdocs.APILink.datatypes.ServiceFeatureInfo();
				feature[0].featureCode = "FBC";
				feature[0].ftrParam = "DATE-OF-BIRTH=" + df.format(birthDate) + "@";
				feature[0].transactionType = amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE;
				serviceInfo[0].feature = feature;

				LOGGER.debug("Calling changeServiceAgreement() for subscriber=[" + subscriberInfo.getSubscriberId() + "] with FBC parameter=[" + feature[0].ftrParam + "] ...");

				amdocsUpdateCellularConv.changeServiceAgreement(serviceInfo);
				
				return null;
			}
		});		
	}

	@Override
	public void changeSerialNumberAndMaybePricePlan(
			final SubscriberInfo subscriberInfo,
			final EquipmentInfo newPrimaryEquipmentInfo,
			final EquipmentInfo[] newSecondaryEquipmentInfo,
			final SubscriberContractInfo subscriberContractInfo, final String dealerCode,
			final String salesRepCode, final PricePlanValidationInfo pricePlanValidation, String sessionId)
	throws ApplicationException {
		
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				
				UpdateCellularConv amdocsUpdateCellularConv = transactionContext.createBean(UpdateCellularConv.class);
				ProductServicesInfo productServicesInfo = new ProductServicesInfo();
				CellularEquipmentInfo[] changeEquipmentInfoArray = null;

				boolean pricePlanChangeRequested = subscriberContractInfo != null;

				try {

					// print input to this method
					LOGGER.debug("Input for subscriber with banId=[" + subscriberInfo.getBanId() + "], subscriberId=[" + subscriberInfo.getSubscriberId() + "]...");
					LOGGER.debug("  newPrimaryEquipmentInfo " + "getSerialNumber=[" + newPrimaryEquipmentInfo.getSerialNumber() + "] " + "getEquipmentType=[" + newPrimaryEquipmentInfo.getEquipmentType() + "]");
					if (newSecondaryEquipmentInfo != null) {
						for (int i = 0; i < newSecondaryEquipmentInfo.length; i++) {
							LOGGER.debug(
									"  newSecondaryEquipmentInfo[" + i + "] " +
									"getSerialNumber=[" + newSecondaryEquipmentInfo[i].getSerialNumber() + "] " +
									"getEquipmentType=[" + newSecondaryEquipmentInfo[i].getEquipmentType() + "]");
						}
					} else {
						LOGGER.debug("  newSecondaryEquipmentInfo is null");
					}
					
					if (pricePlanValidation != null && LOGGER.isDebugEnabled()) {
						LOGGER.debug("isModified="+pricePlanValidation.isModified()+",=validateCurrent="+pricePlanValidation.validateCurrent()+",validateEquipmentServiceMatch="+pricePlanValidation.validateEquipmentServiceMatch()+
								",validateForSale="+pricePlanValidation.validateForSale()+",validatePricePlanServiceGrouping="+pricePlanValidation.validatePricePlanServiceGrouping()+",validateProvinceServiceMatch="+pricePlanValidation.validateProvinceServiceMatch());
					}

					// Set ProductPK (which also retrieves the BAN)
					amdocsUpdateCellularConv.setProductPK(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());

					// populate new/changed equipment array
					changeEquipmentInfoArray = DaoSupport.populateChangeEquipmentArray(
							amdocsUpdateCellularConv.getEquipmentInfo(),
							newPrimaryEquipmentInfo,
							newSecondaryEquipmentInfo,
							subscriberInfo.getEquipment0() 
					);

					LOGGER.info(DaoSupport.extractCellularEquipmentInfoArray(subscriberInfo, changeEquipmentInfoArray));

					// change Equipment and Priceplan (and remove/add regular socs if required)
					// ------------------------------------------------------------------------
					if (pricePlanChangeRequested) {
						productServicesInfo = DaoSupport.mapTelusContractToAmdocsProductServices(amdocsUpdateCellularConv, subscriberInfo, subscriberContractInfo, true, true, true, false);
						LOGGER.debug("Calling changePricePlan()...");
						ContractInfo contractInfo = new ContractInfo();
						if (dealerCode != null && !dealerCode.equals("")) {
							contractInfo.dealerCode = dealerCode;
							contractInfo.salesCode = salesRepCode == null ? "" : salesRepCode;
						} else 
							contractInfo = null;

						boolean modified = pricePlanValidation.isModified();
						if (modified) {
							LOGGER.debug("Calling changeProductServices() with dealerCode = " + contractInfo.dealerCode + " and salesCode = " + contractInfo.salesCode + "...");
							amdocs.APILink.datatypes.PricePlanValidationInfo   ppValidationInfo = new amdocs.APILink.datatypes.PricePlanValidationInfo();
							if (modified) {
								ppValidationInfo.currentSoc = pricePlanValidation.validateCurrent(); 
								ppValidationInfo.equipmentType = newPrimaryEquipmentInfo.isHSPA() ? false : pricePlanValidation.validateEquipmentServiceMatch();	  
								ppValidationInfo.forSale = pricePlanValidation.validateForSale();
								ppValidationInfo.ppSocGrouping = pricePlanValidation.validatePricePlanServiceGrouping();
								ppValidationInfo.province = pricePlanValidation.validateProvinceServiceMatch();
							}
							amdocsUpdateCellularConv.changeProductServices(productServicesInfo, null, contractInfo, changeEquipmentInfoArray, ppValidationInfo);
						} else {
							if (contractInfo != null) {
								LOGGER.debug("Calling changePricePlan() with dealerCode = " + contractInfo.dealerCode + " and salesCode = " + contractInfo.salesCode + "...");
								amdocsUpdateCellularConv.changePricePlan(productServicesInfo, contractInfo, changeEquipmentInfoArray);
							} else {
								LOGGER.debug("Calling changePricePlan()...");
								amdocsUpdateCellularConv.changePricePlan(productServicesInfo, changeEquipmentInfoArray);
							}
						}
					} else {
						LOGGER.debug("Calling changeEquipmentInfo()...");
						amdocsUpdateCellularConv.changeEquipmentInfo(changeEquipmentInfoArray);
					}

					return null;

				} catch (ValidateException ve) {
					ApplicationException rootException = new ApplicationException(SystemCodes.AMDOCS, String.valueOf(ve.getErrorInd()), ve.getErrorMsg(), "", ve);
					if (ve.getErrorInd() == 1116210) {
						LOGGER.debug("New Serial Number In Use Exception Occurred", ve);
						String exceptionMsg = "The new serial number is in use. [pBan=" + subscriberInfo.getBanId() + ", pSubscriberId="
						+ subscriberInfo.getSubscriberId() + ", newSerialNumber=" + newPrimaryEquipmentInfo.getSerialNumber() + ", pNewEquipmentType=" + newPrimaryEquipmentInfo.getEquipmentType() + "]";
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.NEW_SERIAL_IS_IN_USE, exceptionMsg, "", rootException);
					} else if (ve.getErrorInd() == 1117000) {
						LOGGER.debug(ve.getErrorMsg(), ve);
						throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.APP20003, ve.getErrorMsg(), "", rootException);						
					} else {
						throw ve;
					}
				}
			}
		});
	}

	@Override
	public void changeResources(final SubscriberInfo subscriberInfo, final List<ResourceActivityInfo> resourceList, final Date activityDate, final String sessionId) throws ApplicationException {
		
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {

				// Get a reference to the Amdocs EJB
				UpdateCellularConv amdocsUpdateCellularConv = transactionContext.createBean(UpdateCellularConv.class);
				// Set ProductPK
				amdocsUpdateCellularConv.setProductPK(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());
				// Map the ResourceActivityInfo to the SMBResourceAllocationInfo
				List<SMBResourceAllocationInfo> infoList = new ArrayList<SMBResourceAllocationInfo>();
				for (ResourceActivityInfo resourceActivity : resourceList) {
					SMBResourceAllocationInfo info = new SMBResourceAllocationInfo();					
					info.activityDate = activityDate == null ? new Date() : activityDate;
					info.resourceType = AttributeTranslator.byteFromString(resourceActivity.getResource().getResourceType());
					info.resourceNum = resourceActivity.getResource().getResourceNumber();
					info.resourceMode = AttributeTranslator.byteFromString(resourceActivity.getResourceActivity());
					infoList.add(info);
				}
				// Call the Amdocs EJB to change the resources
				amdocsUpdateCellularConv.changeSMBResource(infoList.toArray(new SMBResourceAllocationInfo[infoList.size()]));
				
				return null;
			}
		});
	}
	
	@Override
	public void changeSeatGroup(final SubscriberInfo subscriberInfo, final String seatGroupId, final String sessionId) throws ApplicationException {
		
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {

				// Get a reference to the Amdocs EJB
				UpdateCellularConv amdocsUpdateCellularConv = transactionContext.createBean(UpdateCellularConv.class);
				// Set ProductPK
				amdocsUpdateCellularConv.setProductPK(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());
				// Retrieve the subscriber's current ProductAdditionalInfo attributes
				ProductAdditionalInfo amdocsProductAdditionalInfo = amdocsUpdateCellularConv.getProductAdditionalInfo();
				// Compare and map the seat group ID to the UpdateProductAdditionalInfo
				UpdateProductAdditionalInfo amdocsUpdateProductAdditionalInfo = new UpdateProductAdditionalInfo();
				if (Utility.compare(amdocsProductAdditionalInfo.seatGroup, seatGroupId) != 0) {
					amdocsUpdateProductAdditionalInfo.seatGroup = translateSeatGroupId(seatGroupId);
					// Call the Amdocs EJB to change the seat group
					amdocsUpdateCellularConv.changeSubscriber(amdocsUpdateProductAdditionalInfo);
				}

				return null;
			}
		});
	}
	
	private String translateSeatGroupId(String seatGroupId) {
		// If we're trying to set the seatGroupId to an empty/null value, we need to send the string value 'NULL' to Amdocs
		if ((seatGroupId == null) || (seatGroupId.trim().isEmpty())) {
			return "NULL";
		}
		// Otherwise, set the seatGroupId to whatever the non-null/non-empty string value is
		return seatGroupId;
	}
	
	@Override
	public void resetCSCSubscription(int ban, String subscriberId, String[] cscFeature, String sessionId) throws ApplicationException {
		super.resetCSCSubscription(updatePcsConv, ban, subscriberId, cscFeature,sessionId);
	}
	
	
}