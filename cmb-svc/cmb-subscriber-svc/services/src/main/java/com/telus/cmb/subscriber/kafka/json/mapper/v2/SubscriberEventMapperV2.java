package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.telus.api.account.Subscriber;
import com.telus.cmb.common.kafka.TransactionEventInfo;
import com.telus.cmb.common.kafka.TransactionEventMapper;
import com.telus.cmb.common.kafka.subscriber_v2.PhoneNumberChangeDetail;
import com.telus.cmb.common.kafka.subscriber_v2.PortActivityDetail;
import com.telus.cmb.common.kafka.subscriber_v2.SubscriberEvent;
import com.telus.cmb.common.kafka.subscriber_v2.SubscriberStatusChange;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.cmb.subscriber.kafka.ServiceAgreementUtil;

public class SubscriberEventMapperV2  extends AbstractSchemaMapper<SubscriberEvent, TransactionEventInfo> implements TransactionEventMapper{
	
	public SubscriberEventMapperV2() {
		super(SubscriberEvent.class, TransactionEventInfo.class);
	}

	@Override
	protected SubscriberEvent performSchemaMapping(com.telus.cmb.common.kafka.TransactionEventInfo source, SubscriberEvent target){
		// map base event info  
		target.setNotificationSuppressionInd(source.isNotificationSuppressionInd()); 
		target.setTransactionDate(source.getTransactionDate());
		target.setAuditInfo(SubscriberMapper.mapAuditInfo(source.getAuditInfo(),source.getDealerCode(), source.getSalesRepCode(),source.getSessionId()));
				
		// map account data
		target.setAccount(AccountMapper.mapAccount(source.getAccountInfo()));
		// map subscriber data
		//Note: CAPI don't have complete subscriber info for all cancel use cases , as agreed with ECP team passing the phone number in status change same as account cancel/multiple subscriber cancel.
		if (source.isSubscriberCancel() || source.isSubscriberCancelPortOut()) {
			SubscriberStatusChange subStatusChange = new SubscriberStatusChange();
			subStatusChange.setPhoneNumber(source.getSubscriberInfo().getPhoneNumber());
			if(CollectionUtils.isNotEmpty(source.getWaiveReasonCodeList())){
				subStatusChange.setDepositWaiveReasonCd(source.getWaiveReasonCodeList().get(0));
			}
			target.getSubscriberStatusChangeList().add(subStatusChange);
			// map subscriber cancel/subscriber portOut changes
			target.setActivityDetail(ActivityDetailMapper.mapActivityDetail(source));
			target.setMemo(MemoMapper.mapMemo(source));
		} else {
			target.setSubscriber(SubscriberMapper.mapSubscriber(source.getSubscriberInfo()));
			// map subscriber name
			target.getSubscriber().setConsumerName(ConsumerNameMapper.mapConsumerName(source.getSubscriberInfo().getConsumerName()));
			// map seat data if account is Business Connect
			if (source.getAccountInfo().isPostpaidBusinessConnect()) {
				target.getSubscriber().setSeatData(SeatDataMapper.mapSeatData(source.getSubscriberInfo().getSeatData()));
			}
		}

		// map portIn Activity info
		if (source.isPortIn()) {
			PortActivityDetail portActivityDetail = new PortActivityDetail();
			portActivityDetail.setPortProcessType(source.getPortProcessType());
			portActivityDetail.setPortType(Subscriber.PORT_TYPE_PORT_IN);
			target.setPortActivityDetail(portActivityDetail);
			//also update the port type flag as "IN"
			target.getSubscriber().setPortType(Subscriber.PORT_TYPE_PORT_IN);
		}
		
		
		if (source.isActivation() || source.isServiceAgreementChange()){
			// map service agreement changes 
			target.setServiceAgreement(ServiceAgreementMapper.mapServiceAgreementData(source.getNewContractInfo(),source.getOldContractInfo(),source.isActivation()));
			//map equipment & associatedHandset
			target.setEquipment(EquipmentMapper.mapEquipment(source.getEquipmentInfo()));
			target.setAssociatedEquipment(EquipmentMapper.mapEquipment(source.getEquipmentInfo() != null ? source.getEquipmentInfo().getAssociatedHandset() : null));
		}
		

		// map subscriber move
		if (source.isSubscriberMove()) {
			target.setSubscriberMoveDetail(SubscriberMoveDetailMapper.mapSubscriberMoveDetail(source));
			target.setActivityDetail(ActivityDetailMapper.mapActivityDetail(source));
			target.setMemo(MemoMapper.mapMemo(source));
		}
		
		// map subscriber phone number change 
		if (source.isPhoneNumberChange()) {
			PhoneNumberChangeDetail ctnChangeDetail = new PhoneNumberChangeDetail();
			ctnChangeDetail.setOldPhoneNumber(source.getOldPhoneNumber());
			ctnChangeDetail.setNewPhoneNumber(source.getSubscriberInfo().getPhoneNumber());
			target.setPhoneNumberChangeDetail(ctnChangeDetail);			
		}
				
		// set the event types
		if (source.isServiceAgreementChange()) {
			List<String> eventTypes = new ArrayList<String>();
			eventTypes = ServiceAgreementUtil.popualteServiceAgreementChangeEventTypes(source, target);
			target.setEventType(StringUtils.join(eventTypes, ","));
		} else {
			target.setEventType(String.valueOf(source.getEventType()));
		}

		
		return super.performSchemaMapping(source, target);
	}

}
