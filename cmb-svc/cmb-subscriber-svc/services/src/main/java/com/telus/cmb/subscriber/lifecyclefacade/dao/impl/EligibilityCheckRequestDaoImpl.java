package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.reference.Brand;
import com.telus.cmb.framework.resource.ResourceExecutionCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.EligibilityCheckRequestDao;
import com.telus.cmb.wsclient.wlnp.chels.CheckPortInEligibility;
import com.telus.cmb.wsclient.wlnp.chels.EligibilityCheckPort;
import com.telus.cmb.wsclient.wlnp.chels.EligibilityRequestData;
import com.telus.cmb.wsclient.wlnp.chels.EligibilityResponseData;
import com.telus.cmb.wsclient.wlnp.chels.PRMFault;
import com.telus.cmb.wsclient.wlnp.chels.PRMFaultDetail;
import com.telus.cmb.wsclient.wlnp.chels.PRMServiceException;
import com.telus.cmb.wsclient.wlnp.chels.TypesProductType;
import com.telus.eas.portability.info.PortInEligibilityInfo;

public class EligibilityCheckRequestDaoImpl extends WnpLegacyClient implements EligibilityCheckRequestDao {

	private static final String PRM_TRUE = "Y";

	@Autowired
	private EligibilityCheckPort port;

	private static final Map<String, TypesProductType> ELIGIBILITY_TYPE_MAP = new HashMap<String, TypesProductType>();

	static {

		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_EXTERNAL_2M, TypesProductType.EXTR_2_M);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_EXTERNAL_2P, TypesProductType.EXTR_2_P);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_INTERNAL_M2P, TypesProductType.INTR_M_2_P);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_INTERNAL_P2M, TypesProductType.INTR_P_2_M);

		// backward compatible
		ELIGIBILITY_TYPE_MAP.put("EXT", TypesProductType.EXTR_2_P);
		ELIGIBILITY_TYPE_MAP.put("INT", TypesProductType.INTR_M_2_M);

		// New constants defined at Holborn time
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_EXTERNAL_2C, TypesProductType.EXT_2_C);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_EXTERNAL_2H, TypesProductType.EXT_2_H);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_EXTERNAL_2I, TypesProductType.EXT_2_I);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_INTERNAL_2C, TypesProductType.INT_2_C);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_INTERNAL_2H, TypesProductType.INT_2_H);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_INTERNAL_2I, TypesProductType.INT_2_I);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_M2P_I2C, TypesProductType.M_2_P_I_2_C);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_M2P_I2H, TypesProductType.M_2_P_I_2_H);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_P2M_C2I, TypesProductType.P_2_M_C_2_I);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_P2M_H2I, TypesProductType.P_2_M_H_2_I);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_P2P_2C, TypesProductType.P_2_P_2_C);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_P2P_2H, TypesProductType.P_2_P_2_H);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_SWAP_2C, TypesProductType.SWAP_2_C);
		ELIGIBILITY_TYPE_MAP.put(PortInEligibility.PORT_VISIBILITY_TYPE_SWAP_2H, TypesProductType.SWAP_2_H);
	}

	@Override
	public PortInEligibilityInfo checkPortInEligibility(final String phoneNumber, final String portVisibility, final int incomingBrand) throws ApplicationException {

		return execute( new ResourceExecutionCallback<PortInEligibilityInfo>() {
			
			@Override
			public PortInEligibilityInfo doInCallback() throws Exception {

				PortInEligibilityInfo portInEligibility = null;

				EligibilityRequestData requestData = new EligibilityRequestData();
				TypesProductType productType = (TypesProductType) ELIGIBILITY_TYPE_MAP.get(portVisibility);
				if (productType == null)
					productType = TypesProductType.fromValue(portVisibility);
				requestData.setProductType(productType);
				requestData.setPhoneNumber(phoneNumber);
				requestData.setTargetBrand(String.valueOf(incomingBrand));

				// call service

				CheckPortInEligibility request = new CheckPortInEligibility();
				request.setEligibilityRequestData(requestData);

				try {

					EligibilityResponseData responseData = port.checkPortInEligibility(request).getEligibilityResponseData();
					
					if (responseData.getEligible().equals(PRM_TRUE)) {
						portInEligibility = new PortInEligibilityInfo();
						portInEligibility.setCurrentServiceProvider(responseData.getOSP());
						portInEligibility.setIdenCoverage(responseData.getMIKECoverage().equals(PRM_TRUE));
						portInEligibility.setCDMACoverage(responseData.getCDMACoverage().equals(PRM_TRUE));
						portInEligibility.setCDMAPostpaidCoverage(responseData.getCDMAPostpaidCoverage().equals(PRM_TRUE));
						portInEligibility.setCDMAPrepaidCoverage(responseData.getCDMAPrepaidCoverage().equals(PRM_TRUE));
						portInEligibility.setHSPACoverage(responseData.getHSPACoverage().equals(PRM_TRUE));
						portInEligibility.setHSPAPostpaidCoverage(responseData.getHSPAPostpaidCoverage().equals(PRM_TRUE));
						portInEligibility.setHSPAPrepaidCoverage(responseData.getHSPAPrepaidCoverage().equals(PRM_TRUE));
						portInEligibility.setPortDirectionIndicator(responseData.getNPDirectionIndicator().value());
						portInEligibility.setPortVisibility(portVisibility);
						portInEligibility.setOutgoingBrandId(responseData.getCurrentBrand() != null ? Integer.parseInt(responseData.getCurrentBrand()) : Brand.BRAND_ID_NOT_APPLICABLE);
						portInEligibility.setIncomingBrandId(incomingBrand);
						portInEligibility.setPhoneNumber(phoneNumber);
						if (responseData.getPlatform() != null)
							portInEligibility.setPlatformId(Integer.parseInt(responseData.getPlatform()));
					} else {
						throw new ApplicationException(SystemCodes.CMB_SLF_DAO, responseData.getResponseCode(), "PRM_FALSE", "");
					}
					
				} catch (PRMServiceException prmServiceException) {
					PRMFaultDetail faultDetail = prmServiceException.getFaultInfo();
					if (faultDetail != null) {
						PRMFault fault = faultDetail.getError();
						if (fault != null) {
							throw new ApplicationException("WNP:ELIGB-CHKS", fault.getCode(), fault.getReason(), fault.getReason(), prmServiceException);
						}
					}
				}
				return portInEligibility;
			}
		}, "0001", "SUBS-SVC", "ELIGB-CHKS", "WNP");
	}

}
