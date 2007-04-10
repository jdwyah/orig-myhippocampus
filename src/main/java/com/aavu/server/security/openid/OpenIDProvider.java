package com.aavu.server.security.openid;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.providers.AuthenticationProvider;

public class OpenIDProvider implements AuthenticationProvider {

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean supports(Class authentication) {
		// TODO Auto-generated method stub
		return false;
	}

}
