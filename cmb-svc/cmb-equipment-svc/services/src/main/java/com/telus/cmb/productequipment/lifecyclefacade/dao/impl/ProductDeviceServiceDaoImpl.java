package com.telus.cmb.productequipment.lifecyclefacade.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.productequipment.lifecyclefacade.dao.ProductDeviceServiceDao;
import com.telus.cmb.productequipment.mapping.ProductDeviceServiceMapper;
import com.telus.cmb.wsclient.ProductDeviceServicePort;
import com.telus.eas.equipment.info.UsimProfileInfo;
import com.telus.eas.equipment.productdevice.info.ProductInfo;
import com.telus.tmi.xmlschema.srv.pmo.catalogmgmt.productdeviceservicerequestresponse_v5.GetProduct;
import com.telus.tmi.xmlschema.srv.pmo.catalogmgmt.productdeviceservicerequestresponse_v5.GetProductResponse;
import com.telus.tmi.xmlschema.srv.pmo.catalogmgmt.productdeviceservicerequestresponse_v5.GetProductUSIMProfile;
import com.telus.tmi.xmlschema.srv.pmo.catalogmgmt.productdeviceservicerequestresponse_v5.GetProductUSIMProfileResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

public class ProductDeviceServiceDaoImpl extends SoaBaseSvcClient implements ProductDeviceServiceDao {

	private static final Logger LOGGER = Logger.getLogger(ProductDeviceServiceDaoImpl.class);
	
	@Autowired
	private ProductDeviceServicePort port = null;

	@Override
	public ProductInfo getProduct(final String productCode) throws ApplicationException {

		return execute(new SoaCallback<ProductInfo>() {

			@Override
			public ProductInfo doCallback() throws Throwable {
				GetProduct request = new GetProduct();
				request.setProductCode(productCode);
				GetProductResponse response = port.getProduct(request);
				return ProductDeviceServiceMapper.ProductMapper().mapToDomain(response.getProduct());
			}
		});
	}

	@Override
	public ProductInfo getProduct(final String productCode, boolean isEsim) throws ApplicationException {
		if(!isEsim) {
			return getProduct(productCode);
		}
		
		return execute(new SoaCallback<ProductInfo>() {

			@Override
			public ProductInfo doCallback() throws Throwable {

				GetProduct request = new GetProduct();
				request.setProductCode(productCode);
				request.setFeatureCode("ESIM");
				
				GetProductResponse response = port.getProduct(request);
				return ProductDeviceServiceMapper.ProductMapper().mapToDomain(response.getProduct());
			}
		});
	}
	
	@Override
	public UsimProfileInfo getProductUsimProfile(final long usimProductId) throws ApplicationException {
		
		return execute(new SoaCallback<UsimProfileInfo>() {

			@Override
			public UsimProfileInfo doCallback() throws Throwable {
				String errorMsg = null;
				GetProductUSIMProfileResponse response = null;
				GetProductUSIMProfile request = new GetProductUSIMProfile();
				request.setProductID(usimProductId);
				
				try {
					response = port.getProductUSIMProfile(request);
				} catch (Exception e) {
					errorMsg = "Failed to call ProductDeviceService operation getProductUsimProfile, usimProductId = " + usimProductId;
					LOGGER.error(e);
					throw new ApplicationException(SystemCodes.CMB_PEF_DAO, ErrorCodes.ESIM_ERROR_RETRIEVE_EQUIPMENT_FAILURE, errorMsg, StringUtils.EMPTY, e);
				}
				
				if (response == null || response.getProductUSIMProfile() == null) {
					errorMsg = "ProductDeviceService operation getProductUsimProfile: ProductUSIMProfile is not found in the response. usimProductId = " + usimProductId;
					LOGGER.error(errorMsg);
					throw new ApplicationException(SystemCodes.CMB_PEF_DAO, ErrorCodes.ESIM_ERROR_RETRIEVE_EQUIPMENT_FAILURE, errorMsg, StringUtils.EMPTY);
				}

				return ProductDeviceServiceMapper.ProductUSIMProfileMapper().mapToDomain(response.getProductUSIMProfile());
			}
		});
	}
	
	@Override
	public String ping() throws ApplicationException {

		return execute(new SoaCallback<String>() {

			@Override
			public String doCallback() throws Throwable {
				return port.ping(new Ping()).getVersion();
			}
		});
	}
}