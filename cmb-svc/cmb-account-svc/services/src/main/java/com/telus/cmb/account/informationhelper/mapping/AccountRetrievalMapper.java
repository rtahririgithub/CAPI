package com.telus.cmb.account.informationhelper.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.telus.cmb.common.util.DateUtil;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BankAccountInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;

public class AccountRetrievalMapper {
	private AccountInfo accountInfo;
	private ResultSet resultSet;
	private Date logicalDate;
	
	public AccountRetrievalMapper(AccountInfo accountInfo, ResultSet resultSet, Date logicalDate) {
		this.accountInfo = accountInfo;
		this.resultSet = resultSet;
		this.logicalDate = logicalDate;
	}
	
	public void map() throws SQLException {
		
	}
	
	public void setPaymentMethod(PaymentMethodInfo paymentMethodInfo) throws SQLException {
		paymentMethodInfo.setPaymentMethod(resultSet.getString("auto_gen_pym_type"));
		String returnEnvelopeIndicator = resultSet.getString("cs_ret_envlp_ind");
		if ("N".equals(returnEnvelopeIndicator)) {
			paymentMethodInfo.setSuppressReturnEnvelope(true);
		}else {
			paymentMethodInfo.setSuppressReturnEnvelope(false);
		}
		paymentMethodInfo.setStatus(resultSet.getString("dd_status"));
		paymentMethodInfo.setStatusReason(resultSet.getString("status_reason"));
		setPaymentMethodCreditCard(paymentMethodInfo.getCreditCard0());
		setBankInfo(paymentMethodInfo.getCheque0().getBankAccount0());
	}
	
	private void setPaymentMethodCreditCard(CreditCardInfo creditCardInfo) throws SQLException {
		creditCardInfo.setToken(resultSet.getString("credit_card_no"));
		creditCardInfo.setLeadingDisplayDigits(resultSet.getString("pymt_card_first_six_str"));
		creditCardInfo.setTrailingDisplayDigits(resultSet.getString("pymt_card_last_four_str"));
		Date expiryDate = resultSet.getDate("expiration_date");
		if (expiryDate != null) {
			creditCardInfo.setExpiryMonth(expiryDate.getMonth()+1);
			creditCardInfo.setExpiryYear(expiryDate.getYear()+1900);
		}
		creditCardInfo.setHolderName(resultSet.getString("card_mem_hold_nm"));
	}
	
	private void setBankInfo(BankAccountInfo bankAccountInfo) throws SQLException {
		bankAccountInfo.setBankCode(resultSet.getString("bnk_code"));
		bankAccountInfo.setBankAccountNumber(resultSet.getString("bnk_acct_number"));
		bankAccountInfo.setBankBranchNumber(resultSet.getString("bnk_branch_number"));
		bankAccountInfo.setBankAccountType(resultSet.getString("bnk_acct_type"));
	}
	
	public void setPersonalCreditInfo(PersonalCreditInfo creditInfo) throws SQLException {
		creditInfo.setBirthDate(resultSet.getDate("birth_date"));
		creditInfo.setSin(resultSet.getString("customer_ssn"));
		creditInfo.setDriversLicense(resultSet.getString("drivr_licns_no"));
		creditInfo.setDriversLicenseProvince(resultSet.getString("drivr_licns_state"));
		creditInfo.setDriversLicenseExpiry(resultSet.getTimestamp("drivr_licns_exp_dt"));
		setPersonalCreditInfoCreditCard(creditInfo.getCreditCard0());
	}
	
	private void setPersonalCreditInfoCreditCard (CreditCardInfo creditCardInfo) throws SQLException {
		creditCardInfo.setToken(resultSet.getString("gur_cr_card_no"));
		creditCardInfo.setLeadingDisplayDigits(resultSet.getString("gur_pymt_card_first_six_str"));
		creditCardInfo.setTrailingDisplayDigits(resultSet.getString("gur_pymt_card_last_four_str"));
		Date expiryDate = resultSet.getDate("gur_cr_card_exp_dt");
		if (expiryDate != null) {
			creditCardInfo.setExpiryMonth(expiryDate.getMonth()+1);
			creditCardInfo.setExpiryYear(expiryDate.getYear()+1900);
		}
	}
	
	public void setConsumerNameInfo (ConsumerNameInfo consumerNameInfo) throws SQLException {
		consumerNameInfo.setFirstName(resultSet.getString("first_name"));
		consumerNameInfo.setLastName(resultSet.getString("last_business_name"));
		consumerNameInfo.setMiddleInitial(resultSet.getString("middle_initial"));
		consumerNameInfo.setTitle(resultSet.getString("name_title"));
		consumerNameInfo.setGeneration(resultSet.getString("name_suffix"));
		consumerNameInfo.setAdditionalLine(resultSet.getString("additional_title"));
	}
	
	public void setPrepaidSpecificInfo() throws SQLException {
		((PrepaidConsumerAccountInfo)accountInfo).setBirthDate(resultSet.getDate("birth_date"));

		// set Top Up Credit Card
		if ("C ".equals(resultSet.getString("dd_status")) == false) {
			setPaymentMethodCreditCard(((PrepaidConsumerAccountInfo)accountInfo).getTopUpCreditCard0());
		}
	}
	
	public void setBusinessDetails() throws SQLException {
		// set Name
		((PostpaidBusinessRegularAccountInfo)accountInfo).setLegalBusinessName(resultSet.getString("last_business_name"));

		// set Business Credit Info
		((PostpaidBusinessRegularAccountInfo)accountInfo).getCreditInformation0().setIncorporationNumber(resultSet.getString("incorporation_no"));
		((PostpaidBusinessRegularAccountInfo)accountInfo).getCreditInformation0().setIncorporationDate(resultSet.getTimestamp("incorporation_date"));
	}
	
	public void setCommonDetails() throws SQLException {
		
		accountInfo.setBrandId(resultSet.getInt("brand_ind"));
		accountInfo.setStatus(resultSet.getString("ban_status").charAt(0));
		accountInfo.setStatusActivityCode(resultSet.getString("status_actv_code"));
		accountInfo.setStatusActivityReasonCode(resultSet.getString("status_actv_rsn_code"));
		Date startServiceDate = resultSet.getTimestamp("start_service_date");
		
		if (startServiceDate != null) {
			accountInfo.setCreateDate(startServiceDate);
		}else {
			accountInfo.setCreateDate(resultSet.getTimestamp("status_last_date"));
		}
		accountInfo.setStartServiceDate(startServiceDate);
		accountInfo.setDealerCode(resultSet.getString("dealer_code"));
		accountInfo.setSalesRepCode(resultSet.getString("sales_rep_code"));
		accountInfo.setBillCycle(resultSet.getInt("cycle_code"));
		accountInfo.setBillCycleCloseDay(resultSet.getInt("cycle_close_day"));
		accountInfo.setPin(resultSet.getString("acc_password"));
		accountInfo.setCustomerId(resultSet.getInt("customer_id"));
		accountInfo.setLanguage(resultSet.getString("lang_pref"));
		accountInfo.setEmail(resultSet.getString("email_address"));
		
		if ("Y".equals(resultSet.getString("hot_line_ind"))) {
			accountInfo.setHotlined(true);
		}else {
			accountInfo.setHotlined(false);
		}
		if (resultSet.getString("hierarchy_id") == null || "N".equals(resultSet.getString("hierarchy_id"))) {
			accountInfo.setCorporateHierarchy(false);
		}else {
			accountInfo.setCorporateHierarchy(true);
		}

		if ( accountInfo.isCorporateHierarchy() ) {
			accountInfo.setHierarchyId( resultSet.getInt("hierarchy_id") );
		}
		accountInfo.setCorporateAccountRepCode(resultSet.getString("cs_ca_rep_id") == null ? "" : resultSet.getString("cs_ca_rep_id"));
		accountInfo.setBanSegment(resultSet.getString("gl_segment"));
		accountInfo.setBanSubSegment(resultSet.getString("gl_subsegment"));

		// note: accountInfo.setAdditionalLine should always be set to the same value as accountInfo.getName0().setAdditionalLine
		// for PostpaidConsumer, PostpaidBusinessPersonal and PrepaidConsumer accounts
		accountInfo.setAdditionalLine(resultSet.getString("additional_title"));
		accountInfo.setStatusDate(resultSet.getTimestamp("status_last_date"));
		accountInfo.setFidoConversion(AccountInfo.ACCOUNT_CONV_RUN_NO_FIDO == resultSet.getShort("conv_run_no") ? true : false);
		
		setTaxInfo();

		accountInfo.setHomeProvince(resultSet.getString("home_province"));
		accountInfo.setAccountCategory(resultSet.getString("national_account"));
		accountInfo.setNextBillCycle(resultSet.getInt("next_bill_cycle"));
		accountInfo.setNextBillCycleCloseDay(resultSet.getInt("next_bill_cycle_close_day"));
		accountInfo.setVerifiedDate(resultSet.getTimestamp("verified_date"));
		if ("Y".equals(resultSet.getString("cs_handle_by_ctn_ind"))) {
			accountInfo.setHandledBySubscriberOnly(true);
		}else {
			accountInfo.setHandledBySubscriberOnly(false);
		}
		accountInfo.setCorporateId(resultSet.getString("corporate_id"));

		setInvoiceProperties();
		setAddress();
		setPhoneAndFax();

		setFinancialHistory();
		setDebtSummary();
	}
	
	private void setTaxInfo() throws SQLException {
		String gstExemptInd = resultSet.getString("tax_gst_exmp_ind");

		if (gstExemptInd != null && !gstExemptInd.trim().equals("")) {
			accountInfo.setGstExempt(gstExemptInd.trim().equals("Y") ? (byte)'Y' : (byte)'N');
		}
		
		String pstExemptInd = resultSet.getString("tax_pst_exmp_ind");
		
		if (pstExemptInd != null && !pstExemptInd.trim().equals("")) {
			accountInfo.setPstExempt(pstExemptInd.trim().equals("Y") ? (byte)'Y' : (byte)'N');
		}
		
		String hstExemptInd = resultSet.getString("tax_hst_exmp_ind");
		if (hstExemptInd != null && !hstExemptInd.trim().equals("")) {
			accountInfo.setHstExempt(hstExemptInd.trim().equals("Y") ? (byte)'Y' : (byte)'N');
		}

		accountInfo.setGSTExemptExpiryDate(resultSet.getTimestamp("tax_gst_exmp_exp_dt"));
		accountInfo.setPSTExemptExpiryDate(resultSet.getTimestamp("tax_pst_exmp_exp_dt"));
		accountInfo.setHSTExemptExpiryDate(resultSet.getTimestamp("tax_hst_exmp_exp_dt"));
		accountInfo.setGSTCertificateNumber(resultSet.getString("tax_gst_exmp_rf_no"));
		accountInfo.setPSTCertificateNumber(resultSet.getString("tax_pst_exmp_rf_no"));
		accountInfo.setHSTCertificateNumber(resultSet.getString("tax_hst_exmp_rf_no"));
		accountInfo.setGSTExemptEffectiveDate(resultSet.getTimestamp("tax_gst_exmp_eff_dt"));
		accountInfo.setPSTExemptEffectiveDate(resultSet.getTimestamp("tax_pst_exmp_eff_dt"));
		accountInfo.setHSTExemptEffectiveDate(resultSet.getTimestamp("tax_hst_exmp_eff_dt"));
	}
	
	private void setInvoiceProperties() throws SQLException {
		// set invoice properties - start
		InvoicePropertiesInfo invoiceProperties = new InvoicePropertiesInfo();
		invoiceProperties.setBan(resultSet.getInt("ban"));
		invoiceProperties.setInvoiceSuppressionLevel(resultSet.getString("inv_suppression_ind"));
		invoiceProperties.setHoldRedirectDestinationCode(String.valueOf(resultSet.getInt("bl_man_hndl_req_opid")));
		invoiceProperties.setHoldRedirectFromDate(resultSet.getTimestamp("bl_man_hndl_eff_date"));
		invoiceProperties.setHoldRedirectToDate(resultSet.getTimestamp("bl_man_hndl_exp_date"));
		accountInfo.setInvoiceProperties(invoiceProperties);
	}
	
	private void setAddress() throws SQLException {
		accountInfo.getAddress0().setPrimaryLine(resultSet.getString("adr_primary_ln"));
		accountInfo.getAddress0().setCity(resultSet.getString("adr_city"));
		accountInfo.getAddress0().setProvince(resultSet.getString("adr_province"));
		accountInfo.getAddress0().setPostalCode(resultSet.getString("adr_postal"));
		accountInfo.getAddress0().setSecondaryLine(resultSet.getString("adr_secondary_ln"));
		accountInfo.getAddress0().setCountry(resultSet.getString("adr_country"));
		accountInfo.getAddress0().setZipGeoCode(resultSet.getString("adr_zip_geo_code"));
		accountInfo.getAddress0().setForeignState(resultSet.getString("adr_state_code"));
		accountInfo.getAddress0().setCivicNo(resultSet.getString("civic_no"));
		accountInfo.getAddress0().setCivicNoSuffix(resultSet.getString("civic_no_suffix"));
		accountInfo.getAddress0().setStreetDirection(resultSet.getString("adr_st_direction"));
		accountInfo.getAddress0().setStreetName(resultSet.getString("adr_street_name"));
		accountInfo.getAddress0().setStreetType(resultSet.getString("adr_street_type"));
		accountInfo.getAddress0().setRrDesignator(resultSet.getString("adr_designator"));
		accountInfo.getAddress0().setRrIdentifier(resultSet.getString("adr_identifier"));
		accountInfo.getAddress0().setPoBox(resultSet.getString("adr_box"));
		accountInfo.getAddress0().setUnitDesignator(resultSet.getString("unit_designator"));
		accountInfo.getAddress0().setUnitIdentifier(resultSet.getString("unit_identifier"));
		accountInfo.getAddress0().setRrAreaNumber(resultSet.getString("adr_area_nm"));
		accountInfo.getAddress0().setRrQualifier(resultSet.getString("adr_qualifier"));
		accountInfo.getAddress0().setRrSite(resultSet.getString("adr_site"));
		accountInfo.getAddress0().setRrCompartment(resultSet.getString("adr_compartment"));
		accountInfo.getAddress0().setAttention(resultSet.getString("adr_attention"));
		accountInfo.getAddress0().setRrDeliveryType(resultSet.getString("adr_delivery_tp"));
		accountInfo.getAddress0().setRrGroup(resultSet.getString("adr_group"));
		accountInfo.getAddress0().setAddressType(resultSet.getString("adr_type"));
	}
	
	private void setPhoneAndFax() throws SQLException {
		//set contact phone
		accountInfo.setContactPhone(resultSet.getString("contact_telno"));
		accountInfo.setContactPhoneExtension(resultSet.getString("contact_tn_extno") == null ? null : resultSet.getString("contact_tn_extno").trim());
		accountInfo.setContactFax(resultSet.getString("contact_faxno"));

		// set Home and Business phones
		accountInfo.setHomePhone(resultSet.getString("home_telno"));
		accountInfo.setBusinessPhone(resultSet.getString("work_telno"));
		accountInfo.setBusinessPhoneExtension(resultSet.getString("work_tn_extno") == null ? null : resultSet.getString("work_tn_extno").trim());

		//set Other Phone
		accountInfo.setOtherPhone(resultSet.getString("other_telno"));
		accountInfo.setOtherPhoneExtension(resultSet.getString("other_extno") == null ? null : resultSet.getString("other_extno").trim());
		accountInfo.setOtherPhoneType(resultSet.getString("other_tn_type"));
	}
	
	private void setFinancialHistory() throws SQLException {	
		// set Financial History
		accountInfo.getFinancialHistory0().setDelinquent("D".equals(resultSet.getString("col_delinq_status")));
		accountInfo.getFinancialHistory0().setWrittenOff(resultSet.getString("wo_ind").equals("W") ? true : false);

		accountInfo.getFinancialHistory0().setHotlined("Y".equals(resultSet.getString("hot_line_ind")));

		setCollectionInfo();
	}

	
	private void setCollectionInfo() throws SQLException {
		accountInfo.getFinancialHistory0().setCollectionAgency(resultSet.getString("col_agncy_code"));
		accountInfo.getFinancialHistory0().getCollectionStep0().setPath(resultSet.getString("col_path_code"));
		accountInfo.getFinancialHistory0().getNextCollectionStep0().setPath(resultSet.getString("col_path_code"));
		accountInfo.getFinancialHistory0().getNextCollectionStep0().setStep(resultSet.getInt("col_next_step_no"));
		accountInfo.getFinancialHistory0().getNextCollectionStep0().setTreatmentDate(resultSet.getDate("col_next_step_date"));
		accountInfo.getFinancialHistory0().setHotlinedDate(resultSet.getDate("last_act_hotline"));
		accountInfo.getFinancialHistory0().setDelinquentDate(resultSet.getDate("col_delinq_sts_date"));
	}
	
	private void setDebtSummary() throws SQLException {
		Date collectionStatusEffDate = resultSet.getDate("cs_effective_date");
		double currentDue = 0;
		double pastDue1to30Days = 0;
		double pastDue31to60Days = 0;
		double pastDue61to90Days = 0;
		double pastDueOver90Days = 0;
		double pastDueAmount = 0;
		
		if (DateUtil.isSameDay(collectionStatusEffDate, logicalDate)) {
			currentDue = resultSet.getDouble("cu_age_bucket_0");
			pastDue1to30Days = resultSet.getDouble("cu_age_bucket_1_30");
			pastDue31to60Days = resultSet.getDouble("cu_age_bucket_31_60");
			pastDue61to90Days = resultSet.getDouble("cu_age_bucket_61_90");
			pastDueOver90Days = resultSet.getDouble("cu_age_bucket_91_plus");
			pastDueAmount = resultSet.getDouble("cu_past_due_amt");
		}else if (DateUtil.isSameDay(DateUtil.addDay(collectionStatusEffDate, 1), logicalDate)) {
			currentDue = resultSet.getDouble("nx_age_bucket_0");
			pastDue1to30Days = resultSet.getDouble("nx_age_bucket_1_30");
			pastDue31to60Days = resultSet.getDouble("nx_age_bucket_31_60");
			pastDue61to90Days = resultSet.getDouble("nx_age_bucket_61_90");
			pastDueOver90Days = resultSet.getDouble("nx_age_bucket_91_plus");
			pastDueAmount = resultSet.getDouble("nx_past_due_amt");
		}else {
			currentDue = resultSet.getDouble("fu_age_bucket_0");
			pastDue1to30Days = resultSet.getDouble("fu_age_bucket_1_30");
			pastDue31to60Days = resultSet.getDouble("fu_age_bucket_31_60");
			pastDue61to90Days = resultSet.getDouble("fu_age_bucket_61_90");
			pastDueOver90Days = resultSet.getDouble("fu_age_bucket_91_plus");
			pastDueAmount = resultSet.getDouble("fu_past_due_amt");
		}
		// no Past Due Payments
		if (currentDue == 0 && pastDueAmount == 0) {
			accountInfo.getFinancialHistory0().getDebtSummary0().setCurrentDue(resultSet.getDouble("ar_balance"));
			accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue(0);
		} else {
			accountInfo.getFinancialHistory0().getDebtSummary0().setCurrentDue(currentDue);
			accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue(pastDueAmount);
		}
		accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue1to30Days(pastDue1to30Days);
		accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue31to60Days(pastDue31to60Days);
		accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue61to90Days(pastDue61to90Days);
		accountInfo.getFinancialHistory0().getDebtSummary0().setPastDueOver90Days(pastDueOver90Days);
		
		/*
		 *  Naresh Annabathula - below setter added for notification project payment notification requirements October 2016 ,as Business want to see the real time balance in the template other than current due.
		   This is only being used for notification call for now.*/
		
		
		accountInfo.getFinancialHistory0().getDebtSummary0().setAccountRealTimeBalance(resultSet.getDouble("ar_balance"));
		
		
	}
}
