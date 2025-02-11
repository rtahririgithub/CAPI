package com.telus.cmb.productequipment.manager.dao.impl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.util.TelusConstants;
import com.telus.cmb.productequipment.EquipmentErrorMessages;
import com.telus.cmb.productequipment.manager.dao.EquipmentManagerDao;

public class EquipmentManagerDaoImpl implements EquipmentManagerDao{
	
	private static final Logger LOGGER = Logger.getLogger(EquipmentManagerDaoImpl.class);
	static final String UNKNOWN_ANALOG_HANDSET_PRODUCT_ID = "10006000";
	static final String EXTERNAL_ANALOG_CHANNEL = "10385913";
	static final String EXTERNAL_ANALOG_OUTLET = "10378626";
	static final String PROVIDER_OWNER_ID  = "9";
	static final String UNKNOWN_PAGING_PRODUCT_ID = "10006003";
	static final String PAGER_PROVIDER_OWNER_ID  = "1";

	
	
	private JdbcTemplate knowbilityJdbcTemplate;
	private JdbcTemplate distJdbcTemplate;

	public JdbcTemplate getKnowbilityJdbcTemplate() {
		return knowbilityJdbcTemplate;
	}

	public void setKnowbilityJdbcTemplate(JdbcTemplate knowbilityJdbcTemplate) {
		this.knowbilityJdbcTemplate = knowbilityJdbcTemplate;
	}

	public JdbcTemplate getDistJdbcTemplate() {
		return distJdbcTemplate;
	}
	public void setDistJdbcTemplate(JdbcTemplate distJdbcTemplate) {
		this.distJdbcTemplate = distJdbcTemplate;
	}

	@Override
	public void insertAnalogEquipment(final String pSerialNo, final String pUserID)	throws ApplicationException {
		String queryStr = "INSERT INTO Analog_Equip" +
		"   (serial_no, seq_no, product_id, provider_owner_id, " +
		"    universal_product_cd, equipment_status_cd, equipment_condition_cd, " +
		"    receipt_dt, ship_dt,min_cd ,mdn_cd, product_status_cd, " +
		"    load_dt, update_dt, user_last_modify, " +
		"    imsi_no, card_id, custcode, customer_id) " +
		"    VALUES " +
		"   (?, 0, ?, ?, " +
		"    null, 'WORK', null, " +
		"    null, null, null,null, 'N', " +
		"    SYSDATE, SYSDATE, ?, " +
		"    null, null, null, null)";
		int rowInserted = getDistJdbcTemplate().execute(queryStr, new PreparedStatementCallback<Integer>() {

			@Override
			public Integer doInPreparedStatement(PreparedStatement statement) throws SQLException, DataAccessException {

				statement.setString(1, pSerialNo);
				statement.setString(2, UNKNOWN_ANALOG_HANDSET_PRODUCT_ID);
				statement.setString(3, PROVIDER_OWNER_ID);
				statement.setString(4, pUserID);

				return statement.executeUpdate();			

			}
		}); 

		if (rowInserted ==1 ){
			queryStr = "INSERT INTO channel_org_analog_equip " +
			"   (serial_no, seq_no, channel_org_equip_type_id, effective_dt, " +
			"    chnl_org_id, outlet_id, load_dt, update_dt, user_last_modify) " +
			"VALUES " +
			"   (?, 0, 2, SYSDATE, " +
			"    ?, ?, " +
			"    SYSDATE, SYSDATE, ?) " ;

			getDistJdbcTemplate().execute(queryStr, new PreparedStatementCallback<Object>() {

				@Override
				public Object doInPreparedStatement(PreparedStatement statement)	throws SQLException, DataAccessException {
					statement.setString(1, pSerialNo);
					statement.setString(2, EXTERNAL_ANALOG_CHANNEL);
					statement.setString(3, EXTERNAL_ANALOG_OUTLET);
					statement.setString(4, pUserID);
					statement.executeUpdate();
					return null;
				}
			});
		}
	}

	
	@Override
	public String getMasterLockbySerialNo(final String pSerialNo, final String pUserID,
			final long pLockReasonID) throws ApplicationException {
		
		String call = "{call Client_Equipment.GetMasterLockNumber(?,?,?,?)}";

		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<String>() {

			@Override
			public String doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				String masterLockNo="";
				
				callable.setString(1, pSerialNo);
		        callable.setString(2, pUserID);
		        callable.setLong(3,pLockReasonID);
		        callable.registerOutParameter(4,Types.VARCHAR);
		        callable. execute();

		        masterLockNo=callable.getString(4);
				return masterLockNo;
			}
		}); 

	}

	@Override
	public String getMasterLockbySerialNo(final String pSerialNo, final String pUserID,
			final long pLockReasonID, final long pOutletID, final long pChnlOrgID)
			throws ApplicationException {
		
		String call = "{call Client_Equipment.GetMasterLockNumber(?,?,?,?,?,?)}";

		return getDistJdbcTemplate().execute(call, new CallableStatementCallback<String>() {

			@Override
			public String doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				String masterLockNo="";
				
				callable.setString(1, pSerialNo);
		        callable.setString(2, pUserID);
		        callable.setLong(3, pLockReasonID);
		        callable.setLong(4, pOutletID);
		        callable.setLong(5, pChnlOrgID);
		        callable.registerOutParameter(6,Types.VARCHAR);

		        callable. execute();

		        masterLockNo=callable.getString(6);
				return masterLockNo;
			}
		}); 

	}

	@Override
	public void insertPagerEquipment(final String pSerialNo, final String pCapCode,
			final String pEncoderFormat, final String pFrequencyCode, final String pModelType,
			final String pUserID) throws ApplicationException {
		
		String query = "INSERT INTO Paging_Equip" +
				        "   (serial_no, seq_no, product_id, provider_owner_id, " +
				        "    universal_product_cd, equipment_status_cd, equipment_condition_cd, " +
				        "    receipt_dt, ship_dt, min_cd, product_status_cd, " +
				        "    load_dt, update_dt, user_last_modify, " +
				        "    cap_code, frequency_id, paging_product_id, customer_id, initial_purchase_cost) " +
				        "    VALUES " +
				        "   (UPPER(?), 0, ?, ?, " +
				        "    null, 'WORK', null, " +
				        "    null, null, null, 'N', " +
				        "    SYSDATE, SYSDATE, ?, " +
				        "    ?, ?, ?, null, 0)";
			
		final long  pagingProductId = getPagerProductId (UNKNOWN_PAGING_PRODUCT_ID, pEncoderFormat, pModelType);
		final long  pagingFrequencyId = getPagerFrequencyIdByFrequencyCode (pFrequencyCode);
		
		int rowInserted = getDistJdbcTemplate().execute(query, new PreparedStatementCallback<Integer>() {

			@Override
			public Integer doInPreparedStatement(PreparedStatement statement) throws SQLException, DataAccessException {
				
				   statement.setString(1, pSerialNo);
			       statement.setString(2, UNKNOWN_PAGING_PRODUCT_ID);
			       statement.setString(3, PAGER_PROVIDER_OWNER_ID);
			       statement.setString(4, pUserID);
			       statement.setString(5, pCapCode);
			       statement.setLong(6, pagingFrequencyId);
			       statement.setLong(7, pagingProductId);

			       return statement.executeUpdate();
			}
		}); 

		if (rowInserted ==1 ){
			  query = "INSERT INTO paging_equip_status " +
		              "   (serial_no, seq_no, equipment_status_type_id, equipment_status_id, " +
		              "    equipment_status_dt, load_dt, update_dt, user_last_modify) " +
		              "    VALUES " +
		              "   (UPPER(?), 0, 2, 7, SYSDATE, SYSDATE, SYSDATE, ?)";

			getDistJdbcTemplate().execute(query, new PreparedStatementCallback<Object>() {

			 @Override
			 public Object doInPreparedStatement(PreparedStatement statement)throws SQLException, DataAccessException {
				 
			 	statement.setString(1, pSerialNo);
		        statement.setString(2, pUserID);
	
		        statement.executeUpdate();
		        return null;
			}
		});
	   }
		
	}
	
	
	
	  private long getPagerProductId (final String pProductId, final String pEncoderFormat, final String pModelType)
	     throws ApplicationException
	  {
		  String queryStr = "select paging_product_id from paging_product " +
		      "where product_id = ? and encoder_format_cd = ? " +
		      "and paging_model_type_cd = ? and model_year = 2003 " +
		      "and baud_rate = 0 order by load_dt, update_dt desc " ;
		  
		  try{
		  return getDistJdbcTemplate().execute(queryStr, new PreparedStatementCallback<Long>() {

				@Override
				public Long doInPreparedStatement(PreparedStatement statement)	throws SQLException, DataAccessException {
					
					ResultSet result = null;
					long pagingProductId = 0;
					try{
						statement.setString(1, UNKNOWN_PAGING_PRODUCT_ID );
					    statement.setString(2, pEncoderFormat );
					    statement.setString(3, pModelType );
					    
					    result = statement.executeQuery();
					    
					    if (result!=null && result.next())
					     {
					        pagingProductId = result.getLong(1);
					     }
					} finally {
						if (result != null) {
							result.close();
						}
					}

				    return pagingProductId;
				}
			});
		  }catch(Exception e){
			  throw new ApplicationException(SystemCodes.CMB_PEM_DAO, ErrorCodes.UNKNOWN_PAGING_PRODUCT, EquipmentErrorMessages.UNKNOWN_PAGING_PRODUCT_EN,"");
		  }
		  
	  }
	  

	  private long getPagerFrequencyIdByFrequencyCode (final String pFrequencyCode)
		     throws ApplicationException
		  {
			  String queryStr = "select frequency_id from paging_frequency where frequency_cd = ?" ;
			  try{
			  return getDistJdbcTemplate().execute(queryStr, new PreparedStatementCallback<Long>() {

					@Override
					public Long doInPreparedStatement(PreparedStatement statement)	throws SQLException, DataAccessException {
						
						ResultSet result = null;
						long pagingFrequencyId = 0;
						try{
							 statement.setString(1, pFrequencyCode );
						     result = statement.executeQuery();
						     if (result!=null && result.next())
						     {
						        pagingFrequencyId = result.getLong(1);
						     }
						} finally {
							if (result != null) {
								result.close();
							}
						}

					    return pagingFrequencyId;
					}
				});
			  }catch(Exception e){
				  throw new ApplicationException(SystemCodes.CMB_PEM_DAO, ErrorCodes.UNKNOWN_FREQUENCY_CODE,
						  EquipmentErrorMessages.UNKNOWN_FREQUENCY_CODE_EN,"" );
			  }
			  
		  }

	@Override
	public void updateStatus(final String pSerialNo, final String pUserID,
			final long pEquipmentStatusTypeID, final long pEquipmentStatusID,
			final String pTechType, final long pProductClassID) throws ApplicationException {
		
		 String call = "{call Client_Equipment.ChangeEquipmentStatus (?,?,?,?,?,?)}";
		
		 getDistJdbcTemplate().execute(call, new CallableStatementCallback<Object>() {
			
			@Override
			public Object doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				
				    callable.setString(1, pSerialNo);
			        callable.setString(2, pUserID);
			        callable.setLong(3,pEquipmentStatusTypeID);
			        callable.setLong(4,pEquipmentStatusID);
			        callable.setString(5,pTechType );
			        callable.setLong(6,pProductClassID);

			        callable. execute();

				return null;
			}
		}); 

		
	}
	
	@Override
	public void setSIMMule( String sim,  String mule,  Date activationDate,
			 String eventType) throws ApplicationException {
		
		String input =
		      "\n sim :- " + sim +
		      "\n mule :- " + mule +
		      "\n activationDate :- " + activationDate +
		      "\n eventType :- " + eventType ;
		
		 if (mule == null || mule.trim().equals(""))
		    {
		      mule = getReservedMule(sim);
		      if (mule == null || mule.trim().equals(""))
		      {
		        LOGGER.debug( input + "\n Cannot get reserved Mule, return" );
		        return;
		      }
		      input = input + "\n determined reserved mule :- " + mule;
		    }
		
		  if (activationDate == null){
		      activationDate = new java.util.Date();
		  }
		  
		  LOGGER.debug(input);
		  
		  int seq_no =0;
		  if (eventType.equals(TelusConstants.SIM_IMEI_ACTIVATE))
		  {
		      seq_no = 2;
		  }
		  else if (eventType.equals(TelusConstants.SIM_IMEI_RESERVED))
		  {
		      seq_no = 3;
		  }
		  
		  insertUpdateData(sim,mule, seq_no, activationDate, eventType);
		  
		  // Now update the current row , seq_no = 0;
		  seq_no = 0;
		  
		  insertUpdateData(sim,mule, seq_no, activationDate, eventType);
	      
	}
	
	private void insertUpdateData( String sim,  String mule,  int seq_no,  Date activationDate,
			 String eventType) throws ApplicationException{
		
		int rowsAffected;
		rowsAffected = insertData(sim,mule, seq_no, activationDate, eventType);
	      
	      if (rowsAffected == 1)
	      {
	        LOGGER.debug("inserted seq_no = "+ seq_no);
	      } else
	      {
	    	  rowsAffected = updateData(sim,mule, seq_no, activationDate, eventType);
	    	  
	    	  if (rowsAffected == 1)
		      {
		        LOGGER.debug("updated seq_no = "+ seq_no);
		      }
	      }
	      if (rowsAffected != 1 )
	      {
	    	  throw new ApplicationException(SystemCodes.CMB_PEM_DAO,  "activateSIMMule failed to insert/update seq_no = 0 ","");
	      }
	}
	
	private int insertData(final String sim, final String mule, final int seq_no, final Date activationDate,
			final String eventType) throws ApplicationException {
		
		String insertString =   "insert into sim_imei (sim_id, sim_seq_no, imei_serial_no, " +
		      "imei_seq_no, seq_no, sim_imei_ass_dt, load_dt, update_dt, " +
		      "user_last_modify, event_type) " +
		      "select ? , 0 , ? , 0 , ? , ? , sysdate, sysdate, " +
		      "user, ?  from dual where not exists (select 1 from sim_imei " +
		      "where sim_id = ? and sim_seq_no = 0 " +
		      "and imei_serial_no = ? and imei_seq_no = 0 and seq_no = ? )" ;

		 return getDistJdbcTemplate().execute(insertString, new PreparedStatementCallback<Integer>() {

				@Override
				public Integer doInPreparedStatement(PreparedStatement statement) throws SQLException, DataAccessException {
					
					  statement.setString(1, sim );
				      statement.setString(2, mule);
				      statement.setInt(3, seq_no);
				      //statement.setString(4, actDate);
				      statement.setTimestamp(4, new Timestamp(activationDate.getTime()));
				      statement.setString(5, eventType );
				      statement.setString(6, sim );
				      statement.setString(7, mule);
				      statement.setInt(8, seq_no);
				     return statement.executeUpdate();
				}
			}); 
	}
	
	
	private int updateData(final String sim, final String mule, final int seq_no, final Date activationDate,
			final String eventType) throws ApplicationException {
		
		 
		  String updateString =
		      "update sim_imei set sim_imei_ass_dt = ? , " +
		      "update_dt = sysdate, user_last_modify = user , event_type = ? " +
		      "where sim_id = ? and sim_seq_no = 0 " +
		      "and imei_serial_no = ? and imei_seq_no = 0 and seq_no = ? " ;
		  

		 return getDistJdbcTemplate().execute(updateString, new PreparedStatementCallback<Integer>() {

				@Override
				public Integer doInPreparedStatement(PreparedStatement statement) throws SQLException, DataAccessException {

					 statement.setTimestamp(1, new Timestamp(activationDate.getTime()));
				     statement.setString(2, eventType);
				     statement.setString(3, sim );
				     statement.setString(4, mule);
				     statement.setInt(5, seq_no);
				       
				     return statement.executeUpdate();
				}
			});
	}
	
	private String getReservedMule(final String sim) throws ApplicationException {
		 String queryStr = "select /*+ RULE */ imei_serial_no from sim_imei " +
		 					"where sim_id = ? and sim_seq_no = 0 and imei_seq_no = 0 " +
		 					"and seq_no = 3 order by sim_imei_ass_dt desc " ;

		return getDistJdbcTemplate().execute(queryStr, new PreparedStatementCallback<String>() {
		@Override
		public String doInPreparedStatement(PreparedStatement pstmt)
		throws SQLException, DataAccessException {
		
			String mule = null;
			ResultSet result = null;
	
			try{
				pstmt.setString(1, sim );
			    result = pstmt.executeQuery();
			    if (result.next())
			    {
			        mule = result.getString(1);
			    }
			} finally {
				if (result != null) {
					result.close();
				}
			}
			return mule;
		}
		});
	}
	
}
