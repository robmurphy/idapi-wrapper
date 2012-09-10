package com.actuate.aces.idapi.system;

import com.actuate.aces.idapi.BaseController;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;

public class PartitionAdmin extends BaseController {
	public PartitionAdmin(BaseController controller) {
		super(controller);
	}

	public PartitionAdmin(String host) throws MalformedURLException, ServiceException {
		super(host);
	}

	public PartitionAdmin(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public boolean addPartition(String name, String path) {
		String templates[] = getTemplates();
		String paths[] = new String[templates.length];
		for (int i = 0; i < paths.length; i++)
			paths[i] = path;

		return addPartition(name, templates, paths);
	}

	public boolean addPartition(String name, String[] templates, String[] paths) {
		//TODO: implement
		return true;
	}

	public boolean deletePartition(String name) {
		//TODO: implement
		return true;
	}

	private String[] getTemplates() {
		//TODO: implement
		return new String[]{"topfuel"};
	}

}
