package com.mcnc.payroll.util;

import java.lang.reflect.Modifier;
import java.util.List;

import com.mcnc.payroll.enums.DataType;
import com.mcnc.payroll.enums.RuleType;
import com.mcnc.payroll.model.Property;
import com.mcnc.payroll.model.ValidationRule;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;

public class DynamicEntity {

	private DynamicEntity() {
		throw new IllegalStateException("Utility class");
	}

	public static Class<?> generate(List<Property> properties) {
		Builder<?> builder = new ByteBuddy()
				.subclass(Object.class)
				.name("DynamicEntity");

		// Add fields and method based on the validation rules
		for (Property property : properties) {
			String fieldName = property.getFieldName();
			Class<?> dataType = DataType.dataTypeOf(property.getDataType()).getType();

			builder = builder
				.defineMethod("get" + DynamicEntity.capitalize(fieldName), dataType, Modifier.PUBLIC)
				.intercept(FieldAccessor.ofField(fieldName))
				.defineMethod("set" + DynamicEntity.capitalize(fieldName), void.class, Modifier.PUBLIC)
				.withParameters(dataType)
				.intercept(FieldAccessor.ofField(fieldName));

			Optional<?> fieldBuilder = builder.defineField(fieldName, dataType, Modifier.PRIVATE);

			// Add validation annotations based on the metadata
			if (Boolean.TRUE.equals(property.isRequired())) {
				builder = DynamicEntity.addAnnotationValidateField(fieldBuilder, property);
			}

		}

		// Build and load the class
		return builder
				.make()
				.load(DynamicEntity.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
				.getLoaded();
	}

	private static String capitalize(String fieldName) {
		if (fieldName == null || fieldName.isEmpty()) {
			return fieldName;
		}
		return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	private static Builder<?> addAnnotationValidateField(Optional<?> fieldBuilder, Property property) {

		for (ValidationRule rule : property.getValidationRules()) {
			fieldBuilder = DynamicEntity.notNull(fieldBuilder, rule);
			fieldBuilder = DynamicEntity.notEmpty(fieldBuilder, rule);
			fieldBuilder = DynamicEntity.size(fieldBuilder, rule);
			fieldBuilder = DynamicEntity.min(fieldBuilder, rule);
			fieldBuilder = DynamicEntity.max(fieldBuilder, rule);
			fieldBuilder = DynamicEntity.pattern(fieldBuilder, rule);
		}

		return fieldBuilder;
	}

	private static Optional<?> pattern(Optional<?> fieldBuilder, ValidationRule rule) {
		if (RuleType.PATTERN.getValue().equalsIgnoreCase(rule.getRuleType())) {
			fieldBuilder = fieldBuilder.annotateField(
				AnnotationDescription.Builder.ofType(Pattern.class)
					.define("regexp", rule.getRuleValue())
					.define("message", rule.getErrorMessage())
					.build()
			);
		}
		return fieldBuilder;
	}

	private static Optional<?> max(Optional<?> fieldBuilder, ValidationRule rule) {
		if (RuleType.MAX.getValue().equalsIgnoreCase(rule.getRuleType())) {
			fieldBuilder = fieldBuilder.annotateField(
				AnnotationDescription.Builder.ofType(Max.class)
					.define("value", Long.parseLong(rule.getRuleValue()))
					.define("message", rule.getErrorMessage())
					.build()
			);
		}
		return fieldBuilder;
	}

	private static Optional<?> min(Optional<?> fieldBuilder, ValidationRule rule) {
		if (RuleType.MIN.getValue().equalsIgnoreCase(rule.getRuleType())) {
			fieldBuilder = fieldBuilder.annotateField(
				AnnotationDescription.Builder.ofType(Min.class)
					.define("value", Long.parseLong(rule.getRuleValue()))
					.define("message", rule.getErrorMessage())
					.build()
			);
		}
		return fieldBuilder;
	}

	private static Optional<?> size(Optional<?> fieldBuilder, ValidationRule rule) {
		if (RuleType.SIZE.getValue().equalsIgnoreCase(rule.getRuleType())) {
			String[] range = rule.getRuleValue().split(",");
			fieldBuilder = fieldBuilder.annotateField(
				AnnotationDescription.Builder.ofType(Size.class)
					.define("min", Integer.parseInt(range[0]))
					.define("max", Integer.parseInt(range[1]))
					.define("message", rule.getErrorMessage())
					.build()
			);
		}
		return fieldBuilder;
	}

	private static Optional<?> notEmpty(Optional<?> fieldBuilder, ValidationRule rule) {
		if (RuleType.NOTEMPTY.getValue().equalsIgnoreCase(rule.getRuleType())) {
			fieldBuilder = fieldBuilder.annotateField(
				AnnotationDescription.Builder.ofType(NotEmpty.class)
					.define("message", rule.getErrorMessage())
					.build()
			);
		}
		return fieldBuilder;
	}

	private static Optional<?> notNull(Optional<?> fieldBuilder, ValidationRule rule) {
		if (RuleType.NOTNULL.getValue().equalsIgnoreCase(rule.getRuleType())) {
			fieldBuilder = fieldBuilder.annotateField(
				AnnotationDescription.Builder.ofType(NotNull.class)
					.define("message", rule.getErrorMessage())
					.build()
			);
		}
		return fieldBuilder;
	}
}