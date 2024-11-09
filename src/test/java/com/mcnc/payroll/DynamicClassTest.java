package com.mcnc.payroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mcnc.payroll.model.Field;
import com.mcnc.payroll.model.ValidationRule;
import com.mcnc.payroll.service.DynamicClassGenerator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

public class DynamicClassTest {

    private static Validator validator;

    public static void main(String[] args) throws Exception {
        // Initialize validator (typically injected in Spring)
        try (LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean()) {
            factoryBean.afterPropertiesSet();
            validator = factoryBean.getValidator();
        }
        // Mock metadata from the database
        List<Field> fields = List.of(
            new Field("name", "string", List.of(
                new ValidationRule("size", "25,50", "Name must be between 25 to 50 characters.")
            )),
            new Field("age", "integer", List.of(
                new ValidationRule("notnull", "", "Age must be not null."),
                new ValidationRule("min", "15", "Age should be more than 18 years old.")
            ))
        );

        // Generate the dynamic class
        Class<?> dynamicClass = DynamicClassGenerator.generateClassFromMetadata(fields);
        
        // Create an instance of the class
        Object instance = dynamicClass.getDeclaredConstructor().newInstance();

        // Set field values dynamically
        setField(instance, "name", "John Doe");
        setField(instance, "age", 14);

        // Validate the dynamic instance
        validateDynamicInstance(instance);
    }

    private static void setField(Object instance, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    private static void validateDynamicInstance(Object instance) {
        Set<ConstraintViolation<Object>> violations = validator.validate(instance);
        for (ConstraintViolation<Object> violation : violations) {
            System.out.println(violation.getMessage());
        }
    }
}