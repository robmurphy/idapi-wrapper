/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.aces.idapi.util.ByteArrayDataSource;
import com.actuate.schemas.Attachment;
import com.actuate.schemas.NewFile;
import com.actuate.schemas.UploadFile;
import com.actuate.schemas.UploadFileResponse;
import org.apache.axis.attachments.AttachmentPart;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.rpc.ServiceException;
import java.io.*;
import java.net.MalformedURLException;

public class Uploader extends BaseController {

	public Uploader(BaseController controller) {
		super(controller);
	}

	public Uploader(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public Uploader(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public Uploader(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public String uploadFile(File file, String destination) throws IOException {
		return upload(getFileBytes(file), destination);
	}

	public String uploadFile(File file, String destination, boolean replaceExisting) throws IOException {
		return upload(getFileBytes(file), destination, replaceExisting);
	}

	public String uploadFile(String fileName, String destination) throws IOException {
		return upload(getFileBytes(fileName), destination, true);
	}

	public String uploadFile(String fileName, String destination, boolean replaceExisting) throws IOException {
		return upload(getFileBytes(fileName), destination, replaceExisting);
	}

	public String upload(String sourceData, String destination) throws IOException {
		return upload(sourceData.getBytes(), destination, true);
	}

	public String upload(InputStream inputStream, String destination) throws IOException {
		return upload(getStreamBytes(inputStream), destination, true);
	}

	public String upload(byte[] source, String destination) throws IOException {
		return upload(source, destination, true);
	}

	public String upload(byte[] source, String destination, boolean replaceExisting) throws IOException {
		String contentId = destination.substring(destination.lastIndexOf("/"));
		String contentType = "application/octect.stream";

		DataHandler dataHandler = new DataHandler(new ByteArrayDataSource(source, contentType));
		AttachmentPart attachmentPart = new AttachmentPart();
		attachmentPart.setDataHandler(dataHandler);
		attachmentPart.setContentId(contentId);

		acxControl.proxy.addAttachment(attachmentPart);

		NewFile newFile = new NewFile();
		newFile.setName(destination);
		newFile.setReplaceExisting(replaceExisting);
		if (permissions != null)
			newFile.setACL(permissions);

		Attachment attachment = new Attachment();
		attachment.setContentId(contentId);
		attachment.setContentType(contentType);

		UploadFile uploadFile = new UploadFile();
		uploadFile.setNewFile(newFile);
		uploadFile.setContent(attachment);

		UploadFileResponse uploadFileResponse = acxControl.proxy.uploadFile(uploadFile);

		if (uploadFileResponse.getFileId().equals(""))
			return null;
		else
			return uploadFileResponse.getFileId();
	}

	public String uploadFileAsStream(String fileName, String destination) throws IOException {
		return uploadFileAsStream(new File(fileName), destination, true);
	}

	public String uploadFileAsStream(File file, String destination) throws IOException {
		return uploadFileAsStream(file, destination, true);
	}

	public String uploadFileAsStream(File file, String destination, boolean replaceExisting) throws IOException {
		if (!file.exists())
			throw new FileNotFoundException();

		String contentId = destination.substring(destination.lastIndexOf("/"));
		String contentType = "application/octect.stream";

		DataHandler dataHandler = new DataHandler(new FileDataSource(file));
		AttachmentPart attachmentPart = new AttachmentPart();
		attachmentPart.setDataHandler(dataHandler);
		attachmentPart.setContentId(contentId);

		acxControl.proxy.addAttachment(attachmentPart);

		NewFile newFile = new NewFile();
		newFile.setName(destination);
		newFile.setReplaceExisting(replaceExisting);
		if (permissions != null)
			newFile.setACL(permissions);

		Attachment attachment = new Attachment();
		attachment.setContentId(contentId);
		attachment.setContentType(contentType);

		UploadFile uploadFile = new UploadFile();
		uploadFile.setNewFile(newFile);
		uploadFile.setContent(attachment);

		UploadFileResponse uploadFileResponse = acxControl.proxy.uploadFile(uploadFile);

		if (uploadFileResponse.getFileId().equals(""))
			return null;
		else
			return uploadFileResponse.getFileId();
	}


	private byte[] getFileBytes(File file) throws IOException {
		return getStreamBytes(new FileInputStream(file));
	}

	private byte[] getFileBytes(String fileName) throws IOException {
		return getStreamBytes(new FileInputStream(fileName));
	}

	private byte[] getStreamBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = inputStream.read(buf)) > -1) {
			baos.write(buf, 0, len);
		}

		return baos.toByteArray();
	}
}
