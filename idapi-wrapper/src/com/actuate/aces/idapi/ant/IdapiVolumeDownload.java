/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.actions.VolumeDownload;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;

public class IdapiVolumeDownload extends Task {
	private String serverUrl;
	private String authId;
	private String sourcePath;
	private String targetPath;
	private boolean deleteFirst = false;

	public void execute() throws BuildException {
		if (serverUrl == null || authId == null || sourcePath == null || targetPath == null) {
			throw new BuildException("\r\n\r\nUSAGE ERROR:  IdapiVolumeDownload requires AuthId, ServerUrl, " + "\r\n              SourcePath, TargetPath attributes." + "\r\nOptional attribute is DeleteFirst." + "\r\nExample:" + "\r\n\t<IdapiVolumeDownload AuthId=\"${AuthId}\" " + "\r\n\t                     ServerUrl=\"${ServerUrl}\" " + "\r\n\t                     SourcePath=\"/Public\" " + "\r\n\t                     TargetPath=\"C:/MyEnycDownloadDir\" " + "\r\n\t                     DeleteFirst=\"true\" />");
		}
		try {
			VolumeDownload download = new VolumeDownload(serverUrl, authId);
			download.download(sourcePath, targetPath, deleteFirst);
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

	public void setDeleteFirst(boolean deleteFirst) {
		this.deleteFirst = deleteFirst;
	}
}
