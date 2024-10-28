package com.mcnc.payroll.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mcnc.payroll.mapper.PayrollTransferMapper;
import com.mcnc.payroll.model.MData;

@Service
public class PayrollTransferService {

	private final PayrollTransferMapper payrollTransferMapper;

	public PayrollTransferService(PayrollTransferMapper payrollTransferMapper) {
		this.payrollTransferMapper = payrollTransferMapper;
	}

	public int registerPayrollTransferData(MData inputData) {
		return payrollTransferMapper.registerPayrollTransferData(inputData);
	}

	public List<MData> retrieveListPayrollTransferData(MData inputData) {
		return payrollTransferMapper.retrieveListPayrollTransferData(inputData);
	}

}
