package com.mcnc.payroll.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Property {

	private String apiName;
	private String fieldName;
	private String parentFieldName;
	private String dataType;
	private String fieldLocation;
	private boolean isRequired;
	private List<ValidationRule> validationRules;
	private List<Property> childProperties;

}
