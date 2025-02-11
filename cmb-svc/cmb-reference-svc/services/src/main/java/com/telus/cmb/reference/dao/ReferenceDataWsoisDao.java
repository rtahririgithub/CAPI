package com.telus.cmb.reference.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.WirelessSubscriberOfferInformationServicePort;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.utility.info.OfferPricePlanSetInfo;
import com.telus.eas.utility.info.PricePlanSelectionCriteriaInfo;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelesssubscriberofferinformationservicerequestresponse_v2.GetOfferPricePlanSet;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.BuildInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.NameValuePair;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingStats;
import com.telus.tmi.xmlschema.xsd.product.productoffering.wirelesssubscriberofferinformationtypes_v2.OfferPricePlanSet;

public class ReferenceDataWsoisDao extends SoaBaseSvcClient{
	
	private static final String PING_BUILD_VERSION_KEY = "fw_appVersion";
	
	@Autowired
	private WirelessSubscriberOfferInformationServicePort wirelessSubscriberOfferInformationService;
	
	@Override
	public String ping() throws ApplicationException {
		return execute( new SoaCallback<String>() {
			@Override
			public String doCallback() throws Throwable {				
				Ping parameters = new Ping();
				PingResponse response = wirelessSubscriberOfferInformationService.ping(parameters);
				if (response != null) {
					return getPingVersion(response.getPingStats());					
				}
				return null;
			}
		});
	}

	@Override
	public TestPointResultInfo test() {
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName("Wireless Subscriber Offer Information Service");
		try {
			String pingResult = ping();
			resultInfo.setResultDetail(pingResult);
			resultInfo.setPass(true);
		}catch (Throwable t) {
			resultInfo.setExceptionDetail(t);
			resultInfo.setPass(false);
		}
		
		return resultInfo;	
	}
		
	public OfferPricePlanSetInfo retrieveOfferPricePlanSet(final PricePlanSelectionCriteriaInfo ppCriteriaInfo) throws ApplicationException {
		
		return execute( new SoaCallback<OfferPricePlanSetInfo>() {

			@Override
			public OfferPricePlanSetInfo doCallback() throws Throwable {
				GetOfferPricePlanSet parameters = new GetOfferPricePlanSet();
		
				parameters.setAccountTypeCode(String.valueOf(ppCriteriaInfo.getAccountType()));
				parameters.setAccountSubTypeCode(String.valueOf(ppCriteriaInfo.getAccountSubType()));
				parameters.setProvinceCode(ppCriteriaInfo.getProvinceCode());
				parameters.setProductTypeCode(ppCriteriaInfo.getProductType());
				parameters.setBrandId(String.valueOf(ppCriteriaInfo.getBrandId()));
				parameters.setTermQty(BigInteger.valueOf(ppCriteriaInfo.getTerm()));
				parameters.setEquipmentTypeCode(ppCriteriaInfo.getEquipmentType());
				parameters.setSystemID(ppCriteriaInfo.getOfferCriteria().getSystemId());
				parameters.setOfferID(ppCriteriaInfo.getOfferCriteria().getOfferId());
				parameters.setPerspectiveDate(ppCriteriaInfo.getOfferCriteria().getPerspectiveDate());
				parameters.setPromotionIdList(ppCriteriaInfo.getOfferCriteria().getPromotionIdList());
				parameters.setMscPaidInd(ppCriteriaInfo.getOfferCriteria().isMscPaidIndicator());
				
				OfferPricePlanSet response = wirelessSubscriberOfferInformationService.getOfferPricePlanSet(parameters).getOfferPricePlanSet();
				
				OfferPricePlanSetInfo result = null;

				if (response != null) {
					result = new OfferPricePlanSetInfo();
					// WSOIS may return offer price plan codes with ending spaces, which need to be trimmed.
					result.setEnforcementCode(response.getEnforcementCode());
					
					result.setFetchInMarketPricePlansInd(response.isFetchInMarketPricePlansInd()!=null ? response.isFetchInMarketPricePlansInd() : true);
					result.setInMarketPricePlansOfferInd(response.isInMarketPricePlansOfferInd()!=null ? response.isInMarketPricePlansOfferInd() : true);

					List<String> offerPricePlanCodeList = response.getOfferPricePlanCodeList();
					if (!CollectionUtils.isEmpty(offerPricePlanCodeList)) {
						for (String offerPPCode : offerPricePlanCodeList) {
							result.getOfferPricePlanCodeList().add(offerPPCode.trim());
						}
					}

					List<String> offerPricePlanGroupCodeList = response.getOfferPricePlanGroupCodeList();
					if (!CollectionUtils.isEmpty(offerPricePlanGroupCodeList)) {
						for (String offerPPGroupCode : offerPricePlanGroupCodeList) {
							result.getOfferPricePlanGroupCodeList().add(offerPPGroupCode.trim());
						}
					}

					List<String> offerIncompatiblePricePlanCodeList = response.getOfferIncompatiblePricePlanCodeList();
					if (!CollectionUtils.isEmpty(offerIncompatiblePricePlanCodeList)) {
						for (String offerIncompatiblePPCode : offerIncompatiblePricePlanCodeList) {
							result.getOfferIncompatiblePricePlanCodeList().add(offerIncompatiblePPCode.trim());
						}
					}

				}
				return result;
			}
		});
	}
	
	private String getPingVersion(PingStats pingStats) {
		if (pingStats == null) {
			return null;
		}
		String version = pingStats.getServiceName();
		version = (version == null) ? "" : version;
		BuildInfo buildInfo = pingStats.getBuildInfo();
		if (buildInfo != null) {
			List<NameValuePair> properties = buildInfo.getBuildProperty();
			if (properties != null && properties.isEmpty() == false) {
				for (NameValuePair property : properties) {
					if (PING_BUILD_VERSION_KEY.equals(property.getName())) {
						version += " " + property.getValue();
					}
				}
			}
		}
		return version;
	}

}
