"""
To use this this script:  ( change the userName/password/url/targetServer below  )
1) open command line window
2) %WL_DOMAIN_HOME%\bin\setDomainEnv.cmd
3) java weblogic.WLST configJmsResource.py
"""

print "Starting the script "
"""
sys.argv used below represent the values passed in from the ant script as the wlst tag attribute.
"""
userName = sys.argv[1]
password = sys.argv[2]
url = sys.argv[3]
targetServer = sys.argv[4]
connect( userName,password, url)
edit()
startEdit()

cd('/')
cmo.createFileStore('FileStoreAdminServer')

cd('/FileStores/FileStoreAdminServer')
#set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServer,Type=Server')], ObjectName))
set('Targets',jarray.array([ObjectName('com.bea:Name='+targetServer+',Type=Server')], ObjectName))

print "FileStore created"

cd('/')
cmo.createJMSServer('AdminServerJMSServer')

cd('/Deployments/AdminServerJMSServer')
cmo.setPersistentStore(getMBean('/FileStores/FileStoreAdminServer'))
#set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServer,Type=Server')], ObjectName))
set('Targets',jarray.array([ObjectName('com.bea:Name='+targetServer+',Type=Server')], ObjectName))


print "JMSServer created"


cd('/')
cmo.createJMSSystemResource('cmbJMSModule')

cd('/SystemResources/cmbJMSModule')
#set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServer,Type=Server')], ObjectName))
set('Targets',jarray.array([ObjectName('com.bea:Name='+targetServer+',Type=Server')], ObjectName))

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule')
cmo.createConnectionFactory('cmbJmsConnectionFactory')

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule/ConnectionFactories/cmbJmsConnectionFactory')
cmo.setJNDIName('com.jms.wl.connection.PERSISTENT_CONN_FACTORY')

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule/ConnectionFactories/cmbJmsConnectionFactory/SecurityParams/cmbJmsConnectionFactory')
cmo.setAttachJMSXUserId(false)

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule/ConnectionFactories/cmbJmsConnectionFactory')
cmo.setDefaultTargetingEnabled(true)

cd('/SystemResources/cmbJMSModule')
cmo.createSubDeployment('cmbSubDeployment')

cd('/SystemResources/cmbJMSModule/SubDeployments/cmbSubDeployment')
set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServerJMSServer,Type=JMSServer')], ObjectName))

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule')
cmo.createQueue('CMB_MANAGEMENT_QUEUE_FAILURE')

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule/Queues/CMB_MANAGEMENT_QUEUE_FAILURE')
cmo.setJNDIName('com.telus.cmb.queue.CMB_MANAGEMENT_QUEUE_FAILURE')
cmo.setSubDeploymentName('cmbSubDeployment')

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule')
cmo.createQueue('CMB_MANAGEMENT_QUEUE_RETRY')

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule/Queues/CMB_MANAGEMENT_QUEUE_RETRY')
cmo.setJNDIName('com.telus.cmb.queue.CMB_MANAGEMENT_QUEUE_RETRY')
cmo.setSubDeploymentName('cmbSubDeployment')

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule')
cmo.createQueue('CMB_MANAGEMENT_QUEUE')

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule/Queues/CMB_MANAGEMENT_QUEUE')
cmo.setJNDIName('com.telus.cmb.queue.CMB_MANAGEMENT_QUEUE')
cmo.setSubDeploymentName('cmbSubDeployment')

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule/Queues/CMB_MANAGEMENT_QUEUE_RETRY/DeliveryParamsOverrides/CMB_MANAGEMENT_QUEUE_RETRY')
cmo.setRedeliveryDelay(60)

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule/Queues/CMB_MANAGEMENT_QUEUE_RETRY/DeliveryFailureParams/CMB_MANAGEMENT_QUEUE_RETRY')
cmo.setRedeliveryLimit(3)
cmo.setExpirationPolicy('Redirect')
cmo.setErrorDestination(getMBean('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule/Queues/CMB_MANAGEMENT_QUEUE_FAILURE'))

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule/Queues/CMB_MANAGEMENT_QUEUE/DeliveryParamsOverrides/CMB_MANAGEMENT_QUEUE')
cmo.setRedeliveryDelay(10)

cd('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule/Queues/CMB_MANAGEMENT_QUEUE/DeliveryFailureParams/CMB_MANAGEMENT_QUEUE')
cmo.setRedeliveryLimit(3)
cmo.setExpirationPolicy('Redirect')
cmo.setErrorDestination(getMBean('/JMSSystemResources/cmbJMSModule/JMSResource/cmbJMSModule/Queues/CMB_MANAGEMENT_QUEUE_RETRY'))



try:
    save()
    activate(block="true")
    print "script returns SUCCESS" 
    #stopEdit('y')
    disconnect()	
except:
    print "Error while trying to save and/or activate!!!"
    dumpStack()
    disconnect()	
