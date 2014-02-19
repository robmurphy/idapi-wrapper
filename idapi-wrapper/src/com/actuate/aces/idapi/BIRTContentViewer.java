/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;
import org.apache.axis.attachments.AttachmentPart;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import java.io.*;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;

public class BIRTContentViewer extends BaseController {

	public BIRTContentViewer(BaseController controller) {
		super(controller);
	}

	public BIRTContentViewer(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public BIRTContentViewer(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public BIRTContentViewer(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public void viewToStream(String sourceFile, String outputFormat, OutputStream outputStream) throws IOException, SOAPException {
		viewToStream(sourceFile, outputFormat, null, null, outputStream);
	}

	public void viewToStream(String sourceFile, String outputFormat, String pageRange, OutputStream outputStream) throws IOException, SOAPException {
		viewToStream(sourceFile, outputFormat, null, null, outputStream);
	}

	public void viewToStream(String sourceFile, String outputFormat, String pageRange, HashMap<String, String> viewParams, OutputStream outputStream) throws IOException, SOAPException {

		SelectJavaReportPage selectJavaReportPage = new SelectJavaReportPage();

		ObjectIdentifier objectIdentifier = new ObjectIdentifier();
		objectIdentifier.setName(sourceFile);
		objectIdentifier.setType(sourceFile.substring(sourceFile.lastIndexOf(".") + 1));
		selectJavaReportPage.setObject(objectIdentifier);

		PageIdentifier pageIdentifier = new PageIdentifier();
		if (pageRange == null)
			pageIdentifier.setRange("All");
		else
			pageIdentifier.setRange(pageRange);
		pageIdentifier.setViewMode(1);
		selectJavaReportPage.setPage(pageIdentifier);

		HashMap<String, String> baseViewProps = new HashMap<String, String>();
		baseViewProps.put("SVGFlag", "false");
		baseViewProps.put("ContextPath", "/iportal");
		baseViewProps.put("HTMLLayoutPref", "false");
		baseViewProps.put("MasterPage", "true");
		baseViewProps.put("MasterPageMargin", "false");
		NameValuePair[] nameValuePair = getNameValuePairsFromMap(viewParams, baseViewProps);
		ArrayOfNameValuePair arrayOfNameValuePair = new ArrayOfNameValuePair();
		arrayOfNameValuePair.setNameValuePair(nameValuePair);
		selectJavaReportPage.setViewProperties(arrayOfNameValuePair);

		if (outputFormat == null || outputFormat.equals(""))
			outputFormat = "html";
		selectJavaReportPage.setOutputFormat(outputFormat);
		selectJavaReportPage.setDownloadEmbedded(false);

		acxControl.setFileType(sourceFile.substring(sourceFile.lastIndexOf(".") + 1));
		acxControl.proxy.selectJavaReportPage(selectJavaReportPage);
		acxControl.setFileType(null);

		Iterator iter = acxControl.actuateAPI.getCall().getMessageContext().getResponseMessage().getAttachments();
		while (iter.hasNext()) {
			AttachmentPart attachmentPart = (AttachmentPart) iter.next();
			attachmentPart.getDataHandler().writeTo(outputStream);
			attachmentPart.dispose();
		}
	}

	public java.io.File viewToFile(String sourceFile, String outputFormat, String destinationFile) throws IOException {
		return viewToFile(sourceFile, outputFormat, null, null, destinationFile);
	}

	public java.io.File viewToFile(String sourceFile, String outputFormat, String pageRange, String destinationFile) throws IOException {
		return viewToFile(sourceFile, outputFormat, pageRange, null, destinationFile);
	}

	public java.io.File viewToFile(String sourceFile, String outputFormat, String pageRange, HashMap<String, String> viewParams, String destinationFile) throws IOException {
		java.io.File file = new java.io.File(destinationFile);
		FileOutputStream fileOutputStream = new FileOutputStream(file, false);
		try {
			viewToStream(sourceFile, outputFormat, pageRange, viewParams, fileOutputStream);
		} catch (SOAPException e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}

	public InputStream getViewStream(String sourceFile, String outputFormat) {
		return getViewStream(sourceFile, outputFormat, null, null);
	}

	public InputStream getViewStream(String sourceFile, String outputFormat, String pageRange) {
		return getViewStream(sourceFile, outputFormat, pageRange, null);
	}

	public InputStream getViewStream(String sourceFile, String outputFormat, String pageRange, HashMap<String, String> viewParams) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			viewToStream(sourceFile, outputFormat, pageRange, viewParams, baos);
			return new ByteArrayInputStream(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (SOAPException e) {
			e.printStackTrace();
			return null;
		}
	}
}