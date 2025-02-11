package com.telus.eas.equipment.info;

/**
 * Title:       CellularDigitalEquipmentUpgradeInfo <p>
 * Descriptioncription:    stores Upgrade information <p>
 *
 * Copyright (c) Telus Mobility Inc.<p>
 *
 * @author Peter Frei
 * @version 1.0
 *
 */

import com.telus.api.equipment.CellularDigitalEquipmentUpgrade;
import com.telus.eas.framework.info.*;

public class CellularDigitalEquipmentUpgradeInfo
	extends Info
	implements CellularDigitalEquipmentUpgrade {

    static final long serialVersionUID = 1L;

	private String promotionDescription;
	private String promotionDescriptionFrench;
	private String PRLCode;
	private String browserVersion;
  private String firmwareVersion;
  private java.util.Date startDate;
	private boolean otaspAvailable;

	public CellularDigitalEquipmentUpgradeInfo() {
	}

	public void setOtaspAvailable(boolean otaspAvailable) {
		this.otaspAvailable = otaspAvailable;
	}

	public boolean isOtaspAvailable() {
		return otaspAvailable;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setPRLCode(String PRLCode) {
		this.PRLCode = PRLCode;
	}

	public String getPRLCode() {
		return PRLCode;
	}

	public void setPromotionDescription(String promotionDescription) {
		this.promotionDescription = promotionDescription;
	}

	public String getPromotionDescription() {
		return promotionDescription;
	}

  public void setPromotionDescriptionFrench(String promotionDescriptionFrench) {
      this.promotionDescriptionFrench = promotionDescriptionFrench;
  }

  public String getPromotionDescriptionFrench() {
      return promotionDescriptionFrench;
  }

  public void setStartDate(java.util.Date startDate) {
      this.startDate = startDate;
  }

  public java.util.Date getStartDate() {
      return startDate;
  }

	public String toString() {
		StringBuffer s = new StringBuffer(1024);

		s.append("CellularDigitalEquipmentUpgradeInfo:[\n");
    s.append("    promotionDescription=[").append(promotionDescription).append("]\n");
    s.append("    promotionDescriptionFrench=[").append(promotionDescriptionFrench).append("]\n");
		s.append("    PRLCode=[").append(PRLCode).append("]\n");
		s.append("    browserVersion=[").append(browserVersion).append("]\n");
		s.append("    firmwareVersion=[").append(firmwareVersion).append("]\n");
    s.append("    startDate=[").append(startDate).append("]\n");
		s.append("    otaspAvailable=[").append(otaspAvailable).append("]\n");
		s.append("]");

		return s.toString();
	}

}
