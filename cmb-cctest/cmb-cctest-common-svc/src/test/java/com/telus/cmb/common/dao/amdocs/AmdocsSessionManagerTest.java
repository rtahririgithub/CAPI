package com.telus.cmb.common.dao.amdocs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;

import junit.framework.TestCase;
import mockit.Deencapsulation;
import mockit.MockClass;
import mockit.Mocked;
import mockit.Mockit;
import mockit.NonStrictExpectations;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import amdocs.APILink.accesscontrol.APIAccessInfo;
import amdocs.APILink.accesscontrol.APIConnection;

import com.telus.api.ApplicationException;
import com.telus.cmb.framework.config.ConfigurationManagerImpl;
import com.telus.cmb.common.identity.ClientIdentity;

public class AmdocsSessionManagerTest extends TestCase {
	
	static Logger logger = Logger.getLogger(AmdocsSessionManagerTest.class); 
	
	//need to use JMock as all method we use are static method
	@Mocked APIConnection apiConnectionMock;
	@Mocked Context namingContext;
	
	static long IDLE_TIME = 6 * 1000;
	
	
    @MockClass(realClass = ConfigurationManagerImpl.class, stubs = {"()", "(String[])", "getStringValue(String[])"})
    public final static class MockConfigurationLdapReader {
    	
    	@mockit.Mock
    	public String getStringValue(String [] path) {
    		if ( path[ path.length-1 ].equals("session-idle") ) {
    			return String.valueOf( IDLE_TIME );
    		}
    		else {
    			return String.valueOf( IDLE_TIME /4  );
    		}
    	}
    }
    
  public final static class MockConfigurationLdapReaderWithNullReturn {
    	
    	@mockit.Mock
    	public String getStringValue(String [] path) {
    		return null;
    	}
    }

	@Before
	public void setUp() throws Exception {
		
				
		final APIAccessInfo accessInfo = new APIAccessInfo();
		accessInfo.setErrorType(0);
		accessInfo.setTicket("uams-ticket-abc");
		accessInfo.setContext(namingContext);

		new NonStrictExpectations() {
			{
				APIConnection.connect( withAny(""), withAny(""), withAny(""), withAny("") );
				returns( accessInfo );
			}
		};
	}	
	
	public void testAmdocsSessionCreation() throws ApplicationException {
		
		String sessionId = "124";
		AmdocsSession session  = new AmdocsSession ( sessionId, new ClientIdentity("kbUser1", "kbPass1", "anyAppId1"), "fakeURL" );
		System.out.println ( session );
	}

	public void testAmdocsManagerDefaultSetting() {
		
		//this line substitute the ConfigurationLdapReader with MockConfigurationLdapReader
		Mockit.setUpMock(ConfigurationManagerImpl.class, new MockConfigurationLdapReaderWithNullReturn() );

		new AmdocsSessionManager();
	}
	
	public void testAmdocsManagerSettingWithSystemProperty() {
		
		System.setProperty("cmb.services.amdocs.session.idle", "7000" );
		System.setProperty("cmb.services.amdocs.session.evictionRate", "200" );
		new AmdocsSessionManager();
	}
	
	public void testAmdocsManagerSettingWithLDAP() {
		
		//this line substitute the ConfigurationLdapReader with MockConfigurationLdapReader
		Mockit.setUpMock( ConfigurationManagerImpl.class, new MockConfigurationLdapReader() );
		
		new AmdocsSessionManager();
	}

	
	@Test
	public void testSessionExpiration() throws InterruptedException, ApplicationException {

		//this line substitute the ConfigurationLdapReader with MockConfigurationLdapReader
		Mockit.setUpMock(ConfigurationManagerImpl.class, new MockConfigurationLdapReader() );
		
		
		final AmdocsSessionManager sessionManager = new AmdocsSessionManager();
//		sessionManager.setUamsUrl("uamsUrl");
		
		
		//start create new sessions
		HashMap<String, ClientIdentity> clientIdentities = new LinkedHashMap<String, ClientIdentity>();
		for( int i=0; i< 4; i++ ) {
			ClientIdentity clientId = new ClientIdentity("kbUser"+i, "kbPass"+i, "anyAppId"+i);
			String sid = sessionManager.openSession( clientId ) ;;
			clientIdentities.put(sid, clientId );
		}
		
		List<String > sessionIds = new ArrayList<String> (clientIdentities.keySet());
		
		verifySessions(sessionManager, sessionIds);
		

		Thread clientThread = startSessionAccessThread( sessionManager, sessionIds, IDLE_TIME ) ;
		
		clientThread.join();
		
		//all sessions shall still alive
		verifySessions( sessionManager, sessionIds );
		
		Thread.sleep( IDLE_TIME * 2   );
		
		//all sessions shall expired 
		verifySessions( sessionManager, new ArrayList<String>() );
		
	}

	private Thread startSessionAccessThread( final AmdocsSessionManager sessionManager,
			final List<String> sessionIds, final long idleTime ) {
		
		Thread t = new Thread ( "access session" ) {
			public void run() {
				long startTime = System.currentTimeMillis() + (long) (idleTime * 2 );
				long period = idleTime / 5;

				while ( true ) {
					
					if ( System.currentTimeMillis() > startTime ) break;
					
					for ( String sessionId: sessionIds ) {
						try {
							sessionManager.getSession(sessionId);
						} catch (ApplicationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep( period );
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		t.start();
		return t;
		
	}

	private void verifySessions( AmdocsSessionManager sessionManager,	List<String> sessionIds) throws ApplicationException {
		
		logger.debug( "Verfiy session size:" + sessionIds.size() );
		
		assertEquals ( "session cache size" , sessionIds.size(),((Map) Deencapsulation.getField(sessionManager, "sessions")).size() );
		
		for ( String sessionId : sessionIds ) {
			AmdocsSession amdocsSession = sessionManager.getSession( sessionId  );
			assertNotNull(amdocsSession);
		}
	}
}
