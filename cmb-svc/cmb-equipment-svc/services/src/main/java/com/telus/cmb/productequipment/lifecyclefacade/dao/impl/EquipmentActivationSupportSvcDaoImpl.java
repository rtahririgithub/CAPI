package com.telus.cmb.productequipment.lifecyclefacade.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.productequipment.lifecyclefacade.dao.EquipmentActivationSupportSvcDao;
import com.telus.cmb.wsclient.EquipmentActivationSupportServicePort;
import com.telus.cmb.wsclient.PolicyException_v1;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentactivationsupportservicerequestresponsetypes_v1.ReserveSimProfile;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentactivationsupportservicerequestresponsetypes_v1.ReserveSimProfileResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

public class EquipmentActivationSupportSvcDaoImpl extends SoaBaseSvcClient implements EquipmentActivationSupportSvcDao {
	
	private static final Logger LOGGER = Logger.getLogger(EquipmentActivationSupportSvcDaoImpl.class);
	
	@Autowired
	private EquipmentActivationSupportServicePort port;
	
	/**
	 * For consumer tracker, we use the IMEI and simProfileCd only.
	 */
	@Override
	public ReserveSimProfileResponse reserveSimProfile(final String imei, final String simProfileCd, final String embeddedIccId) throws ApplicationException {

		if (StringUtils.isBlank(embeddedIccId) && ((StringUtils.isBlank(imei) || StringUtils.isBlank(simProfileCd)))) {
			String errorMsg = "Both IMEI and simProfileCd are required, imei = [" + imei + "], simProfileCd = [" + simProfileCd + "]";
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, errorMsg, StringUtils.EMPTY);
		}

		return execute( new SoaCallback<ReserveSimProfileResponse>() {
			
			@Override
			public ReserveSimProfileResponse doCallback() throws Throwable {
				
				ReserveSimProfile req = new ReserveSimProfile();
				ReserveSimProfileResponse res = null;
				String errorMsg = "Failed to call EquipmentActivationSupportService operation reserveSimProfile. imei: [" + imei + "], simProfileCd: [" + simProfileCd + "], ";
				
				if (!StringUtils.isBlank(imei)) {
					req.setImei(imei);
					req.setESimProfileProductCode(simProfileCd);
				} else {
					req.setEmbeddedIccId(embeddedIccId);
				}
				
				try {
					res = port.reserveSimProfile(req);
				} catch (PolicyException_v1 pex) {
					errorMsg += "error code: " + pex.getFaultInfo().getErrorCode() + ", error message: " + pex.getFaultInfo().getErrorMessage();
					LOGGER.error(errorMsg);
					if (pex.getFaultInfo() != null && "2011".equals(pex.getFaultInfo().getErrorCode())) {
						throw new ApplicationException(SystemCodes.CMB_SLF_DAO, ErrorCodes.ESIM_ERROR_EQUIPMENT_SN_IN_USE, errorMsg, StringUtils.EMPTY, pex);
					} else {
						throw new ApplicationException(SystemCodes.CMB_SLF_DAO, ErrorCodes.ESIM_ERROR_RESERVE_SIM_PROFILE, errorMsg, StringUtils.EMPTY, pex);
					}
				} catch (Exception e) {
					errorMsg += "error message: " + e.getMessage();
					LOGGER.error(errorMsg);
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ESIM_ERROR_RESERVE_SIM_PROFILE, errorMsg, StringUtils.EMPTY, e);
				}
				return res;
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient#ping()
	 */
	@Override
	public String ping() throws ApplicationException {
		return execute( new SoaCallback<String>() {
			
			@Override
			public String doCallback() throws Throwable {
				return port.ping(new Ping()).getVersion();
			}
		});
	}
}
