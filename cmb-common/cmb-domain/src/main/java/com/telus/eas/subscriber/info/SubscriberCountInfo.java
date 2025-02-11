package com.telus.eas.subscriber.info;

import java.io.Serializable;

public class SubscriberCountInfo implements Serializable{

	private static final long serialVersionUID = -3952876563445141365L;
	private int ban;
	private char productType;
	private int activeSubscribersCount;
	private int cancelledSubscribersCount;
	private int reservedSubscribersCount;
	private int suspendedSubscribersCount;
	private int allActiveSubscribersCount;
	private int allReservedSubscribersCount;
	private int allSuspendedSubscribersCount;
	private int allCancelledSubscribersCount;
	public int getBan() {
		return ban;
	}
	public void setBan(int ban) {
		this.ban = ban;
	}
	public char getProductType() {
		return productType;
	}
	public void setProductType(char productType) {
		this.productType = productType;
	}
	public int getActiveSubscribersCount() {
		return activeSubscribersCount;
	}
	public void setActiveSubscribersCount(int activeSubscribersCount) {
		this.activeSubscribersCount = activeSubscribersCount;
	}
	public int getCancelledSubscribersCount() {
		return cancelledSubscribersCount;
	}
	public void setCancelledSubscribersCount(int cancelledSubscribersCount) {
		this.cancelledSubscribersCount = cancelledSubscribersCount;
	}
	public int getReservedSubscribersCount() {
		return reservedSubscribersCount;
	}
	public void setReservedSubscribersCount(int reservedSubscribersCount) {
		this.reservedSubscribersCount = reservedSubscribersCount;
	}
	public int getSuspendedSubscribersCount() {
		return suspendedSubscribersCount;
	}
	public void setSuspendedSubscribersCount(int suspendedSubscribersCount) {
		this.suspendedSubscribersCount = suspendedSubscribersCount;
	}
	public int getAllActiveSubscribersCount() {
		return allActiveSubscribersCount;
	}
	public void setAllActiveSubscribersCount(int allActiveSubscribersCount) {
		this.allActiveSubscribersCount = allActiveSubscribersCount;
	}
	public int getAllReservedSubscribersCount() {
		return allReservedSubscribersCount;
	}
	public void setAllReservedSubscribersCount(int allReservedSubscribersCount) {
		this.allReservedSubscribersCount = allReservedSubscribersCount;
	}
	public int getAllSuspendedSubscribersCount() {
		return allSuspendedSubscribersCount;
	}
	public void setAllSuspendedSubscribersCount(int allSuspendedSubscribersCount) {
		this.allSuspendedSubscribersCount = allSuspendedSubscribersCount;
	}
	public int getAllCancelledSubscribersCount() {
		return allCancelledSubscribersCount;
	}
	public void setAllCancelledSubscribersCount(int allCancelledSubscribersCount) {
		this.allCancelledSubscribersCount = allCancelledSubscribersCount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String toString() {
		return "SubscriberCountInfo [ban=" + ban + ", productType=" + productType + ", activeSubscribersCount=" + activeSubscribersCount + ", reservedSubscribersCount=" + reservedSubscribersCount
				+ ", suspendedSubscribersCount=" + suspendedSubscribersCount + ", allActiveSubscribersCount=" + allActiveSubscribersCount + ", allReservedSubscribresCount="
				+ allReservedSubscribersCount + ", allSuspendedSubscribersCount=" + allSuspendedSubscribersCount + ", allCancelledSubscribersCount=" + allCancelledSubscribersCount + "]";
	}
	
	
	
}
