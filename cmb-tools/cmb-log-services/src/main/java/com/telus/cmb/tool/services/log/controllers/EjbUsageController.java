package com.telus.cmb.tool.services.log.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.telus.cmb.tool.services.log.domain.usage.CacheLock;
import com.telus.cmb.tool.services.log.domain.usage.MonthlyUsage;
import com.telus.cmb.tool.services.log.domain.usage.OperationUsage;
import com.telus.cmb.tool.services.log.domain.usage.UsageCache;
import com.telus.cmb.tool.services.log.domain.usage.UsageResult;
import com.telus.cmb.tool.services.log.service.EjbUsageLogService;
import com.telus.cmb.tool.services.log.service.LogFileService;
import com.telus.cmb.tool.services.log.tasks.TaskCredentials;
import com.telus.cmb.tool.services.log.utils.EjbUsageUtil;
import com.telus.cmb.tool.services.log.utils.EncryptionUtil;
import com.telus.cmb.tool.services.log.utils.SessionUtil;

@Controller
public class EjbUsageController {

	@Autowired
	private LogFileService logFileConnector;
	
	@Autowired
	private EjbUsageLogService ejbUsageLogService;

	private FilePathConfig filePathConfig = FilePathConfig.getInstance();
	private Map<String, UsageCache> cacheMap = new LinkedHashMap<>();

	@RequestMapping("/usage")
	public ModelAndView dashboard() {

		ModelAndView model = new ModelAndView("usage_list");
		model.getModelMap().addAttribute("enabled", TaskCredentials.getInstance().isPRInitialized());
		model.getModelMap().addAttribute("locked", CacheLock.getInstance().isLocked());
		model.getModelMap().addAttribute("cacheList", getCacheList());
		model.getModelMap().addAttribute("availableYears", getAvailableYears());

		return model;
	}

	private List<UsageCache> getCacheList() {
		List<UsageCache> cacheList = Arrays.asList(cacheMap.values().toArray(new UsageCache[0]));
		Collections.sort(cacheList);
		return cacheList;
	}

	private List<String> getAvailableYears() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		return Arrays.asList(new String[] { String.valueOf(currentYear - 1), String.valueOf(currentYear), String.valueOf(currentYear + 1) });
	}
	
	@RequestMapping("/usage/login")
	public ModelAndView login() {
		if (TaskCredentials.getInstance().isPRInitialized()) {
			return new ModelAndView("redirect:/usage/");
		}
		return getLoginModel();
	}

	@RequestMapping("/usage/authorize")
	public ModelAndView authorizeCredentials(@RequestParam String unixId, @RequestParam String unixPass, HttpSession session) {

		// For now, we're only authorizing against LAIRD... if needed, extend to all other log servers
		if (SessionUtil.isPassValid(logFileConnector, unixId, unixPass, filePathConfig.getLogServers().getDefaultPRLogServer(), session)) {
			TaskCredentials.getInstance().setUnixUsername(unixId);
			TaskCredentials.getInstance().setUnixPassword(EncryptionUtil.encryptPassword(unixId, unixPass));
			return new ModelAndView("redirect:/usage/");
		}

		ModelAndView model = getLoginModel();
		model.getModelMap().addAttribute("errorMessage", "Your credentials didn't work!  Please try again or request access to the log servers.");
		return model;
	}

	@RequestMapping(value = "/usage/cache/load", method = RequestMethod.POST)
	public ModelAndView loadUsage(@RequestParam String month, @RequestParam String year, HttpSession session) {
		if (TaskCredentials.getInstance().isPRInitialized() && !CacheLock.getInstance().isLocked() && EjbUsageUtil.validateKey(year, month)) {
			asyncLoadUsage(Arrays.asList(new String[]{EjbUsageUtil.getKey(year, month)}), session);
		}
		return new ModelAndView("redirect:/usage/");
	}

	private void asyncLoadUsage(final List<String> keys, final HttpSession session) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (CacheLock.getInstance().lock()) {
					for (String key : keys) {
						UsageCache cache = cacheMap.get(key);
						if (cache != null && !cache.isComplete()) {
							cacheMap.remove(key);
							cache = null;
						}
						if (cache == null) {
							loadUsageIntoCache(key, SessionUtil.getUserFromSession(session), SessionUtil.getPassFromSession(session, true));
						}
					}
					CacheLock.getInstance().unlock();
				}
			}
		}).start();
	}

	@RequestMapping(value = "/usage/cache/unload/{id}", method = RequestMethod.POST)
	public ModelAndView clearUsage(@PathVariable String id, HttpSession session) {
		if (TaskCredentials.getInstance().isPRInitialized() && !CacheLock.getInstance().isLocked()) {
			cacheMap.remove(id);
		}
		return new ModelAndView("redirect:/usage/");
	}

	@RequestMapping(value = "/usage/cache/load/latest", method = RequestMethod.POST)
	public ModelAndView loadLatestUsageIntoCache(HttpSession session) {
		if (TaskCredentials.getInstance().isPRInitialized() && !CacheLock.getInstance().isLocked()) {
			asyncLoadUsage(EjbUsageUtil.getLatestKeys(), session);
		}
		return new ModelAndView("redirect:/usage/");
	}

	@RequestMapping(value = "/usage/search/results", method = RequestMethod.POST)
	public ModelAndView search(@RequestParam String method, HttpSession session) {

		ModelAndView model = new ModelAndView("usage_results");
		Map<String, UsageResult> resultMap = new LinkedHashMap<>();
		for (UsageCache cache : getCacheList()) {
			for (OperationUsage opUsage : cache.getOperationUsageList()) {
				if (StringUtils.contains(opUsage.getOperationName(), method)) {
					UsageResult usageResult = resultMap.get(opUsage.getOperationName());
					if (usageResult == null) {
						usageResult = new UsageResult();
						usageResult.setOperationName(opUsage.getOperationName());
						usageResult.setMonthlyUsageList(new ArrayList<MonthlyUsage>());
					}
					MonthlyUsage monthlyUsage = new MonthlyUsage();
					monthlyUsage.setId(cache.getId());
					monthlyUsage.setUsageList(opUsage.getUsageList());
					usageResult.getMonthlyUsageList().add(monthlyUsage);
					resultMap.put(opUsage.getOperationName(), usageResult);
				}
			}
		}
		List<UsageResult> usageResultList = Arrays.asList(resultMap.values().toArray(new UsageResult[0]));
		Collections.sort(usageResultList);
		model.getModelMap().addAttribute("usageResultList", usageResultList);
		model.getModelMap().addAttribute("methodText", method);
		
		return model;
	}
	
	private void loadUsageIntoCache(String key, String user, String password) {
		UsageCache usageCache = new UsageCache();
		usageCache.setId(key);
		usageCache.setComplete(EjbUsageUtil.isMonthComplete(key));
		usageCache.setLastDate(Calendar.getInstance().getTime());
		usageCache.setOperationUsageList(getOperationUsageList(key, user, password));
		cacheMap.put(key, usageCache);
	}

	private List<OperationUsage> getOperationUsageList(String key, String user, String password) {
		Map<String, OperationUsage> usageMap = new HashMap<>();		
		File localTempFolder = ejbUsageLogService.updateEjbUsageLogFiles(key, user, password);
		for (File file : localTempFolder.listFiles()) {
			EjbUsageUtil.getOperationUsagesFromFile(usageMap, file);
		}
		return Arrays.asList(usageMap.values().toArray(new OperationUsage[0]));
	}

	private ModelAndView getLoginModel() {
		ModelAndView model = new ModelAndView("usage_login");
		model.getModelMap().addAttribute("requireUnixLogin", true);
		model.getModelMap().addAttribute("requireWindowsLogin", false);
		return model;
	}

}
