package com.mcnc.payroll.util;

import java.lang.reflect.Modifier;
import java.util.List;

import com.mcnc.payroll.enums.DataType;
import com.mcnc.payroll.model.Property;
import com.mcnc.payroll.model.ValidationRule;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeDescription.Generic;
import net.bytebuddy.description.type.TypeDescription.Generic.OfNonGenericType;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;

public class DynamicEntity {

	private DynamicEntity() {
		throw new IllegalStateException("Utility class");
	}

	public static Class<?> generate(String className, List<Property> properties) {
		Builder<?> builder = new ByteBuddy()
				.subclass(Object.class)
				.name(className);

		// Add fields and method based on the validation rules
		for (Property property : properties) {
			String fieldName = property.getFieldName();
			Class<?> dataType = DataType.dataTypeOf(property.getDataType()).getType();
			Generic fieldType = null;

			if (dataType == Object.class) {
				dataType = DynamicEntity.generate(DynamicEntity.capitalize(property.getFieldName()), property.getChildProperties());
				fieldType = OfNonGenericType.ForLoadedType.of(dataType);
			} else if (dataType == List.class) {
				dataType = DynamicEntity.generate(DynamicEntity.capitalize(property.getFieldName()), property.getChildProperties());
				fieldType = TypeDescription.Generic.Builder
					.parameterizedType(List.class, dataType)
					.build();
			} else {
				fieldType = OfNonGenericType.ForLoadedType.of(dataType);
			}

			builder = builder
				.defineMethod("get" + DynamicEntity.capitalize(fieldName), fieldType, Modifier.PUBLIC)
				.intercept(FieldAccessor.ofField(fieldName))
				.defineMethod("set" + DynamicEntity.capitalize(fieldName), void.class, Modifier.PUBLIC)
				.withParameters(fieldType)
				.intercept(FieldAccessor.ofField(fieldName));

			Optional<?> fieldBuilder = builder.defineField(fieldName, fieldType, Modifier.PRIVATE);

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

	public static String capitalize(String fieldName) {  
		if (fieldName == null || fieldName.isEmpty()) {
			return fieldName;
		}
		return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	private static Builder<?> addAnnotationValidateField(Optional<?> fieldBuilder, Property property) {

		for (ValidationRule rule : property.getValidationRules()) {
			fieldBuilder = DynamicEntity.applyValidationRule(fieldBuilder, rule);
		}

		return fieldBuilder;
	}

	private static Optional<?> applyValidationRule(Optional<?> fieldBuilder, ValidationRule rule) {
		switch (rule.getRuleType().toLowerCase()) {
			case "valid":
				return fieldBuilder.annotateField(AnnotationDescription.Builder.ofType(Valid.class)
						.build());
			case "notnull":
				return fieldBuilder.annotateField(AnnotationDescription.Builder.ofType(NotNull.class)
						.define("message", rule.getErrorMessage())
						.build());
			case "notempty":
				return fieldBuilder.annotateField(AnnotationDescription.Builder.ofType(NotEmpty.class)
						.define("message", rule.getErrorMessage())
						.build());
			case "pattern":
				return fieldBuilder.annotateField(AnnotationDescription.Builder.ofType(Pattern.class)
						.define("regexp", rule.getRuleValue())
						.define("message", rule.getErrorMessage())
						.build());
			case "decimalmin":
				return fieldBuilder.annotateField(AnnotationDescription.Builder.ofType(DecimalMin.class)
						.define("value", rule.getRuleValue())
						.define("message", rule.getErrorMessage())
						.build());
			case "decimalmax":
				return fieldBuilder.annotateField(AnnotationDescription.Builder.ofType(DecimalMax.class)
						.define("value", rule.getRuleValue())
						.define("message", rule.getErrorMessage())
						.build());
			case "size":
				String[] sizeRange = rule.getRuleValue().split(",");
				return fieldBuilder.annotateField(AnnotationDescription.Builder.ofType(Size.class)
						.define("min", Integer.parseInt(sizeRange[0].trim()))
						.define("max", Integer.parseInt(sizeRange[1].trim()))
						.define("message", rule.getErrorMessage())
						.build());
			case "min":
				return fieldBuilder.annotateField(AnnotationDescription.Builder.ofType(Min.class)
						.define("min", rule.getRuleValue())
						.define("message", rule.getErrorMessage())
						.build());
			case "max":
				return fieldBuilder.annotateField(AnnotationDescription.Builder.ofType(Max.class)
						.define("min", rule.getRuleValue())
						.define("message", rule.getErrorMessage())
						.build());
			default:
				throw new IllegalArgumentException("Unknown validation rule: " + rule.getRuleType());
		}
	}

}