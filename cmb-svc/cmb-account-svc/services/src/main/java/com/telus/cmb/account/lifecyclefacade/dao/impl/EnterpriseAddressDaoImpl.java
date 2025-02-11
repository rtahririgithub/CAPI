/**
 * 
 */
package com.telus.cmb.account.lifecyclefacade.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclefacade.dao.EnterpriseAddressDao;
import com.telus.cmb.account.mapping.EnterpriseAddressValidationServiceMapper;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.EnterpriseAddressValidationServicePort;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationMessagesInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.account.info.AddressValidationResultInfo.VerificationResultStateInfo;
import com.telus.eas.account.info.EnterpriseAddressInfo;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.enterpriseaddressvalidationservicerequestresponse_v1.AddressLayoutType;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.enterpriseaddressvalidationservicerequestresponse_v1.VerificationErrorType;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.enterpriseaddressvalidationservicerequestresponse_v1.VerificationModeType;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.enterpriseaddressvalidationservicerequestresponse_v1.VerificationResultStateType;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.enterpriseaddressvalidationservicerequestresponse_v1.VerificationResultType;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.enterpriseaddressvalidationservicerequestresponse_v1.VerifyCanadianPostalAddress;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customercommon_v3.Address;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

/**
 * @author tongts
 *
 */
public class EnterpriseAddressDaoImpl extends SoaBaseSvcClient implements EnterpriseAddressDao {

	private static final Log logger = LogFactory.getLog(EnterpriseAddressDaoImpl.class);
	
	@Autowired
	private EnterpriseAddressValidationServicePort addressValidationService;

	@Override
	public AddressValidationResultInfo validateAddress(final AddressInfo addressInfo) throws ApplicationException {
		
		return execute( new SoaCallback<AddressValidationResultInfo>() {
			
			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public AddressValidationResultInfo doCallback() throws Exception {

				VerifyCanadianPostalAddress request = new VerifyCanadianPostalAddress();
				
				AddressValidationResultInfo addressValidationResultInfo;
				List<AddressInfo> translatedMatchingAddresses = null;
				EnterpriseAddressInfo enterpriseAddressInfo = new EnterpriseAddressInfo(addressInfo);

				Address addressToVerify = EnterpriseAddressValidationServiceMapper.EnterpriseAddressMapper().mapToSchema(enterpriseAddressInfo);
				
				request.setAddressToVerify(addressToVerify);
				request.setInputAddressLayout(AddressLayoutType.ADDR_ELEMENT_LAYOUT);
				request.setOutputAddressLayout(AddressLayoutType.ADDR_ELEMENT_LAYOUT);
				request.setVerificationMode(VerificationModeType.VERBOSE);

				VerificationResultType verificationResult = addressValidationService.verifyCanadianPostalAddress(request).getVerificationResult();

				List<Address> matchingAddresses = verificationResult.getMatchingAddresses();

				List<EnterpriseAddressInfo> enterpriseAddresses;
				if(!matchingAddresses.isEmpty()){
					enterpriseAddresses = EnterpriseAddressValidationServiceMapper.
					EnterpriseAddressMapper().mapToDomain(matchingAddresses);

					logger.debug("ValidateAddress(): matching addresses array length.....: "+matchingAddresses.size());
					translatedMatchingAddresses = new ArrayList<AddressInfo>();
					for(EnterpriseAddressInfo enterpriseAddress:enterpriseAddresses){
						AddressInfo address = new AddressInfo();
						address.translateAddress(enterpriseAddress);
						translatedMatchingAddresses.add(address);
					}
				}
				addressValidationResultInfo = new AddressValidationResultInfo();
				if(translatedMatchingAddresses != null && translatedMatchingAddresses.size()>0){
					addressValidationResultInfo.setVerifiedAddress(translatedMatchingAddresses.get(0));
					addressValidationResultInfo.setMatchingAddressList(translatedMatchingAddresses);
				}
				addressValidationResultInfo.setnCodeReturnStatus(Integer.toString(verificationResult.getNCodeRetSts()));
				addressValidationResultInfo.setValidAddressInd(verificationResult.isValidAddressInd());

				List<VerificationResultStateInfo> verificationResultStateInfoList = new ArrayList<VerificationResultStateInfo>();
				List<VerificationResultStateType> verificationResultState = verificationResult.getVerificationResultStates();
				if(verificationResultState != null){
					for(VerificationResultStateType vrs:verificationResultState){
						AddressValidationResultInfo avri = new AddressValidationResultInfo();
						AddressValidationResultInfo.VerificationResultStateInfo vrsi=avri.new VerificationResultStateInfo();
						vrsi.setDescription(vrs.getDesc());
						vrsi.setState(vrs.getState());
						verificationResultStateInfoList.add(vrsi);
					}
					addressValidationResultInfo.setVerificationResultStateList(verificationResultStateInfoList);
				}

				List<AddressValidationMessagesInfo> addressValidationMessagesInfoList = new ArrayList<AddressValidationMessagesInfo>();
				List<VerificationErrorType> verificationError = verificationResult.getVerificationErrors();
				if(verificationError != null){
					logger.debug("ValidateAddress(): validation error array length.....: "+verificationError.size());
					for(VerificationErrorType ve:verificationError){
						AddressValidationMessagesInfo avmi = new AddressValidationMessagesInfo();
						avmi.setCode(ve.getErrCode());
						avmi.setMessage(ve.getErrDesc());
						addressValidationMessagesInfoList.add(avmi);
					}
					addressValidationResultInfo.setValidationMessages(addressValidationMessagesInfoList.
							toArray(new AddressValidationMessagesInfo[addressValidationMessagesInfoList.size()]));
				}

				return addressValidationResultInfo;
			}
		});
	}
	
	@Override
	public String ping() throws ApplicationException {
		return execute( new SoaCallback<String>() {
			
			@Override
			public String doCallback() throws Exception {
				return addressValidationService.ping( new Ping()).getVersion();
			}
		});
	}
}
