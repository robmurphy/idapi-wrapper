package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class VolumeUpdater extends BaseController {
	public VolumeUpdater(BaseController controller) {
		super(controller);
	}

	public VolumeUpdater(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public VolumeUpdater(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public VolumeUpdater(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public boolean updateResourcePath(String name) {
		Administrate administrate = new Administrate();
		AdminOperation[] adminOperation = new AdminOperation[1];
		adminOperation[0] = new AdminOperation();
		UpdateVolumeProperties updateVolumeProperties = new UpdateVolumeProperties();
		UpdateVolumePropertiesOperationGroup updateVolumePropertiesOperationGroup = new UpdateVolumePropertiesOperationGroup();
		UpdateVolumePropertiesOperation[] updateVolumePropertiesOperation = new UpdateVolumePropertiesOperation[1];
		updateVolumePropertiesOperation[0] = new UpdateVolumePropertiesOperation();
		Volume volume = new Volume();

		volume.setResourcePath(name);
		updateVolumePropertiesOperation[0].setSetAttributes(volume);
		updateVolumePropertiesOperationGroup.setUpdateVolumePropertiesOperation(updateVolumePropertiesOperation);
		updateVolumeProperties.setUpdateVolumePropertiesOperationGroup(updateVolumePropertiesOperationGroup);
		adminOperation[0].setUpdateVolumeProperties(updateVolumeProperties);
		administrate.setAdminOperation(adminOperation);

		try {
			acxControl.proxy.administrate(administrate);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
