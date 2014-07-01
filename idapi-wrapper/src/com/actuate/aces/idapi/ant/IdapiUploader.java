/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.PermissionSetter;
import com.actuate.aces.idapi.Uploader;
import com.actuate.schemas.ArrayOfPermission;
import org.apache.tools.ant.BuildException;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;

public class IdapiUploader extends BasePermissionTask {
	private String serverUrl;
	private String authId;
	private String sourceFile;
	private String destinationFile;
	private boolean replaceExisting = false;

	public void execute() throws BuildException {
		if (serverUrl == null || authId == null || sourceFile == null || destinationFile == null) {
			throw new BuildException("\r\n\r\nUSAGE ERROR:  IdapiUploader requires AuthId, ServerUrl, " + "\r\n              SourceFile, DestinationFile attributes." + "\r\nOptional attributes are UserPermissions, UserGroupPermissions and ReplaceExisting." + "\r\nExample:" + "\r\n\t<IdapiUploader AuthId=\"${AuthId}\" " + "\r\n\t               ServerUrl=\"${ServerUrl}\" " + "\r\n\t               SourceFile=\"C:/my directory/My Report.rptdesign\" " + "\r\n\t               DestinationFile=\"/Public/My Report.rptdesign\" " + "\r\n\t               UserPermissions=\"jdoe:VRE,tsmith:VRE\" " + "\r\n\t               RolePermissions=\"Finance:VRE\" " + "\r\n\t               ReplaceExisting=\"false\" />");
		}
		try {
			Uploader uploader = new Uploader(serverUrl, authId);
			uploader.uploadFile(sourceFile, destinationFile, replaceExisting);

			ArrayOfPermission aop = getArrayOfPermissions();
			if (aop != null) {
				uploader.setPermissions(aop);
				PermissionSetter ps = new PermissionSetter(uploader);
				ps.setFilePermissions(destinationFile, true);
			}
		} catch (MalformedURLException e) {
			throw new BuildException(e);
		} catch (ServiceException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public void setDestinationFile(String destinationFile) {
		this.destinationFile = destinationFile;
	}

	public void setReplaceExisting(boolean replaceExisting) {
		this.replaceExisting = replaceExisting;
	}
}
