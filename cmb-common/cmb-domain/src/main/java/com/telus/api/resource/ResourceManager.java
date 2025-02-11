package com.telus.api.resource;

import com.telus.api.TelusAPIException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface ResourceManager {

  /**
   * Returns PhoneNumberResource object. It's a remote call.
   * @param phoneNumber String
   * @throws UnknownPhoneNumberResourceException
   * @throws TelusAPIException
   * @return PhoneNumberResource
   */
  PhoneNumberResource getPhoneNumberResource(String phoneNumber) throws UnknownPhoneNumberResourceException, TelusAPIException;
}
