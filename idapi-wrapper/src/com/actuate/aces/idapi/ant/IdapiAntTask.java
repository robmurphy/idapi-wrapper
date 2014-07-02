/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import java.util.ArrayList;
import org.apache.tools.ant.Task;
import com.actuate.schemas.ArrayOfPermission;
import com.actuate.schemas.Permission;

/**
 * Base task class for several IDAPI tasks that sets user/usergroup permissions
 */
public abstract class IdapiAntTask extends Task {
    protected static final String NL = System.lineSeparator();
	protected ArrayList<Permission> permissions = new ArrayList<Permission>();
	
	public void setUserPermissions(String userPermissions) {
		if (userPermissions != null) {
			String[] perms = userPermissions.split(",");
			for (int x = 0; x < perms.length; x++) {
				int colon = perms[x].indexOf(":");
				if (colon > 0) {
					String userName = perms[x].substring(0, colon);
					String rights = perms[x].substring(colon+1);
					Permission p = new Permission();
					p.setUserName(userName);
					p.setAccessRight(rights);
					permissions.add(p);
				}
			}
		}
	}

	/**
	 *  @deprecated Use setUserGroupPermission instead; kept for backward compatibility
	 */
	public void setRolePermissions(String rolePermissions) {
		setUserGroupPermissions(rolePermissions);
	}

	public void setUserGroupPermissions(String userGroupPermissions) {
		if (userGroupPermissions != null) {
			String[] perms = userGroupPermissions.split(",");
			for (int x = 0; x < perms.length; x++) {
				int colon = perms[x].indexOf(":");
				if (colon > 0) {
					String groupName = perms[x].substring(0, colon);
					String rights = perms[x].substring(colon+1);
					Permission p = new Permission();
					p.setUserGroupName(groupName);
					p.setAccessRight(rights);
					permissions.add(p);
				}
			}
		}
	}
	
	/**
	 * @return Permission array. NULL if no user or user group permission is set for task
	 */
	protected ArrayOfPermission getArrayOfPermissions() {
		if (permissions.size() == 0 )
			return null;
		else{
			Permission[] perms = permissions.toArray(new Permission[permissions.size()]);
			ArrayOfPermission aop = new ArrayOfPermission(perms);
			return aop;
		}
	}

}
