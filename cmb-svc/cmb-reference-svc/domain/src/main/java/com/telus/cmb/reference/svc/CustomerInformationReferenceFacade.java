/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc;

import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.AccountTypeInfo;
import com.telus.eas.utility.info.MemoTypeInfo;
import com.telus.eas.utility.info.SegmentationInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public interface CustomerInformationReferenceFacade {

	AccountTypeInfo getAccountType(String accountTypeCode, int accountTypeBrandId) throws TelusException;

	AccountTypeInfo[] getAccountTypes() throws TelusException;

	MemoTypeInfo getMemoType(String memoTypeCode) throws TelusException;

	MemoTypeInfo[] getMemoTypes() throws TelusException;

	SegmentationInfo getSegmentation(int brandId, String accountTypeCode, String provinceCode) throws TelusException;
	
	SegmentationInfo[] getSegmentations() throws TelusException;	
	
}
