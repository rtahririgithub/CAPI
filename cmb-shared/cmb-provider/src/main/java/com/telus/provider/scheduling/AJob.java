package com.telus.provider.scheduling;

public abstract class AJob implements IJob {

	public String getName() {
		return this.getClass().getName();
	}

}
