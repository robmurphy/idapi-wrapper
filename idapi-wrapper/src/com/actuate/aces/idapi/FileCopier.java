package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.AdminOperation;
import com.actuate.schemas.Administrate;
import com.actuate.schemas.CopyFile;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class FileCopier extends BaseController {

	public FileCopier(BaseController controller) {
		super(controller);
	}

	public FileCopier(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public FileCopier(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public FileCopier(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public boolean copy(String source, String destination) {
		String sourceFolder;
		if (source.lastIndexOf("/") == 0)
			sourceFolder = "/";
		else
			sourceFolder = source.substring(0, source.lastIndexOf("/"));
		String sourceFile = source.substring(source.lastIndexOf("/") + 1);

		String destinationFolder;
		if (destination.lastIndexOf("/") == 0)
			destinationFolder = "/";
		else
			destinationFolder = destination.substring(0, destination.lastIndexOf("/"));
		String destinationFile = destination.substring(destination.lastIndexOf("/") + 1);

		if (destinationFile.indexOf(".") < 0)
			return copy(sourceFolder, sourceFile, destination, null);
		else
			return copy(sourceFolder, sourceFile, destinationFolder, destinationFile);
	}

	public boolean copy(String sourceFolder, String sourceFile, String destinationFolder, String destinationFile) {
		CopyFile copyFile = new CopyFile();
		copyFile.setWorkingFolderName(sourceFolder);
		copyFile.setName(sourceFile);

		if (destinationFile == null)
			copyFile.setTarget(destinationFolder + "/" + sourceFile);
		else
			copyFile.setTarget(destinationFolder + "/" + destinationFile);

		AdminOperation adminOperation = new AdminOperation();
		adminOperation.setCopyFile(copyFile);
		AdminOperation[] adminOperations = {adminOperation};
		Administrate administrate = new Administrate(adminOperations);

		try {
			acxControl.proxy.administrate(administrate);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}
}
