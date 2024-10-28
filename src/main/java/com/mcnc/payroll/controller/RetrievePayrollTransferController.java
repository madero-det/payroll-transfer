package com.mcnc.payroll.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mcnc.payroll.model.MData;
import com.mcnc.payroll.service.PayrollTransferService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RetrievePayrollTransferController {

	private final PayrollTransferService payrollTransferService;

	@PostMapping("/sync/retrieve")
	public MData retrieveListPayrollTrasnferData(@RequestBody MData inputData) {
		MData outputData = new MData();

		List<MData> listPayrollTransferData = payrollTransferService.retrieveListPayrollTransferData(new MData());

		outputData.setString("resultCode", "000");
		outputData.setString("resultDescription", "Success");
		outputData.setListMData("payrollList", listPayrollTransferData);
		return outputData;
	}

}
