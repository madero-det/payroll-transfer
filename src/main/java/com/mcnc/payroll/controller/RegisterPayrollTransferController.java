package com.mcnc.payroll.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mcnc.payroll.model.MData;
import com.mcnc.payroll.service.AsynchronousService;
import com.mcnc.payroll.service.PayrollTransferService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RegisterPayrollTransferController {

	private final AsynchronousService asynchronousService;
	private final PayrollTransferService payrollTransferService;

	@PostMapping("/sync/register")
	public MData registerPayrollTrasnferData(@RequestBody MData inputData) {
		MData outputData = new MData();

		for (MData payrollData : inputData.getListMData("payrollList")) {
			payrollTransferService.registerPayrollTransferData(payrollData);
		}

		outputData.setString("resultCode", "000");
		outputData.setString("resultDescription", "Success");
		return outputData;
	}

	@PostMapping("/async/register")
	public MData asyncRegisterPayrollTrasnferData(@RequestBody MData inputData) {
		MData outputData = new MData();
		List<CompletableFuture<Void>> futures = new ArrayList<>();

		for (MData payrollData : inputData.getListMData("payrollList")) {

			CompletableFuture<Void> future = asynchronousService.registerPayrollTransferData(payrollData);
			futures.add(future);

		}

		// Wait for all futures to complete
		CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
		allOf.join(); // This will wait for all tasks to complete

		outputData.setString("resultCode", "000");
		outputData.setString("resultDescription", "Success");
		return outputData;
	}
}
