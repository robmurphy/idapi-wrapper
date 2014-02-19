/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.control;

/*
This class supplement SOAP Header access to the generated code
for use with Actuate server.
*/

import com.actuate.schemas.internal.ActuateAPILocator;
import org.apache.axis.client.Call;
import org.apache.axis.message.SOAPHeaderElement;

import javax.xml.rpc.ServiceException;

public class ActuateAPIInternalLocatorEx extends ActuateAPILocator implements ActuateAPIInternalEx {

	private AcxControl controller = null;
	private Call call = null;

	public ActuateAPIInternalLocatorEx(AcxControl controller) {
		super();
		this.controller = controller;
	}

	public Call createCall() throws ServiceException {

		//If authentication Time is > 0 and has expired, re-issue login based on controller's user/pass/extendedCredentials
		if (controller.isAuthenticationExpired())
			try {
				controller.systemLogin();
			} catch (ActuateException e) {
				e.printStackTrace();
				return null;
			}

		call = (Call) super.createCall();

		if (controller.getAuthenticationId() != null)
			call.addHeader(new SOAPHeaderElement(null, "AuthId", controller.getAuthenticationId()));

		if (controller.getConnectionHandle() != null)
			call.addHeader(new SOAPHeaderElement(null, "ConnectionHandle", controller.getConnectionHandle()));

		if (controller.getLocale() != null)
			call.addHeader(new SOAPHeaderElement(null, "Locale", controller.getLocale()));

		if (controller.getTargetVolume() != null)
			call.addHeader(new SOAPHeaderElement(null, "TargetVolume", controller.getTargetVolume()));

		if (controller.getFileType() != null)
			call.addHeader(new SOAPHeaderElement(null, "FileType", controller.getFileType()));

		if (controller.getDelayFlush() != null)
			call.addHeader(new SOAPHeaderElement(null, "DelayFlush", controller.getDelayFlush()));

		return call;
	}

	public Call getCall() {
		if (call == null) {
			try {
				createCall();
			} catch (ServiceException e) {
			}
		}
		return call;
	}

}
