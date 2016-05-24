/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.AdminOperation;
import com.actuate.schemas.Administrate;
import com.actuate.schemas.DeleteUser;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * Created by rmurphy on 7/25/2014.
 */
public class UserRemover extends BaseController {
	public UserRemover(BaseController controller) {
		super(controller);
	}

	public UserRemover(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public UserRemover(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public UserRemover(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public boolean remove(String userName) {
		DeleteUser deleteUser = new DeleteUser();
		deleteUser.setName(userName);

		AdminOperation[] adminOperations = new AdminOperation[permissions == null ? 1 : 2];
		adminOperations[0] = new AdminOperation();
		adminOperations[0].setDeleteUser(deleteUser);
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
