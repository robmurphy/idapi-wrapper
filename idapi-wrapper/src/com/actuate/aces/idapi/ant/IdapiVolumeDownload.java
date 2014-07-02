/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.ant;

import com.actuate.aces.idapi.actions.VolumeDownload;
import org.apache.tools.ant.BuildException;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;

public class IdapiVolumeDownload extends IdapiAntTask {
    private String serverUrl;
    private String authId;
    private String sourcePath;
    private String targetPath;
    private boolean deleteFirst = false;

    public void execute() throws BuildException {
        if (serverUrl == null || authId == null || sourcePath == null || targetPath == null) {
            throw new BuildException(getUsage());
        }
        try {
            VolumeDownload download = new VolumeDownload(serverUrl, authId);
            download.download(sourcePath, targetPath, deleteFirst);
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

    public void setDeleteFirst(boolean deleteFirst) {
        this.deleteFirst = deleteFirst;
    }

    private String getUsage(){
        return NL + NL +
                "USAGE ERROR:  IdapiVolumeDownload requires AuthId, ServerUrl, " + NL +
                "              SourcePath, TargetPath attributes." + NL +
                "Optional attribute is DeleteFirst." + NL +
                "Example:" + NL +
                "\t<IdapiVolumeDownload AuthId=\"${AuthId}\" " + NL +
                "\t                     ServerUrl=\"${ServerUrl}\" " + NL +
                "\t                     SourcePath=\"/Public\" " + NL +
                "\t                     TargetPath=\"C:/MyEnycDownloadDir\" " + NL +
                "\t                     DeleteFirst=\"true\" />";
    }
}