/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.training;

import com.actuate.aces.idapi.Authenticator;
import com.actuate.aces.idapi.BaseController;
import com.actuate.aces.idapi.actions.VolumeUploadSimple;
import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.aces.idapi.control.AcxControl;
import com.actuate.schemas.*;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

public class ContentLoader {


	public static final String PROP_ACTUATE_HOST = "actuate.host";
	public static final String PROP_ACTUATE_USERNAME = "actuate.username";
	public static final String PROP_ACTUATE_PASSWORD = "actuate.password";
	public static final String PROP_ACTUATE_VOLUME = "actuate.volume";
	public static final String PROP_LOAD_NUM_USERS = "load.numUsers";
	public static final String PROP_LOAD_USER_PREFIX = "load.userPrefix";
	public static final String PROP_LOAD_USER_PASSWORD = "load.userPassword";
	public static final String PROP_LOAD_USER_HOME = "load.userHome";
	public static final String PROP_LOAD_CONTENT_PATH = "load.contentPath";

	private static Properties properties = null;

	public static void main(String args[]) throws IOException, ActuateException, ServiceException {
		if (!loadProperties(args[0])) {
			System.out.println("");
			System.out.println("Exiting Application!");
			System.exit(-1);
		}
		String host = properties.getProperty("actuate.host");
		String username = properties.getProperty("actuate.username");
		String password = properties.getProperty("actuate.password", "");
		String volume = properties.getProperty("actuate.volume");
		Authenticator auth = new Authenticator(host, username, password, volume);
		createUsers(auth.getAcxControl());
		loadContent(auth);
		System.out.println("");
		System.out.println("Finished!");
		System.exit(0);
	}

	private static void loadContent(BaseController controller) throws IOException {
		String baseSourcePath = properties.getProperty("load.contentPath");
		int numOfUsers = Integer.parseInt(properties.getProperty("load.numUsers"));
		String homeFolderRoot = properties.getProperty("load.userHome");

		VolumeUploadSimple volumeUploadSimple = new VolumeUploadSimple(controller);

		File dashboardsFile = new File((new StringBuilder()).append(baseSourcePath).append("Dashboards").toString());
		if (dashboardsFile.exists()) {
			System.out.println("Uploading Dashboards...");

			volumeUploadSimple.setPermissions(null);
			volumeUploadSimple.addPermission(null, "All", "VRW");
			volumeUploadSimple.upload(dashboardsFile.getAbsolutePath(), "/Dashboards");
		}

		File resourcesFile = new File((new StringBuilder()).append(baseSourcePath).append("Resources").toString());
		if (resourcesFile.exists()) {
			System.out.println("Uploading Resources...");

			volumeUploadSimple.setPermissions(null);
			volumeUploadSimple.addPermission(null, "All", "VR");
			volumeUploadSimple.upload(resourcesFile.getAbsolutePath(), "/Resources");
		}

		File homeFile = new File((new StringBuilder()).append(baseSourcePath).append("Home").toString());
		if (homeFile.exists()) {
			System.out.println("Uploading User Home Folder Content...");
			for (int i = 0; i < numOfUsers; i++) {
				String userName = getUserName(i + 1);
				String homeFolder = (new StringBuilder()).append(homeFolderRoot).append(userName).toString();

				volumeUploadSimple.setPermissions(null);
				volumeUploadSimple.addPermission(userName, null, "VSREWDG");

				System.out.println((new StringBuilder()).append("Uploading Content for: ").append(userName).append("...").toString());
				volumeUploadSimple.upload(homeFile.getAbsolutePath(), homeFolder);
			}

		}
	}

	private static void createUsers(AcxControl control) throws RemoteException {
		int numOfUsers = Integer.parseInt(properties.getProperty("load.numUsers"));
		String homeFolderRoot = properties.getProperty("load.userHome");
		String userPassword = properties.getProperty("load.userPassword");
		String userPrefix = properties.getProperty("load.userPrefix");

		AdminOperation adminOperations[] = new AdminOperation[numOfUsers];
		System.out.println((new StringBuilder()).append("Creating ").append(numOfUsers).append(" Users...").toString());

		for (int i = 0; i < numOfUsers; i++) {
			String username = getUserName(i + 1);
			String homeFolder = (new StringBuilder()).append(homeFolderRoot).append(username).toString();

			User user = new User();
			user.setName(username);
			user.setPassword(userPassword);
			user.setHomeFolder(homeFolder);
			user.setViewPreference(UserViewPreference.Default);
			user.setMaxJobPriority(1000L);

			CreateUser createUser = new CreateUser();
			createUser.setUser(user);
			createUser.setIgnoreDup(true);

			adminOperations[i] = new AdminOperation();
			adminOperations[i].setCreateUser(createUser);
		}

		Administrate administrate = new Administrate();
		administrate.setAdminOperation(adminOperations);
		control.proxy.administrate(administrate);

		System.out.println("Setting User Roles...");

		UpdateUserOperation updateUserOperation = new UpdateUserOperation();

		UserCondition userCondition = new UserCondition();
		userCondition.setField(UserField.Name);
		userCondition.setMatch((new StringBuilder()).append(userPrefix).append("*").toString());

		UserSearch userSearch = new UserSearch();
		userSearch.setCondition(userCondition);

		UpdateUserOperationGroup updateUserOperationGroup = new UpdateUserOperationGroup();
		updateUserOperationGroup.setUpdateUserOperation(new UpdateUserOperation[]{updateUserOperation});

		UpdateUser updateUser = new UpdateUser();
		updateUser.setSearch(userSearch);
		updateUser.setUpdateUserOperationGroup(updateUserOperationGroup);

		AdminOperation adminOperation = new AdminOperation();
		adminOperation.setUpdateUser(updateUser);
		administrate.setAdminOperation(new AdminOperation[]{adminOperation});
		control.proxy.administrate(administrate);
	}

	private static boolean loadProperties(String filename) throws IOException {
		System.out.println((new StringBuilder()).append("Loading Properties from ").append(filename).append("...").toString());

		properties = new Properties();
		properties.load(new FileReader(filename));

		boolean validProps = true;
		validProps = checkProperty("actuate.host") && validProps;
		validProps = checkProperty("actuate.username") && validProps;
		validProps = checkProperty("actuate.password", false) && validProps;
		validProps = checkProperty("actuate.volume") && validProps;
		validProps = checkProperty("load.numUsers") && validProps;
		validProps = checkProperty("load.userPrefix") && validProps;
		validProps = checkProperty("load.userPassword") && validProps;
		validProps = checkProperty("load.userHome") && validProps;
		validProps = checkProperty("load.contentPath") && validProps;

		if (validProps)
			System.out.println("Finished Loading Properties.");
		else
			System.out.println("ERROR: Not all Properties are valid!");

		return validProps;
	}

	private static boolean checkProperty(String propName) {
		return checkProperty(propName, true);
	}

	private static boolean checkProperty(String propName, boolean isFatal) {
		if (properties.getProperty(propName, "").equals("")) {
			if (isFatal)
				System.out.print("FATAL ERROR: ");

			System.out.println((new StringBuilder()).append("Property ").append(propName).append(" is invalid, blank, or not specified!").toString());

			if (isFatal)
				return false;
		}
		return true;
	}

	private static String getUserName(int num) {
		String userPrefix = properties.getProperty("load.userPrefix");

		if (num < 10)
			return (new StringBuilder()).append(userPrefix).append("00").append(num).toString();
		if (num < 100)
			return (new StringBuilder()).append(userPrefix).append("0").append(num).toString();
		else
			return (new StringBuilder()).append(userPrefix).append(num).toString();
	}


}
