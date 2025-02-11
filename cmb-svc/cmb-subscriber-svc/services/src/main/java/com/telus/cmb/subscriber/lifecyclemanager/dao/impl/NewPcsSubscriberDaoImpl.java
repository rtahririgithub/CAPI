package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.BrandPortInfo;
import amdocs.APILink.datatypes.CellularEquipmentInfo;
import amdocs.APILink.datatypes.EquipmentInfo;
import amdocs.APILink.datatypes.ImsiInfo;
import amdocs.APILink.datatypes.MigrateM2PInfo;
import amdocs.APILink.datatypes.MigrateReserveInfo;
import amdocs.APILink.datatypes.PortReserveInfo;
import amdocs.APILink.datatypes.ProductActivityInfo;
import amdocs.APILink.datatypes.ProductServicesInfo;
import amdocs.APILink.datatypes.ProductServicesValidationInfo;
import amdocs.APILink.datatypes.ServiceInfo;
import amdocs.APILink.datatypes.SocInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.NewCellularConv;
import amdocs.APILink.sessions.interfaces.NewProductConv;
import amdocs.APILink.sessions.interfaces.UpdateCellularConv;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.ServicesValidation;
import com.telus.api.portability.PortInEligibility;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewIdenPcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewPcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewSubscriberDao;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.NumberGroupInfo;

public class NewPcsSubscriberDaoImpl extends NewSubscriberDaoImpl implements
NewSubscriberDao, NewIdenPcsSubscriberDao, NewPcsSubscriberDao {

	private Class<NewCellularConv> newPcsConv = NewCellularConv.class;

	private final Logger LOGGER = Logger.getLogger(NewPcsSubscriberDaoImpl.class);	

	PcsSubscriberDaoHelperImpl pcsSubscriberDaoHelper;

	public void setPcsSubscriberDaoHelper(
			PcsSubscriberDaoHelperImpl pcsSubscriberDaoHelper) {
		this.pcsSubscriberDaoHelper = pcsSubscriberDaoHelper;
	}

	@Override
	public String[] retrieveAvailablePhoneNumbers(final int ban,
			final PhoneNumberReservationInfo phoneNumberReservation, final int maxNumbers,
			String sessionId) throws ApplicationException {
		String[] phoneNumbers = new String[0];
		final String startFromPhoneNumber = "0";

		try {
			if (((NumberGroupInfo)phoneNumberReservation.getNumberGroup()).getNumberRanges() == null) {
				throw new ApplicationException(SystemCodes.CMB_SLM_DAO, "PhoneNumberReservation.getNumberGroup().getNumberRanges() should not be null.","");
			}

			if (((NumberGroupInfo)phoneNumberReservation.getNumberGroup()).getNumberRanges().length > 0) {
				phoneNumbers = pcsSubscriberDaoHelper.getSubscriberLifecycleHelper().retrieveAvailableCellularPhoneNumbersByRanges(phoneNumberReservation
						, startFromPhoneNumber
						, super.getWildCard(phoneNumberReservation.getPhoneNumberPattern())
						, phoneNumberReservation.isAsian()
						, maxNumbers);
			} else {
				phoneNumbers = super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<String[]>() {

					@Override
					public String[] doInTransaction(
							AmdocsTransactionContext transactionContext)
					throws Exception {
						ProductActivityInfo productActivityInfo = new ProductActivityInfo();
						// Set ProductPK (which also retrieves the BAN)
						NewCellularConv newCellularConv = transactionContext.createBean(NewCellularConv.class);

						newCellularConv.setProductPK(ban);						

						if (phoneNumberReservation.getNumberGroup().getNumberLocation() == null) {
							throw new ApplicationException(SystemCodes.CMB_SLM_DAO, "PhoneNumberReservation.getNumberGroup().getNumberLocation() should not be null.","");
						}
						// populate ProductActivityInfo
						productActivityInfo.numberLocation = phoneNumberReservation.getNumberGroup().getNumberLocation();

						if (productActivityInfo.numberLocation.equals("PPE")) {
							productActivityInfo.dealerCode = phoneNumberReservation.getNumberGroup().getDefaultDealerCode();
							productActivityInfo.salesCode = phoneNumberReservation.getNumberGroup().getDefaultSalesCode();
							newCellularConv.setProductActivityInfo(productActivityInfo);
						}

						return retrievePhoneNumbers(newCellularConv, ban, phoneNumberReservation, maxNumbers);						
					}
				});		
			}
		} catch (TelusAPIException e) {
			throw new SystemException(SystemCodes.CMB_SLM_DAO, "TelusAPIException encountered while calling getNumberRanges", "",e);
		}
		return super.returnAvailablePhoneNumbers(phoneNumbers);
	}

	@Override
	public void createSubscriber(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, boolean activate,
			boolean dealerHasDeposit, boolean portedIn,
			ServicesValidation srvValidation, String portProcessType,
			int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException {
		super.createSubscriber(newPcsConv, subscriberInfo, subscriberContractInfo, activate
				, dealerHasDeposit, portedIn, srvValidation, portProcessType, oldBanId, oldSubscriberId, sessionId);

	}

	@Override
	protected void setProductEquipmentInfo(NewProductConv newProductConv,
			SubscriberInfo subscriberInfo) throws RemoteException, ValidateException {
		NewCellularConv newCellularConv = (NewCellularConv) newProductConv;
		boolean hspa =  isHSPA(subscriberInfo);

		// populate Equipment Info - possibly multiple equipment
		CellularEquipmentInfo[] cellularEquipmentInfo = null;
		int secondaryEquipmentCount = subscriberInfo.getSecondarySerialNumbers() != null ? subscriberInfo.getSecondarySerialNumbers().length : 0;
		cellularEquipmentInfo = new CellularEquipmentInfo[secondaryEquipmentCount + 1];
		// - populate primary equipment
		cellularEquipmentInfo[0] = new CellularEquipmentInfo();
		cellularEquipmentInfo[0].equipmentType = AttributeTranslator.byteFromString(subscriberInfo.getEquipmentType());
		cellularEquipmentInfo[0].possession = (byte)'P';
		cellularEquipmentInfo[0].base = CellularEquipmentInfo.EQUIPMENT_BASE_DECIMAL;
		if(hspa && !isHSIAEquipment(subscriberInfo) && !isVOIPEquipment(subscriberInfo)) {
			cellularEquipmentInfo[0].serialNumber = com.telus.eas.equipment.info.EquipmentInfo.DUMMY_ESN_FOR_USIM;
			cellularEquipmentInfo[0].imsiList  = DaoSupport.extractImsiInfo(subscriberInfo.getEquipment0(), ImsiInfo.INSERT);
		}
		else
			cellularEquipmentInfo[0].serialNumber = subscriberInfo.getSerialNumber();
		cellularEquipmentInfo[0].activateInd = true;
		cellularEquipmentInfo[0].primaryInd = true;
		cellularEquipmentInfo[0].equipmentMode = EquipmentInfo.INSERT;
		// - populate secondary equipment
		if (secondaryEquipmentCount > 0) {
			for (int i=0; i < subscriberInfo.getSecondarySerialNumbers().length; i++) {
				cellularEquipmentInfo[i+1] = new CellularEquipmentInfo();
				cellularEquipmentInfo[i+1].equipmentType = AttributeTranslator.byteFromString(subscriberInfo.getEquipmentType());
				cellularEquipmentInfo[i+1].possession = (byte)'P';
				cellularEquipmentInfo[i+1].base = CellularEquipmentInfo.EQUIPMENT_BASE_DECIMAL;
				cellularEquipmentInfo[i+1].serialNumber = subscriberInfo.getSecondarySerialNumbers()[i];
				cellularEquipmentInfo[i+1].activateInd = true;
				cellularEquipmentInfo[i+1].primaryInd = false;
				cellularEquipmentInfo[i+1].equipmentMode = EquipmentInfo.INSERT;
			}
		}
		// print input to Amdocs
		/*
		      print(getClass().getName(),methodName,"Array passed to Amdocs...",INFO);
		      for (int i=0; i < cellularEquipmentInfo.length; i++) {
		      	print(getClass().getName(),methodName,
		      			"  changeEquipmentInfoArray[" + i + "]: " +
						"equipmentMode=[" + AttributeTranslator.stringFrombyte(cellularEquipmentInfo[i].equipmentMode) + "] " +
						"serialNumber=[" + cellularEquipmentInfo[i].serialNumber + "] " +
						"activateInd=[" + cellularEquipmentInfo[i].activateInd + "] " +
						"primaryInd=[" + cellularEquipmentInfo[i].primaryInd + "] " +
						"base=[" + cellularEquipmentInfo[i].base + "] " +
						"equipmentType=[" + cellularEquipmentInfo[i].equipmentType + "] " +
						"possession=[" + cellularEquipmentInfo[i].possession + "]"
						,INFO);
		      }
		 */
		LOGGER.info(DaoSupport.extractCellularEquipmentInfoArray(subscriberInfo, cellularEquipmentInfo));
		newCellularConv.setEquipmentInfo(cellularEquipmentInfo);
		LOGGER.debug("EquipmentInfo set");		
	}

	private boolean isHSPA(SubscriberInfo subscriberInfo) {
		return (subscriberInfo.getEquipment0() != null? subscriberInfo.getEquipment0().isHSPA() : subscriberInfo.isHSPA());
	}

	private boolean isHSIAEquipment(SubscriberInfo subscriberInfo) {
		return (subscriberInfo.getEquipment0() != null ? subscriberInfo.getEquipment0().isHSIADummyEquipment():false);
	}
	private boolean isVOIPEquipment(SubscriberInfo subscriberInfo) {
		return (subscriberInfo.getEquipment0() != null ? subscriberInfo.getEquipment0().isVOIPDummyEquipment() : false);
	}
	
	@Override
	protected void activateSubscriber(NewProductConv newProductConv, String portProcessType, int oldBanId, String oldSubscriberId) throws RemoteException, ValidateException {		
		if (PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT.equals( portProcessType ) ) {
			BrandPortInfo brandPortInfo = new BrandPortInfo();
			brandPortInfo.isBrandPortActivity=true;
			brandPortInfo.previousBan = oldBanId;
			brandPortInfo.previousSubscriber = oldSubscriberId;
			newProductConv.activateSubscriber(brandPortInfo);
		} else {
			newProductConv.activateSubscriber();
		}
	}

	
	@Override
	protected void setEquipmentTypeMatch(
			SubscriberInfo subscriberInfo,
			ProductServicesValidationInfo validationInfo,
			ServicesValidation srvValidation) {
		validationInfo.equipmentTypeMatch = (isHSPA(subscriberInfo) ? false : srvValidation.validateEquipmentServiceMatch());	
	}

	@Override
	public void releaseSubscriber(SubscriberInfo subscriberInfo,
			String sessionId) throws ApplicationException {
		super.releaseSubscriber(newPcsConv, subscriberInfo, sessionId);		
	}

	@Override
	public SubscriberInfo reserveLikePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation, String sessionId)
	throws ApplicationException {
		return super.reserveLikePhoneNumber(newPcsConv, subscriberInfo, phoneNumberReservation, sessionId);
	}

	@Override
	protected String[] retrieveAvailableSubscriberList(
			NewProductConv newProductConv,
			PhoneNumberReservationInfo phoneNumberReservation,
			String numberPattern) throws Exception {
		if (((NumberGroupInfo)phoneNumberReservation.getNumberGroup()).getNumberRanges().length > 0){ 
			return pcsSubscriberDaoHelper.getSubscriberLifecycleHelper().retrieveAvailableCellularPhoneNumbersByRanges(phoneNumberReservation
					,"0"
					,numberPattern
					,phoneNumberReservation.isAsian()
					,1);
		} else { 
			return super.retrieveAvailableSubscriberList(newProductConv, phoneNumberReservation, numberPattern);
		}
	}

	@Override
	public SubscriberInfo reservePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation, boolean isOfflineReservation,String sessionId)
	throws ApplicationException {
		return super.reservePhoneNumber(newPcsConv, subscriberInfo, phoneNumberReservation, isOfflineReservation,sessionId);
	}

	@Override
	public void migrateSubscriber(
			final SubscriberInfo srcSubscriberInfo,
			final SubscriberInfo newSubscriberInfo,
			final Date activityDate,
			final SubscriberContractInfo subscriberContractInfo,
			final com.telus.eas.equipment.info.EquipmentInfo newPrimaryEquipmentInfo,
			final com.telus.eas.equipment.info.EquipmentInfo[] newSecondaryEquipmentInfo,
			final MigrationRequestInfo migrationRequestInfo, String sessionId)
	throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				NewCellularConv amdocsNewCellularConv = transactionContext.createBean(newPcsConv);

				MigrateReserveInfo reserveInfo = newMigrateReserveInfo(srcSubscriberInfo, migrationRequestInfo);
				ProductActivityInfo productActivityInfo = newProductActivityInfo(activityDate, migrationRequestInfo);

				StringBuffer sb = new StringBuffer("Migration[")
				.append( migrationRequestInfo.getMigrationType().getCode())
				.append(", resson:").append(migrationRequestInfo.getMigrationReasonCode()).append("] ")
				.append( "phone:" ).append(srcSubscriberInfo.getPhoneNumber())
				.append(" from BAN:").append( srcSubscriberInfo.getBanId() ).append(" to BAN:").append( newSubscriberInfo.getBanId())
				.append( ", dealer(").append( productActivityInfo.dealerCode).append("/").append( productActivityInfo.salesCode ).append(")")
				.append(" ; date: ").append( productActivityInfo.activityDate );
				LOGGER.info(sb.toString());

				amdocsNewCellularConv.setProductPK(newSubscriberInfo.getBanId());
				amdocsNewCellularConv.setProductActivityInfo(productActivityInfo);
				amdocsNewCellularConv.migrateReserveCtn(reserveInfo);

				//Feb 20,2008 M.Liao; : save the subscriberId, so that during rollback,  releaseMigrateReserveCtn will have it
				newSubscriberInfo.setSubscriberId(amdocsNewCellularConv.getSubscriberNumber());

				try {
					amdocsNewCellularConv.setProductActivityInfo(productActivityInfo);
					//after reserve CTN, we need to set the equipment and contract information
					amdocsNewCellularConv.setEquipmentInfo( populateEquipmentInfoArray( newPrimaryEquipmentInfo, newSecondaryEquipmentInfo ) );
					amdocsNewCellularConv.setProductCommitmentInfo((short)subscriberContractInfo.getCommitmentMonths());
					ProductServicesInfo productServicesInfo =
						DaoSupport.mapTelusContractToAmdocsProductServices(amdocsNewCellularConv, newSubscriberInfo
								, subscriberContractInfo, true, true, false);

					ProductServicesValidationInfo validationInfo = new ProductServicesValidationInfo ();
					validationInfo.ppSocGrouping = true;
					validationInfo.provinceMatch = true;
					validationInfo.equipmentTypeMatch = newPrimaryEquipmentInfo.isHSPA()? false: true;      
					amdocsNewCellularConv.setProductServices(productServicesInfo, validationInfo);

					MigrateM2PInfo migrateInfo = newMigrateM2PInfo(activityDate, migrationRequestInfo);

					amdocsNewCellularConv.migrateM2P(migrateInfo);
				} catch (Exception m2pEx ) {
					LOGGER.fatal("migrateM2P failed:Throwable occurred ("+ m2pEx.getMessage()+")",m2pEx);
					LOGGER.fatal("releae the reserved CTN");
					try {
						//Feb 20,2008 M. Laio: the following setProductPK is necessary for amdocsNewCellularConv to work properly when the failure
						//is caused by Tuxedo error - BAN changed, or BAN in use.
						amdocsNewCellularConv.setProductPK(newSubscriberInfo.getBanId(), newSubscriberInfo.getSubscriberId() );
						amdocsNewCellularConv.releaseMigrateReserveCtn();
					} catch(Throwable t1) {
						LOGGER.fatal("releaseMigrateReserveCtn failed: Throwable occurred ("+ m2pEx.getMessage()+")",m2pEx);
					}
					//throw the exception that being thrown by migrateM2P()
					throw m2pEx;
				}
				return null;
			}

		});
	}

	private CellularEquipmentInfo[] populateEquipmentInfoArray(
			com.telus.eas.equipment.info.EquipmentInfo pimaryEquipment,
			com.telus.eas.equipment.info.EquipmentInfo[] secondaryEquipments) {

		int secondaryEquipmentCount = secondaryEquipments != null ? secondaryEquipments.length : 0;
		CellularEquipmentInfo[] cellularEquipmentInfo = new CellularEquipmentInfo[secondaryEquipmentCount + 1];
		// - populate primary equipment
		cellularEquipmentInfo[0] = newCellularEquipmentInfo(
				pimaryEquipment.getEquipmentType(),
				pimaryEquipment.getSerialNumber(),
				true);

		if ( pimaryEquipment.isUSIMCard() ) { 
			//HSPA USIM Card
			cellularEquipmentInfo[0].serialNumber = com.telus.eas.equipment.info.EquipmentInfo.DUMMY_ESN_FOR_USIM;
			cellularEquipmentInfo[0].imsiList = DaoSupport.extractImsiInfo(pimaryEquipment, ImsiInfo.INSERT );
		}

		// - populate secondary equipment
		if (secondaryEquipmentCount > 0) {
			for (int i=0; i < secondaryEquipments.length; i++) {
				cellularEquipmentInfo[i+1] = newCellularEquipmentInfo(
						secondaryEquipments[i].getEquipmentType(),
						secondaryEquipments[i].getSerialNumber(),false );
			}
		}
		return cellularEquipmentInfo;
	}

	private CellularEquipmentInfo newCellularEquipmentInfo( String equipmentType, String serialNumber, boolean isPrimary ) {

		CellularEquipmentInfo equipmentInfo= new CellularEquipmentInfo();
		equipmentInfo.equipmentType = AttributeTranslator.byteFromString( equipmentType );
		equipmentInfo.serialNumber = serialNumber;
		equipmentInfo.primaryInd = isPrimary;

		equipmentInfo.possession = (byte)'P';
		equipmentInfo.base = CellularEquipmentInfo.EQUIPMENT_BASE_DECIMAL;
		equipmentInfo.activateInd = true;
		equipmentInfo.equipmentMode = EquipmentInfo.INSERT;
		return equipmentInfo;
	}

	@Override
	public void releasePortedInSubscriber(final SubscriberInfo subscriberInfo,
			String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				NewCellularConv amdocsNewCellularConv = transactionContext.createBean(NewCellularConv.class);

				// Set ProductPK (which also retrieves the BAN)
				amdocsNewCellularConv.setProductPK(subscriberInfo.getBanId(),subscriberInfo.getSubscriberId());

				// Release Subscriber
				amdocsNewCellularConv.releasePortReserveCtn();
				LOGGER.debug("Done releasePortedInSubscriber");
				return null;
			}
		});
	}

	@Override
	public SubscriberInfo reservePortedInPhoneNumber(
			final SubscriberInfo subscriberInfo,
			final PhoneNumberReservationInfo phoneNumberReservation,
			final boolean reserveNumberOnly, String sessionId) throws ApplicationException {
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<SubscriberInfo>() {

			@Override
			public SubscriberInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				NewCellularConv amdocsNewCellularConv = transactionContext.createBean(NewCellularConv.class);
				// Set ProductPK (which also retrieves the BAN)
				amdocsNewCellularConv.setProductPK(subscriberInfo.getBanId());

				// reserve phoneNumber
				String phoneNumber = phoneNumberReservation.getPhoneNumberPattern();
				PortReserveInfo portReserveInfo = new PortReserveInfo();
				portReserveInfo.reservePtnOnly = reserveNumberOnly;
				portReserveInfo.nl = phoneNumberReservation.getNumberGroup().getNumberLocation();
				if (phoneNumber != null && !phoneNumber.equals("")){
					amdocsNewCellularConv.portReserveCtn(phoneNumber,portReserveInfo );
					LOGGER.debug("Reserved Number: " + phoneNumber);
				} else {
					//no numbers found.
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS
							, "Phone number is not provided for Ported In reservation.", "");				
				}

				// set subscriberId and phoneNumber
				subscriberInfo.setSubscriberId(phoneNumber);
				subscriberInfo.setPhoneNumber(phoneNumber);
				//subscriberInfo.setStatus(Subscriber.STATUS_RESERVED);
				
				return subscriberInfo;
			}			
		});
	}

	@Override
	public void updateBirthDate(final SubscriberInfo subscriberInfo, String sessionId)
	throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
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
	public SubscriberInfo retrievePartiallyReservedSubscriber(final int banId, final String subscriberId, String sessionId) throws ApplicationException {
		
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<SubscriberInfo>() {
			
			@Override
			public SubscriberInfo doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				NewCellularConv amdocsNewCellularConv = transactionContext.createBean(NewCellularConv.class);
				amdocsNewCellularConv.setProductPK( banId, subscriberId );
				SubscriberInfo subscriberInfo = new SubscriberInfo();
			    subscriberInfo.setBanId(banId);
			    subscriberInfo.setSubscriberId(subscriberId);
			    subscriberInfo.setPhoneNumber(subscriberId);
			    subscriberInfo.setStatus(com.telus.api.account.Subscriber.STATUS_RESERVED);
			    return subscriberInfo;
			}
		});
	}
}