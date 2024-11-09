package com.mcnc.payroll;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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
        List<ValidationRule> validationRules = List.of(
                new ValidationRule("name", String.class, true, 3, 50),
                new ValidationRule("age", Integer.class, false, null, null)
        );

        // Generate the dynamic class
        Class<?> dynamicClass = DynamicClassGenerator.generateClassFromMetadata(validationRules);
        
        // Create an instance of the class
        Object instance = dynamicClass.getDeclaredConstructor().newInstance();

        // Set field values dynamically
        setField(instance, "name", "John Doe");
        setField(instance, "age", 1);

        // Validate the dynamic instance
        validateDynamicInstance(instance);
    }

    private static void setField(Object instance, String fieldName, Object value) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    private static void validateDynamicInstance(Object instance) {
        Set<ConstraintViolation<Object>> violations = validator.validate(instance);
        for (ConstraintViolation<Object> violation : violations) {
            System.out.println(violation.getPropertyPath() + " " + violation.getMessage());
        }
    }
}