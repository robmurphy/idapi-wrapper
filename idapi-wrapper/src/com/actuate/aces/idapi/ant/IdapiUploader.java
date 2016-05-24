/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.PermissionSetter;
import com.actuate.aces.idapi.Uploader;
import com.actuate.schemas.ArrayOfPermission;
import org.apache.tools.ant.BuildException;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;

public class IdapiUploader extends IdapiAntTask {
    private String serverUrl;
    private String authId;
    private String sourceFile;
    private String destinationFile;
    private String volume;
    private boolean replaceExisting = false;

    public void execute() throws BuildException {
        if (serverUrl == null || authId == null || sourceFile == null || destinationFile == null || volume == null) {
            throw new BuildException(getUsage());
        }
        try {
            Uploader uploader = new Uploader(serverUrl, authId, volume);
            uploader.setReplaceExisting(replaceExisting);
            uploader.uploadFile(sourceFile, destinationFile);

            ArrayOfPermission aop = getArrayOfPermissions();
            if (aop != null) {
                uploader.setPermissions(aop);
                PermissionSetter ps = new PermissionSetter(uploader);
                ps.setFilePermissions(destinationFile, true);
            }
        } catch (MalformedURLException e) {
            throw new BuildException(e);
        } catch (ServiceException e) {
            throw new BuildException(e);
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    public void setVolume(String volume) {
    	this.volume = volume;
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

    public void setReplaceExisting(boolean replaceExisting) {
        this.replaceExisting = replaceExisting;
    }

    private String getUsage(){
        return NL + NL +
                "USAGE ERROR:  IdapiUploader requires AuthId, Volume, ServerUrl, " + NL +
                "              SourceFile, DestinationFile attributes." + NL +
                "Optional attributes are UserPermissions, RolePermissions and ReplaceExisting." + NL +
                "Example:" + NL +
                "\t<IdapiUploader AuthId=\"${AuthId}\" " + NL +
                "\t               Volume=\"${volume}\" " + NL +
                "\t               ServerUrl=\"${ServerUrl}\" " + NL +
                "\t               SourceFile=\"C:/my directory/My Report.rptdesign\" " + NL +
                "\t               DestinationFile=\"/Public/My Report.rptdesign\" " + NL +
                "\t               UserPermissions=\"jdoe:VRE,tsmith:VRE\" " + NL +
                "\t               RolePermissions=\"Finance:VRE\" " + NL +
                "\t               ReplaceExisting=\"false\" />";
    }
}