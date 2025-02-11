package com.telus.eas.framework.info;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 22-Oct-2007
 */
/** * @deprecated Interaction with HPA through Client API has been decommissioned as of July 2013 release.  Use services exposed by HPA team. * @author Naresh Annabathula * */
public class HpaEligibilityInfo extends Info {
  public static final String CREDIT_SCENARIO_A = "A";
  public static final String CREDIT_SCENARIO_B = "B";
  public static final String CREDIT_SCENARIO_C = "C";
  public static final String CREDIT_SCENARIO_D = "D";
  public static final String CREDIT_SCENARIO_E = "E";

  private int ban;
  private float hpaEligibilityAmount;
  private String creditScenario;
  private boolean isAccounOnCLM;
  private boolean isAccountSuspended;

  public int getBan() {
    return ban;
  }

  public void setBan(int ban) {
    this.ban = ban;
  }

  public float getHpaEligibilityAmount() {
    return hpaEligibilityAmount;
  }

  public void setHpaEligibilityAmount(float hpaEligibilityAmount) {
    this.hpaEligibilityAmount = hpaEligibilityAmount;
  }

  public String getCreditScenario() {
    return creditScenario;
  }

  public void setCreditScenario(String creditScenario) {
    this.creditScenario = creditScenario;
  }

  public boolean isAccounOnCLM() {
    return isAccounOnCLM;
  }

  public void setAccounOnCLM(boolean accounOnCLM) {
    isAccounOnCLM = accounOnCLM;
  }

  public boolean isAccountSuspended() {
    return isAccountSuspended;
  }

  public void setAccountSuspended(boolean accountSuspended) {
    isAccountSuspended = accountSuspended;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("HpaEligibilityInfo:{\n");
    s.append("    ban=[").append(ban).append("]\n");
    s.append("    hpaEligibilityAmount=[").append(hpaEligibilityAmount).append("]\n");
    s.append("    creditScenario=[").append(creditScenario).append("]\n");
    s.append("    isAccounOnCLM=[").append(isAccounOnCLM).append("]\n");
    s.append("    isAccountSuspended=[").append(isAccountSuspended).append("]\n");
    s.append("}");

    return s.toString();
  }
}
