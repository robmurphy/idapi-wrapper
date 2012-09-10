package com.actuate.aces.idapi.system;

import com.actuate.aces.idapi.BaseController;
import com.actuate.schemas.internal.*;
import org.apache.axis.client.Call;
import org.apache.axis.message.RPCElement;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;

public class VolumeAdmin extends BaseController {
	public VolumeAdmin(BaseController controller) {
		super(controller);
	}

	public VolumeAdmin(String host) throws MalformedURLException, ServiceException {
		super(host);
	}

	public VolumeAdmin(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public boolean addVolume(String name, String description, String partition, String meta, String schema) {
		return addVolume(name, description, partition, meta, schema, null, 0, null, 0);
	}

	public boolean addVolume(String name, String description, String partition, String meta, String schema, String rsseIP, int rssePort, String rsseContext, int rsseTimeout) {
		if (!createVolume(name))
			return false;

		if (!setConfigurationAttributes(name, description, partition, rsseIP, rssePort, rsseContext, rsseTimeout))
			return false;

		if (!activateVolumePartition(name))
			return false;

		if (!setVolumeSchema(name, meta, schema))
			return false;

		return true;
	}

	public boolean deleteVolume(String name) {
		RemoveConfigurationObject removeConfigurationObject = new RemoveConfigurationObject();

		ObjectReference domainObjectReference = new ObjectReference();
		domainObjectReference.setType("Volume");
		domainObjectReference.setName(name);

		ObjectReference objectReference = new ObjectReference();
		objectReference.setType("Volume");
		objectReference.setName(name);

		removeConfigurationObject.setDomain(domainObjectReference);
		removeConfigurationObject.setObject(objectReference);

		RemoveConfigurationObjectResponse response = null;
		try {
			response = acxControl.proxyInternal.removeConfigurationObject(removeConfigurationObject);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean putOffline(String name) throws ServiceException {
		return putOffline(name, 0);
	}

	public boolean putOffline(String name, int gracePeriod) throws ServiceException {
		RPCElement element = new RPCElement(getNamespace(), "PutVolumeOffline", new Object[0]);

		element.addParam(new org.apache.axis.message.RPCParam("VolumeName", name));
		element.addParam(new org.apache.axis.message.RPCParam("GracePeriod", Integer.toString(gracePeriod)));

		Call call = acxControl.createCallInternal();
		Object response = null;
		try {
			response = call.invoke(element);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean takeOnline(String name) throws ServiceException {
		RPCElement element = new RPCElement(getNamespace(), "TakeVolumeOnline", new Object[0]);

		element.addParam(new org.apache.axis.message.RPCParam("VolumeName", name));

		Call call = acxControl.createCallInternal();
		Object response = null;
		try {
			response = call.invoke(element);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}


	private boolean createVolume(String name) {
		AddConfigurationObject addConfigurationObject = new AddConfigurationObject();

		ObjectReference parentObjectReference = new ObjectReference();
		parentObjectReference.setType("Volumes");
		addConfigurationObject.setParentObject(parentObjectReference);

		ObjectReference objectReference = new ObjectReference();
		objectReference.setType("Volume");
		objectReference.setName(name);
		addConfigurationObject.setNewObject(objectReference);

		AddConfigurationObjectResponse response = null;
		try {
			response = acxControl.proxyInternal.addConfigurationObject(addConfigurationObject);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean setConfigurationAttributes(String name, String description, String partition, String rsseIP, int rssePort, String rsseContext, int rsseTimeout) {
		SetConfigurationAttributes setConfigurationAttributes = new SetConfigurationAttributes();

		ObjectReference domainObjectReference = new ObjectReference();
		domainObjectReference.setType("Volume");
		domainObjectReference.setName(name);

		ObjectReference objectReference = new ObjectReference();
		objectReference.setType("Volume");
		objectReference.setName(name);


		AttributeNameValuePair[] attributeNameValuePairs;
		if (rsseIP != null)
			attributeNameValuePairs = new AttributeNameValuePair[4];
		else
			attributeNameValuePairs = new AttributeNameValuePair[9];

		for (int i = 0; i < attributeNameValuePairs.length; ++i)
			attributeNameValuePairs[i] = new AttributeNameValuePair();

		attributeNameValuePairs[0].setName("Description");
		attributeNameValuePairs[0].setValue(description);
		attributeNameValuePairs[1].setName("EmailURLType");
		attributeNameValuePairs[1].setValue("IPortal");
		attributeNameValuePairs[2].setName("PrimaryPartition");
		attributeNameValuePairs[2].setValue(partition);
		attributeNameValuePairs[3].setName("TransLogPartition");
		attributeNameValuePairs[3].setValue(partition);
		if (rsseIP != null) {
			attributeNameValuePairs[4].setName("EnableRSSEService");
			attributeNameValuePairs[4].setValue("true");
			attributeNameValuePairs[5].setName("RSSECacheTimeout");
			attributeNameValuePairs[5].setValue(String.valueOf(rsseTimeout));
			attributeNameValuePairs[6].setName("RSSEIPAddress");
			attributeNameValuePairs[6].setValue(rsseIP);
			attributeNameValuePairs[7].setName("RSSESOAPPort");
			attributeNameValuePairs[7].setValue(String.valueOf(rssePort));
			attributeNameValuePairs[8].setName("RSSEContextString");
			attributeNameValuePairs[8].setValue(rsseContext);
		}

		ArrayOfAttributeNameValuePair attributes = new ArrayOfAttributeNameValuePair(attributeNameValuePairs);

		setConfigurationAttributes.setDomain(domainObjectReference);
		setConfigurationAttributes.setObject(objectReference);
		setConfigurationAttributes.setAttributes(attributes);

		SetConfigurationAttributesResponse response = null;

		try {
			response = acxControl.proxyInternal.setConfigurationAttributes(setConfigurationAttributes);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean activateVolumePartition(String name) {
		AddConfigurationObject addConfigurationObject = new AddConfigurationObject();

		ObjectReference domainObjectReference = new ObjectReference();
		domainObjectReference.setType("Volume");
		domainObjectReference.setName(name);

		ObjectReference parentObjectReference = new ObjectReference();
		parentObjectReference.setType("VolumeFileSystemSettings");

		ObjectReference objectReference = new ObjectReference();
		objectReference.setType("VolumeFileSystemSetting");
		objectReference.setName("!Primary!");

		AttributeNameValuePair[] attributeNameValuePairs = new AttributeNameValuePair[1];
		attributeNameValuePairs[0] = new AttributeNameValuePair();
		attributeNameValuePairs[0].setName("State");
		attributeNameValuePairs[0].setValue("Active");
		ArrayOfAttributeNameValuePair attributes = new ArrayOfAttributeNameValuePair(attributeNameValuePairs);

		addConfigurationObject.setDomain(domainObjectReference);
		addConfigurationObject.setParentObject(parentObjectReference);
		addConfigurationObject.setNewObject(objectReference);
		addConfigurationObject.setAttributes(attributes);

		AddConfigurationObjectResponse response = null;
		try {
			response = acxControl.proxyInternal.addConfigurationObject(addConfigurationObject);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean setVolumeSchema(String name, String meta, String schema) {
		SetVolumeMetadataDatabase setVolumeMetadataDatabase = new SetVolumeMetadataDatabase();

		setVolumeMetadataDatabase.setVolumeName(name);
		setVolumeMetadataDatabase.setMetadataDatabaseName(meta);
		setVolumeMetadataDatabase.setSchemaName(schema);

		SetVolumeMetadataDatabaseResponse response = null;
		try {
			response = acxControl.proxyInternal.setVolumeMetadataDatabase(setVolumeMetadataDatabase);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
