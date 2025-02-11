package com.telus.cmb.jws.mapping.subscriber.information_types_30;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriptionRole;

public class SubscriptionRoleMapper extends AbstractSchemaMapper<SubscriptionRole, SubscriptionRoleInfo> {
	private static SubscriptionRoleMapper INSTANCE = null;
	
	private SubscriptionRoleMapper() {
		super (SubscriptionRole.class, SubscriptionRoleInfo.class);
	}
	
	public static synchronized SubscriptionRoleMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SubscriptionRoleMapper();
		}
		
		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected SubscriptionRole performSchemaMapping(SubscriptionRoleInfo source, SubscriptionRole target) {
		if (source != null) {
			target.setCode(source.getCode());
			target.setCustomerServiceRepresentativeId(source.getCsrId());
			target.setDealerCode(source.getDealerCode());
			target.setModifiedInd(source.isModified());
			target.setSalesRepCode(source.getSalesRepCode());
		}
		return super.performSchemaMapping(source, target);
	}

	
}
