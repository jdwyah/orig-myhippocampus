package com.aavu.server.service.gwt;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import com.aavu.client.exception.HippoException;

public class GWTDebugAdvice {

	private static final Logger log = Logger.getLogger(GWTDebugAdvice.class);

	/**
	 * Make sure we realize what errors are being sent back to the client
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	public Object wrapGWT(ProceedingJoinPoint pjp) throws Throwable {

		try {
			// start stopwatch
			Object retVal = pjp.proceed();

			return retVal;

		} catch (Exception e) {
			log.error("FAILURE: " + e + " " + e.getMessage());
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
			throw new HippoException(e);
		}

	}
}
