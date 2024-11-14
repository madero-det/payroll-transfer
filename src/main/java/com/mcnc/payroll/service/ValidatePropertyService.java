package com.mcnc.payroll.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcnc.payroll.model.MData;
import com.mcnc.payroll.model.Property;
import com.mcnc.payroll.model.ValidationRule;
import com.mcnc.payroll.util.DynamicEntity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ValidatePropertyService {

	private Validator validator;

	public void validateProperty(MData inputData) {
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
			), null),
			new Property("TRN10100521", "transferList", "", "list", "request", true, List.of(
				new ValidationRule("valid", "", "Transfer list is invalid."),
				new ValidationRule("size", "1,10", 	"Transfer list must have between 1 and 10 items.")
			), List.of(
				new Property("TRN10100521", "recipientAccountNo", "transferList", "string", "request", true, List.of(
					new ValidationRule("notnull", "", "Recipient account number cannot be null."),
					new ValidationRule("notempty", "", "Recipient account number must not be empty.")
				), null),
				new Property("TRN10100521", "transactionAmount", "transferList", "bigdecimal", "request", true, List.of(
					new ValidationRule("notnull", "", "Transaction amount cannot be null."),
					new ValidationRule("decimalmin", "0.01", "Transaction amount must be greater than or equal to 0.01")
				), null)
 			)),
			new Property("TRN10100521", "verifyCode", "", "object", "request", true, List.of(
				new ValidationRule("valid", "", "Verify code is invalid.")
			), List.of(
				new Property("TRN10100521", "otpCode", "verifyCode", "string", "request", true, List.of(
					new ValidationRule("notnull", "", "OTP code cannot be null."),
					new ValidationRule("notempty", "", "OTP code must not be empty.")
				), null)
			))
		);

		// Generate the dynamic class
		Class<?> dynamicClass = DynamicEntity.generate(DynamicEntity.capitalize("TRN10100521"), properties);

		ObjectMapper mapper = new ObjectMapper();
		Object instance = mapper.convertValue(inputData, dynamicClass);

		// Validate the dynamic instance
		this.validateDynamicInstance(instance);
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
