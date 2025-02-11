package com.telus.eas.subscriber.info;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.Info;

/**
 * 
 * @author tongts
 *
 */
public class BaseChangeInfo extends Info {
	protected int ban;
	protected String subscriberId;
	protected SubscriberInfo currentSubscriberInfo;
	protected AccountInfo currentAccountInfo;
	protected EquipmentInfo currentEquipmentInfo;
	protected SubscriberContractInfo currentContractInfo;
	
	protected String productType;
	protected String previousProductType;
	protected int previousBan;
	protected String previousSubscriberId;
	protected SubscriberInfo previousSubscriberInfo;
	protected AccountInfo previousAccountInfo;
	

	
	public int getBan() {
		return ban;
	}
	public void setBan(int ban) {
		this.ban = ban;
	}
	public String getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	public SubscriberInfo getCurrentSubscriberInfo() {
		return currentSubscriberInfo;
	}
	public void setCurrentSubscriberInfo(SubscriberInfo currentSubscriberInfo) {
		this.currentSubscriberInfo = currentSubscriberInfo;
		if (currentSubscriberInfo != null) {
			subscriberId = currentSubscriberInfo.getSubscriberId();
		}
	}
	public AccountInfo getCurrentAccountInfo() {
		return currentAccountInfo;
	}
	public void setCurrentAccountInfo(AccountInfo currentAccountInfo) {
		this.currentAccountInfo = currentAccountInfo;
		if (currentAccountInfo != null) {
			ban = currentAccountInfo.getBanId();
		}
	}
	public EquipmentInfo getCurrentEquipmentInfo() {
		return currentEquipmentInfo;
	}
	public void setCurrentEquipmentInfo(EquipmentInfo currentEquipmentInfo) {
		this.currentEquipmentInfo = currentEquipmentInfo;
	}
	public SubscriberContractInfo getCurrentContractInfo() {
		return currentContractInfo;
	}
	public void setCurrentContractInfo(SubscriberContractInfo currentContractInfo) {
		this.currentContractInfo = currentContractInfo;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getPreviousProductType() {
		return previousProductType;
	}
	public void setPreviousProductType(String previousProductType) {
		this.previousProductType = previousProductType;
	}
	public int getPreviousBan() {
		return previousBan;
	}
	public void setPreviousBan(int previousBan) {
		this.previousBan = previousBan;
	}
	public String getPreviousSubscriberId() {
		return previousSubscriberId;
	}
	public void setPreviousSubscriberId(String previousSubscriberId) {
		this.previousSubscriberId = previousSubscriberId;
	}
	public SubscriberInfo getPreviousSubscriberInfo() {
		return previousSubscriberInfo;
	}
	public void setPreviousSubscriberInfo(SubscriberInfo previousSubscriberInfo) {
		this.previousSubscriberInfo = previousSubscriberInfo;
	}
	public AccountInfo getPreviousAccountInfo() {
		return previousAccountInfo;
	}
	public void setPreviousAccountInfo(AccountInfo previousAccountInfo) {
		this.previousAccountInfo = previousAccountInfo;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("BaseChangeInfo[");
		if ( currentAccountInfo!=null )	
			sb.append("currAcct(").append( currentAccountInfo.getBanId()).append(")");
		else if (ban!=0) 
			sb.append("ban=").append(ban);
		
		if ( currentSubscriberInfo!=null ) {
			sb.append("  currSub(").append( currentSubscriberInfo.getBanId())
				.append("/").append(currentSubscriberInfo.getSubscriberId())
				.append("/").append(currentSubscriberInfo.getPhoneNumber() ).append(")");
		} else 	if (subscriberId!=null) 
			sb.append(" subId=").append( subscriberId );

		if ( previousAccountInfo!=null )	
			sb.append(" prevAcct(").append( previousAccountInfo.getBanId()).append(")");
		else if (previousBan!=0) 
			sb.append("  prevBan=").append(previousBan);
		
		if ( previousSubscriberInfo!=null ) {
			sb.append("  prevSub(").append( previousSubscriberInfo.getBanId())
				.append("/").append(previousSubscriberInfo.getSubscriberId())
				.append("/").append(previousSubscriberInfo.getPhoneNumber() ).append(")")
				.append(" startDate=").append( previousSubscriberInfo.getStartServiceDate() );
		} else 	if (previousSubscriberId!=null) 
			sb.append(", prevSubId=").append( previousSubscriberId );
		
		sb.append("]");
		
		return sb.toString();

	}
	
}
