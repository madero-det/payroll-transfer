package com.mcnc.payroll.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mcnc.payroll.mapper.ApiSpecsMapper;
import com.mcnc.payroll.model.MData;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ApiSpecsService {

	private final ApiSpecsMapper apiSpecsMapper;

	public List<MData> retrieveListApiSpecs(MData inputData) {

		MData param = new MData();
		param.setString("apiName", inputData.getString("apiName"));
		param.setString("parentFieldName", inputData.getString("parentFieldName"));
		param.setString("fieldLocation", inputData.getString("fieldLocation"));
		List<MData> apiSpecsList = apiSpecsMapper.retrieveListApiSpecs(param);

		for (MData item : apiSpecsList) {
			String[] types = {"object", "array"};
			if (Arrays.asList(types).contains(item.getString("dataType"))) {
				param.setString("apiName", inputData.getString("apiName"));
				param.setString("parentFieldName", item.getString("fieldName"));
				item.set("childFields", this.retrieveListApiSpecs(param));
			}
		}

		return apiSpecsList;
	}

}