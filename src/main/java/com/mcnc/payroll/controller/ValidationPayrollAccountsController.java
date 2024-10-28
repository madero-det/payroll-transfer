package com.mcnc.payroll.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mcnc.payroll.model.MData;
import com.mcnc.payroll.rest.service.AccountTransferRestService;
import com.mcnc.payroll.service.AsynchronousService;
import com.mcnc.payroll.util.MValidatorUtil;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ValidationPayrollAccountsController {

	private final AsynchronousService asynchronousService;
	private final AccountTransferRestService accountTransferRestService;

	@PostMapping(path = "/sync/validation")
	public List<MData> syncValidationPayroll(@RequestBody MData inpuitData) {
		List<MData> outputData = new ArrayList<>();
		MValidatorUtil.validate(inpuitData, "accountList");
		for (MData accountData : inpuitData.getListMData("accountList")) {
			MValidatorUtil.validate(accountData, "recipientAccountNo");
			MData receiverAccountData = accountTransferRestService
					.retrieveReceieverAccountData(accountData.getString("recipientAccountNo"));
			outputData.add(receiverAccountData);
		}

		return outputData;
	}

	@PostMapping(path = "/async/validation")
	public List<MData> asyncValidationPayroll(@RequestBody MData inputData) {
		List<MData> outputData = new ArrayList<>();
		List<CompletableFuture<Void>> futures = new ArrayList<>();

		for (MData accountData : inputData.getListMData("accountList")) {

			CompletableFuture<Void> future = asynchronousService
					.execute(accountTransferRestService::retrieveReceieverAccountData,
							accountData.getString("recipientAccountNo"))
					.thenAccept(outputData::add);
			futures.add(future);

		}

		// Wait for all futures to complete
		CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
		allOf.join(); // This will wait for all tasks to complete

		return outputData;
	}

}
