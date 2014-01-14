/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.actions;

import com.actuate.aces.idapi.BaseController;
import com.actuate.aces.idapi.FileRemover;
import com.actuate.aces.idapi.Uploader;
import com.actuate.aces.idapi.control.ActuateException;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class VolumeUploadSimple extends BaseController {
	public VolumeUploadSimple(BaseController controller) {
		super(controller);
	}

	public VolumeUploadSimple(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public VolumeUploadSimple(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public VolumeUploadSimple(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public boolean upload(String sourcePath, String targetPath) throws IOException {
		return upload(sourcePath, targetPath, false, true);
	}

	public boolean upload(String sourcePath, String targetPath, boolean deleteFirst) throws IOException {
		return upload(sourcePath, targetPath, deleteFirst, true);
	}

	public boolean upload(String sourcePath, String targetPath, boolean deleteFirst, boolean recursive) throws IOException {
		boolean retVal = true;
		if (deleteFirst)
			retVal = new FileRemover(this).delete(targetPath);

		return uploadFile(new File(sourcePath), targetPath, recursive) && retVal;
	}

	private boolean uploadFile(File sourceFile, String targetPath, boolean recursive) throws IOException {
		if (sourceFile.isDirectory()) {
			boolean retVal = true;
			File[] files = sourceFile.listFiles();
			for (File file : files) {
				String sourceFileName = sourceFile.getAbsolutePath() + File.separatorChar + file.getName();
				if (targetPath.equals("/"))
					targetPath = "";
				String targetFileName = targetPath + "/" + file.getName();
				if (file.isDirectory() && recursive)
					retVal = uploadFile(new File(sourceFileName), targetFileName, recursive) && retVal;
				else if (!file.isDirectory())
					retVal = uploadFile(new File(sourceFileName), targetFileName, recursive) && retVal;
			}
			return retVal;
		} else {
			Uploader uploader = new Uploader(this);
			String objId = uploader.uploadFileAsStream(sourceFile, targetPath);
			return !(objId == null || objId.equals(""));
		}
	}
}
