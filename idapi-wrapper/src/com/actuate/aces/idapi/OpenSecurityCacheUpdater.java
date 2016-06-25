package com.actuate.aces.idapi;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.AdminOperation;
import com.actuate.schemas.Administrate;
import com.actuate.schemas.ArrayOfString;
import com.actuate.schemas.UpdateOpenSecurityCache;

public class OpenSecurityCacheUpdater extends BaseController {

	private String[] users, userGroups;
	
	public OpenSecurityCacheUpdater(BaseController controller) {
		super(controller);
	}

	public OpenSecurityCacheUpdater(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}
	
	public OpenSecurityCacheUpdater(String host, String authenticationId, String volume) throws MalformedURLException, ServiceException {
		super(host, authenticationId, volume);
	}

	public OpenSecurityCacheUpdater(String host, String username, String password, String volume) throws ServiceException, MalformedURLException, ActuateException {
		super(host, username, password, volume);
	}

	public OpenSecurityCacheUpdater(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	/**
	 * Invalidate cached user properties for specified users and/or userGroups
	 * @param userNames Names of users to invalidate;
	 * @param roleNames Names of user groups to invalidate
	 * @throws RemoteException 
	 */
	public void invalidateCachedUsers(String[] userNames, String [] userGroupNames) throws RemoteException {
		this.users = userNames;
		this.userGroups = userGroupNames;
		doInvalidate();
	}
	
	/**
	 * Invalidate entire cache
	 * @throws RemoteException 
	 */
	public void invalidateCache() throws RemoteException {
		doInvalidate();
	}
	
	private void doInvalidate() throws RemoteException {
		
		UpdateOpenSecurityCache op = new UpdateOpenSecurityCache();
		if (users != null && users.length > 0)
			op.setUserNameList(new ArrayOfString(users));
		if (userGroups != null && userGroups.length > 0) 
			op.setUserGroupList(new ArrayOfString(userGroups));
		
		AdminOperation adminOperation = new AdminOperation();
		adminOperation.setUpdateOpenSecurityCache(op);
		
		Administrate administrate = new Administrate();
		administrate.setAdminOperation(new AdminOperation[]{adminOperation});

		acxControl.proxy.administrate(administrate);
	}
}
