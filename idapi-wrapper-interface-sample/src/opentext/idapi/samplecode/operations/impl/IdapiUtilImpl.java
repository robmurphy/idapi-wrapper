package opentext.idapi.samplecode.operations.impl;

import com.actuate.aces.idapi.*;
import com.actuate.aces.idapi.actions.VolumeDownload;
import com.actuate.aces.idapi.actions.VolumeMigrateSimple;
import com.actuate.aces.idapi.actions.VolumeUploadSimple;
import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;
import opentext.idapi.samplecode.operations.util.IdapiUtil;

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

/**
 * Created by Kristopher Clark on 10/5/2015.
 */
public class IdapiUtilImpl implements IdapiUtil {
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

    public void uploadFile(String host, String username, String password, String volume, String orig, String dest) throws IOException, ActuateException, ServiceException {
        Uploader uploader = new Uploader(host, username, password, volume);
        uploader.uploadFileAsStream("/Users/pierretessier/Desktop/Travel Journeys.DATA", "/Resources/Data Objects/Travel Journeys.DATA");
    }

    public void createUser(String host,
                           String username,
                           String password,
                           String volume,
                           String newUsername,
                           String newPassword,
                           String newHomeFolder) throws MalformedURLException, ActuateException, ServiceException, RemoteException {
        Authenticator auth = new Authenticator(host, username, password, volume);

        AdminOperation adminOperations[] = new AdminOperation[1];

        User user = new User();
        user.setName("demo");
        user.setPassword("demo");
        user.setHomeFolder("/home/demo");
        user.setViewPreference(UserViewPreference.Default);
        user.setMaxJobPriority(1000L);

        CreateUser createUser = new CreateUser();
        createUser.setUser(user);
        createUser.setIgnoreDup(true);

        adminOperations[0] = new AdminOperation();
        adminOperations[0].setCreateUser(createUser);

        Administrate administrate = new Administrate();
        administrate.setAdminOperation(adminOperations);
        auth.getAcxControl().proxy.administrate(administrate);
    }

    public void scheduleJob(String host,
                     String username,
                     String password,
                     String volume,
                     String executableName,
                     String outputName,
                     HashMap<String, String> parameters,
                     String jobName)
            throws MalformedURLException, ActuateException, ServiceException {
        JobScheduler jobScheduler = new JobScheduler(host, username, password, volume);
        String jobId = jobScheduler.scheduleJob(jobName, executableName, outputName, null, parameters);
        System.out.println("jobId = " + jobId);
    }

    public void getReportParameters(String host,
                                    String username,
                                    String password,
                                    String volume,
                                    String reportName)
            throws MalformedURLException, ActuateException, ServiceException {
        ReportParameterProvider test = new ReportParameterProvider(host, username, password, volume);
        ArrayList<ParameterDefinition> params = test.getParameters(reportName);
        for (ParameterDefinition param : params) {
            System.out.println(param.getName());
        }
    }

    public void getJobsForReport(String host,
                                 String username,
                                 String password,
                                 String volume,
                                 String reportName)
            throws MalformedURLException, ActuateException, ServiceException {
        JobSearcher jobSearcher = new JobSearcher(host, username, password, volume);
        ArrayList<JobProperties> jobPropertiesList = jobSearcher.getJobs(reportName);
        for (JobProperties jobProperties : jobPropertiesList) {
            System.out.println(jobProperties.getActualOutputFileName());
        }
    }

    public void migrateFolder(String sourceHost,
                              String sourceUsername,
                              String sourcePassword,
                              String sourceVolume,
                              String sourceFolder,
                              String targetHost,
                              String targetUsername,
                              String targetPassword,
                              String targetVolume,
                              String targetFolder)
            throws IOException, ActuateException, ServiceException {
        VolumeMigrateSimple volumeMigrate = new VolumeMigrateSimple(sourceHost, sourceUsername, sourcePassword, sourceVolume, targetHost, targetUsername, targetPassword, targetVolume);
        volumeMigrate.migrate(sourceFolder, targetFolder, true, true);
    }

    public void uploadEntireFolder(String host,
                                   String username,
                                   String password,
                                   String volume,
                                   String localFolder,
                                   String targetFolder) throws IOException, ActuateException, ServiceException {
        VolumeUploadSimple volumeUpload = new VolumeUploadSimple(host, username, password, volume);
        volumeUpload.upload(localFolder, targetFolder, true);
    }

    public void downloadEntireFolder(String host,
                                     String username,
                                     String password,
                                     String volume,
                                     String localFolder,
                                     String targetFolder)
            throws IOException, ActuateException, ServiceException {
        VolumeDownload volumeDownload = new VolumeDownload(host, username, password, volume);
        volumeDownload.download(targetFolder, localFolder, true);
    }

    public void listAllFilesAndPermissions(String host,
                                           String username,
                                           String password,
                                           String volume,
                                           String targetFolder)
            throws MalformedURLException, ActuateException, ServiceException {
        FileLister fileLister = new FileLister(host, username, password, volume);

        doListFilesAndPermissions(fileLister, targetFolder, null);
    }

    public void executeReport(String  host,
                              String  username,
                              String  password,
                              String  volume,
                              String  report,
                              String  reportLocation,
                              HashMap<String, String> reportParameters,
                              int     mode,
                              boolean open)
            throws MalformedURLException, ActuateException, ServiceException, UnsupportedEncodingException {
        String viewURL = host + ":8700/iportal/";

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
            String objId = reportExecuter.executeReport(reportLocation + report);
            viewURL += "iv?__report=/$$$Transient/" + objId + ".rptdocument&connectionHandle=" + URLEncoder.encode(reportExecuter.getConnectionHandle(), "UTF-8");

        } else if (mode == 2) {
            // Run and Save
            for (int i = 0; i < 5; i++) {
                reportExecuter.executeReport(reportLocation + report);
                System.out.println("DONE " + (i + 1));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            reportExecuter.executeReport(reportLocation + report);
            viewURL += "iv?__report=/" + report.replace("rptdesign", "rptdocument");

        } else if (mode == 3) {
            // On demand with Parameters, no saving output
            String objId = reportExecuter.executeReport(reportLocation + report, null, reportParameters);
            viewURL += "iv?__report=/$$$Transient/" + objId + ".rptdocument&connectionHandle=" + URLEncoder.encode(reportExecuter.getConnectionHandle(), "UTF-8");

        } else if (mode == 4) {
            // Run and Save	with Parameters
            reportExecuter.executeReport(reportLocation + report, reportLocation + report.replace("rptdesign", "rptdocument"), reportParameters);
            viewURL += "iv?__report=" + reportLocation + report.replace("rptdesign", "rptdocument");
        } else if (mode == 5) {
            // progressive viewing, no saving output
            String objId = reportExecuter.executeReport(reportLocation + report, null, null, ExecuteReportStatus.FirstPage);
            viewURL += "iv?__report=/$$$Transient/" + objId + ".rptdocument&connectionHandle=" + URLEncoder.encode(reportExecuter.getConnectionHandle(), "UTF-8");
        } else if (mode == 6) {
            // progressive viewing saved output
            String objId = reportExecuter.executeReport(reportLocation + report, report.replace("rptdesign", "rptdocument"), null, ExecuteReportStatus.FirstPage);
            viewURL += "iv?__report=" + reportLocation + report.replace("rptdesign", "rptdocument") + "&connectionHandle=" + URLEncoder.encode(reportExecuter.getConnectionHandle(), "UTF-8");
        }

        System.out.println("viewURL = " + viewURL);
        if (open) {
            viewURL += "&userid=" + username;
            viewURL += "&password=" + password;
            viewURL += "&volume=" + volume;
            openURL(viewURL);
        }
    }

    public void downloadFile(String  host,
                             String  username,
                             String  password,
                             String  volume,
                             String  report,
                             String  targetFile,
                             String  localFile)
            throws IOException, ActuateException, ServiceException {
        Downloader downloader = new Downloader(host, username, password, volume);
        downloader.downloadToFile(targetFile, localFile);
    }

    public void exportReportAndSaveToDisk(String  host,
                                          String  username,
                                          String  password,
                                          String  volume,
                                          String  targetReport,
                                          String  targetFormat,
                                          String  targetFile)
            throws IOException, ActuateException, ServiceException {
        ReportViewer reportViewer = new ReportViewer(host, username, password, volume);
        reportViewer.viewToFile(targetReport, targetFormat, targetFile);
    }

    public void executeReportAndSaveToDisk(String  host,
                                           String  username,
                                           String  password,
                                           String  volume,
                                           String  targetReport,
                                           String  targetFormat,
                                           String  targetFile)
            throws IOException, ActuateException, ServiceException {
        ReportExecuter reportExecuter = new ReportExecuter(host, username, password, volume);
        String objId = reportExecuter.executeReport(targetReport);

        ReportViewer reportViewer = new ReportViewer(reportExecuter);
        reportViewer.viewToFile("/$$$Transient/" + objId + ".rptdocument", targetFormat, targetFile);
    }

    public void executeReportAndExtractData(String  host,
                                            String  username,
                                            String  password,
                                            String  volume,
                                            String  targetReport,
                                            String  targetFormat,
                                            String  targetFile,
                                            HashMap<String, String> parameters)
            throws IOException, ActuateException, ServiceException, SOAPException {
        ReportExecuter reportExecuter = new ReportExecuter(host, username, password, volume);
        //TODO: The following line can also accept optional report parameters.  Look at ReportExecuter class for more info
        String objId = reportExecuter.executeReport(targetReport, targetReport.replace("rptdesign", "rptdocument"), parameters, ExecuteReportStatus.Done);

        DataExtractor dataExtractor = new DataExtractor(reportExecuter);
        //TODO: In this sample we always target the 1st exportable data set.  Some reports may differ here, depending on how they are designed
        ResultSetSchema resultSetSchema = dataExtractor.getAllMetaData(targetReport.replace("rptdesign", "rptdocument"))[0];
        String tableName = resultSetSchema.getResultSetName();
        String[] columns = dataExtractor.getColumnsFromSchema(resultSetSchema);

        //TODO: All of these are optional, and will have default values hardcoded in task class
        HashMap<String, String> outputProps = new HashMap<String, String>();
        outputProps.put("BIRTDataExtractionEncoding", "utf-8");
        outputProps.put("BIRTDataExtractionLocaleNeutralFormat", "true");
        outputProps.put("Locale", "en_US");
        outputProps.put("BIRTExportDataType", "true");
        outputProps.put("BIRTDataExtractionSep", ",");

        //TODO: Change the following line to generate output based on your system
        FileOutputStream outputStream = new FileOutputStream(targetFile);
        dataExtractor.extractToStream(targetReport.replace("rptdesign", "rptdocument"), tableName, columns, outputStream, outputProps);
    }
}
