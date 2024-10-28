package com.mcnc.payroll.mapper;

import java.util.List;

import com.mcnc.payroll.model.MData;

public interface PayrollTransferMapper {

	int registerPayrollTransferData(MData inputData);

	List<MData> retrieveListPayrollTransferData(MData inputData);

}
