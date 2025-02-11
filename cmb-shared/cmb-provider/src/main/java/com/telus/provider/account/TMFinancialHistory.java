package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.CollectionHistory;
import com.telus.api.account.CollectionState;
import com.telus.api.account.CollectionStep;
import com.telus.api.account.DebtSummary;
import com.telus.api.account.FinancialHistory;
import com.telus.api.account.MonthlyFinancialActivity;
import com.telus.api.reference.CollectionActivity;
import com.telus.api.util.SessionUtil;
import com.telus.eas.account.info.CollectionStateInfo;
import com.telus.eas.account.info.CollectionStepInfo;
import com.telus.eas.account.info.FinancialHistoryInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;

public class TMFinancialHistory extends BaseProvider implements FinancialHistory {

	private FinancialHistoryInfo delegate;
	private int banId;
	private Date accountStartServiceDate;
	
	public TMFinancialHistory(TMProvider provider, FinancialHistoryInfo delegate) {
		super(provider);
		this.delegate = delegate;
	}

	public FinancialHistoryInfo getDelegate() {
		return delegate;
	}

	public boolean isDelinquent() {
		return delegate.isDelinquent();
	}

	public DebtSummary getDebtSummary() {
		return delegate.getDebtSummary0();
	}

	public MonthlyFinancialActivity[] getMonthlyFinancialActivity() {
		return delegate.getMonthlyFinancialActivity0();
	}

	public Date getLastPaymentDate() {
		return delegate.getLastPaymentDate();
	}

	public double getLastPaymentAmount() {
		return delegate.getLastPaymentAmount();
	}

	public int getDishonoredPaymentCount() {
		return delegate.getDishonoredPaymentCount();
	}

	public int getSuspensionCount() {
		return delegate.getSuspensionCount();
	}

	public int getCancellationCount() {
		return delegate.getCancellationCount();
	}

	public boolean isWrittenOff() {
		return delegate.isWrittenOff();
	}

	public CollectionStep getCollectionStep() {
		return delegate.getCollectionStep0();
	}

	public String getCollectionAgency() {
		return delegate.getCollectionAgency();
	}

	public CollectionStep getNextCollectionStep() {
		return delegate.getNextCollectionStep0();
	}

	public boolean isLastPaymentRefunded() {
		return delegate.isLastPaymentRefunded();
	}

	public boolean isHotlined() {
		return delegate.isHotlined();
	}

	public Date getHotlinedDate() {
		return delegate.getHotlinedDate();
	}

	public Date getDelinquentDate() {
		return delegate.getDelinquentDate();
	}

	public Date getWrittenOffDate() {
		return delegate.getWrittenOffDate();
	}

	public CollectionState getCollectionState() throws TelusAPIException {
		CollectionStateInfo ret = delegate.getCollectionState0();
		//introduce lazy load and caching to improve performance 
		if ( ret==null ) {
			ret = retrieveCollectionState();
			delegate.setCollectionState( ret );
		}
		return ret;
	}

	public CollectionStateInfo retrieveCollectionState() throws TelusAPIException {

		CollectionStateInfo ret = new CollectionStateInfo();
		try {
			ret  =  provider.getAccountLifecycleManager().retrieveBanCollectionInfo(banId, SessionUtil.getSessionId( provider.getAccountLifecycleManager()));
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		try{
			//Defect PROD00064773 fix:
			// One BAN could be on multiple collection paths, and KB-API return step# for previous step, but not activity code
			// work around: we have to use step# and treatment date to find the matching step from collection history, 
			// then we will know the activity code. 
			matchAndCopyStepFromCollectionHistory( (CollectionStepInfo) ret.getPreviousCollectionStep() );

			String nextPathCode = ret.getNextCollectionStep().getPath();
			int nextStepNumber = ret.getNextCollectionStep().getStep();
			String nextActivityCode = "";
			CollectionActivity nextCollActv = provider.getReferenceDataManager().getCollectionActivity(nextPathCode,nextStepNumber);
			if (nextCollActv != null)
				nextActivityCode = nextCollActv.getCode();
			((CollectionStepInfo)ret.getNextCollectionStep()).setCollectionActivityCode(nextActivityCode);
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
		return ret;
	}


	private void matchAndCopyStepFromCollectionHistory(CollectionStepInfo collectionStep) {
		try {
			CollectionHistory[] history = getCollectionHistory(
					accountStartServiceDate, getEndDate());
			if (history != null) {
				for (int i = 0; i < history.length; i++) {
					CollectionStepInfo aStep = (CollectionStepInfo) history[i].getCollectionStep();
					if ( collectionStep.isMatch(aStep) ) {
						collectionStep.copyFrom(aStep);
						break;
					}
				}
			}
		} catch (TelusAPIException e) {
		}

	}

	public CollectionHistory[] getCollectionHistory(Date from, Date to) throws TelusAPIException {
		CollectionHistory[] history = null;

		try {
			history =  provider.getAccountInformationHelper().retrieveCollectionHistoryInfo(banId, from, to);
		}catch(Throwable t) {
			Logger.debug(t);
			provider.getExceptionHandler().handleException(t);
		}

		return history;
	}

	public void setBanId(int id) {
		this.banId = id;
	}

	public String toString() {
		return delegate.toString();
	}

	protected static Date getEndDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	public void setAccountStartServiceDate(Date accountStartServiceDate) {
		this.accountStartServiceDate = accountStartServiceDate;
	}

	public String getLastPaymentActivityCode() {
		if(delegate == null) return "";
		return delegate.getLastPaymentActivityCode();
	}

	public boolean isLastPaymentBackedout() {
		if(delegate == null) return false;
		return delegate.isLastPaymentBackedout();
	}

	public boolean isLastPaymentFullyTransferred() {
		if(delegate == null) return false;
		return delegate.isLastPaymentFullyTransferred();
	}

	public boolean isLastPaymentSufficient() {
		if(delegate == null) return false;
		return delegate.isLastPaymentSufficient();
	}	
}
