package com.telus.ait.fwk.page;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageObject extends net.thucydides.core.pages.PageObject {
	public PageObject(WebDriver driver) {
		super(driver);
	}

	protected static void waitForLoad(WebDriver driver) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(pageLoadCondition);
	}

	/**
	 * from https://github.com/paul-hammant/ngWebDriver
	 * 
	 * @param driver
	 */
	protected static void waitForAngularRequestsToFinish(JavascriptExecutor driver) {
		driver.executeAsyncScript("var callback = arguments[arguments.length - 1];"
				+ "angular.element(document.body).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);");
	}

}
