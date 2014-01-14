/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.Authenticator;
import com.actuate.aces.idapi.control.ActuateException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;

public class IdapiAuthenticate extends Task {
	private String serverUrl;
	private String userName;
	private String password;
	private String volume;
	private String authIdProperty;

	public void execute() throws BuildException {
		if (serverUrl == null || userName == null || password == null || volume == null || authIdProperty == null) {
			throw new BuildException("\r\n\r\nUSAGE ERROR:  IdapiAuthenticate requires ServerUrl, UserName, Password, and Volume attributes" + "\r\nExample:" + "\r\n\t<IdapiAuthenticate ServerUrl=\"${ServerUrl}\" " + "\r\n\t                   UserName=\"${UserName}\" " + "\r\n\t                   Password=\"${Password}\" " + "\r\n\t                   Volume=\"myVolume\" />");
		}
		try {
			Authenticator authenticator = new Authenticator(serverUrl, userName, password, volume);
			getProject().setNewProperty(authIdProperty, authenticator.getAuthId());
		} catch (MalformedURLException e) {
			throw new BuildException(e);
		} catch (ServiceException e) {
			throw new BuildException(e);
		} catch (ActuateException e) {
			throw new BuildException(e);
		}
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public void setAuthIdProperty(String authIdProperty) {
		this.authIdProperty = authIdProperty;
	}
}
