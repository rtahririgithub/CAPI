package com.telus.cmb.jws.mapper;

import com.telus.api.account.SubscriberDataSharingDetail.DataSharingDetail;
import com.telus.api.account.SubscriberDataSharingDetail.DataSharingSoc;
import com.telus.api.account.SubscriberDataSharingDetail.NonDataSharingRegularSoc;
import com.telus.cmb.jws.FamilyTypeList;
import com.telus.cmb.jws.SubscriberDataSharingInfo;
import com.telus.cmb.jws.SubscriberDataSharingInfo.DataSharingInfoList;
import com.telus.cmb.jws.SubscriberDataSharingInfo.DataSharingInfoList.DataSharingSocList;
import com.telus.cmb.jws.SubscriberDataSharingInfo.NonDataSharingRegularSocList;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo;


public class SubscriberDataSharingInfoMapper extends AbstractSchemaMapper<SubscriberDataSharingInfo, SubscriberDataSharingDetailInfo> {
	
	private static SubscriberDataSharingInfoMapper instance;

	public SubscriberDataSharingInfoMapper() {
		super(SubscriberDataSharingInfo.class, SubscriberDataSharingDetailInfo.class);
	}
	
	public static synchronized SubscriberDataSharingInfoMapper getInstance() {
		if (instance == null) {
			instance = new SubscriberDataSharingInfoMapper();
		}
		return instance;
	}
	
	@Override
	protected SubscriberDataSharingInfo performSchemaMapping(SubscriberDataSharingDetailInfo source, SubscriberDataSharingInfo target) {
		
		target.setSubscriberId(source.getSubscriberId());
		target.setSubscriptionId(source.getSubscriptionId());
		target.setContractStartDate(source.getContractStartDate());
		target.setPricePlanCode(source.getPricePlanCode());
		target.setPricePlanRecurringChargeAmt(source.getPricePlanRecurringCharge());
		target.getNonDataSharingRegularSocList().addAll(NonDataSharingRegularSocListMapper.getInstance().mapToSchema(source.getNonDataSharingRegularSocList()));
		target.getDataSharingInfoList().addAll(DataSharingInfoListMapper.getInstance().mapToSchema(source.getDataSharingInfoList()));
		
		return super.performSchemaMapping(source, target);
	}
	
	/*------------------------------------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 *  Inner class to map the second-level object - NonDataSharingRegularSocList
	 */
	public static class NonDataSharingRegularSocListMapper extends AbstractSchemaMapper<NonDataSharingRegularSocList, NonDataSharingRegularSoc> {

		private static NonDataSharingRegularSocListMapper instance;
		
		public NonDataSharingRegularSocListMapper() {
			super(NonDataSharingRegularSocList.class, NonDataSharingRegularSoc.class);
		}
		
		protected static synchronized NonDataSharingRegularSocListMapper getInstance() {
			if (instance == null) {
				instance = new NonDataSharingRegularSocListMapper();
			}
			return instance;
		}
		
		@Override
		protected NonDataSharingRegularSocList performSchemaMapping(NonDataSharingRegularSoc source, NonDataSharingRegularSocList target) {
			if (source.getFamilyTypes() != null) {
				if (target.getFamilyTypeList() == null) {
					target.setFamilyTypeList(new FamilyTypeList());
				}
				target.getFamilyTypeList().getFamilyType().addAll(source.getFamilyTypes());
			}
			target.setSocCode(source.getSocCode());
			target.setSocRecurringChargeAmt(source.getSocRecurringCharge());
			
			return target;
		}
	}

	
	/**
	 * Inner class to map the second-level object - DataSharingInfoList
	 */
	public static class DataSharingInfoListMapper extends AbstractSchemaMapper<DataSharingInfoList, DataSharingDetail> {
		
		private static DataSharingInfoListMapper instance;
		
		public DataSharingInfoListMapper() {
			super(DataSharingInfoList.class, DataSharingDetail.class);
		}
		
		protected static synchronized DataSharingInfoListMapper getInstance() {
			if (instance == null) {
				instance = new DataSharingInfoListMapper();
			}
			return instance;
		}
		
		@Override
		protected DataSharingInfoList performSchemaMapping(DataSharingDetail source, DataSharingInfoList target) {
			target.setDataSharingGroupCode(source.getDataSharingGroupCode());
			target.getDataSharingSocList().addAll(DataSharingSocListMapper.getInstance().mapToSchema(source.getDataSharingSocList()));
			
			return target;
		}
	}
	
	
	/**
	 * Inner class to map the third-level object - DataSharingSocList
	 */
	public static class DataSharingSocListMapper extends AbstractSchemaMapper<DataSharingSocList, DataSharingSoc> {
		
		private static DataSharingSocListMapper instance;
		
		public DataSharingSocListMapper() {
			super(DataSharingSocList.class, DataSharingSoc.class);
		}

		protected static synchronized DataSharingSocListMapper getInstance() {
			if (instance == null) {
				instance = new DataSharingSocListMapper();
			}
			return instance;
		}

		@Override
		protected DataSharingSocList performSchemaMapping(DataSharingSoc source, DataSharingSocList target) {
			target.setContributingInd(source.getContributingInd());
			target.setDataSharingSocCode(source.getDataSharingSocCode());
			target.setDataSharingSpentAmt(source.getDataSharingSpentAmt());
			if (source.getFamilyTypes() != null) { 
				if (target.getFamilyTypeList() == null) {
					target.setFamilyTypeList(new FamilyTypeList());
				}
				target.getFamilyTypeList().getFamilyType().addAll(source.getFamilyTypes());
			}
			
			return target;
		}
	}
	
}