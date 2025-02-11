package com.telus.cmb.tool.services.log.domain.usage;

import org.threeten.bp.Duration;
import org.threeten.bp.Instant;

public class CacheLock {

	private static CacheLock INSTANCE = null;
	private Instant lastLockTime;
	private static final long TIMEOUT_IN_MINUTES = 60;

	private CacheLock() {
	}

	public synchronized static CacheLock getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CacheLock();
		}
		return INSTANCE;
	}

	public boolean isLocked() {
		checkTimeout();		
		return this.lastLockTime != null;
	}
	
	private void checkTimeout() {
		if (this.lastLockTime != null && Duration.between(this.lastLockTime, Instant.now()).toMinutes() > TIMEOUT_IN_MINUTES) {
			unlock();
		}
	}
	
	public boolean lock() {
		if (isLocked()) {
			return false;
		}
		this.lastLockTime = Instant.now();
		return true;
	}
	
	public void unlock() {
		this.lastLockTime = null; 
	}
}
