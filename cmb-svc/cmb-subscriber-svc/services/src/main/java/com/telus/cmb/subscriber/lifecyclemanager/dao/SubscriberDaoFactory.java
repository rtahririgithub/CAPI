package com.telus.cmb.subscriber.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.account.Subscriber;

public class SubscriberDaoFactory {
	
	private SubscriberDao subscriberDao;
	


	private NewIdenSubscriberDao newIdenSubscriberDao;
	private NewSubscriberDao newCdpdSubscriberDao;
	private NewSubscriberDao newPagerSubscriberDao;
	private NewSubscriberDao newTangoSubscriberDao;
	private NewPcsSubscriberDao newPcsSubscriberDao;
	
	private UpdateIdenSubscriberDao updateIdenSubscriberDao;
	private UpdateSubscriberDao updateCdpdSubscriberDao;
	private UpdatePagerSubscriberDao updatePagerSubscriberDao;
	private UpdateSubscriberDao updateTangoSubscriberDao;
	private UpdatePcsSubscriberDao updatePcsSubscriberDao;
	
	private UpdateIdenPcsSubscriberDao updateIdenIdenPcsSubscriberDao;
	private UpdateIdenPcsSubscriberDao updatePcsIdenPcsSubscriberDao;
	
	private NewIdenPcsSubscriberDao newPcsIdenPcsSubscriberDao;
	private NewIdenPcsSubscriberDao newIdenIdenPcsSubscriberDao;
	
	public NewSubscriberDao getNewSubscriberDao(String productType) throws ApplicationException {
		if (productType.equals(Subscriber.PRODUCT_TYPE_IDEN)) {
			return newIdenSubscriberDao;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_PCS)) {
			return newPcsSubscriberDao;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_CDPD)) {
			return newCdpdSubscriberDao;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_TANGO)) {
			return newTangoSubscriberDao;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_PAGER)) {
			return newPagerSubscriberDao;
		} else {			
			throw new ApplicationException(SystemCodes.CMB_SLM_DAO, "Product Type not supported: " + productType,"");
		}		
	}
	
	public UpdateSubscriberDao getUpdateSubscriberDao(String productType) throws ApplicationException {
		if (productType.equals(Subscriber.PRODUCT_TYPE_IDEN)) {
			return updateIdenSubscriberDao;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_PCS)) {
			return updatePcsSubscriberDao;			
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_CDPD)) {
			return updateCdpdSubscriberDao;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_TANGO)) {
			return updateTangoSubscriberDao;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_PAGER)) {
			return updatePagerSubscriberDao;
		} else {
			throw new ApplicationException(SystemCodes.CMB_SLM_DAO, "Product Type not supported: " + productType,"");
		}	
	}
	
	public UpdateIdenPcsSubscriberDao getUpdateIdenPcsSubscriberDao(String productType) throws ApplicationException {
		if (productType.equals(Subscriber.PRODUCT_TYPE_IDEN)) {
			return updateIdenIdenPcsSubscriberDao;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_PCS)) {
			return updatePcsIdenPcsSubscriberDao;
		} else {
			throw new ApplicationException(SystemCodes.CMB_SLM_DAO, "Product Type not supported: " + productType,"");
		}
	}
	
	public NewIdenPcsSubscriberDao getNewIdenPcsSubscriberDao(String productType) throws ApplicationException {
		if (productType.equals(Subscriber.PRODUCT_TYPE_IDEN)) {
			return newIdenIdenPcsSubscriberDao;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_PCS)) {
			return newPcsIdenPcsSubscriberDao;
		} else {
			throw new ApplicationException(SystemCodes.CMB_SLM_DAO, "Product Type not supported: " + productType,"");
		}
	}
	
	public void setSubscriberDao(SubscriberDao subscriberDao) {
		this.subscriberDao = subscriberDao;
	}

	public void setNewPcsIdenPcsSubscriberDao(
			NewIdenPcsSubscriberDao newPcsIdenPcsSubscriberDao) {
		this.newPcsIdenPcsSubscriberDao = newPcsIdenPcsSubscriberDao;
	}

	public void setNewIdenIdenPcsSubscriberDao(
			NewIdenPcsSubscriberDao newIdenIdenPcsSubscriberDao) {
		this.newIdenIdenPcsSubscriberDao = newIdenIdenPcsSubscriberDao;
	}

	public void setUpdateIdenIdenPcsSubscriberDao(
			UpdateIdenPcsSubscriberDao updateIdenIdenPcsSubscriberDao) {
		this.updateIdenIdenPcsSubscriberDao = updateIdenIdenPcsSubscriberDao;
	}

	public void setUpdatePcsIdenPcsSubscriberDao(
			UpdateIdenPcsSubscriberDao updatePcsIdenPcsSubscriberDao) {
		this.updatePcsIdenPcsSubscriberDao = updatePcsIdenPcsSubscriberDao;
	}

	public void setNewIdenSubscriberDao(NewIdenSubscriberDao newIdenSubscriberDao) {
		this.newIdenSubscriberDao = newIdenSubscriberDao;
	}

	public void setNewCdpdSubscriberDao(NewSubscriberDao newCdpdSubscriberDao) {
		this.newCdpdSubscriberDao = newCdpdSubscriberDao;
	}

	public void setNewPagerSubscriberDao(NewSubscriberDao newPagerSubscriberDao) {
		this.newPagerSubscriberDao = newPagerSubscriberDao;
	}

	public void setNewTangoSubscriberDao(NewSubscriberDao newTangoSubscriberDao) {
		this.newTangoSubscriberDao = newTangoSubscriberDao;
	}

	public void setNewPcsSubscriberDao(NewPcsSubscriberDao newPcsSubscriberDao) {
		this.newPcsSubscriberDao = newPcsSubscriberDao;
	}

	public void setUpdateIdenSubscriberDao(
			UpdateIdenSubscriberDao updateIdenSubscriberDao) {
		this.updateIdenSubscriberDao = updateIdenSubscriberDao;
	}

	public void setUpdateCdpdSubscriberDao(
			UpdateSubscriberDao updateCdpdSubscriberDao) {
		this.updateCdpdSubscriberDao = updateCdpdSubscriberDao;
	}

	public void setUpdatePagerSubscriberDao(
			UpdatePagerSubscriberDao updatePagerSubscriberDao) {
		this.updatePagerSubscriberDao = updatePagerSubscriberDao;
	}

	public void setUpdateTangoSubscriberDao(
			UpdateSubscriberDao updateTangoSubscriberDao) {
		this.updateTangoSubscriberDao = updateTangoSubscriberDao;
	}

	public void setUpdatePcsSubscriberDao(UpdatePcsSubscriberDao updatePcsSubscriberDao) {
		this.updatePcsSubscriberDao = updatePcsSubscriberDao;
	}

	public UpdateIdenSubscriberDao getUpdateIdenSubscriberDao() {
		return updateIdenSubscriberDao;
	}
	
	public UpdatePcsSubscriberDao getUpdatePcsSubscriberDao() {
		return updatePcsSubscriberDao;
	}
	
	public UpdatePagerSubscriberDao getUpdatePagerSubscriberDao() {
		return updatePagerSubscriberDao;
	}
	
	public NewPcsSubscriberDao getNewPcsSubscriberDao() {
		return newPcsSubscriberDao;
	}
	
	public NewIdenSubscriberDao getNewIdenSubscriberDao() {
		return newIdenSubscriberDao;
	}

	public SubscriberDao getSubscriberDao() {
		return subscriberDao;
	}
}
