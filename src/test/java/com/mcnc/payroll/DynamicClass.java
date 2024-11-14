package com.mcnc.payroll;

import java.util.ArrayList;
import java.util.List;

import com.mcnc.payroll.model.MData;
import com.mcnc.payroll.service.ValidatePropertyService;

public class DynamicClass {

	public static void main(String[] args) throws Exception {

		List<MData> transferList = new ArrayList<>();
		MData itemTransfer = new MData();
		itemTransfer.put("recipientAccountNo", "24543625");
		itemTransfer.put("transactionAmount", 10000);
		transferList.add(itemTransfer);

		MData data = new MData();
		data.put("withdrawalAccountNo", "24543625");
		data.put("transactionCurrencyCode", "THB");
		data.put("transferList", transferList);

		ValidatePropertyService validatePropertyService = new ValidatePropertyService();
		validatePropertyService.validateProperty(data);

	}
}