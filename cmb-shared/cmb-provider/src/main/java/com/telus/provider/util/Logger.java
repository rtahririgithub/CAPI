package com.telus.provider.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	public static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd|kk:mm:ss.SSS");
	
	//--------------------------------------------------------------------
	// Logging.
	//--------------------------------------------------------------------
	public static void debug0(Object message) {
		StringBuffer s = new StringBuffer(1024 * 2);

		s.append(LOG_DATE_FORMAT.format(new Date()));
		s.append("|");
		s.append(Thread.currentThread().getName()+": ");
		s.append(message);
		System.err.println(s);
	}

	public static void debug0(Throwable message) {
		StringBuffer s = new StringBuffer(1024 * 2);

		s.append(LOG_DATE_FORMAT.format(new Date()));
		s.append("|");
		s.append(Thread.currentThread().getName()+": ");
		s.append("EXCEPTION: " + message.getMessage());
		System.err.println(s);
		message.printStackTrace(System.err);
	}

	public static void debug(Object message) {
		debug0(message);
	}

	public static void debug(Throwable message) {
		debug0(message);
	}

	public static void warning(Object message) {
		debug0(message);
	}

	public static void warning(Throwable message) {
		debug0(message);
	}
}
