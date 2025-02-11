package com.telus.cmb.productequipment.helper.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.productequipment.EquipmentErrorMessages;
import com.telus.cmb.productequipment.helper.dao.CardHelperDao;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class CardHelperDaoImpl implements CardHelperDao{
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
	public int checkPINAttemps(final String pSerialNo) throws ApplicationException {
		
		 String pinAttemptsQuery = "select count(card_id) " +
		 							" from distadm.card_pin_access " +
							        " where card_id = ? " +
							        " and status_cd = 'Fail' " +
							        " and access_attempt_dt >= sysdate-(1/24) " ;
		
	   return getDistJdbcTemplate().execute(pinAttemptsQuery, new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement pstmt)
			throws SQLException, DataAccessException {
				ResultSet rset = null;
			    int failedPINAttempts = 0;
				
				
				try{
					
					 pstmt.setString(1,pSerialNo);
				     rset = pstmt.executeQuery();
					//check to make sure there is a record.  If no record is found, then an
				    //exception is thrown.  Otherwise, check the count on the one record
				    //to make sure it does not equal 3 or more
				     if (rset.next()) {
				        failedPINAttempts = rset.getInt(1);
				     } 
				} finally {
					if (rset != null) {
						rset.close();
					}
				}
				return failedPINAttempts;
			}
		});
	}


	@Override
	public CardInfo getCardInfobySerialNo(final String pSerialNo)
			throws ApplicationException {
		
		//define query here - find the card by the primary key which is the card_id
	    //the card_id field is really the serial number of the card (first 11 digits of the full card no)
	    //a) I am assured that there should not be more than 1 record in the product_family_group table
	    //   when we explicitly give the product_id and the product_gp_type_id for feature cards,
	    //   but who knows???  It is not enforced in the database.  Therefore I have added 'and rownum=1' just in case
	    //b) The card_status table may have more than 1 record when joining to it
	    //   even when I choose the equipment_status_type_id of hardcode value of '10'.
	    //   Therefore I have inserted a sub-select here
	    //c) Also note that database DIST has been architected so that in the future
	    //   multiple adjustment codes can be created per adjustment reason code.
	    //   So far, there is only the need for the one.  Therefore, I am forced
	    //   to hardcode the reason code to 'DEFAULT' so that I am assured only the one record
	    //d) Also note that I have to also hardcode the product_group_type being that
	    //   of feature_cards
	    String cardQuery = "select a.card_id, a.card_type_cd, a.denomination_amt, " +
	                       " a.serial_no, a.customer_id, a.min_cd, " +
	                       " a.effective_dt, a.expiry_dt, a.auto_renewal_on_act_ind, " +
	                       " b.english_product_name, b.french_product_name, " +
	                       " c.product_type_id, d.adjustment_cd, " +
	                       " e.equipment_status_id, e.card_status_dt " +
	                       " from distadm.card a, " +
	                       " distadm.product b, " +
	                       " distadm.product_family_group c, " +
	                       " distadm.product_type_adjustment d, " +
	                       " distadm.card_status e " +
	                       " where a.product_id = b.product_id and " +
	                       " a.card_id = e.card_id and rownum = 1 and " +
	                       " a.product_id = c.product_id (+) and " +
	                       " c.product_gp_type_id = d.product_gp_type_id (+) and " +
	                       " c.product_type_id = d.product_type_id (+) and " +
	                       " a.card_id = ? and " + //card serial number
	                       " (d.adjustment_reason_cd = 'DEFAULT' or d.adjustment_reason_cd is null) and " + //hardcoded reason code - as per Rosana
	                       " e.equipment_status_type_id = 10 and " + //hardcoded equipment status type id for status lookup - as per David Suydam
	                       " e.card_status_dt = " +
	         " (select max(f.card_status_dt) from distadm.card_status f " +
	         " where f.card_id = a.card_id and f.equipment_status_type_id = 10)";
	    
	    CardInfo cardInfoResult= getDistJdbcTemplate().execute(cardQuery, new PreparedStatementCallback<CardInfo>() {
			@Override
			public CardInfo doInPreparedStatement(PreparedStatement pstmt)
			throws SQLException, DataAccessException {
				ResultSet rset = null;
				CardInfo cardInfo = null;
								
				try{
					pstmt.setString(1,pSerialNo);
				    rset = pstmt.executeQuery();
					if (rset.next()) {
				        cardInfo = new CardInfo();
				        cardInfo.setSerialNumber(rset.getString(1)); //this is the serial number of the CARD!!
				        cardInfo.setType(rset.getString(2));
				        cardInfo.setAmount(rset.getDouble(3));
				        cardInfo.setSubscriberEquipmentSerialNumber(rset.getString(4)); //this is the serial number of the handset
				        cardInfo.setBanId(rset.getInt(5));
				        cardInfo.setPhoneNumber(rset.getString(6));
				        cardInfo.setAvailableFromDate(rset.getDate(7));
				        cardInfo.setAvailableToDate(rset.getDate(8));
				        if (rset.getString(9) != null && rset.getString(9).equals("Y")) {
				          cardInfo.setAutoRenew(true);
				        } else {
				          cardInfo.setAutoRenew(false);
				        }
				        cardInfo.setDescription(rset.getString(10));
				        cardInfo.setDescriptionFrench(rset.getString(11));
				        if (rset.getString(12) != null) {
				          cardInfo.setProductTypeId(rset.getString(12));
				        }
				        if (rset.getString(13) != null) {
				          cardInfo.setAdjustmentCode(rset.getString(13));
				        }
				        cardInfo.setStatus(rset.getInt(14));
				        cardInfo.setStatusDate(rset.getDate(15));
				    }
					
				} finally {
					if (rset != null) {
						rset.close();
					}
				}
				return cardInfo ;
			}
		});
	    
	    if (cardInfoResult==null){
			throw new ApplicationException(SystemCodes.CMB_PEH_DAO,ErrorCodes.CARD_INFORMATION_NOT_FOUND, 
					EquipmentErrorMessages.CARD_INFORMATION_NOT_FOUND_EN+pSerialNo,"");
		}
	    return cardInfoResult;
	}

	@Override
	public ServiceInfo[] getCardServices(final String pSerialNo, final String pTechType,
			final String pBillType) throws ApplicationException {
		
		String serviceQuery = "select c.unit_type_cd, c.duration, d.service_cd, " +
						        " e.prov_service_category_des " +
						        " from distadm.card a, " +
						        " distadm.product_family_group b, " +
						        " distadm.product_type_prov_service c, " +
						        " distadm.provisioning_service d, " +
						        " distadm.provisioning_service_category e " +
						        " where a.product_id = b.product_id and " +
						        " b.product_gp_type_id = c.product_gp_type_id and " +
						        " b.product_type_id = c.product_type_id and " +
						        " c.provisioning_service_id = d.provisioning_service_id and " +
						        " d.prov_service_category_id = e.prov_service_category_id and " +
						        " c.effective_dt <= sysdate and " +
						        " (c.end_dt >= sysdate or c.end_dt is null) and " +
						        " d.effective_dt <= sysdate and " +
						        " (d.expiry_dt >= sysdate or d.expiry_dt is null) and " +
						        " a.card_id = ? and " + //card serial number
						        " c.compatible_technology_type = ? and " +
						        " c.billing_type = ?";
		
		return getDistJdbcTemplate().execute(serviceQuery, new PreparedStatementCallback<ServiceInfo[]>() {
		@Override
		public ServiceInfo[] doInPreparedStatement(PreparedStatement pstmt)throws SQLException, DataAccessException {
		 
			 ResultSet rset = null;
			 ServiceInfo[] serviceInfo = null;
			 ServiceInfo currentServiceInfo = null;
			 ArrayList<ServiceInfo> myArrayList = new ArrayList<ServiceInfo>();
			try{
				pstmt.setString(1,pSerialNo);
			      pstmt.setString(2,pTechType);
			      pstmt.setString(3,pBillType);
			      rset = pstmt.executeQuery();
			     
			      while (rset.next()) {
			        currentServiceInfo = new ServiceInfo();
			        currentServiceInfo.setTermUnits(rset.getString(1));

			        currentServiceInfo.setTerm(rset.getInt(2));
			        if (rset.getString(1).equals("MTH")) {
			          currentServiceInfo.setTermMonths(rset.getInt(2));
			        }

			        currentServiceInfo.setCode(rset.getString(3));

			        if (rset.getString(4).equals("WPS")) {
			          currentServiceInfo.setKnowbility(false);
			          currentServiceInfo.setWPS(true);
			        }
			        else if (rset.getString(4).equals("KB")) {
			          currentServiceInfo.setKnowbility(true);
			          currentServiceInfo.setWPS(false);
			        }
			        else { //should never come here
			          currentServiceInfo.setKnowbility(false);
			          currentServiceInfo.setWPS(false);
			        }
			        myArrayList.add(currentServiceInfo);
			       
			      }
			      serviceInfo = new ServiceInfo[myArrayList.size()];
			      myArrayList.toArray(serviceInfo);
			} finally {
				if (rset != null) {
					rset.close();
				}
			}
			return serviceInfo;
		}
		});
	}

	

	@Override
	public CardInfo[] getCards(final String pPhoneNo, final String pCardType)
			throws ApplicationException {
		StringBuffer cardQuery = new StringBuffer(); 
		
		 cardQuery.append( "select a.card_id, a.card_type_cd, a.denomination_amt, " +
			         " a.serial_no, a.customer_id, a.min_cd, " +
			         " a.effective_dt, a.expiry_dt, a.auto_renewal_on_act_ind, " +
			         " b.english_product_name, b.french_product_name, " +
			         " c.product_type_id, d.adjustment_cd, " +
			         " e.equipment_status_id, e.card_status_dt " +
			         " from distadm.card a, " +
			         " distadm.product b, " +
			         " distadm.product_family_group c, " +
			         " distadm.product_type_adjustment d, " +
			         " distadm.card_status e " +
			         " where a.product_id = b.product_id and " +
			         " a.card_id = e.card_id and " +
			         " a.product_id = c.product_id and " +
			         " c.product_gp_type_id = d.product_gp_type_id (+) and " +
			         " c.product_type_id = d.product_type_id (+) and " +
			         " a.min_cd = ? and " + //phone number
			        // " a.card_type_cd = ? and " + //card type
			         " d.adjustment_reason_cd = 'DEFAULT' and " + //hardcoded reason code - as per Rosana
			         " e.equipment_status_type_id = 10 and " + //hardcoded equipment status type id for status lookup - as per David Suydam
			         " e.card_status_dt = " +
					" (select max(f.card_status_dt) from distadm.card_status f " +
					" where f.card_id = a.card_id and f.equipment_status_type_id = 10) ");
		    
		   if (pCardType!=null) {
			  cardQuery .append(" and a.card_type_cd = ?  ") ; //card type
		    }
		   
		return getDistJdbcTemplate().execute(cardQuery.toString(), new PreparedStatementCallback<CardInfo[]>() {
		@Override
		public CardInfo[] doInPreparedStatement(PreparedStatement pstmt)throws SQLException, DataAccessException {
		 
			 ResultSet rset = null;
			 CardInfo currentCardInfo = null;
			 CardInfo[] cardInfoArray = null;
			 ArrayList<CardInfo> myArrayList = new ArrayList<CardInfo>();
		   try{
			 pstmt.setString(1,pPhoneNo);
			 if (pCardType!=null ) {
		        pstmt.setString(2,pCardType);
		      }
		     rset = pstmt.executeQuery();

		      //unfortuneately, it is possible for the above queries to come back with
		      //more than one record in the result set per card id.  I am assured that
		      //this won't happen in the database, but in oracle, the product_family_group table
		      //can actual hold more than one product_type_id and product_gp_type_id per
		      //single product in the product table.  Because of this, there may be more
		      //than one adjustment code in the product_type_adjustment table.  Therefore,
		      //since I only need one adjustment code, I will iterate through the resultset
		      //and ignore duplicate card id records...
		     
		      String cardId = "DUMMY";
		      while (rset.next()) {
		        if (! cardId.equals(rset.getString(1))) {
		          cardId = rset.getString(1);
		          currentCardInfo = new CardInfo();
		          currentCardInfo.setSerialNumber(rset.getString(1)); //this is the serial number of the CARD!!
		          currentCardInfo.setType(rset.getString(2));
		          currentCardInfo.setAmount(rset.getDouble(3));
		          currentCardInfo.setSubscriberEquipmentSerialNumber(rset.getString(4)); //this is the serial number of the handset
		          currentCardInfo.setBanId(rset.getInt(5));
		          currentCardInfo.setPhoneNumber(rset.getString(6));
		          currentCardInfo.setAvailableFromDate(rset.getDate(7));
		          currentCardInfo.setAvailableToDate(rset.getDate(8));
		          if (rset.getString(9).equals("Y")) {
		            currentCardInfo.setAutoRenew(true);
		          } else {
		            currentCardInfo.setAutoRenew(false);
		          }
		          currentCardInfo.setDescription(rset.getString(10));
		          currentCardInfo.setDescriptionFrench(rset.getString(11));
		          currentCardInfo.setProductTypeId(rset.getString(12));
		          if (rset.getString(13) != null) {
		            currentCardInfo.setAdjustmentCode(rset.getString(13));
		          }
		          currentCardInfo.setStatus(rset.getInt(14));
		          currentCardInfo.setStatusDate(rset.getDate(15));
		          myArrayList.add(currentCardInfo);
		        }
		      }
		      cardInfoArray = new CardInfo[myArrayList.size()];
		      myArrayList.toArray(cardInfoArray);
		} finally {
			if (rset != null) {
				rset.close();
			}
		}
		return cardInfoArray ;
		}
		});
	}

	@Override
	public String getCypherPIN(final String pSerialNo) throws ApplicationException {
		
		String cypherPinQuery = "select pin_id " +
	                       " from distadm.card_pin " +
	                       " where card_id = ?" ;
		String cypherPin= getDistJdbcTemplate().execute(cypherPinQuery, new PreparedStatementCallback<String>() {
		 @Override
		 public String doInPreparedStatement(PreparedStatement pstmt)throws SQLException, DataAccessException {
				ResultSet rset = null;
				String cypherPIN = null;
				try{
					pstmt.setString(1,pSerialNo);
				    rset = pstmt.executeQuery();
				    if (rset.next()) {
				        cypherPIN = rset.getString(1);
				    }
				} finally {
					if (rset != null) {
						rset.close();
					}
				}
				return cypherPIN;
			}
		 });
			
		if (cypherPin==null){
			throw new ApplicationException(SystemCodes.CMB_PEH_DAO,ErrorCodes.CYPHER_PIN_NOT_FOUND, 
					EquipmentErrorMessages.CYPHER_PIN_NOT_FOUND_EN,"");
		}
		return cypherPin;
	}
	
	




}
