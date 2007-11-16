package com.aavu.server.web.controllers;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;

import com.aavu.server.service.UserService;
import com.aavu.server.util.CryptUtils;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public class SignupIfPossibleController extends BasicController {
	private static final Logger log = Logger.getLogger(SignupIfPossibleController.class);

	public static final String SECRET = "JDSD*&@KKL@)D(@";

	private UserService userService;
	private String signupView;
	private String mailingListView;



	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse arg1)
			throws Exception {

		if (userService.nowAcceptingSignups()) {
			Map<String, Object> model = new HashMap<String, Object>();
			CreateUserRequestCommand comm = new CreateUserRequestCommand();

			Calendar c = Calendar.getInstance();
			c.get(Calendar.DAY_OF_WEEK_IN_MONTH);
			comm.setRandomkey(CryptUtils.hashString(SECRET + c.get(Calendar.DAY_OF_WEEK_IN_MONTH)));
			comm.setOpenIDusername(req.getParameter("openid_identifier"));
			model.put("hideSecretKey", true);
			model.put("command", comm);
			return new ModelAndView(getSignupView(), model);
		} else {
			return new ModelAndView(getMailingListView());
		}
	}


	public String getSignupView() {
		return signupView;
	}


	public void setSignupView(String signupView) {
		this.signupView = signupView;
	}


	public String getMailingListView() {
		return mailingListView;
	}


	public void setMailingListView(String mailingListView) {
		this.mailingListView = mailingListView;
	}

	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


}
