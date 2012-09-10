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

	private String authId = "";
	private String locale = "en_US";
	private String targetVolume;
	private String connectionHandle;
	private Boolean delayFlush;
	private Call call = null;
	private String fileType;

	public ActuateAPIInternalLocatorEx() {
		super();
	}

	public Call createCall() throws ServiceException {
		call = (Call) super.createCall();

		if (authId != null)
			call.addHeader(new SOAPHeaderElement(null, "AuthId", authId));

		if (locale != null)
			call.addHeader(new SOAPHeaderElement(null, "Locale", locale));

		if (targetVolume != null)
			call.addHeader(new SOAPHeaderElement(null, "TargetVolume", targetVolume));

		if (fileType != null)
			call.addHeader(new SOAPHeaderElement(null, "FileType", fileType));

		if (connectionHandle != null)
			call.addHeader(new SOAPHeaderElement(null, "ConnectionHandle", connectionHandle));

		if (targetVolume != null)
			call.addHeader(new SOAPHeaderElement(null, "DelayFlush", delayFlush));

		return call;
	}

	public String getAuthId() {
		return authId;
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

	public String getConnectionHandle() {
		return connectionHandle;
	}

	public Boolean getDelayFlush() {
		return delayFlush;
	}

	public String getLocale() {
		return locale;
	}

	public String getTargetVolume() {
		return targetVolume;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public void setConnectionHandle(String connectionHandle) {
		this.connectionHandle = connectionHandle;
	}

	public void setDelayFlush(Boolean delayFlush) {
		this.delayFlush = delayFlush;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public void setTargetVolume(String targetVolume) {
		this.targetVolume = targetVolume;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}
