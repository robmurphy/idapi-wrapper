/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.actions.VolumeUploadSimple;
import com.actuate.schemas.ArrayOfPermission;
import com.actuate.schemas.Permission;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

public class IdapiVolumeUploadSimple extends Task {
	private String serverUrl;
	private String authId;
	private String sourcePath;
	private String targetPath;
	private String userPermissions;
	private String rolePermissions;
	private boolean deleteFirst = false;
	private boolean recursive = true;

	public void execute() throws BuildException {
		if (serverUrl == null || authId == null || sourcePath == null || targetPath == null) {
			throw new BuildException("\r\n\r\nUSAGE ERROR:  IdapiVolumeUploadSimple requires AuthId, ServerUrl, " + "\r\n              SourcePath, TargetPath attributes." + "\r\nOptional attributes are UserPermissions, RolePermissions, DeleteFirst, and Recursive." + "\r\nExample:" + "\r\n\t<IdapiVolumeUploadSimple AuthId=\"${AuthId}\" " + "\r\n\t                         ServerUrl=\"${ServerUrl}\" " + "\r\n\t                         SourcePath=\"C:/my directory\" " + "\r\n\t                         TargetPath=\"/Public\" " + "\r\n\t                         UserPermissions=\"/Public\" " + "\r\n\t                         RolePermissions=\"/Public\" " + "\r\n\t                         DeleteFirst=\"true\" " + "\r\n\t                         Recursive=\"false\" />");
		}
		try {
			VolumeUploadSimple vus = new VolumeUploadSimple(serverUrl, authId);

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
				vus.setPermissions(aop);
			}
			vus.upload(sourcePath, targetPath, deleteFirst, recursive);
		} catch (MalformedURLException e) {
			throw new BuildException(e);
		} catch (ServiceException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public void setUserPermissions(String userPermissions) {
		this.userPermissions = userPermissions;
	}

	public void setRolePermissions(String rolePermissions) {
		this.rolePermissions = rolePermissions;
	}

	public void setDeleteFirst(String deleteFirst) {
		if (deleteFirst.equalsIgnoreCase("true") || deleteFirst.equalsIgnoreCase("yes"))
			this.deleteFirst = true;
		else
			this.deleteFirst = false;
	}

	public void setRecursive(String recursive) {
		if (recursive.equalsIgnoreCase("true") || recursive.equalsIgnoreCase("yes"))
			this.recursive = true;
		else
			this.recursive = false;
	}
}
