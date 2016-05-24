/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.FolderCreator;
import com.actuate.schemas.ArrayOfPermission;
import org.apache.tools.ant.BuildException;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;

public class IdapiCreateFolder extends IdapiAntTask {
    private String serverUrl;
    private String authId;
    private String folderPath;

    public void execute() throws BuildException {
        if (serverUrl == null || authId == null || folderPath == null ) {
            throw new BuildException(getUsage());
        }
        try {
        	FolderCreator fc = new FolderCreator(serverUrl, authId);
            ArrayOfPermission aop = getArrayOfPermissions();
            if (aop != null) {
            	fc.setPermissions(aop);
            }
            fc.createFolder(folderPath, "/");
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

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    private String getUsage(){
        return NL + NL +
                "USAGE ERROR:  CreateFolder requires AuthId, ServerUrl, FolderPath " + NL +
                "Optional attributes are UserPermissions, UserGroupPermissions" + NL +
                "Example:" + NL +
                "\t<IdapiUploader AuthId=\"${AuthId}\" " + NL +
                "\t               ServerUrl=\"${ServerUrl}\" " + NL +
                "\t               FolderPath=\"/Resources/Data Objects" + NL +
                "\t               UserGroupPermissions=\"All:VE\"";
    }
}