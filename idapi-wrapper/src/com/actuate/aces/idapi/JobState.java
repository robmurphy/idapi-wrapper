package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.ArrayOfString;
import com.actuate.schemas.GetJobDetails;
import com.actuate.schemas.GetJobDetailsResponse;
import com.actuate.schemas.JobPropertiesState;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class JobState extends BaseController {
	public JobState(BaseController controller) {
		super(controller);
	}

	public JobState(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public JobState(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public JobState(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public JobPropertiesState getJobState(String jobId) {
		GetJobDetails getJobDetails = new GetJobDetails();
		getJobDetails.setJobId(jobId);
		getJobDetails.setResultDef(new ArrayOfString(new String[]{"JobAttributes"}));

		GetJobDetailsResponse response;
		try {
			response = acxControl.proxy.getJobDetails(getJobDetails);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

		return response.getJobAttributes().getState();
	}

	public String getJobStateStrign(String jobId) {
		JobPropertiesState state = getJobState(jobId);
		if (state == null)
			return null;
		else
			return state.toString();
	}

	public JobPropertiesState pollJobStateComplete(String jobId, int pollSeconds, int maxAttempts) {
		for (int i = 0; i < maxAttempts; i++) {
			JobPropertiesState state = getJobState(jobId);
			if (state.equals(JobPropertiesState.Cancelled) || state.equals(JobPropertiesState.Expired) || state.equals(JobPropertiesState.Failed) || state.equals(JobPropertiesState.Succeeded))
				return state;

			try {
				Thread.sleep(pollSeconds * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		}

		return null;
	}

	public String pollJobStateCompleteString(String jobId, int pollSeconds, int maxAttempts) {
		JobPropertiesState state = pollJobStateComplete(jobId, pollSeconds, maxAttempts);
		if (state == null)
			return null;
		else
			return state.toString();
	}

}
