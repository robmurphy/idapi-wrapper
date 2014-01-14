/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class ReportExecuter extends BaseController {

	// Used when looping to check for report execution to end.  checkForStatus
	// Not final, so value can be changed in implementation
	public static long DEFAULT_SLEEP_TIME = 250;

	private ExecuteReportStatus lastStatus = ExecuteReportStatus.Pending;

	public ReportExecuter(BaseController controller) {
		super(controller);
	}

	public ReportExecuter(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public ReportExecuter(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public ReportExecuter(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public ExecuteReportStatus getLastStatus() {
		return lastStatus;
	}

	public String executeReport(String reportName) {
		return executeReport(reportName, null, null, ExecuteReportStatus.Done);
	}

	public String executeReport(String reportName, String outputName) {
		return executeReport(reportName, outputName, null, ExecuteReportStatus.Done);
	}

	public String executeReport(String reportName, String outputName, HashMap<String, String> parameters) {
		return executeReport(reportName, outputName, parameters, ExecuteReportStatus.Done);
	}

	public String executeReport(String reportName, String outputName, HashMap<String, String> parameters, ExecuteReportStatus statusWait) {
		lastStatus = ExecuteReportStatus.Pending;

		ExecuteReport executeReport = new ExecuteReport();
		executeReport.setJobName("Synchronous");
		executeReport.setInputFileName(reportName);

		if (outputName == null || outputName.equalsIgnoreCase("")) {
			executeReport.setSaveOutputFile(false);
		} else {
			NewFile outputFile = new NewFile();
			outputFile.setName(outputName);
			if (permissions != null)
				outputFile.setACL(permissions);
			executeReport.setRequestedOutputFile(outputFile);
			executeReport.setSaveOutputFile(true);
		}

		if (parameters != null && parameters.size() > 0) {
			ParameterValue[] parameterValues = new ParameterValue[parameters.size()];
			int i = 0;
			for (Map.Entry<String, String> entry : parameters.entrySet()) {
				parameterValues[i] = new ParameterValue();
				parameterValues[i].setName(entry.getKey());
				parameterValues[i].setValue(entry.getValue());
				i++;
			}
			executeReport.setParameterValues(new ArrayOfParameterValue(parameterValues));
		}

		if (statusWait == null)
			statusWait = ExecuteReportStatus.Done;
		if (statusWait == ExecuteReportStatus.FirstPage)
			executeReport.setProgressiveViewing(true);

		ExecuteReportResponse response;
		try {
			response = acxControl.proxy.executeReport(executeReport);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

		setConnectionHandle(response.getConnectionHandle());

		lastStatus = response.getStatus();
		return checkForStatus(response.getObjectId(), statusWait);
	}

	public String checkForStatus(String objectId, ExecuteReportStatus statusWait) {
		if (lastStatus == ExecuteReportStatus.Failed)
			return null;

		if (statusWait == ExecuteReportStatus.Done) {
			if (lastStatus == ExecuteReportStatus.Done)
				return objectId;
		} else if (statusWait == ExecuteReportStatus.FirstPage) {
			if (lastStatus == ExecuteReportStatus.FirstPage || lastStatus == ExecuteReportStatus.Done)
				return objectId;
		}

		WaitForExecuteReport waitForExecuteReport = new WaitForExecuteReport();
		waitForExecuteReport.setObjectId(objectId);

		WaitForExecuteReportResponse response;
		try {
			response = acxControl.proxy.waitForExecuteReport(waitForExecuteReport);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

		lastStatus = ExecuteReportStatus.fromValue(response.getStatus());
		try {
			Thread.sleep(DEFAULT_SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return checkForStatus(objectId, statusWait);
	}
}