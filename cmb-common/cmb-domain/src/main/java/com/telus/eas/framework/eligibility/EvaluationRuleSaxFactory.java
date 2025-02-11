package com.telus.eas.framework.eligibility;

import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.telus.eas.framework.eligibility.rules.EvaluationRule;

public abstract class EvaluationRuleSaxFactory  extends DefaultHandler implements EvaluationRuleFactory {
	private static Map methodRegistry = Collections.synchronizedMap( new HashMap());

	private Stack ruleStack = new Stack();
	private EvaluationRule rootRule = null; 

	public EvaluationRuleSaxFactory(Reader reader) throws Exception {
		parse( new InputSource(reader));
	}

	private void parse(InputSource inputSource) throws Exception {
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(inputSource, this);
	}
	
	protected abstract Map getRulesRegistry();
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.provider.eligibility.interservice.impl.rules.EvaluationRuleFactory#createEvaluationRule()
	 */
	public EvaluationRule createEvaluationRule() {
		return rootRule;
	}
	
	private String buildMethodName(String prefix, String propertyName) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(prefix);
		buffer.append(Character.toUpperCase(propertyName.charAt(0)));
		buffer.append(propertyName.substring(1));
		
		return buffer.toString();
	}
	
	private Class getRuleClass(String ruleName) {
		Class ruleClass = (Class)getRulesRegistry().get(ruleName);

		if (ruleClass == null) {
			throw new RuntimeException("Unknown rule [" + ruleName + "]");
		}
		
		return ruleClass;
	}
	
	private EvaluationRule createRule(String ruleName) {
		try {
			EvaluationRule rule = (EvaluationRule)getRuleClass(ruleName).newInstance();
			rule.setName(ruleName);
			return rule;
			
		} catch (Exception e) {
			throw new RuntimeException("Unable to instantiate rule for name [" + ruleName + "]");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {
		
//		System.out.println("Creating evaluation rule for name [" + name + "]...");

		EvaluationRule rule = createRule(name);
		
		for (int idx = 0; idx < attributes.getLength(); idx++) {
			String propertyName = attributes.getQName(idx);
			String propertyValue = attributes.getValue(idx);

			setRuleProperty(rule, propertyName, propertyValue);
		}
		
		if (rootRule == null) {
			rootRule = rule;
		}
		
		if (!ruleStack.isEmpty()) {
			EvaluationRule parentRule = (EvaluationRule) ruleStack.peek();
			parentRule.addEvaluationRule(rule);
		}
		
		ruleStack.push(rule);
	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (!ruleStack.isEmpty()) {
			ruleStack.pop();
		}
	}
	
	private void setRuleProperty(EvaluationRule rule, String propertyName, String propertyValue) {
		
//		System.out.println("Rule [" + rule.getName() + "]: setting property [" + propertyName + "] to value [" + propertyValue + "]");
		
		try {
			
			Method setter = (Method) methodRegistry.get(propertyName);
			
			if (setter == null) {
				
				String methodName = buildMethodName("set", propertyName);
				
				Method [] methods = rule.getClass().getMethods();
				for (int idx = 0; idx < methods.length; idx++) {
					if (methods[idx].getName().equalsIgnoreCase(methodName)) {
						setter = methods[idx];
						break;
					}
				}
				
				if (setter == null) {
					throw new RuntimeException("Unable to find setter method for property [" + propertyName + "] in class [" + rule.getClass() + "]");
				}

				methodRegistry.put(propertyName, setter);
			}
			
			if (setter != null) {
				
				Class paramType = setter.getParameterTypes()[0];
				
				Object value = null;
				
				if (paramType.equals(Double.class)) {
					value = new Double(propertyValue);
				} else if (paramType.equals(Integer.class)) {
					value = new Integer(propertyValue);
				} else if (paramType.equals(Boolean.class)) {
					value = new Boolean(propertyValue);
				} else {
					value = propertyValue;
				}
				
				setter.invoke(rule, new Object [] {value});
			} else {
				throw new IllegalArgumentException("Unknown property [" + propertyName + "]");
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Unable set property [" + propertyName + "] on [" + rule.getClass() + "] to value [" + propertyValue + "]: " + e.getMessage());
		}
	}
	

}
