package com.telus.cmb.productequipment.helper.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.prepaid.ProductOfferingServiceClient;
import com.telus.cmb.common.prepaid.VoucherValidationServiceClient;
import com.telus.cmb.productequipment.helper.dao.VoucherValidationServiceDao;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.voucherspecificationtypes_v1.Voucher;

public class VoucherValidationServiceDaoImpl implements
		VoucherValidationServiceDao {

	@Autowired
	private ProductOfferingServiceClient productOfferingServiceWSClient;

	@Autowired
	private VoucherValidationServiceClient voucherValidationServiceWSClient;
	
	private JdbcTemplate distJdbcTemplate;
	
	private JdbcTemplate knowbilityJdbcTemplate;

	private final int POTENTIAL_VALID_VOUCHER_STATUS_CODE = 0;
	private final String VALID_VOUCHER_STATUS_CODE = "101";

	public CardInfo validateCardPIN(String serialNo, String fullCardNo,
			String cypherPIN, String userId, String equipmentSerialNo,
			String phoneNumber, CardInfo cardInfo) throws ApplicationException {
		int statusCode = voucherValidationServiceWSClient.validateVoucher(serialNo, fullCardNo, cypherPIN);
		Voucher voucher = productOfferingServiceWSClient.getVoucherDetail(serialNo);

		if (statusCode == POTENTIAL_VALID_VOUCHER_STATUS_CODE
				&& voucher.getStatusCode() == VALID_VOUCHER_STATUS_CODE) {
			// insert record into CARD_PIN_ACCESS table
			insertCardPINAccessAttempt(serialNo, userId, equipmentSerialNo,
					"SUCCESS", phoneNumber);
			cardInfo.setPIN(fullCardNo.substring(11, 15));
		} else {
			// insert record into CARD_PIN_ACCESS table
			insertCardPINAccessAttempt(serialNo, userId, equipmentSerialNo,
					"FAIL", phoneNumber);
		}

		return cardInfo;
	}

	public void insertCardPINAccessAttempt(final String pSerialNo,
			final String user, final String unitSerialNumber,
			final String pinStatusCode, final String phoneNumber)
			throws ApplicationException {

		String cardPinInsertQuery = "insert into distadm.card_pin_access "
				+ " (card_id, access_attempt_dt, access_attempt_user, "
				+ " pin_access_attempt_no, serial_no, seq_no, "
				+ " status_cd, load_dt, update_dt, user_last_modify) "
				+ " VALUES (?, sysdate, ?, 1, ?, 0, ?, sysdate, sysdate, ?)";

		getDistJdbcTemplate().execute(cardPinInsertQuery,
				new PreparedStatementCallback<String>() {
					@Override
					public String doInPreparedStatement(PreparedStatement pstmt)
							throws SQLException, DataAccessException {
						pstmt.setString(1, pSerialNo);
						pstmt.setString(2, phoneNumber);
						pstmt.setString(3, unitSerialNumber);
						pstmt.setString(4, pinStatusCode);
						pstmt.setString(5, user);
						pstmt.executeUpdate();
						return null;
					}
				});
	}

	public JdbcTemplate getDistJdbcTemplate() {
		return distJdbcTemplate;
	}
	
	public JdbcTemplate getKnowbilityJdbcTemplate() {
		return knowbilityJdbcTemplate;
	}

	public void setKnowbilityJdbcTemplate(JdbcTemplate knowbilityJdbcTemplate) {
		this.knowbilityJdbcTemplate = knowbilityJdbcTemplate;
	}
	
	public void setDistJdbcTemplate(JdbcTemplate distJdbcTemplate) {
		this.distJdbcTemplate = distJdbcTemplate;
	}

	@Override
	public TestPointResultInfo test() {
		return voucherValidationServiceWSClient.test();
	}
}
