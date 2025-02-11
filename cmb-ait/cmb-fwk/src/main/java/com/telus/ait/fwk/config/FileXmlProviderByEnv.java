package com.telus.ait.fwk.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.telus.ait.fwk.util.Utils;
import com.telus.framework.config.BasicPropertyNodeImpl;
import com.telus.framework.config.ConfigProvider;
import com.telus.framework.config.PropertyNodeTypedXmlParser;
import com.telus.framework.exception.ConfigurationException;

public class FileXmlProviderByEnv extends ConfigProvider {

    //NOTE: hast to use System.out.println() as logging.xml infra is not available

    protected static final String CONFIG_ROOT_PATH = "configRootPath";
    protected static final String FILE_EXTENSION = ".xml";

    public void init(Properties envProps, Properties appProps) {
        assert ((envProps != null) && (appProps != null)) : "Properties object for ConfigProvider is null";

        setEnvProperties(envProps);
        setAppProperties(appProps);
/*
        this.m_rootPath = getEnvProperty("configRootPath");
        if (this.m_rootPath == null)
            throw new ConfigurationException(
                    "Environment property not defined: configRootPath");
*/
    }

    protected BasicPropertyNodeImpl loadConfigurationComponent(String appId,
                                                               String compName) {
        String realAppId = getAppIdFromComponent(compName);
        String realCompName = getComponentNameFromComponent(compName);

        if (realAppId == null) {
            realAppId = appId;
        }

        String environmentSuffix = Utils.getEnvironment();
        setSystemProperties(environmentSuffix);
        
        String fileName = /* this.m_rootPath + File.separator + */ realAppId
                + File.separator + realCompName + (realCompName.startsWith("logging") ? "" : ("-" +
                environmentSuffix.toString().toLowerCase())) +".xml";

        ConfigurationException newEx = null;

        try {
            System.out.println("loadConfigurationComponent, attempting reading config from absolute location: " + fileName);
            BasicPropertyNodeImpl cfg = PropertyNodeTypedXmlParser.parse(new FileInputStream(fileName));
            System.out.println("loadConfigurationComponent, configuration read successfully from absolute location: " + fileName);
            return cfg;
        } catch (IOException e) {
            newEx = new ConfigurationException("Error loading xml file: " + fileName, e);
//			newEx.printStackTrace();
            System.out.println("loadConfigurationComponent, error reading config from absolute location: " + fileName);

        } catch (ConfigurationException e) {
            newEx = new ConfigurationException("Error loading xml file: " + fileName, e);
//			newEx.printStackTrace();
            System.out.println("loadConfigurationComponent, error reading config from absolute location: " + fileName);
        }

        try {
            fileName = fileName.replace(File.separator, "/");
            System.out.println("loadConfigurationComponent, attempting reading config from classpath location: " + fileName);
            InputStream in = ClassLoader.getSystemClassLoader().getResource(fileName).openStream();
            BasicPropertyNodeImpl cfg = PropertyNodeTypedXmlParser.parse(in);
            System.out.println("loadConfigurationComponent, configuration read successfully from classpath location: " + fileName);
            return cfg;
        } catch (ConfigurationException e) {
            newEx = new ConfigurationException("Error loading xml file: " + fileName, e);
//			newEx.printStackTrace();
            System.out.println("loadConfigurationComponent, error reading config from classpath location: " + fileName);
        } catch (IOException e) {
            newEx = new ConfigurationException("Error loading xml file: " + fileName, e);
//			newEx.printStackTrace();
            System.out.println("loadConfigurationComponent, error reading config from classpath location: " + fileName);
        }

        throw newEx;
    }    
    
    private void setSystemProperties(String environmentSuffix) {
        if ("pt148".equalsIgnoreCase(environmentSuffix)) {
            System.setProperty("com.telusmobility.config.java.naming.provider.url",
                    "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
            System.setProperty("com.telus.provider.providerURL", "wlqaeaseca:8682");
        } else if ("pt168".equalsIgnoreCase(environmentSuffix)) {
            System.setProperty("com.telusmobility.config.java.naming.provider.url",
                    "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");
            System.setProperty("com.telus.provider.providerURL",
                    "t3://wlpt168easeca:8682");
        } else if ("ps101".equalsIgnoreCase(environmentSuffix)) {
            System.setProperty("com.telusmobility.config.java.naming.provider.url",
                    "ldap://ldapread-qa2.tmi.telus.com:1489/cn=qa2_81,o=telusconfiguration");
            System.setProperty("com.telus.provider.providerURL",
                    "t3://wlqa2easeca:8682");
        } else if ("pt140".equalsIgnoreCase(environmentSuffix)) {
            System.setProperty("com.telusmobility.config.java.naming.provider.url",
                    "ldap://ldapread-qa2.tmi.telus.com:1489/cn=qa2_81,o=telusconfiguration");
            System.setProperty("com.telus.provider.providerURL",
                    "t3://wlqa2easeca:8682");
        }
        System.setProperty("environment", environmentSuffix);
    }

}

