package com.telus.api.account;

import java.util.Date;

public interface MaxVoipLine {
	
	Date getEffectiveStartDate();
	void setEffectiveStartDate(Date effectiveStartDate);

	Date getEffectiveEndDate();
	void setEffectiveEndDate(Date effectiveEndDate);

	long getSubscriptionId();
	void setSubscriptionId(long subscriptionId);

	int getBan();
	void setBan(int ban);

	int getMaxVoipLines();
	void setMaxVoipLines(int maxVoipLines);

	Date getCreateDate();
	void setCreateDate(Date createDate);

	String getCreateUser();
	void setCreateUser(String createUser);

	Date getLastUpdateDate();
	void setLastUpdateDate(Date lastUpdateDate);

	String getLastUpdateUser();
	void setLastUpdateUser(String lastUpdateUser);
	
}