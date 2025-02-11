package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;


import amdocs.APILink.sessions.interfaces.NewProductConv;
import amdocs.APILink.sessions.interfaces.UpdateProductConv;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.subscriber.lifecyclemanager.dao.DepositDao;
import com.telus.cmb.subscriber.utilities.AmdocsConvBeanClassFactory;
import com.telus.eas.subscriber.info.SubscriberInfo;

public class DepositDaoImpl extends AmdocsDaoSupport implements DepositDao {

		@Override
		public void createDeposit(final SubscriberInfo pSubscriberInfo,
				final double amount, final String memoText,String sessionId) throws ApplicationException {

			getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
				
				@Override
				public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
					
					if(pSubscriberInfo.getStatus() == 'R'){
						NewProductConv newProductConv = transactionContext.createBean
						 (AmdocsConvBeanClassFactory.getNewProductConvBean(pSubscriberInfo.getProductType()));
						newProductConv.setProductPK(pSubscriberInfo.getBanId(),pSubscriberInfo.getSubscriberId());
						newProductConv.createDeposit(amount, memoText);
					}
					else{
						UpdateProductConv updateProductConv = transactionContext.createBean
						 (AmdocsConvBeanClassFactory.getUpdateProductConvBean(pSubscriberInfo.getProductType()));
						
						updateProductConv.setProductPK(pSubscriberInfo.getBanId(),pSubscriberInfo.getSubscriberId());
						updateProductConv.createDeposit(amount, memoText);
					}
					 
					return null;
				}
			});
			
		}
		
		
}
