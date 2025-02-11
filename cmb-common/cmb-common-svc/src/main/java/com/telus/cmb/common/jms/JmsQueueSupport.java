package com.telus.cmb.common.jms;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;

public class JmsQueueSupport {
	private static Logger logger = Logger.getLogger(JmsQueueSupport.class);
	
	public JmsQueueSupport() {
		logger.debug("Initializing JmsQueueSupport instance");
	}
	
	@Autowired
	private JmsTemplate jmsTemplate;

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public ArrayList<Message> browseAllMessages()
	{
		return jmsTemplate.browse(new BrowserCallback<ArrayList<Message>>()
		{
			public ArrayList<Message> doInJms(Session session, QueueBrowser browser) throws JMSException
			{
				return getBrowseList(browser);
			}
		});
	}
	
	public ArrayList<Message> browseMessageByCmbJMSType(final String cmbJmsType)
	{
		String messageSelector = "CMB_JMS_messageType = '" + cmbJmsType + "'";
		return browseSelected(messageSelector);
	}
	
	public ArrayList<Message> browseMessageByCmbJMSType(final String cmbJmsType, final String subType)
	{
		String messageSelector = "CMB_JMS_messageType = '" + cmbJmsType + "' AND CMB_JMS_messageSubType = '" + subType + "'";
		return browseSelected(messageSelector);
	}
	
	private ArrayList<Message> browseSelected(String messageSelector) {
		logger.debug("browseSelected() with messageSelector=" + messageSelector);
		return jmsTemplate.browseSelected(messageSelector, new BrowserCallback<ArrayList<Message>>()
		{
			public ArrayList<Message> doInJms(Session session, QueueBrowser browser) throws JMSException
			{
				return getBrowseList(browser);
			}
		});
	}
	
	private ArrayList<Message> getBrowseList(QueueBrowser browser) throws JMSException
	{
		ArrayList<Message> messageList = new ArrayList<Message>();
		Enumeration e = browser.getEnumeration();
		while (e.hasMoreElements()) {
			messageList.add((Message)e.nextElement());
		}
		return messageList;
	}

	public static class QueueHelper {
		private static ApplicationContext applicationContext = null;

		private static ApplicationContext getApplicationContext() {
			if (applicationContext == null)
				applicationContext = new ClassPathXmlApplicationContext("application-context-jms-jndi.xml");
			return applicationContext;
		}

		public static JmsQueueSupport getQueueBean(String queueBeanId) {
			logger.debug("getQueueBean for " + queueBeanId);
			JmsQueueSupport queueBean = getApplicationContext().getBean(queueBeanId, JmsQueueSupport.class);
			logger.debug("got QueueBean instance: " + queueBean);
			return queueBean;
		}
		
	}
}
