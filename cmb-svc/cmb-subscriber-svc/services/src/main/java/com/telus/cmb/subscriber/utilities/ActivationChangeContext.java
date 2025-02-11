package com.telus.cmb.subscriber.utilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.subscriber.bo.ActivationBo;
import com.telus.cmb.subscriber.utilities.activation.ActivationUtilities;
import com.telus.cmb.subscriber.utilities.contract.ContractUtilities;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.utility.info.PricePlanInfo;

/**
 * @Author Brandon Wen, R. Fong
 */
public class ActivationChangeContext extends BaseChangeContext<ActivationChangeInfo> {
	
	private static final Log logger = LogFactory.getLog(ActivationChangeContext.class);
	
	private ActivationBo activationBo = null;

	public ActivationChangeContext(ActivationChangeInfo changeInfo) throws ApplicationException {
		super(changeInfo);
	}

	@Override
	public void initialize() throws ApplicationException {

		if (getChangeInfo().getPortInEligibility() != null) {
			// As BSA/SA confirmation at Nov 26, 2013 - WAR Apple (Jan 2014 release) does not support port-in activation.   
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ACTIVATE_GENERAL_ERROR, "Port-in not supported", "");
		}
		if (getChangeInfo().getCurrentSubscriberInfo() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ACTIVATE_GENERAL_ERROR, "Missing subscriber activation data", "");
		}
		if (getChangeInfo().getCurrentContractInfo() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ACTIVATE_GENERAL_ERROR, "Missing subscriber contract data", "");
		}

		activationBo = new ActivationBo(this);
		
		// Retrieve and enrich the equipmentInfo - this needs to be done first, as there are dependencies below
		EquipmentInfo equipmentInfo = getProductEquipmentHelper().getEquipmentInfobySerialNo(getChangeInfo().getPrimaryEquipmentSerialNumber());
		equipmentInfo.setAssociatedHandsetIMEI(getChangeInfo().getAssociatedHandsetSerialNumber());
		getChangeInfo().setCurrentEquipmentInfo(equipmentInfo);
		
		// Enrich the subscriberContractInfo
		try {
			PricePlanInfo pricePlanInfo = getRefDataFacade().getPricePlan(
					"", 
					getChangeInfo().getCurrentContractInfo().getPricePlanCode(),
		    		ContractUtilities.translateEquipmentType(equipmentInfo, getProductEquipmentHelper()),
		    		getChangeInfo().getCurrentSubscriberInfo().getMarketProvince(),
		    		Character.toString(getCurrentAccount().getDelegate().getAccountType()),
		    		Character.toString(getCurrentAccount().getDelegate().getAccountSubType()),
		    		getCurrentAccount().getDelegate().getBrandId());
			getChangeInfo().getCurrentContractInfo().setPricePlanInfo(pricePlanInfo);
			
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ACTIVATE_GENERAL_ERROR, t.getMessage(), "", t);
		}

		// Finally, enrich the subscriberInfo with additional contract and equipment info
		//getChangeInfo().setCurrentSubscriberInfo(createReservedSubscriberInfo());
		getChangeInfo().getCurrentSubscriberInfo().setPricePlan(getChangeInfo().getCurrentContractInfo().getPricePlanCode());
		getChangeInfo().getCurrentSubscriberInfo().setSerialNumber(getChangeInfo().getCurrentEquipmentInfo().getSerialNumber());
		getChangeInfo().getCurrentSubscriberInfo().setEquipmentType(getChangeInfo().getCurrentEquipmentInfo().getEquipmentType());
		getChangeInfo().getCurrentSubscriberInfo().setEquipment(equipmentInfo);
		
		// The super method will handle the current ContractBo, EquipmentBo and SubscriberBo initialization
		super.initialize();
		
		// Validate the equipment and brand combination for activation
		ActivationUtilities.activationValidate(getCurrentSubscriber().getBrandId(), getCurrentEquipment().getDelegate());
	}
	
	public ActivationChangeInfo activate(boolean validateOnly) throws ApplicationException {
		
		logger.debug("activate(validateOnly=" + validateOnly + ")");
		try {
			if (!StringUtils.hasText(getCurrentSubscriber().getPhoneNumber())) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.NO_PHONE_NUMBER_RESERVED, "no phone number has been reserved");
			}	
			if (getChangeInfo().isDealerHasDeposit() && !StringUtils.hasText(getCurrentSubscriber().getDealerCode())) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "dealerHasDeposit == true, but dealerCode is unset", "");
			}
			
			EquipmentInfo primaryEquipment =  getChangeInfo().getCurrentEquipmentInfo();
			if (!(EquipmentInfo.DUMMY_ESN_FOR_VOIP.equals(primaryEquipment.getSerialNumber()) || EquipmentInfo.DUMMY_ESN_FOR_HSIA.equals(primaryEquipment.getSerialNumber()))) {
				if(!primaryEquipment.isUSIMCard()){
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Invalid equipment. Primary equipment is not USIM + [ "+getChangeInfo().getCurrentEquipmentInfo().getSerialNumber() +" ]", "");
				}
			}

			if (!validateOnly) {
				getChangeInfo().setNewSubscriberInfo(activationBo.activate());
			}
			
			return getChangeInfo();
			
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ACTIVATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}
	
}