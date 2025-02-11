package com.telus.api.account;

import java.util.HashMap;
import java.util.Map;

import com.telus.api.TelusAPIException;

/**
 * This exception indicates that, while the subscriber was successfully created
 * in Knowbility, one or more post creation steps failed. Example post-creation
 * steps are memo creation, fee application, promotional discount application.
 * 
 * Note that the subscriber creation process is NOT idempotent (on a failure) and CANNOT simply be invoked again.
 */
public class IncompleteSubscriberCreationProcessException extends TelusAPIException {

	private static final long serialVersionUID = 1L;
	public static final Integer STEP_CREATE_MEMOS = new Integer(1);
	public static final Integer STEP_APPLY_FEES = new Integer(2);
	public static final Integer STEP_APPLY_DISCOUNTS = new Integer(3);

	public static final Integer[] ALL_STEPS = new Integer[] {
			IncompleteSubscriberCreationProcessException.STEP_CREATE_MEMOS,
			IncompleteSubscriberCreationProcessException.STEP_APPLY_FEES,
			IncompleteSubscriberCreationProcessException.STEP_APPLY_DISCOUNTS
	};

	public static final Integer[] ALL_STEPS_EXCEPT_MEMOS = new Integer[] {
			IncompleteSubscriberCreationProcessException.STEP_APPLY_FEES,
			IncompleteSubscriberCreationProcessException.STEP_APPLY_DISCOUNTS
	};
	
	public static final Integer[] APPLY_DISCOUNTS = new Integer[] {
			IncompleteSubscriberCreationProcessException.STEP_APPLY_DISCOUNTS
	};
	
	
	private static final String baseMessage = "ClientAPI subscriber save() created the subscriber but did not complete the following steps: ";

	private static final Map<Integer,String> reasons = new HashMap<Integer,String>();
	
    static {
    	reasons.put(STEP_CREATE_MEMOS, "Memo creation");
    	reasons.put(STEP_APPLY_FEES, "Fee application");
    	reasons.put(STEP_APPLY_DISCOUNTS, "Discount application");
    }

    private Integer[] incompleteSteps;
    private String reasonMessage;

	public IncompleteSubscriberCreationProcessException(Throwable t, Integer[] incompleteSteps) {
        super( createReasonMessage(incompleteSteps), t);
        this.incompleteSteps = incompleteSteps;
        reasonMessage = createReasonMessage(incompleteSteps);
	}

	public Integer[] getIncompleteSteps() {
        return incompleteSteps;
    }

	public  String getReasonMessage(){
       	return reasonMessage;
    }

	private static String createReasonMessage(Integer[] incompleteSteps) {
		StringBuffer sb = new StringBuffer(255);
		sb.append(baseMessage);
		if (incompleteSteps != null) {
			for (int i = 0; i < incompleteSteps.length; i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append( (String)reasons.get(incompleteSteps[i]) );
			}
		}
		return sb.toString();
	}
}
