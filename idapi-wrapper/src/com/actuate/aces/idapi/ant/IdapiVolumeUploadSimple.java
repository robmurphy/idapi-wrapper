/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.actions.VolumeUploadSimple;
import com.actuate.schemas.ArrayOfPermission;
import org.apache.tools.ant.BuildException;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;

public class IdapiVolumeUploadSimple extends BasePermissionTask {
	private String serverUrl;
	private String authId;
	private String sourcePath;
	private String targetPath;
	private boolean deleteFirst = false;
	private boolean recursive = true;

	public void execute() throws BuildException {
		if (serverUrl == null || authId == null || sourcePath == null || targetPath == null) {
			throw new BuildException("\r\n\r\nUSAGE ERROR:  IdapiVolumeUploadSimple requires AuthId, ServerUrl, " + "\r\n              SourcePath, TargetPath attributes." + "\r\nOptional attributes are UserPermissions, UserGroupPermissions, DeleteFirst, and Recursive." + "\r\nExample:" + "\r\n\t<IdapiVolumeUploadSimple AuthId=\"${AuthId}\" " + "\r\n\t                         ServerUrl=\"${ServerUrl}\" " + "\r\n\t                         SourcePath=\"C:/my directory\" " + "\r\n\t                         TargetPath=\"/Public\" " + "\r\n\t                         UserPermissions=\"/Public\" " + "\r\n\t                         RolePermissions=\"/Public\" " + "\r\n\t                         DeleteFirst=\"true\" " + "\r\n\t                         Recursive=\"false\" />");
		}
		try {
			VolumeUploadSimple vus = new VolumeUploadSimple(serverUrl, authId);

			ArrayOfPermission aop = getArrayOfPermissions();
			if (aop != null) {
				vus.setPermissions(aop);
			}
			vus.upload(sourcePath, targetPath, deleteFirst, recursive);
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

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public void setDeleteFirst(String deleteFirst) {
		if (deleteFirst.equalsIgnoreCase("true") || deleteFirst.equalsIgnoreCase("yes"))
			this.deleteFirst = true;
		else
			this.deleteFirst = false;
	}

	public void setRecursive(String recursive) {
		if (recursive.equalsIgnoreCase("true") || recursive.equalsIgnoreCase("yes"))
			this.recursive = true;
		else
			this.recursive = false;
	}
}
