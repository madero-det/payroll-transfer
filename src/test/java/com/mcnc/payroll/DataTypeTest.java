package com.mcnc.payroll;

import com.mcnc.payroll.enums.DataType;

public class DataTypeTest {

    public static void main(String[] args) {
        Class<?> type = DataType.dataTypeOf("string").getType();
        System.out.println(type);
    }

}
