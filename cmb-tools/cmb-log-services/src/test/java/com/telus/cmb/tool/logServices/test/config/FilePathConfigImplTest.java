package com.telus.cmb.tool.logServices.test.config;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.tool.services.log.config.FilePathConfig;
import com.telus.cmb.tool.services.log.config.FilePathConfigRT;
import com.telus.cmb.tool.services.log.config.domain.AppMap;
import com.telus.cmb.tool.services.log.config.domain.AppMapRepo;
import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.config.domain.app.Application;
import com.telus.cmb.tool.services.log.config.domain.app.Artifact;
import com.telus.cmb.tool.services.log.config.domain.app.Component;
import com.telus.cmb.tool.services.log.dao.EmtoolsDao;
import com.telus.cmb.tool.services.log.domain.LogFilePaths;
import com.telus.cmb.tool.services.log.service.LogFileService;

@Test
@ContextConfiguration("classpath:application-context-test.xml")
public class FilePathConfigImplTest extends AbstractTestNGSpringContextTests {

	private static final String APPMAP_FILENAME = "appMapRepo.xml";
	private static final String APPMAP_CONFIG_OUTPUT_FILE = "C:/tfs/BT/Cust/CMB/main_dev/cmb-tools/cmb-log-services/src/main/resources/config/xml/" + APPMAP_FILENAME;	
//	private static final String APPMAP_CONFIG_OUTPUT_FILE = "C:/work/infra/temp/" + APPMAP_FILENAME;

	FilePathConfig filePathConfig = FilePathConfig.getInstance();

	@Autowired
	FilePathConfigRT filePathConfigRT;

	@Autowired
	LogFileService logFileService;

	@Autowired
	EmtoolsDao emtoolsDao;

	@Test
	public void validate_environments() {

		List<Environment> environments = filePathConfig.getEnvironments();

		System.out.println("Environments:");
		for (Environment environment : filePathConfig.getEnvironments()) {
			System.out.println(environment.getShortname() + " - " + environment.getName());
		}

		List<String> environmentNames = new ArrayList<String>();
		for (Environment environment : environments) {
			environmentNames.add(environment.getShortname());
		}

		assertThat(environmentNames, hasItem("pra"));
	}

	@Test
	public void validate_applications() {

		List<Application> applications = filePathConfig.getApplications("pra");

		System.out.println("Applications:");
		for (Application application : filePathConfig.getApplications("pra")) {
			System.out.println(application.getShortname() + " - " + application.getName());
		}

		List<String> applicationNames = new ArrayList<String>();
		for (Application application : applications) {
			applicationNames.add(application.getShortname());
		}

		assertThat(applicationNames, hasItem("capiejbs"));
	}

	@Test
	public void validate_components() {

		List<String> components = filePathConfig.getComponents("pra", "capiejbs");

		System.out.println("Components:");
		for (String component : components) {
			System.out.println(component);
		}
		assertThat(components, hasItem("CMB Subscriber EJB"));
	}

	@Test
	public void validate_file_paths() {
		String expectedFilePath = "/logsa/lp97814/logs/CustomerInformationManagement2Srv5/applications/cmb-account-svc-lite-1.0/rolling.log*";
		List<LogFilePaths> logFilePaths = filePathConfig.getFilePaths("pra", "capiejbs", "CMB Account EJB");
		verify_file_paths(logFilePaths, expectedFilePath, "lairdpr");
	}

	@Test
	public void validate_live_file_paths() {
		String expectedFilePath = "/shared/logs/ITS04B/btln002039/wls/ST101/logs/applications/ITS04BEntCustomerManagementFulfillmentSvcQ/ContactManagementSrv1/ENDRS-1_0/*";
		List<LogFilePaths> logFilePaths = filePathConfigRT.getFilePaths("st101b", "enp", "Dispatch REST Service v1.0");
		verify_file_paths(logFilePaths, expectedFilePath, "qidc");
	}

	private void verify_file_paths(List<LogFilePaths> logFilePaths, String expectedFilePath, String logServerShortname) {

		List<String> filePaths = new ArrayList<String>();
		for (LogFilePaths logFilePath : logFilePaths) {
			if (StringUtils.equalsIgnoreCase(logFilePath.getLogServer().getShortname(), logServerShortname)) {
				filePaths.addAll(logFilePath.getFilepaths());
				System.out.println(logFilePath.getFilepaths().toString());
			}
		}

		assertThat(filePaths, hasItem(expectedFilePath));
	}

	@Test(enabled = false)
	public void generate_appmap_config() throws JAXBException, FileNotFoundException {

		List<AppMap> appMapList = new ArrayList<AppMap>();
		List<String> artifactList = new ArrayList<String>();
		for (Application application : filePathConfig.getApplications("pra")) {
			for (Component component : application.getComponents()) {
				for (Artifact artifact : component.getArtifacts()) {
					if (!artifactList.contains(artifact.getAppMapName())) {
						appMapList.addAll(fixErroneousMappings(emtoolsDao.getAppMap(artifact.getAppMapName())));
						artifactList.add(artifact.getAppMapName());
					}
				}
			}
		}

		AppMapRepo appMapRepo = new AppMapRepo();
		appMapRepo.setAppMapList(appMapList);
		JAXBContext jc = JAXBContext.newInstance(AppMapRepo.class);

		FileOutputStream outputStream = new FileOutputStream(APPMAP_CONFIG_OUTPUT_FILE);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(appMapRepo, outputStream);
	}

	// NOTE: These exceptions need to be fixed in AppMap
	private List<AppMap> fixErroneousMappings(List<AppMap> appMapList) {

		List<AppMap> trueAppMapList = new ArrayList<AppMap>();
		for (AppMap appMap : appMapList) {
			if (StringUtils.equals(appMap.getName(), "AccountLifeCycleManagementService 10") && StringUtils.endsWith(appMap.getDomain(), "PrepaidManagementSvc")) {
				continue;
			} else if (StringUtils.equals(appMap.getName(), "cmb-subscriber-mgmt-ws-4-1") && StringUtils.endsWith(appMap.getDomain(), "CustomerManagementOSRSvc")) {
				continue;
			} else if (StringUtils.equals(appMap.getName(), "cmb-billing-inquiry-ws") && StringUtils.endsWith(appMap.getDomain(), "CustomerManagementOSRSvc")) {
				continue;
			} else if (appMap.getHost() == null) {
				continue;
			}
			trueAppMapList.add(appMap);
		}

		return trueAppMapList;
	}

	protected List<AppMap> getMissingMappings(String artifactName) {

		List<AppMap> appMapList = new ArrayList<AppMap>();
		if (StringUtils.equals(artifactName, "ssf-eas")) {
			appMapList.add(createAppMap("ssf-eas", "prb", "PRBServiceManagementFulfillmentSvc", "ServiceOrderManagement", "ServiceOrderManagementSrv1", "lp99551"));
			appMapList.add(createAppMap("ssf-eas", "prb", "PRBServiceManagementFulfillmentSvc", "ServiceOrderManagement", "ServiceOrderManagementSrv2", "lp98072"));
			appMapList.add(createAppMap("ssf-eas", "prb", "PRBServiceManagementFulfillmentSvc", "ServiceOrderManagement", "ServiceOrderManagementSrv3", "lp97716"));
			appMapList.add(createAppMap("ssf-eas", "prb", "PRBServiceManagementFulfillmentSvc", "ServiceOrderManagement", "ServiceOrderManagementSrv4", "lp99445"));
			appMapList.add(createAppMap("ssf-eas", "prb", "PRBServiceManagementFulfillmentSvc", "ServiceOrderManagement", "ServiceOrderManagementSrv5", "lp97721"));
			appMapList.add(createAppMap("ssf-eas", "prb", "PRBServiceManagementFulfillmentSvc", "ServiceOrderManagement", "ServiceOrderManagementSrv6", "lp99516"));
		}

		return appMapList;
	}

	private AppMap createAppMap(String name, String environment, String domain, String cluster, String node, String host) {

		AppMap appMap = new AppMap();
		appMap.setName(name);
		appMap.setEnvironment(environment);
		appMap.setDomain(domain);
		appMap.setCluster(cluster);
		appMap.setNode(node);
		appMap.setHost(host);

		return appMap;
	}

	/**
	 * Use this test method to encode your passwords
	 */
	@Test
	public void encodeBase64() {
		String[] passwords = new String[] { "yourpasswords" };
		for (String password : passwords) {
			System.out.println("Your encoded password is: " + Base64.encodeBase64String(password.getBytes()));
		}
	}

	/**
	 * This test case will iterate through all the configured filepaths to make
	 * sure they exist on each log server. You need to enable the test (test
	 * should take around 10 minutes).
	 * 
	 * @throws Exception
	 */
	@Test(enabled = false)
	public void verify_file_paths_exists() throws Exception {

		// Enter your credentials here (use the above encoding method for your passwords)		
		// --------------------------------------------------------
		// ======> DON'T check in your passwords!!!!
		// --------------------------------------------------------
		// Available environments: "pra", "prb", "pt140", "pt148", "pt168", "sta", "stb", "dev", "st", "pr"
		// Available applications: "capiejbs", "cisws", "cmbactws", "cmbbilws", "cmbrefws", "cmbsubws", "enp", "ssf", "wdts", "wps", "wsois"
		String username = "x-id_or_t-id";
		String unixEncodedPassword = "encoded_password";
		String windowsEncodedPassord = "encoded_password";
		List<String> environments = Arrays.asList(new String[] { "prb" });
		List<String> applications = Arrays.asList(new String[] { "capiejbs", "cisws", "cmbactws", "cmbbilws", "cmbrefws", "cmbsubws", "wdts" });

		System.out.println("Running filepath verification test...");
		String unixPassword = new String(Base64.decodeBase64(unixEncodedPassword.getBytes()));
		String windowsPassword = new String(Base64.decodeBase64(windowsEncodedPassord.getBytes()));
		List<String> invalidFolders = new ArrayList<String>();
		for (Environment environment : filePathConfig.getEnvironments()) {
			String envShortname = environment.getShortname();
			if (CollectionUtils.isEmpty(environments) || environments.contains(envShortname)) {
				for (Application application : filePathConfig.getApplications(envShortname)) {
					String appShortname = application.getShortname();
					if (CollectionUtils.isEmpty(applications) || applications.contains(appShortname)) {
						validateFilePaths(envShortname, appShortname, username, unixPassword, windowsPassword, invalidFolders);
					}
				}
			}
		}
		System.out.println(invalidFolders.toString());
		assertThat(invalidFolders.isEmpty(), is(true));
	}

	private void validateFilePaths(String envShortname, String appShortname, String username, String unixPassword, String windowsPassword, List<String> invalidFolders) throws Exception {

		for (String component : filePathConfig.getComponents(envShortname, appShortname)) {
			for (LogFilePaths filePaths : filePathConfig.getFilePaths(envShortname, appShortname, component)) {
				String password = filePaths.getLogServer().usesUnixLogin() ? unixPassword : windowsPassword;
				for (String filePath : filePaths.getFilepaths()) {
					if (!logFileService.doesFolderExist(filePaths.getLogServer(), username, password, filePath)) {
						invalidFolders.add(filePaths.getLogServer().getHost() + ":" + filePath);
					}
				}
			}
		}
	}

}
