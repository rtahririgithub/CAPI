package com.telus.cmb.tool.services.log.tasks.notify;

public class EmailComposerFactory {

	private EmailComposerFactory() {		
	}
	
	public static EmailComposer getEmailComposer(String templateName) {
		return getEmailComposer(EmailTemplateEnum.getTemplate(templateName));
	}
	
	public static EmailComposer getEmailComposer(EmailTemplateEnum template) {
		
		switch(template) {
			case simple:
				return new SimpleEmailComposer();
			case cacheRefresh:
				return new CacheRefreshComposer();
			case penaltyFailure:
				return new PenaltyFailureComposer();
			case welcomeEmail:
				return new WelcomeEmailComposer();
			default:
				return new SimpleEmailComposer();
		}
	}

}