package com.telus.cmb.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;

/**
 * The purpose of this class is to provide a shared reference with EJB and helper classes with
 * the support of on-demand lookup for the EJB only.
 * @author tongts
 *
 */

public class EJBController {
	private static final Log logger = LogFactory.getLog(EJBController.class);
	private Map<String, String> jndiMap = new HashMap<String, String>();
	private Map<String, Object> ejbMap = new HashMap<String, Object>();
	
	public EJBController() {
	
	}
	
	public <T> void setEjb (Class<T> clazz, String jndi, T ejbRef) {
		String clazzName = clazz.getCanonicalName();
		if (jndi != null && jndi.isEmpty() == false) {
			jndiMap.put(clazzName, jndi);
		}
		
		if (ejbRef != null) {
			ejbMap.put(clazzName, ejbRef);
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T> T getEjb (Class<T> clazz) throws ApplicationException {
		String clazzName = clazz.getCanonicalName();
		T ejbRef = (T) ejbMap.get(clazzName);
		
		if (ejbRef == null) {
			String jndiName = jndiMap.get(clazzName);
			
			if (jndiName == null) {
				throw new ApplicationException (SystemCodes.EJBUtil, "JNDI name not initialized for EJB "+clazzName, "");
			}
			ejbRef = EJBUtil.getHelperProxy(clazz, jndiName);
			
			if (ejbRef != null) {
				ejbMap.put(clazzName, ejbRef);
			}
		}else {
			logger.debug("Returning EJB instance " + clazzName + " from jndiMap.");
		}
		
		return ejbRef;
	}
}
