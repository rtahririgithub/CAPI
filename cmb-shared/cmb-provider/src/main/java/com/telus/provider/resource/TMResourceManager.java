package com.telus.provider.resource;

import com.telus.api.TelusAPIException;
import com.telus.api.resource.PhoneNumberResource;
import com.telus.api.resource.ResourceManager;
import com.telus.api.resource.UnknownPhoneNumberResourceException;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TMResourceManager extends BaseProvider implements ResourceManager {

	public TMResourceManager(TMProvider provider) {
		super(provider);
	}

	public PhoneNumberResource getPhoneNumberResource(String phoneNumber) throws UnknownPhoneNumberResourceException, TelusAPIException {
		PhoneNumberResource info = null;

		try {
			info = provider.getReferenceDataHelperEJB().retrievePhoneNumberResource(phoneNumber);
		} catch (UnknownPhoneNumberResourceException e) {
			throw e;
		} catch (Throwable t) {
			Logger.debug(t);
			throw new TelusAPIException(t);
		}

		return info;
	}

}
