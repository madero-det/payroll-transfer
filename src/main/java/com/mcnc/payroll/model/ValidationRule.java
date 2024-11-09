package com.mcnc.payroll.model;

public class ValidationRule {
    private String ruleName;
    private String ruleValue;
    private String errorMessage;
    
    public ValidationRule(String ruleName, String ruleValue, String errorMessage) {
        this.ruleName = ruleName;
        this.ruleValue = ruleValue;
        this.errorMessage = errorMessage;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
