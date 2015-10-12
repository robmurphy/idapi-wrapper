/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.FileCopier;
import com.actuate.aces.idapi.PermissionSetter;
import com.actuate.schemas.ArrayOfPermission;
import org.apache.tools.ant.BuildException;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;

public class IdapiFileCopier extends IdapiAntTask {
    private String serverUrl;
    private String authId;
    private String sourceFile;
    private String destinationFile;
    private boolean replacePermissions = false;

    public void execute() throws BuildException {
        if (serverUrl == null || authId == null || sourceFile == null || destinationFile == null) {
            throw new BuildException(getUsage());
        }
        try {
            FileCopier fileCopier = new FileCopier(serverUrl, authId);
            fileCopier.copy(sourceFile, destinationFile);

            ArrayOfPermission aop = getArrayOfPermissions();
            if (aop != null) {
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

    public void setReplacePermissions(String replacePermissions) {
        if (replacePermissions.equalsIgnoreCase("true") || replacePermissions.equalsIgnoreCase("yes"))
            this.replacePermissions = true;
        else
            this.replacePermissions = false;
    }

    private String getUsage(){
        return NL + NL +
                "USAGE ERROR:  IdapiFileCopier requires AuthId, Host, " + NL +
                "              SourceFile, DestinationFile attributes." + NL +
                "Optional attributes are UserPermissions, UserGroupPermissions and ReplacePermissions." + NL +
                "Example:" + NL +
                "\t<IdapiFileCopier AuthId=\"${AuthId}\" " + NL +
                "\t                 ServerUrl=\"${ServerUrl}\" " + NL +
                "\t                 SourceFile=\"/Home/user/My Report.rptdesign\" " + NL +
                "\t                 DestinationFile=\"/Public/My Report.rptdesign\" " + NL +
                "\t                 UserPermissions=\"jdoe:VRE,tsmith:VRE\" " + NL +
                "\t                 UserGroupPermissions=\"Finance:VRE\" " + NL +
                "\t                 ReplacePermissions=\"true\" />";
    }
}