/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.AdminOperation;
import com.actuate.schemas.Administrate;
import com.actuate.schemas.CreateUserGroup;
import com.actuate.schemas.UserGroup;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * Created by rmurphy on 7/25/2014.
 */
public class UserGroupCreator extends BaseController {


	public UserGroupCreator(BaseController controller) {
		super(controller);
	}

	public UserGroupCreator(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public UserGroupCreator(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public UserGroupCreator(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public boolean create(String groupName) {
		UserGroup userGroup = new UserGroup();
		userGroup.setName(groupName);
		CreateUserGroup createUserGroup = new CreateUserGroup();
		createUserGroup.setUserGroup(userGroup);
		AdminOperation[] adminOperations = new AdminOperation[permissions == null ? 1 : 2];
		adminOperations[0] = new AdminOperation();
		adminOperations[0].setCreateUserGroup(createUserGroup);

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