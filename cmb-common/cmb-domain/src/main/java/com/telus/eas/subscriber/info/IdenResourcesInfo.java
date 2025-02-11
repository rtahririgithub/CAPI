/**
 * Title:        IdenResourcesInfo<p>
 * Description:  The IdenResourcesInfo holds all resources for an IDEN subscriber.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

package com.telus.eas.subscriber.info;


import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.NumberGroupInfo;

@Deprecated
public class IdenResourcesInfo extends Info {

  static final long serialVersionUID = 1L;

  private int network;
  private NumberGroupInfo numberGroup;
  private String subscriberId;
  private String phoneNumber;
  private int urbanId;
  private int fleetId;
  private String memberId;
  private String imsi;
  private String ip;
  private String ipType;
  private String resourceStatus;

  public IdenResourcesInfo() {
  }

  public void setNetwork(int network) {
    this.network = network;
  }
  public int getNetwork() {
    return network;
  }
  public void setNumberGroup(NumberGroupInfo numberGroup) {
    this.numberGroup = numberGroup;
  }
  public NumberGroupInfo getNumberGroup() {
    return numberGroup;
  }
  public void setSubscriberId(String subscriberId) {
    this.subscriberId = subscriberId;
  }
  public String getSubscriberId() {
    return subscriberId;
  }
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  public String getPhoneNumber() {
    return phoneNumber;
  }
  public void setUrbanId(int urbanId) {
    this.urbanId = urbanId;
  }
  public int getUrbanId() {
    return urbanId;
  }
  public void setFleetId(int fleetId) {
    this.fleetId = fleetId;
  }
  public int getFleetId() {
    return fleetId;
  }
  public void setMemberId(String memberId) {
    this.memberId = memberId;
  }
  public String getMemberId() {
    return memberId;
  }
  public void setImsi(String imsi) {
    this.imsi = imsi;
  }
  public String getImsi() {
    return imsi;
  }
  public void setIp(String ip) {
    this.ip = ip;
  }
  public String getIp() {
    return ip;
  }
  public void setIpType(String ipType) {
    this.ipType = ipType;
  }
  public String getIpType() {
    return ipType;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("IdenResourcesInfo:{\n");
    s.append("    network=[").append(network).append("]\n");
    s.append("    numberGroup=[").append(numberGroup).append("]\n");
    s.append("    subscriberId=[").append(subscriberId).append("]\n");
    s.append("    phoneNumber=[").append(phoneNumber).append("]\n");
    s.append("    imsi=[").append(imsi).append("]\n");
    s.append("    urbanId=[").append(urbanId).append("]\n");
    s.append("    fleetId=[").append(fleetId).append("]\n");
    s.append("    memberId=[").append(memberId).append("]\n");
    s.append("    ip=[").append(ip).append("]\n");
    s.append("    ipType=[").append(ipType).append("]\n");
    s.append("}");

    return s.toString();
  }

/**
 * @return Returns the resourceStatus.
 */
public String getResourceStatus() {
	return resourceStatus;
}
/**
 * @param resourceStatus The resourceStatus to set.
 */
public void setResourceStatus(String resourceStatus) {
	this.resourceStatus = resourceStatus;
}
}

