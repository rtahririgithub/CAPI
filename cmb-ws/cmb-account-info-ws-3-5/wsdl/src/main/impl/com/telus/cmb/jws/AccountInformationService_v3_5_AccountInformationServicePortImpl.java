
package com.telus.cmb.jws;

import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.Holder;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.Account;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.AccountMemo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.BillingPropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.ContactPropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.InternationalServiceEligibilityCheckResult;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.InvoicePropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BusinessCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Memo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.ResponseMessage;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6b21 
 * Generated source version: 2.1
 * 
 */
@WebService(portName = "AccountInformationServicePort", serviceName = "AccountInformationService_v3_5", targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/AccountInformationService_3", wsdlLocation = "classpath:wsdls/AccountInformationService_v3_5.wsdl", endpointInterface = "com.telus.cmb.jws.AccountInformationServicePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class AccountInformationService_v3_5_AccountInformationServicePortImpl
    implements AccountInformationServicePort
{


    public AccountInformationService_v3_5_AccountInformationServicePortImpl() {
    }

    /**
     * 
     * @param billingAccountNumber
     * @return
     *     returns com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.Account
     * @throws ServiceException
     * @throws PolicyException
     */
    public Account getAccountByAccountNumber(String billingAccountNumber)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param phoneNumber
     * @param phoneNumberSearchOption
     * @return
     *     returns com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.Account
     * @throws ServiceException
     * @throws PolicyException
     */
    public Account getAccountByPhoneNumber(String phoneNumber, PhoneNumberSearchOption phoneNumberSearchOption)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param billingAccountNumber
     * @return
     *     returns java.util.List<com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.Account>
     * @throws ServiceException
     * @throws PolicyException
     */
    public List<Account> getAccountListByAccountNumbers(List<String> billingAccountNumber)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param imsi
     * @return
     *     returns java.util.List<com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.Account>
     * @throws ServiceException
     * @throws PolicyException
     */
    public List<Account> getAccountListByIMSI(String imsi)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param auditInfo
     * @param emailAddress
     * @param billingAccountNumber
     * @param notificationSuppressionInd
     * @throws ServiceException
     * @throws PolicyException
     */
    public void updateEmailAddress(String billingAccountNumber, String emailAddress, Boolean notificationSuppressionInd, AuditInfo auditInfo)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return;
    }

    /**
     * 
     * @param billingAccountNumber
     * @return
     *     returns java.lang.String
     * @throws ServiceException
     * @throws PolicyException
     */
    public String getEmailAddress(String billingAccountNumber)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param auditInfo
     * @param invoicePropertyList
     * @param billingAccountNumber
     * @param notificationSuppressionInd
     * @throws ServiceException
     * @throws PolicyException
     */
    public void updateInvoicePropertyList(String billingAccountNumber, InvoicePropertyListType invoicePropertyList, Boolean notificationSuppressionInd, AuditInfo auditInfo)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return;
    }

    /**
     * 
     * @param billingAccountNumber
     * @return
     *     returns com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.InvoicePropertyListType
     * @throws ServiceException
     * @throws PolicyException
     */
    public InvoicePropertyListType getInvoicePropertyList(String billingAccountNumber)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param billingAccountNumber
     * @return
     *     returns com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.BillingPropertyListType
     * @throws ServiceException
     * @throws PolicyException
     */
    public BillingPropertyListType getBillingInformation(String billingAccountNumber)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param auditInfo
     * @param contactPropertyList
     * @param billingAccountNumber
     * @param notificationSuppressionInd
     * @throws ServiceException
     * @throws PolicyException
     */
    public void updateContactInformation(String billingAccountNumber, ContactPropertyListType contactPropertyList, Boolean notificationSuppressionInd, AuditInfo auditInfo)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return;
    }

    /**
     * 
     * @param billingAccountNumber
     * @return
     *     returns com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.ContactPropertyListType
     * @throws ServiceException
     * @throws PolicyException
     */
    public ContactPropertyListType getContactInformation(String billingAccountNumber)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param billingAccountNumber
     * @throws ServiceException
     * @throws PolicyException
     */
    public void getBillCyclePropertyList(Holder<String> billingAccountNumber)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return;
    }

    /**
     * 
     * @param billingAccountNumber
     * @param memoType
     * @return
     *     returns com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Memo
     * @throws ServiceException
     * @throws PolicyException
     */
    public Memo getLastMemo(String billingAccountNumber, String memoType)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param billingAccountNumber
     * @param memo
     * @throws ServiceException
     * @throws PolicyException
     */
    public void createMemo(String billingAccountNumber, AccountMemo memo)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return;
    }

    /**
     * 
     * @param billingAccountNumber
     * @return
     *     returns com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.InternationalServiceEligibilityCheckResult
     * @throws ServiceException
     * @throws PolicyException
     */
    public InternationalServiceEligibilityCheckResult checkInternationalServiceEligibility(String billingAccountNumber)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param billingAccountNumber
     * @return
     *     returns com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation
     * @throws ServiceException
     * @throws PolicyException
     */
    public PersonalCreditInformation getPersonalCreditInformation(String billingAccountNumber)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param billingAccountNumber
     * @return
     *     returns com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BusinessCreditInformation
     * @throws ServiceException
     * @throws PolicyException
     */
    public BusinessCreditInformation getBusinessCreditInformation(String billingAccountNumber)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param parameters
     * @param updatePaymentMethodInSoapHdr
     * @return
     *     returns com.telus.cmb.jws.UpdatePaymentMethodResponse
     * @throws ServiceException
     * @throws PolicyException
     */
    public UpdatePaymentMethodResponse updatePaymentMethod(UpdatePaymentMethod parameters, OriginatingUserType updatePaymentMethodInSoapHdr)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param parameters
     * @param registerTopUpCreditCardInSoapHdr
     * @return
     *     returns com.telus.cmb.jws.RegisterTopUpCreditCardResponse
     * @throws ServiceException
     * @throws PolicyException
     */
    public RegisterTopUpCreditCardResponse registerTopUpCreditCard(RegisterTopUpCreditCard parameters, OriginatingUserType registerTopUpCreditCardInSoapHdr)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param billingAccountNumber
     * @throws ServiceException
     * @throws PolicyException
     */
    public void removeTopUpCreditCard(String billingAccountNumber)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return;
    }

    /**
     * 
     * @param followUp
     * @param asyncInd
     * @param billingAccountNumber
     * @throws ServiceException
     * @throws PolicyException
     */
    public void createFollowUp(String billingAccountNumber, FollowUpRequest followUp, boolean asyncInd)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return;
    }

    /**
     * 
     * @param totalDepositHeld
     * @param memoTypeList
     * @param billingAccountNumber
     * @param dateTo
     * @param totalPaymentAmount
     * @param dateFrom
     * @param memoList
     * @throws ServiceException
     * @throws PolicyException
     */
    public void getSubscriberEligibilitySupportingInfo(String billingAccountNumber, List<String> memoTypeList, Date dateFrom, Date dateTo, Holder<List<Memo>> memoList, Holder<String> totalPaymentAmount, Holder<String> totalDepositHeld)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return;
    }

    /**
     * 
     * @param billingAccountNumber
     * @return
     *     returns java.util.List<com.telus.cmb.jws.CustomerNotificationPreferenceType>
     * @throws ServiceException
     * @throws PolicyException
     */
    public List<CustomerNotificationPreferenceType> getCustomerNotificationPreferenceList(String billingAccountNumber)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param billingAccountNumber
     * @param notificationPreferenceList
     * @return
     *     returns com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.ResponseMessage
     * @throws ServiceException
     * @throws PolicyException
     */
    public ResponseMessage updateCustomerNotificationPreferenceList(String billingAccountNumber, List<CustomerNotificationPreferenceUpdateType> notificationPreferenceList)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @return
     *     returns java.lang.String
     * @throws ServiceException
     * @throws PolicyException
     */
    public String getNextSeatGroupId()
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param billingAccountNumber
     * @param subscriptionId
     * @return
     *     returns java.util.List<com.telus.cmb.jws.MaxVoipLineType>
     * @throws ServiceException
     * @throws PolicyException
     */
    public List<MaxVoipLineType> getMaxVoipLineList(String billingAccountNumber, Long subscriptionId)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

    /**
     * 
     * @param maxVoipLine
     * @throws ServiceException
     * @throws PolicyException
     */
    public void createMaxVoipLine(MaxVoipLineBaseType maxVoipLine)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return;
    }

    /**
     * 
     * @param maxVoipLineList
     * @throws ServiceException
     * @throws PolicyException
     */
    public void updateMaxVoipLineList(List<MaxVoipLineBaseType> maxVoipLineList)
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return;
    }

    /**
     * 
     * @return
     *     returns java.lang.String
     * @throws ServiceException
     * @throws PolicyException
     */
    public String ping()
        throws PolicyException, ServiceException
    {
        //replace with your impl here
        return null;
    }

}
