package com.telus.eas.account.info;

import com.telus.api.account.FollowUpText;
import com.telus.eas.framework.info.Info;

import java.util.Date;

public class FollowUpTextInfo extends Info implements FollowUpText {

	 static final long serialVersionUID = 1L;

    private String text;
    private String operatorId;
    private Date createDate;

    public FollowUpTextInfo() {
    }

    public FollowUpTextInfo(Date createDate, String operatorId, String text) {
        this.createDate = createDate;
        this.operatorId = operatorId;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
