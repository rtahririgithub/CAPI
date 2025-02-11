package com.telus.cmb.reference.dao;

import java.util.Date;

import amdocs.APILink.datatypes.ServiceVoiceAllocationInfo;
import amdocs.APILink.datatypes.VoiceAllocationInfo;
import amdocs.APILink.sessions.interfaces.GenericServices;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.dao.util.VoiceAirTimeMapping;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;

public class ReferenceDataAmdocsDao extends AmdocsDaoSupport {
	
	

	public ServiceAirTimeAllocationInfo retrieveVoiceAllocation( final String soc, final Date effectiveDate, String sessionId) throws ApplicationException {
		
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<ServiceAirTimeAllocationInfo>()  {

			@Override
			public ServiceAirTimeAllocationInfo doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				
				GenericServices genericeServcies = transactionContext.createBean(GenericServices.class);
				
				VoiceAllocationInfo voiceAllocation = genericeServcies.getVoiceAllocationInfoBySoc( soc, effectiveDate );
				ServiceVoiceAllocationInfo[] serviceVoiceAllocationInfos = voiceAllocation.serviceVoiceAllocationInfo;
				if (serviceVoiceAllocationInfos!=null && serviceVoiceAllocationInfos.length>0) {
					return VoiceAirTimeMapping.mapServiceVoiceAllocation( serviceVoiceAllocationInfos[0] );
				} else {
					return null;
				}
			}
		});
	}
	
	

}
