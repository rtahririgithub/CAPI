package com.telus.cmb.tool.services.log.controllers;

import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.telus.cmb.tool.services.log.config.FilePathConfig;
import com.telus.cmb.tool.services.log.service.LogFileService;
import com.telus.cmb.tool.services.log.service.TaskSchedulerService;
import com.telus.cmb.tool.services.log.tasks.TaskCredentials;
import com.telus.cmb.tool.services.log.utils.EncryptionUtil;
import com.telus.cmb.tool.services.log.utils.SessionUtil;

@Controller
public class LogMonitoringController {

	private FilePathConfig filePathConfig = FilePathConfig.getInstance();
	
	@Autowired
	private TaskSchedulerService scheduledTaskService;
	
	@Autowired
	private LogFileService logFileConnector;
		
	@RequestMapping("/monitor")
	public ModelAndView dashboard() {		

		ModelAndView model = new ModelAndView("monitor_list");		
		model.getModelMap().addAttribute("enabled", TaskCredentials.getInstance().isInitialized());		
		model.getModelMap().addAttribute("availableTasks", scheduledTaskService.getAvailableTasks());
		model.getModelMap().addAttribute("scheduledTasks", scheduledTaskService.getScheduledTasks());
//		scheduledTaskService.scheduleAllTasks();
		return model;
		
	}
		
	@RequestMapping("/monitor/login")
	public ModelAndView login() {
		if (TaskCredentials.getInstance().isInitialized()) {
			return new ModelAndView("redirect:/monitor");			
		}
		return getLoginModel();
	}

	@RequestMapping("/monitor/authorize")
	public ModelAndView authorizeCredentials(@RequestParam String unixId, @RequestParam String unixPass, @RequestParam String windowsId, @RequestParam String windowsPass, HttpSession session) {

		// For now, we're only authorizing against LAIRD... if needed, extend to all other log servers
		if (SessionUtil.isPassValid(logFileConnector, unixId, unixPass, filePathConfig.getLogServers().getDefaultPRLogServer(), session)
				&& SessionUtil.isPassValid(logFileConnector, windowsId, windowsPass, filePathConfig.getLogServers().getDefaultLogServer(), session)) {
			TaskCredentials.getInstance().setUnixUsername(unixId);
			TaskCredentials.getInstance().setUnixPassword(EncryptionUtil.encryptPassword(unixId, unixPass));
			TaskCredentials.getInstance().setWindowsUsername(windowsId);
			TaskCredentials.getInstance().setWindowsPassword(EncryptionUtil.encryptPassword(windowsId, windowsPass));
			return new ModelAndView("redirect:/monitor");			
		}
		
		ModelAndView model = getLoginModel();
		model.getModelMap().addAttribute("errorMessage", "Your credentials didn't work!  Please try again or request access to the log servers.");		
		return model;
	}

	@RequestMapping("/monitor/task/schedule/{task}")
	public ModelAndView scheduleTask(@PathVariable("task") String taskShortname, HttpSession session) {
		if (TaskCredentials.getInstance().isInitialized()) {
			scheduledTaskService.scheduleTask(taskShortname);
		}
		return new ModelAndView("redirect:/monitor");
	}

	@RequestMapping("/monitor/task/schedule/{task}/{env}")
	public ModelAndView scheduleTask(@PathVariable("task") String taskShortname, @PathVariable("env") String envShortname, HttpSession session) {
		if (TaskCredentials.getInstance().isInitialized()) {
			scheduledTaskService.scheduleTask(taskShortname, Arrays.asList(new String[] {envShortname}));
		}
		return new ModelAndView("redirect:/monitor");
	}

	@RequestMapping("/monitor/task/cancel/{task}/{env}")
	public ModelAndView cancelTask(@PathVariable("task") String taskShortname, @PathVariable("env") String envShortname, HttpSession session) {
		if (TaskCredentials.getInstance().isInitialized()) {
			scheduledTaskService.endTask(taskShortname, envShortname);
		}
		return new ModelAndView("redirect:/monitor");
	}

	@RequestMapping("/monitor/task/execute/{task}/{env}")
	public ModelAndView executeTask(@PathVariable("task") String taskShortname, @PathVariable("env") String envShortname, HttpSession session) {
		if (TaskCredentials.getInstance().isInitialized()) {
			scheduledTaskService.executeTask(taskShortname, envShortname);
		}
		return new ModelAndView("redirect:/monitor");
	}
	
	private ModelAndView getLoginModel() {
		ModelAndView model = new ModelAndView("monitor_login");
		model.getModelMap().addAttribute("requireUnixLogin", true);
		model.getModelMap().addAttribute("requireWindowsLogin", true);
		return model;
	}
	
}
