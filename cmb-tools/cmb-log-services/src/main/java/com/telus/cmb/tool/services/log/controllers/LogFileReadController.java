package com.telus.cmb.tool.services.log.controllers;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.telus.cmb.tool.services.log.config.FilePathConfig;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;
import com.telus.cmb.tool.services.log.service.LogFileService;
import com.telus.cmb.tool.services.log.utils.RequestUtil;
import com.telus.cmb.tool.services.log.utils.SessionUtil;

@Controller
public class LogFileReadController {

	private FilePathConfig filePathConfig = FilePathConfig.getInstance();
	
	@Autowired
	private LogFileService logFileService;
	
	@RequestMapping("/read")
	public ModelAndView selectEnvironment() {
		return new ModelAndView("read_choose_env", "logServers", Arrays.asList(filePathConfig.getLogServers().getLogServers()));
	}

	@RequestMapping("/read/{logserver}")
	public ModelAndView selectFileToRead(@PathVariable("logserver") String logserver, HttpServletRequest request, HttpSession session) {
		
		ModelAndView model = checkUrlAndCredentials(logserver, session);
		if (model != null) {
			return model;
		}
		model = new ModelAndView("read_select_criteria");
		model.getModelMap().addAttribute("logServer", filePathConfig.getLogServers().getLogServer(logserver));
		model.getModelMap().addAttribute("filePath", request.getParameter("filePath"));
		model.getModelMap().addAttribute("lineNumber", request.getParameter("lineNumber"));
		
		return model;
	}

	private ModelAndView checkUrlAndCredentials(String logServerName, HttpSession session) {
		
		LogServerInfo logServer = filePathConfig.getLogServers().getLogServer(logServerName);
		if (logServer == null) {
			return new ModelAndView("redirect:/read");
		}
		if (needsToLogin(logServer, session)) {
			return new ModelAndView("redirect:/read/" + logServerName + "/login");
		}
		
		return null;
	}

	private boolean needsToLogin(LogServerInfo logServer, HttpSession session) {
		return (logServer.usesUnixLogin() && !SessionUtil.checkCredentials(session, true)) || (logServer.usesWindowsLogin() && !SessionUtil.checkCredentials(session, false));
	}

	@RequestMapping("/read/{logserver}/login")
	public ModelAndView login(@PathVariable("logserver") String logServerName, @RequestParam(value = "errorMessage", required = false) String errorMessage) {
		
		LogServerInfo logServer = filePathConfig.getLogServers().getLogServer(logServerName);
		if (logServer == null) {
			return new ModelAndView("redirect:/read");
		}
		ModelAndView model = new ModelAndView("read_login");
		model.getModelMap().addAttribute("logServer", logServer);
		model.getModelMap().addAttribute("errorMessage", errorMessage);
		model.getModelMap().addAttribute("requireUnixLogin", logServer.usesUnixLogin());
		model.getModelMap().addAttribute("requireWindowsLogin", logServer.usesWindowsLogin());
		
		return model;
	}

	@RequestMapping("/read/{logserver}/authorize")
	public ModelAndView authorizeCredentials(@PathVariable("logserver") String logServerName, @RequestParam(value = "unixId", required = false) String unixId,
			@RequestParam(value = "unixPass", required = false) String unixPass, @RequestParam(value = "windowsId", required = false) String windowsId,
			@RequestParam(value = "windowsPass", required = false) String windowsPass, HttpSession session) {
				
		LogServerInfo logServer = filePathConfig.getLogServers().getLogServer(logServerName);
		if (logServer == null) {
			return new ModelAndView("redirect:/read");
		}
		String user = logServer.usesUnixLogin() ? unixId : windowsId;
		String pass = logServer.usesUnixLogin() ? unixPass : windowsPass;
		if (!SessionUtil.isPassValid(logFileService, user, pass, logServer, session)) {
			return new ModelAndView("redirect:/read/" + logServerName + "/login?errorMessage=Your credentials didn't work on log server (" + logServer.getHost()
					+ ") - please try again or request access to the log servers.");
		}
		
		return new ModelAndView("redirect:/read/" + logServerName);
	}

	@RequestMapping(value = "/read/{logserver}/results", method = RequestMethod.POST)
	public ModelAndView readLogfile(@PathVariable("logserver") String logServerName, @RequestParam String file, @RequestParam String lineNumber, HttpServletRequest request, HttpSession session)
			throws Exception {
		
		ModelAndView model = checkUrlAndCredentials(logServerName, session);
		if (model != null) {
			return model;
		}

		model = new ModelAndView("read_results");
		model.getModelMap().addAttribute("logfile", file);
		model.getModelMap().addAttribute("selectedLineNumber", lineNumber);
		
		LogServerInfo logServer = filePathConfig.getLogServers().getLogServer(logServerName);
		String user = SessionUtil.getUserFromSession(session);
		String password = SessionUtil.getPassFromSession(session, logServer.usesUnixLogin());
		int lineNum = Integer.parseInt(lineNumber);
		int linesBefore = RequestUtil.getReadNumLinesBefore(request);
		int linesAfter = RequestUtil.getReadNumLinesAfter(request);
		try {
			model.getModelMap().addAttribute("lines", logFileService.readLogFile(logServer, user, password, file, lineNum, linesBefore, linesAfter));
		} catch (Exception e) {
			model.getModelMap().addAttribute("errorMessage", e.getMessage());			
		}
		
		return model;
	}

	@RequestMapping(value = "/read/{logserver}/download", method = RequestMethod.POST)
	public void downloadFile(HttpServletResponse response, @PathVariable("logserver") String logServerName, @RequestParam String file, HttpServletRequest request, HttpSession session) throws Exception {
		
		ModelAndView model = checkUrlAndCredentials(logServerName, session);
		if (model == null) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.substring(file.lastIndexOf("/") + 1) +"\""));
			LogServerInfo logServer = filePathConfig.getLogServers().getLogServer(logServerName);
			String user = SessionUtil.getUserFromSession(session);
			String password = SessionUtil.getPassFromSession(session, logServer.usesUnixLogin());
			logFileService.downloadFile(logServer, user, password, file, response.getOutputStream());
		}
	}

}
