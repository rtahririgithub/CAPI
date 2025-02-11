/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Date;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.FollowUp;
import com.telus.api.account.FollowUpText;
import com.telus.api.reference.WorkPosition;
import com.telus.api.util.SessionUtil;
import com.telus.eas.account.info.FollowUpTextInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

public class TMFollowUp extends BaseProvider implements FollowUp {
    /**
     * @link aggregation
     */
    private final FollowUpInfo delegate;
    private FollowUpUpdateInfo followUpUpdateInfo;
	
    public TMFollowUp(TMProvider provider, FollowUpInfo delegate) {
        super(provider);
        this.delegate = delegate;
    }

    //--------------------------------------------------------------------
    //  Decorative Methods
    //--------------------------------------------------------------------
    public int getBanId() {
        return delegate.getBanId();
    }

    public int getFollowUpId() {
        return delegate.getFollowUpId();
    }

    public String getFollowUpType() {
        return delegate.getFollowUpType();
    }

    public void setFollowUpType(String newType) {
        delegate.setFollowUpType(newType);
    }

    public Date getDueDate() {
        return delegate.getDueDate();
    }

    public void setDueDate(Date newDueDate) {
        delegate.setDueDate(newDueDate);
    }

    /**
     * @deprecated - use getAssignedToWorkPositionId()
     * @return
     */
    public String getAssignedTo() {
        return getAssignedToWorkPositionId();
    }

    public String getAssignedToWorkPositionId() {
        return delegate.getAssignedToWorkPositionId();
    }

    public void setAssignedToWorkPositionId(String assignedToWorkPositionId) {
        if (!assignedToWorkPositionId.equals(getAssignedToWorkPositionId())) {
            getFollowUpUpdateInfo().setAssignedToWorkPositionId(assignedToWorkPositionId);
        }
    }

    /**
     * @deprecated - use setAssignedToWorkPositionId(String assignedToWorkPositionId)
     * @param newAssignedTo
     */
    public void setAssignedTo(String newAssignedTo) {
        setAssignedToWorkPositionId(newAssignedTo);
    }

    public String getSubscriberId() {
        return delegate.getSubscriberId();
    }

    public String getPhoneNumber() {
        return delegate.getPhoneNumber();
    }

    public String getProductType() {
        return delegate.getProductType();
    }

    public void setProductType(String newProductType) {
        delegate.setProductType(newProductType);
    }

    public String getText() {
        return delegate.getText();
    }

    public void setText(String newText) {
        delegate.setText(newText);
    }

    public boolean isConsiderBanStamp() {
        return delegate.isConsiderBanStamp();
    }

    public void setConsiderBanStamp(boolean newConsiderBanStamp) {
        delegate.setConsiderBanStamp(newConsiderBanStamp);
    }

    public char getStatus() {
        return delegate.getStatus();
    }

    public Date getOpenDate() {
        return delegate.getOpenDate();
    }

    public int getOperatorId() {
        return delegate.getOperatorId();
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public String toString() {
        return delegate.toString();
    }

    //--------------------------------------------------------------------
    //  Service Methods
    //--------------------------------------------------------------------
    public void create() throws TelusAPIException {
        try {
            if (followUpUpdateInfo != null) {
                delegate.setAssignedToWorkPositionId(followUpUpdateInfo.getAssignedToWorkPositionId());
            }
            provider.getAccountLifecycleManager().createFollowUp(delegate, SessionUtil.getSessionId( provider.getAccountLifecycleManager()));
            followUpUpdateInfo = null;
        }
        catch(Throwable e) {
        	provider.getExceptionHandler().handleException(e);
        }
    }

    public String getOpenedBy() {
        return delegate.getOpenedBy();
    }

    public String getClosedBy() {
        return delegate.getClosedBy();
    }

    public Date getCloseDate() {
        return delegate.getCloseDate();
    }

    public String getCloseReasonCode() {
        return delegate.getCloseReasonCode();
    }

    public FollowUpText[] getAdditionalText() throws TelusAPIException {
        return getAdditionalText(false);
    }

    public FollowUpText[] getAdditionalText(boolean refresh) throws TelusAPIException {
        FollowUpText[] additionalText = null;

        try {
            additionalText = delegate.getAdditionalText0();

            if (additionalText == null || refresh) {
            	// The method below should not be called until Amdocs changes the method amdocsWorklistConv.viewFollowUpText
            	// to use both ban and Follow Up ID to retrieve additional text and utilise the index in the FOLLOW_UP table.
            	// additionalText = provider.getAccountManagerEJB().retrieveFollowUpAdditionalText(getFollowUpId());
            	List list = provider.getAccountInformationHelper().retrieveFollowUpAdditionalText(getBanId(), getFollowUpId());
            	additionalText= (FollowUpTextInfo[])list.toArray(new FollowUpTextInfo[list.size()]);
            	delegate.setAdditionalText0(additionalText);
            }
        }
        catch(Throwable e) {
        	provider.getExceptionHandler().handleException(e);
        }

        return additionalText;
    }

    public FollowUp[] getHistory() throws TelusAPIException {
        return getHistory(false);
    }

    public FollowUp[] getHistory(boolean refresh) throws TelusAPIException {
        FollowUp[] history = null;

        try {
        	history = delegate.getHistory0();

        	if (history == null || refresh) {
        		FollowUpInfo[] historyInfo= null;
        		List list = provider.getAccountInformationHelper().retrieveFollowUpHistory(getFollowUpId());
        		historyInfo= (FollowUpInfo[])list.toArray(new FollowUpInfo[list.size()]);

        		int historySize = historyInfo != null ? historyInfo.length : 0;

        		history = new TMFollowUp[historySize];

        		for (int i = 0; i < historySize; i++)
        			history[i] = new TMFollowUp(provider, historyInfo[i]);

        		delegate.setHistory0(history);
        	}
        }
        catch(Throwable e) {
        	provider.getExceptionHandler().handleException(e);
        }

        return history;
    }

    public void addText(String text) {
        getFollowUpUpdateInfo().addText(text);
    }

    public void setCloseReasonCode(String closeReasonCode) {
        getFollowUpUpdateInfo().setCloseReasonCode(closeReasonCode);
    }

    public void refresh() throws TelusAPIException {
        try {
            delegate.copyFrom(provider.getAccountInformationHelper().retrieveFollowUpInfoByBanFollowUpID(getBanId(), getFollowUpId()));
        }
        catch(Throwable e) {
        	provider.getExceptionHandler().handleException(e);
        }
    }

    public void save() throws TelusAPIException {
        try {
            if (followUpUpdateInfo != null) {
                String assignedToWorkPositionId = followUpUpdateInfo.getAssignedToWorkPositionId();

                if (assignedToWorkPositionId != null) {
                    WorkPosition newAssignedToWorkPosition = provider.getReferenceDataManager().getWorkPosition(assignedToWorkPositionId);

                    if (newAssignedToWorkPosition != null) {
                        followUpUpdateInfo.setAssignedToDepartmentCode(newAssignedToWorkPosition.getDepartmentCode());
                        followUpUpdateInfo.setAssignedToFunctionCode(newAssignedToWorkPosition.getFunctionCode());
                    }
                    else {
                        throw new TelusAPIException("Invalid Work Position: " + assignedToWorkPositionId);
                    }
                }
                provider.getAccountLifecycleFacade().updateFollowUp(followUpUpdateInfo, SessionUtil.getSessionId( provider.getAccountLifecycleFacade()));
            }

            followUpUpdateInfo = null;
            refresh();
        }catch(Throwable e) {
        	provider.getExceptionHandler().handleException(e);
        }
    }

    private FollowUpUpdateInfo getFollowUpUpdateInfo() {
        if (followUpUpdateInfo == null) {
            followUpUpdateInfo = new FollowUpUpdateInfo(getBanId(), getFollowUpId());
            
            // the following properties are captured to allow back-end to determine whether the follow up is for adjustment or not
            // they are not used when updating the follow up
            followUpUpdateInfo.setFollowUpType( getFollowUpType() );
            followUpUpdateInfo.setFollowUpText( getText() ) ;
            if (delegate.isSubscriberLevel() ) {
            	followUpUpdateInfo.setSubscriberId( delegate.getSubscriberId() );
            	followUpUpdateInfo.setProductType( delegate.getProductType() );
            }
        }

        return followUpUpdateInfo;
    }

	public void approve() throws TelusAPIException {
		followUpUpdateInfo = getFollowUpUpdateInfo();
		followUpUpdateInfo.setIsApproved(true);
		save();
	}
	
	public void deny() throws TelusAPIException {
		followUpUpdateInfo = getFollowUpUpdateInfo();
		followUpUpdateInfo.setIsDenied(true);
		save();
	}
}




