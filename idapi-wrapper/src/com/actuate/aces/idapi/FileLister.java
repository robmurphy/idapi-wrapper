package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public class FileLister extends BaseController {
	public FileLister(BaseController controller) {
		super(controller);
	}

	public FileLister(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public FileLister(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public FileLister(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public ArrayList<File> getFileList() {
		return getFileList("/", "*", false);
	}

	public ArrayList<File> getFileList(String baseFolder) {
		return getFileList(baseFolder, "*", FileField.Name, false, false);
	}

	public ArrayList<File> getFileList(String baseFolder, String searchPattern) {
		return getFileList(baseFolder, searchPattern, FileField.Name, false, false);
	}

	public ArrayList<File> getFileList(String baseFolder, String searchPattern, FileField fileField) {
		return getFileList(baseFolder, searchPattern, fileField, false, false);
	}

	public ArrayList<File> getFileList(String baseFolder, String searchPattern, boolean latestVersionOnly) {
		return getFileList(baseFolder, searchPattern, FileField.Name, latestVersionOnly, false);
	}

	public ArrayList<File> getFileList(String baseFolder, String searchPattern, FileField fileField, boolean latestVersionOnly) {
		return getFileList(baseFolder, searchPattern, fileField, latestVersionOnly, false);
	}

	public ArrayList<File> getFileList(String baseFolder, String searchPattern, boolean latestVersionOnly, boolean includeHiddenObjects) {
		return getFileList(baseFolder, searchPattern, FileField.Name, latestVersionOnly, includeHiddenObjects);
	}

	public ArrayList<File> getFileList(String baseFolder, String searchPattern, FileField fileField, boolean latestVersionOnly, boolean includeHiddenObjects) {
		if (searchPattern == null || searchPattern.equals(""))
			searchPattern = "*";

		GetFolderItems getFolderItems = new GetFolderItems();
		getFolderItems.setFolderName(baseFolder);
		getFolderItems.setResultDef(getResultDef());
		getFolderItems.setLatestVersionOnly(latestVersionOnly);

		FileSearch fileSearch = new FileSearch();
		fileSearch.setIncludeHiddenObject(includeHiddenObjects);
		fileSearch.setCondition(new FileCondition(fileField, searchPattern));
		getFolderItems.setSearch(fileSearch);


		GetFolderItemsResponse response;
		try {
			response = acxControl.proxy.getFolderItems(getFolderItems);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

		if (response.getItemList().getFile() == null)
			return new ArrayList<File>();

		ArrayList<File> retVal = new ArrayList<File>();
		retVal.addAll(Arrays.asList(response.getItemList().getFile()));

		return retVal;
	}

	private ArrayOfString getResultDef() {
		return new ArrayOfString(new String[]{"Id", "Name", "FileType", "Owner", "Size", "TimeStamp", "Version", "VersionName", "UserPermissions"});
	}
}
