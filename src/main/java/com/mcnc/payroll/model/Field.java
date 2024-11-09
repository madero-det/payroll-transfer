package com.mcnc.payroll.model;

import java.util.List;

public class Field {
    String fieldName;
    String dataType;
    List<ValidationRule> validationRules;

    public Field(String fieldName, String dataType, List<ValidationRule> validationRules) {
        this.fieldName = fieldName;
        this.dataType = dataType;
        this.validationRules = validationRules;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public List<ValidationRule> getValidationRules() {
        return validationRules;
    }
    public void setValidationRules(List<ValidationRule> validationRules) {
        this.validationRules = validationRules;
    }

}
