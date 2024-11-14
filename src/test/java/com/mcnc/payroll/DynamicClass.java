package com.mcnc.payroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcnc.payroll.model.MData;
import com.mcnc.payroll.model.Property;
import com.mcnc.payroll.model.ValidationRule;
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
 			))
		);

		// Generate the dynamic class
		Class<?> dynamicClass = DynamicEntity.generate(properties);

		List<MData> transferList = new ArrayList<>();
		MData itemTransfer = new MData();
		itemTransfer.put("recipientAccountNo", "24543625");
		itemTransfer.put("transactionAmount", 10000);
		transferList.add(itemTransfer);

		MData data = new MData();
		data.put("withdrawalAccountNo", "24543625");
		data.put("transactionCurrencyCode", "THB");
		data.put("transferList", transferList);

		// Set field values dynamically
		ObjectMapper mapper = new ObjectMapper();
		Object instance = mapper.convertValue(data, dynamicClass);

		// Validate the dynamic instance
		validateDynamicInstance(instance);
	}

	private static void validateDynamicInstance(Object instance) {
		Set<ConstraintViolation<Object>> violations = validator.validate(instance);
		for (ConstraintViolation<Object> violation : violations) {
			System.out.println(violation.getMessage());
		}
	}
}