package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.ChargeAndAdjDetailInfo;
import amdocs.APILink.datatypes.CreateChargeAdjustInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.UpdateProductConv;
import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.mapping.ChargeAndAdjustmentMapper;
import com.telus.cmb.subscriber.lifecyclemanager.dao.AdjustmentDao;
import com.telus.cmb.subscriber.utilities.AmdocsConvBeanClassFactory;
import com.telus.eas.framework.info.ChargeAdjustmentInfo;

public class AdjustmentDaoImpl extends AmdocsDaoSupport implements AdjustmentDao{

	private static final Logger LOGGER = Logger.getLogger(AdjustmentDaoImpl.class);

	/*
	  @Override
	  public List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsForSubscriber(final List<ChargeAdjustmentInfo> chargeInfoList, 
			  String sessionId) throws ApplicationException{

		  return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<List<ChargeAdjustmentInfo>>() {

			  @Override
			  public List<ChargeAdjustmentInfo> doInTransaction(AmdocsTransactionContext transactionContext)
					  throws Exception {
				  List<ChargeAdjustmentInfo> list = new ArrayList<ChargeAdjustmentInfo>();

				  for(ChargeAdjustmentInfo chargeInfo : chargeInfoList )
				  {
					  CreateChargeAdjustInfo createChargeAdjustInfo = null;
					  try{

						 createChargeAdjustInfo  = ChargeAndAdjustmentMapper.mapToAmdocsCreateChargeAdjustInfo(chargeInfo);
						  ChargeAndAdjDetailInfo chargeAndAdjDetailInfo  = null;
						  
							UpdateProductConv updateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(chargeInfo.getProductType()));
							updateProductConv.setProductPK(chargeInfo.getBan(), chargeInfo.getProductType());
							
							chargeAndAdjDetailInfo =  updateProductConv.applyChargeAndAdjust(createChargeAdjustInfo);


						  list.add(ChargeAndAdjustmentMapper.mapToTelusChargeAdjustmentInfo(chargeAndAdjDetailInfo, chargeInfo));
						  LOGGER.debug("In loop... chargeSeqNo is " + chargeAndAdjDetailInfo.chargeDetailInfo.chargeSeqNo + " ... adjustmentId is "+chargeAndAdjDetailInfo.adjustDetailInfo.adjustmentId );

					  }catch(ValidateException vex){
						  list.add(ChargeAndAdjustmentMapper.getFailedTransactionsDetails(chargeInfo, "KB-"+String.valueOf(vex.getErrorInd()),vex.getErrorMsg()));
					  }catch(Exception ex){
						  list.add(ChargeAndAdjustmentMapper.getFailedTransactionsDetails(chargeInfo, SystemCodes.CMB_ALM_DAO,ex.getMessage()));
					  }
				  }
				  return list;
			  }

		  });
	  }
*/	  
}
