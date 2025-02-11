/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.subscriber.lifecyclefacade.dao;

import java.util.Collection;

import com.telus.api.ApplicationException;
import com.telus.api.portability.PRMReferenceData;
import com.telus.api.portability.PortRequestSummary;
import com.telus.api.reference.Province;
import com.telus.eas.portability.info.PortRequestInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public interface PortRequestInformationDao {

	Collection<PRMReferenceData> getReferenceData(String category) throws ApplicationException;
	
	Collection<PortRequestInfo> getCurrentPortRequestsByPhoneNumber(String phoneNumber, int brandId, Province[] provinces) throws ApplicationException;

	Collection<PortRequestInfo> getCurrentPortRequestsByBan(int banNumber,  Province[] provinces) throws ApplicationException;
	
	PortRequestSummary getPortRequestStatus(String phoneNumber, int brandId) throws ApplicationException;
	
	boolean isValidPortInRequest(PortRequestInfo portRequest, String applicationId, String user, Province[] provinces) throws ApplicationException;
	
}
