package com.mcnc.payroll.model;

import java.util.List;

public class Property {

	String apiName;
	String fieldName;
	String parentFieldName;
	String dataType;
	String fieldLocation;
	Boolean isRequired;
	List<ValidationRule> validationRules;
	List<Property> childProperties;

	public Property(String apiName, String fieldName, String parentFieldName, String dataType, String fieldLocation, Boolean isRequired, List<ValidationRule> validationRules, List<Property> childProperties) {
		this.apiName = apiName;
		this.fieldName = fieldName;
		this.parentFieldName = parentFieldName;
		this.dataType = dataType;
		this.fieldLocation = fieldLocation;
		this.isRequired = isRequired;
		this.validationRules = validationRules;
		this.childProperties = childProperties;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getParentFieldName() {
		return parentFieldName;
	}

	public void setParentFieldName(String parentFieldName) {
		this.parentFieldName = parentFieldName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getFieldLocation() {
		return fieldLocation;
	}

	public void setFieldLocation(String fieldLocation) {
		this.fieldLocation = fieldLocation;
	}

	public Boolean isRequired() {
		return isRequired;
	}

	public void setRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	public List<ValidationRule> getValidationRules() {
		return validationRules;
	}

	public void setValidationRules(List<ValidationRule> validationRules) {
		this.validationRules = validationRules;
	}

	public List<Property> getChildProperties() {
		return childProperties;
	}

	public void setChildProperties(List<Property> childProperties) {
		this.childProperties = childProperties;
	}

}
