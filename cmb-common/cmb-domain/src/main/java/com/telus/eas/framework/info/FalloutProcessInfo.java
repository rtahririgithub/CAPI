package com.telus.eas.framework.info;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FalloutProcessInfo extends Info {

	private static final long serialVersionUID = 1L;

	public static final String APPID_CLIENTAPI_EJB = "ClientAPI_EJB";
	public static final String SERVICE_NAME_ACCOUNTLIFECYCLEFACADE = "AccountLifecycleFacade";
	public static final String SERVICE_NAME_SUBSCRIBERLIFECYCLEFACADE = "SubscriberLifecycleFacade";
	
	public static final String APPID_DATA_SERVICE = "CODS_App";
	public static final String DATA_SERVICE_CONSUMER_BILLINGACCOUNT = " ConsumerBillingAccountDataManagementService";
	public static final String DATA_SERVICE_CONSUMER_PRODUCT = " ConsumerProductDataManagementService";

	
	private String applicationId;
	private String serviceName;
	private String orderNumber;
	private String customerId;
	private String resourceId;
	private String serviceTelephoneNumber;
	private String correlationId;
	private String requestMessage;
	private List falloutExceptionInfoList = new ArrayList();
	
	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getServiceTelephoneNumber() {
		return serviceTelephoneNumber;
	}

	public void setServiceTelephoneNumber(String serviceTelephoneNumber) {
		this.serviceTelephoneNumber = serviceTelephoneNumber;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	
	public String getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	public List getFalloutExceptionInfoList() {
		return falloutExceptionInfoList;
	}

	public void setFalloutExceptionInfoList(List falloutExceptionInfoList) {
		this.falloutExceptionInfoList = falloutExceptionInfoList;
	}


	public class FalloutExceptionInfo {
		private String targetApplicationId;
		private String targetServiceName;
		private Calendar exceptionTimeStamp;
		private String exceptionType;
		private String exceptionDetail;
		
		public String getTargetApplicationId() {
			return targetApplicationId;
		}
		public void setTargetApplicationId(String targetApplicationId) {
			this.targetApplicationId = targetApplicationId;
		}
		public String getTargetServiceName() {
			return targetServiceName;
		}
		public void setTargetServiceName(String targetServiceName) {
			this.targetServiceName = targetServiceName;
		}
		public Calendar getExceptionTimeStamp() {
			return exceptionTimeStamp;
		}
		public void setExceptionTimeStamp(Calendar exceptionTimeStamp) {
			this.exceptionTimeStamp = exceptionTimeStamp;
		}
		public void setExceptionTimeStamp (long timestamp) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(timestamp);
			setExceptionTimeStamp(cal);
		}
		
		public String getExceptionType() {
			return exceptionType;
		}
		public void setExceptionType(String exceptionType) {
			this.exceptionType = exceptionType;
		}
		public String getExceptionDetail() {
			return exceptionDetail;
		}
		public void setExceptionDetail(String exceptionDetail) {
			this.exceptionDetail = exceptionDetail;
		}
	}
	
	
}
