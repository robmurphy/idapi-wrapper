/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.FileCopier;
import com.actuate.aces.idapi.PermissionSetter;
import com.actuate.schemas.ArrayOfPermission;
import com.actuate.schemas.Permission;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.util.Vector;

public class IdapiFileCopier extends Task {
	private String serverUrl;
	private String authId;
	private String sourceFile;
	private String destinationFile;
	private String userPermissions;
	private String rolePermissions;
	private boolean replacePermissions = false;

	public void execute() throws BuildException {
		if (serverUrl == null || authId == null || sourceFile == null || destinationFile == null) {
			throw new BuildException("\r\n\r\nUSAGE ERROR:  IdapiFileCopier requires AuthId, Host, " + "\r\n              SourceFile, DestinationFile attributes." + "\r\nOptional attributes are UserPermissions, RolePermissions and ReplacePermissions." + "\r\nExample:" + "\r\n\t<IdapiFileCopier AuthId=\"${AuthId}\" " + "\r\n\t                 ServerUrl=\"${ServerUrl}\" " + "\r\n\t                 SourceFile=\"/Home/user/My Report.rptdesign\" " + "\r\n\t                 DestinationFile=\"/Public/My Report.rptdesign\" " + "\r\n\t                 UserPermissions=\"jdoe:VRE,tsmith:VRE\" " + "\r\n\t                 RolePermissions=\"Finance:VRE\" " + "\r\n\t                 ReplacePermissions=\"true\" />");
		}
		try {
			FileCopier fileCopier = new FileCopier(serverUrl, authId);
			fileCopier.copy(sourceFile, destinationFile);

			Vector<Permission> vPermissions = null;
			if (userPermissions != null) {
				vPermissions = new Vector<Permission>();
				if (userPermissions.indexOf(",") != -1) {
					String[] userPerms = userPermissions.split(",");
					for (int x = 0; x < userPerms.length; x++) {
						vPermissions.add(new Permission(null, userPerms[x].substring(0, userPerms[x].indexOf(":")), null, null, userPerms[x].substring(userPerms[x].indexOf(":") + 1)));
					}
				} else {
					vPermissions.add(new Permission(null, userPermissions.substring(0, userPermissions.indexOf(":")), null, null, userPermissions.substring(userPermissions.indexOf(":") + 1)));
				}
			}
			if (rolePermissions != null) {
				if (vPermissions == null)
					vPermissions = new Vector<Permission>();
				if (rolePermissions.indexOf(",") != -1) {
					String[] rolePerms = rolePermissions.split(",");
					for (int x = 0; x < rolePerms.length; x++) {
						vPermissions.add(new Permission(rolePerms[x].substring(0, rolePerms[x].indexOf(":")), null, null, null, rolePerms[x].substring(rolePerms[x].indexOf(":") + 1)));
					}
				} else {
					vPermissions.add(new Permission(rolePermissions.substring(0, rolePermissions.indexOf(":")), null, null, null, rolePermissions.substring(rolePermissions.indexOf(":") + 1)));
				}
			}
			if (vPermissions != null) {
				Permission[] permissions = new Permission[vPermissions.size()];
				ArrayOfPermission aop = new ArrayOfPermission(vPermissions.toArray(permissions));
				fileCopier.setPermissions(aop);
				PermissionSetter ps = new PermissionSetter(fileCopier);
				ps.setFilePermissions(destinationFile, replacePermissions);
			}
			fileCopier.reset();
		} catch (MalformedURLException e) {
			throw new BuildException(e);
		} catch (ServiceException e) {
			throw new BuildException(e);
		}
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public void setDestinationFile(String destinationFile) {
		this.destinationFile = destinationFile;
	}

	public void setUserPermissions(String userPermissions) {
		this.userPermissions = userPermissions;
	}

	public void setRolePermissions(String rolePermissions) {
		this.rolePermissions = rolePermissions;
	}

	public void setReplacePermissions(String replacePermissions) {
		if (replacePermissions.equalsIgnoreCase("true") || replacePermissions.equalsIgnoreCase("yes"))
			this.replacePermissions = true;
		else
			this.replacePermissions = false;
	}

}
