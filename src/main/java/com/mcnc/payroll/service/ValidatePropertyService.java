package com.mcnc.payroll.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcnc.payroll.model.MData;
import com.mcnc.payroll.model.Property;
import com.mcnc.payroll.model.ValidationRule;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ValidatePropertyService {

	private Validator validator;

	public void validateProperty(MData inputData) throws Exception {
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
		Class<?> dynamicClass = DynamicClassGenerator.generateClassFromMetaData(properties);
		
		// Create an instance of the class
		Object instance = dynamicClass.getDeclaredConstructor().newInstance();

		// Set field values dynamically
		MData requestData = new MData();
		requestData.setString("withdrawalAccountNo", "24543625");
		requestData.setString("transactionCurrencyCode", "");
		ObjectMapper mapper = new ObjectMapper();
		instance = mapper.convertValue(requestData, dynamicClass);
		// setField(instance, "withdrawalAccountNo", "24543625");
		// setField(instance, "transactionCurrencyCode", "");

		// Validate the dynamic instance
		validateDynamicInstance(instance);
	}

	private void setField(Object instance, String fieldName, Object value) throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(instance, value);
	}

	private void validateDynamicInstance(Object instance) {
		Set<ConstraintViolation<Object>> violations = validator.validate(instance);
		if (!violations.isEmpty()) {
			log.info(UUID.randomUUID().toString());
		}
		for (ConstraintViolation<Object> violation : violations) {
			log.info(violation.getMessage());
		}
	}

}
