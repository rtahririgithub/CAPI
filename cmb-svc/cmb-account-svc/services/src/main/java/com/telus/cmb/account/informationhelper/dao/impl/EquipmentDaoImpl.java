package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import oracle.jdbc.OracleTypes;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.telus.cmb.account.informationhelper.dao.EquipmentDao;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;

public class EquipmentDaoImpl extends MultipleJdbcDaoTemplateSupport implements EquipmentDao{

	@Override
	public String getUsimBySerialNumber(final String serialNumber) {
		String callString = "{call Client_Equipment.getusimbyimei(?,?)}";
		try {
			return super.getDistJdbcTemplate().execute(callString, new CallableStatementCallback<String>() {

				@Override
				public String doInCallableStatement(CallableStatement callable)
				throws SQLException, DataAccessException {
					callable.setString(1, serialNumber);
					callable.registerOutParameter(2, Types.VARCHAR);

					callable. execute();

					return callable.getString(2);
				}
			});
		} catch (UncategorizedSQLException e) {
			SQLException sqe = e.getSQLException();
			if (sqe != null) {
				if (sqe.getErrorCode() == 20324) {
					return null;
				}
			}
			throw e;
		}
	}

	@Override
	public String[] getImsisByUsim(final String usimId) {
		String callString = "{call Client_Equipment.getIMSIsByUSIM(?,?)}";
		
		try {
		return super.getDistJdbcTemplate().execute(callString, new CallableStatementCallback<String[]>() {

			@Override
			public String[] doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				Collection<String> result = new ArrayList<String>();
				ResultSet rs = null;

				try {
					callable.registerOutParameter(2, OracleTypes.CURSOR);
					callable.setString(1, usimId);

					callable. execute();

					rs = (ResultSet) callable.getObject(2);
					while (rs.next()) {

						result.add(rs.getString(1));
					}
				} finally {
					if (rs != null) {
						rs.close();
					}
				}
				return result.toArray(new String[result.size()]);
			}
			
		});
		} catch (UncategorizedSQLException e) {			
			SQLException sqe = e.getSQLException();
			if (sqe != null) {
				if (sqe.getErrorCode() == 20324) {
					return null;
				}
			}
			throw e;
		}
	}
}
