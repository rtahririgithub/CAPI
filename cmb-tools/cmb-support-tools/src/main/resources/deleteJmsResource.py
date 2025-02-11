"""
To use this this script ( change the userName/password/url below )
1) open command line window
2) %WL_DOMAIN_HOME%\bin\setDomainEnv.cmd
3) java weblogic.WLST deleteJmsResource.py
"""

print 'starting the script of removing JMS System Resources ....'
userName = sys.argv[1]
password = sys.argv[2]
url = sys.argv[3]
connect( userName,password, url)

edit()
startEdit()

cmo.destroyJMSSystemResource(getMBean('/SystemResources/cmbJMSModule'))
cmo.destroyJMSServer(getMBean('/Deployments/AdminServerJMSServer'))
cmo.destroyFileStore(getMBean('/FileStores/FileStoreAdminServer'))

save()
activate(block="true")
print 'JMS System Resource removed '
#stopEdit('y')
disconnect()