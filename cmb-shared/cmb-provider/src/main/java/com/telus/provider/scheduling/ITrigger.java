package com.telus.provider.scheduling;

import java.util.Date;

public interface ITrigger {

	
	Date getNextFireTime();
	void fired();
	
}
