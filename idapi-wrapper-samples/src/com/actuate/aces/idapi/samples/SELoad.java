/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.samples;

import com.actuate.aces.idapi.Authenticator;
import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.aces.idapi.control.AcxControl;
import com.actuate.schemas.*;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.rmi.RemoteException;

public class SELoad {
	public static String[] USERS;
	public static final String USER_PASSORD = "birt";
	public static final String HOME_FOLDER_ROOT = "/home/";


	public static void main(String[] args) throws IOException, ActuateException, ServiceException {

		String[] users = getUsers();
		Authenticator auth = new Authenticator("http://poc.actuate.com:8000", "Administrator", "p0c", "ActuateOne");
		createUsers(auth.getAcxControl(), users);

		System.exit(0);
	}


	private static void createUsers(AcxControl control, String[] users) throws RemoteException {


		AdminOperation[] adminOperations = new AdminOperation[users.length];

		System.out.println("Creating " + users.length + " Users...");
		for (int i = 0; i < users.length; i++) {

			String username = users[i];
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
		roles.setString(new String[]{"Active Portal Administrator", "Sales Engineer"});
		UpdateUserOperation updateUserOperation = new UpdateUserOperation();
		updateUserOperation.setSetRolesByName(roles);

		UpdateUserOperationGroup updateUserOperationGroup = new UpdateUserOperationGroup();
		updateUserOperationGroup.setUpdateUserOperation(new UpdateUserOperation[]{updateUserOperation});

		UpdateUser updateUser = new UpdateUser();
		updateUser.setName("myUser");
		//updateUser.setNameList(new ArrayOfString(getUsers()));
		updateUser.setUpdateUserOperationGroup(updateUserOperationGroup);

		AdminOperation adminOperation = new AdminOperation();
		adminOperation.setUpdateUser(updateUser);

		administrate.setAdminOperation(new AdminOperation[]{adminOperation});
		control.proxy.administrate(administrate);

	}

	private static String[] getUsers() {
		return new String[]{"ananda", "averma", "bgalla", "bmutarelli", "cwong", "dkawaguchi", "drosenbacher", "dfreadhoff", "esanterre", "epartida", "ghess", "jgoodyear", "jleach", "jmangan", "jriglar", "kdowd", "lkucinskaja", "mcoleman", "mblock", "mballou", "mgracan", "ngaudreau", "nhall", "nkarania", "omain", "ptull", "pricher", "ptessier", "ssharma", "smignon", "scaracas", "stilden", "tcarroll", "vdodson", "wwoo", "wdavis", "dmelcher", "doconnell", "jmorris", "mgamble"};
	}
}

