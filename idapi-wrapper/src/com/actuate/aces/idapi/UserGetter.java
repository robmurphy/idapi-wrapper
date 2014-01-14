/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.ArrayOfString;
import com.actuate.schemas.SelectUsers;
import com.actuate.schemas.SelectUsersResponse;
import com.actuate.schemas.User;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA.
 * User: rmurphy
 * Date: 11/6/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserGetter extends BaseController {
	public UserGetter(BaseController controller) {
		super(controller);
	}

	public UserGetter(String host) throws MalformedURLException, ServiceException {
		super(host);
	}

	public UserGetter(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public UserGetter(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public UserGetter(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public User getUser(String username) {
		SelectUsers selectUsers = new SelectUsers();
		selectUsers.setName(username);
		selectUsers.setResultDef(getResultDef());
		SelectUsersResponse selectUsersResponse;
		try {
			selectUsersResponse = acxControl.proxy.selectUsers(selectUsers);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		return selectUsersResponse.getUsers().getUser()[0];
	}

	private ArrayOfString getResultDef() {
		return new ArrayOfString(new String[]{"Id", "Name", "HomeFolder", "Description", "EmailAddress"});
	}
}
