package com.mcnc.payroll.service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mcnc.payroll.model.MData;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AsynchronousService {

	private final PayrollTransferService payrollTransferService;

	@Async("payrollExecutor")
	public CompletableFuture<MData> execute(Function<String, MData> callback, String accountNo) {
		MData receiverAccountData = callback.apply(accountNo);
		log.info("Completed retrieval for account data: {}", accountNo);
		return CompletableFuture.completedFuture(receiverAccountData);
	}

	@Async("payrollExecutor")
	public CompletableFuture<Void> registerPayrollTransferData(MData payrollData) {
		payrollTransferService.registerPayrollTransferData(payrollData);
		log.info("Completed register for account data: {}", payrollData);
		return CompletableFuture.completedFuture(null);
	}
}
