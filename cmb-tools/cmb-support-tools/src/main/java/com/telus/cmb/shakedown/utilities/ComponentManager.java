package com.telus.cmb.shakedown.utilities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.telus.cmb.account.app.AccountInformationHelperShakedown;
import com.telus.cmb.account.app.AccountLifecycleFacadeShakedown;
import com.telus.cmb.account.app.AccountLifecycleManagerShakedown;
import com.telus.cmb.productequipment.app.ProductEquipmentFacadeShakedown;
import com.telus.cmb.productequipment.app.ProductEquipmentHelperShakedown;
import com.telus.cmb.productequipment.app.ProductEquipmentManagerShakedown;
import com.telus.cmb.reference.app.ReferenceDataFacadeShakedown;
import com.telus.cmb.reference.app.ReferenceDataHelperShakedown;
import com.telus.cmb.shakedown.info.ComponentInfo;
import com.telus.cmb.subscriber.app.SubscriberFacadeShakedown;
import com.telus.cmb.subscriber.app.SubscriberHelperShakedown;
import com.telus.cmb.subscriber.app.SubscriberManagerShakedown;
import com.telus.cmb.utility.app.ConfigurationManagerShakedown;
import com.telus.cmb.utility.app.ContactEventManagerShakedown;
import com.telus.cmb.utility.app.DealerManagerShakedown;
import com.telus.cmb.utility.app.QueueEventManagerShakedown;

public class ComponentManager {
	
	private static Hashtable<String, List<ComponentInfo>> initialMap = null;
	
	public static List<String> getCategoryList() {
		return new ArrayList<String>(getInitialMap().keySet());
	}
	
	public static List<ComponentInfo> getComponentInfoList (String category) {
		return getInitialMap().get(category);
	}
	
	public static ComponentInfo getComponentInfo (String category, String componentName) {
		List<ComponentInfo> componentInfoList = getComponentInfoList(category);
		if (componentInfoList != null) {
			for (ComponentInfo componentInfo : componentInfoList) {
				if (componentInfo.getName().equalsIgnoreCase(componentName))
					return componentInfo;
			}
		}
		return null;
	}
	
	private static Hashtable<String, List<ComponentInfo>> getInitialMap() {
		if (initialMap == null) {
			int envType = getEnvironmentType();
			
			initialMap = new Hashtable<String, List<ComponentInfo>>();
			initialMap.put(Constants.CATEGORY_EJB, initialEjbComponents(envType));
			//initialMap.put(Constants.CATEGORY_WS, initialWSComponents(envType));
		}
		return initialMap;
	}
	
	private static int getEnvironmentType() {
		String ldapUrl = System.getProperty("com.telusmobility.config.java.naming.provider.url");
		if ("ldap://ldapread-p.tmi.telus.com:389/cn=prod_81,o=telusconfiguration".equals(ldapUrl)) {
			return Constants.ENV_PR;
		} else if ("ldap://ldapread-s.tmi.telus.com:1589/cn=s_81,o=telusconfiguration".equals(ldapUrl) ||
				"ldap://ldapread-st101a.tmi.telus.com:1589/cn=s_81,o=telusconfiguration_a".equals(ldapUrl)) {
			return Constants.ENV_ST;
		} else if ("ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration".equals(ldapUrl)) {
			return Constants.ENV_D3;
		} else {
			return Constants.ENV_OTHER;
		}
	}
	
	private static List<ComponentInfo> initialEjbComponents(int envType) {
		
		List<ComponentInfo> list = new ArrayList<ComponentInfo>();
		
		String[] clusterGroupCIM = getClusterGroup(envType, Constants.CLUSTER_GROUP_CIM);
		String[] clusterGroupCU = getClusterGroup(envType, Constants.CLUSTER_GROUP_GU);

		list.add(new ComponentInfo("AccountInformationHelper", Constants.CATEGORY_EJB, Constants.GROUP_ACCOUNT_EJB, clusterGroupCIM, new AccountInformationHelperShakedown()));
		list.add(new ComponentInfo("AccountLifecycleFacade", Constants.CATEGORY_EJB, Constants.GROUP_ACCOUNT_EJB, clusterGroupCIM, new AccountLifecycleFacadeShakedown()));
		list.add(new ComponentInfo("AccountLifecycleManager", Constants.CATEGORY_EJB, Constants.GROUP_ACCOUNT_EJB, clusterGroupCIM, new AccountLifecycleManagerShakedown()));

		list.add(new ComponentInfo("ProductEquipmentHelper", Constants.CATEGORY_EJB, Constants.GROUP_PRODUCT_EJB, clusterGroupCIM, new ProductEquipmentHelperShakedown()));
		list.add(new ComponentInfo("ProductEquipmentManager", Constants.CATEGORY_EJB, Constants.GROUP_PRODUCT_EJB, clusterGroupCIM, new ProductEquipmentManagerShakedown()));
		list.add(new ComponentInfo("ProductEquipmentLifecycleFacade", Constants.CATEGORY_EJB, Constants.GROUP_PRODUCT_EJB, clusterGroupCIM, new ProductEquipmentFacadeShakedown()));
		
		list.add(new ComponentInfo("ReferenceDataHelper", Constants.CATEGORY_EJB, Constants.GROUP_REFERENCE_EJB, clusterGroupCU, new ReferenceDataHelperShakedown()));
		list.add(new ComponentInfo("ReferenceDataFacade", Constants.CATEGORY_EJB, Constants.GROUP_REFERENCE_EJB, clusterGroupCU, new ReferenceDataFacadeShakedown()));

		list.add(new ComponentInfo("SubscriberLifecycleFacade", Constants.CATEGORY_EJB, Constants.GROUP_SUBSCRIBER_EJB, clusterGroupCIM, new SubscriberFacadeShakedown()));
		list.add(new ComponentInfo("SubscriberLifecycleHelper", Constants.CATEGORY_EJB, Constants.GROUP_SUBSCRIBER_EJB, clusterGroupCIM, new SubscriberHelperShakedown()));
		list.add(new ComponentInfo("SubscriberLifecycleManager", Constants.CATEGORY_EJB, Constants.GROUP_SUBSCRIBER_EJB, clusterGroupCIM, new SubscriberManagerShakedown()));

		list.add(new ComponentInfo("ConfigurationManager", Constants.CATEGORY_EJB, Constants.GROUP_UTILITY_EJB, clusterGroupCU, new ConfigurationManagerShakedown()));
		list.add(new ComponentInfo("ContactEventManager", Constants.CATEGORY_EJB, Constants.GROUP_UTILITY_EJB, clusterGroupCU, new ContactEventManagerShakedown()));
		list.add(new ComponentInfo("DealerManager", Constants.CATEGORY_EJB, Constants.GROUP_UTILITY_EJB, clusterGroupCU, new DealerManagerShakedown()));
		list.add(new ComponentInfo("QueueEventManager", Constants.CATEGORY_EJB, Constants.GROUP_UTILITY_EJB, clusterGroupCU, new QueueEventManagerShakedown()));

		return list;
	}
	
	private static List<ComponentInfo> initialWSComponents(int envType) {
		List<ComponentInfo> list = new ArrayList<ComponentInfo>();
		//TODO: implement when WS shakedown is ready
		return list;
	}
	
	private static String[] getClusterGroup(int envType, int groupType) {
		if (groupType == Constants.CLUSTER_GROUP_CIM) {
			switch (envType) {
				case Constants.ENV_PR: return new String[]{Constants.CLUSTER_CIM2, Constants.CLUSTER_CIM3, Constants.CLUSTER_CIMBATCH};
				case Constants.ENV_ST: return new String[]{Constants.CLUSTER_CIM2, Constants.CLUSTER_CIMBATCH};
				case Constants.ENV_D3: return new String[]{Constants.CLUSTER_CIM2};
				default: return new String[]{Constants.CLUSTER_CIM2};
			}
		} else if (groupType == Constants.CLUSTER_GROUP_GU) {
			switch (envType) {
				case Constants.ENV_PR: return new String[]{Constants.CLUSTER_GU, Constants.CLUSTER_GU2};
				default: return new String[]{Constants.CLUSTER_GU};
			}
		} else {
			return null;
		}
	}
	
}
