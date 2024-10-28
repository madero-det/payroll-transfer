package com.mcnc.payroll.rest.service;

import java.util.Random;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.mcnc.payroll.exception.ResponseResultException;
import com.mcnc.payroll.model.MData;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountTransferRestService {

	private Random random = new Random();

	@Retryable(retryFor = { ResponseResultException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	@Cacheable(cacheNames = "validateAccounts", key = "#accountNo")
	public MData retrieveReceieverAccountData(String accountNo) {
		MData outputData = new MData();

		try {
			int sleepTime = random.nextInt(101) + 300; // Generates a random number between 300 and 400

			Thread.sleep(sleepTime);
			outputData.setString("responseResult", "SuccessResult " + accountNo);
			outputData.setLong("sleepTime", sleepTime);

		} catch (InterruptedException e) {
			outputData.setString("responseResult", "ErrorException");
			Thread.currentThread().interrupt();
		}

		if (shouldRetry(outputData)) {
			throw new ResponseResultException("Retrying due to invalid response");
		}

		return outputData;
	}

	private boolean shouldRetry(MData response) {
		return response.getString("responseResult").contains("ErrorException");
	}

	@Recover
	public MData recoverRetry(ResponseResultException e, String accountNo) {
		// Handle the failure after retries are exhausted
		MData outputData = new MData();
		outputData.setString("responseResult", "RecoverResult");
		outputData.setLong("sleepTime", 0);
		return outputData;
	}

}
