package com.aavu.server.web.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.ui.openid.OpenIDConsumer;
import org.acegisecurity.ui.openid.OpenIDConsumerException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class OpenIDLoginController extends AbstractController {
	private static final Logger log = Logger.getLogger(OpenIDLoginController.class);

	private OpenIDConsumer consumer;

	private static final String passwordField = "j_password";
	private String identityField = "openid_url";
	private String formLoginUrl = "/j_acegi_security_check";
	private String errorPage = "acegilogin";

	private String trustRoot;
	private String returnTo = "j_acegi_openid_security_check";

	@Required
	public void setTrustRoot(String trustRoot) {
		this.trustRoot = trustRoot;
	}

	public void setConsumer(OpenIDConsumer consumer) {
		this.consumer = consumer;
	}

	public void setIdentityField(String identityField) {
		this.identityField = identityField;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse res)
			throws Exception {

		// HttpUtils.getRequestURL(req).toString();
		// get the submitted id field
		String openID = req.getParameter(identityField);


		// assume page will validate?
		// TODO: null checking!

		// TODO: pattern matching
		String password = req.getParameter(passwordField);

		if ((password != null) && (password.length() > 0)) {
			log.debug("Attempting to authenticate using username/password " + formLoginUrl);
			log.debug("Attempting to authenticate using username/password ");

			// forward to authenticationProcessingFilter
			// (/j_acegi_security_check - depends on param names)
			return new ModelAndView(formLoginUrl);

		} else {
			log.info("No pass, doing  OPENID " + openID);
			// send the user the redirect url to proceed with OpenID
			// authentication
			try {
				String returnToURL = trustRoot + returnTo;

				log.debug("ReturnToURL to: " + returnToURL);

				String redirect = consumer.beginConsumption(req, openID, returnToURL);
				log.debug("Redirecting to: " + redirect);

				return new ModelAndView("redirect:" + redirect);

			} catch (OpenIDConsumerException oice) {
				log.error("Consumer error!", oice);
				System.out.println("oice " + oice.getMessage());

				Map<String, Object> model = new HashMap<String, Object>();
				model.put("message", oice.getMessage());
				if (oice.getCause() != null) {
					model.put("login_error", "Cause: " + oice.getCause().getMessage());
				} else {
					model.put("login_error", "Are you sure you have an OpenID account?");
				}

				return new ModelAndView(errorPage, model);
			}
		}

	}

}

// public class OpenIDLoginInitiationServlet extends HttpServlet {
// final static long serialVersionUID = -997766L;
// private static final Log logger =
// LogFactory.getLog(OpenIDLoginInitiationServlet.class);
// private static final String passwordField = "j_password";
//
// /**
// * Servlet config key for looking up the the HttpServletRequest parameter name
// * containing the OpenID Identity URL from the Servlet config.
// * <br/><b>Only set the identityField servlet init-param if you are not
// using</b> <code>j_username</code>
// * <br/>
// * <br/> &nbsp;&nbsp; &lt;init-param&gt;
// * <br/> &nbsp;&nbsp;&nbsp;&nbsp; &lt;description&gt;The identity form field
// parameter&lt;/description&gt;
// * <br/> &nbsp;&nbsp;&nbsp;&nbsp;
// &lt;param-name&gt;identityField&lt;/param-name&gt;
// * <br/> &nbsp;&nbsp;&nbsp;&nbsp;
// &lt;param-value&gt;/openid_url&lt;/param-value&gt;
// * <br/> &nbsp;&nbsp; &lt;/init-param&gt;
// */
// public static final String IDENTITY_FIELD_KEY = "identityField";
//
// /**
// * Servlet config key for the return to URL
// */
// public static final String ERROR_PAGE_KEY = "errorPage";
//
// /**
// * Servlet config key for looking up the form login URL from the Servlet
// config.
// * <br/><b>Only set the formLogin servlet init-param if you are not using</b>
// <code>/j_acegi_security_check</code>
// * <br/>
// * <br/> &nbsp;&nbsp; &lt;init-param&gt;
// * <br/> &nbsp;&nbsp;&nbsp;&nbsp; &lt;description&gt;The form login URL - for
// standard authentication&lt;/description&gt;
// * <br/> &nbsp;&nbsp;&nbsp;&nbsp;
// &lt;param-name&gt;formLogin&lt;/param-name&gt;
// * <br/> &nbsp;&nbsp;&nbsp;&nbsp;
// &lt;param-value&gt;/custom_acegi_security_check&lt;/param-value&gt;
// * <br/> &nbsp;&nbsp; &lt;/init-param&gt;
// */
// public static final String FORM_LOGIN_URL_KEY = "formLogin";
//
// /**
// * Spring context key for the OpenID consumer bean
// */
// public static final String CONSUMER_KEY = "openIDConsumer";
// private String errorPage = "index.jsp";
// private String identityField = "j_username";
// private String formLoginUrl = "/j_acegi_security_check";
//
// /**
// * Check for init-params
// *
// * @Override
// */
// public void init() throws ServletException {
// super.init();
//
// String configErrorPage = getServletConfig()
// .getInitParameter(ERROR_PAGE_KEY);
//
// if (StringUtils.hasText(configErrorPage)) {
// errorPage = configErrorPage;
// }
//
// String configIdentityField = getServletConfig()
// .getInitParameter(IDENTITY_FIELD_KEY);
//
// if (StringUtils.hasText(configIdentityField)) {
// identityField = configIdentityField;
// }
//
// String configFormLoginUrl = getServletConfig()
// .getInitParameter(FORM_LOGIN_URL_KEY);
//
// if (StringUtils.hasText(configFormLoginUrl)) {
// formLoginUrl = configFormLoginUrl;
// }
// }
//
// /**
// * Process the form post - all the work is done by the
// OpenIDConsumer.beginConsumption method
// *
// * @Override
// */
// protected void doPost(HttpServletRequest req, HttpServletResponse res)
// throws ServletException, IOException {
// WebApplicationContext webApplicationContext =
// WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
// OpenIDConsumer consumer = (OpenIDConsumer)
// webApplicationContext.getBean(CONSUMER_KEY);
//
// // get the submitted id field
// String id = req.getParameter(identityField);
//
// // assume page will validate?
// //TODO: null checking!
//
// //TODO: pattern matching
// String password = req.getParameter(passwordField);
//
//        
// if ((password != null) && (password.length() > 0)) {
// System.out.println("Attempting to authenticate using username/password
// "+formLoginUrl);
// System.out.println("user: "+req.getParameter("j_username")+"
// "+req.getParameter("j_password"));
// logger.debug("Attempting to authenticate using username/password");
//
// // forward to authenticationProcessingFilter (/j_acegi_security_check -
// depends on param names)
// req.getRequestDispatcher(formLoginUrl).forward(req, res);
//
// } else {
// System.out.println("No pass, doing just OPENID");
// // send the user the redirect url to proceed with OpenID authentication
// try {
// String redirect = consumer.beginConsumption(req, id);
// logger.debug("Redirecting to: " + redirect);
// res.sendRedirect(redirect);
// } catch (OpenIDConsumerException oice) {
// logger.error("Consumer error!", oice);
// System.out.println("oice "+oice.getMessage());
// req.setAttribute("exception", oice);
// req.setAttribute("message", oice.getMessage());
// req.getRequestDispatcher(errorPage).forward(req, res);
// }
// }
// }
// }
