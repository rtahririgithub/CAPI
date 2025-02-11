package com.telus.cmb.jws;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.cmb.jws.mapper.WirelessRatedAirtimeUsageInquiryMapper;
import com.telus.cmb.jws.mapper.WirelessRatedAirtimeUsageInquiryMapper.VoiceUsageSummaryMapper;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.ratedairtimeusageinquirytypes_v1.AirtimeUsageChargeInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.ratedairtimeusageinquirytypes_v1.VoiceUsageSummary;

/**
 * @author Brandon Wen
 *
 */

@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(portName = "WirelessRatedAirtimeUsageInquiryServicePort", 
			serviceName = "WirelessRatedAirtimeUsageInquiryService_v1_0", 
			targetNamespace = "http://telus.com/wsdl/CMO/BillingInquiryMgmt/WirelessRatedAirtimeUsageInquiryService_1", 
			wsdlLocation = "/wsdls/WirelessRatedAirtimeUsageInquiryService_v1_0.wsdl", 
			endpointInterface = "com.telus.cmb.jws.WirelessRatedAirtimeUsageInquiryServicePort")

@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class WirelessRatedAirtimeUsageInquiryService_10 extends BaseService implements WirelessRatedAirtimeUsageInquiryServicePort {

	@Override
	@ServiceBusinessOperation(errorCode="CMB_WRAUIS_0001", errorMessage="Get Airtime Usage Summary error")
	public VoiceUsageSummary getAirtimeUsageSummary(
			final String ban, final String subscriberNumber,
			final String featureCode) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<VoiceUsageSummary>() {

			@Override
			public VoiceUsageSummary doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				VoiceUsageSummaryInfo voiceUsageSummaryInfo = context.getSubscriberLifecycleHelper().retrieveVoiceUsageSummary(Integer.parseInt(ban), subscriberNumber, featureCode);
				
				if (voiceUsageSummaryInfo == null) {
					return new VoiceUsageSummary();
				}
				
				return getVoiceUsageSummaryMapper().mapToSchema(voiceUsageSummaryInfo);
			}
			
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_WRAUIS_0002", errorMessage="Get Unpaid Airtime Usage Charge error")
	public AirtimeUsageChargeInfo getUnpaidAirtimeUsageChargeInfo(final String ban) throws PolicyException,	ServiceException {
		
		return execute(new ServiceInvocationCallback<AirtimeUsageChargeInfo>() {

			@Override
			public AirtimeUsageChargeInfo doInInvocationCallback(
					ServiceInvocationContext context) throws Throwable {
				com.telus.eas.account.info.AirtimeUsageChargeInfo airtimeInfo = getAccountInformationHelper(context).retrieveUnpaidAirtimeUsageChargeInfo(Integer.valueOf(ban));
				return WirelessRatedAirtimeUsageInquiryMapper.AirtimeUsageChargeInfoMapper().mapToSchema(airtimeInfo);
			}
			
		});
	}
	
	private VoiceUsageSummaryMapper getVoiceUsageSummaryMapper() {
		return WirelessRatedAirtimeUsageInquiryMapper.VoiceUsageSummaryMapper.getInstance();
	}

}
