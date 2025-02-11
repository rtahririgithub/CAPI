package com.telus.cmb.reference.dto;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 
 * @author Brandon Wen
 * Mar 27, 2015
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceTermDtoUnitTest {
	private Date expirationDate;
	
	@Test
	public void constructor() {
		assertNotNull(new ServiceTermDto("SIWMP3", 24, "M"));
	}
	
	@Test
	public void calculateExpirationDate_month() {
		expirationDate = new ServiceTermDto("SIWMP3", 24, "M").calculateExpirationDate(getEffectiveDate(2015,3,27));
		assertThat(getYear(expirationDate), equalTo(2017));
		assertThat(getMonth(expirationDate), equalTo(3));
		assertThat(getDay(expirationDate), equalTo(27));
	}
	
	@Test
	public void calculateExpirationDate_day() {
		expirationDate = new ServiceTermDto("SIWMP3", 30, "D").calculateExpirationDate(getEffectiveDate(2015,3,27));
		assertThat(getYear(expirationDate), equalTo(2015));
		assertThat(getMonth(expirationDate), equalTo(4));
		assertThat(getDay(expirationDate), equalTo(26));
	}
	
	
	@Test(expected=RuntimeException.class)
	public void calculateExpirationDate_exception() {
		expirationDate = new ServiceTermDto("SIWMP3", 1, "Y").calculateExpirationDate(new Date());
	}

	private Date getEffectiveDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DATE, day);
		return cal.getTime();
	}
	
	private int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		return cal.get(Calendar.YEAR);
	}
	
	private int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		return cal.get(Calendar.MONTH)+1;
	}

	private int getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		return cal.get(Calendar.DATE);
	}

}
