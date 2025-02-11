package com.telus.ait.fwk.logging;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AOM Logger
 * <p>
 * Commons logging.xml with name/value pair support.
 * </p>
 * 
 */
public class AITLogger {

	public final static String LOG_MONITOR = "[AIT]-";
	private Log log = null;

	@SuppressWarnings("rawtypes")
	public AITLogger(Class clazz) {
		log = LogFactory.getLog(clazz);
	}

	/**
	 * 
	 * @param message
	 *            can't be null
	 * @param args
	 *            name/value pairs. e.g: logger.trace("message", "name1",
	 *            "value1",...);
	 */
	public void trace(String message, Object... args) {
		Validate.notNull(message);

		String outMessage = getMessage(message, args);
		log.trace(outMessage);
	}

	/**
	 * 
	 * @param message
	 *            message can't be null
	 * @param args
	 *            name/value pairs. e.g: logger.debug("message", "name1",
	 *            "value1",...);
	 */
	public void debug(String message, Object... args) {
		Validate.notNull(message);

		String outMessage = getMessage(message, args);
		log.debug(outMessage);
	}

	/**
	 * 
	 * @param message
	 *            message can't be null
	 * @param args
	 *            name/value pairs. e.g: logger.debug("message", "name1",
	 *            "value1",...);
	 */
	public void debug(String message, Throwable t, Object... args) {
		Validate.notNull(message);

		String outMessage = getMessage(message, args);
		log.debug(outMessage, t);
	}

	/**
	 * 
	 * @param message
	 *            message can't be null
	 * @param args
	 *            name/value pairs. e.g: logger.warn("message", "name1",
	 *            "value1",...);
	 */
	public void warn(String message, Object... args) {
		Validate.notNull(message);

		String outMessage = getMessage(message, args);
		log.warn(outMessage);
	}

	public void warn(String message, Throwable t, Object... args) {
		Validate.notNull(message);

		String outMessage = getMessage(message, args);
		log.warn(outMessage, t);
	}

	/**
	 * 
	 * @param message
	 *            message can't be null
	 * @param args
	 *            name/value pairs. e.g: logger.error("message", "name1",
	 *            "value1",...);
	 */
	public void error(String message, Object... args) {
		Validate.notNull(message);

		String outMessage = getMessage(message, args);
		log.error(outMessage);
	}

	/**
	 * 
	 * @param message
	 *            message can't be null
	 * @param args
	 *            name/value pairs. e.g: logger.error("message", "name1",
	 *            "value1",...);
	 */
	public void error(String message, Throwable t, Object... args) {
		Validate.notNull(message);

		String outMessage = getMessage(message, args);
		log.error(outMessage, t);
	}

	/**
	 * 
	 * @param message
	 *            message can't be null
	 * @param args
	 *            name/value pairs. e.g: logger.info("message", "name1",
	 *            "value1",...);
	 */
	public void info(String message, Object... args) {
		Validate.notNull(message);

		String outMessage = getMessage(message, args);
		log.info(outMessage);
	}

	/**
	 * 
	 * @param message
	 *            message can't be null
	 * @param args
	 *            name/value pairs. e.g: fatal("message", "name1",
	 *            "value1",...);
	 */
	public void fatal(String message, Object... args) {
		Validate.notNull(message);

		String outMessage = getMessage(message, args);
		log.fatal(outMessage);
	}

	/**
	 * 
	 * @param message
	 *            message can't be null
	 * @param args
	 *            name/value pairs. e.g: fatal("message", "name1",
	 *            "value1",...);
	 */
	public void fatal(String message, Throwable t, Object... args) {
		Validate.notNull(message);

		String outMessage = getMessage(message, args);
		log.fatal(outMessage, t);
	}

	private String getMessage(String message, Object... args) {
		StringBuilder builder = new StringBuilder(message);
		if (!builder.toString().endsWith(".")) {
			builder.append(". ");
		}

		int i = 1;
		for (Object arg : args) {
			if (i % 2 == 0) {
				builder.append(arg == null ? arg : arg.toString());
				if (i < args.length) {
					builder.append(",");
				} else {
					builder.append(".");
				}
			} else {
				builder.append(" ");
				builder.append(arg == null ? arg : arg.toString());
				builder.append(" := ");
			}

			i++;
		}
		return builder.toString();
	}

}
