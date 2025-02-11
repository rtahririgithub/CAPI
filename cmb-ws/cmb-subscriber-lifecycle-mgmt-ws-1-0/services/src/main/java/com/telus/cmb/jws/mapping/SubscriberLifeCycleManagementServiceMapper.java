package com.telus.cmb.jws.mapping;

import java.util.List;

import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.cmb.jws.WarningBaseType;
import com.telus.cmb.jws.WarningType;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.NumberGroup;

/**
 * SubscriberLifeCycleManagementServiceMapper
 * @author tongts
 *
 */
public class SubscriberLifeCycleManagementServiceMapper {

	public static ServiceWarningMapper ServiceWarningMapper() {
		return ServiceWarningMapper.getInstance();
	}
	
	public static WarningBaseTypeMapper WarningBaseTypeMapper() {
		return WarningBaseTypeMapper.getInstance();
	}

	public static NumberGroupMapper NumberGroupMapper() {
		return NumberGroupMapper.getInstance();
	}
	
	
	/**
	 * ServiceWarningMapper
	 * @author tongts
	 *
	 */
	public static class ServiceWarningMapper extends AbstractSchemaMapper<WarningType, WarningFaultInfo> {
		private static ServiceWarningMapper INSTANCE;
		
		private ServiceWarningMapper() {
			super(WarningType.class, WarningFaultInfo.class);
		}
		
		protected static ServiceWarningMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ServiceWarningMapper();			}
			
			return INSTANCE;
		}

		@Override
		protected WarningFaultInfo performDomainMapping(WarningType source, WarningFaultInfo target) {
			// TODO Auto-generated method stub
			return super.performDomainMapping(source, target);
		}

		@Override
		protected WarningType performSchemaMapping(WarningFaultInfo source, WarningType target) {
			target.setSystemCode(source.getSystemCode());
			target.setWarningType(source.getWarningType());
			target.setWarning(WarningBaseTypeMapper().mapToSchema(source));
			return super.performSchemaMapping(source, target);
		}

		
	}

	/**
	 * WarningBaseTypeMapper
	 * @author tongts
	 *
	 */
	public static class WarningBaseTypeMapper extends AbstractSchemaMapper<WarningBaseType, WarningFaultInfo> {
		private static WarningBaseTypeMapper INSTANCE;
		
		private WarningBaseTypeMapper() {
			super (WarningBaseType.class, WarningFaultInfo.class);
		}
		
		protected static WarningBaseTypeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new WarningBaseTypeMapper();
			}
			
			return INSTANCE;
		}

		@Override
		protected WarningBaseType performSchemaMapping(WarningFaultInfo source, WarningBaseType target) {
			target.setMessageId(source.getMessageId());
			target.setWarningCode(source.getErrorCode());
			target.setWarningMessage(source.getErrorMessage());
			return super.performSchemaMapping(source, target);
		}
		
		
	}
	
	public static class NumberGroupMapper extends AbstractSchemaMapper<NumberGroup, NumberGroupInfo> {
		private static NumberGroupMapper INSTANCE;

		private NumberGroupMapper() {
			super(NumberGroup.class, NumberGroupInfo.class);
		}

		protected static NumberGroupMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new NumberGroupMapper();
			}
			return INSTANCE;
		}

		@Override
		protected NumberGroupInfo performDomainMapping(NumberGroup source,NumberGroupInfo target) {
		
			target.setDefaultDealerCode(source.getDefaultDealerCode());
			target.setDefaultSalesCode(source.getDefaultSalesRepCode());
			target.setCode(source.getCode());
			target.setDescription(source.getDescription());
			target.setDescriptionFrench(source.getDescriptionFrench());
			target.setNetworkId(source.getNetworkId());
			 List<String> npanxxList = source.getNpaNXX();
			String [] npanxxArray = (String[]) npanxxList.toArray(new String[npanxxList.size()]);
			target.setNpaNXX(npanxxArray);
			target.setNumberLocation(source.getNumberLocation());
			target.setProvinceCode(source.getProvinceCode().value());
			return super.performDomainMapping(source, target);
		}
	}

}
