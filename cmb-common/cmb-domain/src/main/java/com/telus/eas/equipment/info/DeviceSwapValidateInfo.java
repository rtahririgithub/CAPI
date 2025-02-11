package com.telus.eas.equipment.info;

public class DeviceSwapValidateInfo implements java.io.Serializable {
		private String resultCd;
		private String currentSimType;
		private String currentSimProfileCd;
		private String newSimType;
		private String newDeviceType;
		private String newDeviceSimProfileCd;
		private static final long serialVersionUID = 1L;
		
		public String getResultCd() {
			return resultCd;
		}
		
		public void setResultCd(String resultCd) {
			this.resultCd = resultCd;
		}
		
		public String getCurrentSimType() {
			return currentSimType;
		}
		
		public void setCurrentSimType(String currentSimType) {
			this.currentSimType = currentSimType;
		}
		
		public String getCurrentSimProfileCd() {
			return currentSimProfileCd;
		}
		
		public void setCurrentSimProfileCd(String currentSimProfileCd) {
			this.currentSimProfileCd = currentSimProfileCd;
		}
		
		public String getNewSimType() {
			return newSimType;
		}
		
		public void setNewSimType(String newSimType) {
			this.newSimType = newSimType;
		}
		
		public String getNewDeviceType() {
			return newDeviceType;
		}
		
		public void setNewDeviceType(String newDeviceType) {
			this.newDeviceType = newDeviceType;
		}
		
		public String getNewDeviceSimProfileCd() {
			return newDeviceSimProfileCd;
		}
		
		public void setNewDeviceSimProfileCd(String newDeviceSimProfileCd) {
			this.newDeviceSimProfileCd = newDeviceSimProfileCd;
		}
		
		@Override
		public String toString() {
			return "DeviceSwapValidateInfo [resultCd=" + resultCd + ", currentSimType=" + currentSimType + ", currentSimProfileCd=" + currentSimProfileCd + ", newSimType=" + newSimType + ", newDeviceType=" + newDeviceType
					+ ", newDeviceSimProfileCd=" + newDeviceSimProfileCd + "]";
		}
}
