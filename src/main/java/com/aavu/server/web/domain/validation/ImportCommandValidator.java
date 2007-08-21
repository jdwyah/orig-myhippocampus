package com.aavu.server.web.domain.validation;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.aavu.server.web.domain.ImportCommand;

public class ImportCommandValidator implements Validator {

	public boolean supports(Class clazz) {
		return clazz.equals(ImportCommand.class);
	}

	public void validate(Object target, Errors errors) {

		ImportCommand comm = (ImportCommand) target;

		if (!StringUtils.hasText(comm.getDeliciousName())
				&& !StringUtils.hasText(comm.getGoogleName())) {
			errors.rejectValue("deliciousName", "invalid.importusername.oneorother");
			errors.rejectValue("googleName", "invalid.importusername.oneorother");
		}

	}
}
