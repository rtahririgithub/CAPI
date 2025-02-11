package com.telus.cmb.account.mapping;

import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.EnterpriseAddressInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customercommon_v3.Address;

public class EnterpriseAddressValidationServiceMapper {

	public static EnterpriseAddressInfoMapper EnterpriseAddressMapper() {
		return EnterpriseAddressInfoMapper.getInstance();
	}
	
	/**
	 * EnterpriseAddressInfoMapper
	 * @author tongts
	 *
	 */
	public static class EnterpriseAddressInfoMapper extends AbstractSchemaMapper<Address, EnterpriseAddressInfo> {

		private static EnterpriseAddressInfoMapper INSTANCE;
		
		private EnterpriseAddressInfoMapper(){
			super(Address.class, EnterpriseAddressInfo.class);
		}

		protected static synchronized EnterpriseAddressInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new EnterpriseAddressInfoMapper();
			}
			return INSTANCE;
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected Address performSchemaMapping(EnterpriseAddressInfo source, Address target) {
			
			if(source.getAdditionalAddressInformation()!= null)
				target.setAdditionalAddressInformation(source.getAdditionalAddressInformation());
			target.setAddrAssgnmtId(source.getAddrAssgnmtId());
			if(source.getAddressAssignmentSubTypeCode() != null)
				target.setAddressAssignmentSubTypeCode(source.getAddressAssignmentSubTypeCode());
			if(source.getAddressAssignmentTypeCode() != null)
				target.setAddressAssignmentTypeCode(source.getAddressAssignmentTypeCode());
			target.setAddressId(source.getAddressId());
			if(source.getAddressMatchingStatusCode() != null)
				target.setAddressMatchingStatusCode(source.getAddressMatchingStatusCode());
			if(source.getAddressSearchText() != null)
				target.setAddressSearchText(source.getAddressSearchText());
			if(source.getAddressTypeCode() != null)
				target.setAddressTypeCode(source.getAddressTypeCode());
			if(source.getCanadaPostBuildName() != null)
				target.setCanadaPostBuildName(source.getCanadaPostBuildName());
			if(source.getCanadaPostLocnName() != null)
				target.setCanadaPostLocnName(source.getCanadaPostLocnName());
			if(source.getCanadaPostRecordType() != null)
				target.setCanadaPostRecordType(source.getCanadaPostRecordType());
			if(source.getCareOf() != null)
				target.setCareOf(source.getCareOf());
			if(source.getCivicNumber() != null)
				target.setCivicNumber(source.getCivicNumber());
			if(source.getCivicNumberSuffix() != null)
				target.setCivicNumberSuffix(source.getCivicNumberSuffix());
			if(source.getCountryCode() != null)
				target.setCountryCode(source.getCountryCode());
			if(source.getEmailAddressText() != null)
				target.setEmailAddressText(source.getEmailAddressText());
			if(source.getExternalAddressId() != null)
				target.setExternalAddressId(source.getExternalAddressId());
			target.setExternalAddressSourceId(source.getExternalAddressSourceId());
			if(source.getExternalServiceAddressId() != null)
				target.setExternalServiceAddressId(source.getExternalServiceAddressId());
			if(source.getFleetMailOfficeName() != null)
				target.setFleetMailOfficeName(source.getFleetMailOfficeName());
			if(source.getHmcsName() != null)
				target.setHmcsName(source.getHmcsName());

			target.setLastUpdateTimeStamp(source.getLastUpdateTimeStamp());

			if(source.getMailingTypeCode() != null)
				target.setMailingTypeCode(source.getMailingTypeCode());
			target.setMasterSourceId(source.getMasterSourceId());
			if(source.getMunicipalityName() != null)
				target.setMunicipalityName(source.getMunicipalityName());
			if(source.getPostalZipCode() != null)
				target.setPostalZipCode(source.getPostalZipCode());
			if(source.getPostOfficeBoxNumber() != null)
				target.setPostOfficeBoxNumber(source.getPostOfficeBoxNumber());
			if(source.getProvinceStateCode() != null){	
				//added below if block for "NF" provinceStateCode defect fix  ,NF is not excepted provinceStateCode for EAVS validation , EAVS excepted value is NL. 
				if ("NF".equals(source.getProvinceStateCode())) {
					target.setProvinceStateCode("NL");
				} else {
					target.setProvinceStateCode(source.getProvinceStateCode());
				}
			}
			target.setRelateAddressAssignmentId(source.getRelatedAddressAssignmentId());
			if(source.getRenderedAddress() != null)
				target.setRenderedAddress(source.getRenderedAddress());
			if(source.getRuralRouteNumber() != null)
				target.setRuralRouteNumber(source.getRuralRouteNumber());
			if(source.getRuralRouteTypeCode() != null)
				target.setRuralRouteTypeCode(source.getRuralRouteTypeCode());
			if(source.getStationName() != null)
				target.setStationName(source.getStationName());
			if(source.getStationQualifier() != null)
				target.setStationQualifier(source.getStationQualifier());
			if(source.getStationTypeCode() != null)
				target.setStationTypeCode(source.getStationTypeCode());
			if(source.getStreetDirectionCode() != null)
				target.setStreetDirectionCode(source.getStreetDirectionCode());
			if(source.getStreetName() != null)
				target.setStreetName(source.getStreetName());
			if(source.getStreetTypeCode() != null)
				target.setStreetTypeCode(source.getStreetTypeCode());
			if(source.getUnitNumber() != null)
				target.setUnitNumber(source.getUnitNumber());
			if(source.getUnitTypeCode() != null)
				target.setUnitTypeCode(source.getUnitTypeCode());
			if(source.getValidateAddressIndicator() != null)
				target.setValidateAddressIndicator(source.getValidateAddressIndicator());

			return super.performSchemaMapping(source, target);
		}

		/*
		 * (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected EnterpriseAddressInfo performDomainMapping(Address source, EnterpriseAddressInfo target) {
			
			if(source.getAdditionalAddressInformation()!= null)
				target.setAdditionalAddressInformation(source.getAdditionalAddressInformation());
			if(source.getAddrAssgnmtId()!= null)
				target.setAddrAssgnmtId(source.getAddrAssgnmtId());
			if(source.getAddressAssignmentSubTypeCode() != null)
				target.setAddressAssignmentSubTypeCode(source.getAddressAssignmentSubTypeCode());
			if(source.getAddressAssignmentTypeCode() != null)
				target.setAddressAssignmentTypeCode(source.getAddressAssignmentTypeCode());
			if(source.getAddressId()!= null)
				target.setAddressId(source.getAddressId());
			if(source.getAddressMatchingStatusCode() != null)
				target.setAddressMatchingStatusCode(source.getAddressMatchingStatusCode());
			if(source.getAddressSearchText() != null)
				target.setAddressSearchText(source.getAddressSearchText());
			if(source.getAddressTypeCode() != null)
				target.setAddressTypeCode(source.getAddressTypeCode());
			if(source.getCanadaPostBuildName() != null)
				target.setCanadaPostBuildName(source.getCanadaPostBuildName());
			if(source.getCanadaPostLocnName() != null)
				target.setCanadaPostLocnName(source.getCanadaPostLocnName());
			if(source.getCanadaPostRecordType() != null)
				target.setCanadaPostRecordType(source.getCanadaPostRecordType());
			if(source.getCareOf() != null)
				target.setCareOf(source.getCareOf());
			if(source.getCivicNumber() != null)
				target.setCivicNumber(source.getCivicNumber());
			if(source.getCivicNumberSuffix() != null)
				target.setCivicNumberSuffix(source.getCivicNumberSuffix());
			if(source.getCountryCode() != null)
				target.setCountryCode(source.getCountryCode());
			if(source.getEmailAddressText() != null)
				target.setEmailAddressText(source.getEmailAddressText());
			if(source.getExternalAddressId() != null)
				target.setExternalAddressId(source.getExternalAddressId());
			if(source.getExternalAddressSourceId() != null)
				target.setExternalAddressSourceId(source.getExternalAddressSourceId());
			if(source.getExternalServiceAddressId() != null)
				target.setExternalServiceAddressId(source.getExternalServiceAddressId());
			if(source.getFleetMailOfficeName() != null)
				target.setFleetMailOfficeName(source.getFleetMailOfficeName());
			if(source.getHmcsName() != null)
				target.setHmcsName(source.getHmcsName());
			if(source.getLastUpdateTimeStamp() != null)
				target.setLastUpdateTimeStamp(source.getLastUpdateTimeStamp());
			if(source.getMailingTypeCode() != null)
				target.setMailingTypeCode(source.getMailingTypeCode());
			target.setMasterSourceId(source.getMasterSourceId());
			if(source.getMunicipalityName() != null)
				target.setMunicipalityName(source.getMunicipalityName());
			if(source.getPostalZipCode() != null)
				target.setPostalZipCode(source.getPostalZipCode());
			if(source.getPostOfficeBoxNumber() != null)
				target.setPostOfficeBoxNumber(source.getPostOfficeBoxNumber());
			if(source.getProvinceStateCode() != null)
				target.setProvinceStateCode(source.getProvinceStateCode());
			if(source.getRelateAddressAssignmentId() != null)
				target.setRelatedAddressAssignmentId(source.getRelateAddressAssignmentId());
			if(source.getRenderedAddress() != null)
				target.setRenderedAddress(source.getRenderedAddress());
			if(source.getRuralRouteNumber() != null)
				target.setRuralRouteNumber(source.getRuralRouteNumber());
			if(source.getRuralRouteTypeCode() != null)
				target.setRuralRouteTypeCode(source.getRuralRouteTypeCode());
			if(source.getStationName() != null)
				target.setStationName(source.getStationName());
			if(source.getStationQualifier() != null)
				target.setStationQualifier(source.getStationQualifier());
			if(source.getStationTypeCode() != null)
				target.setStationTypeCode(source.getStationTypeCode());
			if(source.getStreetDirectionCode() != null)
				target.setStreetDirectionCode(source.getStreetDirectionCode());
			if(source.getStreetName() != null)
				target.setStreetName(source.getStreetName());
			if(source.getStreetTypeCode() != null)
				target.setStreetTypeCode(source.getStreetTypeCode());
			if(source.getUnitNumber() != null)
				target.setUnitNumber(source.getUnitNumber());
			if(source.getUnitTypeCode() != null)
				target.setUnitTypeCode(source.getUnitTypeCode());
			if(source.getValidateAddressIndicator() != null)
				target.setValidateAddressIndicator(source.getValidateAddressIndicator());
			
			return super.performDomainMapping(source, target);
		}
	}

}