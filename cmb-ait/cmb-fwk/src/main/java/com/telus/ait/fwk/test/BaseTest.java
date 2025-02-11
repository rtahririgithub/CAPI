package com.telus.ait.fwk.test;

import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.junit.spring.SpringIntegration;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.telus.ait.fwk.logging.AITLogger;
import com.telus.ait.fwk.util.Utils;
import com.telus.framework.config.ConfigContext;

@RunWith(SerenityRunner.class)
@ContextConfiguration({"classpath:fwk-context.xml"})
public abstract class BaseTest extends AbstractJUnit4SpringContextTests {
    protected static AITLogger log = new AITLogger(BaseTest.class);

    private String dataSet;

    @Rule
    public SpringIntegration springIntegration = new SpringIntegration();

    protected String environment() {
        return Utils.getEnvironment().toUpperCase();
    }
    
    protected void useDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    protected String property(String property) {
        return ConfigContext.getProperty(dataSet + property);
    }

    protected long propertyAsLong(String property) {
        return ConfigContext.getPropertyAsLong(dataSet + property);
    }

	protected Boolean getPropertyValue(String propertyName, boolean defaultValue) {
		String value = System.getProperty(propertyName);
		if (value != null) {
			return Boolean.valueOf(value);
		} else {
			return defaultValue;
		}
	}

	{
    	// global ssl config to avoid having to have ssl certificates 
        System.setProperty("UseSunHttpHandler", "true");
        Properties properties = ConfigContext.getProperties("logging");
        Enumeration<String> names = (Enumeration<String>) properties.propertyNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String property = properties.getProperty(name);
            while (property.contains("${")) {
                String var = property.substring(property.indexOf("${"), property.indexOf("}") + 1);
                String varName = var.substring(2, var.length() - 1);
                String value = ConfigContext.getProperty(varName);

                property = property.replace(var, value);
                properties.setProperty(name, property.replace(var, value));
            }
        }
        PropertyConfigurator.configure(properties);
        System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
        
        
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        try {
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            log.error("Unable to set ssl context", e);			
        }
    }    	
}
