package com.telus.provider.dealer;

/**
 * 
 * Title: Telus Domain Project -KB61
 * 
 * Description:
 * 
 * Copyright: Copyright (c) 2002
 * 
 * Company:
 * 
 * @author @version 1.0
 *  
 */

import com.telus.api.*;
import com.telus.api.dealer.*;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.provider.*;
import com.telus.provider.util.ProviderDealerExceptionTranslator;
import com.telus.eas.equipment.info.CPMSDealerInfo;

public class TMCPMSDealer extends BaseProvider implements CPMSDealer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final CPMSDealer delegate;

	public TMCPMSDealer(TMProvider provider, CPMSDealerInfo delegate) {
		super(provider);
		this.delegate = delegate;
	}

	public void validUser(String password) throws InvalidPasswordException,
			TelusAPIException {
		try {
			provider.getDealerManagerEJB().validUser(delegate.getChannelCode(),
				delegate.getUserCode(), password);
		} catch (Throwable e) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderDealerExceptionTranslator(password);
			provider.getExceptionHandler().handleException(e,telusExceptionTranslator);					
		}
	}

	public void resetUserPassword(String newPassword)
			throws InvalidPasswordException, TelusAPIException {

		try {
			provider.getDealerManagerEJB().resetUserPassword(
				delegate.getChannelCode(), delegate.getUserCode(),
				newPassword);
		} catch (Throwable e) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderDealerExceptionTranslator(newPassword);
			provider.getExceptionHandler().handleException(e,telusExceptionTranslator);					
		}
	}

	public void changeUserPassword(String oldPassword, String newPassword)
			throws InvalidPasswordException, TelusAPIException {

		try {
			provider.getDealerManagerEJB().changeUserPassword(
				delegate.getChannelCode(), delegate.getUserCode(),
				oldPassword, newPassword);
		} catch (Throwable e) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderDealerExceptionTranslator(oldPassword);
			provider.getExceptionHandler().handleException(e,telusExceptionTranslator);					
		}
	}

	public String getProvinceCode() {
		return delegate.getProvinceCode();
	}

	public boolean isHighPriority() {
		return delegate.isHighPriority();
	}

	public String getChannelCode() {
		return delegate.getChannelCode();
	}

	public String getChannelDesc() {
		return delegate.getChannelDesc();
	}

	public String getUserCode() {
		return delegate.getUserCode();
	}

	public String getUserDesc() {
		return delegate.getUserDesc();
	}

	public String getChannelOrgTypeCode() {
		return delegate.getChannelOrgTypeCode();
	}

	public String getPhone() {         
		return delegate.getPhone();   
	}   

	public String[] getAddress() {
		return delegate.getAddress();
	}

	public HoursOfOperation[] getHoursOfOperation() {
		return delegate.getHoursOfOperation();
	}
	
	public int[] getBrandIds() {
		return delegate.getBrandIds();
	}

}

