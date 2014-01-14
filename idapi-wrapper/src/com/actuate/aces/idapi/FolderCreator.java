/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class FolderCreator extends BaseController {

	public FolderCreator(BaseController controller) {
		super(controller);
	}

	public FolderCreator(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public FolderCreator(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public FolderCreator(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public boolean createFolder(String name, String baseFolder) {
		if (baseFolder.endsWith("/") && baseFolder.length() > 1)
			baseFolder = baseFolder.substring(0, baseFolder.length() - 1);

		CreateFolder createFolder = new CreateFolder();
		createFolder.setFolderName(name);
		createFolder.setWorkingFolderName(baseFolder);

		AdminOperation[] adminOperations = new AdminOperation[permissions == null ? 1 : 2];
		adminOperations[0] = new AdminOperation();
		adminOperations[0].setCreateFolder(createFolder);

		if (permissions != null) {
			UpdateFileOperation updateFileOperation = new UpdateFileOperation();
			updateFileOperation.setGrantPermissions(permissions);
			UpdateFileOperationGroup updateFileOperationGroup = new UpdateFileOperationGroup();
			updateFileOperationGroup.setUpdateFileOperation(new UpdateFileOperation[]{updateFileOperation});
			UpdateFile updateFile = new UpdateFile();
			updateFile.setName(baseFolder + "/" + name);
			updateFile.setUpdateFileOperationGroup(updateFileOperationGroup);

			adminOperations[1] = new AdminOperation();
			adminOperations[1].setUpdateFile(updateFile);
		}

		Administrate administrate = new Administrate();
		administrate.setAdminOperation(adminOperations);

		try {
			acxControl.proxy.administrate(administrate);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
