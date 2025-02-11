package com.telus.cmb.tool.services.log.utils;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;

public class StringUtil {

	private static Logger logger = Logger.getLogger(StringUtil.class);
	
	private static CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
	private static CronParser parser = new CronParser(cronDefinition);
	private static CronDescriptor descriptor = CronDescriptor.instance(Locale.ENGLISH);
	
	public static String describeCron(String cron) {
		if (cron != null) {
			try {
				return descriptor.describe(parser.parse(cron));
			} catch (Exception e) {
				logger.info("Error parsing cron string " + cron + " with exception: " + e.getMessage());
			}
		} 
		return "";
	}
	
}
