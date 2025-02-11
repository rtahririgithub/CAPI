package com.telus.cmb.tool.services.log.tasks.notify;

import org.apache.commons.lang3.EnumUtils;

public enum EmailTemplateEnum {

	simple, cacheRefresh, welcomeEmail, penaltyFailure;

	public static EmailTemplateEnum getTemplate(String name) {
		return EnumUtils.getEnum(EmailTemplateEnum.class, name);
	}
}
