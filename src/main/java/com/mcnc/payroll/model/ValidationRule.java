package com.mcnc.payroll.model;

public class ValidationRule {
    private String fieldName;
    private Class<?> fieldType;
    private boolean notNull;
    private Integer minSize;
    private Integer maxSize;
    
    public ValidationRule(String fieldName, Class<?> fieldType, boolean notNull, Integer minSize, Integer maxSize) {
        this.setFieldName(fieldName);
        this.setFieldType(fieldType);
        this.setNotNull(notNull);
        this.setMinSize(minSize);
        this.setMaxSize(maxSize);
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public Class<?> getFieldType() {
        return fieldType;
    }
    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }
    public boolean isNotNull() {
        return notNull;
    }
    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }
    public Integer getMinSize() {
        return minSize;
    }
    public void setMinSize(Integer minSize) {
        this.minSize = minSize;
    }
    public Integer getMaxSize() {
        return maxSize;
    }
    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    
}
