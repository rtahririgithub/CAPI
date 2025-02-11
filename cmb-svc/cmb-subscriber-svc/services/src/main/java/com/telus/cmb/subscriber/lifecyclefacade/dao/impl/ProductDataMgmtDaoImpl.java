package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.ProductDataMgmtDao;
import com.telus.cmb.subscriber.mapping.ProductDataMgmtMapper.ProductInstanceMapper;
import com.telus.cmb.subscriber.mapping.ProductDataMgmtMapper.ProductParameterMapper;
import com.telus.cmb.subscriber.mapping.ProductDataMgmtMapper.ProductResourceMapper;
import com.telus.cmb.wsclient.ConsumerProductDataManagementServicePort;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerproductdatamgmtsvcrequestresponse_v1.InsertProductInstance;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerproductdatamgmtsvcrequestresponse_v1.ManageProductParameters;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerproductdatamgmtsvcrequestresponse_v1.ManageProductResources;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerproductdatamgmtsvcrequestresponse_v1.UpdateProductInstance;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.product.productinstance.customer_product_instance_sub_domain_v3.ProductInstance;
import com.telus.tmi.xmlschema.xsd.product.productinstance.customer_product_instance_sub_domain_v3.ProductParameter;
import com.telus.tmi.xmlschema.xsd.product.productinstance.customer_product_instance_sub_domain_v3.ProductResource;

/**
 * 
 * @author tongts
 * 
 */
public class ProductDataMgmtDaoImpl extends SoaBaseSvcClient implements ProductDataMgmtDao {

	@Autowired
	private ConsumerProductDataManagementServicePort port;

	
	public ProductDataMgmtDaoImpl() {
		super(new ProductDataManagementServiceExceptionHandler());
	}
	
	@Override
	public void insertProductInstance(final SubscriberInfo subscriberInfo, final EquipmentInfo equipmentInfo, 
			final SubscriberContractInfo subscriberContractInfo, final String userId) throws ApplicationException {
		
		execute( new SoaCallback<Object>() {
			
			@Override
			public Object doCallback() throws Throwable {
				
				ProductInstanceMapper piMapper = new ProductInstanceMapper();
				ProductInstance newProductInstance = piMapper.mapDomainToSchema(subscriberInfo, equipmentInfo, subscriberContractInfo);
				AuditInfo auditInfo = new AuditInfo();
				auditInfo.setUserId(userId);
				
				InsertProductInstance request = new InsertProductInstance();
				request.setNewProductInstance(newProductInstance);
				request.setAuditInfo(auditInfo);
				
				port.insertProductInstance(request);
				
				return null;
			}
		});
		
	}

	@Override
	public void updateProductInstance(final SubscriberInfo subscriberInfo, final EquipmentInfo equipmentInfo, final SubscriberContractInfo subscriberContractInfo, final String userId) throws ApplicationException {
		
		execute( new SoaCallback<Object>() {
			
			@Override
			public Object doCallback() throws Throwable {
				
				ProductInstanceMapper piMapper = new ProductInstanceMapper();
				ProductInstance newProductInstance = piMapper.mapDomainToSchema(subscriberInfo, equipmentInfo, subscriberContractInfo);
				AuditInfo auditInfo = new AuditInfo();
				auditInfo.setUserId(userId);
				
				UpdateProductInstance request = new UpdateProductInstance();
				request.setModifiedProductInstance(newProductInstance);
				request.setAuditInfo(auditInfo);
				
				port.updateProductInstance(request);
				
				return null;
			}
		});
		
	}

	@Override
	public void manageProductParameters(final SubscriberInfo subscriberInfo, final SubscriberContractInfo subscriberContractInfo, final String userId) throws ApplicationException {
		
		execute( new SoaCallback<Object>() {
			
			@Override
			public Object doCallback() throws Throwable {
				ProductParameterMapper ppMapper = new ProductParameterMapper();
				List<ProductParameter> productParameters = ppMapper.mapDomainToSchema(subscriberInfo);

				ManageProductParameters parameters = new ManageProductParameters();
				parameters.setProductParameterList(productParameters);
				parameters.setMasterSourceId(130L);
				parameters.setKeyId(subscriberInfo.getSubscriptionId());
				parameters.setSubscriberNumber(subscriberInfo.getSubscriberId());

				AuditInfo auditInfo = new AuditInfo();
				auditInfo.setUserId(userId);
				parameters.setAuditInfo(auditInfo);

				port.manageProductParameters(parameters);
				return null;
			}
		});
	}

	@Override
	public void manageProductResources(final SubscriberInfo subscriberInfo, final EquipmentInfo equipmentInfo, final SubscriberContractInfo subscribercontractInfo, final String userId) throws ApplicationException {

		execute( new SoaCallback<Object>() {
			
			@Override
			public Object doCallback() throws Throwable {
				ProductResourceMapper prMapper = new ProductResourceMapper();
				List<ProductResource> productResources = prMapper.mapDomainToSchema(subscriberInfo, subscribercontractInfo, equipmentInfo);

				ManageProductResources resources = new ManageProductResources();
				resources.setMasterSourceId(130L);
				resources.setKeyId(subscriberInfo.getSubscriptionId());
				resources.setSubscriberNumber(subscriberInfo.getSubscriberId());
				resources.setProductResourceList(productResources);

				AuditInfo auditInfo = new AuditInfo();
				auditInfo.setUserId(userId);
				resources.setAuditInfo(auditInfo);

				port.manageProductResources(resources);
				return null;
			}
		});
	}

	@Override
	public String ping() throws ApplicationException {
		
		return execute( new SoaCallback<String>() {
			
			@Override
			public String doCallback() throws Throwable {
				// TODO Auto-generated method stub
				return port.ping( new Ping()).getVersion();
			}
		});
	}

}
