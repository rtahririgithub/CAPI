package com.telus.cmb.testsuite.shakedown;

import java.util.ArrayList;
import java.util.List;

import weblogic.management.MBeanHome;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;

import com.telus.cmb.common.shakedown.EjbShakedown;
import com.telus.cmb.shakedown.info.ComponentInfo;
import com.telus.cmb.shakedown.info.ServerInfo;
import com.telus.cmb.shakedown.utilities.ComponentManager;
import com.telus.cmb.shakedown.utilities.ConfigurationLdapReader;
import com.telus.cmb.shakedown.utilities.Constants;
import com.telus.cmb.shakedown.utilities.ServerInfoUtilities;
import com.telus.cmnb.armx.agent.shakedown.ShakedownResult;

public abstract class EnvBaseTest {

	String domainName;
	ConfigurationLdapReader ldapReader = new ConfigurationLdapReader(); 

	public void testAll() throws Exception {
		testAllAccountEJBs();
		testAllProductEJBs();
		testAllReferenceEJBs();
		testAllSubscriberEJBs();
		testAllUtilityEJBs();
	}
	
	public void testAllVersions() throws Exception {
		testVersion("AccountInformationHelper");
		testVersion("AccountLifecycleManager");
		testVersion("AccountLifecycleFacade");
		testVersion("ProductEquipmentHelper");
		testVersion("ProductEquipmentLifecycleFacade");
		testVersion("ProductEquipmentManager");
		testVersion("ReferenceDataHelper");
		testVersion("ReferenceDataFacade");
		testVersion("SubscriberLifecycleFacade");
		testVersion("SubscriberLifecycleHelper");
		testVersion("SubscriberLifecycleManager");
		testVersion("ConfigurationManagerEJB");
		testVersion("ContactEventManagerEjb");
		testVersion("DealerManagerEjb");
		testVersion("QueueEventManagerEjb");
	}

	public void testAllAccountEJBs() throws Exception {
		testAccountInformationHelper();
		testAccountLifecycleFacade();
		testAccountLifecycleManager();		
	}
	
	public void testAllProductEJBs() throws Exception {
		testProductEquipmentHelper();
		testProductEquipmentLifecycleFacade();
		testProductEquipmentManager();
	}
	
	public void testAllReferenceEJBs() throws Exception {
		testReferenceDataFacade();
		testReferenceDataHelper();
	}
	
	public void testAllSubscriberEJBs() throws Exception {
		testSubscriberLifecycleHelper();
		testSubscriberLifecycleFacade();
		testSubscriberLifecycleManager();
	}
	
	public void testAllUtilityEJBs() throws Exception {
		testConfigurationManagerEJB();
		testContactEventManagerEJB();
		testDealerManagerEJB();
		testQueueEventManagerEJB();
	}
	
	public void testAccountInformationHelper() throws Exception {
		testEjb("AccountInformationHelper");
	}
		
	public void testAccountLifecycleManager() throws Exception {
		testEjb("AccountLifecycleManager");
	}
	
	public void testAccountLifecycleFacade() throws Exception {
		testEjb("AccountLifecycleFacade");
	}
	
	public void testProductEquipmentHelper() throws Exception {
		testEjb("ProductEquipmentHelper");
	}
	
	public void testProductEquipmentLifecycleFacade() throws Exception {
		testEjb("ProductEquipmentLifecycleFacade");
	}
	
	public void testProductEquipmentManager() throws Exception {
		testEjb("ProductEquipmentManager");
	}
	
	public void testReferenceDataHelper() throws Exception {
		testEjb("ReferenceDataHelper");
	}
	
	public void testReferenceDataFacade() throws Exception {
		testEjb("ReferenceDataFacade");
	}
 
	public void testSubscriberLifecycleFacade() throws Exception {
		testEjb("SubscriberLifecycleFacade");
	}
	
	public void testSubscriberLifecycleHelper() throws Exception {
		testEjb("SubscriberLifecycleHelper");
	}
	
	public void testSubscriberLifecycleManager() throws Exception {
		testEjb("SubscriberLifecycleManager");
	}
	
	public void testConfigurationManagerEJB() throws Exception {
		testEjb("ConfigurationManager");
	}
	
	public void testContactEventManagerEJB() throws Exception {
		testEjb("ContactEventManager");
	}
	
	public void testDealerManagerEJB() throws Exception {
		testEjb("DealerManager");
	}
	
	public void testQueueEventManagerEJB() throws Exception {
		testEjb("QueueEventManager");
	}

	
	protected void testEjbTestCase(String testName, String targetUrl, String[] targetClusters, EjbTestSuiteCallback testSuiteCallback) {
		System.out.println("Testing "+testName+"..");
		
		if (targetClusters != null && targetClusters.length > 0 && "true".equalsIgnoreCase(System.getProperty("useLdapUrl")) == false) {
			for (String targetCluster : targetClusters) {
				List<ServerInfo> servers = retrieveServerInfo(targetUrl, targetCluster);

				for (ServerInfo server : servers) {System.out.println(" >>>>>>> server " + server);
					String nodeUrl = "t3s://" + server.getServerAddress() + ":" + server.getServerPort();
					System.out.println("Testing " + nodeUrl + ", Cluster=" + targetCluster);
					List<ShakedownResult> resultList = testSuiteCallback.execute(server);
					printShakeDownResult(resultList);
				}
			}
		} else {
			System.out.println("Testing " + targetUrl);
			ServerInfo server = new ServerInfo(null, null, null, 0);
			server.setNodeUrl(targetUrl);
			List<ShakedownResult> resultList = testSuiteCallback.execute(server);
			printShakeDownResult(resultList);
		}
		
	}
	
	protected void printShakeDownResult(List<ShakedownResult> resultList) {
		for (ShakedownResult result : resultList) {
			if (result.isPass()) {
				System.out.println(result);
			}else {
				System.err.println(result);
			}
		}
	}
	
	protected void testEjb(String testName, String targetUrl, String[] targetClusters, final EjbShakedown ejbTestSuite) {
		testEjbTestCase(testName, targetUrl, targetClusters, new EjbTestSuiteCallback(ejbTestSuite) {
			
			@Override
			public List<ShakedownResult> testcase() {
				return ejbTestSuite.shakedown();
			}
		});
	}
	
	protected void testEjb(String componentName) {
		ComponentInfo componentInfo = ComponentManager.getComponentInfo(Constants.CATEGORY_EJB, componentName);
		if (componentInfo != null) {
			testEjb(componentName, ldapReader.getStringValue(componentInfo.getUrlPath()), 
					getClusters(componentInfo.getGroup(), componentInfo.getClusters()), (EjbShakedown)componentInfo.getTestSuite());
		}
	}
	
	protected void testVersion(String componentName) {
		ComponentInfo componentInfo = ComponentManager.getComponentInfo(Constants.CATEGORY_EJB, componentName);
		if (componentInfo != null) {
			testVersion(componentName, ldapReader.getStringValue(componentInfo.getUrlPath()), 
					getClusters(componentInfo.getGroup(), componentInfo.getClusters()), (EjbShakedown)componentInfo.getTestSuite());
		}
	}
	
	private void testVersion(String testName, String targetUrl, String[] targetClusters, final EjbShakedown ejbTestSuite) {
		testEjbTestCase(testName, targetUrl, targetClusters, new EjbTestSuiteCallback(ejbTestSuite) {
			
			@Override
			public List<ShakedownResult> testcase() {
				ejbTestSuite.testVersion();
				return ejbTestSuite.getResultList();
			}
		});
	}
	
	protected String[] getClusters(String group, String[] defaultClusters) {
		return defaultClusters;
	}
	
	private List<ServerInfo> retrieveServerInfo(String url, String targetCluster) {
		List<ServerInfo> serverList = new ArrayList<ServerInfo>();
		ClusterMBean cluster = null;
		try {
			cluster = ServerInfoUtilities.getClusterMBean(url, "devmonitor", "monitor1", targetCluster);
			MBeanHome mBeanHome  = ServerInfoUtilities.getAdminMBeanHome(url, "devmonitor", "monitor1");
			domainName = mBeanHome.getActiveDomain().getName();
		} catch (Exception e) {
			System.err.println("Unable to connect to admin server at " + url +": "+e.getLocalizedMessage());
			System.out.println("Trying again with t3s");
			url = url.replace("t3://", "t3s://");
			try {
				cluster = ServerInfoUtilities.getClusterMBean(url, "devmonitor", "monitor1", targetCluster);
				MBeanHome mBeanHome  = ServerInfoUtilities.getAdminMBeanHome(url, "devmonitor", "monitor1");
				domainName = mBeanHome.getActiveDomain().getName();
			}catch (Exception e2) {
				System.err.println("Unable to connect to admin server at " + url);;
				e2.printStackTrace();
			}
		}
		if (cluster != null) {
			ServerMBean[] servers = cluster.getServers();
			for (int i = 0; i < servers.length; i++) {
				ServerInfo serverInfo = new ServerInfo(targetCluster, servers[i].getName(), servers[i].getListenAddress(), servers[i].getListenPort());
				serverInfo.setDomainName(domainName);
				serverList.add(serverInfo);
				
			}
		} else {
			System.err.println("Using default URL "+url+" to connect instead of individual nodes.");
			ServerInfo serverInfo = new ServerInfo(targetCluster, "default", null, 0);
			serverInfo.setNodeUrl(url);
			serverInfo.setDomainName(domainName);
			serverList.add(serverInfo);
		}
		
		return serverList;
	}
		
}
