/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JobScheduler extends BaseController {

	public JobScheduler(BaseController controller) {
		super(controller);
	}

	public JobScheduler(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public JobScheduler(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public JobScheduler(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public String scheduleJob(String jobName, String executableName, String outputName) throws RemoteException {
		return scheduleJob(jobName, executableName, outputName, null, null);
	}

	public String scheduleJob(String jobName, String executableName, String outputName, String outputFormat) throws RemoteException {
		return scheduleJob(jobName, executableName, outputName, outputFormat, null);
	}

	public String scheduleJob(String jobName, String executableName, String outputName, String outputFormat, Object scheduleTime) throws RemoteException {

		SubmitJob submitJob = new SubmitJob();
		if(jobName.length() >= 100){
			jobName = jobName.substring(0,99);
		}
		submitJob.setJobName(jobName);
		submitJob.setOperation(SubmitJobOperation.RunReport);
		submitJob.setInputFileName(executableName);
		if (priority != null) {
			submitJob.setPriority(priority);
		}
		NewFile newFile = getNewFile( outputName );
		submitJob.setRequestedOutputFile(newFile);

		if (outputFormat != null) {
			ConversionOptions conversionOptions = new ConversionOptions();
			conversionOptions.setFormat(outputFormat);
			submitJob.setConversionOptions(conversionOptions);
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
			submitJob.setParameterValues(new ArrayOfParameterValue(parameterValues));
		}

		if (scheduleTime != null) {
			TimeZone timeZone;
			Date absoluteScheduleTime;
			if (scheduleTime instanceof GregorianCalendar) {
				absoluteScheduleTime = ((GregorianCalendar) scheduleTime).getTime();
				timeZone = ((GregorianCalendar) scheduleTime).getTimeZone();
			} else if (scheduleTime instanceof Date) {
				timeZone = TimeZone.getDefault();
				absoluteScheduleTime = (Date) scheduleTime;
			} else {
				timeZone = TimeZone.getDefault();
				absoluteScheduleTime = new Date(System.currentTimeMillis());
			}

			JobSchedule jobSchedule = new JobSchedule();
			jobSchedule.setTimeZoneName(timeZone.getID());

			AbsoluteDate absoluteDate = new AbsoluteDate();
			absoluteDate.setOnceADay(new SimpleDateFormat("HH:mm:ss").format(absoluteScheduleTime));
//			absoluteDate.setRunOn(new SimpleDateFormat("EEE").format(absoluteScheduleTime));
			absoluteDate.setRunOn(new SimpleDateFormat("yyyy-MM-dd").format(absoluteScheduleTime));

			JobScheduleDetail[] jobScheduleDetail = new JobScheduleDetail[1];
			jobScheduleDetail[0] = new JobScheduleDetail();
			jobScheduleDetail[0].setAbsoluteDate(absoluteDate);
			jobScheduleDetail[0].setScheduleType(JobScheduleDetailScheduleType.AbsoluteDate);

			jobSchedule.setScheduleDetails(new ArrayOfJobScheduleDetail(jobScheduleDetail));
			submitJob.setSchedules(jobSchedule);
		}

		SubmitJobResponse response;
		response = acxControl.proxy.submitJob(submitJob);

		return response.getJobId();
	}
}
