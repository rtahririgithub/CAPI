package com.telus.provider.scheduling;

import java.util.Date;

public class NowTrigger extends ATrigger implements ITrigger {
	
	public NowTrigger(long repeatInterval) {
		super(repeatInterval);
	}

	protected Date calculateFirstFireTime() {
		return new Date();
	}
}
