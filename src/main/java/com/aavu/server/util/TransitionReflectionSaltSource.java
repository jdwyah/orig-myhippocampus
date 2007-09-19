package com.aavu.server.util;

import java.lang.reflect.Method;
import java.util.Date;

import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.providers.dao.salt.ReflectionSaltSource;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Ok, CodingHorror guilted me into salting passwords.
 * http://www.codinghorror.com/blog/archives/000953.html
 * 
 * The trick is, that's really a decision you'd prefer to make BEFORE you've hashed a whole bunch of
 * passwords. Without making everyone reset their passwords, this class allows you to transition to
 * using salt in an AcegiSecurity system. This class will salt users from after the
 * useSaltAfterDate. Null useSaltAfterDate will not be salted.
 * 
 * @author Jeff Dwyer (blog) http://jdwyah.blogspot.com
 * 
 */
public class TransitionReflectionSaltSource extends ReflectionSaltSource {

	private static final Logger log = Logger.getLogger(TransitionReflectionSaltSource.class);

	private String useSaltAfterDateProperty;
	private Date useSaltAfterDate;

	@Override
	public Object getSalt(UserDetails user) {
		try {
			Method reflectionMethod = user.getClass().getMethod(this.useSaltAfterDateProperty,
					new Class[] {});

			Date userDate = (Date) reflectionMethod.invoke(user, new Object[] {});

			if (null == userDate || userDate.before(useSaltAfterDate)) {
				log.debug("No Salt " + user + " " + userDate);
				return null;
			} else {
				log.debug("Using Salt " + user + " " + userDate);
				return super.getSalt(user);
			}

		} catch (Exception exception) {
			throw new AuthenticationServiceException(exception.getMessage(), exception);
		}
	}

	@Required
	public void setUseSaltAfterDateProperty(String useSaltAfterDateProperty) {
		this.useSaltAfterDateProperty = useSaltAfterDateProperty;
	}

	@Required
	public void setUseSaltAfterDate(Date useSaltAfterDate) {
		this.useSaltAfterDate = useSaltAfterDate;
	}
}
