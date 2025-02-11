package com.telus.cmb.productequipment.manager.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.equipment.Card;
import com.telus.cmb.productequipment.EquipmentErrorMessages;
import com.telus.cmb.productequipment.manager.dao.CardManagerDao;

public class CardManagerDaoImpl implements CardManagerDao{
	
	private static final Logger LOGGER = Logger.getLogger(CardManagerDaoImpl.class);
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
	public void setCardStatus(final String pSerialNo, final int pStatusId, final String pUser, final int pBan, final String pPhoneNumber, 
			final String pEquipmentSerialNo, final boolean pAutoRenewInd) throws ApplicationException {
		
		getDistJdbcTemplate().execute( new ConnectionCallback<String>() {

			  @Override
			  public String doInConnection(Connection connection)  throws SQLException, DataAccessException {
				
				    PreparedStatement pstmtStatus = null;
				    PreparedStatement pstmtCard = null;
				    String cardStatusQuery;
				    String cardQuery;
				  try{

				    //define queries here
				    //if activating a card (status USED), then we must UPDATE
				    //the card table and update the card_status table
				    //if crediting a card (status CREDITED), then we must UPDATE
				    //the card table and insert into the card_status table
				    //if any other status, we must only INSERT into the card_status table
				    if (pStatusId == Card.STATUS_USED) {
				      cardStatusQuery = "update distadm.card_status a" +
				                         " set a.equipment_status_id=?, " +
				                         " a.card_status_dt=sysdate, " +
				                         " a.update_dt=sysdate, " +
				                         " a.user_last_modify=? " +
				                         " where a.card_id=? and " +
				                         " a.equipment_status_id=? and " +
				                         " a.equipment_status_type_id=10 and " +
				                         " a.card_status_dt = " +
				                         " (select max(b.card_status_dt) from distadm.card_status b " +
				           " where b.card_id = a.card_id and b.equipment_status_type_id=10 and b.equipment_status_id=?)";
				      cardQuery = "update distadm.card " +
				                         " set serial_no=?, " +
				                         " customer_id=?, " +
				                         " min_cd=?, " +
				                         " auto_renewal_on_act_ind=?, " +
				                         " update_dt=sysdate, " +
				                         " user_last_modify=? " +
				                         " where card_id=?";
				    } else if (pStatusId == Card.STATUS_CREDITED) {
				      cardStatusQuery = "insert into distadm.card_status " +
				                         " (card_id, equipment_status_type_id, equipment_status_id, " +
				                         " card_status_dt, load_dt, update_dt, user_last_modify) " +
				                         " VALUES (?, 10, ?, sysdate, sysdate, sysdate, ?)" ;
				      cardQuery = "update distadm.card " +
				                         " set serial_no = ?, " +
				                         " customer_id = ?, " +
				                         " min_cd = ?, " +
				                         " auto_renewal_on_act_ind = ?, " +
				                         " update_dt = sysdate, " +
				                         " user_last_modify = ? " +
				                         " where card_id = ? ";
				    } else {
				      cardStatusQuery = "insert into distadm.card_status " +
				                         " (card_id, equipment_status_type_id, equipment_status_id, " +
				                         " card_status_dt, load_dt, update_dt, user_last_modify) " +
				                         " VALUES (?, 10, ?, sysdate, sysdate, sysdate, ?)" ;
				      cardQuery = null;
				    }

				    //execute sql here
				  
				      if (pStatusId == Card.STATUS_USED) {
				        pstmtStatus = connection.prepareStatement(cardStatusQuery);
				        pstmtStatus.setInt(1,Card.STATUS_USED);
				        pstmtStatus.setString(2, pUser);
				        pstmtStatus.setString(3, pSerialNo);
				        pstmtStatus.setInt(4,Card.STATUS_PENDING);
				        pstmtStatus.setInt(5,Card.STATUS_PENDING);
				        pstmtStatus.executeUpdate();
				        LOGGER.debug("query string is:"+cardStatusQuery);
				        LOGGER.debug("serial # in query is:"+pSerialNo);
				       
				        pstmtCard = connection.prepareStatement(cardQuery);
				        pstmtCard.setString(1, pEquipmentSerialNo);
				        pstmtCard.setInt(2, pBan);
				        pstmtCard.setString(3, pPhoneNumber);
				        if (pAutoRenewInd) {
				          pstmtCard.setString(4, "Y");
				        } else {
				          pstmtCard.setString(4, "N");
				        }
				        pstmtCard.setString(5, pUser);
				        pstmtCard.setString(6, pSerialNo);
				        pstmtCard.executeUpdate();

				      } else if (pStatusId == Card.STATUS_CREDITED) {
				        pstmtStatus = connection.prepareStatement(cardStatusQuery);
				        pstmtStatus.setString(1, pSerialNo);
				        pstmtStatus.setInt(2, pStatusId);
				        pstmtStatus.setString(3, pUser);
				        pstmtStatus.executeUpdate();

				        pstmtCard = connection.prepareStatement(cardQuery);
				        pstmtCard.setString(1, pEquipmentSerialNo);
				        pstmtCard.setInt(2, pBan);
				        pstmtCard.setString(3, pPhoneNumber);
				        if (pAutoRenewInd) {
				          pstmtCard.setString(4, "Y");
				        } else {
				          pstmtCard.setString(4, "N");
				        }
				        pstmtCard.setString(5, pUser);
				        pstmtCard.setString(6, pSerialNo);
				        pstmtCard.executeUpdate();

				      } else { //any other status
				        pstmtStatus = connection.prepareStatement(cardStatusQuery);
				        pstmtStatus.setString(1, pSerialNo);
				        pstmtStatus.setInt(2, pStatusId);
				        pstmtStatus.setString(3, pUser);
				        pstmtStatus.executeUpdate();
				      }
				      
				  }finally{
					  if(pstmtStatus!=null ){
						  pstmtStatus.close();
					  }
					  if(pstmtCard!=null ){
						  pstmtCard.close();
					  }
				  }
				return null;
			}
		});
	}
	
	
	@Override
	public int getCardStatus(final String pSerialNo) throws ApplicationException {

		 String currentCardStatusQuery = "select a.equipment_status_id " +
								         " from distadm.card_status a " +
								         " where a.card_id = ? and " +
								         " a.equipment_status_type_id = 10 and " +
								         " a.card_status_dt = "  +
										 " (select max(b.card_status_dt) from distadm.card_status b " +
										 " where b.card_id = a.card_id and b.equipment_status_type_id = 10)";
		try{ 
	 return getDistJdbcTemplate().execute(currentCardStatusQuery, new PreparedStatementCallback<Integer>() {
		
		@Override
		 public Integer doInPreparedStatement(PreparedStatement pstmt)throws SQLException, DataAccessException {
			 	int currentStatusId = 0;
				ResultSet rset = null;
				
				try{
					pstmt.setString(1,pSerialNo);
				      rset = pstmt.executeQuery();
				      if (rset.next()) {
				        currentStatusId = rset.getInt(1);
				      } 
				} finally {
					if (rset != null) {
						rset.close();
					}
				}
				return currentStatusId;
			}
		 });
		}catch(Exception e){
			throw new ApplicationException(SystemCodes.CMB_PEM_DAO, ErrorCodes.GET_CARD_STATUS_FAILED,EquipmentErrorMessages.GET_CARD_STATUS_FAILED_EN,"");
		}
		 
	}
	

}
