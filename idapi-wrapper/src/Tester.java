/*
 * Copyright (c) 2014 Actuate Corporation
 */

import com.actuate.aces.idapi.*;
import com.actuate.aces.idapi.actions.VolumeDownload;
import com.actuate.aces.idapi.actions.VolumeMigrateSimple;
import com.actuate.aces.idapi.actions.VolumeUpload;
import com.actuate.aces.idapi.actions.VolumeUploadSimple;
import com.actuate.aces.idapi.actions.model.VolumeUploadModel;
import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class Tester {

	static String host = "http://localhost:8000";
	static String username = "Administrator";
	static String password = "";
	static String volume = "Default Volume";

	public static void main(String[] args) throws IOException, ActuateException, ServiceException, SOAPException {

		//executeReportAndExtractData();
		//executeReportAndSaveToDisk();
		//exportReportAndSaveToDisk();
		//downloadFile();
		//executeReportTest(2, false);
		listAllFiles();
		//listAllFilesAndPermissions();
		//downloadEntireFolder();
		//uploadEntireFolder();
		//migrateFolder();
		//volumeUpload();
		//getJobsForReport("/Resources/Classic Models.datadesign");
		//getReportParameters("/Ad-Hoc Mashup.rptdesign");
		//scheduleJob();
		//inlineTask();

		System.exit(0);
	}

	private static void inlineTask() throws MalformedURLException, ActuateException, ServiceException {
		String executableName = "/Public/BIRT and BIRT Studio Examples/Customer Order History.rptdesign";
		String outputName = "/My Output.rptdocument";
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("Customer", "Alpha Cognac");

		JobScheduler jobScheduler = new JobScheduler(host, username, password, volume);
		String jobId1 = jobScheduler.scheduleJob("TEST JOB", executableName, outputName, null, parameters);
		String jobId2 = jobScheduler.scheduleJob("Testing 123", executableName, outputName, null, parameters);
		System.out.println("jobIds = " + jobId1 + " -- " + jobId2);

		long sleep = 0;
		sleep = 10000;
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		new BaseController(jobScheduler) {
			public void doInline(String[] jobs) {
				SelectJobs selectJobs = new SelectJobs();
				selectJobs.setIdList(new ArrayOfString(jobs));
				selectJobs.setResultDef(new ArrayOfString(new String[]{"jobName", "state"}));

				SelectJobsResponse response;
				try {
					response = acxControl.proxy.selectJobs(selectJobs);
				} catch (RemoteException e) {
					e.printStackTrace();
					return;
				}

				for (JobProperties props : response.getJobs().getJobProperties()) {
					System.out.println(props.getJobId() + " -- " + props.getJobName() + " -- " + props.getState().getValue());
				}


				//TODO: Implement something here, all BaseController properties are available
			}
		}.doInline(new String[]{jobId1, jobId2});
	}

	private static void scheduleJob() throws MalformedURLException, ActuateException, ServiceException {

		String executableName = "/Public/BIRT and BIRT Studio Examples/Customer Order History.rptdesign";
		String outputName = "/My Output.rptdocument";
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("Customer", "Alpha Cognac");

		JobScheduler jobScheduler = new JobScheduler(host, username, password, volume);
		String jobId = jobScheduler.scheduleJob("TEST JOB", executableName, outputName, null, parameters);
		System.out.println("jobId = " + jobId);
	}

	private static void getReportParameters(String reportName) throws MalformedURLException, ActuateException, ServiceException {

		ReportParameterProvider test = new ReportParameterProvider(host, username, password, volume);
		ArrayList<ParameterDefinition> params = test.getParameters("/Ad-Hoc Mashup.rptdesign;1");
		for (ParameterDefinition param : params) {
			System.out.println(param.getName());
		}
	}


	private static void getJobsForReport(String reportName) throws MalformedURLException, ActuateException, ServiceException {

		JobSearcher jobSearcher = new JobSearcher(host, username, password, volume);
		ArrayList<JobProperties> jobPropertiesList = jobSearcher.getJobs(reportName);
		for (JobProperties jobProperties : jobPropertiesList) {
			System.out.println(jobProperties.getActualOutputFileName());
		}
	}

	private static void volumeUpload() throws IOException, ActuateException, ServiceException {

		try {

			JAXBContext context = JAXBContext.newInstance("com.actuate.aces.idapi.actions.model");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			java.io.File file = new java.io.File("C:\\Work\\Skunkworks\\IDAPI\\idapi-wrapper\\VolumeUploadModel.xml");
			VolumeUploadModel volumeUploadModel = (VolumeUploadModel) unmarshaller.unmarshal(file);

			VolumeUpload volumeUpload = new VolumeUpload(host, username, password, volume);
			volumeUpload.upload("C:/DownloadTest", "/", volumeUploadModel);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private static void migrateFolder() throws IOException, ActuateException, ServiceException {
		String sourceHost = "http://localhost:8000";
		String sourceUsername = "Administrator";
		String sourcePassword = "";
		String sourceVolume = "ActuateOne";

		String targetHost = "http://localhost:8000";
		String targetUsername = "Administrator";
		String targetPassword = "";
		String targetVolume = "Test";

		VolumeMigrateSimple volumeMigrate = new VolumeMigrateSimple(sourceHost, sourceUsername, sourcePassword, sourceVolume, targetHost, targetUsername, targetPassword, targetVolume);
		volumeMigrate.migrate("/", "/", true, true);
	}

	private static void uploadEntireFolder() throws IOException, ActuateException, ServiceException {
		VolumeUploadSimple volumeUpload = new VolumeUploadSimple(host, username, password, volume);
		volumeUpload.upload("C:/DownloadTest", "/", true);
	}

	private static void downloadEntireFolder() throws IOException, ActuateException, ServiceException {
		VolumeDownload volumeDownload = new VolumeDownload(host, username, password, volume);
		volumeDownload.download("/", "C:/DownloadTest", true);
	}

	private static void listAllFiles() throws MalformedURLException, ActuateException, ServiceException {
		FileLister fileLister = new FileLister(host, username, password, volume);
		doListFiles(fileLister, "/", null);
	}

	private static void doListFiles(FileLister fileLister, String folder, String pattern) {
		ArrayList<File> files = fileLister.getFileList(folder, pattern);
		for (File file : files) {
			if (file.getFileType().equalsIgnoreCase("directory")) {
				if (folder.equals("/"))
					doListFiles(fileLister, folder + file.getName(), pattern);
				else
					doListFiles(fileLister, folder + "/" + file.getName(), pattern);
			} else {
				if (folder.equals("/"))
					System.out.println(folder + file.getName());
				else
					System.out.println(folder + "/" + file.getName());
			}
		}
	}

	private static void listAllFilesAndPermissions() throws MalformedURLException, ActuateException, ServiceException {
		FileLister fileLister = new FileLister(host, username, password, volume);
		doListFilesAndPermissions(fileLister, "/", null);
	}

	private static void doListFilesAndPermissions(FileLister fileLister, String folder, String pattern) {
		ArrayList<File> files = fileLister.getFileList(folder, pattern);
		for (File file : files) {
			if (file.getFileType().equalsIgnoreCase("directory")) {
				if (folder.equals("/"))
					doListFilesAndPermissions(fileLister, folder + file.getName(), pattern);
				else
					doListFilesAndPermissions(fileLister, folder + "/" + file.getName(), pattern);
			} else {
				String fullName;
				if (folder.equals("/"))
					fullName = folder + file.getName();
				else
					fullName = folder + "/" + file.getName();

				System.out.println(fullName);
				ArrayOfPermission permissions = new PermissionGetter(fileLister).getPermissions(fullName);
				if (permissions.getPermission() == null) {
					System.out.println("\tNO PERMISSIONS");
				} else {
					for (Permission permission : permissions.getPermission()) {
						String output = "";
						if (permission.getUserName() != null)
							output += "User: " + permission.getUserName();
						else
							output += "Role: " + permission.getRoleName();
						output += " -- " + permission.getAccessRight();
						System.out.println("\t" + output);
					}
				}
			}
		}
	}

	private static void executeReportAndExtractData() throws IOException, ActuateException, ServiceException, SOAPException {

		ReportExecuter reportExecuter = new ReportExecuter(host, username, password, volume);
		//TODO: The following line can also accept optional report parameters.  Look at ReportExecuter class for more info
		String objId = reportExecuter.executeReport("/Public/BIRT and BIRT Studio Examples/Sales by Employee.rptdesign");

		DataExtractor dataExtractor = new DataExtractor(reportExecuter);
		String sourceFile = "/$$$Transient/" + objId + ".rptdocument";
		//TODO: In this sample we always target the 2nd exportable data set.  Some reports may differ here, depending on how they are designed
		ResultSetSchema resultSetSchema = dataExtractor.getAllMetaData(sourceFile)[1];
		String tableName = resultSetSchema.getResultSetName();
		String[] columns = dataExtractor.getColumnsFromSchema(resultSetSchema);

		//TODO: All of these are optional, and will have default values hardcoded in task class
		HashMap<String, String> outputProps = new HashMap<String, String>();
		outputProps.put("BIRTDataExtractionEncoding", "utf-8");
		outputProps.put("BIRTDataExtractionLocaleNeutralFormat", "true");
		outputProps.put("Locale", "en_US");
		outputProps.put("BIRTExportDataType", "true");
		outputProps.put("BIRTDataExtractionSep", "|");

		//TODO: Change the following line to generate output based on your system
		FileOutputStream outputStream = new FileOutputStream("/Users/pierretessier/Desktop/Test.psv");
		dataExtractor.extractToStream(sourceFile, tableName, columns, outputStream, outputProps);
	}

	private static void executeReportAndSaveToDisk() throws IOException, ActuateException, ServiceException {

		ReportExecuter reportExecuter = new ReportExecuter(host, username, password, volume);
		String objId = reportExecuter.executeReport("/Public/BIRT and BIRT Studio Examples/Sales by Employee.rptdesign");

		BIRTContentViewer birtContentViewer = new BIRTContentViewer(reportExecuter);
		birtContentViewer.viewToFile("/$$$Transient/" + objId + ".rptdocument", "PDF", "/Users/pierretessier/Desktop/TestReport.pdf");
	}

	private static void exportReportAndSaveToDisk() throws IOException, ActuateException, ServiceException {
		BIRTContentViewer birtContentViewer = new BIRTContentViewer(host, username, password, volume);
		birtContentViewer.viewToFile("/home/Administrator/Customer Dashboard.rptdocument", "PDF", "/Users/pierretessier/TestReport.pdf");
	}

	private static void executeReportTest(int mode, boolean open) throws MalformedURLException, ActuateException, ServiceException, UnsupportedEncodingException {

		String viewURL = "http://localhost:8700/iportal/";

		ReportExecuter reportExecuter = new ReportExecuter(host, username, password, volume);

		// mode
		// 1 = On demand, no saving output
		// 2 = Run and Save
		// 3 = On demand with Parameters, no saving output
		// 4 = Run and Save with Parameters
		// 5 = Progressive, no saving
		// 6 = Progressive, with saving

		//int mode = 3;
		if (mode == 1) {
			// On demand, no saving output
			String objId = reportExecuter.executeReport("/Public/BIRT and BIRT Studio Examples/Customers List by Country.rptdesign");
			viewURL += "iv?__report=/$$$Transient/" + objId + ".rptdocument&connectionHandle=" + URLEncoder.encode(reportExecuter.getConnectionHandle(), "UTF-8");

		} else if (mode == 2) {
			// Run and Save
			for (int i = 0; i < 5; i++) {
				reportExecuter.executeReport("/Public/BIRT and BIRT Studio Examples/Sales by Region.rptdesign", "/Test Output.rptdocument");
				System.out.println("DONE " + (i + 1));
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			reportExecuter.executeReport("/Public/BIRT and BIRT Studio Examples/Sales by Region.rptdesign", "/Test Output.rptdocument");
			viewURL += "iv?__report=/Test Output.rptdocument";

		} else if (mode == 3) {
			// On demand with Parameters, no saving output
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("Territory", "EMEA");
			String objId = reportExecuter.executeReport("/Public/BIRT and BIRT Studio Examples/Sales by Territory.rptdesign", null, parameters);
			viewURL += "iv?__report=/$$$Transient/" + objId + ".rptdocument&connectionHandle=" + URLEncoder.encode(reportExecuter.getConnectionHandle(), "UTF-8");

		} else if (mode == 4) {
			// Run and Save	with Parameters
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("Territory", "APAC");
			reportExecuter.executeReport("/Public/BIRT and BIRT Studio Examples/Customers List by Country.rptdesign", "/Test Output.rptdocument", parameters);
			viewURL += "iv?__report=/Test Output.rptdocument";
		} else if (mode == 5) {
			// progressive viewing, no saving output
			String objId = reportExecuter.executeReport("Progressive Viewing.rptdesign", null, null, ExecuteReportStatus.FirstPage);
			viewURL += "iv?__report=/$$$Transient/" + objId + ".rptdocument&connectionHandle=" + URLEncoder.encode(reportExecuter.getConnectionHandle(), "UTF-8");
		} else if (mode == 6) {
			// progressive viewing saved output
			String objId = reportExecuter.executeReport("Progressive Viewing.rptdesign", "/Progressive Viewing.rptdocument", null, ExecuteReportStatus.FirstPage);
			viewURL += "iv?__report=/Progressive Viewing.rptdocument&connectionHandle=" + URLEncoder.encode(reportExecuter.getConnectionHandle(), "UTF-8");
		}

		System.out.println("viewURL = " + viewURL);
		if (open) {
			viewURL += "&userid=" + username;
			viewURL += "&password=" + password;
			viewURL += "&volume=" + volume;
			openURL(viewURL);
		}
	}

	private static void downloadFile() throws IOException, ActuateException, ServiceException {
		Downloader downloader = new Downloader(host, username, password, volume);
		downloader.downloadToFile("/Customer Dashboard.pdf", "C:\\Test Report.pdf");
	}

	private static void setPermissionsRecursively() throws MalformedURLException, ActuateException, ServiceException {
		PermissionSetter permissionSetter = new PermissionSetter(host, username, password, volume);
		permissionSetter.addPermission(null, "TestRole", "VR");
		permissionSetter.setRecursivePermissions("/Temp1", true);
	}

	private static void openURL(String url) {
		String os = System.getProperty("os.name");
		Runtime runtime = Runtime.getRuntime();
		try {
			if (os.startsWith("Windows")) {
				// Block for Windows Platform
				String cmd = "rundll32 url.dll,FileProtocolHandler " + url;
				Runtime.getRuntime().exec(cmd);
			} else if (os.startsWith("Mac OS")) {
				//Block for Mac OS
				Class fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
				openURL.invoke(null, url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

