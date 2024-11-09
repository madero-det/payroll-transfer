package com.mcnc.payroll.enums;

import java.math.BigDecimal;

public enum DataType {

    INTEGER("integer", Integer.class),
    BIGDECIMAL("bigdecimal", BigDecimal.class),
    LONG("long", Long.class),
    BOOLEAN("boolean", Boolean.class),
    STRING("string", String.class);

    String value;
    Class<?> type;

    DataType(String value, Class<?> type) {
        this.value = value;
        this.type = type;
    }

    public static DataType dataTypeOf(String value) {
        for (DataType dataType : DataType.values()) {
            if (dataType.getValue().equalsIgnoreCase(value)) {  // Custom matching logic (case-insensitive in this case)
                return dataType;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
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
