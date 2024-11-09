package com.mcnc.payroll.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mcnc.payroll.model.Property;
import com.mcnc.payroll.service.ApiSpecsService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RetrievePayrollTransferController {

	private final ApiSpecsService apiSpecsService;

	@PostMapping("/retrieve")
	public List<Property> retrieveListPayrollTrasnferData(@RequestBody Property inputData) {
		return apiSpecsService.retrieveListApiSpecs(inputData);
	}

}
