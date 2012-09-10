package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.GetReportParameters;
import com.actuate.schemas.GetReportParametersResponse;
import com.actuate.schemas.ParameterDefinition;
import com.actuate.schemas.ReportParameterType;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReportParameterProvider extends BaseController {
	public ReportParameterProvider(BaseController controller) {
		super(controller);
	}

	public ReportParameterProvider(String host) throws MalformedURLException, ServiceException {
		super(host);
	}

	public ReportParameterProvider(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public ReportParameterProvider(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public ReportParameterProvider(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public ArrayList<ParameterDefinition> getParameters(String fileName) {
		GetReportParameters getReportParameters = new GetReportParameters();
		getReportParameters.setReportFileName(fileName);
		getReportParameters.setReportParameterType(ReportParameterType.All);

		GetReportParametersResponse response;
		try {
			response = acxControl.proxy.getReportParameters(getReportParameters);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}


		ArrayList<ParameterDefinition> retVal = new ArrayList<ParameterDefinition>();
		retVal.addAll(Arrays.asList(response.getParameterList().getParameterDefinition()));

		return retVal;
	}

}
