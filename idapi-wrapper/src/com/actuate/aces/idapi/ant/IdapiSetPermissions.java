/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.PermissionSetter;
import com.actuate.schemas.ArrayOfPermission;
import org.apache.tools.ant.BuildException;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;

public class IdapiSetPermissions extends IdapiAntTask {
    private String serverUrl;
    private String authId;
    private String path;
    private boolean recursive = false;
    private boolean replace = false;

    public void execute() throws BuildException {
        if (serverUrl == null || authId == null || path == null ) {
            throw new BuildException(getUsage());
        }
        
        try {
        	PermissionSetter ps = new PermissionSetter(serverUrl, authId);
            ArrayOfPermission aop = getArrayOfPermissions();
            if (aop != null) {
            	ps.setPermissions(aop);
            } else 
                throw new BuildException(getUsage());
            
            if (recursive)
            	ps.setRecursivePermissions(path, replace);
            else
            	ps.setFilePermissions(path, replace);
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

    public void setPath(String path) {
        this.path = path;
    }

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public void setReplace(boolean replace) {
		this.replace = replace;
	}

	private String getUsage(){
        return NL + NL +
                "USAGE ERROR:  SetPermissions requires AuthId, ServerUrl, Path " + NL +
                "Optional attributes are UserPermissions, UserGroupPermissions, Recursive and Replace" + NL +
                "Example:" + NL +
                "\t<SetPermissions AuthId=\"${AuthId}\" " + NL +
                "\t               ServerUrl=\"${ServerUrl}\" " + NL +
                "\t               Path=\"/Resources/Data Objects" + NL +
                "\t               UserGroupPermissions=\"All:VRE\"" + NL + 
                "\t               Recursive=\"true\"" + NL +
                "\t               Replace=\"true\"";
    }
}