package com.mcnc.payroll.service;

import java.lang.reflect.Modifier;
import java.util.List;

import com.mcnc.payroll.model.ValidationRule;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

public class DynamicClassGenerator {

    public static Class<?> generateClassFromMetadata(List<ValidationRule> validationRules) throws Exception {
        DynamicType.Builder<?> builder = new ByteBuddy()
                .subclass(Object.class)
                .name("DynamicEntity");

        // Add fields based on the validation rules
        for (ValidationRule rule : validationRules) {
            DynamicType.Builder.FieldDefinition.Optional<?> fieldBuilder = builder
                    .defineField(rule.getFieldName(), rule.getFieldType(), Modifier.PUBLIC);

            // Add validation annotations based on the metadata
            if (rule.isNotNull()) {
                fieldBuilder = fieldBuilder.annotateField(
                        AnnotationDescription.Builder.ofType(NotNull.class).build()
                );
            }
            if (rule.getMinSize() != null && rule.getMaxSize() != null && rule.getFieldType() == String.class) {
                fieldBuilder = fieldBuilder.annotateField(
                        AnnotationDescription.Builder.ofType(Size.class)
                                .define("min", rule.getMinSize())
                                .define("max", rule.getMaxSize())
                                .build()
                );
            }

            // Add field with annotations to the class builder
            builder = fieldBuilder;
        }

        // Build and load the class
        return builder.make()
                .load(DynamicClassGenerator.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
    }
}