package com.fiera.actuate;

import com.actuate.aces.idapi.Authenticator;
import com.actuate.aces.idapi.Downloader;
import com.actuate.aces.idapi.FileLister;
import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.File;
import com.actuate.schemas.FileField;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class BatchDownload {

	public static final long SLEEP_TIME = 5 * 60 * 1000; // 5 minutes
	public static final long SCAN_TIME_LAG = 60 * 1000; // 1 minute

	private String host;
	private String username;
	private String password;
	private String volume;
	private String serverFolder;
	private String exportFolder;
	private Authenticator auth = null;
	private long authTime = 0;
	private SimpleDateFormat dateFormat = null;

	public BatchDownload(String[] args) {
		if (args.length < 6) {
			System.out.println("Invalid Arguments Specified");
			System.out.println();
			System.out.println("Syntax:");
			System.out.println("BatchDownload <host> <username> <password> <volume> <iServer Folder> <export folder>");
			System.exit(0);
		}

		host = args[0];
		username = args[1];
		password = args[2];
		volume = args[3];
		serverFolder = args[4];
		exportFolder = args[5];

		if (!serverFolder.endsWith("/"))
			serverFolder += "/";
		if (!exportFolder.endsWith("/"))
			exportFolder += "/";

		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	public void start() throws IOException, ActuateException, ServiceException, InterruptedException {
		long lastScanTime = System.currentTimeMillis() - SCAN_TIME_LAG; // current time less 1 minute

		while (true) {
			checkAuthenticate();

			String datePattern = dateFormat.format(lastScanTime);
			lastScanTime = System.currentTimeMillis() - SCAN_TIME_LAG;

			System.out.print("Scanning for files... ");
			FileLister fileLister = new FileLister(auth);
			ArrayList<File> files = fileLister.getFileList(serverFolder, ">= \"" + datePattern + "\"", FileField.TimeStamp);
			System.out.println(files.size() + " file(s) found!");

			if (files.size() > 0) {
				Downloader downloader = new Downloader(auth);
				for (File file : files) {
					System.out.println("Downloading: " + serverFolder + file.getName());
					downloader.downloadToFile(serverFolder + file.getName(), exportFolder + file.getName());
					downloader.reset();
				}
			}

			Thread.sleep(SLEEP_TIME);
		}
	}

	private void checkAuthenticate() throws MalformedURLException, ActuateException, ServiceException {
		// only authenticate if it has been 23 or more hours since last authentication
		// authentication tokens are only valid for 24 hours
		// if SLEEP_TIME is set to be 1 hour or more, this logic should be modified accordingly.

		if (authTime < System.currentTimeMillis() - (23 * 60 * 60 * 1000)) {
			authTime = System.currentTimeMillis();
			auth = new Authenticator(host, username, password, volume);
		}
	}

	public static void main(String[] args) throws IOException, ServiceException, ActuateException, InterruptedException {
		BatchDownload batchDownload = new BatchDownload(args);
		batchDownload.start();
	}
}
