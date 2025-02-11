package com.telus.cmb.tool.services.log.tasks;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.EnumUtils;

public interface ServiceFactory {

	static final String TEST_TASK = "test";
	static final String SIMPLE_TASK = "simple";
	static final String FREQUENCY_TASK = "frequency";
	static final String FREE_FRIES_TASK = "freeFries";
	static final String CACHE_REFRESH_TASK = "cacheRefresh";
	static final String WELCOME_EMAIL_TASK = "welcomeEmail";
	static final String EJB_USAGE_CACHE_TASK = "ejbUsageCache";

	BaseTask getTask(String taskName);

	public enum TaskEnum {
		
		SIMPLE(SIMPLE_TASK, "simpleTask"), 
		FREQUENCY(FREQUENCY_TASK, "frequencyTask"), 
		CACHE_REFRESH(CACHE_REFRESH_TASK, "cacheRefreshTask"), 
		WELCOME_EMAIL(WELCOME_EMAIL_TASK, "welcomeEmailTask"), 
		EJB_USAGE_CACHE(EJB_USAGE_CACHE_TASK, "ejbUsageCacheTask"), 
		FREE_FRIES(FREE_FRIES_TASK, "freeFriesTask"),
		TEST(TEST_TASK, "testTask");

		private String type;
		private String taskName;
	    private static Map<String, TaskEnum> map = new HashMap<String, TaskEnum>();

	    static {
	        for (TaskEnum value : TaskEnum.values()) {
	            map.put(value.type, value);
	        }
	    }
	    
		private TaskEnum(String type, String taskName) {
			this.type = type;
			this.taskName = taskName;
		}

		public String getType() {
			return type;
		}
		
		public String getTaskName() {
			return taskName;
		}
		
		public static TaskEnum getTaskEnumByType(String type) {
			return map.get(type);
		}
		
		public static TaskEnum getTaskEnum(String name) {
			return EnumUtils.getEnum(TaskEnum.class, name);
		}

	}

}