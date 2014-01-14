/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.aces.idapi.control.AcxControl;
import com.actuate.schemas.ArrayOfPermission;
import com.actuate.schemas.NameValuePair;
import com.actuate.schemas.Permission;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

	protected AcxControl acxControl;
	protected ArrayOfPermission permissions;

	public BaseController(BaseController controller) {
		this.acxControl = controller.getAcxControl();
		this.permissions = controller.getPermissions();
	}

	public BaseController(String host) throws MalformedURLException, ServiceException {
		acxControl = new AcxControl(host);
	}

	public BaseController(String host, String authenticationId) throws MalformedURLException, ServiceException {
		acxControl = new AcxControl(host);
		acxControl.setAuthenticationId(authenticationId);
	}

	public BaseController(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		acxControl = new AcxControl(host);
		if (!acxControl.login(username, password, volume))
			throw new ActuateException("Authentication to Actuate Server Failed!");
	}

	public BaseController(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		acxControl = new AcxControl(host);
		acxControl.setExtendedCredentials(extendedCredentials);
		if (!acxControl.login(username, password, volume))
			throw new ActuateException("Authentication to Actuate Server Failed!");
	}

	public void systemLogin(String systemPassword) throws ActuateException {
		if (!acxControl.systemLogin(systemPassword))
			throw new ActuateException("Authentication to Actuate Server System Failed!");
	}

	public String getAuthenticationId() {
		return acxControl.getAuthenticationId();
	}

	public String getConnectionHandle() {
		return acxControl.getConnectionHandle();
	}

	public void setConnectionHandle(String connectionHandle) {
		acxControl.setConnectionHandle(connectionHandle);
	}

	public AcxControl getAcxControl() {
		return acxControl;
	}

	public ArrayOfPermission getPermissions() {
		return permissions;
	}

	public void reset() {
		acxControl.reset();
	}

	public String getNamespace() {
		return acxControl.getNamespace();
	}

	public NameValuePair[] getNameValuePairsFromMap(HashMap<String, String> newNVPairs, HashMap<String, String> baseNVPairs) {
		if (baseNVPairs == null)
			baseNVPairs = new HashMap<String, String>();

		if (newNVPairs != null)
			baseNVPairs.putAll(newNVPairs);

		int size = baseNVPairs.size();
		int ind = 0;
		NameValuePair[] nvpair = new NameValuePair[size];
		for (Map.Entry<String, String> entry : baseNVPairs.entrySet()) {
			nvpair[ind] = new NameValuePair();
			nvpair[ind].setName(entry.getKey());
			nvpair[ind].setValue(entry.getValue());
			ind++;
		}

		return nvpair;
	}

	public void setPermissions(ArrayOfPermission permissions) {
		this.permissions = permissions;
	}

	public void addPermission(String user, String role, String accessRight) {
		Permission permission = new Permission();
		if (user != null)
			permission.setUserName(user);
		if (role != null && user == null)
			permission.setRoleName(role);
		permission.setAccessRight(accessRight);

		if (permissions == null) {
			permissions = new ArrayOfPermission();
			permissions.setPermission(new Permission[]{permission});
		} else {
			Permission[] newPermissions = Arrays.copyOf(permissions.getPermission(), permissions.getPermission().length + 1);
			newPermissions[newPermissions.length - 1] = permission;
			permissions.setPermission(newPermissions);
		}
	}
}
