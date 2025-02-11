package com.telus.provider.account;

import com.telus.api.TelusAPIException;
import com.telus.provider.TMProvider;

public class TMProviderTest extends TMProvider {
	static {
		System.out.println("321");
	}
	public TMProviderTest() throws TelusAPIException {
		super("", "", "", new int[0]);
	}
}
