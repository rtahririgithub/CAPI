package com.telus.cmb.shakedown.utilities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.configuration.AdminMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;

import com.telus.cmb.shakedown.info.ServerInfo;

public class ServerInfoUtilities {
	private static final Logger logger = Logger.getLogger(ServerInfoUtilities.class);
	private static Hashtable<String, ArrayList<ServerInfo>> serverInfoCache = new Hashtable<String, ArrayList<ServerInfo>>(); 
	
	private static MBeanHome getMBeanHome(String url, String userName, String password, String mbeanName) throws NamingException {
		Environment env = new Environment();
//		env.setProviderUrl(url.replace("t3://", "t3s://"));
		env.setProviderURL(url);
		env.setSecurityPrincipal(userName);
		env.setSecurityCredentials(password);
		
		Context ctx = env.getInitialContext();

		// Get the Local MBeanHome
		Object obj = null;
		try {
			obj = ctx.lookup(mbeanName);
		} catch (NamingException ce) {
			ce.printStackTrace();
		}finally  {
			ctx.close();
		}
		return (MBeanHome) obj;
	}


	public static MBeanHome getAdminMBeanHome(String url, String userName, String password) throws NamingException {
		MBeanHome adminHome  = getMBeanHome(url, userName, password, MBeanHome.ADMIN_JNDI_NAME);
        logger.debug("Got the Admin MBeanHome: " + adminHome.getActiveDomain().getName() + " from the Admin server");
		
		/**
		ClusterMBean[] clusters = adminHome.getActiveDomain().getClusters();
		for (ClusterMBean cluster : clusters) {
			logger.debug(cluster.getName());
			ServerMBean[] servers = cluster.getServers();
	        for( int i=0; i<servers.length; i++) {
	        	logger.debug( servers[i].getName() + "[" + servers[i].getListenAddress()+":"+servers[i].getListenPort()+"]");
	        }			
		}
        */
		
		return adminHome;
	}

	public static MBeanHome getAdminMBeanHome(String url, String userName, String password, String cluster) throws NamingException {
		ClusterMBean adminHome  = (ClusterMBean) getMBeanHome(url, userName, password, "weblogic.management.configuration.ClusterMBean"); 
        System.out.println("Got the Admin MBeanHome: " + adminHome.getName() + " from the Admin server");
        ServerMBean[] servers = adminHome.getServers();
        for( int i=0; i<servers.length; i++) {
        	System.out.println( servers[i].getName() + "[" + servers[i].getListenAddress()+":"+servers[i].getListenPort()+"]");
        }
        System.out.println();
        
		return null;
	}
	
	public static ClusterMBean getClusterMBean(String url, String userName, String password, String clusterName) throws NamingException {
//		url = "t3s://ln99254.corp.ads:60150";
		MBeanHome adminHome  = getMBeanHome(url, userName, password, MBeanHome.ADMIN_JNDI_NAME);
//		Set mbeanSet = adminHome.getAllMBeans();
//		Iterator it = mbeanSet.iterator();
//		while (it.hasNext()) {
//			Object obj = it.next();
//			System.out.println(obj);
//		}

		ClusterMBean[] clusters = adminHome.getActiveDomain().getClusters();
		for (ClusterMBean cluster : clusters) {
			if (cluster.getName().equalsIgnoreCase(clusterName)) {
				return cluster;
			}
		}
        
		return null;
	}
	
	public static List<ServerInfo> retrieveServerInfoList(String urlKey, String[] clusters) {
		String url = AppConfiguration.getStringValue(urlKey);
		
		List<ServerInfo> serverList = new ArrayList<ServerInfo>();
		
		for (String targetCluster : clusters) {
			ArrayList<ServerInfo> cacheServerInfo = serverInfoCache.get(targetCluster);
			if (cacheServerInfo == null) {
				cacheServerInfo = new ArrayList<ServerInfo>();
				try {
					MBeanHome mBeanHome  = getAdminMBeanHome(url, "devmonitor", "monitor1");
					String domainName = mBeanHome.getActiveDomain().getName();
	
					ClusterMBean clusterMBean = getClusterMBean(url, "devmonitor", "monitor1", targetCluster);
					if (clusterMBean != null) {
						ServerMBean[] servers = clusterMBean.getServers();
						for (int i = 0; i < servers.length; i++) {
							ServerInfo serverInfo = new ServerInfo(targetCluster, servers[i].getName(), servers[i].getListenAddress(), servers[i].getListenPort());
							serverInfo.setDomainName(domainName);
							cacheServerInfo.add(serverInfo);
							
						}
					} else {
						logger.error("Using default URL "+url+" to connect instead of individual nodes.");
						ServerInfo serverInfo = new ServerInfo(targetCluster, "default", null, 0);
						serverInfo.setNodeUrl(url);
						serverInfo.setDomainName(domainName);
						cacheServerInfo.add(serverInfo);
					}
				} catch (Exception e) {
					logger.error("Unable to connect to admin server at " + url);
					//e.printStackTrace();
				}
				serverInfoCache.put(targetCluster, cacheServerInfo);
			}
			serverList.addAll(cacheServerInfo);
		}
		
		return serverList;
	}
	
}
