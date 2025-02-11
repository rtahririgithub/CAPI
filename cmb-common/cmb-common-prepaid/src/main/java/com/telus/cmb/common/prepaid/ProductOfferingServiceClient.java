package com.telus.cmb.common.prepaid;

import org.apache.commons.lang.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.ProductOfferingServicePort;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.productofferingservice_v2.GetVoucherDetail;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.voucherspecificationtypes_v1.Voucher;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

public class ProductOfferingServiceClient extends SoaBaseSvcClient {

	private final String PREPAID_EQUIPMENT_SERIAL_NUMBER = "10";
	private final String PREPAID_AIRTIME_CARD = "AIR";

	protected ProductOfferingServicePort port;
	
	public void setPort(ProductOfferingServicePort port) {
		this.port = port;
	}

	@Override
	public String ping() throws ApplicationException {
		return execute(new SoaCallback<String>() {
			@Override
			public String doCallback() throws Exception {
				Ping parameter = null;
				return port.ping(parameter).getVersion();
			}
		});
	}

	public Voucher getVoucherDetail(final String serialNumber) throws ApplicationException {
		return getVoucherDetail(serialNumber, "");
	}
	
	public Voucher getVoucherDetail(final String serialNumber, final String voucherPin) throws ApplicationException {
		return execute(new SoaCallback<Voucher>() {
			
			@Override
			public Voucher doCallback() throws Exception {
				
				GetVoucherDetail voucherDetail = new GetVoucherDetail();
				voucherDetail.setVoucherType(PREPAID_AIRTIME_CARD);
				voucherDetail.setEquipmentSerialNumber(PREPAID_EQUIPMENT_SERIAL_NUMBER);				
				voucherDetail.setSerialNumber(serialNumber);
				if (StringUtils.isNotBlank(voucherPin)) {
					voucherDetail.setVoucherPIN(voucherPin);
				}
				return port.getVoucherDetail(voucherDetail,new OriginatingUserType()).getVoucher();
			}
		});
	}

}
