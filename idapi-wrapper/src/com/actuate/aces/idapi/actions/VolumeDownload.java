package com.actuate.aces.idapi.actions;

import com.actuate.aces.idapi.BaseController;
import com.actuate.aces.idapi.Downloader;
import com.actuate.aces.idapi.FileLister;
import com.actuate.aces.idapi.control.ActuateException;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class VolumeDownload extends BaseController {
	public VolumeDownload(BaseController controller) {
		super(controller);
	}

	public VolumeDownload(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public VolumeDownload(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public VolumeDownload(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public boolean download(String sourcePath, String targetPath) throws IOException {
		return download(sourcePath, targetPath, false);
	}

	public boolean download(String iServerPath, String localSystemPath, boolean deleteFirst) throws IOException {
		File targetFilePath = new File(localSystemPath);

		boolean retVal = !deleteFirst || deleteDirectory(targetFilePath);

		ArrayList<com.actuate.schemas.File> files = new ArrayList<com.actuate.schemas.File>();

		return downloadPath(iServerPath, targetFilePath) && retVal;
	}

	private boolean downloadPath(String iServerPath, File localSystemPath) throws IOException {
		boolean retVal = localSystemPath.mkdirs();

		FileLister fileLister = new FileLister(this);
		ArrayList<com.actuate.schemas.File> files = fileLister.getFileList(iServerPath, "*", true, false);

		for (com.actuate.schemas.File file : files) {
			String targetFileName = localSystemPath.getAbsolutePath() + "/" + file.getName();
			String sourceFileName;
			if (iServerPath.equals("/"))
				sourceFileName = "/" + file.getName();
			else
				sourceFileName = iServerPath + "/" + file.getName();

			if (file.getFileType().equalsIgnoreCase("directory")) {
				retVal = downloadPath(sourceFileName, new File(targetFileName)) && retVal;
			} else {
				Downloader downloader = new Downloader(this);
				retVal = (downloader.downloadToFile(sourceFileName, targetFileName) != null) && retVal;
				reset();
			}
		}
		return retVal;
	}

	private boolean deleteDirectory(File path) {
		if (path.exists()) {
			if (path.isDirectory()) {
				boolean retVal = true;
				File[] files = path.listFiles();
				for (File file : files) {
					retVal = deleteDirectory(file) && retVal;
				}
				return path.delete() && retVal;
			} else {
				return path.delete();
			}
		}
		return true;
	}
}
