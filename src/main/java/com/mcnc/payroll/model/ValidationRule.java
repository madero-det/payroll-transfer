package com.mcnc.payroll.model;

public class ValidationRule {

	private String ruleType;
	private String ruleValue;
	private String errorMessage;

	public ValidationRule(String ruleType, String ruleValue, String errorMessage) {
		this.ruleType = ruleType;
		this.ruleValue = ruleValue;
		this.errorMessage = errorMessage;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
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
