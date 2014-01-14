/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JobSearcher extends BaseController {
	public JobSearcher(BaseController controller) {
		super(controller);
	}

	public JobSearcher(String host) throws MalformedURLException, ServiceException {
		super(host);
	}

	public JobSearcher(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public JobSearcher(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public JobSearcher(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public ArrayList<JobProperties> getJobs(String reportName) {
		return getJobs(reportName, null);
	}

	public ArrayList<JobProperties> getJobs(HashMap<JobField, String> filters) {
		return getJobs(null, filters);
	}

	public ArrayList<JobProperties> getJobs(String reportName, HashMap<JobField, String> filters) {

		JobSearch jobSearch = new JobSearch();

		if (reportName != null)
			jobSearch.setInputFileName(reportName);

		if (filters != null) {
			ArrayList<JobCondition> jobConditions = new ArrayList<JobCondition>();
			for (Map.Entry<JobField, String> filter : filters.entrySet()) {
				jobConditions.add(new JobCondition(filter.getKey(), filter.getValue()));
			}
			JobCondition[] aJobCondition = new JobCondition[jobConditions.size()];
			jobConditions.toArray(aJobCondition);
			jobSearch.setConditionArray(new ArrayOfJobCondition(aJobCondition));
		}

		SelectJobs selectJobs = new SelectJobs();
		selectJobs.setSearch(jobSearch);
		selectJobs.setResultDef(getResultDef());

		SelectJobsResponse response;
		try {
			response = acxControl.proxy.selectJobs(selectJobs);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

		ArrayList<JobProperties> retVal = new ArrayList<JobProperties>();
		retVal.addAll(Arrays.asList(response.getJobs().getJobProperties()));

		return retVal;
	}

	private ArrayOfString getResultDef() {
		return new ArrayOfString(new String[]{"JobId", "JobName", "JobType", "State", "ActualOutputFileName", "ActualOutputFileId", "InputFileName", "Owner", "CompletionTime", "PageCount"});
	}
}
