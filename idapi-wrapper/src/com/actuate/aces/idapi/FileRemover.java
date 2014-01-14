/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.AdminOperation;
import com.actuate.schemas.Administrate;
import com.actuate.schemas.DeleteFile;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class FileRemover extends BaseController {

	public FileRemover(BaseController controller) {
		super(controller);
	}

	public FileRemover(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public FileRemover(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public FileRemover(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public boolean delete(String name) {
		DeleteFile deleteFile = new DeleteFile();
		if (name.equals("/"))
			deleteFile.setWorkingFolderName(name);
		else
			deleteFile.setName(name);
		deleteFile.setRecursive(true);

		AdminOperation adminOperation = new AdminOperation();
		adminOperation.setDeleteFile(deleteFile);

		Administrate administrate = new Administrate();
		administrate.setAdminOperation(new AdminOperation[]{adminOperation});

		try {
			acxControl.proxy.administrate(administrate);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
