package com.mcnc.payroll.service;

import java.lang.reflect.Modifier;
import java.util.List;

import com.mcnc.payroll.enums.DataType;
import com.mcnc.payroll.enums.RuleType;
import com.mcnc.payroll.model.Field;
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

public class DynamicClassGenerator {

    public static Class<?> generateClassFromMetadata(List<Field> fields) throws Exception {
        DynamicType.Builder<?> builder = new ByteBuddy()
                .subclass(Object.class)
                .name("DynamicEntity");

        // Add fields based on the validation rules
        for (Field field : fields) {
            DynamicType.Builder.FieldDefinition.Optional<?> fieldBuilder = builder
                    .defineField(field.getFieldName(), DataType.dataTypeOf(field.getDataType()).getType(), Modifier.PUBLIC);

            // Add validation annotations based on the metadata
            for (ValidationRule rule : field.getValidationRules()) {
                if (RuleType.NOTNULL.getValue().equalsIgnoreCase(rule.getRuleName())) {
                    fieldBuilder = fieldBuilder.annotateField(
                        AnnotationDescription.Builder.ofType(NotNull.class)
                        .define("message", rule.getErrorMessage())
                        .build()
                    );
                } 

                if (RuleType.NOTEMPTY.getValue().equalsIgnoreCase(rule.getRuleName())) {
                    fieldBuilder = fieldBuilder.annotateField(
                        AnnotationDescription.Builder.ofType(NotEmpty.class)
                        .define("message", rule.getErrorMessage())
                        .build()
                    );
                }

                if (RuleType.SIZE.getValue().equalsIgnoreCase(rule.getRuleName())) {
                    String range[] = rule.getRuleValue().split(",");
                    fieldBuilder = fieldBuilder.annotateField(
                        AnnotationDescription.Builder.ofType(Size.class)
                        .define("min", Integer.parseInt(range[0]))
                        .define("max", Integer.parseInt(range[1]))
                        .define("message", rule.getErrorMessage())
                        .build()
                    );
                }

                if (RuleType.MIN.getValue().equalsIgnoreCase(rule.getRuleName())) {
                    fieldBuilder = fieldBuilder.annotateField(
                        AnnotationDescription.Builder.ofType(Min.class)
                        .define("value", Long.parseLong(rule.getRuleValue()))
                        .define("message", rule.getErrorMessage())
                        .build()
                    );
                }

                if (RuleType.MAX.getValue().equalsIgnoreCase(rule.getRuleName())) {
                    fieldBuilder = fieldBuilder.annotateField(
                        AnnotationDescription.Builder.ofType(Max.class)
                        .define("value", Long.parseLong(rule.getRuleValue()))
                        .define("message", rule.getErrorMessage())
                        .build()
                    );
                }

                if (RuleType.PATTERN.getValue().equalsIgnoreCase(rule.getRuleName())) {
                    fieldBuilder = fieldBuilder.annotateField(
                        AnnotationDescription.Builder.ofType(Pattern.class)
                        .define("regex", rule.getRuleValue())
                        .define("message", rule.getErrorMessage())
                        .build()
                    );
                }
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