/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.actions.VolumeMigrateSimple;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;

public class IdapiVolumeMigrateSimple extends Task {
	private String sourceAuthId;
	private String sourceServerUrl;
	private String sourcePath;
	private String targetAuthId;
	private String targetServerUrl;
	private String targetPath;
	private boolean copyPermissions = true;
	private boolean deleteFirst = false;

	public void execute() throws BuildException {
		if (sourceAuthId == null || sourceServerUrl == null || sourcePath == null ||
				targetAuthId == null || targetServerUrl == null || targetPath == null) {
			throw new BuildException("\r\n\r\nUSAGE ERROR:  IdapiVolumeMigrateSimple requires SourceAuthId, SourceServerUrl, SourcePath, " + "\r\n              TargetAuthId, TargetHost, and TargetPath." + "\r\nOptional attributes are CopyPermissions and DeleteFirst." + "\r\nExample:" + "\r\n\t<IdapiVolumeMigrateSimple SourceAuthId=\"${SrcAuthId}\" " + "\r\n\t                          SourceServerUrl=\"${SrcServerUrl}\" " + "\r\n\t                          SourcePath=\"/Public\" " + "\r\n\t                          TargetAuthId=\"${TargetAuthId}\" " + "\r\n\t                          TargetServerUrl=\"${TargetServerUrl}\" " + "\r\n\t                          TargetPath=\"/Public\" " + "\r\n\t                          CopyPermissions=\"true\" " + "\r\n\t                          DeleteFirst=\"false\" />");
		}
		try {
			VolumeMigrateSimple vms = new VolumeMigrateSimple(sourceServerUrl, sourceAuthId, targetServerUrl, targetAuthId, copyPermissions);
			vms.migrate(sourcePath, targetPath, copyPermissions, deleteFirst);
		} catch (MalformedURLException e) {
			throw new BuildException(e);
		} catch (ServiceException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	public void setSourceAuthId(String sourceAuthId) {
		this.sourceAuthId = sourceAuthId;
	}

	public void setSourceServerUrl(String sourceServerUrl) {
		this.sourceServerUrl = sourceServerUrl;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public void setTargetAuthId(String targetAuthId) {
		this.targetAuthId = targetAuthId;
	}

	public void setTargetServerUrl(String targetServerUrl) {
		this.targetServerUrl = targetServerUrl;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public void setCopyPermissions(boolean copyPermissions) {
		this.copyPermissions = copyPermissions;
	}

	public void setDeleteFirst(boolean deleteFirst) {
		this.deleteFirst = deleteFirst;
	}
}
