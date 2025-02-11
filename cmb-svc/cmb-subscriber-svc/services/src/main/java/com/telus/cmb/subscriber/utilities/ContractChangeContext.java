package com.telus.cmb.subscriber.utilities;

import org.apache.log4j.Logger;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.cmb.subscriber.bo.ContractBo;
import com.telus.cmb.subscriber.bo.EquipmentBo;
import com.telus.cmb.subscriber.bo.PricePlanBo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.PricePlanChangeInfo;
import com.telus.eas.utility.info.PricePlanInfo;

public class ContractChangeContext extends BaseChangeContext<ContractChangeInfo> {
	private static Logger logger = Logger.getLogger(ContractChangeContext.class);
	protected ContractBo newContract;
	protected EquipmentBo newEquipment;
	protected boolean activationInd;
	protected boolean pricePlanChangeInd;
	protected boolean renewalInd;
	protected boolean skipSubscriberAndServiceCompatibilityTest = false;
	
	public ContractChangeContext (ContractChangeInfo changeInfo) throws SystemException, ApplicationException {
		super (changeInfo);
	}

	@Override
	public void initialize() throws SystemException, ApplicationException {
		super.initialize();
		ContractChangeInfo changeInfo = getChangeInfo();
		activationInd = changeInfo.isActivationInd();
		pricePlanChangeInd = changeInfo.isPricePlanChangeInd();
		renewalInd = changeInfo.isContractRenewalInd();
	}

	
	/**
	 * @return the activationInd
	 */
	public boolean isActivationInd() {
		return activationInd;
	}

	/**
	 * @param activationInd the activationInd to set
	 */
	public void setActivationInd(boolean activationInd) {
		this.activationInd = activationInd;
	}

	/**
	 * @return the pricePlanChangeInd
	 */
	public boolean isPricePlanChangeInd() {
		return pricePlanChangeInd;
	}

	/**
	 * @param pricePlanChangeInd the pricePlanChangeInd to set
	 */
	public void setPricePlanChangeInd(boolean pricePlanChangeInd) {
		this.pricePlanChangeInd = pricePlanChangeInd;
	}

	/**
	 * @return the renewalInd
	 */
	public boolean isRenewalInd() {
		return renewalInd;
	}

	/**
	 * @param renewalInd the renewalInd to set
	 */
	public void setRenewalInd(boolean renewalInd) {
		this.renewalInd = renewalInd;
	}

	public boolean isSkipSubscriberAndServiceCompatibilityTest() {
		return skipSubscriberAndServiceCompatibilityTest;
	}

	public void setSkipSubscriberAndServiceCompatibilityTest(boolean skipSubscriberAndServiceCompatibilityTest) {
		this.skipSubscriberAndServiceCompatibilityTest = skipSubscriberAndServiceCompatibilityTest;
	}

	/**
	 * This method is responsible for determining the transaction based on ContractChangeInfo and return
	 * the desired "new" contract that is going to be modified.
	 * @return
	 * @throws SystemException
	 * @throws ApplicationException
	 * @throws TelusAPIException
	 */
	public ContractBo getNewContract() throws SystemException, ApplicationException{
		if (newContract == null && getChangeInfo().getNewSubscriberContractInfo() != null) {
			newContract = new ContractBo(getChangeInfo().getNewSubscriberContractInfo(), this);
			updateContractValidationInfo(newContract, getChangeInfo().getPricePlanValidatioInfo());
		}else if (newContract == null) {		
			if (!pricePlanChangeInd) { 
				//if it's not a PPC, then we are changing based on the copy of current contract. This applies to RENEWAL and pure service/feature changes 
				newContract = new ContractBo(getCurrentContract().getDelegate(), this);
			}else {
				//otherwise we create a new copy of contract
				PricePlanChangeInfo pricePlanChgInfo = getChangeInfo().getPricePlanChangeInfo();
				PricePlanInfo pricePlanInfo = null;
				try {				
					pricePlanInfo = getRefDataFacade().getPricePlan(pricePlanChgInfo.getCode());
					if (pricePlanInfo == null) {
						throw new ApplicationException (SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_PRICE_PLAN, "Invalid Price Plan ["+pricePlanChgInfo.getCode()+"]", "");
					}
				}catch (TelusException e) {
					throw new ApplicationException (SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
				}
				

				PricePlanBo pricePlan = new PricePlanBo(pricePlanInfo);
				if (!renewalInd && pricePlanChangeInd) {
					newContract = (ContractBo) getCurrentSubscriber().newContract(pricePlan, getChangeInfo().getContractTerm(), getChangeInfo().getEquipmentChangeRequestInfo());
				}else if (renewalInd) {
					newContract = (ContractBo) getCurrentSubscriber().renewContract(pricePlan, getChangeInfo().getContractTerm(), getChangeInfo().getEquipmentChangeRequestInfo());
				}
			}
			updateContractValidationInfo(newContract, getChangeInfo().getPricePlanValidatioInfo());
		}
		
		return newContract;
	}
	
	
	public EquipmentChangeRequestInfo getEquipmentChangeRequest() {
		if (getChangeInfo() instanceof ContractChangeInfo) {
			ContractChangeInfo changeInfo = (ContractChangeInfo) getChangeInfo();
			
			return changeInfo.getEquipmentChangeRequestInfo();
		}
		
		return null;
	}

	/**
	 * This method returns the new equipment in equipment change scenario
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	public EquipmentBo getNewEquipment() throws ApplicationException {
		if (newEquipment == null && getEquipmentChangeRequest() != null) {
			EquipmentInfo newEquipment = (EquipmentInfo) getEquipmentChangeRequest().getNewEquipment();

			if (newEquipment == null && getEquipmentChangeRequest().getNewEquipmentSerialNumber() != null) {
				newEquipment = getProductEquipmentHelper().getEquipmentInfobySerialNo(getEquipmentChangeRequest().getNewEquipmentSerialNumber());
			}

			if (newEquipment != null) {
				getEquipmentChangeRequest().setNewEquipment(newEquipment);
				this.newEquipment = new EquipmentBo(newEquipment, this);
			}
		}
		
		return newEquipment;
	}

	private void updateContractValidationInfo(ContractBo contract, PricePlanValidationInfo ppValidationInfo) {
		if (ppValidationInfo != null) {
			PricePlanValidationInfo validationInfo = contract.getDelegate().getPricePlanValidation0();
			validationInfo.setCurrentValidation(ppValidationInfo.validateCurrent());
			validationInfo.setEquipmentServiceMatch(ppValidationInfo.validateEquipmentServiceMatch());
			validationInfo.setForSaleValidation(ppValidationInfo.validateForSale());
			validationInfo.setPricePlanServiceGrouping(ppValidationInfo.validatePricePlanServiceGrouping());
			validationInfo.setProvinceServiceMatch(ppValidationInfo.validateProvinceServiceMatch());
		}
	}
}
