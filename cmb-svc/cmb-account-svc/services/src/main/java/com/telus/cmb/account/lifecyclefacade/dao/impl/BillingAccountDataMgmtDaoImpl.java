package com.telus.cmb.account.lifecyclefacade.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclefacade.dao.BillingAccountDataMgmtDao;
import com.telus.cmb.account.mapping.BillingAccountDataMgmtMapper;
import com.telus.cmb.account.mapping.BillingAccountDataMgmtMapper.BillingAccountMapper;
import com.telus.cmb.account.mapping.BillingAccountDataMgmtMapper.PayChannelMapper;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.ConsumerBillingAccountDataManagementServicePort;
import com.telus.eas.account.info.AccountInfo;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountdatamgmtsvcrequestresponse_v2.InsertBillingAccount;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountdatamgmtsvcrequestresponse_v2.InsertCustomerWithBillingAccount;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountdatamgmtsvcrequestresponse_v2.UpdateBillCycle;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountdatamgmtsvcrequestresponse_v2.UpdateBillingAccount;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountdatamgmtsvcrequestresponse_v2.UpdateBillingAccountStatus;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountdatamgmtsvcrequestresponse_v2.UpdatePayChannel;
import com.telus.tmi.xmlschema.xsd.customer.customer.customersubdomain_v3.Customer;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.customer_billing_sub_domain_v2.BillingAccount;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.customer_billing_sub_domain_v2.PayChannel;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

public class BillingAccountDataMgmtDaoImpl extends SoaBaseSvcClient implements BillingAccountDataMgmtDao {
	
	private static final long KB_MASTER_SOURCE = 130L;
	
	@Autowired
	private ConsumerBillingAccountDataManagementServicePort billingAccountManagementService;

	@Override
	public void insertBillingAccount(final AccountInfo accountInfo, final String customerID, final String userId, final String appId) throws ApplicationException {
		
		execute( new SoaCallback<Object>() {
			
			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {
				
				InsertBillingAccount request = new InsertBillingAccount();

				request.setAuditInfo(getAuditInfo(userId, appId));

				BillingAccountMapper billingAccountMapper = new BillingAccountDataMgmtMapper.BillingAccountMapper();
				
				BillingAccount newBillingAccount = billingAccountMapper.mapToSchema(accountInfo);
				newBillingAccount.setCustomerId(Long.valueOf(customerID).longValue());
				request.setNewBillingAccount(newBillingAccount);
				
				
				billingAccountManagementService.insertBillingAccount(request);
				
				return null;
			}
		});
	}

	@Override
	public void updateBillingAccount(final AccountInfo accountInfo, final String userId, final String appId) throws ApplicationException {
		
		execute( new SoaCallback<Object>() {
			
			@Override
			public Object doCallback() throws Exception {
				
				UpdateBillingAccount request = new UpdateBillingAccount();
				
				request.setAuditInfo(getAuditInfo(userId, appId));
				
				BillingAccountMapper billingAccountMapper = new BillingAccountDataMgmtMapper.BillingAccountMapper();
				BillingAccount billingAccount = billingAccountMapper.mapToSchema(accountInfo);
				request.setModifiedBillingAccount(billingAccount);
				
				billingAccountManagementService.updateBillingAccount(request);
				
				return null;
			}
		});
	}

	@Override
	public void insertCustomerWithBillingAccount(final AccountInfo accountInfo, final String userId, final String appId) throws ApplicationException  {
		
		execute( new SoaCallback<Object>() {
			
			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {

				InsertCustomerWithBillingAccount request = new InsertCustomerWithBillingAccount();
				
				request.setAuditInfo(getAuditInfo(userId, appId));
				
				BillingAccountMapper billingAccountMapper = new BillingAccountDataMgmtMapper.BillingAccountMapper();
				BillingAccount account = billingAccountMapper.mapToSchema(accountInfo);
				account.setCustomerId(0L);
				request.setNewAccount(account);
				
				Customer customer = new BillingAccountDataMgmtMapper.CustomerMapper().mapToSchema(accountInfo);
				request.setNewCustomer(customer);

				billingAccountManagementService.insertCustomerWithBillingAccount(request);
				
				return null;
			}
		});
	}

	@Override
	public void updatePayChannel(final AccountInfo accountInfo, final String userId, final String appId) throws ApplicationException  {

		execute(new SoaCallback<Object>() {

			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {

				UpdatePayChannel request = new UpdatePayChannel();
				
				request.setAuditInfo(getAuditInfo(userId, appId));
				
				PayChannelMapper channelMapper = new BillingAccountDataMgmtMapper.PayChannelMapper();
				PayChannel channel = channelMapper.mapToSchema(accountInfo);
				request.setModifiedPayChannel(channel);
				
				billingAccountManagementService.updatePayChannel(request);
				
				return null;
			}
		});
		
	}

	@Override
	public void updateBillingAccountStatus(final int billingAccountNumber, final String status, final String userId, final String appId) throws ApplicationException  {
		
		execute(new SoaCallback<Object>() {

			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {
				
				UpdateBillingAccountStatus request = new UpdateBillingAccountStatus();
				
				request.setAuditInfo(getAuditInfo(userId, appId));
				request.setBillingAccountNumber(String.valueOf(billingAccountNumber));
				request.setBillingMasterSourceId(KB_MASTER_SOURCE);
				request.setStatus(status);
				
				billingAccountManagementService.updateBillingAccountStatus(request);

				return null;
			}
		});
	}

	@Override
	public void updateBillingCycle(final int billingAccountNumber, final String newBillCycle, final String userId, final String appId) throws ApplicationException {
		
		execute(new SoaCallback<Object>() {

			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public Object doCallback() throws Exception {

				UpdateBillCycle request = new UpdateBillCycle();
				request.setAuditInfo(getAuditInfo(userId, appId));
				
				request.setBillingAccountNumber(String.valueOf(billingAccountNumber));
				request.setBillingMasterSourceId(KB_MASTER_SOURCE);
				request.setNewBillCycleCode(newBillCycle);
				
				
				return billingAccountManagementService.updateBillCycle(request);
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
			public String doCallback() throws Exception {
				return billingAccountManagementService.ping( new Ping()).getVersion();
			}
			
		});
	}

	private AuditInfo getAuditInfo(String userId, String appId) {
		AuditInfo info = new AuditInfo();
		info.setUserId(userId);
		info.setOriginatorApplicationId(appId);
		return info;
	}
}
