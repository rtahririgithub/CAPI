package com.telus.cmb.subscriber.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.equipment.SIMCardEquipment;
import com.telus.api.reference.NetworkType;
import com.telus.cmb.subscriber.decorators.EquipmentDecorator;
import com.telus.cmb.subscriber.decorators.PricePlanDecorator;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.cmb.subscriber.utilities.contract.ContractUtilities;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServicePeriodInfo;
import com.telus.eas.utility.info.ServiceRelationInfo;
import com.telus.eas.utility.info.ServiceSetInfo;

public class PricePlanBo extends PricePlanDecorator {
	private static final Log logger = LogFactory.getLog(PricePlanBo.class);

	public PricePlanBo(PricePlanInfo pricePlan) {
		super(pricePlan);
	}

	@Override
	public ServiceInfo[] getOptionalServices(EquipmentDecorator equipment) {
		String equipmentType = equipment.getEquipmentType();
		
		return getOptionalServices ((EquipmentBo) equipment, equipmentType);
	}
	
	private ServiceInfo[] getOptionalServices(EquipmentBo equipment, String overrideEquipmentType) {
		// TODO: use better algorithm--this one sucks--it won't remove any dual
		// resource service.
	  
		//PROD00187920 fix begin,  if overrideEquipmentType is null, use equipment object's type
		if (overrideEquipmentType==null ) {
			overrideEquipmentType = equipment.getEquipmentType();
		}
	    
		if (Equipment.EQUIPMENT_TYPE_USIM.equals(overrideEquipmentType) ) {
			overrideEquipmentType = AppConfiguration.getDefaultHSPAEquipmentType();
		}
		//PROD00187920 fix end

		MuleEquipment mule = null;
		if (equipment.isSIMCard()) {
			try {
				mule = ((SIMCardEquipment) equipment).getLastMule();
			} catch (TelusAPIException e) {
				logger.debug(">>>> Mule Equipment is null");
			}
		}

		ServiceInfo[] services = delegate.getOptionalServices0();
		try {
			if (!equipment.isHSPA()) {

				if (!equipment.isDispatchEnabled()) {
					services = ContractUtilities.removeDispatchOnly(services);
				}

				if (!equipment.isTelephonyEnabled()) {
					services = ContractUtilities.removeTelephonyOnly(services);
				}

				if (!equipment.isWirelessWebEnabled()) {
					services = ContractUtilities.removeWirelessWebOnly(services);
				}
				/******** Updated for Combo plan CR- Anitha Duraisamy - start ********/

				services = ContractUtilities.retainServicesForPreHSPA(services, equipment);

			}else { //HSPA
				services = ContractUtilities.retainServicesForHSPA(services, equipment);

			}
		}catch (Throwable t) {
			logger.error("Error while getting optional services: ", t);
			
		}
		/******** Updated for Combo plan CR- Anitha Duraisamy - end ********/

		services = ContractUtilities.retainServicesByNetworkAndEquipmentType(services, getNetworkType(equipment),  overrideEquipmentType );
		return services;
	}
	
	/**
	 * Helper method to return proper network type of equipment
	 * @param equipment
	 * @return
	 */
	private String getNetworkType(EquipmentDecorator equipment)  {
		String networkType = NetworkType.NETWORK_TYPE_ALL;;

		try {
			networkType = equipment.getNetworkType();
		}catch (TelusAPIException e) {
			logger.debug ("getNetworkType:"+e);
		}

		return networkType;
	}

	@Override
	public ServiceRelationInfo[] getRelations(SubscriberContractInfo contract) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceRelationInfo[] getRelations(SubscriberContractInfo contract, String relationType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceRelationInfo[] getRelations(PricePlanInfo pricePlan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceRelationInfo[] getRelations(PricePlanInfo pricePlan, String relationType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceInfo getEquivalentService(PricePlanInfo pricePlan, String networkType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServicePeriodInfo[] getServicePeriods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceSetInfo[] getMandatoryServiceSets(EquipmentInfo equipment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceSetInfo[] getMandatoryServiceSets(String equipmentType, String networkType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceSetInfo[] getMandatoryServiceSets(EquipmentInfo equipment, String overrideEquipmentType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceInfo[] getOptionalServices(EquipmentInfo equipment, boolean includePrepaidServices) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceInfo[] getOptionalServices(EquipmentInfo equipment, boolean includePrepaidServices, String overrideEquipmentType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PricePlanDecorator[] getPricePlanFamily(String provinceCode, String equipmentType, boolean currentPlansOnly, int termInMonths) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PricePlanDecorator[] getPricePlanFamily(String provinceCode, String equipmentType, String networkType, boolean currentPlansOnly, int termInMonths) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceInfo[] getIncludedPromotions(String equpmentType, String provinceCode, int termInMonths) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceInfo[] getIncludedPromotions(String networkType, String equpmentType, String provinceCode, int termInMonths) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceInfo[] getIncludedPromotions(EquipmentInfo equipment, String provinceCode, int termInMonths) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
