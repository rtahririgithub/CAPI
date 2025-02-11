package com.telus.provider.account;

import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.Contract;
import com.telus.api.account.MigrateSeatRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.SeatType;
import com.telus.eas.account.info.MigrateSeatRequestInfo;
import com.telus.provider.TMProvider;

public abstract class TMMigrateSeatRequest implements MigrateSeatRequest {
	
	protected final TMProvider provider;
	protected final transient Subscriber currentSubscriber;
	protected final transient TMAccount newAccount;
	protected transient Contract newContract;
	protected final MigrateSeatRequestInfo migrateSeatRequestInfo;
	
	public TMMigrateSeatRequest(TMProvider provider, MigrationType migrationType, Subscriber currentSubscriber, TMAccount newAccount, String pricePlanCode)
			throws UnknownBANException, TelusAPIException {

		this.provider = provider;
		this.currentSubscriber = currentSubscriber;
		this.newAccount = newAccount;
		((TMAccount) newAccount).assertAccountExists();
        migrateSeatRequestInfo = new MigrateSeatRequestInfo(migrationType);
        setTargetPricePlanCode(pricePlanCode);
        PricePlan pricePlan = provider.getReferenceDataManager().getPricePlan(pricePlanCode, provider.getEquipmentManager0().translateEquipmentType(currentSubscriber.getEquipment()),
        		currentSubscriber.getNumberGroup().getProvinceCode(), newAccount.getAccountType(), newAccount.getAccountSubType(), newAccount.getBrandId());
        if (migrateSeatRequestInfo.isBCMobileToPCSPostPaid() || migrateSeatRequestInfo.isPCSPostPaidToBCMobile()) {
            newContract = ((TMPCSSubscriber) currentSubscriber).newContract(pricePlan, Subscriber.TERM_PRESERVE_COMMITMENT, false, newAccount);
        } else {
        	throw new RuntimeException("Subscriber not supported for Migration [" + migrateSeatRequestInfo.getMigrationType() + "]");
        }
	}

	public static final TMMigrateSeatRequest newMigrateSeatRequest(TMProvider provider, Subscriber currentSubscriber, TMAccount newAccount, String pricePlanCode)
			throws UnknownBANException, TelusAPIException {

		// July 2014 release - only support 2 seat migrations: Mobile Seat -> Postpaid (MSPO) and Postpaid -> Mobile Seat (POMS)		
		// Mobile Seat -> Postpaid (MSPO)
		// Current business rules only support PCS Postpaid Consumer Regular, Business Personal or Business Regular as target account types when migrating from a Business Connect account
		if (newAccount.isPCS() && !newAccount.isPostpaidBusinessConnect() && 
				(newAccount.isPostpaidConsumer() || newAccount.isPostpaidBusinessPersonal() || newAccount.isPostpaidBusinessRegular())) {
			// Check if the current subscriber is a Business Connect subscriber
			if (currentSubscriber.getAccount().isPostpaidBusinessConnect() && currentSubscriber.isPCS()) {
				// Check the seat data to determine if this is a mobile seat subscriber
				if (currentSubscriber.getSeatData() != null && currentSubscriber.getSeatData().getSeatType() != null &&
						currentSubscriber.getSeatData().getSeatType().equalsIgnoreCase(SeatType.SEAT_TYPE_MOBILE)) {
					return new TMBCMobileSeatToPCSPostPaidMigrateSeatRequest(provider, provider.getReferenceDataManager().getMigrationType(MigrationType.BC_MOBILE_TO_PCS_POST),
							currentSubscriber, newAccount, pricePlanCode); 
				} else {
					String errorMessage = "Seat migration is not currently supported for seat type: [" + 
							currentSubscriber.getSeatData() != null && currentSubscriber.getSeatData().getSeatType() != null ? currentSubscriber.getSeatData().getSeatType() : null + "].";
					throw new UnsupportedOperationException(errorMessage);
				}
			}
		}

		String errorMessage = "Seat migration is not currently supported for account type/sub-type: [" + 
				currentSubscriber.getAccount().getAccountType() + "/" + currentSubscriber.getAccount().getAccountSubType() + "] to [" + newAccount.getAccountType() + "/" +
				newAccount.getAccountSubType() + "]";
		throw new UnsupportedOperationException(errorMessage);
	}
	
	public static final TMMigrateSeatRequest newMigrateSeatRequest(TMProvider provider, Subscriber currentSubscriber, TMAccount newAccount, String pricePlanCode, String targetSeatTypeCode,
			String targetSeatGroupId) throws UnknownBANException, TelusAPIException {

		// July 2014 release - only support 2 seat migrations: Mobile Seat -> Postpaid (MSPO) and Postpaid -> Mobile Seat (POMS)	
		// Postpaid -> Mobile Seat (POMS)
		if (newAccount.isPCS() && (newAccount.isPostpaidBusinessConnect())) {
			// Check if the current subscriber is a PCS Postpaid Consumer Regular, Business Personal or Business Regular subscriber
			if (currentSubscriber.isPCS() && !currentSubscriber.getAccount().isPostpaidBusinessConnect() && 
					(currentSubscriber.getAccount().isPostpaidConsumer() || currentSubscriber.getAccount().isPostpaidBusinessPersonal() || currentSubscriber.getAccount().isPostpaidBusinessRegular())) {
				// Current business rules only support BC Mobile Seat type when migrating to a Business Connect account
				// Check the target seat data to determine if we're migrating to a mobile seat subscriber
				if (targetSeatTypeCode.equalsIgnoreCase(SeatType.SEAT_TYPE_MOBILE)) {
					return new TMPCSPostPaidToBCMobileSeatMigrateSeatRequest(provider, provider.getReferenceDataManager().getMigrationType(MigrationType.PCS_POST_TO_BCMOBILE),
							currentSubscriber, newAccount, pricePlanCode, targetSeatTypeCode, targetSeatGroupId);
				} else {
					String errorMessage = "Seat migration is not currently supported for seat type: [" + targetSeatTypeCode + "].";
					throw new UnsupportedOperationException(errorMessage);
				}
			}
		}

		String errorMessage = "Seat migration is not currently supported for account type/sub-type: [" + 
				currentSubscriber.getAccount().getAccountType() + "/" + currentSubscriber.getAccount().getAccountSubType() + "] to [" + newAccount.getAccountType() + "/" +
				newAccount.getAccountSubType() + "]";
		throw new UnsupportedOperationException(errorMessage);
	}
	
	public abstract boolean testMigrationRequest() throws InvalidMigrationRequestException, TelusAPIException;

    public abstract void postMigrationTask() throws InvalidMigrationRequestException, TelusAPIException;

    public abstract void preMigrationTask() throws InvalidMigrationRequestException, TelusAPIException;
	
	public void validateMandatoryFields() throws InvalidMigrationRequestException, TelusAPIException {
		
		if (getNewAccount() == null) {
			throw new InvalidMigrationRequestException("Invalid target account", InvalidMigrationRequestException.REASON_INVALID_MANDATORY_FIELDS);
		}
		if (getTargetPricePlanCode() == null) {
			throw new InvalidMigrationRequestException("MandatoryField missing [TargetPricePlanCode]", InvalidMigrationRequestException.REASON_INVALID_MANDATORY_FIELDS);
		}
		if (getMigrationReasonCode() == null) {
			throw new InvalidMigrationRequestException("MandatoryField missing [MigrationReasonCode]", InvalidMigrationRequestException.REASON_INVALID_MANDATORY_FIELDS);
		}
	}

	public Account getNewAccount() {
		return newAccount;
	}

	public Contract getNewContract() throws TelusAPIException {
		return newContract;
	}

    public Subscriber getCurrentSubscriber() {
        return currentSubscriber;
    }
    
	public int getTargetBan() {
		return newAccount.getBanId();
	}

    public MigrateSeatRequestInfo getDelegate() {
        return migrateSeatRequestInfo;
    }

    public MigrationType getMigrationType() {
        return migrateSeatRequestInfo.getMigrationType();
    }
    
    public String getMigrationReasonCode() {
        return migrateSeatRequestInfo.getMigrationReasonCode();
    }
    
    public void setMigrationReasonCode(String migrationReasonCode) {
        migrateSeatRequestInfo.setMigrationReasonCode(migrationReasonCode);
    }
    
	public String getTargetPricePlanCode() {
		return migrateSeatRequestInfo.getTargetPricePlanCode();
	}

    public void setTargetPricePlanCode(String targetPricePlanCode) {
    	migrateSeatRequestInfo.setTargetPricePlanCode(targetPricePlanCode);
	}
    
    public String getDealerCode() {
        return migrateSeatRequestInfo.getDealerCode();
    }

    public void setDealerCode(String dealerCode) {
        migrateSeatRequestInfo.setDealerCode(dealerCode);
    }
    
    public String getSalesRepCode() {
        return migrateSeatRequestInfo.getSalesRepCode();
    }

    public void setSalesRepCode(String salesRepCode) {
        migrateSeatRequestInfo.setSalesRepCode(salesRepCode);
    }
    
    public String getUserMemoText() {
        return migrateSeatRequestInfo.getUserMemoText();
    }
    
    public void setUserMemoText(String userMemoText) {
        migrateSeatRequestInfo.setUserMemoText(userMemoText);
    }    
    
	public String getTargetSeatTypeCode() {
		return migrateSeatRequestInfo.getTargetSeatTypeCode();
	}

	public void setTargetSeatTypeCode(String targetSeatTypeCode) {
		migrateSeatRequestInfo.setTargetSeatTypeCode(targetSeatTypeCode);
	}
	
	public String getTargetSeatGroupId() {
		return migrateSeatRequestInfo.getTargetSeatGroupId();
	}

	public void setTargetSeatGroupId(String targetSeatGroupId) {
		migrateSeatRequestInfo.setTargetSeatGroupId(targetSeatGroupId);
	}
    
    public String getRequestorId() {
        return migrateSeatRequestInfo.getRequestorId();
    }
    
    public void setRequestorId(String requestorId) {
        migrateSeatRequestInfo.setRequestorId(requestorId);
    }
    
    public ActivationOption getActivationOption() {
    	throw new UnsupportedOperationException("Method is not implemented.");
    }
    
    public void setActivationOption(ActivationOption option) {
    	throw new UnsupportedOperationException("Method is not implemented.");
    }
    
	public Equipment getNewEquipment() {
		throw new UnsupportedOperationException("Method is not implemented.");
	}
  
}