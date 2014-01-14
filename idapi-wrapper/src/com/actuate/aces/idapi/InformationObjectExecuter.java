/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;
import org.apache.axis.attachments.AttachmentPart;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Iterator;

public class InformationObjectExecuter extends BaseController {

	public InformationObjectExecuter(BaseController controller) {
		super(controller);
	}

	public InformationObjectExecuter(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public InformationObjectExecuter(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public InformationObjectExecuter(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public InputStream getXmlStream(String infoObjectName) {

		ExecuteQueryResponse executeQueryResponse = getExecuteQueryId(infoObjectName);

		if (executeQueryResponse == null)
			return null;

		setConnectionHandle(executeQueryResponse.getConnectionHandle());
		ObjectIdentifier objectIdentifier = new ObjectIdentifier();
		objectIdentifier.setId(executeQueryResponse.getObjectId());
		ViewParameter viewParameter = new ViewParameter();
		viewParameter.setFormat("XMLData");
		ComponentType componentType = new ComponentType();
		componentType.setId("0");

		GetContent getContent = new GetContent();
		getContent.setObject(objectIdentifier);
		getContent.setViewParameter(viewParameter);
		getContent.setComponent(componentType);
		getContent.setDownloadEmbedded(false);

		GetContentResponse getContentResponse;
		try {
			getContentResponse = acxControl.proxy.getContent(getContent);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

		try {
			Iterator iter = acxControl.actuateAPI.getCall().getMessageContext().getResponseMessage().getAttachments();
			while (iter.hasNext()) {
				AttachmentPart attachmentPart = (AttachmentPart) iter.next();
				return attachmentPart.getDataHandler().getInputStream();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Document getDocument(String infoObjectName) throws ParserConfigurationException, IOException, SAXException {
		InputStream inputStream = getXmlStream(infoObjectName);

		if (inputStream == null)
			return null;

		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);

		inputStream.close();
		return document;
	}

	public String getXml(String infoObjectName) throws IOException {
		InputStream inputStream = getXmlStream(infoObjectName);

		if (inputStream == null)
			return null;

		StringBuffer stringBuffer = new StringBuffer();
		byte[] buf = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buf)) > 0) {
			stringBuffer.append(new String(buf, 0, len));
		}

		inputStream.close();
		return stringBuffer.toString();
	}

	private ExecuteQueryResponse getExecuteQueryId(String infoObjectName) {
		ArrayOfColumnDefinition arrayOfColumnDefinition = getAvailableColumns(infoObjectName);
		if (arrayOfColumnDefinition == null)
			return null;

		Query query = new Query();
		query.setAvailableColumnList(arrayOfColumnDefinition);
		query.setSelectColumnList(getSelectColumnList(arrayOfColumnDefinition));
		query.setSuppressDetailRows(false);
		query.setOutputDistinctRowsOnly(false);

		query.setSupportedQueryFeatures(SupportedQueryFeatures.UI_Version_2);

		ExecuteQuery executeQuery = new ExecuteQuery();
		executeQuery.setInputFileName(infoObjectName);
		executeQuery.setJobName("$$$TRANSIENT_JOB$$$");
		executeQuery.setSaveOutputFile(false);
		executeQuery.setProgressiveViewing(false);
		executeQuery.setRunLatestVersion(true);
		executeQuery.setWaitTime(300);
		executeQuery.setQuery(query);

		ExecuteQueryResponse executeQueryResponse;
		try {
			executeQueryResponse = acxControl.proxy.executeQuery(executeQuery);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

		return executeQueryResponse;
	}

	private ArrayOfColumnDefinition getAvailableColumns(String infoObjectName) {
		GetQuery getQuery = new GetQuery();
		getQuery.setQueryFileName(infoObjectName);

		GetQueryResponse getQueryResponse;
		try {
			getQueryResponse = acxControl.proxy.getQuery(getQuery);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

		if (getQueryResponse == null || getQueryResponse.getQuery() == null)
			return null;

		return getQueryResponse.getQuery().getAvailableColumnList();
	}

	private ArrayOfString getSelectColumnList(ArrayOfColumnDefinition arrayOfColumnDefinition) {
		ColumnDefinition[] columnDefinitions = arrayOfColumnDefinition.getColumnDefinition();
		String[] columnList = new String[columnDefinitions.length];
		for (int i = 0; i < columnDefinitions.length; i++) {
			columnList[i] = columnDefinitions[i].getName();
		}

		return new ArrayOfString(columnList);
	}
}
