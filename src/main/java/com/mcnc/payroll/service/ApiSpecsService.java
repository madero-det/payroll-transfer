package com.mcnc.payroll.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mcnc.payroll.mapper.ApiSpecsMapper;
import com.mcnc.payroll.model.Property;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ApiSpecsService {

	private final ApiSpecsMapper apiSpecsMapper;

	public List<Property> retrieveListApiSpecs(Property inputData) {

		List<Property> properties = apiSpecsMapper.retrieveListApiSpecs(inputData);

		for (Property property : properties) {
			String[] types = {"object", "array"};
			if (Arrays.asList(types).contains(property.getDataType())) {
				property.setChildProperties(this.retrieveListApiSpecs(property));
			}
		}

		return properties;
	}

}