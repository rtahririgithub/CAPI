package com.telus.ait.fwk.test;

import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.ManagedPages;
import net.thucydides.core.pages.Pages;
import org.openqa.selenium.WebDriver;

public abstract class WebAppBaseTest extends BaseTest {
	@Managed
	protected WebDriver driver;

	@ManagedPages
	protected Pages pages;

	protected boolean isHabitatLoginRequired() {
		boolean skipHabitatLogin = getPropertyValue("skipHabitatLogin", false);
		return !skipHabitatLogin;
	}

}
