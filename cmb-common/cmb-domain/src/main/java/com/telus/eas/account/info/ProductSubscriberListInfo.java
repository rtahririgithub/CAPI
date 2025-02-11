package com.telus.eas.account.info;

import com.telus.api.account.ProductSubscriberList;
import com.telus.api.account.SubscriberIdentifier;
import com.telus.eas.framework.info.Info;

public class ProductSubscriberListInfo extends Info implements
		ProductSubscriberList {
	static final long serialVersionUID = 1L;

	private String productType;
	private SubscriberIdentifier[] activeSubscriberIdentifiers = new SubscriberIdentifierInfo[0];
	private SubscriberIdentifier[] cancelledSubscriberIdentifiers = new SubscriberIdentifierInfo[0];
	private SubscriberIdentifier[] reservedSubscriberIdentifiers = new SubscriberIdentifierInfo[0];
	private SubscriberIdentifier[] suspendedSubscriberIdentifiers = new SubscriberIdentifierInfo[0];

	/**
	 * @deprecated
	 */
	private String[] activeSubscribers;

	/**
	 * @deprecated
	 */
	private String[] cancelledSubscribers;

	/**
	 * @deprecated
	 */
	private String[] reservedSubscribers;

	/**
	 * @deprecated
	 */
	private String[] suspendedSubscribers;

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	/**
	 * @deprecated
	 */
	public String[] getActiveSubscribers() {
		return activeSubscribers;
	}

	/**
	 * @deprecated
	 * @param activeSubscribers
	 */
	public void setActiveSubscribers(String[] activeSubscribers) {
		this.activeSubscribers = activeSubscribers;
	}

	/**
	 * @deprecated
	 */
	public String[] getCancelledSubscribers() {
		return cancelledSubscribers;
	}

	/**
	 * @deprecated
	 * @param cancelledSubscribers
	 */
	public void setCancelledSubscribers(String[] cancelledSubscribers) {
		this.cancelledSubscribers = cancelledSubscribers;
	}

	/**
	 * @deprecated
	 */
	public String[] getReservedSubscribers() {
		return reservedSubscribers;
	}

	/**
	 * @deprecated
	 * @param reservedSubscribers
	 */
	public void setReservedSubscribers(String[] reservedSubscribers) {
		this.reservedSubscribers = reservedSubscribers;
	}

	/**
	 * @deprecated
	 */
	public String[] getSuspendedSubscribers() {
		return suspendedSubscribers;
	}

	/**
	 * @deprecated
	 * @param suspendedSubscribers
	 */
	public void setSuspendedSubscribers(String[] suspendedSubscribers) {
		this.suspendedSubscribers = suspendedSubscribers;
	}

	public int getActiveSubscribersCount() {
		return activeSubscribers != null ? activeSubscribers.length : 0;
	}

	public int getCancelledSubscribersCount() {
		return cancelledSubscribers != null ? cancelledSubscribers.length : 0;
	}

	public int getReservedSubscribersCount() {
		return reservedSubscribers != null ? reservedSubscribers.length : 0;
	}

	public int getSuspendedSubscribersCount() {
		return suspendedSubscribers != null ? suspendedSubscribers.length : 0;
	}

	public SubscriberIdentifier[] getActiveSubscriberIdentifiers() {
		return activeSubscriberIdentifiers;
	}

	public void setActiveSubscriberIdentifiers(
			SubscriberIdentifier[] activeSubscriberIdentifiers) {
		this.activeSubscriberIdentifiers = activeSubscriberIdentifiers;		
		this.activeSubscribers = getSubscriberIdFromSubscriberIdentifierArray(activeSubscriberIdentifiers);
	}

	public SubscriberIdentifier[] getCancelledSubscriberIdentifiers() {
		return cancelledSubscriberIdentifiers;
	}

	public void setCancelledSubscriberIdentifiers(
			SubscriberIdentifier[] cancelledSubscriberIdentifiers) {
		this.cancelledSubscriberIdentifiers = cancelledSubscriberIdentifiers;
		this.cancelledSubscribers = getSubscriberIdFromSubscriberIdentifierArray(cancelledSubscriberIdentifiers);
	}

	public SubscriberIdentifier[] getReservedSubscriberIdentifiers() {
		return reservedSubscriberIdentifiers;
	}

	public void setReservedSubscriberIdentifiers(
			SubscriberIdentifier[] reservedSubscriberIdentifiers) {
		this.reservedSubscriberIdentifiers = reservedSubscriberIdentifiers;
		this.reservedSubscribers = getSubscriberIdFromSubscriberIdentifierArray(reservedSubscriberIdentifiers);
	}

	public SubscriberIdentifier[] getSuspendedSubscriberIdentifiers() {
		return suspendedSubscriberIdentifiers;
	}

	public void setSuspendedSubscriberIdentifiers(
			SubscriberIdentifier[] suspendedSubscriberIdentifiers) {
		this.suspendedSubscriberIdentifiers = suspendedSubscriberIdentifiers;
		this.suspendedSubscribers = getSubscriberIdFromSubscriberIdentifierArray(suspendedSubscriberIdentifiers);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("ProductSubscriberListInfo: [\n");
		sb.append("    productType=[").append(productType).append("]\n");

		sb.append("    activeSubscribers=[\n");
		for (int i = 0; i < getActiveSubscribersCount(); i++)
			sb.append("        [").append(activeSubscribers[i]).append("]\n");
		sb.append("    ]\n");

		sb.append("    cancelledSubscribers=[\n");
		for (int i = 0; i < getCancelledSubscribersCount(); i++)
			sb.append("        [").append(cancelledSubscribers[i])
					.append("]\n");
		sb.append("    ]\n");

		sb.append("    reservedSubscribers=[\n");
		for (int i = 0; i < getReservedSubscribersCount(); i++)
			sb.append("        [").append(reservedSubscribers[i]).append("]\n");
		sb.append("    ]\n");

		sb.append("    suspendedSubscribers=[\n");
		for (int i = 0; i < getSuspendedSubscribersCount(); i++)
			sb.append("        [").append(suspendedSubscribers[i])
					.append("]\n");
		sb.append("    ]\n");
		sb.append("]\n");

		return sb.toString();
	}
	
	private String[] getSubscriberIdFromSubscriberIdentifierArray(
			SubscriberIdentifier[] inputSubscriberIdentifier) {
		if (inputSubscriberIdentifier != null) {
			String[] returnArray = new String[inputSubscriberIdentifier.length];
			
			for (int i = 0; i < inputSubscriberIdentifier.length; i++) {
				returnArray[i] = inputSubscriberIdentifier[i].getSubscriberId();
			}
			return returnArray;
		} else {
			return null;
		}
	}
}
