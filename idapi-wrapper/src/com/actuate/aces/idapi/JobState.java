/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;

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

	public JobPropertiesState[] getJobState(String[] jobIds) {
		SelectJobs selectJobs = new SelectJobs();
		selectJobs.setIdList(new ArrayOfString(jobIds));
		selectJobs.setResultDef(new ArrayOfString(new String[]{"state"}));

		SelectJobsResponse response;
		try {
			response = acxControl.proxy.selectJobs(selectJobs);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

		JobProperties[] jobProperties = response.getJobs().getJobProperties();
		if (jobProperties == null)
			return null;

		JobPropertiesState[] jobPropertiesStates = new JobPropertiesState[jobProperties.length];
		for (int i = 0; i < jobProperties.length; i++) {
			jobPropertiesStates[i] = jobProperties[i].getState();
		}

		return jobPropertiesStates;
	}

	public JobPropertiesState getJobState(String jobId) {
		SelectJobs selectJobs = new SelectJobs();
		selectJobs.setId(jobId);
		selectJobs.setResultDef(new ArrayOfString(new String[]{"state"}));

		SelectJobsResponse response;
		try {
			response = acxControl.proxy.selectJobs(selectJobs);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

		if (response.getJobs().getJobProperties() == null)
			return null;

		return response.getJobs().getJobProperties()[0].getState();
	}

	public String getJobStateString(String jobId) {
		JobPropertiesState state = getJobState(jobId);
		if (state == null)
			return null;
		else
			return state.toString();
	}

	public JobPropertiesState pollJobState(String jobId, int pollSeconds, int maxAttempts) {
		JobPropertiesState state = null;

		for (int i = 0; i < maxAttempts; i++) {
			state = getJobState(jobId);
			if (state.equals(JobPropertiesState.Cancelled) || state.equals(JobPropertiesState.Expired) || state.equals(JobPropertiesState.Failed) || state.equals(JobPropertiesState.Succeeded))
				return state;

			try {
				Thread.sleep(pollSeconds * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		}
		return state;
	}

	public String pollJobStateString(String jobId, int pollSeconds, int maxAttempts) {
		JobPropertiesState state = pollJobState(jobId, pollSeconds, maxAttempts);
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
