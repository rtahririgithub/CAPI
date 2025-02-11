package com.telus.ait.fwk.util;

import com.telus.ait.fwk.logging.AITLogger;

import javax.net.ssl.*;
import javax.xml.ws.BindingProvider;
import java.security.cert.X509Certificate;

public class SslUtil {
    private static AITLogger log = new AITLogger(SslUtil.class);

    // per port ssl config to avoid having to have ssl certificates
    public static void initSsl(Object[] ports) {
        try {          
            final SSLSocketFactory sslSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();

            for (Object port: ports) {
                try {
                    ((BindingProvider) port).getRequestContext()
                            .put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory",
                                    sslSocketFactory);
                } catch (ClassCastException e) {
                    HttpsURLConnection conn = (HttpsURLConnection) port;
                    conn.setSSLSocketFactory(sslSocketFactory);
                }
            }
        } catch (Exception e) {
            log.error("Unable to set ssl context for specific port", e);
        }
    }
}
