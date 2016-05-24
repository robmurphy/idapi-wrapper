/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.Downloader;
import org.apache.tools.ant.BuildException;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;

public class IdapiDownloadFile extends IdapiAntTask {

    private String serverUrl;
    private String authId;
    private String sourceFile;
    private String destinationFile;
    private String volume;

    public void execute() throws BuildException {
        if (serverUrl == null || authId == null || sourceFile == null || destinationFile == null || volume == null) {
            throw new BuildException(getUsage());
        }
        Downloader downloader;
        try {
            downloader = new Downloader(serverUrl, authId, volume);
            downloader.downloadToFile(sourceFile, destinationFile);
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

    private String getUsage(){
        return NL + NL +
                "USAGE ERROR:  IdapiDownloadFile required Volume, AuthId, ServerUrl, " + NL +
                "              SourceFile, DestinationFile attributes." + NL +
                "Example:" + NL +
                "\t<IdapiDownloadFile AuthId=\"${AuthId}\" " + NL +
                "\t                   Volume=\"${vol}\" " + NL +
                "\t                   ServerUrl=\"${ServerUrl}\" " + NL +
                "\t                   SourceFile=\"/Home/user/My Report.rptdesign\" " + NL +
                "\t                   DestinationFile=\"C:/MyFolder/My Report.rptdesign\" />";
    }
}