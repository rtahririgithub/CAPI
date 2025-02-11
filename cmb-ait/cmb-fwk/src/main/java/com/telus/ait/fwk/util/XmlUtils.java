package com.telus.ait.fwk.util;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XmlUtils {
    public static String prettyFormat(String input, int indent) {
        try
        {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // This statement works with JDK 6
            transformerFactory.setAttribute("indent-number", indent);

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        }
        catch (Throwable e)
        {
            // You'll come here if you are using JDK 1.5
            // you are getting an the following exeption
            // java.lang.IllegalArgumentException: Not supported: indent-number
            // Use this code (Set the output property in transformer.
            try
            {
                Source xmlInput = new StreamSource(new StringReader(input));
                StringWriter stringWriter = new StringWriter();
                StreamResult xmlOutput = new StreamResult(stringWriter);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
                transformer.transform(xmlInput, xmlOutput);
                return xmlOutput.getWriter().toString();
            }
            catch(Throwable t)
            {
                return input;
            }
        }
    }

    public static String prettyFormat(String input) {
        return prettyFormat(input, 2);
    }

    public static void main(String[] args) {
        String xml = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header/><S:Body><ns8:getSubmittedOfferItemDetailResponse xmlns:ns8=\"http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WirelessSubscriberOfferInformationServiceRequestResponse_v1\" xmlns:ns10=\"http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v2\" xmlns:ns11=\"http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/AccountInformationTypes_v3\" xmlns:ns12=\"http://xmlschema.tmi.telus.com/xsd/Customer/Customer/CustomerManagementCommonTypes_v3\" xmlns:ns13=\"http://xmlschema.tmi.telus.com/xsd/common/exceptions/Exceptions_v1_0\" xmlns:ns14=\"http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/AccountInformationTypes_v2\" xmlns:ns15=\"http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/types/ping_v1\" xmlns:ns2=\"http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/SubscriberInformationTypes_v3\" xmlns:ns3=\"http://xmlschema.tmi.telus.com/xsd/Customer/Customer/CustomerManagementCommonTypes_v5\" xmlns:ns4=\"http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/customer_information_reference_types_1_0\" xmlns:ns5=\"http://xmlschema.tmi.telus.com/xsd/Resource/BaseTypes/resource_order_reference_types_1_0\" xmlns:ns6=\"http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v7\" xmlns:ns7=\"http://schemas.telus.com/eca/common_types_2_1\" xmlns:ns9=\"http://xmlschema.tmi.telus.com/xsd/Product/ProductOffering/WirelessSubscriberOfferInformationTypes_v1\"><ns8:submittedOfferItemDetail><ns9:customerOfferID>117197</ns9:customerOfferID><ns9:catalogueOfferItemSummary><ns9:offerID>30909</ns9:offerID><ns9:offerCode>DF-PCS-01</ns9:offerCode><ns9:offerDescriptionList><ns10:locale>EN_CA</ns10:locale><ns10:descriptionText>Direct Fulfillment PCS Offer 01</ns10:descriptionText></ns9:offerDescriptionList><ns9:offerDescriptionList><ns10:locale>FR_CA</ns10:locale><ns10:descriptionText>Direct Fulfillment PCS Offer 01 - FRENCH</ns10:descriptionText></ns9:offerDescriptionList><ns9:systemID>NCR</ns9:systemID><ns9:version>3</ns9:version><ns9:offerTypeCode>renewal</ns9:offerTypeCode><ns9:offerSubTypeCode>DEV-TEST</ns9:offerSubTypeCode><ns9:offerCategoryList><ns9:offerCategoryID>6</ns9:offerCategoryID><ns9:descriptionList><ns10:locale>EN_CA</ns10:locale><ns10:descriptionText>Mid-Contract Offer</ns10:descriptionText></ns9:descriptionList><ns9:descriptionList><ns10:locale>FR_CA</ns10:locale><ns10:descriptionText>Offre Ã  mi-entente</ns10:descriptionText></ns9:descriptionList></ns9:offerCategoryList><ns9:channelOrganizationTypeList>IOTA_AGENT</ns9:channelOrganizationTypeList><ns9:channelOrganizationTypeList>IOTA_CLIENT</ns9:channelOrganizationTypeList><ns9:channelOrganizationTypeList>KI</ns9:channelOrganizationTypeList><ns9:channelOrganizationTypeList>DL</ns9:channelOrganizationTypeList><ns9:channelOrganizationTypeList>RT</ns9:channelOrganizationTypeList><ns9:channelOrganizationTypeList>VAR</ns9:channelOrganizationTypeList><ns9:channelOrganizationTypeList>XDL</ns9:channelOrganizationTypeList><ns9:effectiveDate>2011-04-06T12:00:00.000-04:00</ns9:effectiveDate><ns9:offerItemID>30910</ns9:offerItemID><ns9:offerItemDescriptionList><ns10:locale>EN_CA</ns10:locale><ns10:descriptionText>P3MS $50 discount</ns10:descriptionText></ns9:offerItemDescriptionList><ns9:offerItemDescriptionList><ns10:locale>FR_CA</ns10:locale><ns10:descriptionText>P3MS $50 discount</ns10:descriptionText></ns9:offerItemDescriptionList><ns9:hardwareContextInd>true</ns9:hardwareContextInd><ns9:acquisitionEquipmentDiscountInd>false</ns9:acquisitionEquipmentDiscountInd><ns9:contractTerm>12</ns9:contractTerm><ns9:hardwarePromotionVersion>2</ns9:hardwarePromotionVersion><ns9:hardwarePromotionCode>OOMDD01</ns9:hardwarePromotionCode><ns9:clientOwnHardwareInd>false</ns9:clientOwnHardwareInd></ns9:catalogueOfferItemSummary><ns9:commitmentStartDate>2014-04-17T05:08:17.000-04:00</ns9:commitmentStartDate><ns9:commitmentEndDate>2015-04-17T05:08:17.000-04:00</ns9:commitmentEndDate><ns9:commitmentMonthCount>12</ns9:commitmentMonthCount><ns9:redeemDate>2014-04-17T05:08:17.000-04:00</ns9:redeemDate><ns9:hardwareDiscountAmt>0.0</ns9:hardwareDiscountAmt><ns9:requestorRole>csr</ns9:requestorRole><ns9:requestorID>25B3</ns9:requestorID><ns9:statusCode>OPEN</ns9:statusCode><ns9:assignedDate>2014-04-17T05:07:53.000-04:00</ns9:assignedDate><ns9:assignedBy>911520</ns9:assignedBy><ns9:redemptionMethodID>11</ns9:redemptionMethodID><ns9:migrationReasonCodeID>11</ns9:migrationReasonCodeID><ns9:rewardBalance><ns9:rewardBalanceAmt>0.0</ns9:rewardBalanceAmt><ns9:optOutInd>false</ns9:optOutInd></ns9:rewardBalance></ns8:submittedOfferItemDetail></ns8:getSubmittedOfferItemDetailResponse></S:Body></S:Envelope>";
        String prettyXml = prettyFormat(xml);
        System.out.println(prettyXml);
    }
}