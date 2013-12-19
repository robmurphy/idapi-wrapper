package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;
import org.apache.axis.attachments.AttachmentPart;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import java.io.*;
import java.net.MalformedURLException;
import java.util.Iterator;

public class ReportViewer extends BaseController {

	public ReportViewer(BaseController controller) {
		super(controller);
	}

	public ReportViewer(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public ReportViewer(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public ReportViewer(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public void viewToStream(String sourceFile, String outputFormat, OutputStream outputStream) throws IOException, SOAPException {
		viewToStream(sourceFile, outputFormat, null, outputStream);
	}

	public void viewToStream(String sourceFile, String outputFormat, String pageRange, OutputStream outputStream) throws IOException, SOAPException {

		GetDynamicData getDynamicData = new GetDynamicData();
		SelectPage selectPage = new SelectPage();

		ObjectIdentifier objectIdentifier = new ObjectIdentifier();
		objectIdentifier.setName(sourceFile);
		objectIdentifier.setType(sourceFile.substring(sourceFile.lastIndexOf(".") + 1));
		selectPage.setObject(objectIdentifier);
		getDynamicData.setObject(objectIdentifier);

		ViewParameter viewParameter = new ViewParameter();
		viewParameter.setFormat(outputFormat);
		selectPage.setViewParameter(viewParameter);
		getDynamicData.setViewParameter(viewParameter);

		if (pageRange == null) {
			ComponentType componentType = new ComponentType();
			componentType.setId("0");
			selectPage.setComponent(componentType);
			getDynamicData.setComponent(componentType);
		} else {
			PageIdentifier pageIdentifier = new PageIdentifier();
			pageIdentifier.setRange(pageRange);
			pageIdentifier.setViewMode(1);
			selectPage.setPage(pageIdentifier);
		}

		selectPage.setDownloadEmbedded(false);
		getDynamicData.setDownloadEmbedded(false);

		acxControl.proxy.selectPage(selectPage);
		//acxControl.proxy.getDynamicData(getDynamicData);

		Iterator iter = acxControl.actuateAPI.getCall().getMessageContext().getResponseMessage().getAttachments();
		while (iter.hasNext()) {
			AttachmentPart attachmentPart = (AttachmentPart) iter.next();
			attachmentPart.getDataHandler().writeTo(outputStream);
			attachmentPart.dispose();
		}
	}

	public java.io.File viewToFile(String sourceFile, String outputFormat, String destinationFile) throws IOException {
		return viewToFile(sourceFile, outputFormat, null, destinationFile);
	}

	public java.io.File viewToFile(String sourceFile, String outputFormat, String pageRange, String destinationFile) throws IOException {
		java.io.File file = new java.io.File(destinationFile);
		FileOutputStream fileOutputStream = new FileOutputStream(file, false);
		try {
			viewToStream(sourceFile, outputFormat, pageRange, fileOutputStream);
		} catch (SOAPException e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}

	public InputStream getViewStream(String sourceFile, String outputFormat) {
		return getViewStream(sourceFile, outputFormat, null);
	}

	public InputStream getViewStream(String sourceFile, String outputFormat, String pageRange) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			viewToStream(sourceFile, outputFormat, pageRange, baos);
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