package com.telus.cmb.jws.mapping.subscriber.information_types_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.MemberIdentityInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v1.FleetIdentity;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v1.MemberIdentity;

public class MemberIdentityMapper extends AbstractSchemaMapper<MemberIdentity, MemberIdentityInfo> {
	private static MemberIdentityMapper INSTANCE = null;

	private MemberIdentityMapper() {
		super (MemberIdentity.class, MemberIdentityInfo.class);
	}
	
	public static synchronized MemberIdentityMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MemberIdentityMapper();
		}
		
		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected MemberIdentity performSchemaMapping(MemberIdentityInfo source, MemberIdentity target) {
		target.setFleetIdentity(new FleetIdentityMapper().mapToSchema(source.getFleetIdentity0()));
		target.setMemberId(source.getMemberId());
		target.setResourceStatus(source.getResourceStatus());
		return super.performSchemaMapping(source, target);
	}
	
	
	private class FleetIdentityMapper extends AbstractSchemaMapper<FleetIdentity, FleetIdentityInfo> {
		public FleetIdentityMapper() {
			super (FleetIdentity.class, FleetIdentityInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected FleetIdentity performSchemaMapping(FleetIdentityInfo source, FleetIdentity target) {
			target.setFleetId(source.getFleetId());
			target.setUrbanId(source.getUrbanId());
			return super.performSchemaMapping(source, target);
		}
		
		
	}
	
}
