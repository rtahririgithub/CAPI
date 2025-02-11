package com.telus.cmb.common.prepaid;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.VoucherValidationServicePort;

public class VoucherValidationServiceClient extends SoaBaseSvcClient {

	protected VoucherValidationServicePort port;

	public void setPort(VoucherValidationServicePort port) {
		this.port = port;
	}
	
	public Integer validateVoucher(final String serialNumber, final String clearPIN, final String cipherPINString) 	throws ApplicationException {
		
		return execute(new SoaCallback<Integer>() {
			@Override
			public Integer doCallback() throws Exception {
				return port.validateVoucher(serialNumber, clearPIN, cipherPINString);
			}
		});
	}
	
	@Override
	public String ping() throws ApplicationException {
		return execute(new SoaCallback<String>() {
			@Override
			public String doCallback() throws Exception {
				// Since VoucherValidationService has no ping operation, use
				// dummy data
				// From Radha Guduru: If you pass right data type for any field
				// the response will be integer
				return String.valueOf(port
						.validateVoucher("00000000000", "000000000000000",
								"0000000000000000"));
			}
		});
	}
}
