package com.aavu.server.web.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.aavu.client.exception.HippoException;
import com.aavu.server.service.DeliciousService;
import com.aavu.server.service.TheGoogleService;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.ImportCommand;
import com.aavu.server.web.domain.validation.ImportCommandValidator;
import com.google.gdata.util.AuthenticationException;

public class ImportController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(ImportController.class);

	private UserService userService;
	private DeliciousService deliciousService;
	private TheGoogleService googleService;

	private String googleAuthReturnURL;

	public ImportController() {
		setCommandClass(ImportCommand.class);
		setValidator(new ImportCommandValidator());
	}

	private Map<String, Object> getModelForMessage(String message) {
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.put("command", new ImportCommand());
		rtn.put("message", message);
		rtn.put("googleRequestURL", googleService.getAuthorizationURL(googleAuthReturnURL));
		return rtn;
	}

	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.put("googleRequestURL", googleService.getAuthorizationURL(googleAuthReturnURL));
		return rtn;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {

		String type = request.getParameter("type");

		ImportCommand comm = (ImportCommand) command;

		if (type.equals("delicious")) {
			try {
				int found = deliciousService.newLinksForUser(comm.getDeliciousName(), comm
						.getDeliciousPass());

				String successStr = "Found "
						+ found
						+ " bookmarks. Importing in the background, it may take a bit before they are all available.";

				return new ModelAndView(getSuccessView(), "message", successStr);
			} catch (HippoException e) {

				return new ModelAndView(getFormView(), getModelForMessage("Problem Logging In "
						+ e.getMessage()));
			}
		} else if (type.equals("google")) {

			// return new ModelAndView("redirect:"
			// + googleService
			// .getAuthorizationURL(googleAuthReturnURL, comm.getGoogleDomain()));

			try {



				int found = googleService.importDocsForUser(comm.getGoogleName(), comm
						.getGooglePass());

				String successStr = "Found "
						+ found
						+ " documents. Importing in the background, it may take a bit before they are all available.";

				return new ModelAndView(getSuccessView(), "message", successStr);
			} catch (AuthenticationException e) {

				return new ModelAndView(getFormView(), getModelForMessage("Problem Logging In "
						+ e.getMessage()));
			}
		} else {
			throw new RuntimeException("No Import Type Specified");
		}

	}

	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Required
	public void setDeliciousService(DeliciousService deliciousService) {
		this.deliciousService = deliciousService;
	}

	@Required
	public void setGoogleService(TheGoogleService googleService) {
		this.googleService = googleService;
	}

	@Required
	public void setGoogleAuthReturnURL(String googleAuthReturnURL) {
		this.googleAuthReturnURL = googleAuthReturnURL;
	}



}
