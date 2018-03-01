/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.AdminOperation;
import com.actuate.schemas.Administrate;
import com.actuate.schemas.MoveFile;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class FileMover extends BaseController {

	public FileMover(BaseController controller) {
		super(controller);
	}

	public FileMover(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public FileMover(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public FileMover(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public boolean move(String source, String destination) {
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
			return move(sourceFolder, sourceFile, destination, null);
		else
			return move(sourceFolder, sourceFile, destinationFolder, destinationFile);
	}

	public boolean move(String sourceFolder, String sourceFile, String destinationFolder, String destinationFile) {
		MoveFile moveFile = new MoveFile();
		moveFile.setWorkingFolderName(sourceFolder);
		moveFile.setName(sourceFile);

		if (destinationFile == null)
			moveFile.setTarget(destinationFolder + "/" + sourceFile);
		else
			moveFile.setTarget(destinationFolder + "/" + destinationFile);

		AdminOperation adminOperation = new AdminOperation();
		adminOperation.setMoveFile(moveFile);
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
