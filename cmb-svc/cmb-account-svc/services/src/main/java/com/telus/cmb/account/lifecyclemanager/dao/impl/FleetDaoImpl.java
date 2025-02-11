package com.telus.cmb.account.lifecyclemanager.dao.impl;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.ContactInfo;
import amdocs.APILink.datatypes.FleetSecuredInfo;
import amdocs.APILink.datatypes.NameInfo;
import amdocs.APILink.datatypes.NewFleetInfo;
import amdocs.APILink.datatypes.NgpNmbInfo;
import amdocs.APILink.datatypes.UpdateFleetInfo;
import amdocs.APILink.datatypes.UrbanFleetId;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.fleet.Fleet;
import com.telus.cmb.account.lifecyclemanager.dao.FleetDao;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TalkGroupInfo;

public class FleetDaoImpl extends AmdocsDaoSupport implements FleetDao{

	private static Logger LOGGER = Logger.getLogger(FleetDaoImpl.class);	

	@Override
	public FleetInfo createFleet(final int ban,final short network,final FleetInfo fleetInfo,
			final int numberOfSubscribers, String sessionId) throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<FleetInfo>() {

			@Override
			public FleetInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {				
				NewFleetInfo amdocsNewFleetInfo = new NewFleetInfo();
				UpdateFleetInfo amdocsUpdateFleetInfo = new UpdateFleetInfo();
				FleetSecuredInfo amdocsFleetSecuredInfo = new FleetSecuredInfo();
				amdocs.APILink.datatypes.FleetInfo amdocsFleetInfo = new amdocs.APILink.datatypes.FleetInfo();
				NgpNmbInfo amdocsNgpNmbInfo[] = null;
				boolean isPublicFleet = fleetInfo.getType() == Fleet.TYPE_PUBLIC;

				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);

				// Set BanPK (which also retrieves the BAN)
				amdocsUpdateBanConv.setBanPK(ban);

				// populate NewFleetInfo
				amdocsNewFleetInfo.fleetAlias = fleetInfo.getName();
				amdocsNewFleetInfo.expectedSubscriberNumber = numberOfSubscribers > 0 ? numberOfSubscribers	: 1;
				amdocsNewFleetInfo.expectedTalkGroupNumber = 0;

				// populate FleetSecuredInfo (for non-public fleets)
				if (!isPublicFleet) {
					amdocsFleetSecuredInfo.fleetType = (byte) fleetInfo.getType();
					NameInfo banNameInfo = amdocsUpdateBanConv.getBillingName();
					ContactInfo banContactInfo = amdocsUpdateBanConv.getContactInfo();
					amdocsFleetSecuredInfo.ownerName = banNameInfo.firstName.trim().equals("") 
					? banNameInfo.lastBusinessName.trim()
							: banNameInfo.firstName.trim() + " " + banNameInfo.lastBusinessName.trim();
					amdocsFleetSecuredInfo.ownerHomeNumber = banContactInfo.homeTelNo;
					amdocsFleetSecuredInfo.ownerWorkNumber = banContactInfo.workTelNo;
				}

				// set network
				amdocsUpdateBanConv.setNetwork(network);

				// get primary number group
				// (at Telus, each network should have 1 and only 1 primary
				// numbergroup)
				amdocsNgpNmbInfo = amdocsUpdateBanConv.getNumberGroupList();
				if (amdocsNgpNmbInfo == null || amdocsNgpNmbInfo.length != 1) {
					throw new ApplicationException(SystemCodes.CMB_ALM_DAO
							, ErrorCodes.FAILED_TO_GET_PRIMARY_NGP_FOR_NETWORK, "Failed to get primary NGP for network: " + network + " !", "");
				}

				// set numbergroup
				amdocsUpdateBanConv.setNumberGroup(amdocsNgpNmbInfo[0].numberGroup);

				// create new fleet
				LOGGER.debug("Excecuting createFleet() - start...");
				if (isPublicFleet)
					amdocsFleetInfo = amdocsUpdateBanConv.createFleet(amdocsNewFleetInfo);
				else
					amdocsFleetInfo = amdocsUpdateBanConv.createFleet(amdocsNewFleetInfo, amdocsFleetSecuredInfo);
				LOGGER.debug("Excecuting createFleet() - end...");

				fleetInfo.setBanId0(ban);
				fleetInfo.setNetworkId(network);
				fleetInfo.getIdentity0().setUrbanId(amdocsFleetInfo.urbanId);
				fleetInfo.getIdentity0().setFleetId(amdocsFleetInfo.fleetId);
				fleetInfo.setExpectedSubscribers(amdocsFleetInfo.expectedSubscriberNumber);
				fleetInfo.setExpectedTalkGroups(amdocsFleetInfo.expectedTalkGroupNumber);
				fleetInfo.setFleetClass(AttributeTranslator.stringFrombyte(amdocsFleetInfo.fleetClass));

				// modify fleet to set excpected number of subscribers to 1
				// - we used # of subs to drive what fleet class is getting assigned
				// but
				//   we don't really want the # to be 255, 31 or even higher
				// - need to set sschInd because it is not returned properly in
				// createFleet()
				if (amdocsFleetInfo.expectedSubscriberNumber > 1) {
					amdocsUpdateFleetInfo.urbanId = amdocsFleetInfo.urbanId;
					amdocsUpdateFleetInfo.fleetId = amdocsFleetInfo.fleetId;
					amdocsUpdateFleetInfo.expectedSubscriberNumber = 1;
					amdocsUpdateFleetInfo.scchInd = (byte) 'S';
					LOGGER.debug("Excecuting modifyFleet() - start...");
					if (isPublicFleet) {
						amdocsUpdateBanConv.modifyFleet(amdocsUpdateFleetInfo);
					} else {
						amdocsFleetSecuredInfo = new FleetSecuredInfo();
						amdocsUpdateBanConv.modifyFleet(amdocsUpdateFleetInfo,
								amdocsFleetSecuredInfo);
					}
					LOGGER.debug("Excecuting modifyFleet() - end...");
					fleetInfo.setExpectedSubscribers(amdocsUpdateFleetInfo.expectedSubscriberNumber);
				}

				return fleetInfo;

			}			

		});		
	}

	@Override
	public void addFleet(final int ban, short network,final FleetInfo fleetInfo,
			int numberOfSubscribers, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				boolean isPublicFleet = fleetInfo.getType() == Fleet.TYPE_PUBLIC;

				try {
					UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
					// Set BanPK (which also retrieves the BAN)
					amdocsUpdateBanConv.setBanPK(ban);

					// populate UrbanFleetId
					UrbanFleetId amdocsUrbanFleetId = new UrbanFleetId();
					amdocsUrbanFleetId.urbanId = fleetInfo.getIdentity().getUrbanId();
					amdocsUrbanFleetId.fleetId = fleetInfo.getIdentity().getFleetId();
					amdocsUrbanFleetId.expectedSubscriberNumber = 1;
					amdocsUrbanFleetId.expectedTalkGroupNumber = 0;

					// associate exiting fleet
					LOGGER.debug("Excecuting associateFleet() - start...");
					if (isPublicFleet)
						amdocsUpdateBanConv.associateFleet(amdocsUrbanFleetId);
					else
						amdocsUpdateBanConv.associateFleet(amdocsUrbanFleetId, "");
					LOGGER.debug("Excecuting associateFleet() - end...");

					return null;

				} catch (ValidateException ve) {					
					switch (ve.getErrorInd()) {
					// id=1111480; This Fleet already exists in BAN, cannot associate
					case 1111480:
						throw new ApplicationException(SystemCodes.AMDOCS
								, ErrorCodes.AMDOCS_FLEET_ALREADY_ASSOCIATE_TO_THIS_BAN
								, "Fleet is already associated to this BAN.", "", ve);
					default:
						throw ve;
					}
				}
			}
		});

	}

	@Override
	public void dissociateFleet(final int ban, final FleetInfo fleetInfo, String sessionId)
	throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				// Set BanPK (which also retrieves the BAN)
				amdocsUpdateBanConv.setBanPK(ban);

				// Populate UrbanFleetId
				UrbanFleetId amdocsUrbanFleetId = new UrbanFleetId();
				amdocsUrbanFleetId.fleetId = fleetInfo.getIdentity().getFleetId();
				amdocsUrbanFleetId.urbanId = fleetInfo.getIdentity().getUrbanId();
				amdocsUrbanFleetId.expectedSubscriberNumber = fleetInfo.getExpectedSubscribers();
				amdocsUrbanFleetId.expectedTalkGroupNumber = (short)fleetInfo.getExpectedTalkGroups();

				LOGGER.debug("Calling dissociateFleet...");
				amdocsUpdateBanConv.dissociateFleet(amdocsUrbanFleetId);

				return null;
			}		
		});			
	}	

	@Override
	public void removeTalkGroup(final int ban, final TalkGroupInfo talkGroupInfo, String sessionId)
	throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {

				amdocs.APILink.datatypes.TalkGroupInfo amdocsTalkGroupInfo = new amdocs.APILink.datatypes.TalkGroupInfo();
				int urbanId = talkGroupInfo.getFleetIdentity().getUrbanId();
				int fleetId = talkGroupInfo.getFleetIdentity().getFleetId();

				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				// Set BanPK (which also retrieves the BAN)
				amdocsUpdateBanConv.setBanPK(ban);

				// populate UrbanFleetId
				amdocsTalkGroupInfo.urbanId = urbanId;
				amdocsTalkGroupInfo.fleetId = fleetId;
				amdocsTalkGroupInfo.talkGroupId = (short) talkGroupInfo.getTalkGroupId();
				LOGGER.debug("disassociating TG: " + urbanId + "*" + fleetId + "*" + amdocsTalkGroupInfo.talkGroupId);

				// associate exiting talkgroup
				LOGGER.debug("Excecuting cancelBanTalkGroup() - start...");
				amdocsUpdateBanConv.cancelBanTalkGroup(amdocsTalkGroupInfo);
				LOGGER.debug("Excecuting cancelBanTalkGroup() - end...");

				return null;
			}
		});

	}

	@Override
	public void addTalkGroup(final int ban,final TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
					throws Exception {
				amdocs.APILink.datatypes.TalkGroupInfo amdocsTalkGroupInfo = new amdocs.APILink.datatypes.TalkGroupInfo();
				int urbanId = talkGroupInfo.getFleetIdentity().getUrbanId();
				int fleetId = talkGroupInfo.getFleetIdentity().getFleetId();

				try {
					UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
					// Set BanPK (which also retrieves the BAN)
					amdocsUpdateBanConv.setBanPK(ban);

					// populate UrbanFleetId
					amdocsTalkGroupInfo.urbanId = urbanId;
					amdocsTalkGroupInfo.fleetId = fleetId;
					amdocsTalkGroupInfo.talkGroupId = (short) talkGroupInfo
							.getTalkGroupId();
					LOGGER.debug("associating TG: "
							+ urbanId + "*" + fleetId + "*"
							+ amdocsTalkGroupInfo.talkGroupId);

					// associate exiting talkgroup
					LOGGER.debug("Excecuting associateTalkGroup() - start...");
					amdocsUpdateBanConv.associateTalkGroup(amdocsTalkGroupInfo);
					LOGGER.debug("Excecuting associateTalkGroup() - end...");
					
					return null;
				} catch (ValidateException ve) {					
					throw handleValidateException(ve);
				}				
			}
		});		
	}

	@Override
	public TalkGroupInfo createTalkGroup(final int ban, final TalkGroupInfo talkGroupInfo,
			String sessionId) throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<TalkGroupInfo>() {

			@Override
			public TalkGroupInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
					throws Exception {

				amdocs.APILink.datatypes.TalkGroupInfo amdocsTalkGroupInfo = new amdocs.APILink.datatypes.TalkGroupInfo();

				try {

					UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
					// Set BanPK (which also retrieves the BAN)
					amdocsUpdateBanConv.setBanPK(ban);

					// populate TalkGroupInfo
					amdocsTalkGroupInfo.urbanId = talkGroupInfo.getFleetIdentity()
							.getUrbanId();
					amdocsTalkGroupInfo.fleetId = talkGroupInfo.getFleetIdentity()
							.getFleetId();
					amdocsTalkGroupInfo.talkGroupAlias = talkGroupInfo.getName();

					// create new talkgroup
					LOGGER.debug("Excecuting createTalkGroup() - start...");
					amdocsTalkGroupInfo = amdocsUpdateBanConv
							.createTalkGroup(amdocsTalkGroupInfo);
					LOGGER.debug("Excecuting createTalkGroup() - end...");

					talkGroupInfo.setTalkGroupId(amdocsTalkGroupInfo.talkGroupId);

					return talkGroupInfo;

				}  catch (ValidateException ve) {					
					throw handleValidateException(ve);
				}
			}			
			
		});
	}

	@Override
	public void updateTalkGroup(final int ban, final TalkGroupInfo talkGroupInfo, String sessionId)
			throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
					throws Exception {
				amdocs.APILink.datatypes.TalkGroupInfo amdocsTalkGroupInfo = new amdocs.APILink.datatypes.TalkGroupInfo();

				try {
					UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
					// Set BanPK (which also retrieves the BAN)
					LOGGER.debug("Calling setBanPK with ban=[" + ban + "] ...");
					amdocsUpdateBanConv.setBanPK(ban);

					// populate TalkGroupInfo
					amdocsTalkGroupInfo.urbanId = talkGroupInfo.getFleetIdentity().getUrbanId();
					amdocsTalkGroupInfo.fleetId = talkGroupInfo.getFleetIdentity().getFleetId();
					amdocsTalkGroupInfo.talkGroupId = (short)talkGroupInfo.getTalkGroupId();
					// Note: if only the priority is being updated then the name/alias must be empty (Amdocs quirk)
					amdocsTalkGroupInfo.talkGroupAlias = talkGroupInfo.getName().equals(talkGroupInfo.getOriginalName()) ? "" : talkGroupInfo.getName();
					amdocsTalkGroupInfo.talkGroupPriority = (short)talkGroupInfo.getPriority();

					// update talkgroup
					LOGGER.debug("Excecuting modifyTalkGroup() - start...");
					amdocsUpdateBanConv.modifyTalkGroup(amdocsTalkGroupInfo);
					LOGGER.debug("Excecuting modifyTalkGroup() - end...");

					return null;
				} catch (ValidateException ve) {
					throw handleValidateException(ve);
				}
			}
		});
		
		
	}
	
	private Exception handleValidateException(ValidateException ve) {
		ApplicationException ae = new ApplicationException(SystemCodes.AMDOCS, String.valueOf(ve.getErrorInd()), ve.getErrorMsg(), "", ve);
		switch (ve.getErrorInd()) {					
		// id=701383; The requested TG alias %s already exists for this UF
		case 701383:
			return new ApplicationException(SystemCodes.CMB_ALM_DAO, ErrorCodes.DUPLICATE_TALK_GROUP_NAME
					, "Duplicate TalkGroup Name.", "", ae);
		// id=1111360; Expected no. of Talk Groups should lie between 0 and 255.
		case 1111360:
			return new ApplicationException(SystemCodes.CMB_ALM_DAO, ErrorCodes.MAX_NUMBER_OF_TALK_GROUPS_EXCEEDED
					,"Maximum # of talk groups exceeded.", "", ae);
		default:
			return ve;
		}
		
	}
}
