package com.telus.cmb.reference.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.OnlineChargingSubscriberAccountServicePort;
import com.telus.eas.utility.info.SapccOfferInfo;
import com.telus.tmi.xmlschema.srv.cmo.billinginquirymgmt.onlinechargingsubscriberaccountservicerequestresponse_v3.GetReferenceDataList;
import com.telus.tmi.xmlschema.srv.cmo.billinginquirymgmt.onlinechargingsubscriberaccountservicerequestresponse_v3.GetReferenceDataListResponse;
import com.telus.tmi.xmlschema.srv.cmo.billinginquirymgmt.onlinechargingsubscriberaccountservicerequestresponse_v3.ParameterExtension;
import com.telus.tmi.xmlschema.srv.cmo.billinginquirymgmt.onlinechargingsubscriberaccountservicerequestresponse_v3.TranslationTableRow;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse;

public class ReferenceDataOcssaDao extends SoaBaseSvcClient {
	
	// SAPCC offer constants
	private static final String SAPCC_OFFER_DEF_TABLE = "PO_BAU_OFFER_DEF";
	private static final String SAPCC_OFFER_ID_KEY = "OFFER_ID";
	private static final String SAPCC_OFFER_NAME_KEY = "OFFER_NAME";
	private static final String SAPCC_RECURRENCE_TYPE_KEY = "RECURRENCE_TYPE";
	private static final String SAPCC_ZONE_KEY = "ALLOW_NOTIFICATION_OVERRIDE";
	private static final String SAPCC_ZONE_DOMESTIC = "CAN";
	private static final String SAPCC_ZONE_INTERNATIONAL = "INT";
	
	@Autowired
	private OnlineChargingSubscriberAccountServicePort ocssaPort;

	public List<SapccOfferInfo> retrieveWCCSapccOfferList() throws ApplicationException {
				
		List<SapccOfferInfo> list = new ArrayList<SapccOfferInfo>();
		GetReferenceDataListResponse response = getReferenceDataList(SAPCC_OFFER_DEF_TABLE);
		if (response != null) {
			// The OCSSA.getReferenceDataList service returns table contents as a list of rows containing a list of columns, with each 'column' containing
			// parameterized data. We iterate through the list of rows first and then loop through the list of columns to find the column data we need.
			for	(TranslationTableRow row : response.getTranslationTableRowList()) {
				SapccOfferInfo info = new SapccOfferInfo();
				for (ParameterExtension param : row.getTranslationTableColumnList()) {					
					if (StringUtils.equalsIgnoreCase(param.getKey(), SAPCC_OFFER_ID_KEY)) {
						info.setOfferId(param.getValue());
					}
					if (StringUtils.equalsIgnoreCase(param.getKey(), SAPCC_OFFER_NAME_KEY)) {
						info.setOfferName(param.getValue());
					}
					if (StringUtils.equalsIgnoreCase(param.getKey(), SAPCC_RECURRENCE_TYPE_KEY)) {
						info.setRecurrenceType(param.getValue());
					}
					if (StringUtils.equalsIgnoreCase(param.getKey(), SAPCC_ZONE_KEY)) {
						info.setZone(StringUtils.equalsIgnoreCase(SAPCC_ZONE_DOMESTIC, param.getValue()) ? SapccOfferInfo.ZONE_DOMESTIC
								: StringUtils.equalsIgnoreCase(SAPCC_ZONE_INTERNATIONAL, param.getValue()) ? SapccOfferInfo.ZONE_INTERNATIONAL : StringUtils.EMPTY);
					}
				}
				// WCC offers must have a valid offer ID (duh) and a valid zone
				if (StringUtils.isNotBlank(info.getOfferId()) && StringUtils.isNotBlank(info.getZone())) {
					list.add(info);
				}
			}
		}

		return list;
	}
	
	private GetReferenceDataListResponse getReferenceDataList(final String translationTableName) throws ApplicationException {

		return execute(new SoaCallback<GetReferenceDataListResponse>() {

			@Override
			public GetReferenceDataListResponse doCallback() throws Throwable {

				GetReferenceDataList request = new GetReferenceDataList();
				request.setTranslationTableName(translationTableName);
				return ocssaPort.getReferenceDataList(request);
			}

		});
	}

	@Override
	public String ping() throws ApplicationException {
		
		return execute(new SoaCallback<String>() {

			public String doCallback() throws Exception {
				PingResponse pingResponse = ocssaPort.ping(new Ping());
				return ToStringBuilder.reflectionToString(pingResponse.getPingStats());
			}
			
		});
	}

}