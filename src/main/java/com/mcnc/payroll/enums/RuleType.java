package com.mcnc.payroll.enums;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotEmpty;

public enum RuleType {

    SIZE("size", Size.class),
    MIN("min", Min.class),
    MAX("max", Max.class),
    PATTERN("pattern", Pattern.class),
    NOTEMPTY("notempty", NotEmpty.class),
    NOTNULL("notnull", NotNull.class);

    String value;
    Class<?> type;

    RuleType(String value, Class<?> type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    
}
