package com.telus.cmb.common.mapping;

import com.telus.eas.utility.info.MultilingualCodeDescriptionInfo;
import com.telus.eas.utility.info.MultilingualTextInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Description;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.MultilingualCodeDescriptionList;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Name;

/**
 * EnterpriseCommonTypesV9Mapper
 * @author R. Fong
 *
 */
public class EnterpriseCommonTypesV9Mapper {
	
	public static DescriptionMapper DescriptionMapper() {
		return DescriptionMapper.getInstance();
	}
	
	public static class DescriptionMapper extends AbstractSchemaMapper<Description, MultilingualTextInfo> {

		private static DescriptionMapper INSTANCE;
		
		private DescriptionMapper() {
			super(Description.class, MultilingualTextInfo.class);
		}

		protected static synchronized DescriptionMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new DescriptionMapper();
			}
			return INSTANCE;
		}

		@Override
		protected MultilingualTextInfo performDomainMapping(Description source, MultilingualTextInfo target) {
			
			target.setLocale(source.getLocale());
			target.setText(source.getDescriptionText());
	
			return super.performDomainMapping(source, target);
		}
	}
	
	public static NameMapper NameMapper() {
		return NameMapper.getInstance();
	}
	
	public static class NameMapper extends AbstractSchemaMapper<Name, MultilingualTextInfo> {

		private static NameMapper INSTANCE;
		
		private NameMapper() {
			super(Name.class, MultilingualTextInfo.class);
		}

		protected static synchronized NameMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new NameMapper();
			}
			return INSTANCE;
		}

		@Override
		protected MultilingualTextInfo performDomainMapping(Name source, MultilingualTextInfo target) {
			
			target.setLocale(source.getLocale());
			target.setText(source.getName());
	
			return super.performDomainMapping(source, target);
		}
	}
	
	public static CodeDescriptionMapper CodeDescriptionMapper() {
		return CodeDescriptionMapper.getInstance();
	}
	
	public static class CodeDescriptionMapper extends AbstractSchemaMapper<MultilingualCodeDescriptionList, MultilingualCodeDescriptionInfo> {

		private static CodeDescriptionMapper INSTANCE;
		
		private CodeDescriptionMapper() {
			super(MultilingualCodeDescriptionList.class, MultilingualCodeDescriptionInfo.class);
		}

		protected static synchronized CodeDescriptionMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CodeDescriptionMapper();
			}
			return INSTANCE;
		}

		@Override
		protected MultilingualCodeDescriptionInfo performDomainMapping(MultilingualCodeDescriptionList source, MultilingualCodeDescriptionInfo target) {
			
			target.setCode(source.getCode());
			target.setDescriptionList(DescriptionMapper().mapToDomain(source.getDescription()));
	
			return super.performDomainMapping(source, target);
		}
	}

}