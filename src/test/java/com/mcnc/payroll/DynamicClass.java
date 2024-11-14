package com.mcnc.payroll;

import java.util.ArrayList;
import java.util.List;

import com.mcnc.payroll.model.MData;
import com.mcnc.payroll.service.ValidatePropertyService;

public class DynamicClass {

	public static void main(String[] args) {

		List<MData> transferList = new ArrayList<>();
		MData itemTransfer = new MData();
		itemTransfer.put("recipientAccountNo", "00036435");
		itemTransfer.put("transactionAmount", 2.30);
		transferList.add(itemTransfer);

		itemTransfer = new MData();
		itemTransfer.put("recipientAccountNo", "00036436");
		itemTransfer.put("transactionAmount", 1.50);
		transferList.add(itemTransfer);

		MData verifyCode = new MData();
		verifyCode.put("otpCode", "123456");

		MData data = new MData();
		data.put("withdrawalAccountNo", "24543625");
		data.put("transactionCurrencyCode", "KHR");
		data.put("transferList", transferList);
		data.put("verifyCode", verifyCode);

		ValidatePropertyService validatePropertyService = new ValidatePropertyService();
		validatePropertyService.validateProperty(data);

	}
}