package com.telus.cmb.subscriber.utilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.Account;
import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.AccountType;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.PricePlan;
import com.telus.cmb.subscriber.bo.AccountBo;
import com.telus.cmb.subscriber.bo.ContractBo;
import com.telus.cmb.subscriber.bo.EquipmentBo;
import com.telus.cmb.subscriber.bo.PricePlanBo;
import com.telus.cmb.subscriber.bo.SubscriberBo;
import com.telus.cmb.subscriber.decorators.migration.MigrationRequestDecorator;
import com.telus.cmb.subscriber.decorators.migration.MikeToPCSPostMigrationRequestDecorator;
import com.telus.cmb.subscriber.decorators.migration.PCSPrePaidToPCSPostPaidMigrationRequestDecorator;
import com.telus.cmb.subscriber.utilities.contract.ContractUtilities;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.MigrationChangeInfo;
import com.telus.eas.subscriber.info.PCSSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.AccountTypeInfo;
import com.telus.eas.utility.info.PricePlanInfo;

/**
 * @Author Brandon Wen, R. Fong
 */

public class MigrationChangeContext extends BaseChangeContext<MigrationChangeInfo> {

	private AccountBo newAccount;
	private EquipmentBo newEquipment;
	private EquipmentBo associatedEquipment;
	private SubscriberBo newSubscriber;
	private ContractBo newContract;
	private PricePlanBo pricePlan;
	private MigrationRequestDecorator migrationRequest;

	private static final Log logger = LogFactory.getLog(MigrationChangeContext.class);

	public MigrationChangeContext(MigrationChangeInfo changeInfo) throws ApplicationException {
		super(changeInfo);
	}

	@Override
	public void initialize() throws ApplicationException {
		
		if (getChangeInfo().getCurrentSubscriberInfo() == null) {
			SubscriberInfo subscriber = getSubscriberLifecycleHelper().retrieveSubscriber(getChangeInfo().getSubscriberId());
			getChangeInfo().setCurrentSubscriberInfo(subscriber);
		}

		if (getChangeInfo().getCurrentAccountInfo() == null) {
			AccountInfo account = getAccountInformationHelper().retrieveLwAccountByBan(getChangeInfo().getCurrentSubscriberInfo().getBanId());
			getChangeInfo().setCurrentAccountInfo(account);
		}

		EquipmentInfo equipment = getProductEquipmentHelper().getEquipmentInfobySerialNo(getChangeInfo().getCurrentSubscriberInfo().getSerialNumber());
		getChangeInfo().getCurrentSubscriberInfo().setEquipment(equipment);

		super.initialize();

		try {

			if (getNewEquipment() != null && getNewEquipment().isUSIMCard() && getAssociatedEquipment() != null) {
				if (!getAssociatedEquipment().isHSPA()) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR,
							"newAssociatedHandset is not a HSPA equipment, serial [" + getAssociatedEquipment().getSerialNumber() + "]", "");
				}
				getNewEquipment().getDelegate().setAssociatedHandset(getAssociatedEquipment().getDelegate());
			}

			migrationRequest = newMigrationRequest();
			migrationRequest.setUserMemoText(getChangeInfo().getMemoText());

			PricePlanInfo pricePlanInfo = getRefDataFacade().getPricePlan("", getChangeInfo().getPricePlanCode(),
					ContractUtilities.translateEquipmentType(getChangeInfo().getCurrentEquipmentInfo(), getProductEquipmentHelper()), currentSubscriber.getNumberGroup().getProvinceCode(),
					Character.toString(getNewAccount().getAccountType()), Character.toString(getNewAccount().getAccountSubType()), getNewAccount().getBrandId());
			pricePlan = new PricePlanBo(pricePlanInfo);

			// Create newSubscriber and newContract
			SubscriberInfo newSubscriberInfo = migrationRequest.getDelegate().isToPCS() ? new PCSSubscriberInfo() : new IDENSubscriberInfo();
			newSubscriberInfo.setBanId(getNewAccount().getBanId());
			newSubscriberInfo.setProductType(getNewEquipment().getProductType());
			newSubscriberInfo.setEquipmentType(getNewEquipment().getEquipmentType());
			newSubscriberInfo.setDealerCode(getNewAccount().getDealerCode());
			newSubscriberInfo.setSalesRepId(getNewAccount().getSalesRepCode());
			newSubscriberInfo.setLanguage(currentSubscriber.getLanguage());
			newSubscriberInfo.setNumberGroup(currentSubscriber.getNumberGroup());
			newSubscriberInfo.setPhoneNumber(currentSubscriber.getPhoneNumber());
			newSubscriberInfo.setStartServiceDate(getLogicalDate());
			newSubscriberInfo.setBirthDate(currentSubscriber.getBirthDate());
			newSubscriberInfo.setSerialNumber(getNewEquipment().getSerialNumber());

			newSubscriber = new SubscriberBo(newSubscriberInfo, this);

			if (migrationRequest.getDelegate().isP2P()) {
				if (currentSubscriber.getEquipment().getSerialNumber().equals(getNewEquipment().getSerialNumber())) {
					newContract = currentSubscriber.newContract(pricePlan, PricePlan.CONTRACT_TERM_ALL);
				} else {
					currentSubscriber.setMigration(true);
					// OOM newMigrationRequest (CR : CPMS/KB Original Dealer of record - Defect# 9850)
					AccountType accType = getAccountTypeDefaults(getNewAccount().getDelegate());
					EquipmentChangeRequest equipmentChangeRequest = currentSubscriber.newEquipmentChangeRequest(getNewEquipment().getDelegate(), accType.getDefaultDealer(),
							accType.getDefaultSalesCode(), getChangeInfo().getRequestorId(), null, Subscriber.SWAP_TYPE_REPLACEMENT, false);
					// End modification
					newContract = currentSubscriber.newContract(pricePlan, PricePlan.CONTRACT_TERM_ALL, equipmentChangeRequest);
				}

			} else {
				// For both P2M and M2P
				newContract = newSubscriber.newContract(pricePlan, PricePlan.CONTRACT_TERM_ALL);
			}

		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}

	private MigrationRequestDecorator newMigrationRequest() throws TelusException, ApplicationException {
		if (getNewEquipment() != null) {
			if (getNewEquipment().isSIMCard() && getAssociatedEquipment() != null && getAssociatedEquipment().getDelegate().isMule()) {
				getNewEquipment().setLastMule(getAssociatedEquipment().getDelegate());
			}
		}

		// OCT 2013 Release : only require to implement Prepaid to Postpaid and Mike to Postpaid  
		if (getNewAccount().isPCS() && getNewAccount().isPostpaid()) {
			if (currentSubscriber.isPCS() && currentAccount.isPrepaidConsumer()) {
				if (MigrationType.PCS_PRE_TO_PCSPOST.equals(getChangeInfo().getMigrationTypeCode())) {
					return new PCSPrePaidToPCSPostPaidMigrationRequestDecorator(this);
				} else {
					String errMessage = "Expect migration type [" + MigrationType.PCS_PRE_TO_PCSPOST + "] doesn't match input migration type [" + getChangeInfo().getMigrationTypeCode() + "]";
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, errMessage, "");
				}
			} else if (currentSubscriber.isIDEN()) {
				if (MigrationType.IDEN_TO_PCSPOST.equals(getChangeInfo().getMigrationTypeCode())) {
					return new MikeToPCSPostMigrationRequestDecorator(this);
				} else {
					String errMessage = "Expect migration type [" + MigrationType.IDEN_TO_PCSPOST + "] doesn't match input migration type [" + getChangeInfo().getMigrationTypeCode() + "]";
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, errMessage, "");
				}
			}
		}

		String errMessage = "currently Following type of Migration not supported account type/subtype[" + currentAccount.getAccountType() + "/" + currentAccount.getAccountSubType() + "] to ["
				+ getNewAccount().getAccountType() + "/" + getNewAccount().getAccountSubType() + "]";
		throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, errMessage, "");
	}

	// Updated for CDA phase 1B July 2018 - changed to load regular account (with credit check info) instead of lightweight account
	public AccountBo getNewAccount() throws ApplicationException {
		if (newAccount == null) {
			AccountInfo newAccountInfo = getAccountInformationHelper().retrieveAccountByBan(getChangeInfo().getNewBan(), Account.ACCOUNT_LOAD_ALL);
			newAccount = new AccountBo(newAccountInfo, this);
		}
		return newAccount;
	}

	public EquipmentBo getNewEquipment() throws ApplicationException {
		if (newEquipment == null) {
			if (getChangeInfo().getNewEquipmentSerialNumber() == null) {
				newEquipment = getCurrentEquipment();
			} else {
				newEquipment = new EquipmentBo(getProductEquipmentHelper().getEquipmentInfobySerialNo(getChangeInfo().getNewEquipmentSerialNumber()), this);
			}
		}
		return newEquipment;
	}

	public EquipmentBo getAssociatedEquipment() throws ApplicationException {
		if (associatedEquipment == null && getChangeInfo().getNewAssociatedHandsetSerialNumber() != null) {
			associatedEquipment = new EquipmentBo(getProductEquipmentHelper().getEquipmentInfobySerialNo(getChangeInfo().getNewAssociatedHandsetSerialNumber()), this);
		}
		return associatedEquipment;
	}

	public SubscriberBo getNewSubscriber() {
		return newSubscriber;
	}

	public ContractBo getNewContract() {
		return newContract;
	}

	public PricePlanBo getPricePlan() {
		return pricePlan;
	}
	
	public MigrationRequestDecorator getMigrationRequest() {
		return migrationRequest;
	}

	public void testMigrate() throws ApplicationException {
		try {
			currentSubscriber.testMigrate(migrationRequest);
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}

	public MigrationChangeInfo migrate(boolean validateMigrate) throws ApplicationException {
		SubscriberInfo newSubscriber = null;
		try {
			newSubscriber = currentSubscriber.migrate(migrationRequest, validateMigrate);
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, t.getMessage(), "", t);
		}

		if (!validateMigrate) {
			this.getChangeInfo().setNewAccountInfo(this.getNewAccount().getDelegate());
			this.getChangeInfo().setNewSubscriberInfo(newSubscriber);
			this.getChangeInfo().setNewSubscriberContractInfo(getSubscriberLifecycleFacade().getServiceAgreement(newSubscriber, getNewAccount().getDelegate()));
		}
		return getChangeInfo();
	}

	/* 
	 * OOM newMigrationRequest (CR: CPMS/KB Original Dealer of record - Defect# 9850) - Helper method
	 * Given an account object (newAccount object to migrate to) this method will retrieve the default dealer
	 * code and default salesrep code. If the default is not found(ie: return value is null), dealer/salesrep codes are copied from the
	 * object that was passed to the method
	 * 
	 * @param account 	Account object with accountType accountSubType and brandId properties
	 * @return AccountType	account type object with the default Dealer Code and Salesrep code populated
	 */
	private AccountType getAccountTypeDefaults(Account account) throws ApplicationException {
		
		AccountTypeInfo acctType = null;
		try {
			acctType = getRefDataFacade().getAccountType(String.valueOf(account.getAccountType()) + String.valueOf(account.getAccountSubType()), account.getBrandId());
		} catch (TelusException e) {
			logger.debug("unable to retrieve default dealer/salesrepcode values for Account type: " + account.getAccountType() + " ,subtype: " + account.getAccountSubType() + " and BrandID: "
					+ account.getBrandId());
		}
		//If we get null for the default codes, we set the values manually
		if (acctType == null) {
			acctType = new AccountTypeInfo();
			acctType.setDefaultDealer(account.getDealerCode());
			acctType.setDefaultSalesCode(account.getSalesRepCode());
		}
		
		return acctType;
	}
	
}