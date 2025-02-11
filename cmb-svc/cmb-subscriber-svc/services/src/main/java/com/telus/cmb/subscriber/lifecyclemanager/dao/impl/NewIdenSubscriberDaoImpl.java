package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.rmi.RemoteException;
import java.util.Date;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.EquipmentInfo;
import amdocs.APILink.datatypes.IdenEquipmentInfo;
import amdocs.APILink.datatypes.IdenMigrateReserveResourceInfo;
import amdocs.APILink.datatypes.IdenPortReserveResourceInfo;
import amdocs.APILink.datatypes.IdenResourceAllocationInfo;
import amdocs.APILink.datatypes.IdenResourceInfo;
import amdocs.APILink.datatypes.ManualFleetInfo;
import amdocs.APILink.datatypes.MigrateM2PInfo;
import amdocs.APILink.datatypes.MigrateReserveInfo;
import amdocs.APILink.datatypes.NgpNmbInfo;
import amdocs.APILink.datatypes.PortReserveInfo;
import amdocs.APILink.datatypes.ProductActivityInfo;
import amdocs.APILink.datatypes.ProductAdditionalInfo;
import amdocs.APILink.datatypes.ProductServicesInfo;
import amdocs.APILink.datatypes.PtnImsiInfo;
import amdocs.APILink.datatypes.TalkGroupChangeInfo;
import amdocs.APILink.datatypes.UFMIInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.IdenResourceServices;
import amdocs.APILink.sessions.interfaces.NewIdenConv;
import amdocs.APILink.sessions.interfaces.NewProductConv;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.account.ServicesValidation;
import com.telus.api.fleet.MemberIdentity;
import com.telus.api.reference.NumberGroup;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewIdenPcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewIdenSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewSubscriberDao;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.framework.exception.TelusApplicationException;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.ExceptionInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

@Deprecated 
public class NewIdenSubscriberDaoImpl extends NewSubscriberDaoImpl 
implements NewSubscriberDao, NewIdenPcsSubscriberDao, NewIdenSubscriberDao {

	private final Logger LOGGER = Logger.getLogger(NewIdenSubscriberDaoImpl.class);

	Class<NewIdenConv> newIdenConv = NewIdenConv.class;

	IdenSubscriberDaoHelperImpl idenSubscriberDaoHelper;

	public void setIdenSubscriberDaoHelper(
			IdenSubscriberDaoHelperImpl idenSubscriberDaoHelper) {
		this.idenSubscriberDaoHelper = idenSubscriberDaoHelper;
	}

	@Override
	public String[] retrieveAvailablePhoneNumbers(int ban,
			PhoneNumberReservationInfo phoneNumberReservation, int maxNumbers, String sessionId)
	throws ApplicationException {
		return idenSubscriberDaoHelper.retrieveAvailablePhoneNumbers(ban, phoneNumberReservation, maxNumbers, sessionId);
	}

	@Override
	public void createSubscriber(final SubscriberInfo subscriberInfo,
			final SubscriberContractInfo subscriberContractInfo, final boolean activate,
			final boolean dealerHasDeposit, final boolean portedIn,
			final ServicesValidation srvValidation, final String portProcessType,
			final int oldBanId, final String oldSubscriberId, final String sessionId)
	throws ApplicationException {
		super.createSubscriber(newIdenConv, subscriberInfo, subscriberContractInfo, activate, dealerHasDeposit, portedIn, srvValidation, portProcessType, oldBanId, oldSubscriberId, sessionId);
	}

	@Override
	protected void setProductEquipmentInfo(NewProductConv newProductConv,
			SubscriberInfo subscriberInfo) throws RemoteException,
			ValidateException {
		NewIdenConv newIdenConv = (NewIdenConv) newProductConv;
		IdenEquipmentInfo idenEquipmentInfo = new IdenEquipmentInfo();
		//populate IdenEquipmentInfo
		idenEquipmentInfo.serialNumber = subscriberInfo.getSerialNumber();
		idenEquipmentInfo.activateInd = true;
		idenEquipmentInfo.primaryInd = true;
		idenEquipmentInfo.equipmentMode = EquipmentInfo.INSERT;
		newIdenConv.setEquipmentInfo(idenEquipmentInfo);
		LOGGER.debug("EquipmentInfo set");
	}

	@Override
	protected void setProductActivityInfo(
			ProductActivityInfo productActivityInfo,
			SubscriberInfo subscriberInfo, boolean activate) {
		productActivityInfo.numberLocation = subscriberInfo.getNumberGroup().getNumberLocation();
	}

	@Override
	protected void setProductPk(NewProductConv newProductConv,
			SubscriberInfo subscriberInfo, boolean portedIn) throws ApplicationException, RemoteException, ValidateException {
		if (!portedIn){
			// Set ProductPK (which also retrieves the BAN)
			newProductConv.setProductPK(subscriberInfo.getBanId());
			LOGGER.debug("ProductPK set");
		} else {
			// Set ProductPK (which also retrieves the BAN)
			newProductConv.setProductPK(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());
			LOGGER.debug("ProductPK set");
		}
	}

	@Override
	protected boolean performIdenSpecificWork(UpdateBanConv amdocsUpdateBanConv, NewProductConv newProductConv, boolean portedIn, SubscriberInfo subscriberInfo, String currentSessionId) throws RemoteException, ValidateException, ApplicationException {
		NewIdenConv newIdenConv = (NewIdenConv) newProductConv;

		IDENSubscriberInfo idenSubscriberInfo = (IDENSubscriberInfo) subscriberInfo;
		UFMIInfo ufmiInfo = new UFMIInfo();
		IdenResourceInfo idenResourceInfo = new IdenResourceInfo();

		if (!portedIn){
			// reserve the subscriber (ie. allocate reserved resources) and subscriber id
			// Note: this has to happen PRIOR to setting ProductActivityInfo, otherwise, the activity reason code
			// is getting overwritten
			if (idenSubscriberInfo.getMemberIdentity0().getMemberId() != null && idenSubscriberInfo.getMemberIdentity0().getMemberId().length() > 0) {
				ufmiInfo.urbanId = idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getUrbanId();
				ufmiInfo.fleetId = idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getFleetId();
				ufmiInfo.memberId = Integer.parseInt(idenSubscriberInfo.getMemberIdentity0().getMemberId());
				idenResourceInfo = newIdenConv.reserveSubscriber(idenSubscriberInfo.getPhoneNumber(),ufmiInfo, idenSubscriberInfo.getIPAddress(), IdenResourceAllocationInfo.IP_TYPE_PRIVATE, idenSubscriberInfo.getIMSI());
			} else {
				idenResourceInfo = newIdenConv.reserveSubscriber(idenSubscriberInfo.getPhoneNumber(), idenSubscriberInfo.getIPAddress(), IdenResourceAllocationInfo.IP_TYPE_PRIVATE, idenSubscriberInfo.getIMSI());
			}
			idenSubscriberInfo.setSubscriberId(idenResourceInfo.subscriberNumber);
			LOGGER.debug("reserveSubscriber successfull - subscriber id: " + idenSubscriberInfo.getSubscriberId());
		}

		// set talk groups if available
		if (idenSubscriberInfo.getTalkGroups0() == null || idenSubscriberInfo.getTalkGroups0().length == 0) {
			LOGGER.debug("no talk groups to process");
		} else {
			LOGGER.debug("calling setTalkGroups...");
			setTalkGroups(amdocsUpdateBanConv, newIdenConv, idenSubscriberInfo, currentSessionId);
		}

		// set Number group
		// - needed for getting additional resources (fax etc.)
		newIdenConv.setNumberGroup(idenSubscriberInfo.getNumberGroup().getCode());
	      
		return portedIn;
	}

	private void setTalkGroups(UpdateBanConv amdocsUpdateBanConv, NewIdenConv newIdenConv, IDENSubscriberInfo idenSubscriberInfo, String currentSessionId) throws ApplicationException, ValidateException, RemoteException {
		TalkGroupChangeInfo[] amdocsTalkGroupChangeInfoArray = null;

		com.telus.eas.account.info.FleetInfo fleetInfo = new com.telus.eas.account.info.FleetInfo();
		int urbanId = 0;
		int fleetId = 0;

		com.telus.eas.account.info.TalkGroupInfo[] talkGroups = idenSubscriberInfo.getTalkGroups0() == null ? new com.telus.eas.account.info.TalkGroupInfo[0] : idenSubscriberInfo.getTalkGroups0();
			
		// check that talk groups array is not empty
		LOGGER.debug("Checking that talk group array is not empty...");
		if (talkGroups.length == 0) {
			throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.TALK_GROUPS_EMPTY ,"List of Talk Groups empty.","");
		}

		// check that all talk groups in the array are for the same fleet
		LOGGER.debug("Checking that all talk groups in the array are for the same fleet...");
		if (talkGroups.length > 0) {
			urbanId = talkGroups[0].getFleetIdentity().getUrbanId();
			fleetId = talkGroups[0].getFleetIdentity().getFleetId();
			LOGGER.debug("fleet is: " + urbanId + "*" + fleetId);
			fleetInfo.getIdentity0().setUrbanId(urbanId);
			fleetInfo.getIdentity0().setFleetId(fleetId);
			LOGGER.debug("fleetInfo.fleet is: " + fleetInfo.getIdentity0().getUrbanId( ) + "*" + fleetInfo.getIdentity0().getFleetId());

			for (int i=1; i < talkGroups.length; i++) {
				if (talkGroups[i].getFleetIdentity().getUrbanId() != urbanId ||
						talkGroups[i].getFleetIdentity().getFleetId() != fleetId) {
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.TALK_GROUPS_FROM_SAME_FLEET, "Talk Groups must be from same fleet.", "");
				}
			}
		}

		// associate Fleet and TGs to BAN
		ClientIdentity clientIdentity = getAmdocsTemplate().getSessionManager().getClientIdentity(currentSessionId);
		DaoSupport.associateFleetAndTGsToBan(amdocsUpdateBanConv, idenSubscriberInfo, talkGroups, clientIdentity.getPrincipal()
				, clientIdentity.getCredential(), clientIdentity.getApplication());

		// check that member id has been allocated
		LOGGER.debug("Checking that member id has been allocated...");
		if (idenSubscriberInfo.getMemberIdentity0().getMemberId().trim().equals("") || idenSubscriberInfo.getMemberIdentity0().getMemberId().trim().equals("0")) {
			throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.DISPATCH_RESOURCE_NOT_ALLOCATED, "Dispatch Resource has to be allocated.", "");
		}


		// Set ProductPK (which also retrieves the BAN)
		newIdenConv.setProductPK(idenSubscriberInfo.getBanId(), idenSubscriberInfo.getSubscriberId());

		// map Telus to Amdocs
		amdocsTalkGroupChangeInfoArray = new TalkGroupChangeInfo[talkGroups.length];
		for (int i=0; i < talkGroups.length; i++) {
			amdocsTalkGroupChangeInfoArray[i] = new TalkGroupChangeInfo();
			amdocsTalkGroupChangeInfoArray[i].actMode = amdocsTalkGroupChangeInfoArray[0].TALK_GROUP_CHANGE_INSERT;
			amdocsTalkGroupChangeInfoArray[i].urbanId = urbanId;
			amdocsTalkGroupChangeInfoArray[i].fleetId = fleetId;
			amdocsTalkGroupChangeInfoArray[i].talkGroupId = (short)talkGroups[i].getTalkGroupId();
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

		newIdenConv.changeTalkGroupList(amdocsTalkGroupChangeInfoArray);
	}

	@Override
	public void releaseSubscriber(final SubscriberInfo subscriberInfo,
			final String sessionId) throws ApplicationException {

		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {

				IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
				idenSubscriberInfo = (IDENSubscriberInfo)subscriberInfo;

				// Resources need to be released according to the state of the new subscriber
				// - if subscriber has been reserved ie. the subscriber id has been assigned,
				//   releaseSubscrber() is used which in turn releases all reserved resources
				// - if subscriber has not been reserved, we need to use the release.. () methods
				//   for each reserved resource
				if (idenSubscriberInfo.getSubscriberId() == null) {
					IdenResourceServices amdocsIdenResourceServices = transactionContext.createBean(IdenResourceServices.class);
					// release PTN
					if (idenSubscriberInfo.getPhoneNumber() != null) {
						LOGGER.debug("Calling releasePtnResource() for PTN: " + idenSubscriberInfo.getPhoneNumber() + " ...");
						amdocsIdenResourceServices.releasePtnResource(idenSubscriberInfo.getPhoneNumber(), idenSubscriberInfo.getIMSI());
					}
					// release UFMI
					if (idenSubscriberInfo.getMemberIdentity0().getMemberId() != null) {
						UFMIInfo ufmiInfo = new UFMIInfo();
						ufmiInfo.urbanId = idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getUrbanId();
						ufmiInfo.fleetId = idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getFleetId();
						ufmiInfo.memberId = Integer.parseInt(idenSubscriberInfo.getMemberIdentity0().getMemberId().trim());

						LOGGER.debug("Calling releaseUfmiResource() for UFMI: " + ufmiInfo.urbanId + "*" + ufmiInfo.fleetId + "*" + ufmiInfo.memberId + " ...");
						amdocsIdenResourceServices.releaseUfmiResource(ufmiInfo, idenSubscriberInfo.getIMSI());
					}
					// release IP
					if (idenSubscriberInfo.getIPAddress() != null) {
						LOGGER.debug("Calling releaseIpResource() for IP: " + idenSubscriberInfo.getIPAddress() + " ...");
						amdocsIdenResourceServices.releaseIpResource(idenSubscriberInfo.getIPAddress(), idenSubscriberInfo.getIMSI());
					}
				} else {
					releaseSubscriber(newIdenConv, subscriberInfo, sessionId);					

				}
				LOGGER.debug("Done releaseSubscriber");


				return null;
			}
		});

	}

	@Override
	public SubscriberInfo reserveLikePhoneNumber(final SubscriberInfo subscriberInfo,
			final PhoneNumberReservationInfo phoneNumberReservation, final String sessionId)
	throws ApplicationException {
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<SubscriberInfo>() {

			@Override
			public SubscriberInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				IdenResourceServices amdocsIdenResourceServices = transactionContext.createBean(IdenResourceServices.class);

				PtnImsiInfo ptnImsiInfo = new PtnImsiInfo();
				String ip = new String();

				IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
				idenSubscriberInfo = (IDENSubscriberInfo)subscriberInfo;

				String[] phoneNumbers = null;
				String[] numberPattern = getPhoneNumberPattern(phoneNumberReservation.getPhoneNumberPattern());


				// loop thru number patterns and find list of available phone numbers
				for (int i = 0; i < numberPattern.length; i++) {
					phoneNumbers = amdocsIdenResourceServices.getAvailablePtnList(
							phoneNumberReservation.getNumberGroup().getNumberLocation(),
							phoneNumberReservation.getNumberGroup().getCode(), "0000000000",
							numberPattern[i], phoneNumberReservation.isAsian());
					// exit if a number was found
					if (phoneNumbers != null && phoneNumbers.length > 0){
						break;
					}
				}

				// reserve phone number and IP
				if (phoneNumbers != null && phoneNumbers.length > 0){

					ptnImsiInfo = amdocsIdenResourceServices.allocatePtnManualResource(
							idenSubscriberInfo.getBanId(), phoneNumbers[0]);
					ip = amdocsIdenResourceServices.allocateIpRandomResource(
							idenSubscriberInfo.getBanId(),
							IdenResourceAllocationInfo.IP_TYPE_PRIVATE, "", ptnImsiInfo.imsi);

					LOGGER.debug("Reserved Phone#/IMSI/IP: " + ptnImsiInfo.ptn + "/" + ptnImsiInfo.imsi + "/" + ip);
				} else {
					//no numbers found.
					throw new TelusApplicationException("APP20001",genNoAvailablePhoneNumberMessage(phoneNumberReservation));
				}

				// set phoneNumber, IMSI and IP
				idenSubscriberInfo.setPhoneNumber(ptnImsiInfo.ptn);
				idenSubscriberInfo.setIMSI(ptnImsiInfo.imsi);
				idenSubscriberInfo.setIPAddress(ip);

				return idenSubscriberInfo;
			}

		});		
	}

	@Override
	public SubscriberInfo reservePhoneNumber(final SubscriberInfo subscriberInfo,
			final PhoneNumberReservationInfo phoneNumberReservation, boolean isOfflineReservation,String sessionId)
	throws ApplicationException {
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<SubscriberInfo>() {

			@Override
			public SubscriberInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {	
				IdenResourceServices amdocsIdenResourceServices = transactionContext.createBean(IdenResourceServices.class);

				PtnImsiInfo ptnImsiInfo = new PtnImsiInfo();
				String ip = new String();

				IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
				idenSubscriberInfo = (IDENSubscriberInfo)subscriberInfo;

				String wildCard = new String();

				// get list of available phone numbers
				wildCard = phoneNumberReservation.getPhoneNumberPattern();
				if (wildCard.length() < 10)
					wildCard.concat(phoneNumberWildCards.substring(0,10 - wildCard.length()));

				String[] phoneNumbers = amdocsIdenResourceServices.getAvailablePtnList(
						phoneNumberReservation.getNumberGroup().getNumberLocation(),
						phoneNumberReservation.getNumberGroup().getCode(), "0000000000",
						wildCard, phoneNumberReservation.isAsian());

				// reserve phone number and IP
				if (phoneNumbers != null && phoneNumbers.length > 0){

					ptnImsiInfo = amdocsIdenResourceServices.allocatePtnManualResource(
							idenSubscriberInfo.getBanId(), phoneNumbers[0]);
					ip = amdocsIdenResourceServices.allocateIpRandomResource(
							idenSubscriberInfo.getBanId(),
							IdenResourceAllocationInfo.IP_TYPE_PRIVATE, "", ptnImsiInfo.imsi);

					LOGGER.debug("Reserved Phone#/IMSI/IP: " + ptnImsiInfo.ptn + "/" + ptnImsiInfo.imsi + "/" + ip);
				} else {
					//no numbers found.
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS
							,genNoAvailablePhoneNumberMessage(phoneNumberReservation), "");
				}

				// set phoneNumber, IMSI and IP
				idenSubscriberInfo.setPhoneNumber(ptnImsiInfo.ptn);
				idenSubscriberInfo.setIMSI(ptnImsiInfo.imsi);
				idenSubscriberInfo.setIPAddress(ip);

				return idenSubscriberInfo;

			}
		});
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

				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				NewIdenConv amdocsNewIdenConv = transactionContext.createBean(NewIdenConv.class);

				if ( migrationRequestInfo.isPhoneOnly()==false
						&& migrationRequestInfo.isPTNBasedFleet() ){
					//associate fleet and BAN
					int urbanId = Integer.parseInt( newSubscriberInfo.getPhoneNumber().substring(0, 3));
					int fleetId = Integer.parseInt( newSubscriberInfo.getPhoneNumber().substring(3, 6));
					createFleet(amdocsUpdateBanConv, newSubscriberInfo.getBanId(), urbanId, fleetId, newSubscriberInfo.getNumberGroup());
					//need to increase expected number for this fleet in this fleet created by others.
					idenSubscriberDaoHelper.increaseExpectedNumberOfSubscribers(amdocsUpdateBanConv, newSubscriberInfo.getBanId(), urbanId, fleetId );
				}

				//populate reserve information
				IdenMigrateReserveResourceInfo idenReserveInfo = newIdenMigrateReservResourceInfo( newSubscriberInfo, migrationRequestInfo);
				MigrateReserveInfo reserveInfo = newMigrateReserveInfo(srcSubscriberInfo, migrationRequestInfo);
				ProductActivityInfo productActivityInfo = newProductActivityInfo(activityDate, migrationRequestInfo);

				StringBuffer sbuff = new StringBuffer("Migration[")
				.append( migrationRequestInfo.getMigrationType().getCode())
				.append(", resson:").append(migrationRequestInfo.getMigrationReasonCode()).append("] ")
				.append( "phone:" ).append(srcSubscriberInfo.getPhoneNumber())
				.append(" from BAN:").append( srcSubscriberInfo.getBanId() ).append(" to BAN:").append( newSubscriberInfo.getBanId())
				.append( ", dealer(").append( productActivityInfo.dealerCode).append("/").append( productActivityInfo.salesCode ).append(")")
				.append(" ; date: ").append( productActivityInfo.activityDate );
				LOGGER.info(sbuff.toString());

				amdocsNewIdenConv.setProductPK( newSubscriberInfo.getBanId() );
				amdocsNewIdenConv.setProductActivityInfo(productActivityInfo);
				amdocsNewIdenConv.migrateReservePtn(idenReserveInfo, reserveInfo);

				//Feb 20,2008 M.Liao; : save the subscriberId, so that during rollback,  releaseMigrateReserveCtn will have it
				newSubscriberInfo.setSubscriberId(amdocsNewIdenConv.getSubscriberNumber());

				try {
					amdocsNewIdenConv.setProductActivityInfo(productActivityInfo);
					//set equipment
					IdenEquipmentInfo idenEquipmentInfo = new IdenEquipmentInfo();
					idenEquipmentInfo.serialNumber = newPrimaryEquipmentInfo.getSerialNumber();
					idenEquipmentInfo.activateInd = true;
					idenEquipmentInfo.primaryInd = true;
					idenEquipmentInfo.equipmentMode = EquipmentInfo.INSERT;
					amdocsNewIdenConv.setEquipmentInfo(idenEquipmentInfo);

					//commitment info
					amdocsNewIdenConv.setProductCommitmentInfo((short)subscriberContractInfo.getCommitmentMonths());

					// set Number group
					// - needed for getting additional resources (fax etc.): happens inside mapTelusContractToAmdocsProductServices()
					String numberGroup  = newSubscriberInfo.getNumberGroup().getCode();
					try {
						amdocsNewIdenConv.setNumberGroup( numberGroup );
					} catch (ValidateException ve){
						StringBuilder sb  =new StringBuilder();
						DaoSupport.appendSubscriberInfo(sb, newSubscriberInfo);
						sb.append("encountered ValidateException(").append( ve.getErrorInd()).append(" ").append(ve.getErrorMsg()).append(")");
						sb.append("  while setting NumberGroup for P2M migration, NGP:" ).append(numberGroup);

						LOGGER.debug(sb.toString(), ve);

						// 1110560 - Invalid Network_Id/NumberGroup
						// this exception indicates that there are no available phone #s in this numbergroup and
						// since we only do setNumberGroup() for the case where a service is added that needs
						// an additional phone # we ignore this exception at this time. In the case where a phone #
						// is required we are then catching and handling the error 'NumberGroup required'
						if ( ve.getErrorInd() == 1110560) {
							LOGGER.debug("ValidateException ignored for now - processing continued");
						} else {							
							throw ve;
						}
					}


					// populate ProductServicesInfo
					ProductServicesInfo productServicesInfo = DaoSupport.mapTelusContractToAmdocsProductServices(amdocsNewIdenConv
							, newSubscriberInfo
							, subscriberContractInfo, true, true, false);
					amdocsNewIdenConv.setProductServices(productServicesInfo);

					MigrateM2PInfo migrateInfo = newMigrateM2PInfo(activityDate, migrationRequestInfo);

					amdocsNewIdenConv.migrateP2M( migrateInfo);
				} catch (Exception p2mEx ) {
					LOGGER.fatal("migrateP2M failed:Throwable occurred ("+ p2mEx.getMessage()+")",p2mEx);
					LOGGER.fatal("releae the reserved PTN");
					try {
						//Feb 20,2008 M. Laio: the following setProductPK is necessary for amdocsNewCellularConv to work properly when the failure
						//is caused by Tuxedo error - BAN changed, or BAN in use.
						amdocsNewIdenConv.setProductPK(newSubscriberInfo.getBanId(), newSubscriberInfo.getSubscriberId() );
						amdocsNewIdenConv.releaseMigrateReservePtn();
					} catch(Throwable t1) {
						LOGGER.fatal("migrateP2M failed:Throwable occurred ("+ p2mEx.getMessage()+")",p2mEx);
					}
					//throw the exception that being thrown by migrateP2M()
					throw p2mEx;
				}
				return null;
			}

		});

	}	

	private amdocs.APILink.datatypes.FleetInfo createFleet(UpdateBanConv amdocsUpdateBanConv, int banId, int urbanId, int fleetId
			, NumberGroup numberGroup ) throws RemoteException, ValidateException, TelusException {

		amdocsUpdateBanConv.setBanPK( banId );
		// set network
		LOGGER.debug("Calling setNetwork() for network: " + numberGroup.getNetworkId() + "...");
		amdocsUpdateBanConv.setNetwork( (short) numberGroup.getNetworkId() );
		NgpNmbInfo[] amdocsNgpNmbInfo = amdocsUpdateBanConv.getNumberGroupList();
		if (amdocsNgpNmbInfo == null || amdocsNgpNmbInfo.length != 1) {
			throw new TelusApplicationException(new ExceptionInfo(
					"APP10012", "Failed to get primary NGP for network: "+ numberGroup.getNetworkId() + " !"));
		}
		LOGGER.debug("Calling setNumberGroup() for numberGroup: " + amdocsNgpNmbInfo[0].numberGroup + "...");
		// set numbergroup
		amdocsUpdateBanConv.setNumberGroup(amdocsNgpNmbInfo[0].numberGroup);

		// call createFleet, which will create the fleet if necessary and
		// also associate the fleet to the BAN
		ManualFleetInfo manualFleetInfo = new ManualFleetInfo();
		manualFleetInfo.urbanId = urbanId;
		manualFleetInfo.fleetId = fleetId;
		manualFleetInfo.fleetAlias = manualFleetInfo.urbanId + "*" + manualFleetInfo.fleetId;

		return amdocsUpdateBanConv.createFleet(manualFleetInfo);
	}

	private IdenMigrateReserveResourceInfo newIdenMigrateReservResourceInfo(SubscriberInfo subscriberInfo, MigrationRequestInfo migrationRequestInfo) {
		IdenMigrateReserveResourceInfo idenReserveInfo = new IdenMigrateReserveResourceInfo();
		if (migrationRequestInfo.isPhoneOnly()==true) {
			idenReserveInfo.reserveUfmiInd=false;
		}
		else {
			idenReserveInfo.reserveUfmiInd=true;
			idenReserveInfo.fleetPassword="";
			idenReserveInfo.ufmiReserveMethod = IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_MANUAL;
			if (migrationRequestInfo.isPTNBasedFleet()) {
				UFMIInfo ufmi = new UFMIInfo();
				ufmi.urbanId = Integer.parseInt( subscriberInfo.getPhoneNumber().substring(0,3) );
				ufmi.fleetId = Integer.parseInt( subscriberInfo.getPhoneNumber().substring(3,6) );
				ufmi.memberId = Integer.parseInt( subscriberInfo.getPhoneNumber().substring(6,10) );
				idenReserveInfo.ufmi=ufmi;
			}
			else {
				MemberIdentity memberIdentity =  migrationRequestInfo.getMemeberIdentity();
				UFMIInfo ufmi = new UFMIInfo();
				ufmi.urbanId = memberIdentity.getFleetIdentity().getUrbanId();
				ufmi.fleetId = memberIdentity.getFleetIdentity().getFleetId();
				ufmi.memberId = Integer.parseInt( memberIdentity.getMemberId() );
				idenReserveInfo.ufmi=ufmi;
			}
		}
		//always reserve IP address
		idenReserveInfo.reserveIpInd=true;
		idenReserveInfo.ipReserveMethod=IdenResourceAllocationInfo.IDEN_RESOURCE_ALLOC_RANDOM;
		idenReserveInfo.ipType=IdenResourceAllocationInfo.IP_TYPE_PRIVATE;
		idenReserveInfo.corporateCode=subscriberInfo.getNumberGroup().getNumberLocation();
		/*
			idenReserveInfo.ip=;
		 */

		return idenReserveInfo;
	}

	@Override
	public void releasePortedInSubscriber(final SubscriberInfo subscriberInfo,
			String sessionId) throws ApplicationException {

		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				NewIdenConv amdocsNewIdenConv = transactionContext.createBean(NewIdenConv.class);

				IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
				idenSubscriberInfo = (IDENSubscriberInfo)subscriberInfo;

				// Set ProductPK (which also retrieves the BAN)
				if (idenSubscriberInfo.getSubscriberId()!=null && idenSubscriberInfo.getSubscriberId().trim().length()>0) {
					amdocsNewIdenConv.setProductPK(idenSubscriberInfo.getBanId(),idenSubscriberInfo.getSubscriberId());
				} else {
					amdocsNewIdenConv.setProductPK(idenSubscriberInfo.getBanId());
				}

				// Release Subscriber
				LOGGER.debug("Calling releasePortReservePtn() for PTN: " + idenSubscriberInfo.getPhoneNumber() + " ...");
				amdocsNewIdenConv.releasePortReservePtn(idenSubscriberInfo.getPhoneNumber(), false );

				LOGGER.debug("Done releasePortedInSubscriber");
				return null;
			}
		});				
	}

	@Override
	public SubscriberInfo reservePortedInPhoneNumber(
			final SubscriberInfo subscriberInfo,
			final PhoneNumberReservation phoneNumberReservation,
			final boolean reserveNumberOnly, final boolean reserveUfmi, final boolean ptnBased,
			final byte ufmiReserveMethod, final int urbanId, final int fleetId, final int memberId,
			final AvailablePhoneNumber availPhoneNumber, final String sessionId) throws ApplicationException {

		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<SubscriberInfo>() {

			@Override
			public SubscriberInfo doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {
				NewIdenConv amdocsNewIdenConv = transactionContext.createBean(NewIdenConv.class);
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);

				IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
				IdenResourceInfo idenResourceInfo = new IdenResourceInfo();
				idenSubscriberInfo = (IDENSubscriberInfo) subscriberInfo;

				// PTN-based fleet
				// - fleet might not be created yet, therefore we need to create and/or associate
				// fleet to BAN
				if (ptnBased) {
					amdocs.APILink.datatypes.FleetInfo amdocsFleetInfo = createFleet(amdocsUpdateBanConv, idenSubscriberInfo.getBanId()
							, urbanId, fleetId, availPhoneNumber.getNumberGroup());
					idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().setUrbanId(urbanId);
					idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().setFleetId(fleetId);
					idenSubscriberInfo.setFleetClass(AttributeTranslator.stringFrombyte(amdocsFleetInfo.fleetClass));
				}

				// Class-based fleet
				// - fleet might not associated to ban yet
				if (!ptnBased && reserveUfmi) {
					// associate fleet to BAN
					idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().setUrbanId(urbanId);
					idenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().setFleetId(fleetId);
				
					ClientIdentity clientIdentity = getAmdocsTemplate().getSessionManager().getClientIdentity(sessionId);
					DaoSupport.associateFleetAndTGsToBan(amdocsUpdateBanConv, idenSubscriberInfo
							, new com.telus.eas.account.info.TalkGroupInfo[0], clientIdentity.getPrincipal()
							, clientIdentity.getCredential(), clientIdentity.getApplication());
				}

				if (reserveUfmi){
					// increase 'expected # of subscribers' for subscriber with UFMI
					DaoSupport.increaseExpectedNumberOfSubscribers( amdocsUpdateBanConv, idenSubscriberInfo.getBanId(),
							urbanId, fleetId);
				}

				// Set ProductPK (which also retrieves the BAN)
				amdocsNewIdenConv.setProductPK(idenSubscriberInfo.getBanId());

				// reserve phoneNumber
				IdenPortReserveResourceInfo idenPortRsvResInfo = new IdenPortReserveResourceInfo();
				PortReserveInfo portReserveInfo = new PortReserveInfo();

				// Populate portReserveInfo
				String phoneNumber = phoneNumberReservation.getPhoneNumberPattern();
				portReserveInfo.reservePtnOnly = reserveNumberOnly;
				portReserveInfo.nl = phoneNumberReservation.getNumberGroup().getNumberLocation();

				// Populate idenPortRsvResInfo
				idenPortRsvResInfo.corporateCode = phoneNumberReservation.getNumberGroup().getNumberLocation();
				idenPortRsvResInfo.ptn = phoneNumber;
				idenPortRsvResInfo.reserveUfmiInd = reserveUfmi;
				idenPortRsvResInfo.ufmiReserveMethod = ufmiReserveMethod;
				UFMIInfo ufmiPortIn = new UFMIInfo();
				ufmiPortIn.fleetId = fleetId;
				ufmiPortIn.urbanId = urbanId;
				ufmiPortIn.memberId = memberId;
				idenPortRsvResInfo.ufmi = ufmiPortIn;
				idenPortRsvResInfo.reserveIpInd = true;
				idenPortRsvResInfo.ipReserveMethod = 'R';
				idenPortRsvResInfo.ipType = 'Y';

				// reserve phone number
				if (phoneNumber != null && !phoneNumber.equals("")){
					idenResourceInfo   = amdocsNewIdenConv.portReservePtn(idenPortRsvResInfo,portReserveInfo );

				} else {
					//no numbers found.
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS, "Phone number is not provided for Ported In reservation.", "");					
				}

				// set subscriberNumber, IMSI and IP
				idenSubscriberInfo.setPhoneNumber(idenResourceInfo.ptn);
				idenSubscriberInfo.setSubscriberId(idenResourceInfo.subscriberNumber);
				idenSubscriberInfo.setIMSI(idenResourceInfo.imsi);
				idenSubscriberInfo.setIPAddress(idenResourceInfo.ip);
				idenSubscriberInfo.getMemberIdentity0().setMemberId(String.valueOf(idenResourceInfo.memberId));
				//idenSubscriberInfo.setStatus(Subscriber.STATUS_RESERVED);
				return idenSubscriberInfo;				
			}
		});
	}	

	@Override
	protected void setSubscriberAlias(
			SubscriberInfo subscriberInfo, ProductAdditionalInfo productAdditionalInfo) {
		productAdditionalInfo.subscriberAlias = AttributeTranslator.emptyFromNull(((IDENSubscriberInfo) subscriberInfo).getSubscriberAlias());
	}
	
	@Override
	public IDENSubscriberInfo retrievePartiallyReservedSubscriber(final int banId, final String subscriberId, String sessionId) throws ApplicationException {
		
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<IDENSubscriberInfo>() {
			
			@Override
			public IDENSubscriberInfo doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				NewIdenConv amdocsNewIdenConv = transactionContext.createBean(NewIdenConv.class);
				amdocsNewIdenConv.setProductPK( banId, subscriberId );
				IDENSubscriberInfo subscriberInfo = new IDENSubscriberInfo();
			    subscriberInfo.setBanId(banId);
			    subscriberInfo.setSubscriberId(subscriberId);
			    subscriberInfo.setStatus(com.telus.api.account.Subscriber.STATUS_RESERVED);
			    
			    IdenResourceInfo idenResourceInfo = amdocsNewIdenConv.getResourceInfo();
			    subscriberInfo.setIMSI(idenResourceInfo.imsi);
			    subscriberInfo.setIPAddress(idenResourceInfo.ip);
			    subscriberInfo.setPhoneNumber(idenResourceInfo.ptn);
			    
			    return subscriberInfo;
			}
		});
	}
}
