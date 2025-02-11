package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;

public class NotificationMessageTemplateInfo extends Info implements com.telus.api.reference.Reference {


 static final long serialVersionUID = 1L;

  private String code;
  private String template;
  private String templateFrench;

  public NotificationMessageTemplateInfo() {
  }

  public String getCode(){
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return "English description N/A";
  }

  public String getDescriptionFrench() {
    return "French description N/A";
  }

  public String getMessageTemplate() {
    return template;
  }

  public String getMessageTemplateFrench() {
    return templateFrench;
  }

  public void setMessageTemplate(String template) {
    this.template = template;
  }

  public void setMessageTemplateFrench(String templateFrench) {
    this.templateFrench = templateFrench;
  }

}
