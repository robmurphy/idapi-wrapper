/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.DownloadFile;
import org.apache.axis.attachments.AttachmentPart;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Iterator;

public class Downloader extends BaseController {

	public Downloader(BaseController controller) {
		super(controller);
	}

	public Downloader(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}
	
	public Downloader(String host, String authenticationId, String volume) throws MalformedURLException, ServiceException {
		super(host, authenticationId, volume);
	}

	public Downloader(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public Downloader(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public InputStream downloadToStream(String sourceFile) {
		DownloadFile downloadFile = new DownloadFile();
		downloadFile.setFileName(sourceFile);
		downloadFile.setDownloadEmbedded(false);

		try {
			acxControl.proxy.downloadFile(downloadFile);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

		try {
			Iterator iter = acxControl.actuateAPI.getCall().getMessageContext().getResponseMessage().getAttachments();
			while (iter.hasNext()) {
				AttachmentPart attachmentPart = (AttachmentPart) iter.next();
				return attachmentPart.getDataHandler().getInputStream();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		}

		return null;
	}

	public File downloadToFile(String sourceFile, String destinationFile) throws IOException {
		InputStream inputStream = downloadToStream(sourceFile);

		if (inputStream == null)
			return null;

		File file = new File(destinationFile);
		FileOutputStream fileOutputStream = new FileOutputStream(file, false);

		byte[] buf = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buf)) > 0) {
			fileOutputStream.write(buf, 0, len);
		}

		inputStream.close();
		fileOutputStream.close();
		return file;
	}
}
