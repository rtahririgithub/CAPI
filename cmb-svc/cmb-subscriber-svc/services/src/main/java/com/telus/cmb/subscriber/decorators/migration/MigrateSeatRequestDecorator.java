package com.telus.cmb.subscriber.decorators.migration;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.Contract;
import com.telus.api.account.MigrateSeatRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.MigrationType;
import com.telus.cmb.subscriber.utilities.MigrateSeatChangeContext;
import com.telus.eas.account.info.MigrateSeatRequestInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.MigrateSeatChangeInfo;

/**
 * @Author R. Fong
 * 
 * @see TMMigrateSeatRequest.java
 */
public abstract class MigrateSeatRequestDecorator implements MigrateSeatRequest {

	protected String migrationTypeCode;
	protected MigrateSeatChangeContext migrateSeatChangeContext;
	protected MigrateSeatRequestInfo migrateSeatRequestInfo;
	protected MigrateSeatChangeInfo migrateSeatChangeInfo;

    public MigrateSeatRequestDecorator(String code, MigrateSeatChangeContext context) throws TelusException, ApplicationException {
    	
    	migrationTypeCode = code;
    	migrateSeatChangeContext = context;
    	migrateSeatChangeInfo = context.getChangeInfo();
    	migrateSeatRequestInfo = new MigrateSeatRequestInfo(context.getRefDataFacade().getMigrationType(migrationTypeCode));
    	migrateSeatRequestInfo.setTargetBan(migrateSeatChangeInfo.getTargetAccountNumber());
    	migrateSeatRequestInfo.setTargetPricePlanCode(migrateSeatChangeInfo.getPricePlanCode());
    	migrateSeatRequestInfo.setMigrationReasonCode(migrateSeatChangeInfo.getActivityReasonCode());
    	migrateSeatRequestInfo.setUserMemoText(migrateSeatChangeInfo.getMemoText());
    	migrateSeatRequestInfo.setDealerCode(migrateSeatChangeInfo.getDealerCode());
    	migrateSeatRequestInfo.setSalesRepCode(migrateSeatChangeInfo.getSalesRepCode());
    	migrateSeatRequestInfo.setTargetSeatTypeCode(migrateSeatChangeInfo.getTargetSeatTypeCode());
    	migrateSeatRequestInfo.setTargetSeatGroupId(migrateSeatChangeInfo.getTargetSeatGroupId());
    }

	public void validateMigrateSeatRequest() throws ApplicationException {
		
		if (getNewAccount() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Invalid target account", "");
		}
		if (getTargetPricePlanCode() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Missing mandatory field [TargetPricePlanCode]", "");
		}
		if (getMigrationReasonCode() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Missing mandatory field [MigrationReasonCode]", "");
		}
	}
	
	protected EquipmentInfo getCurrentEquipment() throws ApplicationException {
		return migrateSeatChangeContext.getCurrentEquipment().getDelegate();
	}

	public Account getNewAccount() {
		try {
			return migrateSeatChangeContext.getNewAccount().getDelegate();
		} catch (ApplicationException e) {
			return null;
		}
	}
	
	public Contract getNewContract() throws TelusAPIException {
		return migrateSeatChangeContext.getNewContract().getDelegate();
	}
	
	public abstract boolean testMigrationRequest() throws ApplicationException;
	
    public abstract void postMigrationTask() throws TelusAPIException, ApplicationException;

    public abstract void preMigrationTask()	throws TelusAPIException, ApplicationException;
	
	public MigrateSeatRequestInfo getDelegate() {
        return migrateSeatRequestInfo;
    }
    
    public Subscriber getCurrentSubscriber() {
        return migrateSeatChangeInfo.getCurrentSubscriberInfo();
    }
    
    public Account getCurrentAccount() {
        return migrateSeatChangeInfo.getCurrentAccountInfo();
    }
    
    public MigrationType getMigrationType() {
        return migrateSeatRequestInfo.getMigrationType();
    }
	
	public int getTargetBan() {
		return migrateSeatRequestInfo.getTargetBan();
	}
	
	public void setTargetBan(int targetBan) {
		migrateSeatRequestInfo.setTargetBan(targetBan);
	}
	
	public String getTargetPricePlanCode() {
		return migrateSeatRequestInfo.getTargetPricePlanCode();
	}
	
	public void setTargetPricePlanCode(String targetPricePlanCode) {
		migrateSeatRequestInfo.setTargetPricePlanCode(targetPricePlanCode);
	}
	
    public String getMigrationReasonCode() {
        return migrateSeatRequestInfo.getMigrationReasonCode();
    }

    public void setMigrationReasonCode(String migrationReasonCode) {
    	migrateSeatRequestInfo.setMigrationReasonCode(migrationReasonCode);
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