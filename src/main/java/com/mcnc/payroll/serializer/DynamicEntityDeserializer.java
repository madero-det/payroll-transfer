package com.mcnc.payroll.serializer;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class DynamicEntityDeserializer extends JsonDeserializer<Object>  {
    private final Class<?> dynamicClass;

    public DynamicEntityDeserializer(Class<?> dynamicClass) {
        this.dynamicClass = dynamicClass;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        Object instance;
        try {
            instance = dynamicClass.getDeclaredConstructor().newInstance();
            for (Field field : dynamicClass.getDeclaredFields()) {
                String fieldName = field.getName();
                if (node.has(fieldName)) {
                    field.setAccessible(true);
                    // Convert JsonNode to appropriate type and set to field
                    Object value = p.getCodec().treeToValue(node.get(fieldName), field.getType());
                    field.set(instance, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize", e);
        }
        return instance;
    }

}
