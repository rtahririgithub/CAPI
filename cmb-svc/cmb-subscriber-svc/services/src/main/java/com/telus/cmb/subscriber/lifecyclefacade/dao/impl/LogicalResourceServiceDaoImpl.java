package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.LogicalResourceServiceDao;
import com.telus.cmb.wsclient.LogicalResourceServicePort;
import com.telus.eas.framework.exception.TelusException;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.logicalresourceservicerequestresponse_v6.ChangeIMSI;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.logicalresourceservicerequestresponse_v6.SetIMSIStatus;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.logicalresourceservicerequestresponse_v6.SetTNStatus;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.resource.resource.commonlogicalresourcetypes_v6.NetworkType;
import com.telus.tmi.xmlschema.xsd.resource.resource.commonlogicalresourcetypes_v6.TelephoneNumber;
import com.telus.tmi.xmlschema.xsd.resource.resource.commonlogicalresourcetypes_v6.TnProvisioningStatusType;

public class LogicalResourceServiceDaoImpl extends SoaBaseSvcClient implements LogicalResourceServiceDao {

	@Autowired
	private LogicalResourceServicePort port;

	@Override
	public void setIMSIStatus(final String networkType, final String localIMSI, final String remoteIMSI, final String status) throws ApplicationException {

		execute(new SoaCallback<Object>() {

			@Override
			public Object doCallback() throws Throwable {

				if (networkType == null || networkType.trim().equals("") || localIMSI == null || localIMSI.trim().equals("") || status == null || status.trim().equals("")) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, "Missing or invalid input parameter !", "");
				}

				SetIMSIStatus request = new SetIMSIStatus();

				request.setLocalIMSI(localIMSI);
				request.setRemoteIMSI(remoteIMSI);
				request.setProvisioningStatus(TnProvisioningStatusType.fromValue(status));

				port.setIMSIStatus(request);

				return null;
			}
		});
	}

	@Override
	public void setTNStatus(final String phoneNumber, final String networkType, final String status) throws ApplicationException {

		execute(new SoaCallback<Object>() {

			@Override
			public Object doCallback() throws Throwable {
				if (phoneNumber == null || phoneNumber.trim().equals("") || networkType == null || networkType.trim().equals("") || status == null || status.trim().equals("")) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, "Missing or invalid input parameter !", "");
				}

				SetTNStatus request = new SetTNStatus();

				request.setTelephoneNumber(getTelephoneNumberType(phoneNumber));
				request.setNetworkType(translateNetworkType(networkType));
				request.setProvisioningStatus(TnProvisioningStatusType.fromValue(status));

				port.setTNStatus(request);

				return null;
			}
		});
	}

	@Override
	public void changeIMSIs(final String phoneNumber, final String networkType, final String newLocalIMSI, final String newRemoteIMSI) throws ApplicationException {

		execute(new SoaCallback<Object>() {

			@Override
			public Object doCallback() throws Throwable {
				if (phoneNumber == null || phoneNumber.trim().equals("") || networkType == null || networkType.trim().equals("") || newLocalIMSI == null || newLocalIMSI.trim().equals("")) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, "Missing or invalid input parameter !", "");
				}

				ChangeIMSI request = new ChangeIMSI();

				request.setTelephoneNumber(getTelephoneNumberType(phoneNumber));
				request.setNetworkType(translateNetworkType(networkType));
				request.setLocalIMSI(newLocalIMSI);
				request.setRemoteIMSI(newRemoteIMSI);

				port.changeIMSI(request);

				return null;
			}
		});
	}

	private NetworkType translateNetworkType(String networkType) throws TelusException {

		NetworkType netType = null;
		if (networkType.equals(com.telus.api.reference.NetworkType.NETWORK_TYPE_ALL)) {
			throw new TelusException("Unsupported NETWORK TYPE : ALL"); // not
																		// supported
																		// by
																		// RCM ,
																		// only
																		// by KB
		} else if (networkType.equals(com.telus.api.reference.NetworkType.NETWORK_TYPE_CDMA)) {
			netType = NetworkType.CDMA;
		} else if (networkType.equals(com.telus.api.reference.NetworkType.NETWORK_TYPE_HSPA)) {
			netType = NetworkType.HSPA;
		} else if (networkType.equals(com.telus.api.reference.NetworkType.NETWORK_TYPE_IDEN)) {
			netType = NetworkType.IDEN;
		}

		return netType;
	}

	private TelephoneNumber getTelephoneNumberType(String phoneNumber) {

		TelephoneNumber tnType = new TelephoneNumber();
		tnType.setNpa(phoneNumber.substring(0, 3));
		tnType.setNxx(phoneNumber.substring(3, 6));
		tnType.setLineNumber(phoneNumber.substring(6));

		return tnType;
	}

	@Override
	public String ping() throws ApplicationException {
		return execute(new SoaCallback<String>() {

			@Override
			public String doCallback() throws Throwable {
				return port.ping(new Ping()).getVersion();
			}
		});
	}

}
