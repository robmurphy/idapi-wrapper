package com.actuate.aces.idapi.samples;

import com.actuate.aces.idapi.Authenticator;
import com.actuate.aces.idapi.BaseController;
import com.actuate.aces.idapi.actions.VolumeUploadSimple;
import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.aces.idapi.control.AcxControl;
import com.actuate.schemas.*;

import javax.xml.rpc.ServiceException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

public class MappsLoad {

	public static final String PROP_ACTUATE_HOST = "actuate.host";
	public static final String PROP_ACTUATE_USERNAME = "actuate.username";
	public static final String PROP_ACTUATE_PASSWORD = "actuate.password";
	public static final String PROP_ACTUATE_VOLUME = "actuate.volume";
	public static final String PROP_LOAD_NUM_USERS = "load.numUsers";
	public static final String PROP_LOAD_USER_PREFIX = "load.userPrefix";
	public static final String PROP_LOAD_USER_PASSWORD = "load.userPassword";
	public static final String PROP_LOAD_USER_HOME = "load.userHome";
	public static final String PROP_LOAD_CONTENT_PATH = "load.contentPath";

	private static final int xxNUM_OF_USERS = 100;
	public static final String xxUSER_PREFIX = "User";
	public static final String xxUSER_PASSORD = "birt";
	public static final String xxHOME_FOLDER_ROOT = "/home/";

	private static Properties properties = null;


	public static void main(String[] args) throws IOException, ActuateException, ServiceException {

		loadProperties(args[0]);

		String host = properties.getProperty(PROP_ACTUATE_HOST);
		String username = properties.getProperty(PROP_ACTUATE_USERNAME);
		String password = properties.getProperty(PROP_ACTUATE_PASSWORD);
		String volume = properties.getProperty(PROP_ACTUATE_VOLUME);

		Authenticator auth = new Authenticator(host, username, password, volume);
		createUsers(auth.getAcxControl());
		loadContent(auth);

		System.exit(0);
	}

	private static void loadContent(BaseController controller) throws IOException {

		String baseSourcePath = properties.getProperty(PROP_LOAD_CONTENT_PATH);
		int numOfUsers = Integer.parseInt(properties.getProperty(PROP_LOAD_NUM_USERS));
		String homeFolderRoot = properties.getProperty(PROP_LOAD_USER_HOME);

		VolumeUploadSimple volumeUploadSimple = new VolumeUploadSimple(controller);

		java.io.File dashboardsFile = new java.io.File(baseSourcePath + "Dashboards");
		if (dashboardsFile.exists()) {
			System.out.println("Uploading Dashboards...");
			volumeUploadSimple.setPermissions(null);
			volumeUploadSimple.addPermission(null, "All", "VRW");
			volumeUploadSimple.upload(dashboardsFile.getAbsolutePath(), "/Dashboard/Contents");
		}

		java.io.File resourcesFile = new java.io.File(baseSourcePath + "Resources");
		if (resourcesFile.exists()) {
			System.out.println("Uploading Resources...");
			volumeUploadSimple.setPermissions(null);
			volumeUploadSimple.addPermission(null, "All", "VR");
			volumeUploadSimple.upload(resourcesFile.getAbsolutePath(), "/Resources");
		}

		java.io.File homeFile = new java.io.File(baseSourcePath + "Home");
		if (homeFile.exists()) {
			System.out.println("Uploading User Home Folder Content...");
			for (int i = 0; i < numOfUsers; i++) {
				String userName = getUserName(i + 1);
				String homeFolder = homeFolderRoot + userName;
				volumeUploadSimple.setPermissions(null);
				volumeUploadSimple.addPermission(userName, null, "VSREWDG");

				System.out.println("Uploading Content for: " + userName + "...");
				volumeUploadSimple.upload(homeFile.getAbsolutePath(), homeFolder);
			}
		}

	}


	private static void createUsers(AcxControl control) throws RemoteException {

		int numOfUsers = Integer.parseInt(properties.getProperty(PROP_LOAD_NUM_USERS));
		String homeFolderRoot = properties.getProperty(PROP_LOAD_USER_HOME);
		String userPassword = properties.getProperty(PROP_LOAD_USER_PASSWORD);
		String userPrefix = properties.getProperty(PROP_LOAD_USER_PREFIX);

		AdminOperation[] adminOperations = new AdminOperation[numOfUsers];

		System.out.println("Creating " + numOfUsers + " Users...");
		for (int i = 0; i < numOfUsers; i++) {

			String username = getUserName(i + 1);
			String homeFolder = homeFolderRoot + username;

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
		ArrayOfString roles = new ArrayOfString();
		roles.setString(new String[]{"Active Portal Administrator"});
		UpdateUserOperation updateUserOperation = new UpdateUserOperation();
		updateUserOperation.setSetRolesByName(roles);
		UserCondition userCondition = new UserCondition();
		userCondition.setField(UserField.Name);
		userCondition.setMatch(userPrefix + "*");
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
		System.out.println("Loading Properties from " + filename + "...");
		properties = new Properties();
		properties.load(new FileReader(filename));

		boolean validProps = true;
		validProps = validProps && checkProperty(PROP_ACTUATE_HOST);
		validProps = validProps && checkProperty(PROP_ACTUATE_USERNAME);
		validProps = validProps && checkProperty(PROP_ACTUATE_PASSWORD);
		validProps = validProps && checkProperty(PROP_ACTUATE_VOLUME);
		validProps = validProps && checkProperty(PROP_LOAD_NUM_USERS);
		validProps = validProps && checkProperty(PROP_LOAD_USER_PREFIX);
		validProps = validProps && checkProperty(PROP_LOAD_USER_PASSWORD);
		validProps = validProps && checkProperty(PROP_LOAD_USER_HOME);
		validProps = validProps && checkProperty(PROP_LOAD_CONTENT_PATH);

		if (validProps)
			System.out.println("Finished Loading Properties!");
		else
			System.out.println("ERROR: Not all Properties are valid!");

		return validProps;

	}

	private static boolean checkProperty(String propName) {
		if (!properties.contains("propName") || properties.getProperty(propName, "").equals("")) {
			System.out.println("Property " + propName + " is invalid or not specified!");
			return false;
		}
		return true;
	}

	private static String getUserName(int num) {
		String userPrefix = properties.getProperty(PROP_LOAD_USER_PREFIX);

		if (num < 10)
			return userPrefix + "00" + num;
		else if (num < 100)
			return userPrefix + "0" + num;
		else
			return userPrefix + num;
	}
}