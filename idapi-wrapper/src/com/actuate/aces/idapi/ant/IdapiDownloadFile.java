/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.Downloader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;

public class IdapiDownloadFile extends Task {

	private String serverUrl;
	private String authId;
	private String sourceFile;
	private String destinationFile;

	public void execute() throws BuildException {
		if (serverUrl == null || authId == null || sourceFile == null || destinationFile == null) {
			throw new BuildException("\r\n\r\nUSAGE ERROR:  IdapiDownloadFile required AuthId, ServerUrl, " + "\r\n              SourceFile, DestinationFile attributes." + "\r\nExample:" + "\r\n\t<IdapiDownloadFile AuthId=\"${AuthId}\" " + "\r\n\t                   ServerUrl=\"${ServerUrl}\" " + "\r\n\t                   SourceFile=\"/Home/user/My Report.rptdesign\" " + "\r\n\t                   DestinationFile=\"C:/MyFolder/My Report.rptdesign\" />");
		}
		Downloader downloader;
		try {
			downloader = new Downloader(serverUrl, authId);
			downloader.downloadToFile(sourceFile, destinationFile);
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
}
