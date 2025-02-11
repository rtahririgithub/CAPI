/**
 * 
 */
package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.cmb.account.informationhelper.dao.CollectionDao;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.eas.account.info.CollectionHistoryInfo;
import com.telus.eas.account.info.CollectionStepInfo;

/**
 * @author Canh Tran
 *
 */
public class CollectionDaoImpl extends MultipleJdbcDaoTemplateSupport implements CollectionDao {

	/* (non-Javadoc)
	 * @see com.telus.cmb.account.informationhelper.dao.CollectionDao#retrieveCollectionHistoryInfo(int, java.util.Date, java.util.Date)
	 */
	@Override
	public CollectionHistoryInfo[] retrieveCollectionHistoryInfo(final int banId,
			final Date fromDate, final Date toDate) throws ApplicationException {

		if (fromDate == null || toDate == null) {
			throw new ApplicationException(SystemCodes.CMB_AIH_DAO, "Inputs fromDate and toDate should not be null", "");
		}
		
		String call = "{ call ra_utility_pkg.getCollectionHistory(?, ?, ?, ?) }";
		
		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<CollectionHistoryInfo[]>() {
			@Override
			public CollectionHistoryInfo[] doInCallableStatement(
					CallableStatement callable) throws SQLException,
					DataAccessException {

			    List<CollectionHistoryInfo> list = new ArrayList<CollectionHistoryInfo>();
			    ResultSet result = null;
			    CollectionHistoryInfo history = null;
			    CollectionStepInfo step = null;				
				
				callable.setInt(1, banId);
				callable.setDate(2, new java.sql.Date(fromDate.getTime()));
				callable.setDate(3, new java.sql.Date(toDate.getTime()));
				callable.registerOutParameter(4, OracleTypes.CURSOR);

				callable.execute();

				try {
					result = (ResultSet)callable.getObject(4);

					while (result.next()) {
						history = new CollectionHistoryInfo();
						history.setActivityMode(result.getString("col_actv_type_ind"));
						history.setCollectorCode(result.getString("asgn_collector"));
						history.setCollectorName(result.getString("user_short_name"));
						history.setAgencyCode(result.getString("asgn_agency"));

						step = new CollectionStepInfo();
						step.setStep(result.getInt("col_step_num"));
						step.setCollectionActivityCode(result.getString("col_actv_code"));
						step.setTreatmentDate(result.getDate("col_actv_date"));
						step.setPath(result.getString("col_path_code"));

						history.setCollectionStepInfo(step);

						list.add(history);
					}
				} finally {
					if (result != null){
						result.close();
					}
				}

				return (CollectionHistoryInfo[])list.toArray(new CollectionHistoryInfo[list.size()]);
			}			
		});
	}
}
