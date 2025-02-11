package com.telus.api.portability;

public class DeactivateMVNESubscriberException extends PRMSystemException {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 3285679539913992135L;
	private String code = null;
	private String subSystemException = null;

	public DeactivateMVNESubscriberException(String message) {
		super(message);
	}
	
	public DeactivateMVNESubscriberException(String message, String code, String subSystemException) {
		super(message);
		this.code = code;
		this.subSystemException = subSystemException;
	}

	public DeactivateMVNESubscriberException(String message, Throwable t) {
	    super(message, t);
	  }
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the subSystemException
	 */
	public String getSubSystemException() {
		return subSystemException;
	}

	/**
	 * @param subSystemException the subSystemException to set
	 */
	public void setSubSystemException(String subSystemException) {
		this.subSystemException = subSystemException;
	}
	
}
