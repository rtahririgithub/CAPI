package com.telus.cmb.subscriber.lifecyclehelper.dao;

import org.springframework.dao.DataAccessException;

import com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry;

public interface SubscriberInformationCodsDao{
	
	public PhoneDirectoryEntry[] getPhoneDirectory(long subscriptionID) throws DataAccessException;

	public void updatePhoneDirectory(final long subscriptionID, final PhoneDirectoryEntry[] entries) throws DataAccessException;

	public void deletePhoneDirectoryEntries(final long subscriptionID, final PhoneDirectoryEntry[] entries) throws DataAccessException;
}
