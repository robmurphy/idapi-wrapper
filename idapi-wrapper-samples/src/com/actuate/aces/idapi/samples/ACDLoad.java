package com.actuate.aces.idapi.samples;

import com.actuate.aces.idapi.Authenticator;
import com.actuate.aces.idapi.BaseController;
import com.actuate.aces.idapi.actions.VolumeUploadSimple;
import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.aces.idapi.control.AcxControl;
import com.actuate.schemas.*;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

public class ACDLoad {

	private static final int NUM_OF_USERS = 100;
	public static final String USER_PREFIX = "User";
	public static final String USER_PASSORD = "birt";
	public static final String HOME_FOLDER_ROOT = "/home/";


	public static void main(String[] args) throws IOException, ActuateException, ServiceException {

		Authenticator auth = new Authenticator("http://192.168.1.2:8000", "Administrator", "Actuate!", "ActuateOne");
		//Authenticator auth = new Authenticator("http://vm-nitrous:8000", "Administrator", "", "ActuateOne");
		createUsers(auth.getAcxControl());
		loadContent(auth, "/Users/pierretessier/Desktop/Roadshow 2012/iServer Content/");

		System.exit(0);
	}

	private static void loadContent(BaseController controller, String baseSourcePath) throws IOException {

		VolumeUploadSimple volumeUploadSimple = new VolumeUploadSimple(controller);

		System.out.println("Uploading Dashboards...");
		File dashboardsFile = new File(baseSourcePath + "Dashboards");
		if (dashboardsFile.exists()) {
			volumeUploadSimple.setPermissions(null);
			volumeUploadSimple.addPermission(null, "All", "VRW");
			volumeUploadSimple.upload(dashboardsFile.getAbsolutePath(), "/Dashboard/Contents");
		}

		System.out.println("Uploading Resources...");
		File resourcesFile = new File(baseSourcePath + "Resources");
		if (resourcesFile.exists()) {
			volumeUploadSimple.setPermissions(null);
			volumeUploadSimple.addPermission(null, "All", "VR");
			volumeUploadSimple.upload(resourcesFile.getAbsolutePath(), "/Resources");
		}

		System.out.println("Uploading User Home Folder Content...");
		File homeFile = new File(baseSourcePath + "Home");
		if (homeFile.exists()) {
			for (int i = 0; i < NUM_OF_USERS; i++) {
				String userName = getUserName(i + 1);
				String homeFolder = HOME_FOLDER_ROOT + userName;
				volumeUploadSimple.setPermissions(null);
				volumeUploadSimple.addPermission(userName, null, "VSREWDG");

				System.out.println("Uploading Content for: " + userName + "...");
				volumeUploadSimple.upload(homeFile.getAbsolutePath(), homeFolder);
			}
		}

	}


	private static void createUsers(AcxControl control) throws RemoteException {
		AdminOperation[] adminOperations = new AdminOperation[NUM_OF_USERS];

		System.out.println("Creating " + NUM_OF_USERS + " Users...");
		for (int i = 0; i < NUM_OF_USERS; i++) {

			String username = getUserName(i + 1);
			String homeFolder = HOME_FOLDER_ROOT + username;

			User user = new User();
			user.setName(username);
			user.setPassword(USER_PASSORD);
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
		userCondition.setMatch(USER_PREFIX + "*");
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

	private static String getUserName(int num) {
		if (num < 10)
			return USER_PREFIX + "00" + num;
		else if (num < 100)
			return USER_PREFIX + "0" + num;
		else
			return USER_PREFIX + num;
	}
}

