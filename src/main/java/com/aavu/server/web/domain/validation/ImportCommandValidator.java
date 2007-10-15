package com.aavu.server.web.domain.validation;

import java.io.StringReader;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.aavu.server.web.domain.ImportCommand;

public class ImportCommandValidator implements Validator {
	private static final Logger log = Logger.getLogger(ImportCommandValidator.class);

	public boolean supports(Class clazz) {
		return clazz.equals(ImportCommand.class);
	}

	public void validate(Object target, Errors errors) {

		ImportCommand comm = (ImportCommand) target;

		// if (!StringUtils.hasText(comm.getDeliciousName())
		// && !StringUtils.hasText(comm.getGoogleName())) {
		// errors.rejectValue("deliciousName", "invalid.importusername.oneorother");
		// errors.rejectValue("googleName", "invalid.importusername.oneorother");
		// }

		if (StringUtils.hasText(comm.getDeliciousXMLString())) {
			SAXReader reader = new SAXReader();
			try {
				Document response = reader.read(new StringReader(comm.getDeliciousXMLString()));
			} catch (DocumentException e) {
				log.warn(e);
				errors.rejectValue("deliciousXMLString", "invalid.deliciousXMLString");
			}

		}


	}
}
