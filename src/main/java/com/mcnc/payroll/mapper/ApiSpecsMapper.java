package com.mcnc.payroll.mapper;

import java.util.List;

import com.mcnc.payroll.model.Property;

public interface ApiSpecsMapper {
	List<Property> retrieveListApiSpecs(Property property);
}
