package com.mcnc.payroll;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mcnc.payroll.model.MData;
import com.mcnc.payroll.model.Property;
import com.mcnc.payroll.model.ValidationRule;
import com.mcnc.payroll.serializer.DynamicEntityDeserializer;
import com.mcnc.payroll.util.DynamicEntity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

public class DynamicClass {

	private static Validator validator;

	public static void main(String[] args) throws Exception {
		// Initialize validator (typically injected in Spring)
		try (LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean()) {
			factoryBean.afterPropertiesSet();
			validator = factoryBean.getValidator();
		}
		// Mock metadata from the database
		List<Property> properties = List.of(
			new Property("TRN10100521", "withdrawalAccountNo", "", "string", "request", true, List.of(
				new ValidationRule("notnull", "", "Withdrawal account number cannot be null."),
				new ValidationRule("notempty", "", "Withdrawal account number must not be empty.")
			), null),
			new Property("TRN10100521", "transactionCurrencyCode", "", "string", "request", true, List.of(
				new ValidationRule("notnull", "", "Transaction currency code cannot be null."),
				new ValidationRule("notempty", "", "Transaction currency code cannot be empty."),
				new ValidationRule("pattern", "^[USD|KHR]{3}$", "Transaction currency code must be USD or KHR.")
			), null)
		);

		// Generate the dynamic class
		Class<?> dynamicClass = DynamicEntity.generate(properties);
		
		// Create an instance of the class
		Object instance = dynamicClass.getDeclaredConstructor().newInstance();

		// SimpleModule module = new SimpleModule();
		// module.addDeserializer(dynamicClass, new DynamicEntityDeserializer(dynamicClass));

		// ObjectMapper mapper = new ObjectMapper();
		// mapper.registerModule(module);

		// MData data = new MData();
		// data.put("withdrawalAccountNo", "24543625");
		// data.put("transactionCurrencyCode", "");
		// Object pojoInstance = mapper.convertValue(data, dynamicClass);

		// Set field values dynamically
		setField(instance, "withdrawalAccountNo", "24543625");
		setField(instance, "transactionCurrencyCode", "");

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
			System.out.println(violation.getMessage());
		}
	}
}