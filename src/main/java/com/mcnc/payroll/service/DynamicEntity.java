package com.mcnc.payroll.service;

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
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;

public class DynamicEntity {

	public static Class<?> generate(List<Property> properties) throws Exception {
		DynamicType.Builder<?> builder = new ByteBuddy()
			.subclass(Object.class)
			.name("DynamicEntity");

		// Add fields and method based on the validation rules
		for (Property property : properties) {
			String fieldName = property.getFieldName();
			Class<?> dataType = DataType.dataTypeOf(property.getDataType()).getType();

			// Add method getter and setter base on field name
			builder
				.defineMethod("get" + capitalize(fieldName), dataType, Modifier.PUBLIC)
				.intercept(FieldAccessor.ofField(fieldName))
				.defineMethod("set" + capitalize(fieldName), Void.class, Modifier.PUBLIC)
				.withParameter(dataType)
				.intercept(FieldAccessor.ofField(fieldName));

			DynamicType.Builder.FieldDefinition.Optional<?> fieldBuilder = builder
				.defineField(fieldName, dataType, Modifier.PRIVATE);

			// Add validation annotations based on the metadata
			if (property.isRequired()) {
				builder = addAnnotationValidateField(fieldBuilder, property);
			}

		}

		// Build and load the class
		return builder.make()
				.load(DynamicEntity.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
	}

	private static String capitalize(String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

	private static DynamicType.Builder<?> addAnnotationValidateField(DynamicType.Builder.FieldDefinition.Optional<?> fieldBuilder, Property property) {
		
		for (ValidationRule rule : property.getValidationRules()) {
			if (RuleType.NOTNULL.getValue().equalsIgnoreCase(rule.getRuleType())) {
				fieldBuilder = fieldBuilder.annotateField(
					AnnotationDescription.Builder.ofType(NotNull.class)
					.define("message", rule.getErrorMessage())
					.build()
				);
			} 

			if (RuleType.NOTEMPTY.getValue().equalsIgnoreCase(rule.getRuleType())) {
				fieldBuilder = fieldBuilder.annotateField(
					AnnotationDescription.Builder.ofType(NotEmpty.class)
					.define("message", rule.getErrorMessage())
					.build()
				);
			}

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

			if (RuleType.MIN.getValue().equalsIgnoreCase(rule.getRuleType())) {
				fieldBuilder = fieldBuilder.annotateField(
					AnnotationDescription.Builder.ofType(Min.class)
					.define("value", Long.parseLong(rule.getRuleValue()))
					.define("message", rule.getErrorMessage())
					.build()
				);
			}

			if (RuleType.MAX.getValue().equalsIgnoreCase(rule.getRuleType())) {
				fieldBuilder = fieldBuilder.annotateField(
					AnnotationDescription.Builder.ofType(Max.class)
					.define("value", Long.parseLong(rule.getRuleValue()))
					.define("message", rule.getErrorMessage())
					.build()
				);
			}

			if (RuleType.PATTERN.getValue().equalsIgnoreCase(rule.getRuleType())) {
				fieldBuilder = fieldBuilder.annotateField(
					AnnotationDescription.Builder.ofType(Pattern.class)
					.define("regexp", rule.getRuleValue())
					.define("message", rule.getErrorMessage())
					.build()
				);
			}
		}

		return fieldBuilder;
	}
}