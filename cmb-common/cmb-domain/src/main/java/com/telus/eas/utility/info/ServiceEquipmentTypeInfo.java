package com.telus.eas.utility.info;

/**
 * Title:        Telus Domain Project
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */


import java.util.ArrayList;

import com.telus.api.reference.NetworkType;
import com.telus.api.reference.Reference;
import com.telus.eas.framework.info.Info;

public class ServiceEquipmentTypeInfo extends Info implements Reference {
  static final long serialVersionUID = 1L;
  
  private String code;
  private String description = "";
  private String descriptionFrench = "";
  private ArrayList equipmentTypes = new ArrayList();
  private String networkType = NetworkType.NETWORK_TYPE_ALL;


  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getDescription() {
    return description;
  }
  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }
  public String getDescriptionFrench() {
    return descriptionFrench;
  }
  public void setEquipmentTypes(ArrayList equipmentTypes) {
    this.equipmentTypes = equipmentTypes;
  }
  public ArrayList getEquipmentTypes() {
    return equipmentTypes;
  }
  
  public void addEquipmentType(String equipmentType ) {
	if ( equipmentTypes.contains(equipmentType)==false )
	   	equipmentTypes.add(equipmentType);
  } 

  public void setNetworkType(String networkType) {
	  this.networkType = networkType;
  }
  
  public String getNetworkType() {
	  return this.networkType;
  }
  
  public ServiceEquipmentTypeInfo merge(ServiceEquipmentTypeInfo anotherInfo) {
	  //can merge the same SOC with PCS networks only
	  if (anotherInfo != null && code.equals(anotherInfo.getCode()) && 
		  !NetworkType.NETWORK_TYPE_IDEN.equals(anotherInfo.getNetworkType()) && 
		  !NetworkType.NETWORK_TYPE_IDEN.equals(networkType)) {
		  ServiceEquipmentTypeInfo info = new ServiceEquipmentTypeInfo();  

		  info.setCode(code);
		  info.getEquipmentTypes().addAll(equipmentTypes);
		  info.getEquipmentTypes().removeAll(anotherInfo.getEquipmentTypes());
		  info.getEquipmentTypes().addAll(anotherInfo.getEquipmentTypes());
		  info.setNetworkType(NetworkType.NETWORK_TYPE_ALL);
		  return info;
	  }
	  
	  return this;
  }
  
  public String toString()
  {
      StringBuffer s = new StringBuffer(128);
      s.append("ServiceEquipmentTypeInfo:[\n");
      s.append("   code=[").append(code).append("]\n");
      s.append("   networkType=[").append(networkType).append("]\n");
      s.append("   equipmentTypes=[").append(equipmentTypes).append("]\n");
      s.append("]\n");
      
      return s.toString();
  }
}