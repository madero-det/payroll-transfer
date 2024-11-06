package com.mcnc.payroll.mapper;

import java.util.List;

import com.mcnc.payroll.model.MData;

public interface ApiSpecsMapper {
	List<MData> retrieveListApiSpecs(MData inputData);
}
