/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.ArrayOfPermission;
import com.actuate.schemas.GetFileACL;
import com.actuate.schemas.GetFileACLResponse;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class PermissionGetter extends BaseController {

	public PermissionGetter(BaseController controller) {
		super(controller);
	}

	public PermissionGetter(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public PermissionGetter(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public PermissionGetter(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public ArrayOfPermission getPermissions(String fileName) {
		GetFileACL getFileACL = new GetFileACL();
		getFileACL.setFileName(fileName);

		GetFileACLResponse getFileACLResponse;
		try {
			getFileACLResponse = acxControl.proxy.getFileACL(getFileACL);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

		return getFileACLResponse.getACL();
	}
}
