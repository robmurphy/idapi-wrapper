/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;

public class Authenticator extends BaseController {
	public Authenticator(BaseController controller) {
		super(controller);
	}

	public Authenticator(String host) throws MalformedURLException, ServiceException {
		super(host);
	}

	public Authenticator(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public Authenticator(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public Authenticator(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public String getAuthId() {
		return acxControl.getAuthenticationId();
	}
}
