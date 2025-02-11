package com.telus.eas.account.info;

import java.util.Date;
import java.util.List;

import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.transaction.info.ActivityResultInfo;

public class PaymentNotificationResponseInfo extends Info {
	
	private static final long serialVersionUID = 1L;

	private final int id;
	private final MemoInfo memo;
	private final FollowUpInfo followUp;
	private final Date dueDate;
	private List activityResults;

	public PaymentNotificationResponseInfo(int id, MemoInfo memo, FollowUpInfo followUp, Date dueDate) {
		this.id = id;
		this.memo = memo;
		this.followUp = followUp;
		this.dueDate = dueDate;
	}

	public PaymentNotificationResponseInfo(int id, MemoInfo memo, FollowUpInfo followUp, Date dueDate, List activityResults) {
		this.id = id;
		this.memo = memo;
		this.followUp = followUp;
		this.dueDate = dueDate;
		this.activityResults = activityResults;
	}

	public Date getPaymentNotificationDueDate() {
		return dueDate;
	}
	
	public MemoInfo getMemo() {
		return memo;
	}

	public FollowUpInfo getFollowUp() {
		return followUp;
	}

	public int getId() {
		return id;
	}
	
	public List getActivityResults() {
		return activityResults;
	}

	public String toString() {
		
		StringBuffer str = new StringBuffer();
		str.append("PaymentNotificationResponseInfo: [").append('\n');
		str.append("   id=[").append(id).append("]").append('\n');
		str.append("   dueDate=[").append(dueDate).append("]").append('\n');
		str.append(memo.toString()).append('\n');
		if (followUp != null) {
			str.append(followUp.toString()).append('\n');
		}
		if (activityResults != null && !activityResults.isEmpty()) {
			str.append("ActivityResultInfo: [").append('\n');
			while (activityResults.iterator().hasNext()) {
				ActivityResultInfo info = (ActivityResultInfo) activityResults.iterator().next();
				str.append("   value=[").append(info.getActivity().getValue()).append("]").append('\n');
				str.append("   throwable=[").append(info.getThrowable().getMessage()).append("]").append('\n');
			}
			str.append("]");
		}
		str.append("]");

		return str.toString();
	}
	
}