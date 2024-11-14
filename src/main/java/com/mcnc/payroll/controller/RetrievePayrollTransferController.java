package com.mcnc.payroll.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mcnc.payroll.model.MData;
import com.mcnc.payroll.model.Property;
import com.mcnc.payroll.service.ApiSpecsService;
import com.mcnc.payroll.service.ValidatePropertyService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RetrievePayrollTransferController {

	private final ValidatePropertyService validatePropertyService;
	private final ApiSpecsService apiSpecsService;

	@PostMapping("/retrieve")
	public List<Property> retrieveListPayrollTrasnferData(@RequestBody MData inputData) {
		validatePropertyService.validateProperty(inputData);
		return new ArrayList<>();
	}

	@PostMapping("/retrieves")
	public List<Property> retrieveListApiSpecs(@RequestBody Property inputData) {
		return apiSpecsService.retrieveListApiSpecs(inputData);
	}
}
