package com.telus.cmb.tool.services.log.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.telus.cmb.tool.services.log.config.FilePathConfig;
import com.telus.cmb.tool.services.log.config.FilePathConfigRT;
import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.config.domain.app.Application;
import com.telus.cmb.tool.services.log.domain.DateRangeFilter;
import com.telus.cmb.tool.services.log.domain.ListFileResult;
import com.telus.cmb.tool.services.log.domain.LogFilePaths;
import com.telus.cmb.tool.services.log.domain.LogSearchResult;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;
import com.telus.cmb.tool.services.log.service.LogFileService;
import com.telus.cmb.tool.services.log.utils.ConfigUtil;
import com.telus.cmb.tool.services.log.utils.RequestUtil;
import com.telus.cmb.tool.services.log.utils.SessionUtil;

@Controller
public class LogFileSearchController {

	private FilePathConfig filePathConfig = FilePathConfig.getInstance();

	@Autowired
	private FilePathConfigRT filePathConfigRT;
	
	@Autowired
	private LogFileService logFileService;

	@RequestMapping("/search")
	public ModelAndView selectEnvironment() {
		return new ModelAndView("search_choose_env", "environments", filePathConfig.getEnvironments());
	}

	@RequestMapping("/search/{env}")
	public ModelAndView selectApplication(@PathVariable("env") String envShortName, HttpSession session) {

		Environment environment = filePathConfig.getEnvironment(envShortName);
		if (environment == null) {
			return new ModelAndView("redirect:/search");
		}
		ModelAndView model = new ModelAndView("search_choose_app");
		model.getModelMap().addAttribute("environment", environment);
		model.getModelMap().addAttribute("applications", filePathConfig.getApplications(envShortName));

		return model;
	}

	@RequestMapping("/search/{env}/{app}")
	public ModelAndView selectCriteriaForSearch(@PathVariable("env") String envShortName, @PathVariable("app") String appShortName, HttpSession session) {

		ModelAndView model = checkUrlAndCredentials(envShortName, appShortName, session, false);
		if (model != null) {
			return model;
		}
		model = new ModelAndView("search_select_criteria");
		model.getModelMap().addAttribute("environment", filePathConfig.getEnvironment(envShortName));
		model.getModelMap().addAttribute("application", filePathConfig.getApplication(envShortName, appShortName));
		model.getModelMap().addAttribute("components", filePathConfig.getComponents(envShortName, appShortName));

		return model;

	}

	// EASTER EGG #2
	@RequestMapping("/search/{env}/{app}/live")
	public ModelAndView selectCriteriaForSearchLive(@PathVariable("env") String envShortName, @PathVariable("app") String appShortName, HttpSession session) {

		ModelAndView model = checkUrlAndCredentials(envShortName, appShortName, session, false);
		if (model != null) {
			return model;
		}
		model = new ModelAndView("search_select_criteria");
		model.getModelMap().addAttribute("environment", filePathConfig.getEnvironment(envShortName));
		model.getModelMap().addAttribute("application", filePathConfig.getApplication(envShortName, appShortName));
		model.getModelMap().addAttribute("components", filePathConfig.getComponents(envShortName, appShortName));
		model.getModelMap().addAttribute("live", "/live");

		return model;

	}

	@RequestMapping("/search/{env}/{app}/login")
	public ModelAndView login(@PathVariable("env") String envShortName, @PathVariable("app") String appShortName, @RequestParam(value = "errorMessage", required = false) String errorMessage) {

		Environment environment = filePathConfig.getEnvironment(envShortName);
		if (environment == null) {
			return new ModelAndView("redirect:/search");
		}

		Set<LogServerInfo> logServers = getLogServers(envShortName, appShortName, false);
		ModelAndView model = new ModelAndView("search_login");
		model.getModelMap().addAttribute("environment", environment);
		model.getModelMap().addAttribute("application", filePathConfig.getApplication(envShortName, appShortName));
		model.getModelMap().addAttribute("errorMessage", errorMessage);
		model.getModelMap().addAttribute("requireUnixLogin", requiresUnixLogin(logServers));
		model.getModelMap().addAttribute("requireWindowsLogin", requiresWindowsLogin(logServers));

		return model;
	}

	@RequestMapping("/search/{env}/{app}/authorize")
	public ModelAndView authorizeCredentials(@PathVariable("env") String envShortName, @PathVariable("app") String appShortName, @RequestParam(value = "unixId", required = false) String unixId,
			@RequestParam(value = "unixPass", required = false) String unixPass, @RequestParam(value = "windowsId", required = false) String windowsId,
			@RequestParam(value = "windowsPass", required = false) String windowsPass, HttpSession session) {

		if (filePathConfig.getEnvironment(envShortName) == null) {
			return new ModelAndView("redirect:/search");
		}
		for (LogServerInfo logServer : getLogServers(envShortName, appShortName, false)) {
			String user = logServer.usesUnixLogin() ? unixId : windowsId;
			String pass = logServer.usesUnixLogin() ? unixPass : windowsPass;
			if (!SessionUtil.isPassValid(logFileService, user, pass, logServer, session)) {
				return new ModelAndView("redirect:/search/" + envShortName + "/" + appShortName + "/login?errorMessage=Your credentials didn't work on log server (" + logServer.getHost()
						+ ") - please try again or request access to the log servers.");
			}
		}

		return new ModelAndView("redirect:/search/" + envShortName + "/" + appShortName);
	}

	@RequestMapping(value = "/search/{env}/{app}/results", method = RequestMethod.POST)
	public ModelAndView searchLogfiles(@PathVariable("env") String envShortName, @PathVariable("app") String appShortName, @RequestParam String componentName, HttpServletRequest request,
			HttpSession session) throws Exception {
		return getLogFileResults(envShortName, appShortName, componentName, request, session, false);
	}

	@RequestMapping(value = "/search/{env}/{app}/results/live", method = RequestMethod.POST)
	public ModelAndView searchLiveLogfiles(@PathVariable("env") String envShortName, @PathVariable("app") String appShortName, @RequestParam String componentName, HttpServletRequest request,
			HttpSession session) throws Exception {
		return getLogFileResults(envShortName, appShortName, componentName, request, session, true);
	}

	private Set<LogServerInfo> getLogServers(String envShortName, String appShortName, boolean useLiveData) {
		if (useLiveData) {
			return filePathConfigRT.getLogServers(envShortName, appShortName);
		} 
		return filePathConfig.getLogServers(envShortName, appShortName);		
	}

	private List<LogFilePaths> getLogFilePathsList(String envShortName, String appShortName, String componentName, boolean useLiveData) {
		if (useLiveData) {
			filePathConfigRT.getFilePaths(envShortName, appShortName, componentName);
		}
		return filePathConfig.getFilePaths(envShortName, appShortName, componentName);
	}
	
	private ModelAndView checkUrlAndCredentials(String envShortName, String appShortName, HttpSession session, boolean useLiveData) {

		Environment environment = filePathConfig.getEnvironment(envShortName);
		Application application = filePathConfig.getApplication(envShortName, appShortName);
		if (environment == null) {
			return new ModelAndView("redirect:/search");
		} else if (application == null) {
			return new ModelAndView("redirect:/search/" + envShortName);
		} else if (needsToLogin(envShortName, appShortName, session, useLiveData)) {
			return new ModelAndView("redirect:/search/" + envShortName + "/" + appShortName + "/login");
		}

		return null;
	}

	private boolean needsToLogin(String envShortName, String appShortName, HttpSession session, boolean useLiveData) {		
		Set<LogServerInfo> logServers = getLogServers(envShortName, appShortName, useLiveData);
		return (requiresUnixLogin(logServers) && !SessionUtil.checkCredentials(session, true)) || (requiresWindowsLogin(logServers) && !SessionUtil.checkCredentials(session, false));
	}

	private boolean requiresUnixLogin(Set<LogServerInfo> logServers) {
		for (LogServerInfo logServer : logServers) {
			if (logServer.usesUnixLogin()) {
				return true;
			}
		}
		return false;
	}

	private boolean requiresWindowsLogin(Set<LogServerInfo> logServers) {
		for (LogServerInfo logServer : logServers) {
			if (logServer.usesWindowsLogin()) {
				return true;
			}
		}
		return false;
	}

	private ModelAndView getLogFileResults(String envShortName, String appShortName, String componentName, HttpServletRequest request, HttpSession session, boolean useLiveData) throws Exception {

		ModelAndView model = checkUrlAndCredentials(envShortName, appShortName, session, false);
		if (model != null) {
			return model;
		}

		// EASTER EGG #1
		Set<String> criteria = RequestUtil.getSearchCriteria(request);
		if (showAllFolders(criteria)) {
			return getLogFileFoldersModel(envShortName, appShortName, componentName, useLiveData);
		} else if (showAllFiles(criteria)) {
			return getLogListFilesModel(envShortName, appShortName, componentName, useLiveData, session);
		}

		Application app = filePathConfig.getApplication(envShortName, appShortName);
		DateRangeFilter dateRangeFilter = RequestUtil.getDateRange(request, ConfigUtil.getTimestampFormat(app, app.getComponent(componentName)));
		int limit = RequestUtil.getSearchLimit(request);
		long startTime = System.currentTimeMillis();
		String user = SessionUtil.getUserFromSession(session);
		List<String> allFilePaths = new ArrayList<String>();
		List<LogSearchResult> results = new ArrayList<LogSearchResult>();
		for (LogFilePaths logFilePaths : getLogFilePathsList(envShortName, appShortName, componentName, useLiveData)) {
			allFilePaths.addAll(logFilePaths.getFilepaths());
			LogServerInfo logServer = logFilePaths.getLogServer();
			results.addAll(logFileService.getSearchResults(logFilePaths.getFilepaths(), logServer, user, SessionUtil.getPassFromSession(session, logServer.usesUnixLogin()), criteria,
					dateRangeFilter, limit));
		}
		long timeElapsed = System.currentTimeMillis() - startTime;

		model = new ModelAndView("search_results");
		model.getModelMap().addAttribute("environment", filePathConfig.getEnvironment(envShortName));
		model.getModelMap().addAttribute("application", filePathConfig.getApplication(envShortName, appShortName));
		model.getModelMap().addAttribute("component", componentName);
		model.getModelMap().addAttribute("dateRange", dateRangeFilter);
		model.getModelMap().addAttribute("time", timeElapsed);
		model.getModelMap().addAttribute("results", results);
		model.getModelMap().addAttribute("filepaths", allFilePaths);
		model.getModelMap().addAttribute("criteria", criteria);

		return model;
	}
	
	private boolean showAllFolders(Set<String> criteria) {
		for (String criterion : criteria) {
			if (StringUtils.equals(criterion, "#showmethefolders")) {
				return true;
			}
		}
		return false;
	}

	private boolean showAllFiles(Set<String> criteria) {
		for (String criterion : criteria) {
			if (StringUtils.equals(criterion, "#showmethefiles")) {
				return true;
			}
		}
		return false;
	}

	private ModelAndView getLogListFilesModel(String envShortName, String appShortName, String componentName, boolean useLiveData, HttpSession session) throws Exception {

		List<ListFileResult> listFileResults = new ArrayList<ListFileResult>();
		for (LogFilePaths logFilePaths : getLogFilePathsList(envShortName, appShortName, componentName, useLiveData)) {
			LogServerInfo logServer = logFilePaths.getLogServer();
			Set<String> folders = new HashSet<String>();
			for (String filePath : logFilePaths.getFilepaths()) {
				folders.add(filePath.substring(0, filePath.lastIndexOf("/") + 1));
			}
			String password = SessionUtil.getPassFromSession(session, logServer.usesUnixLogin());
			listFileResults.addAll(logFileService.getFilenames(new ArrayList<String>(folders), logServer, SessionUtil.getUserFromSession(session), password, ""));
		}

		ModelAndView model = new ModelAndView("search_listfiles");
		model.getModelMap().addAttribute("environment", filePathConfig.getEnvironment(envShortName));
		model.getModelMap().addAttribute("application", filePathConfig.getApplication(envShortName, appShortName));
		model.getModelMap().addAttribute("component", componentName);
		model.getModelMap().addAttribute("listresults", listFileResults);

		return model;
	}

	private ModelAndView getLogFileFoldersModel(String envShortName, String appShortName, String componentName, boolean useLiveData) {

		List<String> allFilePaths = new ArrayList<String>();
		Map<String, List<String>> filesMapByFolder = new LinkedHashMap<String, List<String>>();
		for (LogFilePaths logFilePaths : getLogFilePathsList(envShortName, appShortName, componentName, useLiveData)) {
			for (String filePath : logFilePaths.getFilepaths()) {
				String folder = filePath.substring(0, filePath.lastIndexOf("/"));
				String filename = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
				List<String> files = filesMapByFolder.get(folder);
				if (files == null) {
					files = new ArrayList<String>();
				}
				if (!filename.isEmpty()) {
					files.add(filename);
				}
				filesMapByFolder.put(folder, files);
			}
		}
		for (String folder : filesMapByFolder.keySet()) {
			String files = filesMapByFolder.get(folder).isEmpty() ? "" : "{" + StringUtils.join(filesMapByFolder.get(folder), ",") + "}";
			allFilePaths.add(folder + "/" + files);
		}

		ModelAndView model = new ModelAndView("search_logfilepaths");
		model.getModelMap().addAttribute("environment", filePathConfig.getEnvironment(envShortName));
		model.getModelMap().addAttribute("application", filePathConfig.getApplication(envShortName, appShortName));
		model.getModelMap().addAttribute("component", componentName);
		model.getModelMap().addAttribute("filepaths", allFilePaths);

		return model;
	}

}
