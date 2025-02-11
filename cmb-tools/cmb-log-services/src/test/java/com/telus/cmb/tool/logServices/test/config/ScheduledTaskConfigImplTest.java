package com.telus.cmb.tool.logServices.test.config;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.tool.services.log.config.ScheduledTaskConfig;
import com.telus.cmb.tool.services.log.config.domain.task.Task;
import com.telus.cmb.tool.services.log.config.domain.task.TaskEnvironment;
import com.telus.cmb.tool.services.log.utils.StringUtil;

@Test
@ContextConfiguration("classpath:application-context-test.xml")
public class ScheduledTaskConfigImplTest extends AbstractTestNGSpringContextTests {

	ScheduledTaskConfig scheduledTaskConfig = ScheduledTaskConfig.getInstance();

	@Test
	public void validate_tasks() {

		List<Task> tasks = scheduledTaskConfig.getTaskList();
		
		System.out.println("Tasks:");
		List<String> taskNames = new ArrayList<String>();
		for (Task task : tasks) {
			System.out.println(task.getName() + task.getEnvironmentList().toString());
			taskNames.add(task.getShortname());
		}

		assertThat(taskNames, hasItem("cacheRefresh"));
	}

	@Test
	public void validate_schedule_format() {
		
		List<Task> tasks = scheduledTaskConfig.getTaskList();
		for (Task task : tasks) {
			for (TaskEnvironment taskEnvironment : task.getTaskEnvironments()) {
				System.out.println(StringUtil.describeCron(taskEnvironment.getSchedule().getCron()));
			}
		}
	}


}
