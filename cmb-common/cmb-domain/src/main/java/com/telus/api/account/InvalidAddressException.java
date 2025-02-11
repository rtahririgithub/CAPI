package com.telus.api.account;

import com.telus.api.TelusAPIException;

/**
 * 
 * <CODE>InvalidAddressException</CODE>
 * 
 * 
 */

public class InvalidAddressException extends TelusAPIException {

	private Address offendingAddress;

	private Address suggestedAddress;

	private boolean serviceUnavailable;

	public InvalidAddressException(String message, Throwable exception, Address offendingAddress, Address suggestedAddress) {

		super(message, exception);

		this.offendingAddress = offendingAddress;

	}

	public InvalidAddressException(String message, Throwable exception, boolean serviceUnavailable, Address offendingAddress) {

		super(message, exception);

		this.serviceUnavailable = serviceUnavailable;

		this.offendingAddress = offendingAddress;

	}

	public InvalidAddressException(Throwable exception, Address offendingAddress, Address suggestedAddress) {

		super(exception);

		this.suggestedAddress = suggestedAddress;

		this.offendingAddress = offendingAddress;

	}

	public InvalidAddressException(Throwable exception, boolean serviceUnavailable, Address offendingAddress) {

		super(exception);

		this.serviceUnavailable = serviceUnavailable;

		this.offendingAddress = offendingAddress;

	}

	public InvalidAddressException(String message, Address offendingAddress, Address suggestedAddress) {

		super(message);

		this.suggestedAddress = suggestedAddress;

		this.offendingAddress = offendingAddress;

	}

	public InvalidAddressException(String message, boolean serviceUnavailable, Address offendingAddress) {

		super(message);

		this.serviceUnavailable = serviceUnavailable;

		this.offendingAddress = offendingAddress;

	}

	public Address getOffendingAddress() {

		return offendingAddress;

	}

	public Address getSuggestedAddress() {

		return suggestedAddress;

	}

	public boolean isServiceUnavailable() {

		return serviceUnavailable;

	}

}
