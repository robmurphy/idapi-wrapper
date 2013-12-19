package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;

@Deprecated
public class JavaReportViewer extends BIRTContentViewer {
	public JavaReportViewer(BaseController controller) {
		super(controller);
	}

	public JavaReportViewer(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public JavaReportViewer(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public JavaReportViewer(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}
}