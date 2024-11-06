package com.mcnc.payroll.util;

import java.util.ArrayList;

import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import com.mcnc.payroll.model.MData;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class MValidatorUtil {

	@SuppressWarnings("unchecked")
	public void validate(@NonNull MData ipParam, String... sField) {
		if (ipParam.isEmpty()) {
			log.debug(String.format(">>> MData cannot be empty"));
		}

		for (String sKey : sField) {

			if (sKey.isEmpty()) {
				log.debug(">>> Key cannot be empty");
			}

			Object obj = ipParam.get(sKey);
			if (obj instanceof ArrayList) {
				if (((ArrayList<MData>) obj).isEmpty())
					log.debug(String.format(">>> List<MData> of %s cannot be empty", sKey));
			} else if (obj instanceof MData) {
				if (((MData) obj).isEmpty())
					log.debug(String.format(">>> MData of %s cannot be empty", sKey));
			} else {
				if (StringUtils.hasText(ipParam.getString(sKey))) {
					log.debug(String.format(">>> String of %s cannot be empty", sKey));
				}
			}
		}
	}

}
