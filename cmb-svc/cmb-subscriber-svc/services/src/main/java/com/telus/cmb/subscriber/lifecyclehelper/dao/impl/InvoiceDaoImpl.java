package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.subscriber.lifecyclehelper.dao.InvoiceDao;
import com.telus.eas.account.info.ChargeTypeTaxInfo;
import com.telus.eas.account.info.InvoiceTaxInfo;

public class InvoiceDaoImpl  extends MultipleJdbcDaoTemplateSupport implements InvoiceDao {

	@Override
	public InvoiceTaxInfo retrieveInvoiceTaxInfo(final int ban, final String subscriberId,
			final int billSeqNo) {
		String callString="{ call history_utility_pkg.retrieve_subscriber_taxes (?,?,?,?) }";
		return  super.getKnowbilityJdbcTemplate().execute(callString,new CallableStatementCallback<InvoiceTaxInfo>(){
			@Override
			public InvoiceTaxInfo doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{

				InvoiceTaxInfo tax = new InvoiceTaxInfo();
				ChargeTypeTaxInfo chargeTypeTax = null;
				List<ChargeTypeTaxInfo> tempList = new ArrayList<ChargeTypeTaxInfo>();

				ResultSet results = null;
				boolean loopFlag = false;


				try{
					callable.setInt(1, ban);
					callable.setString(2, subscriberId);
					callable.setInt(3, billSeqNo);
					callable.registerOutParameter(4, OracleTypes.CURSOR);

					callable.execute();

					results = (ResultSet)callable.getObject(4);

					while (results.next()) {
						chargeTypeTax = new ChargeTypeTaxInfo();
						chargeTypeTax.setChargeType(results.getString(1));
						chargeTypeTax.setChargeAmount(results.getDouble(2));
						chargeTypeTax.setGSTTaxAmount(results.getDouble(3));
						chargeTypeTax.setPSTTaxAmount(results.getDouble(4));
						chargeTypeTax.setHSTTaxAmount(results.getDouble(5));
						chargeTypeTax.setRoamingTaxAmount(results.getDouble(6));

						if (loopFlag == false) {

							tax.setGSTExempt( ("B".equalsIgnoreCase(results.getString(7)) ||
									"C".equalsIgnoreCase(results.getString(7))) ? true : false);
							tax.setPSTExempt( ("B".equalsIgnoreCase(results.getString(8)) ||
									"C".equalsIgnoreCase(results.getString(8))) ? true : false);
							tax.setHSTExempt( ("B".equalsIgnoreCase(results.getString(9)) ||
									"C".equalsIgnoreCase(results.getString(9))) ? true : false);
							tax.setRoamingExempt( ("B".equalsIgnoreCase(results.getString(10)) ||
									"C".equalsIgnoreCase(results.getString(10))) ? true : false);

							loopFlag = true;
						}

						chargeTypeTax.setAdjustedTaxAmount(results.getDouble(11));

						tempList.add(chargeTypeTax);
					}
					tax.setChargeTypeTaxes((ChargeTypeTaxInfo[])tempList.toArray(new ChargeTypeTaxInfo[tempList.size()]));
				}finally{
					if(results != null)
						results.close();
				}
				return tax;
			}
		});	
	}

}
