package com.telus.cmb.account.lifecyclemanager.dao.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Date;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import amdocs.APILink.datatypes.SpecialBillDetailsInfo;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.BillNotificationContact;
import com.telus.api.account.BillNotificationHistoryRecord;
import com.telus.api.reference.InvoiceSuppressionLevel;
import com.telus.cmb.account.lifecyclemanager.dao.InvoiceDao;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;


public class InvoiceDaoImpl extends AmdocsDaoSupport implements InvoiceDao {
	private static final Logger LOGGER = Logger.getLogger(InvoiceDaoImpl.class);
	private JdbcTemplate codsJdbcTemplate;	
	private static final String E_BILL_STAGING_GP = "EBILLSTAGE";
	
	private JdbcTemplate getCodsJdbcTemplate() {
		return codsJdbcTemplate;
	}

	public void setCodsJdbcTemplate(JdbcTemplate CodsJdbcTemplate) {
		this.codsJdbcTemplate=CodsJdbcTemplate;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.account.lifecyclefacade.dao.AccountModificationDao#updateInvoiceProperties(int, com.telus.eas.account.info.InvoicePropertiesInfo, java.lang.String)
	 */
	@Override
	public void updateInvoiceProperties(final int ban, final InvoicePropertiesInfo invoicePropertiesInfo, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {

				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				updateBanConv.setBanPK(ban);

				SpecialBillDetailsInfo specialBillDetailsInfo = new SpecialBillDetailsInfo();

				if (invoicePropertiesInfo.getInvoiceSuppressionLevel() != null && 
						!invoicePropertiesInfo.getInvoiceSuppressionLevel().trim().equals(""))
					specialBillDetailsInfo.invSuppressionInd = AttributeTranslator.byteFromString(invoicePropertiesInfo.getInvoiceSuppressionLevel());

				if (invoicePropertiesInfo.getHoldRedirectDestinationCode() != null && 
						!invoicePropertiesInfo.getHoldRedirectDestinationCode().trim().equals("") && 
						!invoicePropertiesInfo.getHoldRedirectDestinationCode().trim().equals("0")) {
					specialBillDetailsInfo.blManHndlReqOpid = AttributeTranslator.parseInt(invoicePropertiesInfo.getHoldRedirectDestinationCode());
					specialBillDetailsInfo.blManHndlEffDate = invoicePropertiesInfo.getHoldRedirectFromDate() != null ? 
							invoicePropertiesInfo.getHoldRedirectFromDate() : new Date();
							specialBillDetailsInfo.blManHndlExpDate = invoicePropertiesInfo.getHoldRedirectToDate() != null ? 
									invoicePropertiesInfo.getHoldRedirectToDate() : AttributeTranslator.dateFromString("21001231", "yyyyMMdd");
				}

				updateBanConv.setBillDetailsInfo(specialBillDetailsInfo);
				updateBanConv.saveBan();				

				return null;
			}
		});
	}

	@Override
	public void updateBillSuppression(final int ban, final boolean suppressBill,
			final Date effectiveDate, final Date expiryDate, String sessionId)
	throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				SpecialBillDetailsInfo specialBillDetailsInfo = new SpecialBillDetailsInfo();


				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				// Set BanPK (which also retrieves the BAN)
				amdocsUpdateBanConv.setBanPK(ban);

				// BAN cannot be in 'Tentative'
				if (amdocsUpdateBanConv.getBanHeaderInfo().banStatus == (byte) 'T') {
					throw new ApplicationException(SystemCodes.CMB_ALM_DAO, ErrorCodes.ACCOUNT_CANNOT_BE_TENTATIVE, "BAN cannot be in tentative status.");
				}

				// Add Bill Suppression
				if (suppressBill) {
					specialBillDetailsInfo.blManHndlReqOpid = 1;
					specialBillDetailsInfo.blManHndlEffDate = effectiveDate == null ? new java.util.GregorianCalendar()
					.getTime()
					: effectiveDate;
					specialBillDetailsInfo.blManHndlExpDate = expiryDate == null ? AttributeTranslator
							.dateFromString("20991231", "yyyyMMdd")
							: expiryDate;
							amdocsUpdateBanConv.setBillDetailsInfo(specialBillDetailsInfo);

							// Remove Bill Suppression
				} else {
					specialBillDetailsInfo.blManHndlReqOpid = 0;
					specialBillDetailsInfo.blManHndlEffDate = null; //blManHdnlReqOpid cannot be 0 if date is set. setting to null
					specialBillDetailsInfo.blManHndlExpDate = null; //blManHdnlReqOpid cannot be 0 if date is set. setting to null
					amdocsUpdateBanConv.setBillDetailsInfo(specialBillDetailsInfo);
				}


				amdocsUpdateBanConv.saveBan();

				return null;

			}
		});

	}

	@Override
	public void updateInvoiceSuppressionIndicator(final int ban, String sessionId)
	throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				SpecialBillDetailsInfo specialBillDetailsInfo = new SpecialBillDetailsInfo();


				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				// Set BanPK (which also retrieves the BAN)
				amdocsUpdateBanConv.setBanPK(ban);

				// Add Bill Suppression
				specialBillDetailsInfo.blManHndlReqOpid = 1;
				specialBillDetailsInfo.blManHndlEffDate = new java.util.GregorianCalendar().getTime();
				specialBillDetailsInfo.blManHndlExpDate = AttributeTranslator.dateFromString("20991231", "yyyyMMdd");
				specialBillDetailsInfo.invSuppressionInd = AttributeTranslator.byteFromString(InvoiceSuppressionLevel.SUPPRESS_ALL);
				amdocsUpdateBanConv.setBillDetailsInfo(specialBillDetailsInfo);

				// save bill suppression
				LOGGER.debug("Excecuting updateBillSuppression() - start...");
				amdocsUpdateBanConv.saveBan();
				LOGGER.debug("Excecuting updateBillSuppression() - end...");

				return null;
			}
		});		

	}

	@Override
	public void updateReturnEnvelopeIndicator(final int ban,
			final boolean returnEnvelopeRequested, String sessionId)
	throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				
				SpecialBillDetailsInfo specialBillDetailsInfo = new SpecialBillDetailsInfo();

				// Set BanPK (which also retrieves the BAN)
				amdocsUpdateBanConv.setBanPK(ban);

				// Set Return Envelope Indicator
				specialBillDetailsInfo.csRetEnvlpInd = returnEnvelopeRequested ? (byte) 'Y'
						: (byte) 'N';

				// update Return Envelope Indicator (if changed)
				if (specialBillDetailsInfo.csRetEnvlpInd != amdocsUpdateBanConv
						.getBillDetailsInfo().csRetEnvlpInd) {

					amdocsUpdateBanConv.setBillDetailsInfo(specialBillDetailsInfo);
					LOGGER.debug("Excecuting saveBan() - start...");
					amdocsUpdateBanConv.saveBan();
					LOGGER.debug("Excecuting saveBan() - end...");
				} else {
					LOGGER.debug("specialBillDetailsInfo.csRetEnvlpInd:"
							+ specialBillDetailsInfo.csRetEnvlpInd);
					LOGGER.debug("amdocsUpdateBanConv.getBillDetailsInfo().csRetEnvlpInd:"
							+ amdocsUpdateBanConv.getBillDetailsInfo().csRetEnvlpInd);
					LOGGER.debug("Return Envelope Indicator has not changed - no update performed!");
				}

				return null;
			}
		});

	}

	@Override
	public void hasEPostFalseNotificationTypeNotEPost(final int ban, final long portalUserID,
			final BillNotificationContactInfo[] billNotificationContact,
			final String applicationCode){
		String call="{call portal_notification_pkg.saveBillNotificationDetails(?,?,?,?,?)}";
		getCodsJdbcTemplate().execute(call, new CallableStatementCallback<String>() {

			@Override
			public String doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {

				StructDescriptor contactDescriptor = StructDescriptor.createDescriptor("CONTACTS_O", callable.getConnection());
				Object[] contacts = new Object[billNotificationContact.length];
				for (int i = 0; i < contacts.length; i++) {

					Object[] attr = new Object[2];
					attr[0] = new Integer((billNotificationContact[i].getContactType()).equalsIgnoreCase(BillNotificationContact.CONTACT_TYPE_EMAIL) ? 5 : 1);
					attr[1] = billNotificationContact[i].getNotificationAddress();

					contacts[i] = new STRUCT(contactDescriptor, callable.getConnection(), attr);
				}			
				// create array descriptor
				ArrayDescriptor contactArrayDesc = ArrayDescriptor.createDescriptor("CONTACTS_T", callable.getConnection());
				ARRAY contactsArray = new ARRAY(contactArrayDesc, callable.getConnection(), contacts);
				callable.setInt(1, ban);
				callable.setLong(2, portalUserID);
				callable.setArray(3, contactsArray);
				callable.setString(4, E_BILL_STAGING_GP);
				callable.setString(5, applicationCode);

				callable.execute();
				return "";

			}
		});


	}
	
	@Override
	public void hasEPostFalseNotificationTypeEPost(final int ban, final long portalUserID,
			final BillNotificationContactInfo[] billNotificationContact,
			final String applicationCode){
		String call="{call portal_notification_pkg.createBillNotifiConDetForEPost(?,?,?,?,?,?,?,?,?,?)}";
		getCodsJdbcTemplate().execute(call, new CallableStatementCallback<String>() {

			@Override
			public String doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				BillNotificationContactInfo contactInfo = billNotificationContact[0];
				callable.setInt(1,ban);
				callable.setInt(2,BillNotificationHistoryRecord.ACTIVITY_TYPE_EPOST);
				callable.setInt(3,BillNotificationHistoryRecord.ACTIVITY_ACTION_REGISTRATION);
				callable.setInt(4,BillNotificationHistoryRecord.ACTIVITY_REASON_CUSTOMER_REQ);
				callable.setInt(5,BillNotificationHistoryRecord.ACTIVITY_TYPE_PAPER_INV_SUPPR);
				callable.setInt(6,BillNotificationHistoryRecord.ACTIVITY_ACTION_ACTIVATION);
				callable.setInt(7,BillNotificationHistoryRecord.ACTIVITY_REASON_EPOST_REGISTRATION);
				callable.setString(8,contactInfo.getBillNotificationRegistrationId());
				callable.setString(9,contactInfo.getNotificationAddress());
				callable.setLong(10, portalUserID);

				callable.execute();
				return "";
			}
		});
	}
	
	@Override
	public void hasEPostNotificationTypeNotEPost(final int ban, final long portalUserID,
			final BillNotificationContactInfo[] billNotificationContact,
			final String applicationCode){
		String call="{call portal_notification_pkg.cancelBillNotifiConDetForEPost(?,?,?,?,?,?,?,?)}";
		getCodsJdbcTemplate().execute(call, new CallableStatementCallback<String>() {

			@Override
			public String doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				BillNotificationContactInfo contactInfo = billNotificationContact[0];
				callable.setInt(1,ban);
				callable.setInt(2,BillNotificationHistoryRecord.ACTIVITY_TYPE_EPOST);
				callable.setInt(3,BillNotificationHistoryRecord.ACTIVITY_ACTION_CANCELLATION);
				callable.setInt(4,BillNotificationHistoryRecord.ACTIVITY_REASON_CUSTOMER_REQ);
				callable.setInt(5,BillNotificationHistoryRecord.ACTIVITY_TYPE_PAPER_INV_SUPPR);
				callable.setInt(6,BillNotificationHistoryRecord.ACTIVITY_ACTION_CANCELLATION);
				callable.setInt(7,BillNotificationHistoryRecord.ACTIVITY_REASON_EPOST_CANCELLATION);
				callable.setString(8,contactInfo.getBillNotificationRegistrationId());

				callable.execute();
				return "";
			}
		});


	}
	
	@Override
	public void hasEPostNotificationTypeEPost(final int ban, final long portalUserID,
			final BillNotificationContactInfo[] billNotificationContact,
			final String applicationCode){
		String call="{call portal_notification_pkg.updateBillNotifiConDetForEPost(?,?,?)}}";
		getCodsJdbcTemplate().execute(call, new CallableStatementCallback<String>() {

			@Override
			public String doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				BillNotificationContactInfo contactInfo = billNotificationContact[0];
				callable.setInt(1,ban);
				callable.setInt(2,contactInfo.isBillNotificationEnabled()?1:0);
				callable.setString(3,contactInfo.getNotificationAddress());
				callable.execute();
				return "";
			}
		});
	}
}
