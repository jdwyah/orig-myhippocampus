package com.aavu.server.service.gwt;

import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

public class GWTDebugAdvice {
	private static final Logger log = Logger.getLogger(GWTDebugAdvice.class);

	public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {

		log.error("I'M HERE");
		System.out.println("\n\n!!!!!!!!!!!!");
		Date d = new Date();

		// start stopwatch
		Object retVal = pjp.proceed();

		Date end = new Date();
		System.out.println("TOOK: " + (d.getTime() - end.getTime()));

		// stop stopwatch
		return retVal;


	}
}
