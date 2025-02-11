package com.telus.cmb.subscriber.utilities;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.SeatType;
import com.telus.cmb.subscriber.bo.AccountBo;
import com.telus.cmb.subscriber.bo.ContractBo;
import com.telus.cmb.subscriber.bo.PricePlanBo;
import com.telus.cmb.subscriber.decorators.migration.BCMobileSeatToPCSPostPaidMigrateSeatRequestDecorator;
import com.telus.cmb.subscriber.decorators.migration.MigrateSeatRequestDecorator;
import com.telus.cmb.subscriber.decorators.migration.PCSPostPaidToBCMobileSeatMigrateSeatRequestDecorator;
import com.telus.cmb.subscriber.utilities.contract.ContractUtilities;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.MigrateSeatChangeInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.PricePlanInfo;

/**
 * @author R. Fong
 *
 */
public class MigrateSeatChangeContext extends BaseChangeContext<MigrateSeatChangeInfo> {
	
	private AccountBo newAccount;
	private ContractBo newContract;
	private PricePlanBo pricePlan;
	private MigrateSeatRequestDecorator migrateSeatRequest;
	
	public MigrateSeatChangeContext(MigrateSeatChangeInfo changeInfo) throws ApplicationException {
		super(changeInfo);
	}
	
	@Override
	public void initialize() throws ApplicationException {
		
		// If the current subscriber is null, retrieve the subscriber using the subscriber ID 
		if (getChangeInfo().getCurrentSubscriberInfo() == null) {
			SubscriberInfo subscriber = getSubscriberLifecycleHelper().retrieveSubscriber(getChangeInfo().getSubscriberId());
			getChangeInfo().setCurrentSubscriberInfo(subscriber);
		}
		// If the current account is null, retrieve the account using the subscriber's BAN
		if (getChangeInfo().getCurrentAccountInfo() == null) { 
			AccountInfo account = getAccountInformationHelper().retrieveLwAccountByBan(getChangeInfo().getCurrentSubscriberInfo().getBanId());
			getChangeInfo().setCurrentAccountInfo(account);
		}
		// Retrieve the current equipment using the subscriber's equipment serial number
		EquipmentInfo equipment = getProductEquipmentHelper().getEquipmentInfobySerialNo(getChangeInfo().getCurrentSubscriberInfo().getSerialNumber());
		getChangeInfo().getCurrentSubscriberInfo().setEquipment(equipment);

		super.initialize();
		
		try {
			// Create a new request
			migrateSeatRequest = newMigrateSeatRequest();

			// Retrieve the price plan
			PricePlanInfo pricePlanInfo = getRefDataFacade().getPricePlan("", getChangeInfo().getPricePlanCode(),
		    		ContractUtilities.translateEquipmentType(getChangeInfo().getCurrentEquipmentInfo(), getProductEquipmentHelper()),
		    		currentSubscriber.getNumberGroup().getProvinceCode(), Character.toString(getNewAccount().getAccountType()),
		    		Character.toString(getNewAccount().getAccountSubType()), getNewAccount().getBrandId());
			pricePlan = new PricePlanBo(pricePlanInfo);
			
			// Note: we assume there are no equipment changes for seat migrations, so use the current subscriber to generate a new contract
			newContract = currentSubscriber.newContract(pricePlan, Subscriber.TERM_PRESERVE_COMMITMENT);
			
			// Check the current equipment type - if it's HSPA, we need to override equipment type validation or else KB will barf on the price plan change
			if (getCurrentEquipment().isHSPA()) {
				newContract.getDelegate().getPricePlanValidation0().setEquipmentServiceMatch(false);
			}
			
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, t.getMessage(), "", t);
		}
	}
	
	private MigrateSeatRequestDecorator newMigrateSeatRequest() throws TelusException, ApplicationException {

		// July 2014 release - only support 2 seat migrations: Mobile Seat -> Postpaid (MSPO) and Postpaid -> Mobile Seat (POMS)
		// Mobile Seat -> Postpaid (MSPO)
		// Current business rules only support PCS Postpaid Consumer Regular, Business Personal or Business Regular as target account types when migrating
		// from a Business Connect account
		if (getNewAccount().isPCS() && !getNewAccount().isPostpaidBusinessConnect() &&
				(getNewAccount().isPostpaidConsumer() || getNewAccount().isPostpaidBusinessPersonal() || getNewAccount().isPostpaidBusinessRegular())) {
			// Check if the current subscriber is a Business Connect subscriber
			if (currentAccount.isPostpaidBusinessConnect() && currentSubscriber.isPCS()) {
				// Check the seat data to determine if this is a mobile seat subscriber
				if (currentSubscriber.getSeatData() != null && currentSubscriber.getSeatData().getSeatType() != null &&
						currentSubscriber.getSeatData().getSeatType().equalsIgnoreCase(SeatType.SEAT_TYPE_MOBILE)) {
					if (MigrationType.BC_MOBILE_TO_PCS_POST.equals(getChangeInfo().getMigrationTypeCode())) {
						return new BCMobileSeatToPCSPostPaidMigrateSeatRequestDecorator(this); 
					} else {
						String errorMessage = "Expected migration type [" + MigrationType.BC_MOBILE_TO_PCS_POST + "] doesn't match the input migration type [" + getChangeInfo().getMigrationTypeCode() + "].";
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, errorMessage, "");
					}
				} else {
					String errorMessage = "Seat migration is not currently supported for seat type: [" + 
							currentSubscriber.getSeatData() != null && currentSubscriber.getSeatData().getSeatType() != null ? currentSubscriber.getSeatData().getSeatType() : null + "].";
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, errorMessage, "");
				}
			}
		// Postpaid -> Mobile Seat (POMS)
		} else if (getNewAccount().isPCS() && getNewAccount().isPostpaidBusinessConnect()) {
			// Check if the current subscriber is a PCS Postpaid Consumer Regular, Business Personal or Business Regular subscriber
			if (currentSubscriber.isPCS() && !currentAccount.isPostpaidBusinessConnect() && 
					(currentAccount.isPostpaidConsumer() || currentAccount.isPostpaidBusinessPersonal() || currentAccount.isPostpaidBusinessRegular())) {
				// Check the target seat data to determine if we're migrating to a mobile seat subscriber
				if (getChangeInfo().getTargetSeatTypeCode().equalsIgnoreCase(SeatType.SEAT_TYPE_MOBILE)) {
					if (MigrationType.PCS_POST_TO_BCMOBILE.equals(getChangeInfo().getMigrationTypeCode())) {
						return new PCSPostPaidToBCMobileSeatMigrateSeatRequestDecorator(this); 
					} else {
						String errorMessage = "Expected migration type [" + MigrationType.PCS_POST_TO_BCMOBILE + "] doesn't match the input migration type [" + getChangeInfo().getMigrationTypeCode() + "].";
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, errorMessage, "");
					}
				} else {
					String errorMessage = "Seat migration is not currently supported for seat type: [" + getChangeInfo().getTargetSeatTypeCode() + "].";
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, errorMessage, "");
				}
			}
		}

		String errorMessage = "Seat migration is not currently supported for account type/sub-type: [" + 
				currentAccount.getAccountType() + "/" + currentAccount.getAccountSubType() + "] to [" + getNewAccount().getAccountType() + "/" +
				getNewAccount().getAccountSubType() + "]";
		throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, errorMessage, "");
	}

	public AccountBo getNewAccount() throws ApplicationException {
		if (newAccount == null) {
			AccountInfo newAccountInfo = getAccountInformationHelper().retrieveLwAccountByBan(getChangeInfo().getTargetAccountNumber());
			newAccount = new AccountBo(newAccountInfo, this);
		}
		return newAccount;
	}

	public ContractBo getNewContract() {
		return newContract;
	}

	public PricePlanBo getPricePlan() {
		return pricePlan;
	}
	
	public void testMigrate() throws ApplicationException {
		try {
			currentSubscriber.testMigrate(migrateSeatRequest);
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}
	
	public MigrateSeatChangeInfo migrate(boolean validateMigrate, boolean notificationSuppressionInd, AuditInfo auditInfo) throws ApplicationException {
		
		SubscriberInfo newSubscriber = null;
		try {
			newSubscriber = currentSubscriber.migrate(migrateSeatRequest, validateMigrate, notificationSuppressionInd, auditInfo);
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
		
		if (!validateMigrate) {
			this.getChangeInfo().setNewAccountInfo(this.getNewAccount().getDelegate());
			this.getChangeInfo().setNewSubscriberInfo(newSubscriber);
			this.getChangeInfo().setNewContractInfo(getSubscriberLifecycleFacade().getServiceAgreement(newSubscriber, getNewAccount().getDelegate()));
		}
		
		return getChangeInfo();
	}
    
}