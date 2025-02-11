package com.telus.cmb.reference.utilities;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.cmb.common.util.StringUtil;

public class CronExpressionUtil {
	private static final Log logger = LogFactory.getLog(CronExpressionUtil.class);
	public final static int MINUTE = 1;
	
	public static String addRandomMinute(String cronExpression, int maximum) {
		Random r = new Random();
		int addMinute = r.nextInt(maximum + 1);
		logger.info("Adding random minute [" + addMinute +"] to " + cronExpression);
		return addMinute(cronExpression, addMinute);
	}
	/*
	 * This method has low validation which doesn't check all the boundary cases
	 */
	public static String addMinute(String cronExpression, int minuteToAdd) {
		String newExpression;
		
		if (cronExpression == null) {
			return cronExpression;
		}
		
		String [] cronValues = cronExpression.split(" ");
		
		int cronMinute = Integer.parseInt(cronValues[1]);
		int newMinute = cronMinute + minuteToAdd;
		if (newMinute >= 60) {
			int cronHour = Integer.parseInt(cronValues[2]) + 1;
			cronValues[2] = String.valueOf(cronHour);
			newMinute = newMinute - 60;
		}
		cronValues[1] = String.valueOf(newMinute);
		
		newExpression = StringUtil.arrayToString(cronValues, " ");
		
		return newExpression;
		
	}
	
	public static void main (String[] args) {
		String cronExprsesion = "0 45 4 * * ?";
		int maximum = 30;
		Random r = new Random();
		int addMinute = r.nextInt(maximum + 1);
		
		String newExprsesion = addMinute (cronExprsesion, addMinute);
		System.out.println(newExprsesion);
	}
}
