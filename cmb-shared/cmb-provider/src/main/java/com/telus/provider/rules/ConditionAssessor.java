package com.telus.provider.rules;

import java.lang.reflect.Constructor;

import com.telus.api.TelusAPIException;
import com.telus.api.rules.RulesException;

public abstract class ConditionAssessor {

	protected String conditionId;
	
	public ConditionAssessor(String conditionId) {
		this.conditionId = conditionId;
	}
	
	public String getConditionId() {
		return conditionId;
	}

	/**
	 * This method provides allows an assessor to use other assessors as helper classes.  The helper assessor is instantiated
	 * using reflection and then cached in the working memory, using its full package name as the key.
	 * 
	 * @param String className
	 * @param WorkingMemory workingMemory
	 * @return ConditionAssessor
	 * @throws RulesException
	 * @throws TelusAPIException
	 */
	protected ConditionAssessor getHelperAssessor(String className, WorkingMemory workingMemory) 
	throws RulesException, TelusAPIException {
		
		try {
			WorkingMemoryElement assessorElement = (WorkingMemoryElement)workingMemory.getAttribute(className);
			if (assessorElement == null) {
				Class assessorClass = Class.forName(className);
				Constructor constructor = assessorClass.getConstructor(new Class[] {String.class});
				ConditionAssessor assessor = (ConditionAssessor)constructor.newInstance(new Object[] {assessorClass.getName()});
				assessorElement = new WorkingMemoryElement("ConditionAssessor", assessor);
				workingMemory.putAttribute(assessorClass.getName(), assessorElement);
			}

			return (ConditionAssessor)assessorElement.getElement();
		
		} catch(ClassNotFoundException cnfe) {
			throw new RulesException("Assessor class " + className + " instantiation failed.", RulesException.REASON_ASSESSOR_CLASS_INSTANTIATION_FAILURE, 
					cnfe);			
		} catch(Throwable t) {
			throw new TelusAPIException(t);
		}
	}
	
	public abstract ConditionResult evaluate(WorkingMemory workingMemory) throws TelusAPIException;
	
}
