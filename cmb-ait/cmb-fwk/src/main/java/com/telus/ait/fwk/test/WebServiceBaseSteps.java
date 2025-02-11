package com.telus.ait.fwk.test;

import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.StepGroup;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static net.serenitybdd.core.Serenity.sessionVariableCalled;

public abstract class WebServiceBaseSteps {
    @StepGroup
    /** Do not remove 'environment' unused parameter. It's here so that will be shown on Serenity BDD reports */
    public void setUpTestForEnvironment(String environment) throws Exception {
        defineStaticData();
        loadDynamicData();
        resetData();
        collectDataForRequestAttributes();
    }

    /* All elements of given data set are displayed automatically on Thucydides reports.
	 * No need for test developer to write any custom code regarding this aspect.
	  */
    private void defineStaticData() {
        describeTest("", Serenity.getCurrentSession().entrySet());
    }

    private void describeTest(String header, Set<Map.Entry<Object, Object>> properties) {
        String descriptionHeader = "<h2 style=\"font-style:italic;color:darkblue\">" + header + "</h2>";

        String requestDataHeader = "<div><p>Request Data:</p>" +
                "<ul style=\"margin-left:5%; font-weight:200; color:darkblue; font-size:12px;\">";

        String htmlDescription = descriptionHeader + requestDataHeader;

		/*
		 * Request data formatting
		 */
        String requestData = "";
        for (Map.Entry<Object, Object> property : properties) {
            if (((String)property.getKey()).toLowerCase().startsWith("expected")) {
                continue;
            }
            requestData += "<li>" + property.getKey() + ": " + property.getValue() + "</li>";
        }

        if (StringUtils.isEmpty(requestData)) {
            requestData = "No request data";
        }

        htmlDescription += requestData + "</ul></div>";

        String validationDataHeader = "<div><p>Validation Data:</p>" +
                "<ul style=\"margin-left:5%; font-weight:200; color:darkblue; font-size:12px;\">";

        htmlDescription += validationDataHeader;

		/*
		 * Validation data formatting
		 */
        String validationData = "";
        for (Map.Entry<Object, Object> property : properties) {
            if (((String)property.getKey()).toLowerCase().startsWith("expected")) {
                validationData += "<li>" + property.getKey() + ": " + property.getValue() + "</li>";
            }
        }

        if (StringUtils.isEmpty(validationData)) {
            validationData = "No validation data";
        }

        htmlDescription += validationData + "</ul></div>";

        defineStaticData(htmlDescription);
    }

    @Step
    /* Step used to display static data properties on Thucydides reports */
    public void defineStaticData(String htmlDescription) {
    }

    /** START: Test setup hooks to be implemented by stepGroup subclasses - if needed */

    @StepGroup
    public void loadDynamicData() throws Exception {
        noDynamicDataHasBeenDefined();
    }

    @Step
    protected void noDynamicDataHasBeenDefined() {
    }

    @StepGroup
    public void resetData() throws Exception {
        noDataToReset();
    }

    @Step
    protected void noDataToReset() {
    }

    @StepGroup
    public void collectDataForRequestAttributes() throws Exception {
        noCollectionRequired();
    }

    @Step
    protected void noCollectionRequired() {
    }

    /** END: Test setup hooks to be implemented by stepGroup subclasses - if needed */

    @StepGroup
    public void executeOperation() throws Exception {
    }

    @StepGroup
    public void validateResponse() throws Exception {
    }

    /***
     * Must be called *last* in the executeOperation overridden method
     ***/
    protected void displayRequest() {
        request(asHtml((String) sessionVariableCalled("messageRequest")));
    }

    /***
     * Must be called *first* in the validateResponse overridden method
     ***/
    protected void displayResponse() {
        response(asHtml((String) sessionVariableCalled("messageResponse")));
    }

    @Step
    protected void request(String request) {
    }

    @Step
    protected void response(String response) {
    }

    private String asHtml(String xmlString) {
        xmlString = xmlString.trim().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "_");

        String script = "" +
                "<script type=\"text/javascript\">" +
                "	function toggleExpand_" + uuid + "(elementId) {" +
                "		var ecLinkElem = document.getElementById(\"ec-\" + elementId + \"-link\");" +
                "		var ecContentElem = document.getElementById(\"ec-\" + elementId + \"-content\");" +
                "		if(ecContentElem.style.display == \"none\") {" +
                "			ecLinkElem.innerHTML  = \"- <u>Collapse</u>\";" +
                "			ecContentElem.style.display = \"\";" +
                "		} else {" +
                "			ecLinkElem.innerHTML  = \"+ <u>Expand</u>\";" +
                "			ecContentElem.style.display = \"none\";" +
                "		}" +
                "	};" +
                "</script>";

        //href=""
        String html = "" +
                "<div>" +
                "	<a id=\"ec-" + uuid + "-link\" style=\"cursor: pointer;margin-left:5%; font-weight:200; color:darkblue; font-size:12px;\" onclick=\"toggleExpand_" + uuid + "('" + uuid + "');\">+ <u>Expand</u></a>" +
                "	<pre id=\"ec-" + uuid + "-content\" style=\"display: none;\">" + xmlString + "</pre>" +
                "</div>";
        return script + html;
    }

}
