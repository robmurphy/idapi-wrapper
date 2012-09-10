package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class PermissionSetter extends BaseController {

	public PermissionSetter(BaseController controller) {
		super(controller);
	}

	public PermissionSetter(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public PermissionSetter(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public PermissionSetter(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public boolean setFilePermissions(String file) {
		return setFilePermissions(new String[]{file}, false);
	}

	public boolean setFilePermissions(String file, boolean replace) {
		return setFilePermissions(new String[]{file}, replace);
	}

	public boolean setFilePermission(String[] fileList) {
		return setFilePermissions(fileList, false);
	}

	public boolean setFilePermissions(String[] fileList, boolean replace) {

		UpdateFileOperationGroup updateFileOperationGroup = new UpdateFileOperationGroup();
		UpdateFileOperation updateFileOperation = new UpdateFileOperation();
		if (replace)
			updateFileOperation.setSetPermissions(permissions);
		else
			updateFileOperation.setGrantPermissions(permissions);
		updateFileOperationGroup.setUpdateFileOperation(new UpdateFileOperation[]{updateFileOperation});

		UpdateFile updateFile = new UpdateFile();
		updateFile.setNameList(new ArrayOfString(fileList));
		updateFile.setUpdateFileOperationGroup(updateFileOperationGroup);

		AdminOperation adminOperation = new AdminOperation();
		adminOperation.setUpdateFile(updateFile);

		Administrate administrate = new Administrate(new AdminOperation[]{adminOperation});

		try {
			acxControl.proxy.administrate(administrate);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean setRecursivePermissions(String folder) {
		return setRecursivePermissions(folder, false);
	}

	public boolean setRecursivePermissions(String folder, boolean replace) {
		AdminOperation[] adminOperations = new AdminOperation[2];

		UpdateFileOperationGroup updateFileOperationGroup = new UpdateFileOperationGroup();
		UpdateFileOperation updateFileOperation = new UpdateFileOperation();
		if (replace)
			updateFileOperation.setSetPermissions(permissions);
		else
			updateFileOperation.setGrantPermissions(permissions);
		updateFileOperationGroup.setUpdateFileOperation(new UpdateFileOperation[]{updateFileOperation});

		UpdateFile updateFile = new UpdateFile();
		updateFile.setName(folder);
		updateFile.setRecursive(true);
		updateFile.setUpdateFileOperationGroup(updateFileOperationGroup);

		adminOperations[0] = new AdminOperation();
		adminOperations[0].setUpdateFile(updateFile);


		updateFileOperationGroup = new UpdateFileOperationGroup();
		updateFileOperation = new UpdateFileOperation();
		if (replace)
			updateFileOperation.setSetPermissions(permissions);
		else
			updateFileOperation.setGrantPermissions(permissions);
		updateFileOperationGroup.setUpdateFileOperation(new UpdateFileOperation[]{updateFileOperation});

		updateFile = new UpdateFile();
		updateFile.setWorkingFolderName(folder);
		updateFile.setRecursive(true);
		updateFile.setUpdateFileOperationGroup(updateFileOperationGroup);

		adminOperations[1] = new AdminOperation();
		adminOperations[1].setUpdateFile(updateFile);

		Administrate administrate = new Administrate(adminOperations);

		try {
			acxControl.proxy.administrate(administrate);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
}
