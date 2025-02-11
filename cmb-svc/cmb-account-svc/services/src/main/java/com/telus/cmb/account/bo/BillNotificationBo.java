package com.telus.cmb.account.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.account.Account;
import com.telus.api.reference.Brand;
import com.telus.cmb.account.lifecyclemanager.dao.AdjustmentDao;
import com.telus.cmb.account.lifecyclemanager.dao.MemoDao;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.common.dao.billnotification.BillNotificationManagementDao;
import com.telus.cmb.common.dao.portalprofile.Persona;
import com.telus.cmb.common.dao.portalprofile.PortalProfile;
import com.telus.cmb.common.dao.portalprofile.PortalProfileFilterCriteria;
import com.telus.cmb.common.dao.portalprofile.PortalProfileMgmtDao;
import com.telus.cmb.common.dao.portalprofile.PortalProfileResponse;
import com.telus.cmb.common.enums.BillNotificationActivityType;
import com.telus.eas.account.info.BillMediumInfo;
import com.telus.eas.account.info.BillNotificationAddressInfo;
import com.telus.eas.account.info.FeeWaiverInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public class BillNotificationBo {
	
	private static final Logger LOGGER = Logger.getLogger(BillNotificationBo.class);

	@Autowired
	private BillNotificationManagementDao billNotificationManagementDao;
	
	@Autowired
	private PortalProfileMgmtDao portalProfileMgmtDao;
	
	@Autowired
	private AdjustmentDao adjustmentDao;

	@Autowired
	private MemoDao memoDao;
	
	private static final String FEE_WAIVER_REASON_CODE_OTHERS = "WTLCO";
	private static final String FEE_WAIVER_TYPE_CODE_TELUS = "PBF";
	private static final String FEE_WAIVER_TYPE_CODE_KOODO = "PBFK";
	private static final String MEMO_TYPE_MISCELLANEOUS = "EMIS";

	public static final String USER_PERSONA_STATUS_ACTIVE = "Active";
	public static final String USER_PERSONA_STATUS_PENDING = "Pending";
	
	public static final String USER_PERSONA_ROLE_OWNER = "Owner";
	public static final String USER_PERSONA_ROLE_MANAGER = "Manager";
	public static final String USER_PERSONA_ROLE_MEMBER = "Member";
	
	public void setAdjustmentDao(AdjustmentDao adjustmentDao) {
		this.adjustmentDao = adjustmentDao;
	}
	
	public void setBillNotificationManagementServiceDao(BillNotificationManagementDao billNotificationManagementDao) {
		this.billNotificationManagementDao = billNotificationManagementDao;
	}

	public void setMemoDao(MemoDao memoDao) {
		this.memoDao = memoDao;
	}

	public void setPortalProfileMgmtDao(PortalProfileMgmtDao portalProfileMgmtDao) {
		this.portalProfileMgmtDao = portalProfileMgmtDao;
	}
	
	public TestPointResultInfo testBillNotificationManagementService() {
		return billNotificationManagementDao.test();
	}

	public TestPointResultInfo testPortalProfileMgmtService() {
		return portalProfileMgmtDao.test();
	}
	
	/**
	 * Method to process bill medium changes based on the ban, account type, brand, and activity type
	 * Current in scope activity type is Account Cancellation only (ACTIVITY_TYPE_ACCOUNT_CANCEL)
	 * 
	 * @param banId
	 * @param accountType
	 * @param brandId
	 * @param activityType
	 * @param sessionId
	 * @throws ApplicationException
	 */
	public void processBillMediumChanges(int banId, char accountType, int brandId, String subscriberId, BillNotificationActivityType activityType, String sessionId) {
		BillMediumInfo billMediumInfo;
		List<BillNotificationAddressInfo> billNotificationAddressList;
		List<PortalProfile> portalProfileList;
		
		// Do some very basic validation on banId
		if (banId < 0) {
			LOGGER.error("ProcessBillMediumChanges is skipped since the ban is invalid. ban = [ " + banId + " ]");
			return;
		}
		
		try {			
			if (isAccountTypeEligible(activityType, accountType)) {				
				billMediumInfo = billNotificationManagementDao.getBillMediumInfo(banId);
				billNotificationAddressList = billNotificationManagementDao.getBillNotificationAddressInfo(banId);
				portalProfileList = getPortalProfiles(banId);
				
				if (BillNotificationActivityType.ACCOUNT_CANCEL == activityType) {
					updateBillMediumAndNotificationAddressDueToAccountCancel(banId, brandId, billMediumInfo, billNotificationAddressList, portalProfileList, sessionId);	
				} else if (BillNotificationActivityType.SUBSCRIBER_CANCEL == activityType) {
					updateNotificationAddressesDueToSubscriberCancel(banId, subscriberId, billMediumInfo, billNotificationAddressList, portalProfileList, sessionId);	
				} else if (BillNotificationActivityType.ACCOUNT_SUSPEND == activityType || BillNotificationActivityType.SUBSCRIBER_SUSPEND == activityType) {
					updateNotificationAddressesDueToSuspension(banId, billMediumInfo, billNotificationAddressList, portalProfileList, sessionId);
				}
			} else {
				LOGGER.debug("Account type is not eligible to process bill medium changes , ban = [ " + banId + " ], activityType = [" + activityType + "]");
			}
		} catch (Throwable t) {
			// log the error for processBillMediumChanges and don't block any other account cancel post tasks
			LOGGER.error("Account cancel bill processBillMediumChanges error : ban = [ " + banId + " ]" + t.getMessage(), t);
		}
	}

	/**
	 * Method to update the bill medium as well as the notification addresses due to account cancellation
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	private void updateBillMediumAndNotificationAddressDueToAccountCancel(int banId, int brandId, BillMediumInfo billMediumInfo, List<BillNotificationAddressInfo> billNotificationAddressList, List<PortalProfile> portalProfileList, String sessionId) throws ApplicationException {
		
		// We only care if the bill medium is E-BILL and if there is an SMS address
		if (isEbillBillMedium(billMediumInfo) && isPhoneNumberBillNotificationAddress(billNotificationAddressList)) {

			// 1. If there is no email address nor valid profile email, we will need to de-activate the E-BILL medium and activate FULL_PAPER (if paper isn't activated)
			String portalProfileEmail = null;
			if (!isEmailBillNotificationAddress(billNotificationAddressList)) {
				portalProfileEmail = getPortalProfileEmail(banId, portalProfileList);
				if (StringUtils.isBlank(portalProfileEmail)) {
					updateBillMediumToPaperOnly(banId, brandId, billMediumInfo, sessionId);
				} 
			} else {
				LOGGER.debug("There is no change in bill medium for banId = [ " + banId + " ] ,  Bill MediumType(s) =  " + billMediumInfo.getBillTypeList());				
			}
			
			// 2. Remove all the SMS notification addresses and add a portal profile email address (if applicable)
			processBillNotificationAddresses(banId, getBillNotificationPhoneNumbers(billNotificationAddressList), portalProfileEmail, sessionId);
		} else {
			// Do nothing for PAPER, E-POST and E-BILL without SMS (i.e. email only)
			LOGGER.info("Bill medium change not required for Ban = [ " + banId + " ] ,  Bill MediumType(s) =  " + billMediumInfo.getBillTypeList());
		}
	}
	
	/**
	 * Method to update the bill notification addresses due to subscriber cancellation.
	 * @param sessionId
	 * @throws ApplicationException
	 */
	private void updateNotificationAddressesDueToSubscriberCancel(int banId, String subscriberId, BillMediumInfo billMediumInfo, List<BillNotificationAddressInfo> billNotificationAddressList, List<PortalProfile> portalProfileList, String sessionId) throws ApplicationException {

		// We only care if the bill medium is E-BILL and the notification addresses is SMS ONLY
		if (isEbillBillMedium(billMediumInfo) && isPhoneNumberBillNotificationAddress(billNotificationAddressList) && !isEmailBillNotificationAddress(billNotificationAddressList)) {
			String portalProfileEmail = getPortalProfileEmail(banId, portalProfileList);
			// We will only expire the SMS phone number (and add the portal profile email) if a valid portal profile email exists
			if (StringUtils.isNotBlank(portalProfileEmail)) {
				processBillNotificationAddresses(banId, Arrays.asList(new String[]{subscriberId}), portalProfileEmail, sessionId);				
			}
		}
	}
	
	/**
	 * Update the notification addresses due to account or subscriber suspension
	 * @param sessionId the session ID for KB API
	 * @throws ApplicationException
	 */
	private void updateNotificationAddressesDueToSuspension(int banId, BillMediumInfo billMediumInfo, List<BillNotificationAddressInfo> billNotificationAddressList, List<PortalProfile> portalProfileList, String sessionId) throws ApplicationException {

		// We only care if the bill medium is E-BILL and the notification addresses is SMS ONLY
		if (isEbillBillMedium(billMediumInfo) && isPhoneNumberBillNotificationAddress(billNotificationAddressList) && !isEmailBillNotificationAddress(billNotificationAddressList)) {
			String portalProfileEmail = getPortalProfileEmail(banId, portalProfileList);
			// We will only add the portal profile email if it exists (it has to be an active one with either an AO/AM role)
			if (StringUtils.isNotBlank(portalProfileEmail)) {
				processBillNotificationAddresses(banId, null, portalProfileEmail, sessionId);				
			}
		}
	}

	private void updateBillMediumToPaperOnly(int banId, int brandId, BillMediumInfo billMediumInfo, String sessionId) throws ApplicationException {

		boolean paperMediumActivated = false;
		BillMediumInfo processBillMediumInfo = new BillMediumInfo();		
		processBillMediumInfo.getRemovedBillTypeList().add(BillMediumInfo.BILL_MEDIUM_EBILL);
		
		// If there is also no PAPER medium and we are de-activating the E-BILL medium, we will need to activate FULL_PAPER
		if (!isPaperBillMedium(billMediumInfo)) {
			processBillMediumInfo.getAddedBillTypeList().add(BillMediumInfo.BILL_MEDIUM_FULL_PAPER);
			paperMediumActivated = true;
		}			

		// Update bill medium
		LOGGER.debug("Bill medium update for Ban = [ " + banId + " ] ,  Added bill medium types = " + processBillMediumInfo.getAddedBillTypeList() + "Removed bill medium types = "
				+ processBillMediumInfo.getRemovedBillTypeList());
		billNotificationManagementDao.processBillMedium(banId, processBillMediumInfo);
		
		// Create fee waiver and memo only if the paper bill medium is activated
		if (paperMediumActivated) {
			LOGGER.debug("The paper bill medium is activated for ban = [ " + banId + " ], waiving paper fee and creating memo.");
			waivePaperBillCharge(banId, brandId, sessionId);
			createMemo(banId, "FULL PAPER bill medium activated on the account and paper bill fee waived.", sessionId);
		}
	}

	/**
	 * Method to handle expiring SMS phone number notification address and adding the portal profile email address
	 * 
	 * @param expirePhoneNumberList
	 * @param newEmailAddress
	 * @param sessionId
	 * @throws ApplicationException
	 */
	private void processBillNotificationAddresses(int banId, List<String> expirePhoneNumberList, String newEmailAddress, String sessionId) throws ApplicationException {

		LOGGER.debug("Bill notification address update for BAN = [ " + banId + " ] , expireBillNotificationPhoneNumberList = " + expirePhoneNumberList + " registerBillNotificationEmailAddressList = "
				+ newEmailAddress);
		List<String> registerEmailAddressList = StringUtils.isNotBlank(newEmailAddress) ? Arrays.asList(new String[] { newEmailAddress }) : null;
		billNotificationManagementDao.processBillNotificationAddress(banId, expirePhoneNumberList, null, null, registerEmailAddressList);

		// If we're adding a new email address, we have to create a memo to track the change
		if (StringUtils.isNotBlank(newEmailAddress)) {
			createMemo(banId, "Updated E-Bill notification - adding email address: " + newEmailAddress, sessionId);
		}
	}

	private boolean isAccountTypeEligible(BillNotificationActivityType activityType, char accountType) {
		// [Bill Wang] Feb 2019 Release - For all bill notification updates, the current scope is consumer Only.
		return Account.ACCOUNT_TYPE_CONSUMER == accountType;
	}

	private boolean isPaperBillMedium(BillMediumInfo billMediumInfo) {
		return billMediumInfo.getBillTypeList().contains(BillMediumInfo.BILL_MEDIUM_FULL_PAPER) || billMediumInfo.getBillTypeList().contains(BillMediumInfo.BILL_MEDIUM_PAPER);
	}

	private boolean isEbillBillMedium(BillMediumInfo billMediumInfo) {
		return billMediumInfo.getBillTypeList().contains(BillMediumInfo.BILL_MEDIUM_EBILL);
	}

	private List<String> getBillNotificationPhoneNumbers(List<BillNotificationAddressInfo> billNotificationAddressList) {
		List<String> billNotificationPhoneNumberList = new ArrayList<String>();
		for (BillNotificationAddressInfo billNotificationAddressInfo : billNotificationAddressList) {
			if (StringUtils.isNotEmpty(billNotificationAddressInfo.getPhoneNumber())) {
				billNotificationPhoneNumberList.add(billNotificationAddressInfo.getPhoneNumber());
			}
		}
		return billNotificationPhoneNumberList;
	}

	private boolean isEmailBillNotificationAddress(List<BillNotificationAddressInfo> billNotificationAddressList) {
		for (BillNotificationAddressInfo billNotificationAddressInfo : billNotificationAddressList) {
			if (StringUtils.isNotEmpty(billNotificationAddressInfo.getEmailAddress())) {
				return true;
			}
		}
		return false;
	}

	private boolean isPhoneNumberBillNotificationAddress(List<BillNotificationAddressInfo> billNotificationAddressList) {
		for (BillNotificationAddressInfo billNotificationAddressInfo : billNotificationAddressList) {
			if (StringUtils.isNotEmpty(billNotificationAddressInfo.getPhoneNumber())) {
				return true;
			}
		}
		return false;
	}
		
	/**
	 * Method to retrieve the portal profile email.
	 * 
	 * This method will return any email that's in active state and is linked to either the owner or manager role.  
	 * If there's an active owner email on the portal profile, it will always be returned first.
	 * 
	 * @return the portal profile email if any
	 */
	private String getPortalProfileEmail(int banId, List<PortalProfile> portalProfileList) {
		
		String portalProfileEmail = null;
		
		for (PortalProfile portalProfile : portalProfileList) {
			// Let's check that the uuid status is also active (it's possible that it's a different status than the persona status)
			if (StringUtils.equalsIgnoreCase(portalProfile.getUuidStatus(), USER_PERSONA_STATUS_ACTIVE)) {
				for (Persona persona : portalProfile.getPersonaList()) {
					if (isValidPortalProfilePersona(banId, persona)) {
						// If the role is an owner, let's just return it immediately.  If the role is manager, we will have to keep track of it and return it later.
						if (StringUtils.equalsIgnoreCase(persona.getRole(), USER_PERSONA_ROLE_OWNER) && !StringUtils.isBlank(portalProfile.getUuidEmail())) {
							return portalProfile.getUuidEmail();
						} else if (StringUtils.equalsIgnoreCase(persona.getRole(), USER_PERSONA_ROLE_MANAGER)) {
							portalProfileEmail = portalProfile.getUuidEmail();
						}
					}
				}
			}
		}

		return portalProfileEmail;
	}
	
	/**
	 * Method to determine if a portal profile personal is valid.
	 * @param persona
	 * @return
	 */
	private boolean isValidPortalProfilePersona(int banId, Persona persona) {				
		return StringUtils.equals(persona.getBan(), String.valueOf(banId));
	}
		
	private List<PortalProfile> getPortalProfiles(int ban) throws ApplicationException {

		List<PortalProfile> portalProfileList = new ArrayList<PortalProfile>();
		
		PortalProfileFilterCriteria criteria = new PortalProfileFilterCriteria();
		
		// TODO: Remove this rollback flag after April 2019 Release
		if (AppConfiguration.isEaaEbillRollback()) {
			criteria.setStatus(USER_PERSONA_STATUS_ACTIVE);
		}
		
		PortalProfileResponse response = portalProfileMgmtDao.getPortalProfiles(ban, criteria);
		
		if (response != null && CollectionUtils.isNotEmpty(response.getPortalProfileList())) {			
			// This response sometimes gives us extra info, let's filter out all the profiles with non-matching personas
			for (PortalProfile portalProfile : response.getPortalProfileList()) {
				if (portalProfile.hasMatchingPersonaToBan(String.valueOf(ban))) {
					portalProfileList.add(portalProfile);
				}
			}
		}
		
		return portalProfileList;
	}
	
	/**
	 * Waive the Paper bill charge of the account.
	 */
	private void waivePaperBillCharge(int banId, int brandId, String sessionId) throws ApplicationException {

		FeeWaiverInfo feeWaiver = new FeeWaiverInfo();
		feeWaiver.setBanId(banId);
		feeWaiver.setEffectiveDate(new Date());
		Calendar expiryDate = Calendar.getInstance();
		expiryDate.set(2099, 11, 0);
		feeWaiver.setExpiryDate(expiryDate.getTime());
		feeWaiver.setMode(FeeWaiverInfo.INSERT);
		feeWaiver.setReasonCode(FEE_WAIVER_REASON_CODE_OTHERS);
		feeWaiver.setTypeCode(FEE_WAIVER_TYPE_CODE_TELUS);

		if (brandId == Brand.BRAND_ID_KOODO) {
			feeWaiver.setTypeCode(FEE_WAIVER_TYPE_CODE_KOODO);
		}

		adjustmentDao.applyFeeWaiver(feeWaiver, sessionId);
	}
	
	/**
	 * Create a memo for the Account to signify bill notification setting changed.
	 */
	private void createMemo(int banId, String memoText, String sessionId) throws ApplicationException {
		
		MemoInfo memoInfo = new MemoInfo();
		memoInfo.setBanId(banId);
		memoInfo.setMemoType(MEMO_TYPE_MISCELLANEOUS);
		memoInfo.setDate(new Date());
		memoInfo.setText(memoText);

		memoDao.createMemoForBan(memoInfo, sessionId);
	}
}
