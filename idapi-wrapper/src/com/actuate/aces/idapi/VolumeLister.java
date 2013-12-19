package com.actuate.aces.idapi;

import com.actuate.aces.idapi.BaseController;
import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.GetSystemVolumeNames;
import com.actuate.schemas.GetSystemVolumeNamesResponse;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class VolumeLister extends BaseController {
    public VolumeLister(BaseController controller) {
        super(controller);
    }

    public VolumeLister(String host) throws MalformedURLException, ServiceException {
        super(host);
    }

    public VolumeLister(String host, String authenticationId) throws MalformedURLException, ServiceException {
        super(host, authenticationId);
    }

    public VolumeLister(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
        super(host, username, password, volume);
    }

    public VolumeLister(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
        super(host, username, password, volume, extendedCredentials);
    }

    public String[] getAllVolumes(){
        return getVolumes(false);
    }

    public String[] getOnlineVolumes(){
        return getVolumes(true);
    }
    public String[] getVolumes(boolean onlineOnly){
        GetSystemVolumeNames getSystemVolumeNames = new GetSystemVolumeNames(onlineOnly);
        GetSystemVolumeNamesResponse response = null;
        try {
            response = acxControl.proxy.getSystemVolumeNames(getSystemVolumeNames);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
        return response.getVolumeNameList().getString();
    }
}
