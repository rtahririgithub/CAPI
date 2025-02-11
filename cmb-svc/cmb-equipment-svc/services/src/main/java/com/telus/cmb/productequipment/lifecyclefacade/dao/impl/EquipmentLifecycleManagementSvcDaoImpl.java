package com.telus.cmb.productequipment.lifecyclefacade.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.productequipment.lifecyclefacade.dao.EquipmentLifecycleManagementSvcDao;
import com.telus.cmb.wsclient.EquipmentLifeCycleManagementServicePort;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentlifecyclemanagementservicerequestresponse_v2.ApproveReservedEquipmentForPhoneNumberRequestType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentlifecyclemanagementservicerequestresponse_v2.AssignEquipmentToPhoneNumberRequestType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentlifecyclemanagementservicerequestresponse_v2.ChangePhoneNumberRequestType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentlifecyclemanagementservicerequestresponse_v2.DisassociateEquipmentFromPhoneNumberRequestType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentlifecyclemanagementservicerequestresponse_v2.ReleaseReservedEquipmentForPhoneNumberRequestType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentlifecyclemanagementservicerequestresponse_v2.SwapEquipmentForPhoneNumberRequestType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentlifecyclemanagementservicerequestresponse_v2.UpdateEquipmentClientStatusRequestType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentlifecyclemanagementservicerequestresponse_v2.UpdateEquipmentClientStatusResponseType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentlifecyclemanagementservicerequestresponse_v2.UpdateEquipmentClientStatusResponseType.MessageList;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.Message;


public  class EquipmentLifecycleManagementSvcDaoImpl extends SoaBaseSvcClient implements EquipmentLifecycleManagementSvcDao{

	private static final String USER_ID = "Client API";
	private static final String FLAGGING_USER_PARTY_CD = "SYSTEM";
	private static final String CUSTOMER_LOST_REASON_CODE = "3";
	private static final String CUSTOMER_STOLEN_REASON_CODE = "4";
	private static final String CUSTOMER_FOUND_REASON_CODE = "6";

	@Autowired
	private EquipmentLifeCycleManagementServicePort service;

	@Override
	public void assignEquipmentToPhoneNumber(final String phoneNumber,final String serialNumber, final String associatedHandsetIMEI) throws ApplicationException {

		if (phoneNumber == null || phoneNumber.trim().equals("") || serialNumber == null || serialNumber.trim().equals("")) {
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB,ErrorCodes.INVALID_INPUT_PARAMETERS,"subscriber phoneNumber ,serialNumber both are mandatory and it should not be empty or null.","");
		}

		execute( new SoaCallback<Object>() {
			
			@Override
			public Object doCallback() throws Throwable {
				
				AssignEquipmentToPhoneNumberRequestType request = new AssignEquipmentToPhoneNumberRequestType();
				request.setImei(associatedHandsetIMEI);
				request.setPhoneNumber(phoneNumber);
				request.setUsimId(serialNumber);
				
				service.assignEquipmentToPhoneNumber(request);
				
				return null;
			}
		});
	}


	@Override
	public void changePhoneNumber(final String serialNumber, final String newPhoneNumber) throws ApplicationException {

		if(newPhoneNumber==null || newPhoneNumber.trim().equals("") || serialNumber==null || serialNumber.trim().equals("")){
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB,ErrorCodes.INVALID_INPUT_PARAMETERS,"subscriber newphoneNumber ,serialNumber both are mandatory and it should not be empty or null.","");
		}
		
		execute(new SoaCallback<Object>() {

			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {

				ChangePhoneNumberRequestType request = new ChangePhoneNumberRequestType();
				request.setPhoneNumber(newPhoneNumber);
				request.setUsimId(serialNumber);
				service.changePhoneNumber(request);
				return null;
			}
		});
	}


	@Override
	public void markEquipmentStolen(final String usimId,final String equipmentGroup) throws ApplicationException {
		
		if (usimId == null || usimId.trim().equals("")) {
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB,ErrorCodes.INVALID_INPUT_PARAMETERS,"usimId missing in request,it should be mandatory to mark Equipment as stolen.","");
		}
		
		execute(new SoaCallback<Object>() {

			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {

				UpdateEquipmentClientStatusRequestType markEqStolenReq = new UpdateEquipmentClientStatusRequestType();
				markEqStolenReq.setSerialNumber(usimId);
				markEqStolenReq.setEquipmentGroup(equipmentGroup.toUpperCase());
				markEqStolenReq.setReasonCode(CUSTOMER_STOLEN_REASON_CODE);
				markEqStolenReq.setUserId(USER_ID);
				markEqStolenReq.setFlaggingUserPartyCd(FLAGGING_USER_PARTY_CD);
				UpdateEquipmentClientStatusResponseType markEquipmentStolenResponse = service.updateEquipmentClientStatus(markEqStolenReq);
				String message = "";
				if (!markEquipmentStolenResponse.getMarkEquipmentStatusCd().equalsIgnoreCase("SUCCESS")) {
					for (MessageList messageList : markEquipmentStolenResponse.getMessageList()) {
						for (Message message1 : messageList.getMessageList()) {
							message += message1.getMessage();
						}
					}
					throw new ApplicationException(SystemCodes.CMB_PEF_DAO, "", message.isEmpty() ? "markEquipmentStolen error occured for usimId" + usimId : message, "");
				}
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.productequipment.lifecyclefacade.dao.EquipmentLifecycleManagementSvcDao#markEquipmentLost(java.lang.String, java.lang.String)
	 */
	@Override
	public void markEquipmentLost(final String usimId, final String equipmentGroup) throws ApplicationException {

		if (usimId == null || usimId.trim().equals("")){
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB,ErrorCodes.INVALID_INPUT_PARAMETERS,"usimId missing in request,it should be mandatory to mark Equipment as lost","");
		}
		
		execute(new SoaCallback<Object>() {

			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {
				
				UpdateEquipmentClientStatusRequestType markEqLostReq = new UpdateEquipmentClientStatusRequestType();
				markEqLostReq.setSerialNumber(usimId);
				markEqLostReq.setEquipmentGroup(equipmentGroup.toUpperCase());
				markEqLostReq.setReasonCode(CUSTOMER_LOST_REASON_CODE);
				markEqLostReq.setUserId(USER_ID);
				markEqLostReq.setFlaggingUserPartyCd(FLAGGING_USER_PARTY_CD);
				UpdateEquipmentClientStatusResponseType markEquipmentStolenResponse = service.updateEquipmentClientStatus(markEqLostReq);
				String message = "";
				if (!markEquipmentStolenResponse.getMarkEquipmentStatusCd().equalsIgnoreCase("SUCCESS")) {
					for (MessageList messageList : markEquipmentStolenResponse.getMessageList()) {
						for (Message message1 : messageList.getMessageList()) {
							message += message1.getMessage();
						}
					}

					throw new ApplicationException(SystemCodes.CMB_PEF_DAO, "", message.isEmpty() ? "markEquipmentLost error occred for usimid :" + usimId : message, "");
				}
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.productequipment.lifecyclefacade.dao.EquipmentLifecycleManagementSvcDao#markEquipmentFound(java.lang.String, java.lang.String)
	 */
	@Override
	public void markEquipmentFound(final String usimId, final String equipmentGroup) throws ApplicationException {

		if (usimId == null || usimId.trim().equals("")){
			throw new ApplicationException(SystemCodes.CMB_PELF_EJB,ErrorCodes.INVALID_INPUT_PARAMETERS,"usimId missing in request,it should be mandatory to mark Equipment as found .","");
		}
		
		execute(new SoaCallback<Object>() {

			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {
				
				UpdateEquipmentClientStatusRequestType markEqFoundReq = new UpdateEquipmentClientStatusRequestType();
				markEqFoundReq.setSerialNumber(usimId);
				markEqFoundReq.setEquipmentGroup(equipmentGroup.toUpperCase());
				markEqFoundReq.setReasonCode(CUSTOMER_FOUND_REASON_CODE);
				markEqFoundReq.setUserId(USER_ID);
				markEqFoundReq.setFlaggingUserPartyCd(FLAGGING_USER_PARTY_CD);
				UpdateEquipmentClientStatusResponseType markEquipmentStolenResponse = service.updateEquipmentClientStatus(markEqFoundReq);
				String message = "";
				if (!markEquipmentStolenResponse.getMarkEquipmentStatusCd().equalsIgnoreCase("SUCCESS")) {
					for (MessageList messageList : markEquipmentStolenResponse.getMessageList()) {
						for (Message message1 : messageList.getMessageList()) {
							message += message1.getMessage();
						}
					}

					throw new ApplicationException(SystemCodes.CMB_PEF_DAO, "", message.isEmpty() ? "markEquipmentFound error occured for usimId :" + usimId : message, "");
				}
				return null;
			}
		});
	}

	@Override
	public void approveReservedEquipmentForPhoneNumber(final String phoneNumber,final String serialNumber, final String associatedHandsetIMEI) throws ApplicationException {
		
		if(phoneNumber==null || phoneNumber.trim().equals("") || serialNumber==null || serialNumber.trim().equals("")){
			throw new ApplicationException (SystemCodes.CMB_PELF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, "subscriber phoneNumber ,serialNumber both are mandatory and it should not be empty or null.", "");
		}
		
		execute(new SoaCallback<Object>() {

			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {
				ApproveReservedEquipmentForPhoneNumberRequestType approveReservedEqForPN = new ApproveReservedEquipmentForPhoneNumberRequestType();
				approveReservedEqForPN.setUsimId(serialNumber);
				approveReservedEqForPN.setPhoneNumber(phoneNumber);
				approveReservedEqForPN.setImei(associatedHandsetIMEI);
				service.approveReservedEquipmentForPhoneNumber(approveReservedEqForPN);
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.productequipment.lifecyclefacade.dao.EquipmentLifecycleManagementSvcDao#releaseReservedEquipmentForPhoneNumber(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void releaseReservedEquipmentForPhoneNumber(final String phoneNumber,final String serialNumber, final String associatedHandsetIMEI) throws ApplicationException {
		
		if(phoneNumber==null || phoneNumber.trim().equals("") || serialNumber==null || serialNumber.trim().equals("")){
			throw new ApplicationException (SystemCodes.CMB_PELF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, "subscriber phoneNumber ,serialNumber both are mandatory and it should not be empty or null.", "");
		}
		
		execute(new SoaCallback<Object>() {

			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {
				ReleaseReservedEquipmentForPhoneNumberRequestType releaseReservedEqForPN = new ReleaseReservedEquipmentForPhoneNumberRequestType();
				releaseReservedEqForPN.setUsimId(serialNumber);
				releaseReservedEqForPN.setPhoneNumber(phoneNumber);
				releaseReservedEqForPN.setImei(associatedHandsetIMEI);
				service.releaseReservedEquipmentForPhoneNumber(releaseReservedEqForPN);
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.productequipment.lifecyclefacade.dao.EquipmentLifecycleManagementSvcDao#disassociateEquipmentFromPhoneNumber(java.lang.String, java.lang.String)
	 */
	@Override
	public void disassociateEquipmentFromPhoneNumber(final String phoneNumber,final String usimId) throws ApplicationException {

		if(phoneNumber==null || phoneNumber.trim().equals("") || usimId==null || usimId.trim().equals("")){
			throw new ApplicationException (SystemCodes.CMB_PELF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, "subscriber phoneNumber ,serialNumber both are mandatory and it should not be empty or null.", "");
		}
		
		execute(new SoaCallback<Object>() {

			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {
				DisassociateEquipmentFromPhoneNumberRequestType disEqFromPNType = new DisassociateEquipmentFromPhoneNumberRequestType();
				disEqFromPNType.setPhoneNumber(phoneNumber);
				disEqFromPNType.setUsimId(usimId);
				service.disassociateEquipmentFromPhoneNumber(disEqFromPNType);
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.productequipment.lifecyclefacade.dao.EquipmentLifecycleManagementSvcDao#swapHSPAOnlyEquipmentForPhoneNumber(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void swapHSPAOnlyEquipmentForPhoneNumber(final String phoneNumber,final String oldSerialNumber, final String newSerialNumber,String oldAssociatedHandsetIMEI, final String newAssociatedHandsetIMEI) throws ApplicationException {
		
		execute(new SoaCallback<Object>() {

			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {
				SwapEquipmentForPhoneNumberRequestType swapEqForPNType = new SwapEquipmentForPhoneNumberRequestType();
				swapEqForPNType.setPhoneNumber(phoneNumber);
				swapEqForPNType.setOldUsimId(oldSerialNumber);
				swapEqForPNType.setImei(newAssociatedHandsetIMEI);
				// if new USIM Id is the same with the old USIMId , it's a handset only swap , therefore the new USIM Id .should be passed as a NULL value
				if (!newSerialNumber.equalsIgnoreCase(oldSerialNumber))swapEqForPNType.setNewUsimId(newSerialNumber);
				{
					service.swapEquipmentForPhoneNumber(swapEqForPNType);
				}
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient#ping()
	 */
	@Override
	public String ping() throws ApplicationException {
		return execute( new SoaCallback<String>() {
			
			@Override
			public String doCallback() throws Throwable {
				return service.ping();
			}
		});
	}
}
