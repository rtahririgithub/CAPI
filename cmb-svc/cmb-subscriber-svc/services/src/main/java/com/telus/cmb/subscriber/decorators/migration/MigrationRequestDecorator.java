package com.telus.cmb.subscriber.decorators.migration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.ActivationTopUp;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.Contract;
import com.telus.api.account.CreditCard;
import com.telus.api.account.MigrationRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.fleet.MemberIdentity;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.reference.Brand;
import com.telus.api.reference.Dealer;
import com.telus.api.reference.MigrationType;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.bo.EquipmentBo;
import com.telus.cmb.subscriber.bo.SubscriberBo;
import com.telus.cmb.subscriber.utilities.MigrationChangeContext;
import com.telus.cmb.subscriber.utilities.migration.MigrationUtilities;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.subscriber.info.MigrationChangeInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.ServiceInfo;

/**
 * @Author Brandon Wen
 * 
 * @see TMMigrationRequest.java
 */
public abstract class MigrationRequestDecorator implements MigrationRequest {
	
	private final static String BILLING_SYSTEM_FAILED = "BSTF";

	private static final Log logger = LogFactory.getLog(MigrationRequestDecorator.class);
	protected String migrationTypeCode;
	protected MigrationChangeContext migrationChangeContext;
	protected MigrationRequestInfo migrationRequestInfo;
	protected MigrationChangeInfo migrationChangeInfo;
	
    protected transient String portRequestId;
    
	private String migrationContext = null;

    public MigrationRequestDecorator(String migrationTypeCode, MigrationChangeContext migrationChangeContext) throws TelusException, ApplicationException {
    	this.migrationTypeCode = migrationTypeCode;
		this.migrationChangeContext = migrationChangeContext;
		this.migrationChangeInfo = migrationChangeContext.getChangeInfo();
    	this.migrationRequestInfo = new MigrationRequestInfo(migrationChangeContext.getRefDataFacade().getMigrationType(migrationTypeCode));
    	this.setMigrationReasonCode(migrationTypeCode);
    	this.setRequestorId(migrationChangeInfo.getRequestorId());
    	this.setDealerCode(migrationChangeInfo.getDealerCode());
    	this.setSalesRepCode(migrationChangeInfo.getSalesRepCode());
    }
    
	protected void populateNewSubNumberGroup() throws TelusAPIException, ApplicationException {
		AvailablePhoneNumber apn = getAvailablePhoneNumber(migrationChangeContext.getRefDataHelper(), migrationChangeInfo.getCurrentSubscriberInfo().getPhoneNumber(), 
				migrationChangeContext.getNewEquipment().getProductType(), getDealerCode());
		if (apn != null && apn.getNumberGroup() != null && apn.getNumberGroup().getCode() != null) {
			migrationChangeContext.getNewSubscriber().getDelegate().setNumberGroup(apn.getNumberGroup());
		} else {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Unable to retrieve NumberGroup information for phone:"
					+ migrationChangeInfo.getCurrentSubscriberInfo().getPhoneNumber() + "; could be data related.", "");
		}
	}

	public void validateMandatoryFields() throws ApplicationException {
		
		Equipment equipment = this.getNewEquipment0();
		if (getMigrationReasonCode() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Missing mandatory filed [MigrationReasonCode].", "");
		}
		if (getRequestorId() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Missing mandatory filed [RequestorId].", "");
		}
		if (equipment.isStolen()) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Equipment is stolen.", "");
		}
		if (!equipment.getSerialNumber().equals(getCurrentEquipment().getSerialNumber()) && equipment.isInUse()) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Equipment is already in use.", "");
		}
		if (MigrationUtilities.getProductType(getNewAccount()).equals(Subscriber.PRODUCT_TYPE_PCS)	&& !equipment.isCellular()) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Invalid equipment type.", "");
		}
	}
	
	protected EquipmentInfo getCurrentEquipment() throws ApplicationException {
		return migrationChangeContext.getCurrentEquipment().getDelegate();
	}
	
	public EquipmentInfo getNewEquipment() {
        // Don't use for this release - Oct 2013;
		return null; 
	}

	public EquipmentInfo getNewEquipment0() throws ApplicationException {
		return migrationChangeContext.getNewEquipment().getDelegate();
	}

	public Account getNewAccount() {
		try {
			return migrationChangeContext.getNewAccount().getDelegate();
		} catch (ApplicationException e) {
			return null;
		}
	}
	
	public Contract getNewContract() throws TelusAPIException {
		return migrationChangeContext.getNewContract().getDelegate();
	}
	
	public abstract boolean testMigrationRequest() throws ApplicationException;
	
    public abstract void postMigrationTask() throws TelusAPIException, ApplicationException;

    public abstract void preMigrationTask() throws TelusAPIException, ApplicationException;
	
	public MigrationRequestInfo getDelegate() {
        return migrationRequestInfo;
    }
    
    public Subscriber getCurrentSubscriber() {
        return migrationChangeInfo.getCurrentSubscriberInfo();
    }
	
    public String getActivationAirtimeCardNumber() {
        return migrationRequestInfo.getActivationAirtimeCardNumber();
    }

    public CreditCard getActivationCreditCard() {
        // Don't use for this release - Oct 2013;
    	return null;
    }

    public ActivationTopUp getActivationTopUp() {
        return migrationRequestInfo.getActivationTopUp();
    }
    
    public double getActivationCreditAmount() {
    	return migrationRequestInfo.getActivationCreditAmount();
    }

    public int getActivationType() {
    	return migrationRequestInfo.getActivationType();
    }
    
    public String getDealerCode() {
        return migrationRequestInfo.getDealerCode();
    }

    public String getMigrationReasonCode() {
        return migrationRequestInfo.getMigrationReasonCode();
    }

    public MigrationType getMigrationType() {
        return migrationRequestInfo.getMigrationType();
    }

    public String getRequestorId() {
        return migrationRequestInfo.getRequestorId();
    }

    public String getSalesRepCode() {
        return migrationRequestInfo.getSalesRepCode();
    }

    public String getUserMemoText() {
        return migrationRequestInfo.getUserMemoText();
    }

    public boolean isDealerAccepteddeposit() {
        return migrationRequestInfo.isDealerAccepteddeposit();
    }

    public void setActivationAirtimeCardNumber(String activationAirtimeCardNumber) {
        migrationRequestInfo.setActivationAirtimeCardNumber(activationAirtimeCardNumber);
    }

    public void setActivationCreditCard(CreditCard activationCreditCard, AuditHeader auditHeader) throws TelusAPIException {
        // Don't use for this release - Oct 2013;
    }

    public void setActivationTopUp(ActivationTopUp activationTopUp) {
        migrationRequestInfo.setActivationTopUp(activationTopUp);
    }

    public void setActivationCreditAmount(double activationCreditAmount) {
        migrationRequestInfo.setActivationCreditAmount(activationCreditAmount);
    }
    
    public void setActivationType(int activationType) {
        migrationRequestInfo.setActivationType(activationType);
    }
    
    public void setDealerAccepteddeposit(boolean dealerAccepteddeposit) {
        migrationRequestInfo.setDealerAccepteddeposit(dealerAccepteddeposit);
    }

    public void setDealerCode(String dealerCode) {
        migrationRequestInfo.setDealerCode(dealerCode);
    }

    public void setMigrationReasonCode(String migrationReasonCode) {
        migrationRequestInfo.setMigrationReasonCode(migrationReasonCode);
    }

    public void setRequestorId(String requestorId) {
        migrationRequestInfo.setRequestorId(requestorId);
    }

    public void setSalesRepCode(String salesRepCode) {
        migrationRequestInfo.setSalesRepCode(salesRepCode);
    }

    public void setUserMemoText(String userMemoText) {
        migrationRequestInfo.setUserMemoText(userMemoText);
    }

	public boolean isDepositTransferInd() {
		return migrationRequestInfo.isDepositTransferInd();
	}

	public void setDepositTransferInd(boolean depositTransferInd) {
		migrationRequestInfo.setDepositTransferInd(depositTransferInd);
	}

	public MemberIdentity getMemeberIdentity() {
		return migrationRequestInfo.getMemeberIdentity();
	}

	public boolean isPhoneOnly() {
		return migrationRequestInfo.isPhoneOnly();
	}

	public boolean isPTNBasedFleet() {
		return migrationRequestInfo.isPTNBasedFleet();
	}

	public void setMemberIdentity(MemberIdentity memberIdentity) {
		migrationRequestInfo.setMemberIdentity(memberIdentity);
	}

	public void setPhoneOnly(boolean phoneOnly) {
		migrationRequestInfo.setPhoneOnly(phoneOnly);
	}

	public void setPTNBasedFleet(boolean PTNBasedFleet) {
		migrationRequestInfo.setPTNBasedFleet(PTNBasedFleet);
	}

	public SubscriberInfo getNewSubscriber() {
		return migrationChangeContext.getNewSubscriber().getDelegate();
	}

	private String getMigrationContext() throws ApplicationException {
		if (migrationContext == null) {
			migrationContext = "migrate [type: " + getMigrationType().getCode() + ", reason: " + getMigrationReasonCode() + "] for [" + migrationChangeContext.getCurrentSubscriber().getPhoneNumber() + "]";
		}
		return migrationContext;
	}

	public void createPortRequestAsNeeded() throws TelusAPIException, ApplicationException {
		
		SubscriberBo sub = migrationChangeContext.getNewSubscriber();

		boolean needToSubmit = migrationRequestInfo.isM2P() || migrationRequestInfo.isP2M();
		sub.logMessage(getMigrationContext(), "needSubmit port request=" + needToSubmit, null);

		String activity = "create port request";
		if (needToSubmit) {
			try {
				PortRequest portRequest = sub.newPortRequest(sub.getPhoneNumber(), PortInEligibility.PORT_DIRECTION_INDICATOR_WIRELESS_WIRELESS, false);
				portRequest.setAgencyAuthorizationIndicator("Y");
				portRequest.setAgencyAuthorizationDate(sub.getStartServiceDate());
				portRequest.setAgencyAuthorizationName(portRequest.getPortRequestName().getFirstName() + " " + portRequest.getPortRequestName().getLastName());

				portRequestId = migrationChangeContext.getSubscriberLifecycleFacade().createPortInRequest(sub.getDelegate(), PortInEligibility.PORT_PROCESS_MIGRATION, Brand.BRAND_ID_NOT_APPLICABLE,
						Brand.BRAND_ID_NOT_APPLICABLE, getOldNetworkType(), getNewNetworkType(), migrationChangeInfo.getApplicationId(), migrationChangeInfo.getRequestorId(),
						(PortRequestInfo) sub.getPortRequest());

				try {
					Long.parseLong(portRequestId);
				} catch (NumberFormatException nfe) {
					throw new PortRequestException(portRequestId);
				}

				sub.logSuccess(getMigrationContext(), activity, "portRequestId=" + portRequestId);

			} catch (ApplicationException ae) {
				throw ae;
			} catch (TelusAPIException tae) {
				sub.logFailure(getMigrationContext(), activity, tae, "portRequestId=" + portRequestId);
				throw tae;
			}
		}
	}

	public void submitPortRequestAsNeeded() throws ApplicationException	{
		String activity = "submit port request [" + portRequestId + "]";
		if ( migrationRequestInfo.isM2P() || migrationRequestInfo.isP2M() ) {
			try {
				migrationChangeContext.getSubscriberLifecycleFacade().submitPortInRequest(portRequestId, migrationChangeInfo.getApplicationId());
				migrationChangeContext.getNewSubscriber().logSuccess(getMigrationContext(), activity, null );
			} catch (ApplicationException ae){
				migrationChangeContext.getNewSubscriber().logFailure(getMigrationContext(), activity, ae, null );
			    throw ae;
			}
        }
	}
	
	public void cancelPortRequestAsNeeded() throws ApplicationException {
		SubscriberBo sub = migrationChangeContext.getNewSubscriber();
		String activity = "cancel port request [" + portRequestId + "]";
		if (migrationRequestInfo.isM2P() || migrationRequestInfo.isP2M() ) {
			try {
				migrationChangeContext.getSubscriberLifecycleFacade().cancelPortInRequest(portRequestId, BILLING_SYSTEM_FAILED, migrationChangeInfo.getApplicationId());
				sub.logSuccess(getMigrationContext(), activity, null );
			} catch (ApplicationException ae) {
				sub.logFailure(getMigrationContext(), activity, ae, null );
			    throw ae;
			}
		}
	}
	
	public void setActivationOption(ActivationOption option) {
		migrationRequestInfo.setActivationOption(option);
	}
	
    private String getOldNetworkType() throws TelusAPIException {
    	return getCurrentSubscriber().getEquipment().getNetworkType(); 
    }
    
    private String getNewNetworkType() throws TelusAPIException, ApplicationException {
    	return getNewEquipment0().getNetworkType();
    }

	private AvailablePhoneNumber getAvailablePhoneNumber(ReferenceDataHelper referenceDataHelper, String phoneNumber, String productType, String dealerCode) throws TelusException {
		
		AvailablePhoneNumberInfo availablePhoneNumber = new AvailablePhoneNumberInfo();

		NumberGroupInfo ngp = referenceDataHelper.retrieveNumberGroupByPortedInPhoneNumberProductType(phoneNumber, productType);
		availablePhoneNumber.setPhoneNumber(phoneNumber);
		Dealer dealer = referenceDataHelper.retrieveDealerbyDealerCode(dealerCode, true);
		ngp.setNumberLocation(dealer.getNumberLocationCD());
		ngp.setDefaultDealerCode(dealerCode);
		ngp.setDefaultSalesCode("0000");
		availablePhoneNumber.setNumberLocationCode(ngp.getNumberLocation());
		availablePhoneNumber.setNumberGroup(ngp);

		return availablePhoneNumber;
	}
	
	protected void checkForVoLTEService() throws ApplicationException {
		// Covent Phase 4: conditionally add the 'SVOLTE' soc to the contract
		try {
			if (getNewEquipment0() != null && getNewEquipment0().isUSIMCard()) {
				EquipmentBo newHandSet = migrationChangeContext.getAssociatedEquipment();

				if (newHandSet != null) {
					ServiceInfo volte = migrationChangeContext.getSubscriberLifecycleFacade().getVolteSocIfEligible(migrationChangeContext.getNewSubscriber().getDelegate(),
							migrationChangeContext.getNewContract().getDelegate(), newHandSet.getDelegate(), false);

					if (volte != null) {
						migrationChangeContext.getNewContract().addService(volte);
					}
				} else {
					logger.debug("Handset is null for subscriber ID [" + migrationChangeContext.getNewSubscriber().getSubscriberId() + "].");
				}

			}
		} catch (Throwable t) {
			// Any errors that occur are to be caught and ignored. Failure to add VoLTE service should not impede the migration of a subscriber.
			migrationChangeContext.getNewSubscriber().logFailure(getMigrationContext(), "checkForVoLTEService [" + t.getMessage() + "]", t, null);
		}
	}
	
}