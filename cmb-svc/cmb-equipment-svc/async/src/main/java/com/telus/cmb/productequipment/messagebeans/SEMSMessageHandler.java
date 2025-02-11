package com.telus.cmb.productequipment.messagebeans;

import org.apache.log4j.Logger;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.cmb.productequipment.domain.SemsEquipmentInfo;
import com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacade;

public class SEMSMessageHandler implements MessageHandler<SemsEquipmentInfo> {

	private static final Logger LOGGER = Logger.getLogger(SEMSMessageHandler.class);
	
	@Override
	public void handle(SemsEquipmentInfo semsEquipmentInfo,ClientIdentity clientIdentity, MessageBeanContext beanContext)
			throws ApplicationException {

		String semsMethodType = semsEquipmentInfo.getSemsMethodType();
		ProductEquipmentLifecycleFacade productEquipmentLifecycleFacade = beanContext.getEjb(ProductEquipmentLifecycleFacade.class);

		if (SemsEquipmentInfo.METHOD_TYPE_ASSIGN_EQUIPMENT_PHONENUMBER.equals(semsMethodType)) {
			productEquipmentLifecycleFacade.assignEquipmentToPhoneNumber(
					semsEquipmentInfo.getPhoneNumber(),
					semsEquipmentInfo.getSerialNumber(),
					semsEquipmentInfo.getAssociatedHandsetIMEI());
		} else if (SemsEquipmentInfo.METHOD_TYPE_CHANGE_PHONENUMBER.equals(semsMethodType)) {
			productEquipmentLifecycleFacade.changePhoneNumber(
					semsEquipmentInfo.getSerialNumber(),
					semsEquipmentInfo.getPhoneNumber());
		} else if (SemsEquipmentInfo.METHOD_TYPE_APPROVE_RESERVED_EQUIPMENT_PHONENUMBER.equals(semsMethodType)) {
			productEquipmentLifecycleFacade.approveReservedEquipmentForPhoneNumber(
					semsEquipmentInfo.getPhoneNumber(),
					semsEquipmentInfo.getSerialNumber(),
					semsEquipmentInfo.getAssociatedHandsetIMEI());
		} else if (SemsEquipmentInfo.METHOD_TYPE_RELEASE_RESERVED_EQUIPMENT_PHONENUMBER.equals(semsMethodType)) {
			productEquipmentLifecycleFacade.releaseReservedEquipmentForPhoneNumber(
					semsEquipmentInfo.getPhoneNumber(),
					semsEquipmentInfo.getSerialNumber(),
					semsEquipmentInfo.getAssociatedHandsetIMEI());
		} else if (SemsEquipmentInfo.METHOD_TYPE_DISASSOCIATE_EQUIPMENT_PHONENUMBER.equals(semsMethodType)) {
			productEquipmentLifecycleFacade.disassociateEquipmentFromPhoneNumber(
					semsEquipmentInfo.getPhoneNumber(),
					semsEquipmentInfo.getUsimId());
		}
		else if (SemsEquipmentInfo.METHOD_TYPE_SWAP_HSPA_EQUIPMENT_PHONENUMBER.equals(semsMethodType)) {
			productEquipmentLifecycleFacade.swapHSPAOnlyEquipmentForPhoneNumber(
					semsEquipmentInfo.getPhoneNumber(),
					semsEquipmentInfo.getOldSerialNumber(),
					semsEquipmentInfo.getNewSerialNumber(),
					semsEquipmentInfo.getOldAssociatedHandsetIMEI(),
					semsEquipmentInfo.getNewAssociatedHandsetIMEI());
		}
		else {
			LOGGER.error("Invalid semsMethodType passed to SEMSMessageHandler :"+semsMethodType);
		}

	}

}
