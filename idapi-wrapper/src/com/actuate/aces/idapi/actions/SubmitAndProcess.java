package com.actuate.aces.idapi.actions;

import com.actuate.aces.idapi.*;
import com.actuate.aces.idapi.control.ActuateException;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

public class SubmitAndProcess extends BaseController {

	private String jobName = "Submit And Process";
	private int pollSeconds = 5;
	private int pollRetryAttempts = 100;
	private boolean needsReset = false;

	public SubmitAndProcess(BaseController controller) {
		super(controller);
	}

	public SubmitAndProcess(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public SubmitAndProcess(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public SubmitAndProcess(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public void setPollSeconds(int pollSeconds) {
		this.pollSeconds = pollSeconds;
	}

	public void setPollRetryAttempts(int pollRetryAttempts) {
		this.pollRetryAttempts = pollRetryAttempts;
	}

	public boolean download(String executableName, String outputName, HashMap<String, String> params, String outputFormat, String downloadFileName) {

		if (needsReset) {
			reset();
			needsReset = false;
		}

		JobScheduler jobScheduler = new JobScheduler(this);
		String jobId = jobScheduler.scheduleJob(jobName, executableName, outputName, outputFormat, params);
		JobState jobState = new JobState(jobScheduler);
		String state = jobState.pollJobStateCompleteString(jobId, pollSeconds, pollRetryAttempts);
		if (state == null) {
			return false;
		} else if (state.equals("Succeeded")) {
			Downloader downloader = new Downloader(jobState);
			needsReset = true;
			try {
				downloader.downloadToFile(outputName, downloadFileName);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean view(String executableName, String outputName, HashMap<String, String> params, String viewFormat, String viewFileName) {
		if (needsReset) {
			reset();
			needsReset = false;
		}

		JobScheduler jobScheduler = new JobScheduler(this);
		String jobId = jobScheduler.scheduleJob(jobName, executableName, outputName, null, params);
		JobState jobState = new JobState(jobScheduler);
		String state = jobState.pollJobStateCompleteString(jobId, pollSeconds, pollRetryAttempts);
		if (state == null) {
			return false;
		} else if (state.equals("Succeeded")) {
			ReportViewer reportViewer = new ReportViewer(jobState);
			needsReset = true;
			try {
				reportViewer.viewToFile(outputName, viewFormat, viewFileName);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean javaView(String executableName, String outputName, HashMap<String, String> params, String viewFormat, String viewFileName) {
		if (needsReset) {
			reset();
			needsReset = false;
		}

		JobScheduler jobScheduler = new JobScheduler(this);
		String jobId = jobScheduler.scheduleJob(jobName, executableName, outputName, null, params);
		JobState jobState = new JobState(jobScheduler);
		String state = jobState.pollJobStateCompleteString(jobId, pollSeconds, pollRetryAttempts);
		if (state == null) {
			return false;
		} else if (state.equals("Succeeded")) {
			JavaReportViewer javaReportViewer = new JavaReportViewer(jobState);
			needsReset = true;
			try {
				javaReportViewer.viewToFile(outputName, viewFormat, viewFileName);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
}