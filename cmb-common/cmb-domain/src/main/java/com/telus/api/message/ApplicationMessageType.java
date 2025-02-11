package com.telus.api.message;

import java.util.Hashtable;

/**
 * This class is a typesafe implementation of the enumerable set of application message type.
 * 
 * @author Vladimir Tsitrin
 * @version 1.0, 22-Dec-2005
 * @since 2.863.0.0
 */
public final class ApplicationMessageType {
	
	private final int messageTypeId;
	private final String messageTypeDesc;
	private static final Hashtable messageTypesById;

	private ApplicationMessageType(int messageTypeId, String messageTypeDesc) {
		this.messageTypeId = messageTypeId;
		this.messageTypeDesc = messageTypeDesc;
	}

	public static final int MESSAGE_TYPE_ID_INFORMATION = 1;
	public static final int MESSAGE_TYPE_ID_CONFIRMATION = 2;
	public static final int MESSAGE_TYPE_ID_WARNING = 3;
	public static final int MESSAGE_TYPE_ID_ERROR = 4;
	public static final int MESSAGE_TYPE_ID_VOID = 5;

	public final static ApplicationMessageType INFORMATION = new ApplicationMessageType(
			MESSAGE_TYPE_ID_INFORMATION, "Information.");

	public final static ApplicationMessageType CONFIRMATION = new ApplicationMessageType(
			MESSAGE_TYPE_ID_CONFIRMATION, "Confirmation.");

	public final static ApplicationMessageType WARNING = new ApplicationMessageType(
			MESSAGE_TYPE_ID_WARNING, "Warning.");

	public final static ApplicationMessageType ERROR = new ApplicationMessageType(
			MESSAGE_TYPE_ID_ERROR, "Error.");

	public final static ApplicationMessageType VOID = new ApplicationMessageType(
			MESSAGE_TYPE_ID_VOID, "Void.");
	
	static {
		messageTypesById = new Hashtable();
		messageTypesById.put(Integer.toString(MESSAGE_TYPE_ID_INFORMATION), INFORMATION);
		messageTypesById.put(Integer.toString(MESSAGE_TYPE_ID_CONFIRMATION), CONFIRMATION);
		messageTypesById.put(Integer.toString(MESSAGE_TYPE_ID_WARNING), WARNING);
		messageTypesById.put(Integer.toString(MESSAGE_TYPE_ID_ERROR), ERROR);
		messageTypesById.put(Integer.toString(MESSAGE_TYPE_ID_VOID), VOID);
	}

	public static ApplicationMessageType getApplicationMessageTypeById(int id) {
		return (ApplicationMessageType) messageTypesById.get(Integer.toString(id));
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ApplicationMessageType: [\n");
		sb.append("    messageTypeId: [").append(messageTypeId).append("]\n");
		sb.append("    messageTypeDesc: [").append(messageTypeDesc).append("]\n");
		sb.append("]\n");
		
		return sb.toString();
	}
}