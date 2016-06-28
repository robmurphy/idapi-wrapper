package opentext.idapi.samplecode.operations.util;

import com.actuate.aces.idapi.control.ActuateException;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by Kristopher Clark on 10/5/2015.
 */
public interface IdapiUtil {
    void executeReportAndExtractData(String  host,
                                     String  username,
                                     String  password,
                                     String  volume,
                                     String  targetReport,
                                     String  targetFormat,
                                     String  targetFile,
                                     HashMap<String, String> parameters)
            throws IOException, ActuateException, ServiceException, SOAPException;

    void executeReportAndSaveToDisk(String  host,
                                    String  username,
                                    String  password,
                                    String  volume,
                                    String  targetReport,
                                    String  targetFormat,
                                    String  targetFile)
            throws IOException, ActuateException, ServiceException;

    void exportReportAndSaveToDisk(String  host,
                                   String  username,
                                   String  password,
                                   String  volume,
                                   String  targetReport,
                                   String  targetFormat,
                                   String  targetFile)
            throws IOException, ActuateException, ServiceException;

    void downloadFile(String  host,
                      String  username,
                      String  password,
                      String  volume,
                      String  report,
                      String  targetFile,
                      String  localFile)
            throws IOException, ActuateException, ServiceException;

    void executeReport(String  host,
                       String  username,
                       String  password,
                       String  volume,
                       String  report,
                       String  reportLocation,
                       HashMap<String, String> reportParameters,
                       int     mode,
                       boolean open)
            throws MalformedURLException, ActuateException, ServiceException, UnsupportedEncodingException;

    void listAllFilesAndPermissions(String host,
                                    String username,
                                    String password,
                                    String volume,
                                    String targetFolder)
            throws MalformedURLException, ActuateException, ServiceException;

    void downloadEntireFolder(String host,
                              String username,
                              String password,
                              String volume,
                              String localFolder,
                              String targetFolder)
            throws IOException, ActuateException, ServiceException;

    void uploadEntireFolder(String host,
                            String username,
                            String password,
                            String volume,
                            String localFolder,
                            String targetFolder)
            throws IOException, ActuateException, ServiceException;

    void migrateFolder(String sourceHost,
                       String sourceUsername,
                       String sourcePassword,
                       String sourceVolume,
                       String sourceFolder,
                       String targetHost,
                       String targetUsername,
                       String targetPassword,
                       String targetVolume,
                       String destFolder)
            throws IOException, ActuateException, ServiceException;

    void getJobsForReport(String host,
                          String username,
                          String password,
                          String volume,
                          String reportName)
            throws MalformedURLException, ActuateException, ServiceException;

    void getReportParameters(String host,
                             String username,
                             String password,
                             String volume,
                             String reportName)
            throws MalformedURLException, ActuateException, ServiceException;

    void scheduleJob(String host,
                     String username,
                     String password,
                     String volume,
                     String executableName,
                     String outputName,
                     HashMap<String, String> parameters,
                     String jobName)
            throws MalformedURLException, ActuateException, ServiceException;

    void createUser(String host,
                    String username,
                    String password,
                    String volume,
                    String newUsername,
                    String newPassword,
                    String newHomeFolder)
            throws MalformedURLException, ActuateException, ServiceException, RemoteException;

    void uploadFile(String host,
                    String username,
                    String password,
                    String volume,
                    String orig,
                    String dest)
            throws IOException, ActuateException, ServiceException;
}
