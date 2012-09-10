package com.actuate.aces.idapi.actions;

import com.actuate.aces.idapi.*;
import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.ArrayOfPermission;
import com.actuate.schemas.File;
import com.actuate.schemas.Permission;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class VolumeMigrateSimple extends BaseController {
	private BaseController targetController;

	public VolumeMigrateSimple(BaseController controller) {
		super(controller);
	}

	public VolumeMigrateSimple(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public VolumeMigrateSimple(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public VolumeMigrateSimple(BaseController sourceController, BaseController targetController) {
		super(sourceController);
		this.targetController = targetController;
	}

	public VolumeMigrateSimple(String sourceHost, String sourceAuthenticationId, String targetHost, String targetAuthenticationId, boolean flag) throws MalformedURLException, ServiceException {
		super(sourceHost, sourceAuthenticationId);
		targetController = new Authenticator(targetHost, targetAuthenticationId);
	}

	public VolumeMigrateSimple(String sourceHost, String sourceUsername, String sourcePassword, String sourceVolume, String targetHost, String targetUsername, String targetPassword, String targetVolume) throws ServiceException, ActuateException, MalformedURLException {
		super(sourceHost, sourceUsername, sourcePassword, sourceVolume);
		targetController = new Authenticator(targetHost, targetUsername, targetPassword, targetVolume);
	}

	public VolumeMigrateSimple(String sourceHost, String sourceUsername, String sourcePassword, String sourceVolume, byte[] sourceExtendedCredentials, String targetHost, String targetUsername, String targetPassword, String targetVolume, byte[] targetExtendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(sourceHost, sourceUsername, sourcePassword, sourceVolume, sourceExtendedCredentials);
		targetController = new Authenticator(targetHost, targetUsername, targetPassword, targetVolume, targetExtendedCredentials);
	}

	public void setTargetController(BaseController targetController) {
		this.targetController = targetController;
	}

	public boolean migrate(String sourcePath, String targetPath) throws IOException {
		return migrate(sourcePath, targetPath, true, false);
	}

	public boolean migrate(String sourcePath, String targetPath, boolean copyPermissions) throws IOException {
		return migrate(sourcePath, targetPath, copyPermissions, false);
	}

	public boolean migrate(String sourcePath, String targetPath, boolean copyPermissions, boolean deleteFirst) throws IOException {
		boolean retVal = true;
		if (deleteFirst)
			retVal = new FileRemover(targetController).delete(targetPath);

		return migratePath(sourcePath, targetPath, copyPermissions) && retVal;
	}

	private boolean migratePath(String sourcePath, String targetPath, boolean copyPermissions) throws IOException {
		boolean retVal = true;

		FileLister fileLister = new FileLister(this);
		ArrayList<File> files = fileLister.getFileList(sourcePath);

		if (sourcePath.equals("/"))
			sourcePath = "";
		if (targetPath.equals("/"))
			targetPath = "";

		for (com.actuate.schemas.File file : files) {

			String sourceFile = sourcePath + "/" + file.getName();
			String targetFile = targetPath + "/" + file.getName();

			if (copyPermissions) {
				PermissionGetter permissionGetter = new PermissionGetter(this);
				ArrayOfPermission permissions = permissionGetter.getPermissions(sourceFile);
				targetController.setPermissions(cleanPermissions(permissions));
			}

			if (file.getFileType().equalsIgnoreCase("directory")) {

				FolderCreator folderCreator = new FolderCreator(targetController);
				retVal = folderCreator.createFolder(file.getName(), targetPath) && retVal;

				retVal = migratePath(sourceFile, targetFile, copyPermissions) && retVal;

			} else {

				Downloader downloader = new Downloader(this);
				InputStream is = downloader.downloadToStream(sourceFile);
				Uploader uploader = new Uploader(targetController);
				retVal = (uploader.upload(is, targetFile) != null) && retVal;

				downloader.reset();
				uploader.reset();
			}
		}

		return retVal;
	}

	private ArrayOfPermission cleanPermissions(ArrayOfPermission permissions) {
		if (permissions == null || permissions.getPermission() == null)
			return null;

		Permission[] permissionsArray = permissions.getPermission();
		for (Permission permission : permissionsArray) {
			permission.setRoleId(null);
			permission.setUserId(null);
		}

		ArrayOfPermission newPermissions = new ArrayOfPermission();
		newPermissions.setPermission(permissionsArray);
		return newPermissions;
	}

}
