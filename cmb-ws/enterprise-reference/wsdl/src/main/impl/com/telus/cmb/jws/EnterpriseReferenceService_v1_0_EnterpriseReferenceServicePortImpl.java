
package com.telus.cmb.jws;

import java.util.List;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.Country;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.Language;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.Province;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.State;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.UnitType;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6b21 
 * Generated source version: 2.1
 * 
 */
@WebService(portName = "EnterpriseReferenceServicePort", serviceName = "EnterpriseReferenceService_v1_0", targetNamespace = "http://telus.com/wsdl/EO/KnowledgeMgmt/EnterpriseReferenceService_1", wsdlLocation = "/wsdls/EnterpriseReferenceService_v1_0.wsdl", endpointInterface = "com.telus.cmb.jws.EnterpriseReferenceServicePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class EnterpriseReferenceService_v1_0_EnterpriseReferenceServicePortImpl
    implements EnterpriseReferenceServicePort
{


    public EnterpriseReferenceService_v1_0_EnterpriseReferenceServicePortImpl() {
    }

    /**
     * 
     * @return
     *     returns java.util.List<com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.UnitType>
     * @throws PolicyException
     * @throws ServiceException
     */
    public List<UnitType> getUnitTypes()
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @return
     *     returns java.util.List<com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.Province>
     * @throws PolicyException
     * @throws ServiceException
     */
    public List<Province> getProvinces()
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @return
     *     returns java.util.List<com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.State>
     * @throws PolicyException
     * @throws ServiceException
     */
    public List<State> getStates()
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @return
     *     returns java.util.List<com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.Country>
     * @throws PolicyException
     * @throws ServiceException
     */
    public List<Country> getCountries()
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @return
     *     returns java.util.List<com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.Language>
     * @throws PolicyException
     * @throws ServiceException
     */
    public List<Language> getLanguages()
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @return
     *     returns java.lang.String
     * @throws PolicyException
     * @throws ServiceException
     */
    public String ping()
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

}
